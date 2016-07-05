#
# Makefile for lab3.
# Author: Andreas Sandberg
#

CC=gcc
CFLAGS=-std=c99 -D_XOPEN_SOURCE=600 -msse3 -O2 -Wall -Werror -g
LDFLAGS=-g
LIBS=-lpthread -lrt

TEST_IMPLS=cmpxchg critical incdec
CS_IMPLS=pthreads null mutex_lab3 

all: lab3

lab3: lab3.o $(CS_IMPLS:%=cs_%.o) $(TEST_IMPLS:%=test_%.o)
	$(CC) $(CFLAGS) $(LDFLAGS) -o $@ $^ $(LIBS)

clean:
	rm -f lab3 *.o

.PHONY: all clean
