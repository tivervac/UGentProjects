#include "Macroblock.h"

#include <memory.h>

Macroblock::Macroblock() : mb_num(-1), x_pos(-1), y_pos(-1)
{
	init();
}

Macroblock::Macroblock(int mb_num, int x_pos, int y_pos) : mb_num(mb_num), x_pos(x_pos), y_pos(y_pos)
{
	init();
}

// Copy constructor
Macroblock::Macroblock(const Macroblock &mb)
{
	init();

	(*this) = mb;
}

void Macroblock::init()
{
	// Geheugen alloceren voor macroblok
	luma = new pixel*[16];
	cb = new pixel*[8];
	cr = new pixel*[8];
	pixel *buffer = new pixel[16*8*3];

	for (int i = 0; i < 16; i++)
		luma[i] = buffer+i*16;

	buffer += 16*16;
	for (int i = 0; i < 8; i++)
	{
		cb[i] = buffer+i*8;
		cr[i] = buffer+8*8+i*8;
	}

	state = YUV;
}

Macroblock &Macroblock::operator=(const Macroblock &mb)
{
	this->state = mb.state;
	this->mb_num = mb.mb_num;
	this->x_pos = mb.x_pos;
	this->y_pos = mb.y_pos;
	
	this->mv = mb.mv;

	memcpy(this->luma[0], mb.luma[0], 16*8*3*sizeof(pixel));

	return *this;
}

Macroblock::~Macroblock()
{
	delete[] luma[0];
	delete[] luma;
	delete[] cb;
	delete[] cr;
}

int Macroblock::getMBNum()
{
	return mb_num;
}

int Macroblock::getXPos()
{
	return x_pos;
}

int Macroblock::getYPos()
{
	return y_pos;
}

