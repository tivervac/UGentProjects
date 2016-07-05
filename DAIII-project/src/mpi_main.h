#ifndef MPI_MAIN_H
#define MPI_MAIN_H

#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <time.h>
#include "mpi_polygon.h"
#include "safe_alloc.h"
#include "mpi_genetic_algorithm.h"
#include "mpi_wrapper.h"

int main(int argc, char** argv);

void check_parameters(int argc, char** argv);

#endif
