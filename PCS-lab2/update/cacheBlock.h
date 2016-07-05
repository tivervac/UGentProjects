#include <inttypes.h>

#if !defined( CacheBlock_H )
#define CacheBlock_H

	/* This enum contains a list of all
	 * possible states. Change te list
	 * when needed.
	 */
	enum State{
		INVALID,
		MODIFIED,
        SHARED_CLEAN,
        SHARED_MODIFIED,
        EXCLUSIVE
	};

	/* The location of the cache block.
	 * CACHE - when the cache block is fetched from a cache.
	 * DRAM - when the cache block is fetched from the memory.
	 *
	 * HINT:
	 * This can be used to know the source of a returned
	 * cache block by a busRead, busWrite, etc.
	 */
	enum Location{
		CACHE,
		DRAM
	};

	class CacheBlock {
	public:
		const static uint8_t cacheBlockSize = 64; //size in byte
	private:
		State state;
		Location location;
		uint64_t tag;
		uint64_t data[cacheBlockSize / 8];

		void setTag(uint64_t addr);
	public:
		CacheBlock();
		CacheBlock(Location location);

		uint64_t getTag();
		uint64_t getTag(uint64_t addr);

		bool isValid(uint64_t addr);
		State getState();
		void setState(State state);
		Location getLocation();

		uint64_t getData(uint64_t ADDR, uint8_t size);
		void setData(uint64_t ADDR, uint64_t data_in, uint8_t size);

		// copy
		void setCacheBlock(CacheBlock cb);
	};

#endif
