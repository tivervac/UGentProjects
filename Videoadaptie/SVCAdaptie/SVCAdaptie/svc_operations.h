#ifndef SVC_OPERATIONS_H
#define SVC_OPERATIONS_H

#include <stdlib.h>
#include <stdio.h>
#include <string.h>

typedef struct options{
	int l;
	int t;
	int f;
} options;

typedef struct nalu_header{
	unsigned char zero: 1;
	unsigned char idc: 2;
	unsigned char type: 5;
} nalu_header;

typedef struct ext_header{
	unsigned char S: 1;
	unsigned char I: 1;
	unsigned char PRID: 6;
	unsigned char N: 1;
	unsigned char DID: 3;
	unsigned char QID: 4;
	unsigned char TID: 3;
	unsigned char U: 1;
	unsigned char D: 1;
	unsigned char O: 1;
	unsigned char PR: 1;
} ext_header;

void read_svc(unsigned char *in_array, int file_size, char *out_file_name, options *opt);

void sync_word_search(unsigned char *in_array, unsigned char *out_array, char *out_file_name, int *cur_pos, int *cur_pos_out, char *print, int file_size);

int get_file_size(FILE *file);

void nal_unit_header(unsigned char *in_array, unsigned char *out_array, int *cur_pos, int *cur_pos_out, options *opt, char *print, FILE *log_file);

#endif