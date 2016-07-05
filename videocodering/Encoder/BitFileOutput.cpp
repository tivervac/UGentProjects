#include "BitFileOutput.h"
#include <stdlib.h>

BitFileOutput::BitFileOutput(char* filename) : used_bits(0), bitbuffer(0)
{
	fopen_s(&outputfile, filename, "wb");

	if (outputfile==NULL) 
	{
		printf("Fout - kan outputbestand niet openen!\n");
		exit(0);
	}
}

BitFileOutput::~BitFileOutput()
{
	// Laatste byte uitschrijven
	while (used_bits != 0)
		WriteBit(0);

	fclose(outputfile);
	outputfile = NULL;
}

void BitFileOutput::WriteByte(byte b)
{
	fwrite(&b, 1, 1, outputfile);
}

void BitFileOutput::Write(byte* buffer, int length)
{
	fwrite(buffer, 1, length, outputfile);
}

void BitFileOutput::WriteBit(bool bit)
{
	bitbuffer <<= 1;
	bitbuffer |= (byte) bit;
	used_bits++;

	if (used_bits == 8)
	{
		WriteByte(bitbuffer);
		bitbuffer = 0;
		used_bits = 0;
	}
}

void BitFileOutput::WriteBits(int val, int bits)
{
	for (int i = bits-1; i >= 0; i--)
		WriteBit((val & (0x1 << i))!=0);
}

void BitFileOutput::flush()
{
	fflush(outputfile);
}