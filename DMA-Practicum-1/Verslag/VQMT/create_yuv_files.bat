@echo off
setlocal enabledelayedexpansion
set OPGAVEN=(2A 2B 2C 3A 3B1 3B2 3B3 3B4 3C 3D)
set /a index=0
for %%o in %OPGAVEN% do (
	for %%d in (simple complex) do (
		set t0=!time: =0!
		Decoder_with_error_concealment.exe common_natural_40.enc B:\Downloads\VQMT\output\beowulf_%%o_%%d.yuv error_pattern_%%d_beowulf.txt %index%
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
		@echo %%o %%d !runtime!0 >> common.log
		
		set t0=!time: =0!
		Decoder_with_error_concealment.exe common_synthetic_40.enc B:\Downloads\VQMT\output\elephants_dream_%%o_%%d.yuv error_pattern_%%d_elephants_dream.txt %index%
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
		@echo %%o %%d !runtime!0 >> synthetic.log
		
		set t0=!time: =0!
		Decoder_with_error_concealment.exe group_40.enc B:\Downloads\VQMT\output\theseekerdarkrising_%%o_%%d.yuv error_pattern_%%d_theseekerdarkrising.txt %index%
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
		@echo %%o %%d !runtime!0 >> group.log
	)
	set /a index=%index%+1
)