/*****************************************
* game.h
* Titouan Vervack en Caroline De Brouwer
* Groep 24
******************************************/

#ifndef	H_GAME
#define H_GAME

#include "hud.h"
#include "world.h"

typedef enum {NONE, BUILD_TOWER, DESTROY_TOWER, GENERATE_FROST_WAVE, RELEASE_POISON_GAS} Action;

typedef struct {
	int wave_number;
	int spawn_interval;
	int spawn_counter;
	int started;
	int completed;
	int cool_down;
	int cool_down_counter;
	int nr_of_enemies;
	int nr_of_spawned_enemies;
	int boss_wave;
} WaveInfo;

typedef struct {
	float screen_x;
	float screen_y;
	float world_x;
	float world_y;
	int tile_x;
	int tile_y;
	int tile_changed;
} Mouse;

typedef struct {
	int valid;
	Entity entity;
} Blueprint;

typedef struct {
	int frost_wave_active;
	int poison_gas_active;
	int spell_duration;
} Spells;

typedef struct {
	Entity * enemies;
	Entity ** towers;
	int enemies_length;
	int towers_length;
	
	World world;
	WaveInfo wave;

	Action action;
	Blueprint blueprint;
	
	Mouse mouse;
	Hud hud;

	Spells spells;

	int refresh_paths;
	int redraw;

	long money;
	long score;
	long mana;
	
	int game_over;
	int play;
} GameState;

/*
 * Starts the main game loop
 */
int run_game_loop(GameState * state);

/*
 * Check if certain spells are active.
 * If so, execute them.
 */
void check_spells(GameState * state);

/*
 * Check if the cooldown can start or is over so 
 * that the next wave can be prepared.
 */
void check_enemy_wave(GameState * state); 

/*
 * Do the movement of all entities.
 */
void update_movement(GameState * state);

/*
 * Execute the attacking logic of the towers.
 */
void do_tower_attacks(GameState * state);

/*
 * Initializes the game state.
 */
void init_game_state(GameState * state);

/*
 * Cleans up the game state.
 */
void destroy_game_state(GameState * state);

/*
 * Main render method.
 */
void render_game(GameState * state);

/* === Mouse method === */

/*
 * Update all the Mouse fields (screen, world and tile).
 */
void update_mouse(GameState * state, float screen_x, float screen_y);


/* === Event handlers === */

/*
 * Mouse has moved.
 */
void mouse_move(MouseMoveEvent * ev, GameState * state);

/*
 * Mouse button is down.
 */
void mouse_down(MouseDownEvent * ev, GameState * state);

/*
 * Mouse button is up
 */
void mouse_up(MouseUpEvent * ev, GameState * state);

#endif