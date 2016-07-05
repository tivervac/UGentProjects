#include "ErrorConcealer.h"
#include "MacroblockEmpty.h"
#include "SubMacroblock.h"
#include <stdio.h>
#include <math.h>
#include <list>
#include <utility>
#include <iostream>
#include <ctime>
#include <algorithm>
#include <assert.h> 

#define PI	3.14159265359

ErrorConcealer::ErrorConcealer(short conceal_method)
{
	this->conceal_method = conceal_method;
}

ErrorConcealer::~ErrorConcealer(void)
{
}

void ErrorConcealer::concealErrors(Frame *frame, Frame *referenceFrame)
{
	switch(conceal_method){
		case 0:
			conceal_spatial_1(frame);
			break;
		case 1:
			conceal_spatial_2(frame,1);
			break;
		case 2:
			conceal_spatial_3(frame);
			break;
		case 3:
			conceal_temporal_1(frame, referenceFrame);
			break;
		case 4:
			conceal_temporal_2(frame, referenceFrame, 1);
			break;
		case 5:
			conceal_temporal_2(frame, referenceFrame, 2);
			break;
		case 6:
			conceal_temporal_2(frame, referenceFrame, 3);
			break;
		case 7:
			conceal_temporal_2(frame, referenceFrame, 4);
			break;
		case 8:
			conceal_temporal_3(frame, referenceFrame, 4);
			break;
		case 9:
			// To be completed. Add explanatory notes in English.
			conceal_temporal_4(frame, referenceFrame);
			break;
		case 10:
			conceal_spatial_1_old(frame);
			break;
		case 11:
			conceal_spatial_2(frame,0);
			break;
		default:
			printf("\nWARNING: NO ERROR CONCEALMENT PERFORMED! (conceal_method %d unknown)\n\n",conceal_method);
	}
}

void ErrorConcealer::conceal_spatial_1_old(Frame *frame)
{
	printf("Conceal spatial 1 old version\n");
	int numMB = frame->getNumMB();
	Macroblock* MB;
	Macroblock* MB_l;
	Macroblock* MB_r;
	Macroblock* MB_t;
	Macroblock* MB_b;
	MacroblockEmpty* MBEmpty = new MacroblockEmpty();
	int exist_t = 1;
	int exist_b = 1;
	int exist_r = 1;
	int exist_l = 1;
	for (int MBx = 0; MBx < numMB; ++MBx)
	{
		MB = frame->getMacroblock(MBx);
		exist_t = 1;
		exist_b = 1;
		exist_r = 1;
		exist_l = 1;
		if (MB->isMissing())
		{
			//determine MB_l
			if (MB->getXPos() == 0){
				MB_l = MBEmpty;
				exist_l = 0;
			}
			else{
				MB_l = frame->getMacroblock(MBx - 1);
			}
			//determine MB_r
			if (MB->getXPos() == frame->getWidth() - 1){
				MB_r = MBEmpty;
				exist_r = 0;
			}
			else{
				MB_r = frame->getMacroblock(MBx + 1);
			}
			//determine MB_t
			if (MB->getYPos() == 0){
				MB_t = MBEmpty;
				exist_t = 0;
			}
			else{
				MB_t = frame->getMacroblock(MBx - frame->getWidth());
			}
			//determine MB_b
			if (MB->getYPos() == frame->getHeight() - 1){
				MB_b = MBEmpty;
				exist_b = 0;
			}
			else{
				MB_b = frame->getMacroblock(MBx + frame->getWidth());
			}

			//Spatial interpolate pixels
			for (int i = 0; i < 16; ++i)	{
				for (int j = 0; j < 16; ++j)		{
					//calculate value
					MB->luma[i][j] = ((17 - j - 1)*MB_l->luma[i][15] * exist_l + 
						(j + 1)*MB_r->luma[i][0] * exist_r + 
						(17 - i - 1)*MB_t->luma[15][j] * exist_t + 
						(i + 1)*MB_b->luma[0][j] * exist_b ) 
						/ ( 
						( (17 - j - 1) * exist_l ) + 
						( (j + 1) * exist_r ) +
						( (17 - i - 1) * exist_t ) + 
						( (i + 1) * exist_b )
						);					
				}
			}
			for (int i = 0; i < 8; ++i)	{
				for (int j = 0; j < 8; ++j)		{
					MB->cb[i][j] = ((9 - j - 1)*MB_l->cb[i][7] * exist_l  + 
						(j + 1)*MB_r->cb[i][0] * exist_r + 
						(9 - i - 1)*MB_t->cb[7][j] *exist_t + 
						(i + 1)*MB_b->cb[0][j] * exist_b ) 
						/ ( 
						( (9 - j - 1) * exist_l ) + 
						( (j + 1) * exist_r ) +
						( (9 - i - 1) * exist_t ) + 
						( (i + 1) * exist_b )
						);
					MB->cr[i][j] = ((9 - j - 1)*MB_l->cr[i][7] * exist_l + 
						(j + 1)*MB_r->cr[i][0] * exist_r + 
						(9 - i - 1)*MB_t->cr[7][j] * exist_t + 
						(i + 1)*MB_b->cr[0][j] * exist_b ) 
						/ ( 
						( (9 - j - 1) * exist_l ) + 
						( (j + 1) * exist_r ) +
						( (9 - i - 1) * exist_t) + 
						( (i + 1) * exist_b)
						);
				}
			}
			MB->setConcealed();

		}
	}
	delete MBEmpty;
}

void ErrorConcealer::conceal_spatial_1(Frame *frame)
{
	int numMB = frame->getNumMB();
	Macroblock* MB;
	Macroblock* MB_l;
	Macroblock* MB_r;
	Macroblock* MB_t;
	Macroblock* MB_b;
	MacroblockEmpty* MBEmpty = new MacroblockEmpty();
	int exist_t = 1;
	int exist_b = 1;
	int exist_r = 1;
	int exist_l = 1;
	int l_taken = 0;
	int r_taken = 0;
	int t_taken = 0;
	int b_taken = 0;
	int *distances = new int[4]; // T,B,L,R
	for (int MBx = 0; MBx < numMB; ++MBx)
	{
		MB = frame->getMacroblock(MBx);
		exist_t = 1;
		exist_b = 1;
		exist_r = 1;
		exist_l = 1;
		if (MB->isMissing())
		{
			//determine MB_l
			if (MB->getXPos() == 0){
				MB_l = MBEmpty;
				exist_l = 0;
			}
			else{
				MB_l = frame->getMacroblock(MBx - 1);
			}
			//determine MB_r
			if (MB->getXPos() == frame->getWidth() - 1){
				MB_r = MBEmpty;
				exist_r = 0;
			}
			else{
				MB_r = frame->getMacroblock(MBx + 1);
			}
			//determine MB_t
			if (MB->getYPos() == 0){
				MB_t = MBEmpty;
				exist_t = 0;
			}
			else{
				MB_t = frame->getMacroblock(MBx - frame->getWidth());
			}
			//determine MB_b
			if (MB->getYPos() == frame->getHeight() - 1){
				MB_b = MBEmpty;
				exist_b = 0;
			}
			else{
				MB_b = frame->getMacroblock(MBx + frame->getWidth());
			}

			//Spatial interpolate pixels
			for (int i = 0; i < 16; ++i)	{
				distances[0] = exist_t? i : 16; //16 = MAX_VALUE + 1
				distances[1] = exist_b? 15-i : 16;
				for (int j = 0; j < 16; ++j)		{
					distances[2] = exist_l? j : 16;
					distances[3] = exist_r? 15-j : 16;
					//determine indexes of the 2 lowest values
					int index1=4;
					int index2=4;
					for(int k=0; k<4; k++){
						if (index1==4 || distances[k] <= distances[index1]){
							index2 = index1;
							index1 = k;
						}else if(index2==4 || distances[k] <= distances[index2]){
							index2 = k;
						}
					}
					//set booleans for which pixels have to be taken for calculation
					t_taken = 0;
					b_taken = 0;
					l_taken = 0;
					r_taken = 0;
					if(index1 == 0 || index2 == 0){
						t_taken = 1;
					}
					if(index1 == 1 || index2 == 1){
						b_taken = 1;
					}
					if(index1 == 2 || index2 == 2){
						l_taken = 1;
					}
					if(index1 == 3 || index2 == 3){
						r_taken = 1;
					}
					//calculate value
					MB->luma[i][j] = ((17 - j - 1)*MB_l->luma[i][15] * exist_l * l_taken + 
						(j + 1)*MB_r->luma[i][0] * exist_r * r_taken+ 
						(17 - i - 1)*MB_t->luma[15][j] * exist_t * t_taken+ 
						(i + 1)*MB_b->luma[0][j] * exist_b * b_taken) 
						/ ( 
						( (17 - j - 1) * exist_l * l_taken) + 
						( (j + 1) * exist_r * r_taken) +
						( (17 - i - 1) * exist_t * t_taken) + 
						( (i + 1) * exist_b * b_taken)
						);					
				}
			}
			for (int i = 0; i < 8; ++i)	{
				distances[0] = exist_t? i : 8;
				distances[1] = exist_b? 7-i : 8;
				for (int j = 0; j < 8; ++j)		{
					distances[2] = exist_l? j : 8;
					distances[3] = exist_r? 7-j : 8;
					//determine indexes of the 2 lowest values
					int index1=4; //= MAX_VALUE + 1
					int index2=4;
					for(int k=0; k<4; k++){
						if (index1==4 || distances[k] <= distances[index1]){
							index2 = index1;
							index1 = k;
						}else if(index2==4 || distances[k] <= distances[index2]){
							index2 = k;
						}
					}
					//set booleans for which pixels have to be taken for calculation
					t_taken = 0;
					b_taken = 0;
					l_taken = 0;
					r_taken = 0;
					if(index1 == 0 || index2 == 0){
						t_taken = 1;
					}
					if(index1 == 1 || index2 == 1){
						b_taken = 1;
					}
					if(index1 == 2 || index2 == 2){
						l_taken = 1;
					}
					if(index1 == 3 || index2 == 3){
						r_taken = 1;
					}
					MB->cb[i][j] = ((9 - j - 1)*MB_l->cb[i][7] * exist_l * l_taken + 
						(j + 1)*MB_r->cb[i][0] * exist_r * r_taken+ 
						(9 - i - 1)*MB_t->cb[7][j] *exist_t * t_taken + 
						(i + 1)*MB_b->cb[0][j] * exist_b * b_taken) 
						/ ( 
						( (9 - j - 1) * exist_l * l_taken) + 
						( (j + 1) * exist_r * r_taken) +
						( (9 - i - 1) * exist_t * t_taken) + 
						( (i + 1) * exist_b * b_taken)
						);
					MB->cr[i][j] = ((9 - j - 1)*MB_l->cr[i][7] * exist_l * l_taken+ 
						(j + 1)*MB_r->cr[i][0] * exist_r * r_taken+ 
						(9 - i - 1)*MB_t->cr[7][j] * exist_t * t_taken+ 
						(i + 1)*MB_b->cr[0][j] * exist_b * b_taken) 
						/ ( 
						( (9 - j - 1) * exist_l * l_taken) + 
						( (j + 1) * exist_r * r_taken) +
						( (9 - i - 1) * exist_t * t_taken) + 
						( (i + 1) * exist_b * b_taken)
						);
				}
			}
			MB->setConcealed();

		}
	}
	delete MBEmpty;
}


enum MBSTATE { OK, MISSING, CONCEALED };

void f_old(Macroblock* MB, 
	   int* exist_l, int* exist_r, int* exist_t, int* exist_b, 
	   MBSTATE* MBstate, 
	   int MBx, 
	   Frame *frame){
		   Macroblock* MB_l ;
		   Macroblock* MB_r ;
		   Macroblock* MB_t ;
		   Macroblock* MB_b ;
		   MacroblockEmpty* MBEmpty = new MacroblockEmpty();
		   //determine MB_l
		   if (MB->getXPos() == 0){//blocks on the left side, these have no left neighbour
			   MB_l = MBEmpty;
			   *exist_l = 0;
		   }
		   else{//if the left neighbour exist, there is a chance this block is missing
			   if (MBstate[MBx - 1] == MISSING){
				   MB_l = MBEmpty;
				   *exist_l = 0;
			   }
			   else{
				   MB_l = frame->getMacroblock(MBx - 1);
			   }
		   }
		   //determine MB_r
		   if (MB->getXPos() == frame->getWidth() - 1){
			   MB_r = MBEmpty;
			   *exist_r = 0;
		   }
		   else{
			   if (MBstate[MBx + 1] == MISSING){
				   MB_r = MBEmpty;
				   *exist_r = 0;
			   }
			   else{
				   MB_r = frame->getMacroblock(MBx + 1);
			   }
		   }
		   //determine MB_t
		   if (MB->getYPos() == 0){
			   MB_t = MBEmpty;
			   *exist_t = 0;
		   }
		   else{
			   if (MBstate[MBx - frame->getWidth()] == MISSING){
				   MB_t = MBEmpty;
				   *exist_t = 0;
			   }
			   else{
				   MB_t = frame->getMacroblock(MBx - frame->getWidth());
			   }
		   }
		   //determine MB_b
		   if (MB->getYPos() == frame->getHeight() - 1){
			   MB_b = MBEmpty;
			   *exist_b = 0;
		   }
		   else{
			   if (MBstate[MBx + frame->getWidth()] == MISSING){
				   MB_b = MBEmpty;
				   *exist_b = 0;
			   }
			   else{
				   MB_b = frame->getMacroblock(MBx + frame->getWidth());
			   }
		   }

		   if (*exist_l + *exist_r + *exist_t + *exist_b > 0){ //if the block has minimum 1 neighbour available
			   //Spatial interpolate pixels
			   for (int i = 0; i < 16; ++i)	{
				   for (int j = 0; j < 16; ++j)		{
					   MB->luma[i][j] = ((17 - j - 1)*MB_l->luma[i][15] * (*exist_l)  + 
						   (j + 1)*MB_r->luma[i][0] * (*exist_r) + 
						   (17 - i - 1)*MB_t->luma[15][j] * (*exist_t) + 
						   (i + 1)*MB_b->luma[0][j] * (*exist_b) ) 
						   / ( 
						   ( (17 - j - 1) * (*exist_l) ) + 
						   ( (j + 1) * (*exist_r) ) +
						   ( (17 - i - 1) * (*exist_t) ) + 
						   ( (i + 1) * (*exist_b) )
						   );					
				   }
			   }
			   for (int i = 0; i < 8; ++i)	{
				   for (int j = 0; j < 8; ++j)	{
					   MB->cb[i][j] = ((9 - j - 1)*MB_l->cb[i][7] * *exist_l + 
						   (j + 1)*MB_r->cb[i][0] * *exist_r + 
						   (9 - i - 1)*MB_t->cb[7][j] * *exist_t + 
						   (i + 1)*MB_b->cb[0][j] * *exist_b ) 
						   / ( 
						   ( (9 - j - 1) * *exist_l ) + 
						   ( (j + 1) * *exist_r ) +
						   ( (9 - i - 1) * *exist_t ) + 
						   ( (i + 1) * *exist_b )
						   );
					   MB->cr[i][j] = ((9 - j - 1)*MB_l->cr[i][7] * *exist_l + 
						   (j + 1)*MB_r->cr[i][0] * *exist_r + 
						   (9 - i - 1)*MB_t->cr[7][j] * *exist_t + 
						   (i + 1)*MB_b->cr[0][j] * *exist_b ) 
						   / ( 
						   ( (9 - j - 1) * *exist_l ) + 
						   ( (j + 1) * *exist_r ) +
						   ( (9 - i - 1) * *exist_t ) + 
						   ( (i + 1) * *exist_b )
						   );
				   }
			   }
		   }
		   delete MBEmpty;
}

void f(Macroblock* MB, 
	   int* exist_l, int* exist_r, int* exist_t, int* exist_b, 
	   MBSTATE* MBstate, 
	   int MBx, 
	   Frame *frame){
		   Macroblock* MB_l ;
		   Macroblock* MB_r ;
		   Macroblock* MB_t ;
		   Macroblock* MB_b ;
		   MacroblockEmpty* MBEmpty = new MacroblockEmpty();
		   int* distances = new int[4];
		   int l_taken = 0;
		   int r_taken = 0;
		   int t_taken = 0;
		   int b_taken = 0;
		   //determine MB_l
		   if (MB->getXPos() == 0){//blocks on the left side, these have no left neighbour
			   MB_l = MBEmpty;
			   *exist_l = 0;
		   }
		   else{//if the left neighbour exist, there is a chance this block is missing
			   if (MBstate[MBx - 1] == MISSING){
				   MB_l = MBEmpty;
				   *exist_l = 0;
			   }
			   else{
				   MB_l = frame->getMacroblock(MBx - 1);
			   }
		   }
		   //determine MB_r
		   if (MB->getXPos() == frame->getWidth() - 1){
			   MB_r = MBEmpty;
			   *exist_r = 0;
		   }
		   else{
			   if (MBstate[MBx + 1] == MISSING){
				   MB_r = MBEmpty;
				   *exist_r = 0;
			   }
			   else{
				   MB_r = frame->getMacroblock(MBx + 1);
			   }
		   }
		   //determine MB_t
		   if (MB->getYPos() == 0){
			   MB_t = MBEmpty;
			   *exist_t = 0;
		   }
		   else{
			   if (MBstate[MBx - frame->getWidth()] == MISSING){
				   MB_t = MBEmpty;
				   *exist_t = 0;
			   }
			   else{
				   MB_t = frame->getMacroblock(MBx - frame->getWidth());
			   }
		   }
		   //determine MB_b
		   if (MB->getYPos() == frame->getHeight() - 1){
			   MB_b = MBEmpty;
			   *exist_b = 0;
		   }
		   else{
			   if (MBstate[MBx + frame->getWidth()] == MISSING){
				   MB_b = MBEmpty;
				   *exist_b = 0;
			   }
			   else{
				   MB_b = frame->getMacroblock(MBx + frame->getWidth());
			   }
		   }

		   if (*exist_l + *exist_r + *exist_t + *exist_b > 0){ //if the block has minimum 1 neighbour available
			   //Spatial interpolate pixels
			   for (int i = 0; i < 16; ++i)	{
			   		distances[0] = *exist_t? i : 16; //16 = MAX_VALUE + 1
					distances[1] = *exist_b? 15-i : 16;
				   for (int j = 0; j < 16; ++j)		{
				   		distances[2] = *exist_l? j : 16;
						distances[3] = *exist_r? 15-j : 16;
						//determine indexes of the 2 lowest values
						int index1=4; //= MAX_VALUE + 1
						int index2=4;
						for(int k=0; k<4; k++){
							if (index1==4 || distances[k] <= distances[index1]){
								index2 = index1;
								index1 = k;
							}else if(index2==4 || distances[k] <= distances[index2]){
								index2 = k;
							}
						}
						//set booleans for which pixels have to be taken for calculation
						t_taken = 0;
						b_taken = 0;
						l_taken = 0;
						r_taken = 0;
						if((index1 == 0 || index2 == 0) && *exist_t){
							t_taken = 1;
						}
						if((index1 == 1 || index2 == 1) && *exist_b){
							b_taken = 1;
						}
						if((index1 == 2 || index2 == 2) && *exist_l){
							l_taken = 1;
						}
						if((index1 == 3 || index2 == 3) && *exist_r){
							r_taken = 1;
						}
						//printf("%d %d %d %d\n", t_taken, b_taken, l_taken, r_taken);
					   MB->luma[i][j] = ((17 - j - 1)*MB_l->luma[i][15] * (*exist_l) * l_taken+ 
						   (j + 1)*MB_r->luma[i][0] * (*exist_r) * r_taken+ 
						   (17 - i - 1)*MB_t->luma[15][j] * (*exist_t) * t_taken+ 
						   (i + 1)*MB_b->luma[0][j] * (*exist_b) * b_taken) 
						   / ( 
						   ( (17 - j - 1) * (*exist_l) * l_taken) + 
						   ( (j + 1) * (*exist_r) * r_taken) +
						   ( (17 - i - 1) * (*exist_t) * t_taken) + 
						   ( (i + 1) * (*exist_b) * b_taken)
						   );					
				   }
			   }
			   for (int i = 0; i < 8; ++i)	{
				   	distances[0] = *exist_t? i : 8;
					distances[1] = *exist_b? 7-i : 8;
				   for (int j = 0; j < 8; ++j)		{
					   	distances[2] = *exist_l? j : 8;
						distances[3] = *exist_r? 7-j : 8;
						//determine indexes of the 2 lowest values
						int index1=4; //= MAX_VALUE + 1
						int index2=4;
						for(int k=0; k<4; k++){
							if (index1==4 || distances[k] <= distances[index1]){
								index2 = index1;
								index1 = k;
							}else if(index2==4 || distances[k] <= distances[index2]){
								index2 = k;
							}
						}
						//set booleans for which pixels have to be taken for calculation
						t_taken = 0;
						b_taken = 0;
						l_taken = 0;
						r_taken = 0;
						if((index1 == 0 || index2 == 0) && *exist_t){
							t_taken = 1;
						}
						if((index1 == 1 || index2 == 1) && *exist_b){
							b_taken = 1;
						}
						if((index1 == 2 || index2 == 2) && *exist_l){
							l_taken = 1;
						}
						if((index1 == 3 || index2 == 3) && *exist_r){
							r_taken = 1;
						}
					   MB->cb[i][j] = ((9 - j - 1)*MB_l->cb[i][7] * *exist_l * l_taken+ 
						   (j + 1)*MB_r->cb[i][0] * *exist_r * r_taken+ 
						   (9 - i - 1)*MB_t->cb[7][j] * *exist_t * t_taken+ 
						   (i + 1)*MB_b->cb[0][j] * *exist_b * b_taken) 
						   / ( 
						   ( (9 - j - 1) * *exist_l * l_taken) + 
						   ( (j + 1) * *exist_r * r_taken) +
						   ( (9 - i - 1) * *exist_t * t_taken) + 
						   ( (i + 1) * *exist_b * b_taken)
						   );
					   MB->cr[i][j] = ((9 - j - 1)*MB_l->cr[i][7] * *exist_l * l_taken+ 
						   (j + 1)*MB_r->cr[i][0] * *exist_r * r_taken+ 
						   (9 - i - 1)*MB_t->cr[7][j] * *exist_t * t_taken+ 
						   (i + 1)*MB_b->cr[0][j] * *exist_b * b_taken) 
						   / ( 
						   ( (9 - j - 1) * *exist_l * l_taken) + 
						   ( (j + 1) * *exist_r * r_taken) +
						   ( (9 - i - 1) * *exist_t * t_taken) + 
						   ( (i + 1) * *exist_b * b_taken)
						   );
				   }
			   }
		   }
		   delete MBEmpty;
}
//version == 0 => old version (f_old)
//version == 1 => new version (f)
void ErrorConcealer::conceal_spatial_2(Frame *frame, int version)
{
	if (version == 0){
		printf("Conceal spatial 2 old version\n");
	}
	int numMB = frame->getNumMB();
	Macroblock* MB;
	int exist_t = 1;
	int exist_b = 1;
	int exist_r = 1;
	int exist_l = 1;
	int MBsConcealedL1 = 1;
	int MBsConcealedL1L2 = 1;
	int nrOfMBsMissing = 0;
	int totalConcealed = 0;
	MBSTATE* MBstate = new MBSTATE[numMB];
	//determine which macroblocks are missing
	//MISSING: macroblock is missing
	//OK: macroblock is not missing
	for (int MBx = 0; MBx < numMB; ++MBx){
		if (frame->getMacroblock(MBx)->isMissing()){
			MBstate[MBx] = MISSING;
			++nrOfMBsMissing;
		}
		else{
			MBstate[MBx] = OK;
		}
	}
	int loop = 0;
	while (nrOfMBsMissing > 0){//executed only if there is at least 1 macroblock missing

		MBsConcealedL1L2 = 1;
		while (MBsConcealedL1L2 > 0){
			MBsConcealedL1 = 1;
			MBsConcealedL1L2 = 0;
			while (MBsConcealedL1 > 0){
				MBsConcealedL1 = 0;
				for (int MBx = 0; MBx < numMB; ++MBx)
				{
					MB = frame->getMacroblock(MBx);
					exist_t = 1;
					exist_b = 1;
					exist_r = 1;
					exist_l = 1;
					if (MBstate[MBx] == MISSING)
					{
						if(version == 0){
							f_old(MB, &exist_l, &exist_r, &exist_t, &exist_b,
								MBstate, MBx, frame);
						}else{
							f( MB, &exist_l,  &exist_r,  &exist_t,  &exist_b, 
								MBstate,MBx,frame);
						}
						if (exist_l + exist_r + exist_t + exist_b > 2){//if macroblock has 3 or 4 neighbours available
							MB->setConcealed();
							++MBsConcealedL1;
							++MBsConcealedL1L2;
							--nrOfMBsMissing;
							++totalConcealed;
							MBstate[MBx] = CONCEALED;
						}
					}
				}
				//convert all CONCEALED to OK
				for (int MBx = 0; MBx < numMB; ++MBx){
					if (MBstate[MBx] == CONCEALED){
						MBstate[MBx] = OK;						
					}					
				}
			}
			bool oneMBConcealed = false;
			for (int MBx = 0; MBx < numMB && !oneMBConcealed; ++MBx)
			{
				MB = frame->getMacroblock(MBx);
				exist_t = 1;
				exist_b = 1;
				exist_r = 1;
				exist_l = 1;
				if (MBstate[MBx] == MISSING)
				{
					f( MB, &exist_l,  &exist_r,  &exist_t,  &exist_b, 
						MBstate,MBx,frame);
					if (exist_l + exist_r + exist_t + exist_b > 1){
						MB->setConcealed();
						--nrOfMBsMissing;
						++totalConcealed;
						MBstate[MBx] = CONCEALED;
						oneMBConcealed = true;
						++MBsConcealedL1L2;						
					}
				}
			}
			//convert all CONCEALED to OK
			for (int MBx = 0; MBx < numMB; ++MBx){
				if (MBstate[MBx] == CONCEALED){
					MBstate[MBx] = OK;						
				}					
			}
		}		
		bool oneMBConcealed = false;
		for (int MBx = 0; MBx < numMB && !oneMBConcealed; ++MBx)
		{
			MB = frame->getMacroblock(MBx);
			exist_t = 1;
			exist_b = 1;
			exist_r = 1;
			exist_l = 1;
			if (MBstate[MBx] == MISSING)
			{
				f( MB, &exist_l,  &exist_r,  &exist_t,  &exist_b, 
					MBstate,MBx,frame);
				if (exist_l + exist_r + exist_t + exist_b > 0){
					MB->setConcealed();
					--nrOfMBsMissing;
					++totalConcealed;
					MBstate[MBx] = CONCEALED;
					oneMBConcealed = true;
					++MBsConcealedL1L2;					
				}
			}
		}
		//convert all CONCEALED to OK
		for (int MBx = 0; MBx < numMB; ++MBx){
			if (MBstate[MBx] == CONCEALED){
				MBstate[MBx] = OK;						
			}					
		}
	}
}


void ErrorConcealer::conceal_spatial_3(Frame *frame)
{
	std::clock_t start;
	start = std::clock();
	for (int i = 0; i < frame->getNumMB(); i++) {
		Macroblock *mb = frame->getMacroblock(i);
		if (!mb->isMissing()){
			continue;
		}

		int width = frame->getWidth();
		// Get the neighbouring macroblocks of the missing block
		Macroblock *top = (i - width >= 0) ? missing_to_null(frame->getMacroblock(i - width)) : NULL;
		Macroblock *below = (i + width < frame->getNumMB()) ? missing_to_null(frame->getMacroblock(i + width)) : NULL;
		Macroblock *left = (i % width != 0) ? missing_to_null(frame->getMacroblock(i - 1)) : NULL;
		Macroblock *right = (i % width != width - 1) ? missing_to_null(frame->getMacroblock(i + 1)) : NULL;

		// Only take the 3 rows/columns next to the missing block
		edge *top_edge = sobel(top, 13, 0, 16, 16);
		edge *below_edge = sobel(below, 0, 0, 3, 16);
		edge *left_edge = sobel(left, 0, 13, 16, 16);
		edge *right_edge = sobel(right, 0, 0, 16, 3);
		
		// Cleanup
		if (top_edge != NULL) delete top_edge;
		if (below_edge != NULL) delete below_edge;
		if (left_edge != NULL) delete left_edge;
		if (right_edge != NULL) delete right_edge;
	}
	std::cout << "Frame reconstructed with edge detection in " << (std::clock() - start) / (double)(CLOCKS_PER_SEC / 1000) << "ms CPU time.\n";
}

/*
* This helper function returns null if the given macroblock is missing, else it return the macroblock
*/
Macroblock* ErrorConcealer::missing_to_null(Macroblock *mb) {
	if (mb->isMissing()){
		return NULL;
	}
	else {
		return mb;
	}
}

short horizontal_sobel[3][3] = {
		{ -1, 0, 1 },
		{ -2, 0, 2 },
		{ -1, 0, 1 }
};

short vertical_sobel[3][3] = {
		{ 1, 2, 1 },
		{ 0, 0, 0 },
		{ -1, -2, -1 }
};


/*
* Apply sobel to the luma component of mb
*/
ErrorConcealer::edge* ErrorConcealer::sobel(Macroblock *mb, int starti, int startj, int endi, int endj){
	if (mb == NULL){
		return NULL;
	}

	// The 8 wind directions 45°, 90°, 135°, ...
	int direction[8] = { 0, 0, 0, 0, 0, 0, 0, 0 };
	// The pixels where we detected the angles
	std::vector<std::vector<std::pair<int, int>>>  indices(8, std::vector<std::pair<int, int>>(3 * 16));
	// Sobel
	for (int i = starti; i < endi; i++) {
		for (int j = startj; j < endj; j++) {
			int gx = 0;
			int gy = 0;
			for (int x = 0; x < 3; x++){
				for (int y = 0; y < 3; y++){
					int xindex = i + x - 1;
					int yindex = j + y - 1;
					if (xindex < 0 || xindex >= 16){ continue; }
					gx += mb->luma[xindex][yindex] * horizontal_sobel[x][y];
					gy += mb->luma[xindex][yindex] * vertical_sobel[x][y];
				}
			}

			double g = sqrt((gx*gx + gy*gy) / 8);
			// Used to see sobel on the video
			//mb->luma[i][j] = g;

			// Calculate the angle
			double angle = atan2(gy, gx) * 180.0 / PI;
			if (angle < 0) angle += 360;

			int a = (int)floor(angle / 45);
			direction[a] += g;
			// Remember which pixel has what angle
			indices[a].push_back(std::pair<int, int>(i, j));
		}
	}

	// Find the most visible edge
	int max = -99999;
	int angle = 0;
	for (int i = 0; i < 8; i++){
		if (direction[i] > max) {
			max = direction[i];
			angle = i;
		}
	}

	// Return the edge, contains it's angle and the pixels that have this angle
	edge *e = new edge;
	e->angle = angle;
	e->indices = indices[angle];

	return e;
}


void ErrorConcealer::conceal_temporal_1(Frame *frame, Frame *referenceFrame)
{
	int numMB = frame->getNumMB();
	for (int MBx = 0; MBx < numMB; ++MBx){
		if (frame->getMacroblock(MBx)->isMissing()){
			Macroblock *MB = frame->getMacroblock(MBx);
			Macroblock *MBref = referenceFrame->getMacroblock(MBx);
			for (int i = 0; i < 16; ++i)	{
				for (int j = 0; j < 16; ++j)		{
					MB->luma[i][j] = MBref->luma[i][j];
					MB->cb[i/2][j/2] = MBref->cb[i/2][j/2];
					MB->cr[i/2][j/2] = MBref->cr[i/2][j/2];
				}
			}
		}
	}
}

enum MBPOSITION { NONE, TOP, BOTTOM, LEFT, RIGHT , SPATIAL};

pixel getYPixel(Frame *frame, int posx, int posy){
	int MBx = (int(posy/16) * frame->getWidth() ) ;
	MBx += (int(posx/16));
	Macroblock *MB = frame->getMacroblock(MBx);
	pixel luma = MB->luma[posy%16][posx%16];
	return luma;	
}

void setYPixel(Frame *frame, int posx, int posy, pixel value){
	int MBx = (int(posy / 16) * frame->getWidth());
	MBx += (int(posx / 16));
	Macroblock *MB = frame->getMacroblock(MBx);
	MB->luma[posy % 16][posx % 16] = value;
}

pixel getCbPixel(Frame *frame, int posx, int posy){
	posx = posx / 2;
	posy = posy / 2;

	int MBx = (int(posy/8) * frame->getWidth() ) ;
	MBx += (int(posx/8));
	Macroblock *MB = frame->getMacroblock(MBx);
	pixel cb = MB->cb[posy % 8][posx % 8];
	return cb;
}

void setCbPixel(Frame *frame, int posx, int posy, pixel value){
	posx = posx / 2;
	posy = posy / 2;

	int MBx = (int(posy / 8) * frame->getWidth());
	MBx += (int(posx / 8));
	Macroblock *MB = frame->getMacroblock(MBx);
	MB->cb[posy % 8][posx % 8] = value;
}

pixel getCrPixel(Frame *frame, int posx, int posy){
	posx = posx / 2;
	posy = posy / 2;

	int MBx = (int(posy/8) * frame->getWidth() ) ;
	MBx += (int(posx/8));
	Macroblock *MB = frame->getMacroblock(MBx);
	pixel cr = MB->cr[posy % 8][posx % 8];
	return cr;	
}

void setCrPixel(Frame *frame, int posx, int posy, pixel value){
	posx = posx / 2;
	posy = posy / 2;

	int MBx = (int(posy / 8) * frame->getWidth());
	MBx += (int(posx / 8));
	Macroblock *MB = frame->getMacroblock(MBx);
	MB->cr[posy % 8][posx % 8] = value;
}

float get_smb_error(Frame *frame, Frame* reference_frame, SubMacroblock* source, SubMacroblock* destination){
	// Calculate the error on the boundary of the submacroblocks 
	// if we copy the submacroblock source from reference_frame into submacroblock destination from frame

	int total_error = 0;
	int nr_of_compared_pixels = 0;

	// TOP
	SubMacroblock above = destination->get_submacroblock_above(frame);
	if (above.submacroblock_nr != -1){
		int source_x = source->get_submacroblock_x();
		int source_y = source->get_submacroblock_y(); // top row of source

		int above_x = above.get_submacroblock_x();
		int above_y = above.get_submacroblock_y() + above.submacroblock_width - 1; // bottom row of above
		for (int t = 0; t < destination->submacroblock_width; ++t){
			total_error += abs(source->get_luma(source_x + t, source_y) - above.get_luma(above_x + t, above_y));
			++nr_of_compared_pixels;
		}
	}

	// BOTTOM
	SubMacroblock below = destination->get_submacroblock_below(frame);
	if (below.submacroblock_nr != -1){
		int source_x = source->get_submacroblock_x();
		int source_y = source->get_submacroblock_y() + source->submacroblock_width - 1; // bottom row of source

		int below_x = below.get_submacroblock_x();
		int below_y = below.get_submacroblock_y(); // top row of below
		for (int t = 0; t < destination->submacroblock_width; ++t){
			total_error += abs(source->get_luma(source_x + t, source_y) - below.get_luma(below_x + t, below_y));
			++nr_of_compared_pixels;
		}
	}

	// LEFT
	SubMacroblock left = destination->get_submacroblock_left(frame);
	if (left.submacroblock_nr != -1){
		int source_x = source->get_submacroblock_x();
		int source_y = source->get_submacroblock_y(); // left collumn of source

		int left_x = left.get_submacroblock_x() + left.submacroblock_width - 1; // right collumn of left
		int left_y = left.get_submacroblock_y();

		for (int t = 0; t < destination->submacroblock_width; ++t){
			total_error += abs(source->get_luma(source_x, source_y + t) - left.get_luma(left_x, left_y + t));
			++nr_of_compared_pixels;
		}
	}

	// RIGHT
	SubMacroblock right = destination->get_submacroblock_right(frame);
	if (right.submacroblock_nr != -1){
		int source_x = source->get_submacroblock_x() + source->submacroblock_width - 1; // right collumn of source
		int source_y = source->get_submacroblock_y();

		int right_x = right.get_submacroblock_x(); // left collumn of right
		int right_y = right.get_submacroblock_y();

		for (int t = 0; t < destination->submacroblock_width; ++t){
			total_error += abs(source->get_luma(source_x, source_y + t) - right.get_luma(right_x, right_y + t));
			++nr_of_compared_pixels;
		}
	}

	float average_error = float(total_error) / nr_of_compared_pixels;
	return average_error;
}

std::list<MotionVector> get_movement_vectors(Frame* frame, Macroblock* mb){
	// Get all possible motionvectors for a missing macroblock

	std::list<MotionVector> mb_movement_vectors;
	int mb_x = mb->getXPos();
	int mb_y = mb->getYPos();
	int frame_width = frame->getWidth();
	int frame_height = frame->getHeight();

	// the first macroblock movement vectors to consider are those from the macroblock on top, below, to the left and to the right
	// of the current macroblock

	// top
	int top_x = mb_x;
	int top_y = mb_y - 1;
	if (top_y >= 0){
		Macroblock* top = frame->getMacroblock(top_y*frame_width + top_x);
		if (!top->isMissing()){
			mb_movement_vectors.push_back(top->mv);
		}
	}
	// bottom
	int bottom_x = mb_x;
	int bottom_y = mb_y + 1;
	if (bottom_y < frame_height){
		Macroblock* bottom = frame->getMacroblock(bottom_y*frame_width + bottom_x);
		if (!bottom->isMissing()){
			mb_movement_vectors.push_back(bottom->mv);
		}
	}
	// left
	int left_x = mb_x - 1;
	int left_y = mb_y;
	if (left_x >= 0){
		Macroblock* left = frame->getMacroblock(left_y*frame_width + left_x);
		if (!left->isMissing()){
			mb_movement_vectors.push_back(left->mv);
		}
	}
	// right
	int right_x = mb_x + 1;
	int right_y = mb_y;
	if (right_x < frame_width){
		Macroblock* right = frame->getMacroblock(right_y*frame_width + right_x);
		if (!right->isMissing()){
			mb_movement_vectors.push_back(right->mv);
		}
	}

	return mb_movement_vectors;
}

void ErrorConcealer::conceal_temporal_2(Frame *frame, Frame *referenceFrame, int size){
// Sub-macroblock size to be completed. Add explanatory notes in English.
	if (!frame->is_p_frame()){
		conceal_spatial_2(frame, 1);
	}
	else{
		int sub_macroblock_width;
		switch (size){
			// size of a macroblock is 16 pixels by 16 pixels
			// size of a submacroblock is 2 by 2 or 4 by 4 or 8 by 8 or 16 by 16
		case 1: sub_macroblock_width = 2; break;
		case 2: sub_macroblock_width = 4; break;
		case 3: sub_macroblock_width = 8; break;
		case 4: sub_macroblock_width = 16; break;
		}

		int numMB = frame->getNumMB(); // number of macroblocks in a frame
		int numSMB = (16 / sub_macroblock_width) * (16 / sub_macroblock_width); // number of submacroblocks in a macroblock

		std::list<MotionVector> mb_movement_vectors;  // Macroblock movement vectors

		std::list<MotionVector> smb_movement_vectors; // Submacroblock movement vectors span a whole macroblock
		for (int x = 0; x < 16/sub_macroblock_width; ++x){
			for (int y = 0; y < 16/sub_macroblock_width; ++y){
				MotionVector mv;
				mv.x = x; // sub macroblock collumn
				mv.y = y; // sub macroblock row
				smb_movement_vectors.push_back(mv);
			}
		}

		// Iterate over all the macroblocks, and conceil if a macroblock is missing
		for (int MBx = 0; MBx < numMB; ++MBx){
			if (frame->getMacroblock(MBx)->isMissing()){
				// macroblock is missing
				Macroblock *MB = frame->getMacroblock(MBx);

				// get the possible movement vectors for the missing macroblock
				mb_movement_vectors = get_movement_vectors(frame, MB);

				// Iterate over all the submacroblocks within the missing macroblock
				for (int sub_macroblock_index = 0; sub_macroblock_index < numSMB; ++sub_macroblock_index){
					SubMacroblock SMB = SubMacroblock(MB, sub_macroblock_width, sub_macroblock_index);

					// pocket the best result
					int smallest_error = 99999999;
					MotionVector best_mb_movement_vector = MotionVector();
					MotionVector best_smb_movement_vector = MotionVector();

					// iterate over all possible macroblock movement vectors
					for (std::list<MotionVector>::iterator mb_mv = mb_movement_vectors.begin(); mb_mv != mb_movement_vectors.end(); mb_mv++){
						Macroblock* source_mb;
						int source_mb_x = MB->getXPos() + mb_mv->x;
						int source_mb_y = MB->getYPos() + mb_mv->y;

						if (source_mb_x >= 0 && source_mb_x < frame->getWidth() && source_mb_y >= 0 && source_mb_y < frame->getHeight()){
							// The mb pointed to by the mb movement vectors is within the frame
							source_mb = referenceFrame->getMacroblock(source_mb_y * referenceFrame->getWidth() + source_mb_x);

							// Iterate over all the sub macroblock movement vectors
							for (std::list<MotionVector>::iterator smb_mv = smb_movement_vectors.begin(); smb_mv != smb_movement_vectors.end(); smb_mv++){
								int source_smb_x = SMB.get_submacroblock_x() + smb_mv->x;
								int source_smb_y = SMB.get_submacroblock_y() + smb_mv->y;
								SubMacroblock source_smb = SubMacroblock(source_mb, sub_macroblock_width, source_mb_x, source_mb_y);

								int smb_error = get_smb_error(frame, referenceFrame, &source_smb, &SMB);
								if (smb_error < smallest_error){
									smallest_error = smb_error;
									best_mb_movement_vector = *mb_mv;
									best_smb_movement_vector = *smb_mv;
								}
							}
						}
					}

					// we found the best matching macroblock movement vector and the best matching submacroblock movement vector
					// now copy the content from the submacroblock in the reference frame into the submacroblock in the current frame
					for (int i = 0; i < SMB.submacroblock_width; ++i){
						for (int j = 0; j < SMB.submacroblock_width; ++j){
							int destination_x = MB->getXPos() * 16 + SMB.get_submacroblock_x() * SMB.submacroblock_width + i;
							int destination_y = MB->getYPos() * 16 + SMB.get_submacroblock_y() * SMB.submacroblock_width + j;

							int ref_x = destination_x + best_mb_movement_vector.x * 16 + best_smb_movement_vector.x * SMB.submacroblock_width;
							int ref_y = destination_y + best_mb_movement_vector.y * 16 + best_smb_movement_vector.y * SMB.submacroblock_width;

							pixel reference_y = getYPixel(referenceFrame, ref_x, ref_y);
							pixel reference_cr = getCrPixel(referenceFrame, ref_x, ref_y);
							pixel reference_cb = getCbPixel(referenceFrame, ref_x, ref_y);

							setYPixel(frame, destination_x, destination_y, reference_y);
							setCrPixel(frame, destination_x, destination_y, reference_cr);
							setCbPixel(frame, destination_x, destination_y, reference_cb);
						}
					}
				}

				MB->setConcealed();
			}
		}
	}
}

Macroblock* get_missing_macroblock_with_most_neighbours(Frame* frame){
	// this methods get the macroblock with the least missing neighbours

	int number_of_macroblocks = frame->getNumMB();

	Macroblock* macroblock_with_least_missing_neighbours = &Macroblock(); // initialize on first macroblock
	int largest_number_of_neighbours = 0;

	for (int MBx = 0; MBx < number_of_macroblocks; ++MBx){
		if (frame->getMacroblock(MBx)->isMissing()){
			Macroblock* mb = frame->getMacroblock(MBx);

			int number_of_neighbours = get_movement_vectors(frame, mb).size();
			if (number_of_neighbours > largest_number_of_neighbours){
				largest_number_of_neighbours = number_of_neighbours;
				macroblock_with_least_missing_neighbours = mb;
			}
		}
	}

	return macroblock_with_least_missing_neighbours;
}

void ErrorConcealer::conceal_temporal_3(Frame *frame, Frame *referenceFrame, int size){

	if (!frame->is_p_frame()){
		conceal_spatial_2(frame,1);
	}
	else{
		int sub_macroblock_width;
		switch (size){
			// size of a macroblock is 16 pixels by 16 pixels
			// size of a submacroblock is 2 by 2 or 4 by 4 or 8 by 8 or 16 by 16
		case 1: sub_macroblock_width = 2; break;
		case 2: sub_macroblock_width = 4; break;
		case 3: sub_macroblock_width = 8; break;
		case 4: sub_macroblock_width = 16; break;
		}

		int numMB = frame->getNumMB(); // number of macroblocks in a frame
		int numSMB = (16 / sub_macroblock_width) * (16 / sub_macroblock_width); // number of submacroblocks in a macroblock

		std::list<MotionVector> mb_movement_vectors;  // Macroblock movement vectors

		std::list<MotionVector> smb_movement_vectors; // Submacroblock movement vectors span a whole macroblock
		for (int x = 0; x < 16/sub_macroblock_width; ++x){
			for (int y = 0; y < 16/sub_macroblock_width; ++y){
				MotionVector mv;
				mv.x = x;
				mv.y = y;
				smb_movement_vectors.push_back(mv);
			}
		}

		// The problem of neighbouring macroblocks missing is solved by always 
		// conceiling the macroblock with the most neighbours (at least one, if all macroblocks are missing this function fails)

		Macroblock* missing_macroblock = get_missing_macroblock_with_most_neighbours(frame);
		while (missing_macroblock->getMBNum() != -1){
			Macroblock* MB = missing_macroblock;
			// get the possible movement vectors for the missing macroblock
			mb_movement_vectors = get_movement_vectors(frame, MB);

			// Iterate over all the submacroblocks within the missing macroblock
			for (int sub_macroblock_index = 0; sub_macroblock_index < numSMB; ++sub_macroblock_index){
				SubMacroblock SMB = SubMacroblock(MB, sub_macroblock_width, sub_macroblock_index);

				// pocket the best result
				int smallest_error = 99999999;
				MotionVector best_mb_movement_vector = MotionVector();
				MotionVector best_smb_movement_vector = MotionVector();

				// iterate over all possible macroblock movement vectors
				for (std::list<MotionVector>::iterator mb_mv = mb_movement_vectors.begin(); mb_mv != mb_movement_vectors.end(); mb_mv++){
					Macroblock* source_mb;
					int source_mb_x = MB->getXPos() + mb_mv->x;
					int source_mb_y = MB->getYPos() + mb_mv->y;

					if (source_mb_x >= 0 && source_mb_x < frame->getWidth() && source_mb_y >= 0 && source_mb_y < frame->getHeight()){
						// The mb pointed to by the mb movement vectors is within the frame
						source_mb = referenceFrame->getMacroblock(source_mb_y * referenceFrame->getWidth() + source_mb_x);

						// Iterate over all the sub macroblock movement vectors
						for (std::list<MotionVector>::iterator smb_mv = smb_movement_vectors.begin(); smb_mv != smb_movement_vectors.end(); smb_mv++){
							int source_smb_x = SMB.get_submacroblock_x() + smb_mv->x;
							int source_smb_y = SMB.get_submacroblock_y() + smb_mv->y;
							SubMacroblock source_smb = SubMacroblock(source_mb, sub_macroblock_width, source_mb_x, source_mb_y);

							int smb_error = get_smb_error(frame, referenceFrame, &source_smb, &SMB);
							if (smb_error < smallest_error){
								smallest_error = smb_error;
								best_mb_movement_vector = *mb_mv;
								best_smb_movement_vector = *smb_mv;
							}
						}
					}
				}

				// we found the best matching macroblock movement vector and the best matching submacroblock movement vector
				// now copy the content from the submacroblock in the reference frame into the submacroblock in the current frame
				for (int i = 0; i < SMB.submacroblock_width; ++i){
					for (int j = 0; j < SMB.submacroblock_width; ++j){
						int destination_x = MB->getXPos() * 16 + SMB.get_submacroblock_x() * SMB.submacroblock_width + i;
						int destination_y = MB->getYPos() * 16 + SMB.get_submacroblock_y() * SMB.submacroblock_width + j;

						int ref_x = destination_x + best_mb_movement_vector.x * 16 + best_smb_movement_vector.x * SMB.submacroblock_width;
						int ref_y = destination_y + best_mb_movement_vector.y * 16 + best_smb_movement_vector.y * SMB.submacroblock_width;

						pixel reference_y = getYPixel(referenceFrame, ref_x, ref_y);
						pixel reference_cr = getCrPixel(referenceFrame, ref_x, ref_y);
						pixel reference_cb = getCbPixel(referenceFrame, ref_x, ref_y);

						setYPixel(frame, destination_x, destination_y, reference_y);
						setCrPixel(frame, destination_x, destination_y, reference_cr);
						setCbPixel(frame, destination_x, destination_y, reference_cb);
					}
				}
			}
			MB->setConcealed();

			// get the missing macroblock for the next iteration
			missing_macroblock = get_missing_macroblock_with_most_neighbours(frame);
		}
	}
}

void copy_submacroblock(Frame* frame, Frame* referenceFrame, SubMacroblock source, SubMacroblock destination){
	// we found the best matching macroblock movement vector and the best matching submacroblock movement vector
	// now copy the content from the submacroblock in the reference frame into the submacroblock in the current frame

	Macroblock* mb = destination.parent_macroblock;

	for (int i = 0; i < destination.submacroblock_width; ++i){
		for (int j = 0; j < destination.submacroblock_width; ++j){
			int destination_x = mb->getXPos() * 16 + destination.get_submacroblock_x() * destination.submacroblock_width + i;
			int destination_y = mb->getYPos() * 16 + destination.get_submacroblock_y() * destination.submacroblock_width + j;

			int ref_x = source.parent_macroblock->getXPos() * 16 + source.get_submacroblock_x() * source.submacroblock_width + i;
			int ref_y = source.parent_macroblock->getYPos() * 16 + source.get_submacroblock_y() * source.submacroblock_width + j;

			pixel reference_y = getYPixel(referenceFrame, ref_x, ref_y);
			pixel reference_cr = getCrPixel(referenceFrame, ref_x, ref_y);
			pixel reference_cb = getCbPixel(referenceFrame, ref_x, ref_y);

			setYPixel(frame, destination_x, destination_y, reference_y);
			setCrPixel(frame, destination_x, destination_y, reference_cr);
			setCbPixel(frame, destination_x, destination_y, reference_cb);
		}
	}
}

std::pair<int, SubMacroblock> submacroblock_lowest_error(Frame* frame, Frame* referenceFrame, SubMacroblock smb){
	int sub_macroblock_width = smb.submacroblock_width;
	Macroblock* MB = smb.parent_macroblock;
	SubMacroblock SMB = smb;

	int numSMB = (16 / sub_macroblock_width) * (16 / sub_macroblock_width); // number of submacroblocks in a macroblock
	bool none_found = false;

	std::list<MotionVector> mb_movement_vectors;  // Macroblock movement vectors
	mb_movement_vectors = get_movement_vectors(frame, smb.parent_macroblock);
	assert(mb_movement_vectors.size() > 0);

	std::list<MotionVector> smb_movement_vectors; // Submacroblock movement vectors span a whole macroblock
	for (int x = 0; x < 16 / sub_macroblock_width; x++){
		for (int y = 0; y < 16 / sub_macroblock_width; y++){
			MotionVector mv;
			mv.x = x;
			mv.y = y;
			smb_movement_vectors.push_back(mv);
		}
	}
	assert(smb_movement_vectors.size() > 0);

	// pocket the best result
	int smallest_error = 999;
	SubMacroblock best_submacroblock_source = SubMacroblock(referenceFrame->getMacroblock(0), smb.submacroblock_width, 0);

	// iterate over all possible macroblock movement vectors
	for (std::list<MotionVector>::iterator mb_mv = mb_movement_vectors.begin(); mb_mv != mb_movement_vectors.end(); mb_mv++){
		Macroblock* source_mb;
		int source_mb_x = MB->getXPos() + mb_mv->x;
		int source_mb_y = MB->getYPos() + mb_mv->y;

		if (source_mb_x >= 0 && source_mb_x < frame->getWidth() && source_mb_y >= 0 && source_mb_y < frame->getHeight()){
			// The mb pointed to by the mb movement vectors is within the frame
			source_mb = referenceFrame->getMacroblock(source_mb_y * referenceFrame->getWidth() + source_mb_x);

			// Iterate over all the sub macroblock movement vectors
			for (std::list<MotionVector>::iterator smb_mv = smb_movement_vectors.begin(); smb_mv != smb_movement_vectors.end(); smb_mv++){
				int source_smb_x = smb_mv->x;
				int source_smb_y = smb_mv->y;
				SubMacroblock source_smb = SubMacroblock(source_mb, sub_macroblock_width, source_smb_x, source_smb_y);

				int smb_error = get_smb_error(frame, referenceFrame, &source_smb, &SMB);
				if (smb_error < smallest_error){
					smallest_error = smb_error;
					best_submacroblock_source = source_smb;
				}
			}
		}
	}

	none_found = (best_submacroblock_source.submacroblock_nr == -1); // for conditional breakpoint
	return std::make_pair(smallest_error, best_submacroblock_source);
}

int conceal_submacroblock_with_partitioning(Frame* frame, Frame* referenceFrame, SubMacroblock smb){
	int nr_of_submacroblocks_per_row = 16 / smb.submacroblock_width;

	int error = 0;

	if (smb.submacroblock_width >= 4){
		// Partition the submacroblock into four quarters
		int first_quarter_x = smb.get_submacroblock_x() * 2;
		int first_quarter_y = smb.get_submacroblock_y() * 2;
		SubMacroblock first_quarter = SubMacroblock(smb.parent_macroblock, smb.submacroblock_width / 2, first_quarter_x, first_quarter_y);

		int second_quarter_x = first_quarter_x;
		int second_quarter_y = first_quarter_y + 1;
		SubMacroblock second_quarter = SubMacroblock(smb.parent_macroblock, smb.submacroblock_width / 2, second_quarter_x, second_quarter_y);

		int third_quarter_x = first_quarter_x + 1;
		int third_quarter_y = first_quarter_y;
		SubMacroblock third_quarter = SubMacroblock(smb.parent_macroblock, smb.submacroblock_width / 2, third_quarter_x, third_quarter_y);

		int fourth_quarter_x = first_quarter_x + 1;
		int fourth_quarter_y = first_quarter_y + 1;
		SubMacroblock fourth_quarter = SubMacroblock(smb.parent_macroblock, smb.submacroblock_width / 2, fourth_quarter_x, fourth_quarter_y);

		//get the errors for each quarter
		int first_quarter_result = conceal_submacroblock_with_partitioning(frame, referenceFrame, first_quarter);
		int second_quarter_result = conceal_submacroblock_with_partitioning(frame, referenceFrame, second_quarter);
		int third_quarter_result = conceal_submacroblock_with_partitioning(frame, referenceFrame, third_quarter);
		int fourth_quarter_result = conceal_submacroblock_with_partitioning(frame, referenceFrame, fourth_quarter);

		//get the boundary error if we do not partition the submacroblock
		std::pair<int, SubMacroblock> full_smb_result = submacroblock_lowest_error(frame, referenceFrame, smb);

		//we use the full submacroblock if his average error is smaller than minimum error of the quarters
		int min_average_error_quarters = std::min(std::min(first_quarter_result, second_quarter_result), std::min(third_quarter_result, fourth_quarter_result));
		if (full_smb_result.first < min_average_error_quarters){
			copy_submacroblock(frame, referenceFrame, full_smb_result.second, smb);
			error = full_smb_result.first;
		}
		else{
			//in case we do not use the full submacroblock as concealing block, it will be concealed already in deeper recursions
			error = min_average_error_quarters;
		}
	}
	else{
		//the submacroblock width == 2, so we cannot partition further
		std::pair<int, SubMacroblock> result = submacroblock_lowest_error(frame, referenceFrame, smb);
		copy_submacroblock(frame, referenceFrame, result.second, smb);
		error = result.first;
	}
	return error;
}

void ErrorConcealer::conceal_temporal_4(Frame* frame, Frame* referenceFrame){
	// Recursivly try out smaller submacroblock sizes

	if (!frame->is_p_frame()){
		conceal_spatial_2(frame,1);
	}
	else{
		// The problem of neighbouring macroblocks missing is solved by always 
		// conceiling the macroblock with the most neighbours (at least one, if all macroblocks are missing this function fails)

		Macroblock* missing_macroblock = get_missing_macroblock_with_most_neighbours(frame);
		while (missing_macroblock->getMBNum() != -1){
			Macroblock* MB = missing_macroblock;
			SubMacroblock big_submacroblock = SubMacroblock(MB, 16, 0, 0); // one submacroblock that spans the whole macroblock;
			conceal_submacroblock_with_partitioning(frame, referenceFrame, big_submacroblock);
			MB->setConcealed();

			// get the missing macroblock for the next iteration
			missing_macroblock = get_missing_macroblock_with_most_neighbours(frame);
		}
	}

}


