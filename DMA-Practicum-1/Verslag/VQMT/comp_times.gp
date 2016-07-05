set term epslatex color colortex size 17cm,10cm
set output "times_comp.tex"
set datafile separator ' '
# Plaats van de legende
set key top right
set grid
set style data histogram
set style histogram cluster gap 1
set style fill solid border rgb "black"
set xrange [-1:4]
set yrange [0:150]
set xtics ("10" 0, "0" 1, "11" 2, "1" 3)
set xlabel "Conceal method"
set ylabel "Execution time (ms)"
plot "beowulf_times_comp_simple.dat" using 3 lt 1 lc  rgb "orange" title "Beowulf Simple", \
	  "elephant_times_comp_simple.dat" using 3 lt 1 lc  rgb "red" title "Elephants Dream Simple", \
	  "rising_times_comp_simple.dat" using 3 lt 1 lc  rgb "yellow" title "The Seeker Dark Rising Simple", \
	  "beowulf_times_comp_complex.dat" using 3 lt 1 lc rgb "blue" title "Beowulf Complex", \
	  "elephant_times_comp_complex.dat" using 3 lt 1 lc rgb "magenta" title "Elephants Dream Complex", \
	  "rising_times_comp_complex.dat" using 3 lt 1 lc rgb "cyan" title "The Seeker Dark Rising Complex"
set output