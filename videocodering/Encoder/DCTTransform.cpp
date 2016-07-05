#include "DCTTransform.h"
#define _USE_MATH_DEFINES
#include <math.h>
#include <stdio.h>

// Breng wijzigingen aan in onderstaande methode
// Initialiseren van de matrix A. Maak hiervoor gebruik van de formule 3 in de opgave
DCTTransform::DCTTransform()
{
	for (int i = 0; i < max; i++){
		for(int j = 0; j < max; j++){
			double c_i = (i == 0) ? sqrt((double) 1 / max) : sqrt((double) 2 / max);
			c[i][j] = c_i * cos((2*j + 1)* i * M_PI / (2 * max));
		}
	}
}

void DCTTransform::Transform(Macroblock *mb)
{
	mb->state = DCT;

	// Luma
	Transform_8x8(mb->luma, 0, 0);
	Transform_8x8(mb->luma, 8, 0);
	Transform_8x8(mb->luma, 0, 8);
	Transform_8x8(mb->luma, 8, 8);

	// Chroma
	Transform_8x8(mb->cb, 0, 0);
	Transform_8x8(mb->cr, 0, 0);
}

// Breng wijzigingen aan in onderstaande methode
// Uitvoeren van de DCT op een 8x8 blok. De offsets worden gebruikt om de plaats van de luminantiecoefficienten aan te duiden.
void DCTTransform::Transform_8x8(pixel **block, int offset_x, int offset_y)
{
	//We moeten een tijdelijke matrix gebruiken omdat we block nog nodig hebben
	pixel tmp[8][8];

	//Pas regel 1 toe, dit moet in twee keer, eerst bereken we A * X
	for(int i = 0; i < max; i++) {
		for(int j = 0; j < max; j++) {
			double sub_total = 0;
			for(int r = 0; r < 8; r++) {
				sub_total += c[i][r] * block[offset_y + r][offset_x + j];
			}
			tmp[i][j] = (pixel) sub_total;
		}
	}

	//Nu passen we het tweede deel toe, dit is (A * X) * A^T
	for (int i = 0; i < max; i++){
		for (int j = 0; j < max; j++){
			double sub_total = 0;
			for (int r = 0; r < max; r++){
				//We moeten c niet transponeren, we kunnen hem ook gewoon getransponeerd inlezen
				sub_total += tmp[i][r] * c[j][r];
			}
			block[offset_y + i][offset_x + j] = (pixel) sub_total;
		}
	}
}