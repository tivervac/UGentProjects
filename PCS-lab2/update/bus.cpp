
#include "bus.h"
#include "cacheBlock.h"
#include "cache.h"
#include "dram.h"


Bus::Bus(int n_cores){
	N_CORES = n_cores;
}

Bus::~Bus(){

}

/* Method to connect the Dram memory to the bus.
 */
void Bus::addDram(Dram* dram){
	this->dram = dram;
}

/* Method to connect the Caches to the bus.
 */
void Bus::addCache(Cache* cache, int index){
	this->l1cache[index] = cache;
}

/* This method is called by the cache
 * to resolve a cache miss event when
 * trying to read data.
 *
 * HINT:
 * Use l1cache[i]->replyToBusRead(addr)
 * to get data from another cache to read from.
 */
CacheBlock* Bus::busRead(uint64_t addr, uint8_t CACHE_ID){
	busReads ++;

	CacheBlock* askedBlock = NULL;
    for (int i = 0; i < N_CORES; i++) {
        if (CACHE_ID == i) continue;
        askedBlock = l1cache[i]->replyToBusRead(addr);
        // Found it
        if (askedBlock) break;
    }

    // Didn't find it, get it from memory
    if (!askedBlock) askedBlock = dram->readCacheBlockFromDram(addr);

	return askedBlock;
}

/* This method is called by a cache to
 * inform other caches is wrote data to
 * a specific address. This method is only
 * used by the UPDATE and DRAGON protocol.
 *
 * HINT:
 * l1cache[i]->replyToBusUpdate(addr, cacheBlock)
 */
bool Bus::busUpdate(uint64_t addr, uint8_t CACHE_ID, CacheBlock cacheBlock){
	busUpdates ++;

    bool result = true;
    for (int i = 0; i < N_CORES; i++) {
        if (CACHE_ID == i) continue;
        result &= l1cache[i]->replyToBusUpdate(addr, cacheBlock);
    }

	//Only needed for dragon protocol.
	return result;
}


/* This method is called by the cache to
 * store an evicted cacheBlock in memory.
 */
void Bus::writeBack(CacheBlock cacheBlock){
	dram->writeCacheBlockToDram(cacheBlock);
}

void Bus::analyse(std::stringstream* traceFile){
	*traceFile << "\t" << "busReads:     " << busReads << "\n";
	*traceFile << "\t" << "busUpdates:   " << busUpdates << "\n";
}
