#include "mpi_wrapper.h"

/*
* Initializes MPI and confirms the result is positive
* @arg argc: number of arguments
* @arg argv: array of arguments
*/
void s_MPI_Init(int* argc, char*** argv) {
    handle_error("MPI_Init", MPI_Init(argc, argv));
}

/*
* Presents you with your process' rank and checks for success
* @arg comm: the communicator
* @arg rank: the rank of the calling process in the group of comm
*/
void s_MPI_Comm_rank(MPI_Comm comm, int* rank) {
    handle_error("MPI_Comm_rank", MPI_Comm_rank(comm, rank));
}

/*
* Presents you with the amount of running process' and checks for success
* @arg comm: the communicator
* @arg size: the number of processes in the group of comm
*/
void s_MPI_Comm_size(MPI_Comm comm, int* size) {
    handle_error("MPI_Comm_size", MPI_Comm_size(comm, size));
}

/*
* Blocking sends a message and checks for success
* @arg buf: the initial address of the send buffer
* @arg count: the number of elements in the send buffer
* @arg datatype: the datatype of each send buffer's element
* @arg dest: the rank of the destination
* @arg tag: the message tag
* @arg comm: the communicator
*/
void s_MPI_Send(void* buf, int count, MPI_Datatype datatype, int dest, int tag, MPI_Comm comm) {
    handle_error("MPI_Send", MPI_Send(buf, count, datatype, dest, tag, comm));
}

/*
* Blocking receives a message and checks for success
* @arg buf: the initial address of the receive buffer
* @arg count: the number of elements in the receive buffer
* @arg datatype: the datatype of each receive buffer's element
* @arg source: the rank of the source
* @arg tag: the message tag
* @arg comm: the communicator
* @arg status: the status object
*/
void s_MPI_Recv(void* buf, int count, MPI_Datatype datatype, int source, int tag, MPI_Comm comm, MPI_Status* status) {
    handle_error("MPI_Recv", MPI_Recv(buf, count, datatype, source, tag, comm, status));
}

void s_MPI_Barrier(MPI_Comm comm) {
    handle_error("MPI_Barrier", MPI_Barrier(comm));
}

/*
* Finalizes MPI and confirms the result is positive
*/
void s_MPI_Finalize() {
    handle_error("MPI_finalize", MPI_Finalize());
}

/*
* Check for an error and prints the error msg to stderr in case of a failure
* @arg method_name: the name of the mpi method
* @arg result: the result of the mpi method
*/
void handle_error(char* method_name, int result) {
    if (result != MPI_SUCCESS) {
        fprintf(stderr, "%s failed with code %i!", method_name, result);
    }
}
