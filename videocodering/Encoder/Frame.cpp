#include "Frame.h"

#include <memory.h>
#include <assert.h>
#include <stdlib.h>
#include <malloc.h>

Frame::Frame(int width, int height) : width(width), height(height)
{
	Init();
}

Frame::Frame(const Frame &frame)
{
	Init();

	(*this) = frame;
}

Frame &Frame::operator=(const Frame &frame)
{
	assert(width == frame.width && height == frame.height);

	for (int i = 0; i < width*height; i++)
		memcpy(this->macroblocks[i].luma[0], frame.macroblocks[i].luma[0], 16*16);

	return *this;
}

void Frame::Init()
{
	num_mb = width * height;

	// Alloceer macroblokken
	macroblocks = new Macroblock[num_mb];

	for (int i = 0; i < num_mb; i++)
		macroblocks[i] = Macroblock(i, i%width, i/width);

	// Referenties naar omliggende macroblokken instellen
	for (int i = 0; i < num_mb; i++)
	{
		macroblocks[i].mb_up = (i > width) ? &macroblocks[i-width] : NULL;
		macroblocks[i].mb_left = (i % width != 0) ? &macroblocks[i-1] : NULL;
	}
}

Frame::~Frame()
{
	delete[] macroblocks;
	macroblocks = 0;
}

int Frame::getWidth()
{
	return width;
}

int Frame::getHeight()
{
	return height;
}

int Frame::getNumMB()
{
	return num_mb; 
}

Macroblock *Frame::getMacroblock(int index)
{
	return &macroblocks[index];
}
