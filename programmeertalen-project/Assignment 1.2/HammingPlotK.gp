# Gnuplot script file for plotting data in files
# The output can be found in plot1a.eps
set term postscript eps enhanced
set term epslatex
set output "HammingK.eps"
set datafile separator ' '
# Plaats van de legende
set key top right
set grid
set xrange [5:15]
#set yrange [0.8:2]
set xlabel "Amount of primes"
set ylabel "Processing time (ms)"
set title "N=10.000"
plot "EagerHammingCstN.dat" using 1:2 with linespoints pt 6 ps 0.5 lt -1 linecolor rgb "red" title "Eager", \
     "LazyHammingCstN.dat" using 1:2 with linespoints pt 6 ps 0.5 lt -1 linecolor rgb "blue" title "Lazy"
set output
