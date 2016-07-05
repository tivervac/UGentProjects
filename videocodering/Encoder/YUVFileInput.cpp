#include "YUVFileInput.h"

YUVFileInput::YUVFileInput(char* filename, int width, int height) : width(width), height(height)
{
	fopen_s(&inputfile,filename, "rb");
}

YUVFileInput::~YUVFileInput()
{
	fclose(inputfile);
	inputfile = NULL;
}

Frame* YUVFileInput::getNextFrame()
{
	Frame* next_frame = new Frame(width, height);

	// Luma
	for (int mb_y = 0; mb_y < height; mb_y++)				// Voor elke rij macroblokken
		for (int i = 0; i < 16; i++)						// Voor elke rij pixels
			for (int mb_x = 0; mb_x < width; mb_x++)		// Voor elk macroblok
				for (int j = 0; j < 16; j++)				// Voor elke kolom pixels
					next_frame->macroblocks[mb_y*width+mb_x].luma[i][j] = fgetc(inputfile);

	// Chroma
	for (int mb_y = 0; mb_y < height; mb_y++)				// Voor elke rij macroblokken
		for (int i = 0; i < 8; i++)							// Voor elke rij pixels
			for (int mb_x = 0; mb_x < width; mb_x++)		// Voor elk macroblok
				for (int j = 0; j < 8; j++)					// Voor elke kolom pixels
					next_frame->macroblocks[mb_y*width+mb_x].cb[i][j] = fgetc(inputfile);

	// Chroma
	for (int mb_y = 0; mb_y < height; mb_y++)				// Voor elke rij macroblokken
		for (int i = 0; i < 8; i++)							// Voor elke rij pixels
			for (int mb_x = 0; mb_x < width; mb_x++)		// Voor elk macroblok
				for (int j = 0; j < 8; j++)					// Voor elke kolom pixels
					next_frame->macroblocks[mb_y*width+mb_x].cr[i][j] = fgetc(inputfile);

	if (feof(inputfile))
	{
		delete next_frame;
		return NULL;
	}

	return next_frame;
}
