#include "svc_operations.h"


/*
* @arg argc: number of arguments
* @arg argv: array of arguments
* @return: INT_MAX when no flag found, argument if flag is found
*/
int getArg(int argc, char **argv, char* flag){
	//Command = argv[0]
	//Input = argv[1]
	//Output = argv[argc - 1]
	for (argc = argc - 1;argc > 2; argc--){
		if(!strcmp(argv[argc - 1], flag)){
			return atoi(argv[argc]);
		}
	}
	return INT_MAX;
}

/*
* @arg argc: number of arguments
* @arg argv: array of arguments
* @flag -l: spatial adjustment
* @flag -f: quality adjustment
* @flag -t: temporal adjustment
* @return: 0 on normal execution
* @return: 1 when a read error has occured while opening in_file
* @return: 2 when logfile can't be opened
* @return: 3 wrong call to the program 
* @return: 4 when a write error has occured while creating out_file
*/
int main(int argc, char **argv){
	char *input = argv[1]; //First argument
	char *output = argv[argc - 1]; //Last argument
	FILE *in_file;
	options opt;

	if (argc < 3){
		printf("Usage: %s [-l L] [-f F] [-t T] input_file output_file\n", argv[0]);
		exit(3);
	}

	opt.l = getArg(argc, argv, "-l");
	opt.f = getArg(argc, argv, "-f");
	opt.t = getArg(argc, argv, "-t");

	in_file = fopen(input, "rb");
	if (in_file){
		int file_size = get_file_size(in_file);

		unsigned char *in_array = (unsigned char*) malloc(file_size);
		fread(in_array, 1, file_size, in_file);
		fclose(in_file);
		read_svc(in_array, file_size, output, &opt);
	} else {
		perror("Cannot open file!");
		exit(1);
	}
	return 0;
}