#include <inttypes.h>
#include <sstream>

#ifndef Cache_H
#define Cache_H
	
	//forward declaration
	class CacheBlock;
	class Bus;

	class Cache {
		private:
			uint64_t hit, miss, access;

			uint8_t CACHE_ID;

			Bus* bus;
			CacheBlock* cache;
			uint64_t addresMask;
			
		public:
			Cache(uint8_t cacheId, int height, Bus* bus);
			~Cache(void);

			void logCacheAccess(uint64_t ADDR);

			bool isInCache(uint64_t ADDR);
			
			uint64_t readFromCache(uint64_t ADDR, uint8_t size);
			void writeToCache(uint64_t ADDR, uint64_t data, uint8_t size);
			void evictFromCache(uint64_t index);

			CacheBlock* replyToBusRead(uint64_t ADDR);
			CacheBlock* replyToBusWrite(uint64_t ADDR);
			void replyToBusUpgrade(uint64_t ADDR);
			bool replyToBusUpdate(uint64_t ADDR, CacheBlock cacheBlock);

			void analyse(std::stringstream* traceFile);
		private:

			uint64_t getCacheIndex(uint64_t ADDR);
	};

#endif
