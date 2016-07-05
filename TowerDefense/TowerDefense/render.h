/*****************************************
* render.h
* Titouan Vervack en Caroline De Brouwer
* Groep 24
******************************************/

#ifndef H_RENDER
#define H_RENDER

#include "world.h"
#include "entities.h"
#include "game.h"

/*
 * Pre-render the loaded world into one big sprite,
 * that can be used to render the world in the game loop.
 */
void render_world_to_sprite(World * world);

/*
 * Render the playworld.
 * This includes background tiles and grid.
 */
void render_world(World * world);

/*
 * Render the transparent spell effect layer over the world
 */
void render_spellscreen(Spells * spells);

/*
 * Renders an entity.
 */
void render_entity(Entity * entity); 

/*
 * Render the projectile of a tower, passed as an entity
 */
void render_projectiles(Entity * tower);

/*
 * Renders the ui.
 */
void render_ui(GameState * state);

/*
 * Renders the hud.
 */
void render_hud(Hud * hud);

/*
 * Render game over, if the game is truly over.
 */
void render_game_over(GameState * state);

/*
 * Render all actions for the mouse.
 */
void render_mouse_actions(GameState * state);

/*
 * Render Paths on the screen.
 * Useful for debugging.
 * ALREADY IMPLEMENTED, DO NOT CHANGE
 */
void render_paths(GameState * state);

#endif