module Commander
( walk
, moonwalk
, stop
, left
, right
, wait
, rgb
, ultraSonic
, lineF
) where

import           Control.Concurrent
import           Data.Bits
import qualified MBot               as M
import           System.HIDAPI

-- ID's for the left and right motor
lm = 0x9
rm = 0xa
-- Defaults for motor speed
rightM = (80, 0)
leftM  = mapT complement rightM
turn = (80, 0)

-- Move the robot forwards
walk :: Device -> IO ()
walk d = motors d leftM rightM

-- Move the robot backwards
moonwalk :: Device -> IO ()
moonwalk d = motors d (mapT complement leftM) (mapT complement rightM)

-- Stop the robot
stop :: Device -> IO ()
stop d = motors d (0, 0) (0, 0)

-- Make the robot turn left
left :: Device -> IO ()
left d = motors d turn (0, 0)

-- Make the robot turn right
right :: Device -> IO ()
right d = motors d (0, 0) (mapT complement turn)

-- Activate the motors with given speeds
motors :: Device -> (Int, Int) -> (Int, Int) -> IO ()
motors d (ls, lu) (rs, ru) = do send d $ M.setMotor rm rs ru
                                send d $ M.setMotor lm ls lu

-- Activate a given led with a given colour
rgb :: Device -> [Int] -> IO ()
rgb d p = send d $ M.setRGB (head p) (p !! 1) (p !! 2) (p !! 3)

-- Read the value from the ultra sonic sensor
ultraSonic :: Device -> IO Float
ultraSonic = M.readUltraSonic

-- Read the value from the line follower sensor
lineF :: Device -> IO M.Line
lineF = M.readLineFollower

-- Sleeps for t milliseconds
wait :: Int -> IO ()
wait t = threadDelay $ t*1000

-- Helper function

-- Send a command to the robot
send :: Device -> M.Command -> IO ()
send = M.sendCommand

-- The map function over a tuple
mapT :: (a -> a) -> (a, a) -> (a, a)
mapT f (l, r) = (f l, f r)
