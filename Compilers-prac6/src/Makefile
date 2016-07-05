ROOT=..

CC=gcc
CFLAGS=-g -m32 -I$(ROOT)/tools/include
LDFLAGS=-L$(ROOT)/tools/lib -lantlr3c -Wl,-rpath,"$(realpath $(ROOT)/tools/lib)"

.PHONY: all
all: tiger

.PHONY: clean
clean:
	rm -f i386codegen.o tiger


#
# Build rules
#

%.o:  %.c *.h
	$(CC) -c $(CFLAGS) $<

tiger: i386codegen.o
	$(CC) -o $@ $(CFLAGS) $< -L. -lpract6 $(LDFLAGS) 
