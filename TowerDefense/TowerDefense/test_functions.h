/*****************************************
* test_functions.h
* Titouan Vervack en Caroline De Brouwer
* Groep 24
******************************************/

#ifndef H_TEST_FUNCTIONS
#define H_TEST_FUNCTIONS

#include "game.h"

/*
 * Starts a small test loop to
 * see if everything works as 
 * intended
 */
int run_test_loop(GameState * state);

/*
 * Render a test procedure to see if GUI is working.
 */
void render_test(GameState * state);

#endif