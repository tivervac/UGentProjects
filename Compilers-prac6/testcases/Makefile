ROOT=..

TIGER=$(ROOT)/src/tiger
CFLAGS=-g -m32
LDFLAGS=-L$(ROOT)/runtime -ltiger

.SUFFIXES:

.PHONY: clean
clean:
	rm -f *.o *.asm *.bc.tig *.lu.tig *.bl.tig *.bin


#
# Build rules
#

.PRECIOUS: %.asm
%.asm: %.tig
	$(TIGER) -S $< > $@

%.o: %.asm
	gcc $(CFLAGS) -c -o $@ -x assembler $<

%.bin: %.o $(ROOT)/runtime/libtiger.a
	gcc $(CFLAGS) -o $@ $< $(LDFLAGS)
