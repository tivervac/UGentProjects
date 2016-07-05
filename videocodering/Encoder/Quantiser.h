#ifndef QUANTISER_H
#define QUANTISER_H

#include "Macroblock.h"

class Quantiser
{
public:
	static void Quantise(Macroblock *mb, int qp);
	static int sign(pixel i);
private:
	static const int max = 8;
};

#endif