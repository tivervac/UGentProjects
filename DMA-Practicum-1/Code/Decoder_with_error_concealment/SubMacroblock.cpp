#include "SubMacroblock.h"

SubMacroblock::SubMacroblock(){
	this->parent_macroblock = nullptr;
	this->submacroblock_nr = -1;
	this->submacroblock_width = -1;
}

SubMacroblock::SubMacroblock(Macroblock* parent_macroblock, int submacroblock_width, int submacroblock_nr){
	this->parent_macroblock = parent_macroblock;
	this->submacroblock_width = submacroblock_width;
	this->submacroblock_nr = submacroblock_nr;
}

SubMacroblock::SubMacroblock(Macroblock* parent_macroblock, int submacroblock_width, int x, int y){
	this->parent_macroblock = parent_macroblock;
	this->submacroblock_width = submacroblock_width;

	int submacroblocks_per_row = 16 / submacroblock_width;
	this->submacroblock_nr = y * submacroblocks_per_row + x;
}

SubMacroblock::~SubMacroblock(){

}

int SubMacroblock::get_submacroblock_x(){
	int submacroblocks_per_row = 16 / submacroblock_width;
	return submacroblock_nr % submacroblocks_per_row;
}

int SubMacroblock::get_submacroblock_y(){
	int submacroblocks_per_row = 16 / submacroblock_width;
	return submacroblock_nr / submacroblocks_per_row;
}

SubMacroblock SubMacroblock::get_submacroblock_above(Frame* frame){
	if (get_submacroblock_y() == 0){
		// this submacroblock lies on the top row of the macroblock. 
		// so the submacroblock above lies in another macroblock

		if (parent_macroblock->getYPos() != 0){
			// the parent macroblock does not lie on the top row of the frame,
			// so there is a macroblock above this macroblock
			int above_x = parent_macroblock->getXPos();
			int above_y = parent_macroblock->getYPos() - 1;
			Macroblock* above = frame->getMacroblock(above_y * 16 + above_x);

			// the submacroblock above this one has the same x coordinate, and lies on the bottom of the macroblock
			if (!above->isMissing()){
				int submacroblocks_per_row = 16 / submacroblock_width;
				int above_submacroblock_x = get_submacroblock_x();
				int above_submacroblock_y = submacroblocks_per_row - 1;

				return SubMacroblock(above, submacroblock_width, above_submacroblock_x, above_submacroblock_y);
			}
		}
	}
	else{
		// this submacroblock does not lie on the top row of the macroblock
		// so the submacroblock above lies within the same macroblock
		int submacroblock_above_x = get_submacroblock_x();
		int submacroblock_above_y = get_submacroblock_y() - 1;

		return SubMacroblock(parent_macroblock, submacroblock_width, submacroblock_above_x, submacroblock_above_y);
	}

	// In case there is no submacroblock above, return a null submacroblock
	return SubMacroblock();
}
SubMacroblock SubMacroblock::get_submacroblock_below(Frame* frame){
	int submacroblocks_per_row = 16 / submacroblock_width;
	if (get_submacroblock_y() == submacroblocks_per_row - 1){
		// the current submacroblock lies on the bottom row of the macroblock,
		// therefore the submacroblock below lies in another macroblock

		if (parent_macroblock->getYPos() != frame->getHeight() - 1){
			// parent macroblock does not lie on the bottom of the frame,
			// so there is a macroblock below it
			int mb_below_x = parent_macroblock->getXPos();
			int mb_below_y = parent_macroblock->getYPos() + 1;

			Macroblock* mb_below = frame->getMacroblock(mb_below_y * frame->getWidth() + mb_below_x);

			// the submacroblock below this one lies on the top row of the macroblock mb_below
			if (!mb_below->isMissing()){
				int smb_below_x = get_submacroblock_x();
				int smb_below_y = 0;

				return SubMacroblock(mb_below, submacroblock_width, smb_below_x, smb_below_y);
			}
		}

	}
	else{
		// the current submacroblock does not lie on the bottom row of the macroblock,
		// so there is a submacroblock below in the same macroblock

		int smb_below_x = get_submacroblock_x();
		int smb_below_y = get_submacroblock_y() + 1;

		return SubMacroblock(parent_macroblock, submacroblock_width, smb_below_x, smb_below_y);
	}

	// return an empty submacroblock if there is no submacroblock below
	return SubMacroblock();
}
SubMacroblock SubMacroblock::get_submacroblock_left(Frame* frame){
	int submacroblocks_per_row = 16 / submacroblock_width;
	if (get_submacroblock_x() == 0){
		// if this submacroblock lies on the first collumn of the macroblock,
		// the the submacroblock to the left lies within another macroblock

		if (parent_macroblock->getYPos() != 0){
			// the parent macroblock does not lie on the first collumn of the frame
			// so there is a macroblock to the left of this one
			int mb_left_x = parent_macroblock->getXPos() - 1;
			int mb_left_y = parent_macroblock->getYPos();

			Macroblock* mb_left = frame->getMacroblock(mb_left_y*frame->getWidth() + mb_left_x);

			// the submacroblock to the left lies on the right collumn of macroblock mb_left
			if (!mb_left->isMissing()){
				int smb_left_x = submacroblocks_per_row - 1;
				int smb_left_y = get_submacroblock_y();

				return SubMacroblock(mb_left, submacroblock_width, smb_left_x, smb_left_y);
			}
		}
	}
	else{
		// this submacroblock does not lie on the first collumn of the macroblock,
		// so the submacroblock to the left lies within the same macroblock

		int smb_left_x = get_submacroblock_x() - 1;
		int smb_left_y = get_submacroblock_y();

		return SubMacroblock(parent_macroblock, submacroblock_width, smb_left_x, smb_left_y);
	}

	// if no submacroblock exists to the left of this macroblock, then return an empty submacroblock
	return SubMacroblock();
}
SubMacroblock SubMacroblock::get_submacroblock_right(Frame* frame){
	int submacroblocks_per_row = 16 / submacroblock_width;
	if (get_submacroblock_x() == submacroblocks_per_row - 1){
		// this submacroblock lies on the right column of the current macroblock
		// so the submacroblock to the right must be found in another macroblock

		if (parent_macroblock->getXPos() != frame->getWidth() - 1){
			// the parent macroblock does not lie on the right column of the frame
			// so there is a macroblock to the right of it
			int mb_right_x = parent_macroblock->getXPos() + 1;
			int mb_right_y = parent_macroblock->getYPos();

			Macroblock* mb_right = frame->getMacroblock(mb_right_y*frame->getWidth() + mb_right_x);
			if (!mb_right->isMissing()){
				// the submacroblock to the right lies on the left collumn of mb_right
				int smb_right_x = 0;
				int smb_right_y = get_submacroblock_y();

				return SubMacroblock(mb_right, submacroblock_width, smb_right_x, smb_right_y);
			}
		}
	}
	else{
		// this submacroblock does not lie on the right column of the parent macroblock
		// so there is a submacroblock to the right withing the parent macroblock

		int smb_right_x = get_submacroblock_x() + 1;
		int smb_right_y = get_submacroblock_y();

		return SubMacroblock(parent_macroblock, submacroblock_width, smb_right_x, smb_right_y);
	}

	// if there is no submacroblock to the right, return a empty submacroblock
	return SubMacroblock();
}

pixel SubMacroblock::get_luma(int x, int y){
	return parent_macroblock->luma[(get_submacroblock_y() * submacroblock_width + y) % (16)][(get_submacroblock_x() * submacroblock_width + x) % 16];
}

pixel SubMacroblock::get_cb(int x, int y){
	return parent_macroblock->cb[(get_submacroblock_y() * submacroblock_width + y) % 8][(get_submacroblock_x() * submacroblock_width + x) % 8];
}

pixel SubMacroblock::get_cr(int x, int y){
	return parent_macroblock->cr[(get_submacroblock_x() * submacroblock_width + x) % 8][(get_submacroblock_y() * submacroblock_width + y) % 8];
}

