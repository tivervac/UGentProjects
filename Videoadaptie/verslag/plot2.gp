set term epslatex
set output "2.eps"
# Plaats van de legende
set key bottom right
set grid
#set yrange [0.5:1.5]
set xlabel "Bitrate [kbps]"
set ylabel "PSNR-Y [dB]"
#set logscale x
plot "2singlelayer.dat" using 1:2 pt 6 ps 1 lt -1 linecolor rgb "red" title "Single-layer codering",\
     "2withIP.dat" using 1:2 pt 6 ps 1 lt -1 linecolor rgb "blue" title "SVC met inter-layer predictie",\
     "2withoutIP.dat" using 1:2 pt 6 ps 1 lt -1 linecolor rgb "green" title "SVC zonder inter-layer predictie"	
