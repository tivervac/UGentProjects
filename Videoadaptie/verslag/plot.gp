set term epslatex
set output "1.eps"
# Plaats van de legende
set key bottom right
set grid
#set yrange [0.5:1.5]
set xlabel "Bitrate [kbps]"
set ylabel "PSNR-Y [dB]"
#set logscale x
plot "1singlelayer.dat" using 1:2 with linespoints pt 6 ps 0.5 lt -1 linecolor rgb "red" title "Single-layer codering",\
     "1withIP.dat" using 1:2 with linespoints pt 6 ps 0.5 lt -1 linecolor rgb "blue" title "SVC met inter-layer predictie",\
     "1withoutIP.dat" using 1:2 with linespoints pt 6 ps 0.5 lt -1 linecolor rgb "green" title "SVC zonder inter-layer predictie"	
