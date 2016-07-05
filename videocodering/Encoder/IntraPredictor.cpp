#include "IntraPredictor.h"
#include <math.h>
#include <stdio.h>
#include <stdlib.h>
#include "Config.h"

#define CALC_DIFF(x, y) ( x - y ) * ( x - y )
//#define CALC_DIFF(x, y) abs( x - y )

IntraPredictor::IntraPredictor()
{
	pixels_up_luma = new pixel[16];
	pixels_left_luma = new pixel[16];

	pixels_up_cb = new pixel[8];
	pixels_left_cb = new pixel[8];

	pixels_up_cr = new pixel[8];
	pixels_left_cr = new pixel[8];
}

IntraPredictor::~IntraPredictor()
{
	delete pixels_up_luma;
	delete pixels_left_luma;

	delete pixels_up_cb;
	delete pixels_left_cb;

	delete pixels_up_cr;
	delete pixels_left_cr;
}

void IntraPredictor::setCurrentFrame(Frame* frame)
{
	current_frame = frame;
}

// Breng wijzigingen aan in onderstaande methode
int IntraPredictor::predictIntra(int current_mb, int width, int height) // breedte en hoogte in aantal macroblokken
{
	// get current macroblock
	Macroblock* mb = current_frame->getMacroblock(current_mb);

	// Haal de predictiepixels op uit omliggende macroblokken (links, boven, linksboven)
	// Indien de pixels niet beschikbaar zijn, gebruik de waarde 128
	Macroblock * mbUp = NULL;
	Macroblock * mbLeft = NULL;
	Macroblock * mbUpLeft = NULL;
	if(current_mb >= width){
		mbUp = current_frame->getMacroblock(current_mb-width);
	}
	if(current_mb%width>0){
		mbLeft = current_frame->getMacroblock(current_mb-1);
	}
	if(mbUp != NULL && current_mb%current_frame->getWidth() > 0){
		mbUpLeft = current_frame->getMacroblock(current_mb-width-1);
	}
	for(int i=0; i<16; i++){
		pixels_up_luma[i] = mbUp?mbUp->luma[15][i]:128;
		pixels_left_luma[i] = mbLeft?mbLeft->luma[i][15]:128;
	}
	for(int i=0; i<8; i++){
		if(mbUp){
			pixels_up_cb[i] = mbUp->cb[7][i];
			pixels_up_cr[i] = mbUp->cr[7][i];
		} else{
			pixels_up_cb[i] = 128;
			pixels_up_cr[i] = 128;
		}
		if(mbLeft){
			pixels_left_cb[i] = mbLeft->cb[i][7];
			pixels_left_cr[i] = mbLeft->cr[i][7];
		} else{
			pixels_left_cb[i] = 128;
			pixels_left_cr[i] = 128;
		}
	}
	if(mbUpLeft){
		pixel_up_left_luma = mbUpLeft->luma[15][15];
		pixel_up_left_cr = mbUpLeft->cr[7][7];
		pixel_up_left_cb = mbUpLeft->cb[7][7];
	} else{
		pixel_up_left_luma=128;
		pixel_up_left_cr = 128;
		pixel_up_left_cb = 128;
	}


	// Evalueer de verschillende predictiemodes (op basis van de luma-component)
	int mode = 0;
	int avg = DCPrediction(pixels_up_luma, pixels_left_luma, 16);
	//calculate the difference between the prediction pixels and the real ones
	for(int i=0; i<16; i++){
		for(int j=0; j<16; j++){
			//0 for DC, 1 for vertical, 2 for horizontal and 3 for diagonal
			differences[0] += CALC_DIFF(mb->luma[i][j], avg);
			differences[1] += CALC_DIFF(mb->luma[i][j], pixels_up_luma[j]);
			differences[2] += CALC_DIFF(mb->luma[i][j], pixels_left_luma[i]);
			int diagonal;
			if(i<j) diagonal = pixels_up_luma[j-i-1];
			else if(i>j) diagonal = pixels_left_luma[i-j-1];
			else diagonal = pixel_up_left_luma;
			differences[3] += CALC_DIFF(mb->luma[i][j], diagonal);
		}
	}
	// Calculate the lowest
	for(int i=1; i<4; i++){
		if(differences[i] < differences[mode]) mode = i;
	}
	int cb = DCPrediction(pixels_up_cb, pixels_left_cb, 8);
	int cr = DCPrediction(pixels_up_cr, pixels_left_cr, 8);
	switch(mode)
	{
	case 0:
		for(int i=0; i<16; i++){
			for(int j=0; j<16; j++){
				mb->luma[i][j] -= avg;
			}
		}
		for(int i=0; i<8; i++){
			for(int j=0; j<8; j++){
				mb->cb[i][j] -= cb;
				mb->cr[i][j] -= cr;
			}
		}
		break;
	case 1:
		for(int i=0; i<16; i++){
			for(int j=0; j<16; j++){
				mb->luma[i][j] -= pixels_up_luma[j];
			}
		}
		for(int i=0; i<8; i++){
			for(int j=0; j<8; j++){
				mb->cb[i][j] -= pixels_up_cb[j];
				mb->cr[i][j] -= pixels_up_cr[j];
			}
		}
		break;
	case 2:
		for(int i=0; i<16; i++){
			for(int j=0; j<16; j++){
				mb->luma[i][j] -= pixels_left_luma[i];
			}
		}
		for(int i=0; i<8; i++){
			for(int j=0; j<8; j++){
				mb->cb[i][j] -= pixels_left_cb[i];
				mb->cr[i][j] -= pixels_left_cr[i];
			}
		}
		break;
	case 3:
		for(int i=0; i<16; i++){
			for(int j=0; j<16; j++){
				if(i<j) mb->luma[i][j] -= pixels_up_luma[j - i - 1];
				else if(i>j) mb->luma[i][j] -= pixels_left_luma[i - j - 1];
				else mb->luma[i][j] -= pixel_up_left_luma;
			}
		}
		for(int i=0; i<8; i++){
			for(int j=0; j<8; j++){
				if(i<j){
					mb->cb[i][j] -= pixels_up_cb[j-i-1];
					mb->cr[i][j] -= pixels_up_cr[j-i-1];
				} else if(i>j){
					mb->cb[i][j] -= pixels_left_cb[i-j-1];
					mb->cr[i][j] -= pixels_left_cr[i-j-1];
				} else{
					mb->cb[i][j] -= pixel_up_left_cb;
					mb->cr[i][j] -= pixel_up_left_cr;
				}
			}
		}
		break;
	default:
		break;
	}
	return mode; // Optimale mode als return-waarde
}
int IntraPredictor::DCPrediction(pixel* up, pixel* left, int n){
	int avg = 0;
	for(int i=0; i<n; i++){
		avg += (up[i] + left[i]); 
	}
	return avg/(2*n);
}