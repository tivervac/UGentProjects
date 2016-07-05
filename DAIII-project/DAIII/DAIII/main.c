#include "main.h"

/*
* @arg argc: number of arguments
* @arg argv: array of arguments
* @return: 0 everything ran as expected
* @return: 1 when s_*alloc failed -> when *alloc failed
* @return: 2 when not enough arguments were given to the executable
* @return: 3 when too many arguments were given to the executable
* @return: 4 when the polygon_file couldn't be read
*/
int main(int argc, char** argv) {
	int organism_size, filename_len;
	char* filename;
	polygon polygon;
	organism* best_fit;

	check_parameters(argc, argv);

	// Initialize random
    srand((unsigned int)time(NULL));

	// Read the parameters
	organism_size = atoi(argv[1]);

	filename_len = strlen(argv[2]);
	filename = (char*)s_malloc((filename_len + 1) * sizeof(char));
	filename = strcpy(filename, argv[2]);

	initialize_polygon(&polygon, organism_size, filename);
	best_fit = find_fitter_organism(&polygon);
    print_organism(best_fit, polygon.organism_size);

	// Cleanup
	cleanup_polygon(&polygon);

	s_free(filename);
	filename = NULL;

	return 0;
}

/*
* @arg argc: number of arguments
* @arg argv: array of arguments
*/
void check_parameters(int argc, char** argv){
	if (argc < 3){
		printf("USAGE: %s <amount_of_points> <path/to/polygon_file>\n", argv[0]);
		exit(2);
	}
	else if (argc > 3){
		printf("Too many parameters\nUSAGE: %s <amount_of_points> <path/to/polygon_file>\n", argv[0]);
		exit(3);
	}
}
