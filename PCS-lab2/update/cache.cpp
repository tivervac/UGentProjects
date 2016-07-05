#include <sstream>

#include "cache.h"

#include "cacheBlock.h"
#include "bus.h"

/* Constructor for a cache.
 *
 * cacheId - is the ID of the cache
 * height - number of entries in the cache = 2^height
 * bus - the bus to connect to.
 */
Cache::Cache(uint8_t cacheId, int height, Bus* bus){
	uint64_t maxIndex = ((uint64_t)1 << height);
	this->CACHE_ID = cacheId;
	this->bus = bus;
	this->cache = new CacheBlock[maxIndex];
	this->addresMask = ((uint64_t)1 << height) - 1;
	this->hit = 0;
	this->miss = 0;
	this->access = 0;
}

/* Destructor
 */
Cache::~Cache(void){
	delete cache;
	delete bus;
}

/* Private method to generate the cache index
 * based on the address.
 */
uint64_t Cache::getCacheIndex(uint64_t addr){
	return (addr / CacheBlock::cacheBlockSize) & addresMask;
}

/* This method will Check the tag and count the
 * number of accesses, hits and misses. This
 * method needs to be called before every read
 * and write operation.
 */
void Cache::logCacheAccess(uint64_t addr){
	//Log Cache access, miss or hit.
	access ++;
	if(isInCache(addr)){
		hit ++;
	} else {
		miss ++;
	}
}

/* This method will check if the cache
 * contains the data on the specified address
 */
bool Cache::isInCache(uint64_t addr){
	//Get the index of the cacheBlock we are looking for.
	uint64_t indexCache = getCacheIndex(addr);
	//Return whether this cache block contains the data.
	return cache[indexCache].isValid(addr);
}

/* This method is called on eviction of the
 * cache block with specified index.
 */
void Cache::evictFromCache(uint64_t indexCache){
    //Store data in cache
    if (cache[indexCache].getState() == SHARED_MODIFIED ||
        cache[indexCache].getState() == MODIFIED) {
        bus->writeBack(cache[indexCache]);
    }

    cache[indexCache].setState(INVALID);
}

/* This method is called by the bus to forward
 * an update message containing the cache block
 * from an other cache.
 */
bool Cache::replyToBusUpdate(uint64_t addr, CacheBlock cacheBlock){
    uint64_t indexCache = getCacheIndex(addr);

    if (cache[indexCache].isValid(addr)) {
        if (cache[indexCache].getState() == SHARED_CLEAN ||
            cache[indexCache].getState() == SHARED_MODIFIED) {
            cache[indexCache].setCacheBlock(cacheBlock);
        }
        if (cache[indexCache].getState() == SHARED_MODIFIED) {
            cache[indexCache].setState(SHARED_CLEAN);
        }
    }

    // Error when in the EXCLUSIVE or MODIFIED state
    // Only needed for dragon protocol.
    return !(cache[indexCache].getState() == EXCLUSIVE || cache[indexCache].getState() == MODIFIED);
}

/* This method is called by the bus to forward
 * a read message from an other cache.
 */
CacheBlock* Cache::replyToBusRead(uint64_t addr){
    uint64_t indexCache = getCacheIndex(addr);

    if (cache[indexCache].getState() == EXCLUSIVE) {
        cache[indexCache].setState(SHARED_CLEAN);
    } else if (cache[indexCache].getState() == MODIFIED) {
        cache[indexCache].setState(SHARED_MODIFIED);
    }

    return (cache[indexCache].isValid(addr)) ? &cache[indexCache] : NULL;
}

/* This method is called by the CacheSimulator.cpp to
 * simulate a cache read request from the CPU.
 */
uint64_t Cache::readFromCache(uint64_t addr, uint8_t size){
    //Log cache access
    logCacheAccess(addr);
    //Get index
    uint64_t indexCache = getCacheIndex(addr);
    uint64_t data;

    if(!isInCache(addr)){	//Cache miss
        //Local Eviction
        evictFromCache(indexCache);
        //Bus Read
        CacheBlock* newBlock = bus->busRead(addr, CACHE_ID);
        cache[indexCache].setCacheBlock(*newBlock);
        //Get actual data from cache
        data = cache[indexCache].getData(addr, size);
        cache[indexCache].setState(newBlock->getLocation() == DRAM ? EXCLUSIVE : SHARED_CLEAN);
    } else {	//Cache hit
        //Local Read
        data = cache[indexCache].getData(addr, size);
    }

    //return data to memoryController
    return data;
}

/* This method is called by the CacheSimulator.cpp to
 * simulate a cache write request from the CPU.
 */
void Cache::writeToCache(uint64_t addr, uint64_t data, uint8_t size){
    //Log cache access
    logCacheAccess(addr);
    //Get index
    uint64_t indexCache = getCacheIndex(addr);

    if(!isInCache(addr)){	//Cache Miss
        //Local Eviction
        evictFromCache(indexCache);
        //Bus Write
        CacheBlock* newBlock = bus->busRead(addr, CACHE_ID);
        cache[indexCache].setCacheBlock(*newBlock);
        //Execute actual write operation (Local Write)
        cache[indexCache].setData(addr, data, size);
        if (newBlock->getLocation() == DRAM) {
            cache[indexCache].setState(MODIFIED);
        } else {
            bus->busUpdate(addr, CACHE_ID, cache[indexCache]);
            cache[indexCache].setState(SHARED_MODIFIED);
        }
	} else {	//Cache Hit
		cache[indexCache].setData(addr, data, size);
        if (cache[indexCache].getState() == SHARED_CLEAN ||
            cache[indexCache].getState() == SHARED_MODIFIED) {
            bus->busUpdate(addr, CACHE_ID, cache[indexCache]);
            cache[indexCache].setState(cache[indexCache].getLocation() == DRAM ? MODIFIED : SHARED_MODIFIED);
        } else {
            cache[indexCache].setState(MODIFIED);
        }
	}

}

void Cache::analyse(std::stringstream* traceFile){
	*traceFile << "\t" << "Hits:      " << hit << "\n";
	*traceFile << "\t" << "Misses:    " << miss << "\n";
	*traceFile << "\t" << "Accesses:  " << access << "\n";
}
