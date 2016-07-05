module Interpreter
( evalExp
, evalStats
) where

import           Commander           as C
import           Control.Monad.State
import qualified Data.Map            as M
import           MBot                (Line (..))
import           Parser
import           System.Directory    as D
import           System.HIDAPI

-- The State Map: it holds all of our variables
type SMap = M.Map String Int
-- The Program State: concists of a state (the map) and an IO action as value
type PState = StateT SMap IO ()

-- Evaluates an expression
evalExp :: Device -> SMap -> Exp -> IO Int
evalExp d s expr = case expr of
      Feel     -> do f <- C.ultraSonic d; return $ truncate f
      Look     -> do status <- C.lineF d
                     case status of
                       LEFTB  -> return 1
                       RIGHTB -> return 2
                       BOTHB  -> return 3
                       BOTHW  -> return 4
      Lit n    -> return n
      e :*: f  -> evalAriOp  (*)  e f
      e :+: f  -> evalAriOp  (+)  e f
      e :-: f  -> evalAriOp  (-)  e f
      e :==: f -> evalBinOp  (==) e f
      e :!=: f -> evalBinOp  (/=) e f
      e :>: f  -> evalBinOp  (>)  e f
      e :>=: f -> evalBinOp  (>=) e f
      e :<: f  -> evalBinOp  (<)  e f
      e :<=: f -> evalBinOp  (<=) e f
      e :&&: f -> evalBBinOp (&&) e f
      e :||: f -> evalBBinOp (||) e f
      Name n   -> case M.lookup n s of
                    Nothing -> Prelude.error $ "Using uninitialized variable: " ++ n
                    Just x  -> return x
      _        -> Prelude.error "Unknown expression found in evalExp"
      where
      -- Helper functions
            -- Evaluates an arithmetic operation
            evalAriOp :: (Int -> Int -> Int) -> Exp -> Exp -> IO Int
            evalAriOp o e f = o <$> evalExp d s e <*> evalExp d s f
            -- Evaluates a binary operation
            evalBinOp :: (Int -> Int -> Bool) -> Exp -> Exp -> IO Int
            evalBinOp o e f = getInt $ o <$> evalExp d s e <*> evalExp d s f
            -- Evaluates a boolean binary operation
            evalBBinOp :: (Bool -> Bool -> Bool) -> Exp -> Exp -> IO Int
            evalBBinOp o e f = getInt $ o <$> getBool (evalExp d s e) <*> getBool (evalExp d s f)
            -- Transforms an IO Int into an IO Bool
            getBool :: IO Int -> IO Bool
            getBool ion = do n <- ion
                             return (n > 0)
            -- Transforms an IO Bool into an IO Int
            getInt :: IO Bool -> IO Int
            getInt iob = do b <- iob
                            if b
                            then return 1
                            else return 0

-- Evaluates multiple expressions
evalExps :: Device -> SMap -> [Exp] -> IO [Int]
evalExps d m = mapM (evalExp d m)

-- Represents a block of statements
type Block = [String]

-- Evaluates all the statements of the program
evalStats :: Device -> String -> Block -> PState
evalStats _ _ [] = return ()
evalStats d name (s:xs) =
      if null s -- Allow empty lines
      then evalStats d name xs
      else do m <- get
              case parse parseExpression s of
                n :=: v -> do plog name $ "Assignment " ++ show n ++ " " ++ show v
                              do value <- liftIO $ evalExp d m v
                                 setVar (getName n) value
                                 recall
                Rgb p -> do plog name $ "Rgb " ++ show p
                            lift $ do exps <- evalExps d m p
                                      if length exps /= 4
                                      then Prelude.error "RGB has 4 parameters!"
                                      else C.rgb d exps
                            recall
                Wait t -> do plog name $ "Wait " ++ show t
                             lift $ do time <- evalExp d m t; C.wait time
                             recall
                Walk -> do plog name "Walk"; lift $ C.walk d; recall
                Moonwalk -> do plog name "Moonwalk"; lift $ C.moonwalk d; recall
                Hammertime -> do plog name "Hammertime"; lift $ C.stop d; recall
                TurnLeft -> do plog name "Turn left"; lift $ C.left d; recall
                TurnRight -> do plog name "Turn right"; lift $ C.right d; recall
                Comment -> recall
                While bexp -> do plog name $ "While " ++ show bexp
                                 processWhile d name bexp wBlock
                                 -- +1 because wBlock does not contain the Elihw
                                 evalStats d name $ drop (length wBlock +1) xs
                                 where
                                   wBlock = reverse $ findBlock Elihw 0 xs []
                If bexp -> do plog name $ "If " ++ show bexp
                              processIf d name bexp tBlock fBlock
                              evalStats d name $ drop (length tBlock + length fBlock + 2) xs
                              where
                                tBlock = reverse $ findBlock Else 0 xs []
                                fBlock = reverse $ findBlock Fi   0 (drop (length tBlock + 1) xs) []
                e -> Prelude.error $ "Invalid statement: " ++ show e
                where
                      recall = evalStats d name xs

-- Processes a while loop
processWhile :: Device -> String -> Exp -> Block -> PState
processWhile d name e wBlock = do m <- get
                                  r <- lift $ evalExp d m e
                                  when (r > 0) $
                                    do evalStats d name wBlock
                                       processWhile d name e wBlock

-- Proccess an if statement
processIf :: Device -> String -> Exp -> Block -> Block -> PState
processIf d name e tBlock fBlock = do m <- get
                                      r <- lift $ evalExp d m e
                                      if r > 0
                                      then evalStats d name tBlock
                                      else evalStats d name fBlock

-- Represents the level of conditional nesting
type Level = Int
-- Finds a block of a conditional, either a the block of a while loop or the
-- true/false block in an if statement
findBlock :: Exp -> Level -> Block -> Block -> Block
findBlock e _ [] _ = Prelude.error $ "Missing " ++ show e
findBlock e l (s:xs) c
      | l < 0 =  Prelude.error $ "Missing " ++ show e
      | e == r = if l == 0 then c else findBlock e (l-1) xs c++[s]
      | otherwise = case e of
            -- We're looking for a while block
            Elihw -> case r of
              -- Found another while, increase depth
              While _ -> findBlock e (l+1) xs c++[s]
              _ -> findBlock e l xs c++[s]
            -- We're looking for a true block
            Else -> case r of
              If _ -> findBlock e (l+1) xs c++[s]
              _ -> findBlock e l xs c++[s]
            -- We're looking for a false block
            Fi -> case r of
              If _ -> findBlock e (l+1) xs c++[s]
              _ -> findBlock e l xs c++[s]
            expr -> Prelude.error $ "Trying to find block of an unsupported expression: " ++ show expr
      where
        r = parse parseExpression s

-- Logs a string to a given file
plog :: String -> String -> PState
plog name s = liftIO $ appendFile (name ++ ".log") (s ++ "\n")

-- Adds or sets a variable in the State
setVar :: String -> Int -> PState
setVar k v = do
      m <- get
      put $ M.insert k v m
      return ()

-- Returns the name of a Name expression
getName :: Exp -> String
getName (Name n) = n
getName e        = Prelude.error "Trying to get an identifier out of an unsupported expression: " ++ show e
