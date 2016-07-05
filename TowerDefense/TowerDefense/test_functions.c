/*****************************************
* test_functions.c
* Titouan Vervack en Caroline De Brouwer
* Groep 24
******************************************/

#include <stdio.h>
#include "game.h"
#include "gui.h"
#include "config.h"
#include "test_functions.h"

/* Variables */
int test_x = 400;
int test_y = 300;

/*
 * Starts a small test loop to
 * see if everything works as 
 * intended
 */
int run_test_loop(GameState * state) {
	int done = 0;

	/* Initialise gui */
	init_gui(800,600);

	/* Load resources */
	init_sprite_cache();

	/* Initialise game loop */
	init_game_loop(FPS);
	while (!done) {
		/* Get events */
		Event ev;
		wait_for_event(&ev);

		/* Event handlers */
		switch (ev.type) {
			case EVENT_TIMER:
				state->redraw = 1;
				break;
			case EVENT_DISPLAY_CLOSE:
				done = 1;
				break;
		}	

		/* Render only on timer event AND if all movement and logic was processed */
		if (state->redraw && all_events_processed()) { 
			render_test(state);
		}
	}

	/* Cleanup */
	cleanup_game_loop();
	cleanup_sprite_cache();
	return 0;
}

void render_test(GameState * state) {
	/* Variables */
	Color color = {255, 0, 255, 255};
	Color black = {0, 0, 0, 255};
	char buffer [20];
	int n;
	
	test_x += 1;
	test_y += 1;
	if (test_x >= 660)
		test_x = 140;
	if (test_y >= 560)
		test_y = 40;

	state->redraw = 0;

	/* Render text */
	draw_text("GUI correct geinitialiseerd!", FONT_LARGE, color, test_x, test_y, ALIGN_CENTER);
	n = sprintf(buffer, "%#.1f fps", get_current_fps());
	draw_text(buffer, FONT_LARGE, color, test_x, test_y+20, ALIGN_CENTER);

	/* Render to screen */
	flip_display();
	clear_to_color(black);
}