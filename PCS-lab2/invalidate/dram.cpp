#include "dram.h"

#include "cacheBlock.h"

/* Standard constructor
 */
Dram::Dram(void){

}

/* Empty destructor
 */
Dram::~Dram(void){

}

/* This method read data from the memory.
 * Dram will only read complete cache blocks.
 * Location of the returned block will be set 
 * on DRAM (see cacheBlock.h)
 */
CacheBlock* Dram::readCacheBlockFromDram(uint64_t ADDR){
	access ++;
	reads ++;
	dataRead += CacheBlock::cacheBlockSize;
	
	CacheBlock* cb = new CacheBlock(DRAM);
	uint64_t tag = cb->getTag(ADDR);
	for(int i=0; i< (cb->cacheBlockSize / 8); i++){
		cb->setData(tag, get(tag), 8);
		tag += 8;
	}
	
	return cb;
}

/* This method will write data to the memory.
 * Dram will only write complete cache blocks.
 */
void Dram::writeCacheBlockToDram(CacheBlock cb){
	access ++;
	writes ++;
	dataWriten += CacheBlock::cacheBlockSize;

	uint64_t addr = cb.getTag();
	for(int i=0; i< (cb.cacheBlockSize / 8); i++){
		set(addr, cb.getData(addr, 8));
		addr += 8;
	}
}

void Dram::analyse(std::stringstream* traceFile){
	*traceFile << "\t" << "Access:  " << access << "\n";
	*traceFile << "\t" << "Reads:   " << reads << "\n";
	*traceFile << "\t" << "Written: " << writes << "\n";
	*traceFile << "\n";
	*traceFile << "\t" << "Data read:   " << (dataRead >> 10) << " kbyte" << "\n";
	*traceFile << "\t" << "Data written: " << (dataWriten >> 10) << " kbyte" << "\n";
}