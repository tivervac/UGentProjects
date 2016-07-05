#include <inttypes.h>
#include <sstream>

#if !defined( Bus_H )
#define Bus_H
	
	//forward declaration
	class CacheBlock;
	class Cache;
	class Dram;

	class Bus{
	private:
		uint64_t busWrites, busReads, busUpgrades, busUpdates;

		int N_CORES;
		Cache* l1cache[24];
		Dram* dram;

	public:
		Bus(int n_cores);
		~Bus();

		void addDram(Dram* dram);
		void addCache(Cache* cache, int index);

		CacheBlock* busWrite(uint64_t addr, uint8_t CACHE_ID);
		CacheBlock* busRead(uint64_t addr, uint8_t CACHE_ID);
		void busUpgrade(uint64_t addr, uint8_t CACHE_ID);
		bool busUpdate(uint64_t addr, uint8_t CACHE_ID, CacheBlock cacheBlock);

		void writeBack(CacheBlock cacheBlock);

		void analyse(std::stringstream* traceFile);
	};

#endif