set term epslatex color colortex size 17cm,10cm
set output "psnr.tex"
set datafile separator ' '
# Plaats van de legende
set key top right
set grid
set style data histogram
set style histogram cluster gap 1
set style fill solid border rgb "black"
set xrange [-1:10]
set yrange [0:60]
set xtics ("2A" 0, "2B" 1, "2C"  2, "3A" 3, "3B1" 4, "3B2" 5, "3B3" 6, "3B4" 7 , "3C" 8, "3D" 9)
set xlabel "Exercise"
set ylabel "PSNR (dB)"
plot "beowulf_psnr_simple.dat" using 2 lt 1 lc  rgb "orange" title "Beowulf Simple", \
	  "elephant_psnr_simple.dat" using 2 lt 1 lc  rgb "red" title "Elephants Dream Simple", \
	  "rising_psnr_simple.dat" using 2 lt 1 lc  rgb "yellow" title "The Seeker Dark Rising Simple", \
	  "beowulf_psnr_complex.dat" using 2 lt 1 lc rgb "blue" title "Beowulf Complex", \
	  "elephant_psnr_complex.dat" using 2 lt 1 lc rgb "magenta" title "Elephants Dream Complex", \
	  "rising_psnr_complex.dat" using 2 lt 1 lc rgb "cyan" title "The Seeker Dark Rising Complex"
set output