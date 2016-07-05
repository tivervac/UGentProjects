#include "Encoder.h"
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "YUVFileInput.h"
#include "BitFileOutput.h"
#include "MotionCompensator.h"
#include "IntraPredictor.h"
#include "DCTTransform.h"
#include "Quantiser.h"
#include "EntropyCoder.h"
#include "IQuantiser.h"
#include "IDCTTransform.h"
#include "IMotionCompensator.h"
#include "IIntraPredictor.h"
#include "Clipper.h"
#include "Config.h"


int Encoder::Encode(char *inputfile, int width, int height, int qp, int i_interval, char *outputfile)
{
	YUVFileInput in(inputfile, width/16, height/16);
	BitFileOutput out(outputfile);

	EntropyCoder entropy_coder(&out);
	DCTTransform dct;
	MotionCompensator mc(SEARCH_WIDTH, SEARCH_HEIGHT);
	IntraPredictor ip;
	
	printf("File:\t%s\nWidth:\t%d\nHeight:\t%d\nQP:\t%d\nI-interval:\t%d\nSearch window: \t%dx%d\n\n", inputfile, width, height, qp, i_interval, SEARCH_WIDTH, SEARCH_HEIGHT);

	// Schrijf de hoofding uit
	entropy_coder.CodeUInt(width/16);
	entropy_coder.CodeUInt(height/16);
	out.WriteBit(DO_INTRA);
	out.WriteBit(DO_MC);
	out.WriteBit(DO_DCT);
	out.WriteBit(DO_QT);

	if (DO_QT) entropy_coder.CodeUInt(qp);
	if (DO_MC) entropy_coder.CodeUInt(i_interval);

	// Decoder
	IDCTTransform idct;
	IMotionCompensator imc;
	IIntraPredictor iip;

	// Codeerlus
	Frame *current_frame = in.getNextFrame();
	int count = 0;

	while (current_frame != NULL)
	{
		bool i_frame = (count % i_interval == 0);
		bool p_frame = (count % i_interval != 0);

		printf("#%3d %c ", count++, p_frame?'P':'I');

		long used_bits = 0;
		int num_mb = current_frame->getNumMB();

		ip.setCurrentFrame(current_frame);
		iip.setCurrentFrame(current_frame);

		// Variabelen voor statistieken intravoorspelling
		int mode0 = 0, mode1 = 0, mode2 = 0, mode3 = 0;

		for (int current_mb = 0; current_mb < num_mb; current_mb++)
		{
			Macroblock *mb = current_frame->getMacroblock(current_mb);		// Get YUV macroblock

			mb->qp = qp;

			int mode = -1; // Intrapredictiemode

			if (DO_INTRA && i_frame)	mode = ip.predictIntra(current_mb, width/16, height/16); // Intrapredictie
			if (DO_MC && p_frame)		mc.motionCompensate(mb);				// Bewegingsestimatie en -compensatie
			if (DO_DCT)					dct.Transform(mb);						// DCT-transformatie
			if (DO_QT)					Quantiser::Quantise(mb, qp);			// Quantisatie
			
			used_bits += entropy_coder.EntropyCode(mb, p_frame, qp);		// Entropiecodering

			if (DO_INTRA && i_frame)	
			{
				entropy_coder.CodeIntraMode(mode);
				used_bits += 2; // 2 bits nodig om intrapredictiemode te signaleren in de bitstroom
			}

			// Codeerlus sluiten en referentiebeeld reconstrueren
			if (DO_QT)					IQuantiser::IQuantise(mb, qp);			// Invers quantiseren
			if (DO_DCT)					idct.ITransform(mb);					// Invers transformeren
			if (DO_INTRA && i_frame)	iip.predictIntra(current_mb, width/16, height/16, mode); // Intrapredictie
			if (DO_MC && p_frame)		imc.iMotionCompensate(mb);				// Bewegingscompensatie

			Clipper::Clip(mb);
		}

		printf("%8d\n", used_bits);

		// Referentie instellen naar huidige beeld
		delete mc.getReferenceFrame();
		mc.setReferenceFrame(current_frame);
		imc.setReferenceFrame(current_frame);

		// Nieuw beeld
		current_frame = in.getNextFrame();
	}

	// Geheugen vrijgeven
	delete mc.getReferenceFrame();

	printf("\nTotal: %8d\n", entropy_coder.getTotalUsedBits());

	return 0;
}


int main(int argc, char* argv[])
{
	Encoder enc;

	if (argc != 7)
	{
		printf("\nGEBRUIK:   encoder.exe <input_bestand> <input_breedte> <input_hoogte> <qp> <I-interval> <output_bestand>\n");
		printf("            <input_breedte> en <input_hoogte> worden uitgedrukt in aantal pixels\n");
		return 1;
	}
	
	enc.Encode(argv[1], atoi(argv[2]), atoi(argv[3]),atoi(argv[4]),atoi(argv[5]),argv[6]);

	return 0;
}
