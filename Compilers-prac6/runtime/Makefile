CC=gcc
CFLAGS=-Wall -g -m32

all: libtiger.a

.PHONY: clean
clean:
	rm -f runtime.o libtiger.a


#
# Build rules
#

runtime.o: runtime.c
	$(CC) $(CFLAGS) -c $<

libtiger.a: runtime.o
	ar cr $@ runtime.o
