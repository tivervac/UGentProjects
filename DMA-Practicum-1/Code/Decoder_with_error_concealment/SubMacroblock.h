#ifndef SUBMACROBLOCK_H
#define SUBMACROBLOCK_H

#include "Macroblock.h"
#include "Frame.h"

class SubMacroblock
{
public:
	SubMacroblock();
	SubMacroblock(Macroblock* parent_macroblock, int submacroblock_width, int submacroblock_nr);
	SubMacroblock(Macroblock* parent_macroblock, int submacroblock_width, int x, int y);
	~SubMacroblock();

	Macroblock* parent_macroblock;
	int submacroblock_width; // width of a submacroblock in pixels
	// Sub macroblocks are indexed using the following pattern:
	//      0  1  2  3
	//      4  5  6  7
	//      8  9 10 11
	//     12 13 14 15
	int submacroblock_nr; // index of the submacroblock in the macroblock

	int get_submacroblock_x(); // x position of the submacroblock within the macroblock (in submacroblocks)
	int get_submacroblock_y(); 

	SubMacroblock get_submacroblock_above(Frame* frame);
	SubMacroblock get_submacroblock_below(Frame* frame);
	SubMacroblock get_submacroblock_left(Frame* frame);
	SubMacroblock get_submacroblock_right(Frame* frame);

	pixel get_luma(int x, int y);
	pixel get_cb(int x, int y);
	pixel get_cr(int x, int y);
};

#endif
