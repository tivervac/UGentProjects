-------------------------------------------------
--   __  __     ____     U  ___ u _____
-- U|' \/ '|uU | __")u    \/"_ \/|_ " _|
-- \| |\/| |/ \|  _ \/    | | | |  | |
--  | |  | |   | |_) |.-,_| |_| | /| |\
--  |_|  |_|   |____/  \_)-\___/ u |_|U
-- <<,-,,-.   _|| \\_       \\   _// \\_
--  (./  \.) (__) (__)     (__) (__) (__)
--  April 2016
------------------------------------------------
-- This is a library to control the mBot robot
-- with haskell.
-- This will only work when you connect the
-- robot with the default firmware over 2.4ghz
-- the Bluetooth version is not supported !
--
-- If you find an error, improve the library
-- or just want to ask me questions
-- please contact me at
-- Christophe.Scholliers@UGent.be
------------------------------------------------
-- Compiling this library on mac can be done as
-- follows:
-- ghc mBot.hs -framework IOKit -framework CoreFoundation
-------------------------------------------------

module MBot (openMBot,
             closeMBot,
             sendCommand,
             readUltraSonic,
             readLineFollower,
             setMotor,
             leftMotor,
             rightMotor,
             motorVooruit,
             motorLinks,
             motorRechts,
             motorStop,
             setRGB,
             Line(LEFTB, RIGHTB, BOTHB, BOTHW),
             Command(MBotCommand) ) where

import Control.Monad.Trans
import Control.Concurrent
import Data.Int
import qualified Data.ByteString as BS
import System.HIDAPI as HID
import GHC.Word
import Data.Maybe
import Data.Bits
import Unsafe.Coerce
-- The mBot protocol works by sending commands in
-- in the following format:
----------------------------------------------------
--   header  2   3   4      5      6    7
--   ff  55  len idx action device port slot data
----------------------------------------------------

-- The header is always sent followed by
-- len: the length of the remaining data, without the header.

-- idx: i have no clue what the idx is and in the mbot code
-- it is mostly ignored from what I have seen.

-- action: can be either GET,RUN,RESET, START
-- GET is used to retrieve data from the mbot
-- RUN is used to make the robot take some action
-- it seems that RESET and START are ignored

-- device: All the components attached to the
-- core mbot are called devices.

-- port: the mbot has several ports to connect the devices to

-- data: some command take a number of arguments
-- these arguments are contained in the data section


-- We represent the devices by an algebraic data type.
-- Because somebody decided it was a good idea to make these
-- enumeration of devices  count up till 22 and then decided
-- to jump to 31 we can't use deriving enum ...
data Dev     = VERSION | ULTRASONIC_SENSOR | TEMPERATURE_SENSOR  | LIGHT_SENSOR |
               POTENTIONMETER | JOYSTICK | GYRO | SOUND_SENSOR | RGBLED | SEVSEG  |
               MOTOR | SERVO | ENCODER  | IR | IRREMOTE | PIRMOTION | INFRARED  |
               LINEFOLLOWER  | IRREMOTECODE  | SHUTTER | LIMITSWITCH | BUTTON | DIGITAL |
               ANALOG  | PWM | SERVO_PIN | TONE |BUTTON_INNER | LEDMATRIX | TIMER  deriving(Eq)


data Line = LEFTB | RIGHTB | BOTHB | BOTHW deriving(Show,Eq)

--                         idx  action device port data
data Command = MBotCommand Int  Action Dev    Int [Int]

-- actions, NOTHING not really exits but otherwise the numbers
-- don't match the ones of mBot
data Action = NOTHING | GET | RUN | RESET | START deriving (Enum)

-- constant definition for the header of a command
header = [0xff,0x55]
-- idx doesn't seem to be used for action commands
-- so I just put it on 0
idx          = 0
-- To check that we are receiving the correct
-- data for a reading.
lineIdx      = 81
ultraIdx     = 42
-- ID for the dongle
dongleID     = 1046
deviceID     = 65535
-- ID's for the left and right motor
leftMotor    = 0x9
rightMotor   = 0xa
-- length of an OK message
ackLength    = 4
sensorLength = 10
-- maximum retries
maxRetries   = 15
-- defaults for motor speed
speed        = 60
stops        = 0
-- port of the rbg led
rgbp         = 7
linp         = 2
sonp         = 3

-- Functionality codes
-- These codes are invented by
-- mBot and can't be touched unfortunately.
-- for more info see https://github.com/Makeblock-official/mBot/blob/master/mBot-default-program/mBot-default-program.ino
devEnumTable = [
 (VERSION             , 0),
 (ULTRASONIC_SENSOR   , 1),
 (TEMPERATURE_SENSOR  , 2),
 (LIGHT_SENSOR        , 3),
 (POTENTIONMETER      , 4),
 (JOYSTICK            , 5),
 (GYRO                , 6),
 (SOUND_SENSOR        , 7),
 (RGBLED              , 8),
 (SEVSEG              , 9),
 (MOTOR               , 10),
 (SERVO               , 11),
 (ENCODER             , 12),
 (IR                  , 13),
 (IRREMOTE            , 14),
 (PIRMOTION           , 15),
 (INFRARED            , 16),
 (LINEFOLLOWER        , 17),
 (IRREMOTECODE        , 18),
 (SHUTTER             , 20),
 (LIMITSWITCH         , 21),
 (BUTTON              , 22),
 -- WHYYYY WHYYY WHYYYY
 (DIGITAL             , 30),
 (ANALOG              , 31),
 (PWM                 , 32),
 (SERVO_PIN           , 33),
 (TONE                , 34),
 (BUTTON_INNER        , 35),
 (LEDMATRIX           , 41),
 (TIMER               , 50)]

-- We implement the conversion ourself
instance Enum Dev where
        fromEnum e   = fromJust $ lookup e devEnumTable
        -- this is clearly wrong if you would need it
        -- implement it ;)
        toEnum  num = VERSION

-- Helper function converting a number to a Word8
intToWord8 i = fromIntegral i :: Word8
intToWord8m  = map intToWord8
word8ToInteger i = fromIntegral i :: Integer

-- Write a raw word8 array to the HID
-- the interface expects that the head of the
-- array is also it's length minus 1
writeRaw device array = HID.write device $ BS.pack $ intToWord8 (length array) : array

-- Throw away the length information in the return
-- array
cutlength (x:rest) = flip take rest $ fromIntegral x

-- Unpack and transform to an int
firstInt = fromIntegral . head . BS.unpack

-- Read a fixed amount of data from the
-- connection, with a maximum number of tries.
-- There are a few reasons why this code is so ugly
-- 1) there is no synchronous timeout call in the library hdapi
-- 2) the library does not return the actually read bytes
--    therefore we just need to test and see whether the sent bytes are 0
--    this again give a major problem because the bytes might actually be 0
--    in practice I have not encountered the problem though.
-- 3) reading to fast from the library makes it crash
--    this is really annoying and that's why there is a timeout
--    of this probably depends on the hardware so this timeout
--    might be too small or too big depending on the operating system
-- TODO I think it would be best to adjust the hdapi library
readLength _ _ 0   = return []
readLength d 0 max = return []
readLength d x m = do
                threadDelay 35000
                bs <- HID.read d x
                let n = firstInt bs
                if  0 /=  n  then
                   do
                      rest <- readLength d  (x-n)  (m-1)
                      return . (++rest) . cutlength . BS.unpack $  bs
                else
                   readLength d x (m-1)

-- Too many constants !
-- maybe  I should change this to a form of enum or something
convertToReading r  |  (r!!6) == 128                   =  LEFTB
                    | ((r!!6) == 0) && ((r!!7) == 64)  =  RIGHTB
                    | ((r!!6) == 0) && ((r!!7) == 0)   =  BOTHB
                    | ((r!!6) == 64) && ((r!!7) == 64) =  BOTHW

checkConnection [di] = Just <$> openDeviceInfo di
checkConnection _    = return Nothing

validReading x idx | null x  || (x!!2 /= fromIntegral idx) = Left x
                   | otherwise = Right x

readSensor d command idx = do
         sendCommand d command
         r <- readLength d sensorLength maxRetries
         either (const $ readSensor d command idx) return  $ validReading r idx

clearBuffer d RUN = readLength d ackLength maxRetries
clearBuffer d _   = return []


-- Conversion functions for reading in a float
shiftMap n (x:rest) = shift (word8ToInteger x) (8 * n) .|. shiftMap (n + 1) rest
shiftMap n []       = 0

ultra :: [Word8] -> Float
ultra  = unsafeCoerce . shiftMap 0 . take 4 . drop 4

--------------------------------------------------------------------------------------------
-- Here the interface for the programmers starts
--------------------------------------------------------------------------------------------

-- Open a connection with the mBot
openMBot = withHIDAPI $ do
             HID.init
             d <- HID.open dongleID deviceID Nothing
             return d

-- Close the connection with the mBot
closeMBot d = withHIDAPI $ HID.close d

-- Send a mbot command over the HID device
-- Note that we have to send the length information twice !
-- Once for the hidapi (7+ length args) and once for the mbot (4 + length args)
-- Strangely enough the hidapi for mac doesn't need the length information
-- even more strange is that it also works with this information ...
sendCommand :: Device -> Command -> IO ()
sendCommand device (MBotCommand idx act dev port args) =
    do let package =  intToWord8m ( [7+length args] ++  header ++ [4 + length args]
                                          ++ [idx,fromEnum act,fromEnum dev, port]
                                          ++ args)
       writeRaw device package
       clearBuffer device act
       return ()

-- Actuators
setRGB  index red green blue = MBotCommand idx RUN  RGBLED rgbp  [2,index,red,green,blue]
setMotor port speed sp       = MBotCommand idx RUN  MOTOR  port  [speed,sp]
-- Sensors
getLineFollower              = MBotCommand  lineIdx   GET LINEFOLLOWER      linp  []
getUltrasonicSensor          = MBotCommand  ultraIdx  GET ULTRASONIC_SENSOR sonp  []

readUltraSonic   d = ultra <$> readSensor d getUltrasonicSensor ultraIdx
readLineFollower d = convertToReading <$> readSensor d getLineFollower  lineIdx

-- Example code to show how the motor commands work
motorVooruit d = do  sendCommand d $ setMotor rightMotor speed  stops
                     -- Sending negative speed (for left motor) use complement !
                     sendCommand d $ setMotor leftMotor   (complement speed)  (complement stops)

motorRechts d  = do   sendCommand d $ setMotor leftMotor  (complement stops) (complement stops)
                      sendCommand d $ setMotor rightMotor speed stops

motorLinks d = do    sendCommand d $ setMotor rightMotor  stops  stops
                     -- Sending negative speed (for left motor) use complement !
                     sendCommand d $ setMotor leftMotor  (complement speed)  (complement stops)

motorStop d   = do   sendCommand d $ setMotor rightMotor  stops stops
                     sendCommand d $ setMotor leftMotor   stops stops
