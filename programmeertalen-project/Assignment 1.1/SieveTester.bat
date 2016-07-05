@echo off
setlocal enabledelayedexpansion
for /l %%i in (100000, 100000, 1000000) do (
    set t0=!time: =0!
    EagerSieve.exe %%i
    set t=!time: =0!
    set /a h=1!t0:~0,2!-100
    set /a m=1!t0:~3,2!-100
    set /a s=1!t0:~6,2!-100
    set /a c=1!t0:~9,2!-100
    set /a starttime = !h! * 360000 + !m! * 6000 + 100 * !s! + !c!

    set /a h=1!t:~0,2!-100
    set /a m=1!t:~3,2!-100
    set /a s=1!t:~6,2!-100
    set /a c=1!t:~9,2!-100
    set /a endtime = !h! * 360000 + !m! * 6000 + 100 * !s! + !c!

    set /a runtime = !endtime! - !starttime!
    set runtime = !s!.!c!

    set /a input = %%i/1000
    echo !input! !runtime!0 >> EagerSieve.dat

    set t0=!time: =0!
    LazySieve.exe %%i
    set t=!time: =0!
    set /a h=1!t0:~0,2!-100
    set /a m=1!t0:~3,2!-100
    set /a s=1!t0:~6,2!-100
    set /a c=1!t0:~9,2!-100
    set /a starttime = !h! * 360000 + !m! * 6000 + 100 * !s! + !c!

    set /a h=1!t:~0,2!-100
    set /a m=1!t:~3,2!-100
    set /a s=1!t:~6,2!-100
    set /a c=1!t:~9,2!-100
    set /a endtime = !h! * 360000 + !m! * 6000 + 100 * !s! + !c!

    set /a runtime = !endtime! - !starttime!
    set runtime = !s!.!c!

    set /a input = %%i/1000
    echo !input! !runtime!0 >> LazySieve.dat
)
