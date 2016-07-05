#include "svc_operations.h"

/*
* @arg out_array: array containing the date to write
* @arg in_array: array containg the initial file, passed so it can be freed
* @arg out_file_name: file to write to
* @arg size: size of out_array
* @return: none
*/
void write_out_file(unsigned char* out_array, unsigned char* in_array, char *out_file_name, int size){
	FILE *out_file = fopen(out_file_name, "wb");
	if (!out_file){
		perror("Cannot open file!");
		exit(4);
	}

	fwrite(out_array, 1, size, out_file);
	fclose(out_file);
	free(out_array);
	free(in_array);
	exit(0);
}

/*
* @arg in_array: array to parse
* @arg file_size: size of in_array
* @arg out_file_name: file to write to
* @arg opt: struct containing the optional arguments
* @return: none
*/
void read_svc(unsigned char *in_array, int file_size, char *out_file_name, options *opt){
	int cur_pos= 0;
	int cur_pos_out = 0;
	//Boolean to check if we have to print or not
	char print = 1;
	FILE *log_file;
	unsigned char *out_array = (unsigned char*) malloc(file_size);
	int name_size = strlen(out_file_name);
	char *log = (char*) malloc(name_size);

	//Make a nice name for the log file: out_file.log
	strncpy(log, out_file_name, name_size - 3);
	log[name_size - 3] = 'l';
	log[name_size - 2] = 'o';
	log[name_size - 1] = 'g';
	log[name_size] = '\0';
	log_file = fopen(log, "w");
	free(log);

	if (!log_file){
		perror("Cannot open logfile!");
		exit(2);
	}

	//Write some extra information to the log file
	fprintf(log_file, "Spatial  Adjustment: %d\nQuality  Adjustment: %d\nTemporal Adjustment: %d\n\n", opt->l, opt->f, opt->t);
	fprintf(log_file,"-------------------------------------------------------------------------");
	fprintf(log_file,"\n|  Address  |  Wrote  |  Nalu  |  No.  |  Type  |  DID  |  QID  |  TID  |\n");
	fprintf(log_file,"-------------------------------------------------------------------------");

	//Run through the file
	while(1){
		//Read zero bytes until you've found the sync word
		sync_word_search(in_array, out_array, out_file_name, &cur_pos, &cur_pos_out, &print, file_size);
		nal_unit_header(in_array, out_array, &cur_pos, &cur_pos_out, opt, &print, log_file);
	}
}

/*
* @arg file: file to measure file_size of
* @return: size of the file in bytes 
*/
int get_file_size(FILE *file){
	int size;

	fseek(file, 0, SEEK_END);
	size = ftell(file);
	rewind(file);

	return size;
}

/*
* @arg in_array: array to search in
* @arg out_array: array to write to
* @arg out_file_name: file to write to
* @arg cur_pos: pointer to current position in in_array
* @arg cur_pos_out: pointer to current position in out_array
* @arg print: boolean to check if we have to print or not
* @arg file_size: size of in_array
* @return: none
*/
void sync_word_search(unsigned char *in_array, unsigned char *out_array, char *out_file_name, int *cur_pos, int *cur_pos_out, char *print, int file_size){
	unsigned char first, second, third;
	char first_time = 1;

	//First check for sync word
	first = in_array[*cur_pos];
	second = in_array[(*cur_pos) + 1];
	third = in_array[(*cur_pos) + 2];
	(*cur_pos) += 3;
	while(!((first + second == 0) && (second + third == 1))){
		if(*cur_pos > file_size){
			write_out_file(out_array, in_array, out_file_name, *cur_pos_out);
		}
		if (first_time){
			if (*print){
				out_array[*cur_pos_out] = first;
				out_array[(*cur_pos_out) + 1] = second;
				(*cur_pos_out) += 2;
			}
			first_time = 0;
		}
		if(*print){
			//Write RBSP to the output
			out_array[*cur_pos_out] = third;
			++(*cur_pos_out);
		}
		//Roll the list over
		first = second;
		second = third;
		//Try to find sync word
		third = in_array[*cur_pos];
		++(*cur_pos);
	}
}

/*
* @arg in_array: array to read from
* @arg out_array: array to write to
* @arg cur_pos: pointer to current position in in_array
* @arg cur_pos_out: pointer to current position in out_array
* @arg opt: struct containing the optional arguments
* @arg print: boolean to check if we have to print or not
* @arg log_file: the log file
* @return: none
* NALU header representation: ZIIT TTTT
*/
void nal_unit_header(unsigned char *in_array, unsigned char *out_array, int* cur_pos, int *cur_pos_out, options *opt, char *print, FILE *log_file){
	char buf = 0;
	nalu_header n_header = {0};
	ext_header e_header = {0};
	static int amount_of_headers = 0;
	static short previous_type = 0;

	buf = in_array[*cur_pos];
	//Get the second and third bit, skip zero bit
	n_header.idc = (buf & 0x60) >> 5;
	//Get the last 5 bits
	n_header.type = buf & 0x1F;
	++(*cur_pos);

	//Delete the complete frame!
	if ((n_header.type == 6 || n_header.type == 5 || n_header.type == 1) && (previous_type == 20 || previous_type == 14) && !*print){
		fprintf(log_file, "\n%10x %9s %8s %7d %8d", *cur_pos - 4, "No", "No", amount_of_headers, n_header.type);
		++amount_of_headers;
		return;
	}
	previous_type = n_header.type;

	//Read the second, 3-byte-long, header
	if(n_header.type == 20 || n_header.type == 14){		
		//First byte
		char buf1, buf2, buf3 = 0;
		buf1 = in_array[*cur_pos];
		e_header.S = (buf1 & 0x80) >> 7;
		e_header.I = (buf1 & 0x40) >> 6;
		e_header.PRID = buf1 && 0x3F;
		//Second byte
		buf2 = in_array[(*cur_pos) + 1];
		e_header.N = (buf2 & 0x80) >> 7;
		e_header.DID = (buf2 & 0x70) >> 4;
		e_header.QID = buf2 & 0xF;
		//Third byte
		buf3 = in_array[(*cur_pos) + 2];
		e_header.TID = (buf3 & 0xE0) >> 5;
		e_header.U = (buf3 & 0x10) >> 4;
		e_header.D = (buf3 & 0x8) >> 3;
		e_header.O = (buf3 & 0x4) >> 2;
		e_header.PR = buf3 & 0x3;
		(*cur_pos) += 3;
		if (e_header.DID <= opt->l && e_header.QID <= opt->f && e_header.TID <= opt->t){
			//Write sync word
			out_array[*cur_pos_out] = 1;
			//NALU header
			out_array[*cur_pos_out + 1] = buf;
			//Extra header
			out_array[*cur_pos_out + 2] = buf1;
			out_array[*cur_pos_out + 3] = buf2;
			out_array[*cur_pos_out + 4] = buf3;
			*cur_pos_out += 5;
			fprintf(log_file, "\n%10x %9s %8s %7d %8d %7d %7d %7d", *cur_pos - 7, "Yes", "No", amount_of_headers, n_header.type, e_header.DID, e_header.QID, e_header.TID);
			*print = 1;
		} else {
			if (*print){
				//Delete the first parts of the sync word
				(*cur_pos_out) -= 1;
				*print = 0;
			}
			fprintf(log_file, "\n%10x %9s %8s %7d %8d %7d %7d %7d", *cur_pos - 7, "No", "No", amount_of_headers, n_header.type, e_header.DID, e_header.QID, e_header.TID);}
	} else {
		//Write sync word
		out_array[*cur_pos_out] = 1;
		out_array[(*cur_pos_out) + 1] = buf;
		(*cur_pos_out) += 2;
		*print = 1;
		fprintf(log_file, "\n%10x %9s %8s %7d %8d", *cur_pos - 4, "Yes", "Yes", amount_of_headers, n_header.type);
	}
	++amount_of_headers;
}
