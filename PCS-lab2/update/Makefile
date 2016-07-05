CODEDIR=./obj

all: CacheSimulator

CacheSimulator: directories cacheBlock.o cache.o bus.o dram.o CacheSimulator.o
	g++ $(CODEDIR)/cacheBlock.o $(CODEDIR)/cache.o $(CODEDIR)/bus.o $(CODEDIR)/dram.o $(CODEDIR)/CacheSimulator.o -o CacheSimulator

directories:
	mkdir -p $(CODEDIR)

cacheBlock.o: cacheBlock.cpp
	g++ -c -o $(CODEDIR)/cacheBlock.o cacheBlock.cpp

cache.o: cache.cpp
	g++ -c -o $(CODEDIR)/cache.o cache.cpp

bus.o: bus.cpp
	g++ -c -o $(CODEDIR)/bus.o bus.cpp

dram.o: dram.cpp
	g++ -c -o $(CODEDIR)/dram.o dram.cpp

CacheSimulator.o: CacheSimulator.cpp
	g++ -c -o $(CODEDIR)/CacheSimulator.o CacheSimulator.cpp

clean:
	rm -rf $(CODEDIR) CacheSimulator
	