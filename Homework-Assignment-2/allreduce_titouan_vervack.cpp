/***************************************************************************
 *   Name Titouan Vervack
 ***************************************************************************/

#include "mpi.h"
#include <cstdlib>
#include <iostream>

using namespace std;

/**
 * Wrapper function around MPI_Allreduce (leave this unchanged)
 * @param sendbuf Send buffer containing count doubles (input)
 * @param recvbuf Pre-allocated receive buffer (output)
 * @param count Number of elements in the send and receive buffers
 */
void allreduce(double *sendbuf, double *recvbuf, int count)
{
	MPI_Allreduce(sendbuf, recvbuf, count, MPI_DOUBLE, MPI_SUM, MPI_COMM_WORLD);
}

/**
 * Wrapper function around MPI_Allreduce (implement reduce-scatter / allgather algorithm)
 * @param sendbuf Send buffer containing count doubles (input)
 * @param recvbuf Pre-allocated receive buffer (output)
 * @param count Number of elements in the send and receive buffers
 */
void allreduceRSAG(double *sendbuf, double *recvbuf, int count)
{
	// Note: assume MPI_DOUBLE, MPI_SUM and MPI_COMM_WORLD parameters
	// These may be hard-coded in the implementation. You may assume
	// that the number of processes is a power of two and you may assume
	// that "count" is a multiple of P.

	// Implement the allreduce algorithm using only point-to-point
	// communications, using the reduce-scatter / allgather algorithm
	// (see exercises slides for communication scheme)

	// Do not change the function signature as your implementation
	// will be automatically validated using a test framework.
    MPI_Status status;
    int thisProc, nProc;
    int steps =  0;

    MPI_Comm_rank(MPI_COMM_WORLD, &thisProc);
    MPI_Comm_size(MPI_COMM_WORLD, &nProc);

    // Calculate the amount of steps
    while (nProc >> ++steps);

    int prev_start = 0;
    int prev_stop = nProc;
    double buf[count] = { 0 };
    // Perform reduce scatter
    for (int j = 0; j < steps; j++) {
        int send_size = count / (2 << j);
        int recv_size = (count >> j) ? count >> j : 1;
        int switch_flow = (j == 0) ? 1 : 2 << (j - 1);
        int prev_switch_flow = (j != 1) ? 2 << (j - 2) : 1;
        int stop, start;

        // Only receive after the first step
        if (j) {
            MPI_Recv(recvbuf, recv_size, MPI_DOUBLE, MPI_ANY_SOURCE, MPI_ANY_TAG, MPI_COMM_WORLD, &status);

            // Find our bounds from the previous proces
            bool prev_forward = true;
            for (int i = 0; i < (nProc / prev_switch_flow); i += 2) {
                if ((status.MPI_SOURCE >= ((i + 1) * prev_switch_flow)) && (status.MPI_SOURCE < ((i + 2) * prev_switch_flow))) {
                    prev_forward = false;
                    break;
                }
            }
            start = prev_forward ? prev_stop - recv_size : prev_start;
            stop = start + recv_size;
            prev_start = start;
            prev_stop = stop;
            // Add the data
            for (int i = 0; i < recv_size; i++) {
                sendbuf[start + i] += recvbuf[i];
            }
        } else {
            start = prev_start;
            stop = prev_stop;
        }

        // Don't send in the last step
        if (j != (steps - 1)) {
            // Check if we're going forward or backward
            bool forward = true;
            for (int i = 0; i < (nProc / switch_flow); i += 2) {
                if ((thisProc >= ((i + 1) * switch_flow)) && (thisProc < ((i + 2) * switch_flow))) {
                    forward = false;
                    break;
                }
            }

            // Send last part forward
            if (forward) {
                std::copy(sendbuf + stop - ((stop - start) / 2), sendbuf + stop, buf);
                MPI_Send(buf, send_size, MPI_DOUBLE, thisProc + switch_flow, 1, MPI_COMM_WORLD);
            } // Send first part backward
            else {
                std::copy(sendbuf + start, sendbuf + start + send_size, buf);
                MPI_Send(buf, send_size, MPI_DOUBLE, thisProc - switch_flow, 1, MPI_COMM_WORLD);
            }
        }
    }

    // Perform modified allgather
    for (int j = 0; j < steps; j++) {
        int send_size = (j == 0) ? 1 : count / (2 << (steps - j - 1));
        int recv_size = (j == 1) ? 1 : count >> (steps - j - 1);
        int switch_flow = (count >> (j + 1)) ? count >> (j + 1) : 1;
        int prev_switch_flow = (count >> (j + 1)) ? count >> (j + 1) : 1;
        int stop, start;

        if (j) {
            MPI_Recv(recvbuf, recv_size, MPI_DOUBLE, MPI_ANY_SOURCE, MPI_ANY_TAG, MPI_COMM_WORLD, &status);
            // Find our bounds from the previous proces
            bool prev_forward = true;
            for (int i = 0; i < (nProc / prev_switch_flow); i += 2) {
                if ((status.MPI_SOURCE >= ((i + 1) * prev_switch_flow)) && (status.MPI_SOURCE < ((i + 2) * prev_switch_flow))) {
                    prev_forward = false;
                    break;
                }
            }
            if (prev_forward){
                stop = prev_stop;
                start = prev_start - recv_size;
            } else {
                start = prev_start;
                stop = stop + recv_size;
            }
            prev_start = start;
            prev_stop = stop;
            // Add the data
            for (int i = 0; i < recv_size; i++) {
                sendbuf[start + i] = recvbuf[i];
            }
        } else {
            stop = prev_stop;
            start = prev_start;
        }

        // Don't send in the last step
        if (j != (steps - 1)) {
            // Check if we're going forward or backward
            bool forward = true;
            for (int i = 0; i < (nProc / switch_flow); i += 2) {
                if ((thisProc >= ((i + 1) * switch_flow)) && (thisProc < ((i + 2) * switch_flow))) {
                    forward = false;
                    break;
                }
            }

            std::copy(sendbuf + start, sendbuf + stop, buf);
            // Send first part forward
            if (!forward) {
                MPI_Send(buf, send_size, MPI_DOUBLE, thisProc - switch_flow, 1, MPI_COMM_WORLD);
            } // Send last part backward
            else {
                MPI_Send(buf, send_size, MPI_DOUBLE, thisProc + switch_flow, 1, MPI_COMM_WORLD);
            }
        }
    }
}

/**
 * Program entry
 */
int main(int argc, char* argv[])
{
	int thisProc, nProc;

	MPI_Init(&argc, &argv);
	MPI_Comm_rank(MPI_COMM_WORLD, &thisProc);
	MPI_Comm_size(MPI_COMM_WORLD, &nProc);

	// optionally: write test code
	// (this is not required as we will only test the
	// allreduceRSAG implementation itself)

    int count = 4;
    double sendbuf[count] = {0, 1, 2, 3};
    double recvbuf[count] = {0, 0, 0, 0};

    allreduceRSAG(sendbuf, recvbuf, count);

    for (int i = 0; i < count; i++) {
        cout << sendbuf[i] << " ";
    }
    cout << endl << endl;

    MPI_Finalize();
	exit(EXIT_SUCCESS);
}
