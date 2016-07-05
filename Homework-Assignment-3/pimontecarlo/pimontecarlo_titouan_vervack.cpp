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
#include <random>

using namespace std;

int N;
int Nloc;
double pi;
pthread_mutex_t mutex;
pthread_mutex_t cout_mutex;

void* pimontecarlo(void *vargs)
{
	long threadID = (long) vargs;
    double my_pi = 0.0;
    pthread_mutex_lock(&cout_mutex);
    cout << "Hello from thread " << threadID << endl;
    pthread_mutex_unlock(&cout_mutex);

	std::default_random_engine generator;
	std::uniform_real_distribution<double> distribution(-1.0,1.0);

	for (size_t i = 0; i < Nloc; i++) {
		double x = distribution(generator);
		double y = distribution(generator);
		if (sqrt(x*x + y*y) < 1.0)
			my_pi += 4.0 / double(N);
	}

    pthread_mutex_lock(&mutex);
    pi += my_pi;
    pthread_mutex_unlock(&mutex);

	return NULL;
}

int main(int argc, char* argv[])
{
	if (argc != 2) {
		cerr << "Usage: ./pimontecarlo <number of threads>" << endl;
		exit(EXIT_FAILURE);
	}

	int numThreads = atoi(argv[1]);

	N = 1000000;
	cout << "Generating " << N << " random values using " << numThreads << " thread(s)." << endl;

	Nloc = N / numThreads;

    pthread_mutex_init(&mutex, NULL);
    pthread_mutex_init(&cout_mutex, NULL);

	pthread_t *thread = new pthread_t[numThreads];

	// create threads
	for (long i = 0; i < numThreads; i++)
        pthread_create(&thread[i], NULL, pimontecarlo, (void*)i);

	for (int i = 0; i < numThreads; i++)
		pthread_join(thread[i], NULL);

    pthread_mutex_destroy(&mutex);

	cout.precision(12);
	cout << "Value for pi: " << pi << endl;

	delete [] thread;
	exit(EXIT_SUCCESS);
}
