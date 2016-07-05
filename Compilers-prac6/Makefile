.PHONY: all
all: src/tiger runtime/libtiger.a

.PHONY: clean
clean:
	make -C src clean
	make -C runtime clean
	make -C testcases clean


#
# Build rules
#

src/tiger: src
	make -C src

runtime/libtiger.a: runtime
	make -C runtime


#
# Test rules
#

TESTS=bubble factorial

test: all
	make -C testcases $(addsuffix .bin, $(TESTS))
