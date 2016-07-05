import           Control.Monad.State
import qualified Data.Map            as M
import           Interpreter
import           MBot
import           System.Environment

main =  do
  (args:_) <- getArgs
  contents <- readFile args
  d <- openMBot
  let name = reverse (drop 4 $ reverse args)
  writeFile (name ++ ".log") ""
  -- Evaluate the statements and intialize an empty state
  runStateT (evalStats d name $ lines contents) M.empty
  closeMBot d
