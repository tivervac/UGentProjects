#include "Quantiser.h"
#include <math.h>
#include <stdio.h>

// Breng wijzigingen aan in onderstaande methode
// Quantisatie van de luminanitie- en chrominantiecoefficienten.
// De quantisatiestap qp moet bij het encoderen verschillend zijn van nul aangezien deling door nul niet is toegestaan.
int Quantiser::sign(pixel i){
	return (i > 0) ? 1 : -1;
}
void Quantiser::Quantise(Macroblock *mb, int qp)
{
	//qp mag niet 0 zijn maar we willen wel Quantise uitvoeren
	qp++;
	//CB en CR zijn 8x8 matrices
	for(int i = 0; i < max; i++){
		for(int j = 0; j < max; j++){
			int cb = mb->cb[i][j];
			int cr = mb->cr[i][j];
			mb->cb[i][j] = (pixel) (sign(cb) * floor(abs(cb) / qp + 0.5));
			mb->cr[i][j] = (pixel) (sign(cr) * floor(abs(cr) / qp + 0.5));
		}
	}
	//Luma moeten we apart doen omdat het een 16x16 matrix is
	for (int i = 0; i < 2 * max; i++){
		for (int j = 0; j < 2 * max; j++){
			int luma = mb->luma[i][j];
			mb->luma[i][j] = (pixel) (sign(luma) * floor(abs(luma) / qp + 0.5));
		}
	}		
}