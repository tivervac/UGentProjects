/*****************************************
* hud.h
* Titouan Vervack en Caroline De Brouwer
* Groep 24
******************************************/

#ifndef H_HUD
#define H_HUD

#include "config.h"

typedef enum {BUTTON_UP, BUTTON_HOVER, BUTTON_DOWN} ButtonState;
typedef enum {BUTTON_PLAY, BUTTON_TOWER_MACHINEGUN, BUTTON_TOWER_ROCKET, BUTTON_TOWER_FLAK, BUTTON_DESTROY_TOWER, BUTTON_SPELL_FREEZE, BUTTON_SPELL_KILL} ButtonType;


typedef struct {
	float screen_sx;
	float screen_sy;
	float screen_dx;
	float screen_dy;
} Bounds;

typedef struct {
	ButtonState state;
	Bounds bounds;
} Button;

typedef struct {
	long * score;
	long * mana;
	long * money;
	int * castle_health;
	int * wave_number;
	int * play;
	Button buttons[BUTTON_AMOUNT];
} Hud;

/* === Buttons === */

/*
 * Initialises the buttons.
 */
void init_buttons(Hud * hud);

/*
 * Renders the buttons of the ui.
 */
void render_buttons(Hud * hud);

/*
 * Returns true if the given coordinate is within the bounds.
 */
int in_bounds(float screen_x, float screen_y, Bounds bounds);


/* === Pure hud functions === */

/*
 * Updates the hud fields by pointing them to the actual fields.
 */
void update_hud(Hud * hud, long * score, long * mana, long * money, int * castle_health, int * wave_number, int * play);


#endif