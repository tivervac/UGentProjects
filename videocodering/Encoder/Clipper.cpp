#include "Clipper.h"

void Clipper::Clip(Macroblock *mb)
{
	// Clip luma
	for (int mb_y = 0; mb_y < 16; mb_y++)
		for (int mb_x = 0; mb_x < 16; mb_x++)
		{
			if (mb->luma[mb_y][mb_x] > 255)
				mb->luma[mb_y][mb_x] = 255;
			else if (mb->luma[mb_y][mb_x] < 0)
				mb->luma[mb_y][mb_x] = 0;
		}

	// Clip chroma
	for (int mb_y = 0; mb_y < 8; mb_y++)
		for (int mb_x = 0; mb_x < 8; mb_x++)
		{
			if (mb->cb[mb_y][mb_x] > 255)
				mb->cb[mb_y][mb_x] = 255;
			else if (mb->cb[mb_y][mb_x] < 0)
				mb->cb[mb_y][mb_x] = 0;

			if (mb->cr[mb_y][mb_x] > 255)
				mb->cr[mb_y][mb_x] = 255;
			else if (mb->cr[mb_y][mb_x] < 0)
				mb->cr[mb_y][mb_x] = 0;
		}
}