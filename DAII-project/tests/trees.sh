#! /bin/bash
seed=1
sizes=( 5000 5500 6000 6500 7000 7500 8000 8500 9000 1000000 1500000 2000000 2500000 3000000 3500000 4000000 4500000 5000000 )
rand=( 0 0.1 0.2 0.3 0.4 0.5 0.6 0.7 0.8 0.9 )
verd=( 1 8 30 )
for i in "${sizes[@]}"
do
	for j in "${verd[@]}"
	do
		echo "Starting ${i}_${j}"
		./mn_random8020 $i $(echo "$i*10" | bc) $j $seed > trees/8020_${i}_${j}
		echo "Finished ${i}_${j}"
	done
	for j in "${rand[@]}"
	do
		echo "Starting ${i}_${j}"
		./mn_random_zip $i $(echo "$i*10" | bc) $j 1 $seed > trees/zip_${i}_${j}
		echo "Finished ${i}_${j}"
	done
done
