#ifndef MPI_WRAPPER_H
#define MPI_WRAPPER_H

#include <stdio.h>
#include <mpi.h>

void s_MPI_Init(int* argc, char*** argv);

void s_MPI_Comm_rank(MPI_Comm comm, int* rank);

void s_MPI_Comm_size(MPI_Comm comm, int* size);

void s_MPI_Send(void* buf, int count, MPI_Datatype datatype, int dest, int tag, MPI_Comm comm);

void s_MPI_Recv(void* buf, int count, MPI_Datatype datatype, int source, int tag, MPI_Comm comm, MPI_Status* status);

void s_MPI_Barrier(MPI_Comm comm);

void s_MPI_Finalize();

void handle_error(char* method_name, int result);

#endif
