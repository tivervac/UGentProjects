#include "MotionCompensator.h"
#include <math.h>
#include <stdio.h>
#include <stdlib.h>
#include "Config.h"

#define CALC_DIFF(x, y) ( x - y ) * ( x - y )
//#define CALC_DIFF(x, y) abs( x - y )

MotionCompensator::MotionCompensator(int search_width, int search_height)
	: search_width(search_width), search_height(search_height), reference_frame(0)
{
	search_buffer = new pixel*[this->search_height+16];

	pixel *buffer = new pixel[(this->search_width+16)*(this->search_height+16)];
	for (int i = 0; i < this->search_height+16; i++)
		search_buffer[i] = buffer + i*(this->search_width+16);
}

MotionCompensator::~MotionCompensator()
{
	delete[] search_buffer[0];
	delete[] search_buffer;
}

void MotionCompensator::setReferenceFrame(Frame *frame)
{
	reference_frame = frame;
	ref_width = frame->getWidth();
	ref_height = frame->getHeight();
}

Frame *MotionCompensator::getReferenceFrame()
{
	return reference_frame;
}

// Breng wijzigingen aan in onderstaande methode
void MotionCompensator::motionCompensate(Macroblock *mb)
{
	////////////////////////
	// Bewegingsestimatie //
	////////////////////////

	// Construeer eerst het zoekvenster
	for(int i = 0; i < 16 + search_height; i++) {
		for(int j = 0; j < 16 + search_width; j++) {
			search_buffer[i][j] = GetRefPixelLuma((mb->getXPos()*16 - search_height/2) + j, (mb->getYPos()*16 - search_width/2) + i);
		}
	}

	// Zoek de optimale bewegingsvector
	// Sla de optimale vector op in mb->mv
	int minimum = -1;
	for(int i = 0; i < search_height; i++) {
		for(int j = 0; j < search_width; j++) {
			int energy = 0;
			for(int k = 0; k < 16 ; k++) {
				for(int l = 0; l < 16; l++) {
					energy += CALC_DIFF(mb->luma[k][l], search_buffer[i+k][j+l]);
				}
			}
			if (minimum == -1 || energy <= minimum) {
				minimum = energy;
				mb->mv.x = mb->getXPos() - j;
				mb->mv.y = mb->getYPos() - i;
			}
		}
	}
	
	/////////////////////////////////////////////////
	// Voer de eigenlijke bewegingscompensatie uit //
	/////////////////////////////////////////////////

	for(int i = 0; i < 16; i++) {
		for(int j = 0; j < 16; j++) {
			mb->luma[i][j] -= GetRefPixelLuma((mb->getXPos()*16 + mb->mv.x) + j, (mb->getYPos()*16 + mb->mv.y) + i);
		}
	}
	for(int i = 0; i < 8; i++) {
		for(int j = 0; j < 8; j++) {
			mb->cb[i][j] -= GetRefPixelCb(2*((mb->getXPos()*8 + mb->mv.x/2) + j), 2*((mb->getYPos()*8 + mb->mv.y/2) + i));
			mb->cr[i][j] -= GetRefPixelCr(2*((mb->getXPos()*8 + mb->mv.x/2) + j), 2*((mb->getYPos()*8 + mb->mv.y/2) + i));
		}
	}
}


// Onderstaande methoden worden gebruikt wanneer de search_buffer van een macroblock wordt opgesteld.
// Deze methoden gaan de te gebruiken waarden van het referentiebeeld ophalen.
// Randpixels worden gebruikt indien de motion vector naar pixels buiten beeld verwijst.

int MotionCompensator::GetRefPixelLuma(int x, int y)
{
	checkPixels(&x, &y);
	return reference_frame->getMacroblock((y/16)*ref_width + (x/16))->luma[y%16][x%16];
}

int MotionCompensator::GetRefPixelCb(int x, int y)
{
	checkPixels(&x, &y);
	return reference_frame->getMacroblock((y/16)*ref_width + (x/16))->cb[(y / 2)%8][(x / 2)%8];
}

int MotionCompensator::GetRefPixelCr(int x, int y)
{
	checkPixels(&x, &y);
	return reference_frame->getMacroblock((y/16)*ref_width + (x/16))->cr[(y/2)%8][(x/2)%8];
}

void MotionCompensator::checkPixels(int* x, int* y){
	if(*x < 0) *x = 0;
	else if(*x >= ref_width*16) *x = ref_width*16 - 1;
	if(*y < 0) *y = 0;
	else if(*y >= ref_height*16) *y = ref_height*16 - 1;
}