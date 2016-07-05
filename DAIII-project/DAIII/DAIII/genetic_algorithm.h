#ifndef GENETIC_ALGORITHM_H
#define GENETIC_ALGORITHM_H

#include "polygon.h"
#include "safe_alloc.h"

organism* find_fitter_organism(polygon* polygon);

void calculate_fitness(organisms population, int start, int stop, int organism_size);

void sort_population(organisms population, int population_size);

organism* best_fit(polygon* polygon);

double random_between_doubles(double min, double max);

void crossover(polygon* polygon);

void mutate(polygon* polygon, double mutation_weight);

void selection(polygon* polygon);

void kill_off(polygon* polygon);

#endif
