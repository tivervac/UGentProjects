#include "cacheBlock.h"

/* Standard constructor of a cache block.
 * Standard location is "CACHE" (see CacheBlock.h)
 */
CacheBlock::CacheBlock(){
	tag = 0;
	state = INVALID;
	location = CACHE;
	for(int i=0; i<cacheBlockSize/8; i++)
		data[i] = -1;
}

/* Constructor of a cache block with a 
 * specified location (see CacheBlock.h).
 */
CacheBlock::CacheBlock(Location loc){
	tag = 0;
	state = INVALID;
	location = loc;
	for(int i=0; i<cacheBlockSize/8; i++)
		data[i] = -1;
}

/* Copy the content of the specified cache block into
 * this cache block.
 */
void CacheBlock::setCacheBlock(CacheBlock cb){
	setTag(cb.getTag());
	uint64_t addr = cb.getTag();
	for(int i=0; i< (cb.cacheBlockSize / 8); i++){
		setData(addr, cb.getData(addr, 8), 8);
		addr += 8;
	}
}

/* Get the tag of this cache block.
 */
uint64_t CacheBlock::getTag(){
	return tag;
}
/* Generate the tag for the cache block
 * containing the data from address (addr).
 */
uint64_t CacheBlock::getTag(uint64_t addr){
	return addr - (addr % cacheBlockSize);
}
/* Set the tag of this cacheBlock.
 */
void CacheBlock::setTag(uint64_t addr){
	tag = getTag(addr);
}

/* Is the data from address (addr) present in this
 * cache block?
 * 
 * true - the data is present and valid (not invalid)
 * false - the data is not present of not valid (anymore)
 */
bool CacheBlock::isValid(uint64_t ADDR){
	return ((getTag(ADDR) == getTag()) && state != INVALID);
}
/* Set the current state of the cache block.
 */
void CacheBlock::setState(State state){
	this->state = state;
}
/* Get the current state of the cache block.
 */
State CacheBlock::getState(){
	return this->state;
}
/* Get the location of the cache block.
 * Where does the cache block comes from?
 */
Location CacheBlock::getLocation(){
	return this->location;
}

/* Get the data from the cache from address (addr)
 * for the specified size (in byte).
 *
 * DONT CHANGE THIS CODE!!!
 */
uint64_t CacheBlock::getData(uint64_t ADDR, uint8_t size){
	int offset = ADDR % 8;
	int offsetArray = (ADDR % cacheBlockSize - offset) >> 3;
	uint64_t data_out = data[offsetArray];
	if(size < 8){
		uint64_t mask = ((uint64_t)1 << (size * 8)) - 1;
		mask <<= offset * 8;

		data_out &= mask;
		data_out >>= offset * 8;
	}
	return data_out;
}

/* Put the specified data in the cache
 * on address (addr) for the specified
 * size (in byte).
 *
 * DONT CHANGE THIS CODE!!!
 */
void CacheBlock::setData(uint64_t ADDR, uint64_t data_in, uint8_t size){
	setTag(ADDR);

	int offset = ADDR % 8;
	int offsetArray = (ADDR % cacheBlockSize - offset) >> 3;
	if(size == 8){
		data[offsetArray] = data_in;
	} else {
		uint64_t mask = ((uint64_t)1 << (size * 8)) - 1;
		mask <<= offset * 8;
		mask ^= (uint64_t)-1;

		data_in <<= offset * 8;

		
		data[offsetArray] &= mask;
		data[offsetArray] |= data_in;

	}
}
