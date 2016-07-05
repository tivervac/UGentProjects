#ifndef MPI_POLYGON_H
#define MPI_POLYGON_H

#include <stdlib.h>
#include <stdio.h>
#include <float.h>
#include <stdbool.h>
#include <math.h>
#include "safe_alloc.h"
#include "mpi_wrapper.h"

typedef struct {
	double x;
	double y;
} point;

typedef point** points;

typedef struct {
	points points;
	double fitness;
	bool used;
} organism;

typedef organism** organisms;

typedef struct {
	points corners;
	organisms population;

	point* upper_left;
	point* bottom_right;

	int index_of_smallest_y;
	int index_of_biggest_y;
	int population_size;
	int children_size;
	int organism_size;
	int no_of_corners;
} polygon;

void initialize_polygon(polygon* polygon, int no_of_points, char* polygon_filename);

void read_input(polygon* polygon, char* polygon_filename);

void create_initial_population(polygon* polygon);

bool is_point_in_polygon(polygon* polygon, point* p);

void deep_copy_point(point* src, point* dest);

void print_organism(organism* organism, int organism_size);

void cleanup_polygon(polygon* polygon);

#endif
