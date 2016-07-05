#ifndef ERRORCONCEALER_H
#define ERRORCONCEALER_H

#include "Frame.h"
#include <vector>

class ErrorConcealer
{
public:
	ErrorConcealer(short conceal_method);
	~ErrorConcealer(void);

	void concealErrors(Frame* frame, Frame* referenceFrame);

protected:
	short conceal_method;
	void conceal_spatial_1_old(Frame* frame);
	void conceal_spatial_1(Frame* frame);
	void conceal_spatial_2(Frame* frame, int version);
	void conceal_spatial_3(Frame* frame);
	void conceal_temporal_1(Frame* frame, Frame* referenceFrame);
	void conceal_temporal_2(Frame* frame, Frame* referenceFrame, int size);	
    void conceal_temporal_3(Frame* frame, Frame* referenceFrame, int size);
	void conceal_temporal_4(Frame* frame, Frame* referenceFrame);

private:
	typedef struct edge {
		short angle;
		std::vector<std::pair<int, int>> indices;
	};

	Macroblock* missing_to_null(Macroblock *mb);
	edge* sobel(Macroblock *mb, int starti, int startj, int endi, int endj);
};

#endif
