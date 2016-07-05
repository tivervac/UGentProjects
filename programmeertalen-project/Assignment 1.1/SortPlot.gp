# Gnuplot script file for plotting data in files
# The output can be found in plot1a.eps
set term postscript eps enhanced
set term epslatex
set output "Sort.eps"
set datafile separator ' '
# Plaats van de legende
set key top left
set grid
#set xrange [3:23]
#set yrange [0.8:2]
set xlabel "Input size (in thousands)"
set ylabel "Processing time (ms)"
plot "EagerSort.dat" using 1:2 with linespoints pt 6 ps 0.5 lt -1 linecolor rgb "red" title "Eager", \
     "LazySort.dat" using 1:2 with linespoints pt 6 ps 0.5 lt -1 linecolor rgb "blue" title "Lazy"
set output
