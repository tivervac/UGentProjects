/***************************************************************************
 *   Copyright (C) 2012-2013 Jan Fostier (jan.fostier@intec.ugent.be)      *
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *                                                                         *
 *   This program is distributed in the hope that it will be useful,       *
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of        *
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the         *
 *   GNU General Public License for more details.                          *
 *                                                                         *
 *   You should have received a copy of the GNU General Public License     *
 *   along with this program; if not, write to the                         *
 *   Free Software Foundation, Inc.,                                       *
 *   59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.             *
 ***************************************************************************/
// Titouan Vervack
#include <cstdlib>
#include <iostream>
#include <ctime>
#include <cstring>
#include <cmath>
#include <pthread.h>

using namespace std;

// ==========================================================================
// TIMING ROUTINES
// ==========================================================================

int N;
int numThreads;
double *value;
double *localSum;
double prevTime;

void startChrono()
{
        prevTime = double(clock()) / CLOCKS_PER_SEC;
}

double stopChrono()
{
        double currTime = double(clock()) / CLOCKS_PER_SEC;
        return currTime - prevTime;
}

// ==========================================================================
// THREAD ROUTINE
// ==========================================================================

void* threadsum(void *vargs)
{
    long threadID = (long)vargs;

    int start = threadID * N / numThreads;
    int stop = (threadID + 1) * N / numThreads;

    localSum[threadID] = 0.0;
    double my_localSum = 0;
    for (size_t i = start; i < stop; i++)
        my_localSum += value[i];

    localSum[threadID] += my_localSum;

    return NULL;
}

int main(int argc, char* argv[])
{
    if (argc != 2) {
        cerr << "Usage: ./threadsum <number of threads>" << endl;
        exit(EXIT_FAILURE);
    }

    numThreads = atoi(argv[1]);
    N = 100000000;

    value = new double[N];
    for (size_t i = 0; i < N; i++)
        value[i] = i;

    cout << "Summing " << N << " values using " << numThreads << " thread(s)." << endl;

    pthread_t *thread = new pthread_t[numThreads];
    localSum = new double[numThreads];

    startChrono();

    // create threads
    for (long i = 0; i < numThreads; i++)
        pthread_create(&thread[i], NULL, threadsum, (void*)i);

    for (int i = 0; i < numThreads; i++)
        pthread_join(thread[i], NULL);
	
    // calculate global sum
    double sum = 0.0;
    for (int i = 0; i < numThreads; i++)
        sum += localSum[i];

    cout.precision(12);
    cout << "Sum = " << sum << endl;

    // start/stopChrono provides CPU time, therefore divide by numThreads
    // to approximately get the wall clock time
    cout << "Runtime: " << stopChrono() / numThreads << endl;

    delete [] thread;
    delete [] localSum;
    delete [] value;

    exit(EXIT_SUCCESS);
}
