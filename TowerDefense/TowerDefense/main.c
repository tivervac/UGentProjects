/*****************************************
* main.c
* Titouan Vervack en Caroline De Brouwer
* Groep 24
******************************************/

#include <stdlib.h>
#include <stdio.h>
#include <crtdbg.h>

#include "hash_set.h"
#include "priority_queue.h"
#include "config.h"
#include "test_functions.h"
#include "game.h"

int main(int argc, char **argv) {
	//_CrtSetBreakAlloc(x); // Use this to seek your reported memory leaks!! */
	{
		/* Variables */
		GameState state;
		/* Initialize GameState */
		init_game_state(&state);

		if (RUN_TEST) {
			run_test_loop(&state);
		}
		else {
			run_game_loop(&state);
		}

		/* Destroy GameState */
		destroy_game_state(&state);
	}

	_CrtDumpMemoryLeaks();
	return 0;
}

