/*****************************************
* gui.h
* Titouan Vervack en Caroline De Brouwer
* Groep 24
******************************************/

#ifndef H_GUI
#define H_GUI

#include <allegro5\allegro.h>
#include <allegro5\allegro_image.h>
#include "entities.h"
#include "util.h"

#define NR_OF_SPRITES 24
/* Sprite mapping */
typedef enum {
	SPRITE_WORLD,
	SPRITE_SPAWN,
	SPRITE_CASTLE,
	SPRITE_GRASS, 
	SPRITE_TOWER_MACHINE_GUN,
	SPRITE_TOWER_ROCKET_LAUNCHER,
	SPRITE_TOWER_FLAK_CANNON,
	SPRITE_DESTROY_TOWER,
	SPRITE_MOUTAIN, 
	SPRITE_WATER, 
	SPRITE_UI,
	SPRITE_NORMAL, 
	SPRITE_ELITE,
	SPRITE_FAST,
	SPRITE_AIR,
	SPRITE_BOSS,
	SPRITE_BUTTON_BLANCO_UP,
	SPRITE_BUTTON_BLANCO_DOWN,
	SPRITE_BUTTON_BLANCO_HOVER,
	SPRITE_AMMO_BULLET,
	SPRITE_AMMO_ROCKET,
	SPRITE_AMMO_FLAK,
	SPRITE_SPELL_FREEZE,
	SPRITE_SPELL_POISON
} SPRITE_TYPE;

/*
 * Inits all gui functions.
 * Width and height are the game screen's dimensions.
 *
 * Returns -1 if not ok 
 */
int init_gui(int width, int height);

/*
 * Initialize all game loop variables.
 * Put this method just in front of 
 * the loop's while condition!
 */
void init_game_loop(int fps);

/*
 * Do all necessary clean up of previously
 * created structs and pointers.
 */
void cleanup_game_loop(void);


/* === Drawing === */

/*
 * Draw the currently set sprite.
 */
void draw_sprite(SPRITE_TYPE type, float screen_x, float screen_y, float angle_radians);

/*
 * Draw the currently set animated sprite
 */
void draw_sprite_animated(SPRITE_TYPE type, FrameAnimator * animator, float screen_x, float screen_y, float angle_radians);

/*
 * Draw a basic line.
 */
void draw_line(Color color, float screen_sx, float screen_sy, float screen_dx, float screen_dy, float thickness);

/*
 * Draw a rectangle.
 */
void draw_rectangle(float screen_sx, float screen_sy, float screen_dx, float screen_dy, Color color);

/*
 * Draw a circle.
 */
void draw_circle(float screen_x, float screen_y, float r, Color color);

/*
 * Draw a triangle.
 */
void draw_triangle(float screen_x1, float screen_y1, float screen_x2, float screen_y2, float screen_x3, float screen_y3, Color color);

/*
 * Draw text at a given size in a 
 * given color on the given coordinates.
 * Align can be LEFT, RIGHT or CENTER.
 */
void draw_text(char* txt, Font font, Color color, float screen_x, float screen_y, ALIGN align);

/*
 * Switches transparency on for all following draw methods.
 */
void set_transparency_on(void);

/*
 * Switches transparancy off for all following draw methods.
 */
void set_transparency_off(void);

/*
 * Clear the entire screen to the given color.
 */
void clear_to_color(Color color);

/*
 * Flip the offscreen bitmap with the current
 * and render it to the screen.
 */
void flip_display();


/*
 * Starts drawing the world sprite as a seperate bitmap.
 */
void start_drawing_world();

/*
 * Stops drawing the world sprite as a seperate bitmap 
 * and saves it to the sprite cache.
 */
void stop_drawing_world();


/* === Events === */

/*
 * Wait for the next event.
 * ev will be filled with next event.
 */
void wait_for_event(Event *ev);

/*
 * Returns 1 if all events have been processed
 * and none are waiting in the queue.
 */
int all_events_processed(void);

/*
 * Retrieve the current fps.
 */
double get_current_fps(void);


/* === Sprite caching === */

/*
 * Load all the sprites for the game.
 */
void init_sprite_cache(void);

/*
 * Destroy all the sprites in the game.
 */
void cleanup_sprite_cache(void);


#endif