#include "mpi_genetic_algorithm.h"

#define MUTATION_FACTOR 25
#define MPI_STOP_PRECISION 0.001f
#define MPI_STOP_BARRIER 10
#define STOP_PRECISION 0.1f
#define STOP_BARRIER 100
// SHOULD NEVER BE MORE THAN 0.5 AS WE SEARCH FOR UNIQUE PARENTS
#define FRACTION 0.23
#define SURVIVORS 0.45
#define MUTATION 30

/*
* @arg polygon: the polygon containing organisms for which we want to find a maximized fitness
* @return: the organism with the highest fitness calculated by our genetic algorithm
*/
organism* find_fitter_organism(polygon* polygon) {
	int stop_counter = 0;
    int mpi_stop_counter = 0;
	double prev_fitness = DBL_MIN;
    double mpi_prev_fitness = DBL_MIN;
    double x = polygon->bottom_right->x - polygon->upper_left->x;
    double y = polygon->upper_left->y - polygon->bottom_right->y;
    double mutation_weight = x > y ? y / MUTATION_FACTOR : x / MUTATION_FACTOR;
    double weight;
    int noOfProcesses, rank, i;
    MPI_Status status;
    organism* best;

    s_MPI_Comm_size(MPI_COMM_WORLD, &noOfProcesses);
    s_MPI_Comm_rank(MPI_COMM_WORLD, &rank);
    weight = rank ? random_between_doubles(0, mutation_weight) : mutation_weight;
    {
        int noOfOrganisms =  polygon->population_size / noOfProcesses;
        int buffer_size = (noOfOrganisms * 2 * polygon->organism_size) + noOfOrganisms;
        double sendbuffer[buffer_size];
        double recvbuffer[buffer_size];
        int stop = 0;

        // Calculate fitness the first time, in next iterations this will already be known
        calculate_fitness(polygon->population, 0, polygon->population_size, polygon->organism_size);
        // Define the amount of children
        polygon->children_size = (int)(polygon->population_size * FRACTION);
        while (!stop) {
            while (stop_counter < STOP_BARRIER) {
                // Do crossover on children_size pairs of parents
                crossover(polygon);
                mutate(polygon, weight);
                selection(polygon);

                best = best_fit(polygon);
                // If the difference in fitness remains smaller than STOP_PRECISION
                // for STOP_BARRIER amount of generations, stop the algorithm
                stop_counter = best->fitness - prev_fitness < STOP_PRECISION
                    ? stop_counter + 1
                    : 0;
                prev_fitness = best->fitness;
            }

            create_MPI_buffers(polygon, sendbuffer, noOfOrganisms);
            // Distribute points and fitness'
            if (rank % 2) {
                s_MPI_Send(sendbuffer, buffer_size, MPI_DOUBLE, (rank + 1) % noOfProcesses, 0, MPI_COMM_WORLD);
            } else {
                s_MPI_Recv(recvbuffer, buffer_size, MPI_DOUBLE, (rank - 1) < 0 ? noOfProcesses - 1 : rank - 1, 0, MPI_COMM_WORLD, &status);
            }
            if (rank % 2) {
                s_MPI_Recv(recvbuffer, buffer_size, MPI_DOUBLE, (rank - 1) < 0 ? noOfProcesses - 1 : rank - 1, 0, MPI_COMM_WORLD, &status);
            } else {
                s_MPI_Send(sendbuffer, buffer_size, MPI_DOUBLE, (rank + 1) % noOfProcesses, 0, MPI_COMM_WORLD);
            }
            mpi_merge(polygon, recvbuffer, noOfOrganisms);
            best = best_fit(polygon);

            // No need to sort since crossover will do this in the next iteration
            // Check if we're stuck on the same maximum
            mpi_stop_counter = best->fitness - mpi_prev_fitness < MPI_STOP_PRECISION
                ? mpi_stop_counter + 1
                : 0;
            mpi_prev_fitness = best->fitness;

            // Send if we're ready and receive if we should stop
            if (rank) {
                int buffer[1];

                buffer[0] = mpi_stop_counter >= MPI_STOP_BARRIER;
                s_MPI_Send(buffer, 1, MPI_INT, 0, 0, MPI_COMM_WORLD);
                s_MPI_Recv(buffer, 1, MPI_INT, 0, 0, MPI_COMM_WORLD, &status);

                stop = buffer[0];
            } else {
                int buffer[1];

                // Receive everyone's status
                stop = mpi_stop_counter >= MPI_STOP_BARRIER;
                for (i = 1; i < noOfProcesses; i++) {
                    s_MPI_Recv(buffer, 1, MPI_INT, i, 0, MPI_COMM_WORLD, &status);
                    stop &= buffer[0];
                }

                // Send if we have to stop or not
                buffer[0] = stop;
                for (i = 1; i < noOfProcesses; i++) {
                    s_MPI_Send(buffer, 1, MPI_INT, i, 0, MPI_COMM_WORLD);
                }
            }

            // Reset the counter if needed
            if (mpi_stop_counter >= MPI_STOP_BARRIER) {
                mpi_stop_counter == 0;
            }
            stop_counter = 0;
        }
    }

    // Receive and send the best organisms
    {
        int size = 1 + (polygon->organism_size * 2);
        double buffer[size];
        if (!rank) {
            int i, j;

            for (i = 1; i < noOfProcesses; i++){
                s_MPI_Recv(buffer, size, MPI_DOUBLE, i, 0, MPI_COMM_WORLD, &status);
                // If it's better, overwrite ours
                if (buffer[0] > best->fitness) {
                    best->fitness = buffer[0];
                    for (j = 0; j < polygon->organism_size; j++) {
                        best->points[j]->x = buffer[1 + (j * 2)];
                        best->points[j]->y = buffer[2 + (j * 2)];
                    }
                }
            }
        } else {
            int i;

            buffer[0] = best->fitness;
            for (i = 0; i < polygon->organism_size; i++) {
                best->points[i]->x = buffer[1 + (i * 2)];
                best->points[i]->y = buffer[2 + (i * 2)];
            }
            s_MPI_Send(buffer, size, MPI_DOUBLE, 0, 0, MPI_COMM_WORLD);
        }
    }

    return best;
}

/*
* This function will fill buffer with noOfOrganisms random organisms
* @arg polygon: the polygon containing the population
* @arg buffer: the buffer to fill
* @arg noOfOrganisms: the number of organisms to put in the buffer
*/
void create_MPI_buffers(polygon* polygon, double* buffer, int noOfOrganisms) {
    int random, second_random, i, j;
    int index = 0;
    organism* organism;

    for (i = 0; i < noOfOrganisms; i++) {
		// Give a double chance to be selected for interprocescrossover to better fit organisms
		do {
			random = rand() % polygon->population_size;
			second_random = rand() % polygon->population_size;
			random = (random < second_random) ? random : second_random;
		} while (polygon->population[random]->used && polygon->population[second_random]->used);
		organism = polygon->population[random]->used ? polygon->population[second_random] : polygon->population[random];
        organism->used = true;
        for (j = 0; j < polygon->organism_size; j++) {
            buffer[index++] = organism->points[j]->x;
            buffer[index++] = organism->points[j]->y;
        }
        buffer[index++] = organism->fitness;
    }
}

/*
* This function will replace the organisms we send with those we received
* @arg polygon: the polygon containing the population
* @arg buffer: the buffer to empty
* @arg noOfOrganisms: the number of organisms to replace in our population
*/
void mpi_merge(polygon* polygon, double* buffer, int noOfOrganisms) {
    int i, j;
    int index = 0;

    for (i = 0; i < polygon->population_size; i++) {
        if (polygon->population[i]->used) {
            for (j = 0; j < polygon->organism_size; j++) {
                polygon->population[i]->points[j]->x = buffer[index++];
                polygon->population[i]->points[j]->y = buffer[index++];
            }
            polygon->population[i]->fitness = buffer[index++];
            polygon->population[i]->used = false;

        }
    }
}

/*
* This function calculates the fitness for the organsims between start and stop
* @arg population: the population to normalize
* @arg start: the index of where to start calculating fitness
* @arg stop: the index of where to stop calculating fitness
* @arg organism_size: the size of an organism
*/
void calculate_fitness(organisms population, int start, int stop, int organism_size){
	int i, j, k;

	for (i = start; i < stop; i++) {
		double fitness = 0;
		for (j = 0; j < organism_size; j++) {
			point* a = population[i]->points[j];
			for (k = 0; k < organism_size; k++) {
				if (j == k) continue;
				point* b = population[i]->points[k];
				fitness += sqrt(sqrt((a->x - b->x) * (a->x - b->x) + (a->y - b->y) * (a->y - b->y)));
			}
		}
		population[i]->fitness = fitness;
	}
}

/*
* This function sorts a population according to it's organisms' fitness in descending order
* The sort performed is a quick sort
* @arg population: the population to sort
* @arg population_size: the size of the population
*/
void sort_population(organisms population, int population_size){
	if (population_size < 2)
		return;

	organism* pivot = population[population_size / 2];
	int left = 0;
	int right = population_size - 1;

	while (left <= right) {
		while (population[left]->fitness > pivot->fitness) left++;
		while (population[right]->fitness < pivot->fitness) right--;
		if (left <= right) {
			organism* temp = population[left];
			population[left++] = population[right];
			population[right--] = temp;
		}
	}
	sort_population(population, right + 1);
	sort_population(&population[left], population_size - left);
}


/*
* @arg polygon: the polygon in which we want to find the best fit organism
* @return: the organism with the highest fitness
*/
organism* best_fit(polygon* polygon) {
	int i;
	int index = 0;
	double max = DBL_MIN;

	for (i = 0; i < polygon->population_size; i++) {
		if (polygon->population[i]->fitness > max) {
			max = polygon->population[i]->fitness;
			index = i;
		}
	}

	return polygon->population[index];
}

/*
* This function generates a random double between a given lower and upper bound
* @arg min: the lower bound
* @arg max: the upper bound
*/
double random_between_doubles(double min, double max) {
	return (min)+(((double)rand()) / (double)RAND_MAX) * (max - min);
}

/*
* @arg polygon: the polygon containing our population
* @arg parent_pairs: the amount of parent pairs to use for crossover
*/
void crossover(polygon* polygon) {
	int i, j, random, second_random;
	organism *father, *mother;
	int new_size = polygon->population_size + polygon->children_size;
	int population_size = polygon->population_size;

	// Prepare the population for crossover
	sort_population(polygon->population, polygon->population_size);

	// Initialize the new part of the population
	polygon->population = (organisms)s_realloc(polygon->population, new_size * sizeof(organism*));
	for (i = population_size; i < new_size; i++) {
		polygon->population[i] = (organism*)s_malloc(sizeof(organism));
		polygon->population[i]->points = (points)s_malloc(polygon->organism_size * sizeof(point*));
		polygon->population[i]->used = false;

		// Give a double chance to be selected for crossover to better fit organisms
		do {
			random = rand() % population_size;
			second_random = rand() % population_size;
			random = (random < second_random) ? random : second_random;
		} while (polygon->population[random]->used && polygon->population[second_random]->used);
		father = polygon->population[random]->used ? polygon->population[second_random] : polygon->population[random];
		father->used = true;

		do {
			random = rand() % population_size;
			second_random = rand() % population_size;
			random = (random < second_random) ? random : second_random;
		} while (polygon->population[random]->used && polygon->population[second_random]->used);
		mother = polygon->population[random]->used ? polygon->population[second_random] : polygon->population[random];
		mother->used = true;
		for (j = 0; j < polygon->organism_size; j++) {
			polygon->population[i]->points[j] = (point*)s_malloc(sizeof(point));
			deep_copy_point(j < (polygon->organism_size / 2) ? father->points[j] : mother->points[j], polygon->population[i]->points[j]);
		}
	}

	// Reset the used values
	for (i = 0; i < population_size; i++) {
		polygon->population[i]->used = false;
	}
}

/*
* @arg polygon: the polygon containing the organisms we want to mutate
*/
void mutate(polygon* polygon, double weight){
	int i, randompoint;
	double randomx, randomy;
	bool generate;
	int new_size = polygon->population_size + polygon->children_size;

	// Mutate one point in every child
	for (i = polygon->population_size; i < new_size; i++) {
		// Only mutate with a MUTATION% chance
		generate = rand() % 100 < MUTATION ? true : false;
		randompoint = rand() % polygon->organism_size;
		while (generate) {
			// Mutate the point by [-MUTATION_WEIGHT, +MUTATION_WEIGHT]
            randomx = random_between_doubles(-weight, weight);
			randomy = random_between_doubles(-weight, weight);
			polygon->population[i]->points[randompoint]->x += randomx;
			polygon->population[i]->points[randompoint]->y += randomy;

			// Reset if mutated point isn't inside polygon
			if (!is_point_in_polygon(polygon, polygon->population[i]->points[randompoint])) {
				polygon->population[i]->points[randompoint]->x -= randomx;
				polygon->population[i]->points[randompoint]->y -= randomy;
			}
			else {
				generate = false;
			}
		}
	}
}

/*
* @arg polygon: the polygon containing the organisms on which we want to do a selection
*/
void selection(polygon* polygon) {
	int i, random, second_random;
	int new_size = polygon->population_size + polygon->children_size;

	// Calculate fitness of the children
	calculate_fitness(polygon->population, polygon->population_size, new_size, polygon->organism_size);
	sort_population(polygon->population, new_size);
	for (i = 0; i < polygon->children_size; i++) {
		// Give a double chance to be selected for death to worse fit organisms
		do {
			// Leave the fittest SURVIVORS% alone
			random = (int)(new_size * SURVIVORS) + (rand() % (int)(new_size - new_size * SURVIVORS));
			second_random = (int)(new_size * SURVIVORS) + (rand() % (int)(new_size - new_size * SURVIVORS));
			random = (random > second_random) ? random : second_random;
		} while (polygon->population[random]->used);
		polygon->population[random]->used = true;
	}

	kill_off(polygon);
}

/*
* @arg polygon: the polygon containing the organisms we want to kill
*/
void kill_off(polygon* polygon) {
	int i, j;
	int new_size = polygon->population_size + polygon->children_size;
	int kills = 0;
	int prev_kill = polygon->population_size;

	// Throw the sentenced organisms in the garbage end
	// and migrate the non-garbage to the population
	// Stop this if we've killed children_size organisms
	for (i = (int)(new_size * SURVIVORS); i < polygon->population_size && kills < polygon->children_size; i++) {
		if (polygon->population[i]->used) {
			for (j = prev_kill; j < new_size; j++) {
				if (!polygon->population[j]->used) {
					organism* temp = polygon->population[i];
					polygon->population[i] = polygon->population[j];
					polygon->population[j] = temp;
					kills++;
					prev_kill = j;
					break;
				}
			}
		}
	}

	// Cleanup
	for (i = polygon->population_size; i < new_size; i++) {
		for (j = 0; j < polygon->organism_size; j++) {
			s_free(polygon->population[i]->points[j]);
		}
		s_free(polygon->population[i]->points);
		s_free(polygon->population[i]);
	}

	polygon->population = s_realloc(polygon->population, polygon->population_size * sizeof(organisms*));
}
