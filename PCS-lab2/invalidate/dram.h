#include <map>
#include <inttypes.h>
#include <sstream>

#ifndef Dram_H
#define Dram_H

	//forward declaration
	class CacheBlock;

	class Dram {
		private:
			uint64_t access, reads, writes, dataRead, dataWriten;

			std::map<uint64_t, uint64_t> dram;

		public:
			Dram(void);
			~Dram(void);

			uint64_t readFromDram(uint64_t ADDR, uint8_t size);
			CacheBlock* readCacheBlockFromDram(uint64_t ADDR);
			
			void writeToDram(uint64_t ADDR, uint64_t data, uint8_t size);
			void writeCacheBlockToDram(CacheBlock cb);

			void analyse(std::stringstream* traceFile);

		private:
			/* Get 8 bytes of data from memory
			 *
			 * DONT CHANGE THIS CODE!
			 */
			uint64_t get(uint64_t ADDR){
				uint64_t dramIndex = getDramIndex(ADDR);
				if(dram.count(dramIndex) == 0){
					dram.insert(std::pair<uint64_t, uint64_t>(dramIndex, -1));
				}
				return dram.at(dramIndex);
			}

			/* Store 8 bytes of data in memory
			 *
			 * DONT CHANGE THIS CODE!
			 */
			void set(uint64_t ADDR, uint64_t data){
				uint64_t dramIndex = getDramIndex(ADDR);
				if(dram.count(dramIndex) == 0){
					dram.insert(std::pair<uint64_t, uint64_t>(dramIndex, -1));
				}
				dram.at(dramIndex) = data;
			}

			/* Calculate the dram index
			 * (by removing the 3 LSBs)
			 *
			 * DONT CHANGE THIS CODE!
			 */
			uint64_t getDramIndex(uint64_t addr){
				uint64_t mask = ((uint64_t)-1) << 3;
				return addr & mask;
			}
	};

#endif
