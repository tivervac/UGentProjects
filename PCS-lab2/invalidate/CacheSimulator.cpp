#include <fstream>
#include <ostream>
#include <string>
#include <vector>
#include <iostream>
#include <cstdlib>
#include <inttypes.h>
#include <sstream>
#include <cmath>

using namespace std;

#include "cacheBlock.h"
#include "cache.h"
#include "dram.h"
#include "bus.h"


Cache* l1cache[4];
Dram* dram;
Bus* bus;
int N_CORES;


uint64_t lineNumber = 0;
uint64_t errors = 0;

vector<string> splitLineString(string line, string del){
  vector<string> vector;
  size_t found = line.find(del);
  
  while (found!=std::string::npos) {
    vector.push_back(line.substr(0, found));
    line = line.substr(found+del.size());
    found=line.find(del);
  }

  vector.push_back(line);

  return vector;
}

uint64_t hexStringToNumber(string input){
  uint64_t x;   
  std::stringstream ss;
  ss << std::hex << input;
  ss >> x;
  return x;
}

void setUpSimulation(int n_cores, int size){
  dram = new Dram();
  bus = new Bus(n_cores);
  bus->addDram(dram);

  int height = log2(size * 1024 / CacheBlock::cacheBlockSize);
  for(int i=0; i<4; i++){
    l1cache[i] = new Cache(i, height, bus);
    bus->addCache(l1cache[i], i);
  }
}

void simulateRead(int threadId, uint64_t addr, uint64_t data, int size){
  uint64_t dataFromMem = l1cache[threadId % N_CORES]->readFromCache(addr, size);
  if(dataFromMem != data){
    errors ++;
    if(errors <= 100)
      cout << dec << lineNumber << ": got data: '" << hex << dataFromMem << "' but expected to get '" << data << "' for addr: " << addr << "(" << dec << size << ")" << endl;
    if(errors == 100)
      cout << "100 errors printed.\nSwitching to silent mode, errors will no longer be reported." << endl;
  }
}

void simulateWrite(int threadId, uint64_t addr, uint64_t data, int size){
  l1cache[threadId % N_CORES]->writeToCache(addr, data, size);
}

void analyse(){
	//Collect stats from caches.
	stringstream traceFile;
	traceFile << endl << "Errors reported: " << errors << endl << endl;

  for(int i=0; i<N_CORES; i++){
    traceFile << "L1." << (i+1) << " Data Cache Stats: " << endl;
    l1cache[i]->analyse(&traceFile);
  }

  traceFile << endl;
  traceFile << "Bus Stats: " << endl;
  bus->analyse(&traceFile);

  traceFile << endl;
  traceFile << "Dram Stats: " << endl;
  dram->analyse(&traceFile);
  traceFile << endl;

  //Print output to terminal
  cout << traceFile.str();

  //Save output in file
  ofstream outFile;
  outFile.open("cacheStats.out");
  outFile.setf(ios::showbase);
  outFile << traceFile.str();
  outFile.close();
}

int main(int argc, char * argv[]){
  if(argc != 3){
    cerr << "USAGE:" << endl;
    cerr << "./CacheSimulator <#Cores> <Input File>" << endl;
    exit(1);
  }

  N_CORES = atoi(argv[1]);  //Number of cores used for scheduling
  string inputFile = argv[2]; //input trace file
  int size = 32;  //CacheBlockSize in kbyte

  setUpSimulation(N_CORES, size);
  
  cout << "Starting simulation." << endl;

  string line;
  ifstream myfile (inputFile.c_str());
  if (myfile.is_open()){
    while ( getline (myfile,line) ){
      lineNumber ++;
      vector<string> splitLine = splitLineString(line, "\t");
      int threadId     = atoi(splitLine.at(0).c_str());
      uint64_t address = hexStringToNumber(splitLine.at(2));
      uint64_t data    = atoll(splitLine.at(3).c_str());
      int size         = atoi(splitLine.at(4).c_str());

      if(splitLine.at(1).compare("R") == 0){
        simulateRead(threadId, address, data, size);
      } else {
        simulateWrite(threadId, address, data, size);
      }

      if(lineNumber % 1000000 == 0) cout << "Simulated " << (lineNumber / 1000000) << " million instructions." << endl;
    }
    myfile.close();
  }

  analyse();
}
