#include "mpi_polygon.h"
#define POPULATION_SIZE 400

/*
* @arg polygon: the polygon to initialize
* @arg organism_size: the number of points to place in the polygon, size of an organism
* @arg polygon_filename: the path to the polygon_file
*/
void initialize_polygon(polygon* polygon, int organism_size, char* polygon_filename){
    int processes;

    s_MPI_Comm_size(MPI_COMM_WORLD, &processes);

	polygon->organism_size = organism_size;
	polygon->population_size = POPULATION_SIZE / processes;
	read_input(polygon, polygon_filename);
	create_initial_population(polygon);
}

/*
* Reads the input and initializes the polygon
* @arg polygon: the polygon to create
* @arg polygon_filename: the path to the polygon_file
*/
void read_input(polygon* polygon, char* polygon_filename) {
	FILE* polygon_file;
	int i;

	// Read the polygon file
	polygon_file = fopen(polygon_filename, "r");
	if (polygon_file == NULL) {
		printf("Failed to read %s\n", polygon_filename);
		exit(4);
	}

	fscanf(polygon_file, "%i", &polygon->no_of_corners);

	// Initialize the polygon
	polygon->corners = (points)s_malloc(polygon->no_of_corners * sizeof(point*));
	polygon->upper_left = (point*)s_malloc(sizeof(point));
	polygon->bottom_right = (point*)s_malloc(sizeof(point));
	polygon->upper_left->x = DBL_MAX;
	polygon->upper_left->y = DBL_MIN;
	polygon->bottom_right->x = DBL_MIN;
	polygon->bottom_right->y = DBL_MAX;

	for (i = 0; i < polygon->no_of_corners; i++) {
		point* p = (point*)s_malloc(sizeof(point));
		fscanf(polygon_file, "%lf", &p->x);
		fscanf(polygon_file, "%lf", &p->y);
		polygon->corners[i] = p;

		// Check for new bounds
		if (polygon->upper_left->x > p->x) {
			polygon->upper_left->x = p->x;
		}
		if (polygon->upper_left->y < p->y) {
			polygon->upper_left->y = p->y;
			polygon->index_of_biggest_y = i;
		}
		if (polygon->bottom_right->x < p->x) {
			polygon->bottom_right->x = p->x;
		}
		if (polygon->bottom_right->y > p->y) {
			polygon->bottom_right->y = p->y;
			polygon->index_of_smallest_y = i;
		}
	}

	// Cleanup
	fclose(polygon_file);
}

/*
* This function will decide the initial placement of the points.
* These points will be placed within the bounds calculated in read_input
* @arg polygon: the polygon in which we have to place points
*/
void create_initial_population(polygon* polygon) {
	int i, j;

	polygon->population = (organisms)s_malloc(polygon->population_size * sizeof(organism));
	for (i = 0; i < polygon->population_size; i++) {
		polygon->population[i] = (organism*)s_malloc(sizeof(organism));
		polygon->population[i]->points = (points)s_malloc(polygon->organism_size * sizeof(point*));
		polygon->population[i]->used = false;
		for (j = 0; j < polygon->organism_size; j++){
			point* p = s_malloc(sizeof(point));
			do {
				p->x = polygon->upper_left->x + (((double)rand() / (double)(RAND_MAX)) * (polygon->bottom_right->x - polygon->upper_left->x));
				p->y = polygon->bottom_right->y + (((double)rand() / (double)(RAND_MAX)) * (polygon->upper_left->y - polygon->bottom_right->y));
			} while (!is_point_in_polygon(polygon, p));
			polygon->population[i]->points[j] = p;
		}
	}
}

/*
* This function checks if a given point is within the bounds of the given polygon
* @arg polygon: the polygon in which the point should be situated
* @arg p: the point to check
*/
bool is_point_in_polygon(polygon* polygon, point* p) {
	int i;
	bool first_intersection = false;
	bool second_intersection = false;

	// Check if polygon is above or beneath the point
	if (polygon->bottom_right->y >= p->y || polygon->upper_left->y <= p->y) {
		return false;
	}

	// Loop over the edges of right polyline
	for (i = polygon->index_of_smallest_y; i < polygon->index_of_biggest_y; i++) {
		point a = *polygon->corners[i];
		point b = *polygon->corners[i + 1];
		if (p->y <= b.y && p->y >= a.y){
			/*
			*  Found an edge right of point
			*  Use the sign of the cross product to determine wether the point is left of, right of or on the edge
			*  if this is 0 it's on the edge, -1 above/right the edge, +1 under/left the edge
			*/
			if (copysignf(1.0, (b.x - a.x)*(p->y - a.y) - (b.y - a.y)*(p->x - a.x)) <= 0) {
				// Point is right of or on the polyline so not in the polygon
				return false;
			}
			first_intersection = true;
			break;
		}
	}

	// No intersection to the right of the point => not in polygon
	if (first_intersection == false) {
		return false;
	}

	// Loop over the edges of the left polyline
	for (i = polygon->index_of_biggest_y; i < polygon->index_of_biggest_y + (polygon->no_of_corners + polygon->index_of_smallest_y); i++) {
		point a = *polygon->corners[i % polygon->no_of_corners];
		point b = *polygon->corners[(i + 1) % polygon->no_of_corners];
		if (p->y <= a.y && p->y >= b.y){
			// Found an edge left of point
			// If this is 0 it's on the edge, +1 above/right the edge, -1 under/left the edge
			if (copysignf(1.0, (a.x - b.x)*(p->y - b.y) - (a.y - b.y)*(p->x - b.x)) >= 0) {
				// Point is left of or on the polyline so not in the polygon
				return false;
			}
			second_intersection = true;
			break;
		}
	}

	return first_intersection & second_intersection;
}

/*
* @arg src: the source point
* @arg dest: the point to which we want to copy
*/
void deep_copy_point(point* src, point* dest) {
	dest->x = src->x;
	dest->y = src->y;
}

/*
* @arg organism: the organism to print
* @arg organism_size: the size of the organism to print
*/
void print_organism(organism* organism, int organism_size) {
	int j;

	printf("%lf\n", organism->fitness);
	for (j = 0; j < organism_size; j++) {
		printf("%lf %lf\n", organism->points[j]->x, organism->points[j]->y);
	}
}

/*
* @arg polygon: the polygon to be cleaned
*/
void cleanup_polygon(polygon* polygon){
	int i, j;

	for (i = 0; i < polygon->no_of_corners; i++){
		s_free(polygon->corners[i]);
	}

	for (i = 0; i < polygon->population_size; i++) {
		for (j = 0; j < polygon->organism_size; j++) {
			s_free(polygon->population[i]->points[j]);
		}
		s_free(polygon->population[i]->points);
		s_free(polygon->population[i]);
	}

	s_free(polygon->upper_left);
	s_free(polygon->bottom_right);
	s_free(polygon->population);
	s_free(polygon->corners);
}
