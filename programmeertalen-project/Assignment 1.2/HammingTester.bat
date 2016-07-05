@echo off
setlocal enabledelayedexpansion
for /l %%i in (10000, 1000, 20000) do (
    for /l %%j in (5, 1, 15) do (
        set t0=!time: =0!
        EagerHamming.exe %%i %%j
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
        echo %%j !input! !runtime!0 >> EagerHamming.dat

        set t0=!time: =0!
        LazyHamming.exe %%i %%j
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
        echo %%j !input! !runtime!0 >> LazyHamming.dat
    )
)
