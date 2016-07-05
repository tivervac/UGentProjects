#ifndef DCTTRANSFORM_H
#define DCTTRANSFORM_H

#include "Macroblock.h"

class DCTTransform
{
public:
	DCTTransform();
	void Transform(Macroblock *mb);

private:
	static const int max = 8;
	double c[8][8];

	void Transform_8x8(pixel **block, int offset_x, int offset_y);
};

#endif