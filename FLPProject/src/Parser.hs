module Parser
( Exp(..)
, Params
, parseExpression
, parse
) where

import           Control.Applicative
import           Control.Monad
import           Data.Char

-- The type of parsers
newtype Parser a = Parser (String -> [(a, String)])

-- Apply a parser
apply :: Parser a -> String -> [(a, String)]
apply (Parser f) = f

-- Return parsed value, assuming at least one successful parse
parse :: Parser a -> String -> a
parse m s = one [ x | (x,t) <- apply m s, t == "" ]
  where
  one [] = error "no parse"
  one [x] = x
  one xs | length xs > 1 = error "ambiguous parse"

-- Adding Applicative/Functor Instances for the Parser Monad according to the
-- Functor-Applicative-Monad Proposal
instance Functor Parser where
  fmap = liftM

instance Applicative Parser where
  pure x = Parser (\s -> [(x,s)])
  (<*>) = ap

instance Alternative Parser where
  (<|>) = mplus
  empty = mzero

instance Monad Parser where
  return = pure
  m >>= k = Parser (\s ->
                [ (y, u) |
                (x, t) <- apply m s,
                (y, u) <- apply (k x) t ])

instance MonadPlus Parser where
  mzero = Parser $ const []
  mplus m n = Parser (\s -> apply m s ++ apply n s)

-- Parse one character
char :: Parser Char
char = Parser f
  where
  f [] = []
  f (c:s) = [(c,s)]

-- Parse a character satisfying a predicate (e.g., isDigit)
spot :: (Char -> Bool) -> Parser Char
spot p = do { c <- char; guard (p c); return c }

-- Match a given character
token :: Char -> Parser Char
token c = spot (== c)

-- Match a given string
match :: String -> Parser String
match = mapM token

-- Match zero or more occurrences
star :: Parser a -> Parser [a]
star p = plus p `mplus` return []

-- Match one or more occurrences
plus :: Parser a -> Parser [a]
plus p = do x <- p
            xs <- star p
            return (x:xs)

-- Match a natural number
parseNat :: Parser Int
parseNat = do s <- plus (spot isDigit)
              return (read s)

-- Match a negative number
parseNeg :: Parser Int
parseNeg = do token '-'
              n <- parseNat
              return (-n)

-- Match an integer
parseInt :: Parser Int
parseInt = parseNat `mplus` parseNeg

-- Match a lower case char
parselChar :: Parser Char
parselChar = spot isLower

-- Match an alphanumeric string
parseString :: Parser String
parseString = star (spot isAlphaNum)

-- Match False
parseFalse :: Parser Int
parseFalse = do match "False"
                return 0

-- Match True
parseTrue :: Parser Int
parseTrue = do match "True"
               return 1

-- Match a boolean
parseBoolean :: Parser Int
parseBoolean = parseFalse `mplus` parseTrue

-- Match any string
parseRandom :: Parser String
parseRandom = plus (spot $ not . isControl)

type Params = [Exp]

data Exp = Lit Int
         | Name String
         | Exp :+:  Exp
         | Exp :-:  Exp
         | Exp :*:  Exp
         | Exp :=:  Exp
         | Exp :==: Exp
         | Exp :!=: Exp
         | Exp :>:  Exp
         | Exp :>=: Exp
         | Exp :<:  Exp
         | Exp :<=: Exp
         | Exp :&&: Exp
         | Exp :||: Exp
         | While Exp
         | Elihw
         | If Exp
         | Else
         | Fi
         | Rgb Params
         | Wait Exp
         | Look
         | Feel
         | Walk
         | Moonwalk
         | Hammertime
         | TurnLeft
         | TurnRight
         | Comment
         deriving (Eq,Show)

-- Parse a binary operation
parseBinOp :: String -> Parser (Exp, Exp)
parseBinOp o = do token '('
                  f <- parseExp
                  match $ " " ++ o ++ " "
                  s <- parseExp
                  token ')'
                  return (f, s)

-- Allow for whitespace in front of a statement
parseExpression :: Parser Exp
parseExpression = do _ <- star (spot isSpace)
                     parseExp

-- Actually parse an expression
parseExp :: Parser Exp
parseExp = parseLit `mplus` parseBool `mplus` parseWhile `mplus`
           parseElihw `mplus `parseIf `mplus` parseFi `mplus` parseElse `mplus`
           parseName `mplus` parseAdd `mplus` parseMin `mplus` parseMul `mplus`
           parseAssign `mplus` parseEq `mplus` parseNeq `mplus`
           parseGt `mplus` parseGte `mplus` parseLt `mplus` parseLte `mplus`
           parseAnd `mplus` parseOr `mplus` parseRGB `mplus` parseWait `mplus`
           parseLook `mplus`parseFeel `mplus` parseWalk `mplus`
           parseMwalk `mplus` parseHTime `mplus` parseTLeft `mplus`
           parseTRight `mplus` parseComm `mplus` parseBraces
  where
  parseLit    = do n <- parseInt
                   return (Lit n)
  parseBool   = do n <- parseBoolean;
                   return (Lit n)
  parseName   = do f <- parselChar
                   r <- parseString
                   return $ Name $ f:r
  parseAdd    = do (d, e) <- parseBinOp "+"
                   return (d :+: e)
  parseMin    = do (d, e) <- parseBinOp "-"
                   return (d :-: e)
  parseMul    = do (d, e) <- parseBinOp "*"
                   return (d :*: e)
  parseAssign = do n <- parseName
                   match " = "
                   v <- parseExp
                   return (n :=: v)
  parseEq     = do (d, e) <- parseBinOp "=="
                   return (d :==: e)
  parseNeq    = do (d, e) <- parseBinOp "!="
                   return (d :!=: e)
  parseGt     = do (d, e) <- parseBinOp ">"
                   return (d :>: e)
  parseGte    = do (d, e) <- parseBinOp ">="
                   return (d :>=: e)
  parseLt     = do (d, e) <- parseBinOp "<"
                   return (d :<: e)
  parseLte    = do (d, e) <- parseBinOp "<="
                   return (d :<=: e)
  parseAnd    = do (d, e) <- parseBinOp "&&"
                   return (d :&&: e)
  parseOr     = do (d, e) <- parseBinOp "||"
                   return (d :||: e)
  parseWhile  = do match "WHILE "
                   c <- parseExp
                   return $ While c
  parseElihw  = do match "ELIHW"
                   return Elihw
  parseIf     = do match "IF "
                   c <- parseExp
                   return $ If c
  parseElse   = do match "ELSE"
                   return Else
  parseFi     = do match "FI"
                   return Fi
  parseRGB    = do match "Rgb("
                   l <- parseExp
                   match ", "
                   r <- parseExp
                   match ", "
                   g <- parseExp
                   match ", "
                   b <- parseExp
                   token ')'
                   return (Rgb [l, r, g, b])
  parseWait   = do match "Wait("
                   t <- parseExp
                   token ')'
                   return (Wait t)
  parseLook   = do match "Look"
                   return Look
  parseFeel   = do match "Feel"
                   return Feel
  parseWalk   = do match "Walk"
                   return Walk
  parseMwalk  = do match "Moonwalk"
                   return Moonwalk
  parseHTime  = do match "Hammertime"
                   return Hammertime
  parseTLeft  = do match "TurnLeft"
                   return TurnLeft
  parseTRight = do match "TurnRight"
                   return TurnRight
  parseComm   = do match "-- "
                   parseRandom
                   return Comment
  parseBraces = do token '('
                   e <- parseExp
                   token ')'
                   return e
