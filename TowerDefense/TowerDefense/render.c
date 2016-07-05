/*****************************************
* render.c
* Titouan Vervack en Caroline De Brouwer
* Groep 24
******************************************/

#include <stdio.h>
#include "gui.h"
#include "world.h"
#include "game.h"
#include "hud.h"
#include "render.h"
#include "util.h"

#define THICKNESS 4

void render_world_to_sprite(World* world) {
	register int i, j;
	Color color = {0, 0, 0, 127};
	start_drawing_world();
	for (i = 0; i < world->width_in_tiles; i++) {
		for (j  = 0; j < world->height_in_tiles; j++) {
			int x = convert_tile2world_x(i);
			int y = convert_tile2world_y(j);
			Entity* tile = &world->entities[i][j];
			if (SHOW_GRID == 1) {
				draw_line(color, x, y, x + TILE_SIZE, y, THICKNESS);
				draw_line(color, x, y, x, y + TILE_SIZE, THICKNESS);
			}
			draw_sprite(SPRITE_GRASS, x, y, 0);
			if (tile->type != EMPTY) {
				render_entity(tile);
			}
		}
	}
	stop_drawing_world();
}

void render_world(World* world) {
	draw_sprite(SPRITE_WORLD, SCREEN_START_X, SCREEN_START_Y, 0);
	render_entity(world->spawn);
	render_entity(world->castle);
}

void render_spellscreen(Spells* spells) {
	Color green = {0, 255, 0, 127};
	Color blue = {0, 0, 255, 127};
	set_transparency_on();
	if (spells->frost_wave_active) {
		draw_rectangle(SCREEN_START_X, SCREEN_START_Y, SCREEN_WIDTH, SCREEN_HEIGHT, blue);
	} else if (spells->poison_gas_active) {
		draw_rectangle(SCREEN_START_X, SCREEN_START_Y, SCREEN_WIDTH, SCREEN_HEIGHT, green);
	}
	set_transparency_off();
}

void render_entity(Entity* entity) {
	double xhealth;
	Color red = {255, 0, 0, 255};
	Color green = {0, 255, 0, 255};
	switch (entity->type) {
		case ENEMY:
		xhealth = ((double)entity->enemy.health / entity->enemy.health_max)*TILE_SIZE;
		draw_rectangle(convert_world2screen_x(entity->enemy.world_x), convert_world2screen_y(entity->enemy.world_y), convert_world2screen_x(entity->enemy.world_x) + TILE_SIZE, convert_world2screen_y(entity->enemy.world_y) + THICKNESS, red);
		draw_rectangle(convert_world2screen_x(entity->enemy.world_x), convert_world2screen_y(entity->enemy.world_y), convert_world2screen_x(entity->enemy.world_x) + xhealth, convert_world2screen_y(entity->enemy.world_y) + THICKNESS, green);
		switch (entity->enemy.enemy_type) {
		case NORMAL:
			draw_sprite_animated(SPRITE_NORMAL, &entity->all.frameAnimator, convert_world2screen_x(entity->enemy.world_x), convert_world2screen_y(entity->enemy.world_y), entity->enemy.angle);
			break;
		case ELITE:
			draw_sprite_animated(SPRITE_ELITE, &entity->all.frameAnimator, convert_world2screen_x(entity->enemy.world_x), convert_world2screen_y(entity->enemy.world_y), entity->enemy.angle);
			break;
		case FAST:
			draw_sprite_animated(SPRITE_FAST, &entity->all.frameAnimator, convert_world2screen_x(entity->enemy.world_x), convert_world2screen_y(entity->enemy.world_y), entity->enemy.angle);
			break;
		case AIR:
			draw_sprite_animated(SPRITE_AIR, &entity->all.frameAnimator, convert_world2screen_x(entity->enemy.world_x), convert_world2screen_y(entity->enemy.world_y), entity->enemy.angle);
			break;
		case BOSS:
			draw_sprite_animated(SPRITE_BOSS, &entity->all.frameAnimator, convert_world2screen_x(entity->enemy.world_x), convert_world2screen_y(entity->enemy.world_y), entity->enemy.angle);
			break;
		}
		break;
	case SPAWN_LOCATION:
		draw_sprite_animated(SPRITE_SPAWN, &entity->all.frameAnimator, convert_world2screen_x(entity->spawn_location.world_x), convert_world2screen_y(entity->spawn_location.world_y), 0);
		break;
	case CASTLE:
		draw_sprite_animated(SPRITE_CASTLE, &entity->all.frameAnimator, convert_world2screen_x(entity->castle.world_x), convert_world2screen_y(entity->castle.world_y), 0);
		break;
	case TOWER:
		switch (entity->tower.tower_type) {
		case MACHINE_GUN: 
			draw_sprite(SPRITE_TOWER_MACHINE_GUN, convert_tile2screen_x(convert_world2tile_x(entity->tower.world_x)), convert_tile2screen_y(convert_world2tile_y(entity->tower.world_y)), 0);
			break;
		case ROCKET_LAUNCHER:
			draw_sprite(SPRITE_TOWER_ROCKET_LAUNCHER, convert_tile2screen_x(convert_world2tile_x(entity->tower.world_x)), convert_tile2screen_y(convert_world2tile_y(entity->tower.world_y)), 0);
			break;
		case FLAK_CANNON:
			draw_sprite(SPRITE_TOWER_FLAK_CANNON, convert_tile2screen_x(convert_world2tile_x(entity->tower.world_x)), convert_tile2screen_y(convert_world2tile_y(entity->tower.world_y)), 0);
			break;
		}
		break;
	case OBSTACLE:
		switch (entity->obstacle.obstacle_type) {
		case WATER:
			draw_sprite(SPRITE_WATER, entity->obstacle.world_x, entity->obstacle.world_y, 0);
			break;
		case MOUNTAIN:
			draw_sprite(SPRITE_MOUTAIN, entity->obstacle.world_x, entity->obstacle.world_y, 0);
			break;
		}
	}
}

void render_projectiles(Entity* tower) {
	register int i;
	for (i = 0; i < tower->tower.ammo; i++) {
		Projectile proj = tower->tower.projectiles[i];
		if (&proj != NULL && proj.live) {
			switch (proj.projectile_type) {
			case BULLET:
				draw_sprite(SPRITE_AMMO_BULLET, convert_world2screen_x(proj.world_x), convert_world2screen_y(proj.world_y), proj.angle);
				break;
			case ROCKET:
				draw_sprite(SPRITE_AMMO_ROCKET, convert_world2screen_x(proj.world_x), convert_world2screen_y(proj.world_y), proj.angle);
				break;
			case FLAK:
				draw_sprite(SPRITE_AMMO_FLAK, convert_world2screen_x(proj.world_x), convert_world2screen_y(proj.world_y), proj.angle);
				break;
			}
		}
	}
}

void render_ui(GameState* state) {
	/* UI background */
	draw_sprite(SPRITE_UI, 0, 0, 0);

	/* Buttons */
	render_buttons(&state->hud);

	/* HUD */
	render_hud(&state->hud);
}

void render_hud(Hud* hud) {
	Color black = {0, 0, 0, 255};
	char buffer[50];
	sprintf(buffer, "$%#ld", *hud->money);
	draw_text(buffer, FONT_LARGE, black, SCREEN_START_X - 50, 6, ALIGN_RIGHT);
	sprintf(buffer, "Score: %#ld - HP: %#ld - Mana: %#ld ", *hud->score, *hud->castle_health, *hud->mana);
	draw_text(buffer, FONT_LARGE, black, SCREEN_WIDTH / 2 + 175, 6, ALIGN_RIGHT);
	sprintf(buffer, "Wave: %#ld", *hud->wave_number);
	draw_text(buffer, FONT_LARGE, black, SCREEN_WIDTH - 25, 6, ALIGN_RIGHT);
}

void render_game_over(GameState* state) {
	Color white = {255, 255, 255, 255};
	if (state->game_over) {
		draw_text("Game Over", FONT_HUGE, white, 600, 250, ALIGN_CENTER);
	}
}

void render_mouse_actions(GameState* state) {
	if (in_world_screen(state->mouse.screen_x, state->mouse.screen_y)) {
		if (state->action == BUILD_TOWER) {
			Color white = {255, 255, 255, 50};
			Color green = {0, 255, 0, 255};
			Color red = {255, 0, 0, 255};
			Color color;
			if (state->blueprint.valid && state->money >= state->blueprint.entity.tower.cost && (state->mouse.tile_x != convert_world2tile_x(state->world.castle->all.world_x) 
				|| state->mouse.tile_y != convert_world2tile_y(state->world.castle->all.world_y)) && (state->mouse.tile_x != convert_world2tile_x(state->world.spawn->all.world_x) 
				|| state->mouse.tile_y != convert_world2tile_y(state->world.spawn->all.world_y))) {
					color = green;
			} else{
				color = red;
			}
			draw_rectangle(convert_tile2screen_x(convert_screen2tile_x(state->mouse.screen_x)), convert_tile2screen_y(convert_screen2tile_y(state->mouse.screen_y)), convert_tile2screen_x(convert_screen2tile_x(state->mouse.screen_x)) + TILE_SIZE, convert_tile2screen_y(convert_screen2tile_y(state->mouse.screen_y)) + TILE_SIZE, color);
			set_transparency_on();
			draw_circle(convert_tile2screen_x(convert_screen2tile_x(state->mouse.screen_x)) + TILE_SIZE/2, convert_tile2screen_y(convert_screen2tile_y(state->mouse.screen_y))+ TILE_SIZE/2, state->blueprint.entity.tower.range, white);
			set_transparency_off();
		}
	}
}



/* === Implemented methods, DO NOT CHANGE === */

void render_paths(GameState* state) {
	Path* path;
	Color red = {255, 0, 0, 255};
	Color orange = {255, 192, 0, 255};
	int i,j, x = 0, y = 0, prev_x = 0, prev_y = 0;
	float scale = 0.2;

	for (i = 0; i < state->enemies_length; i++) {
		path = &state->enemies[i].enemy.path;
		for (j = 0; j < path->length; j++) {
			prev_x = x;
			prev_y = y;
			x = convert_tile2screen_x(path->nodes[j].tile_x) + 0.5*TILE_SIZE;
			y = convert_tile2screen_y(path->nodes[j].tile_y) + 0.5*TILE_SIZE;

			if (j != 0) {
				draw_line(red, prev_x, prev_y, x, y, 2.0);
			}
			if (j == 0 || j == (path->length - 1)) {
				draw_rectangle(x-(TILE_SIZE*scale),y-(TILE_SIZE*scale), x+(TILE_SIZE*scale), y+(TILE_SIZE*scale), red);
			} else if (j == path->current_node_index) {
				draw_triangle(x, y-TILE_SIZE*(scale+0.1), x-TILE_SIZE*(scale+0.1), y+TILE_SIZE*(scale+0.1), x+TILE_SIZE*(scale+0.1), y+TILE_SIZE*(scale+0.1), red);
				draw_triangle(x, y-TILE_SIZE*scale, x-TILE_SIZE*scale, y+TILE_SIZE*(scale+0.05), x+TILE_SIZE*scale, y+TILE_SIZE*(scale+0.05), orange);
			} else {
				draw_circle(x, y, scale*TILE_SIZE, red);
			}

		}
	}
}
