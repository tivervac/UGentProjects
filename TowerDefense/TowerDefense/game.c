/*****************************************
* game.c
* Titouan Vervack en Caroline De Brouwer
* Groep 24
******************************************/
#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include "config.h"
#include "world.h"
#include "gui.h"
#include "render.h"
#include "pathfinding.h"
#include "entities.h"
#include "util.h"
#include "game.h"
#include "tower_ai.h"

/* === Prototypes === */
void set_button_hover_state(GameState * state);
void set_button_action(GameState * state);
void reset_button_action(GameState * state);
void reset_all_buttons_action(GameState * state);
int is_dead(Enemy* enemy, GameState* state);

void init_wave(GameState* state) {
	state->wave.wave_number++;
	state->wave.spawn_interval = 4 * FPS;
	state->wave.spawn_counter = 0;
	state->wave.started = 1;
	state->wave.completed = 0;
	state->wave.cool_down = 8 * FPS;
	state->wave.cool_down_counter = 0;
	state->wave.nr_of_enemies = 4 + state->wave.wave_number;
	state->wave.nr_of_spawned_enemies = 0;
	state->wave.boss_wave = (state->wave.wave_number % 5 == 0) ? 1 : 0;
}

int run_game_loop(GameState * state) {
	int done = 0;
	int i = 0;

	/* Initialise gui */
	init_gui(SCREEN_WIDTH,SCREEN_HEIGHT);

	/* Load resources */
	init_sprite_cache();
	render_world_to_sprite(&state->world);
	update_hud(
		&state->hud, 
		&state->score,
		&state->mana,
		&state->money, 
		&state->world.castle->castle.health,
		&state->wave.wave_number,
		&state->play);

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
				if (!state->game_over) {
					if (*state->hud.play) {
						check_spells(state);
						check_enemy_wave(state);
						update_movement(state);
						do_tower_attacks(state);
					}
				}
				break;
			case EVENT_MOUSE_MOVE:
				mouse_move(&ev.mouseMoveEvent, state);
				break;
			case EVENT_MOUSE_DOWN:
				mouse_down(&ev.mouseDownEvent, state);
				break;
			case EVENT_MOUSE_UP:
				mouse_up(&ev.mouseUpEvent, state);
				break;
			case EVENT_DISPLAY_CLOSE:
				done = 1;
				break;
			} 

			/* Render only on timer event AND if all movement and logic was processed */
			if (state->redraw && all_events_processed()) { 
				render_game(state);
			}
	}
	/* Cleanup */
	cleanup_game_loop();
	cleanup_sprite_cache();
	return 0;
}

void check_spells(GameState * state) {
	if (state->action == GENERATE_FROST_WAVE && state->mana >= FROST_WAVE_COST && state->spells.spell_duration <= 0) {
		// frost wave is actief
		state->mana -= FROST_WAVE_COST;
		state->action = NONE;
		state->spells.spell_duration = FROST_WAVE_DURATION;
		state->spells.frost_wave_active = 1;
	} else if (state->spells.poison_gas_active || state->action == RELEASE_POISON_GAS) {
		register int i;
		if (state->action == RELEASE_POISON_GAS) {
			state->spells.spell_duration = POISON_GAS_DURATION;
			state->action = NONE;
			state->spells.poison_gas_active = 1;
		}
		//MAX_MANA wordt nooit bereikt tijdens een poison gas dus dit mag
		if (state->mana == POISON_GAS_COST) {
			state->mana -= POISON_GAS_COST;
		}
		//damage over time
		if (state->wave.spawn_counter % FPS == 0) {
			for (i = 0; i < state->enemies_length;i++) {
				if (state->enemies[i].enemy.alive) {
					state->enemies[i].enemy.health -= POISON_DAMAGE * state->wave.wave_number / 2;
					is_dead(&state->enemies[i].enemy, state);	
				}
			}
		}
	}
	if (state->spells.spell_duration > 0) {
		state->spells.spell_duration--;
	} else {
		state->spells.frost_wave_active = 0;
		state ->spells.poison_gas_active = 0;
	}
}

void init_spawning(GameState* state) {
	if (!state->spells.frost_wave_active && state->wave.spawn_counter == state->wave.spawn_interval) {
		state->wave.spawn_counter = 0;
		if (state->wave.nr_of_spawned_enemies != state->wave.nr_of_enemies) {
			//nieuwe entity aanmaken en checken welk type vijand er moer gespawned worden
			Entity enemy;
			init_entity(&enemy, ENEMY, state->world.spawn->spawn_location.world_x, state->world.spawn->spawn_location.world_y, ENEMY_SIZE, ENEMY_SIZE);
			if (state->wave.nr_of_spawned_enemies % 5 == 1  && (state->wave.boss_wave && state->wave.nr_of_enemies - 1 != state->wave.nr_of_spawned_enemies)) {
				init_enemy(&enemy, ELITE);
				enemy.enemy.health_max = ENEMY_ELITE_HEALTH + (state->wave.wave_number * (state->wave.wave_number / 3) * (ENEMY_ELITE_HEALTH * 0.1));
			} else if (state->wave.boss_wave == 1 && state->wave.nr_of_enemies - 1 == state->wave.nr_of_spawned_enemies) {
				init_enemy(&enemy, BOSS);
				enemy.enemy.health_max = ENEMY_BOSS_HEALTH + (state->wave.wave_number * (state->wave.wave_number / 3) * (ENEMY_BOSS_HEALTH * 0.1));
			} else if (state->wave.wave_number >= 4) {
				int random = rand() % 100;
				if (state->wave.wave_number >= 8) {
					//5% kans op FAST
					if (random < 5) {
						init_enemy(&enemy, FAST);
						enemy.enemy.health_max = ENEMY_FAST_HEALTH + (state->wave.wave_number * (state->wave.wave_number / 3) * (ENEMY_FAST_HEALTH * 0.1));
					} //Ook nog eens 10% kans op AIR
					else if (random < 15) {
						init_enemy(&enemy, AIR);
						enemy.enemy.health_max = ENEMY_AIR_HEALTH + (state->wave.wave_number * (state->wave.wave_number / 3) * (ENEMY_AIR_HEALTH * 0.1));
					}
					else{
						init_enemy(&enemy, NORMAL);
						enemy.enemy.health_max = ENEMY_NORMAL_HEALTH + (state->wave.wave_number * (state->wave.wave_number / 3) * (ENEMY_NORMAL_HEALTH * 0.1));
					}
				} else {
					// 10% kans op AIR
					if (random < 10) {
						init_enemy(&enemy, AIR);
						enemy.enemy.health_max = ENEMY_AIR_HEALTH + (state->wave.wave_number * (state->wave.wave_number / 3) * (ENEMY_AIR_HEALTH * 0.1));
					}else {
						init_enemy(&enemy, NORMAL);
						enemy.enemy.health_max = ENEMY_NORMAL_HEALTH + (state->wave.wave_number * (state->wave.wave_number / 3) * (ENEMY_NORMAL_HEALTH * 0.1));
					}
				}
			} else {
				init_enemy(&enemy, NORMAL);
				enemy.enemy.health_max = ENEMY_NORMAL_HEALTH + (state->wave.wave_number * (state->wave.wave_number / 3) * (ENEMY_NORMAL_HEALTH * 0.1));
			}
			//verdere algemene eigenschappen instellen
			enemy.enemy.health = enemy.enemy.health_max;
			state->enemies = (Entity*) realloc(state->enemies, (state->enemies_length + 1) * sizeof(Entity));
			create_path(&enemy.enemy.path, &state->world, &enemy, state->world.castle, enemy.enemy.path.trajectory_type);
			state->enemies[state->enemies_length] = enemy;
			state->enemies[state->enemies_length].type = ENEMY;
			state->enemies_length++;
			state->wave.nr_of_spawned_enemies++;
		}
	} else if (!state->spells.frost_wave_active) {
		state->wave.spawn_counter++;
	}
}

void check_enemy_wave(GameState * state) {
	if (state->spells.frost_wave_active) {
		return;
	}
	if (state->wave.completed) {
		state->wave.cool_down_counter++;
		if (state->wave.cool_down_counter == state->wave.cool_down) {
			init_wave(state);
		}
	} else {
		if (state->wave.nr_of_enemies == state->wave.nr_of_spawned_enemies) {
			state->wave.completed = 1;
			state->wave.started = 0;
		} else {
			init_spawning(state);
		}
	}
}

void move_enemies(GameState* state) {
	register int i;
	for (i = 0; i < state->enemies_length; i++) {
		Enemy* en = &state->enemies[i].enemy;
		if (en->alive) {
			int to_x = convert_tile2world_x(en->path.nodes[en->path.current_node_index].tile_x);
			int to_y = convert_tile2world_y(en->path.nodes[en->path.current_node_index].tile_y);
			//@euclidean_distance: laat een kleine speling toe
			if (euclidean_distance(en->world_x, en->world_y, to_x, to_y) < 2 && en->path.current_node_index > 0) {
				//enemy heeft tile bereikt; verzet zijn doel naar de volgende node
				en->path.current_node_index--;
			}
			calc_direction(en->world_x, en->world_y, to_x, to_y, &en->direction_x, &en->direction_y);
			en->angle = find_render_angle(0, en->direction_x, en->direction_y);
			en->world_x += en->speed * en->direction_x;
			en->world_y += en->speed * en->direction_y;
			if (en->world_x == state->world.castle->castle.world_x && en->world_y == state->world.castle->castle.world_y) {
				//enemy heeft kasteel bereikt, trek levenspunten af en despawn enemy
				state->world.castle->castle.health -= en->damage;
				en->alive = 0;
				if (state->world.castle->castle.health <= 0) {
					//in geval dat health onder 0 zou staan
					state->world.castle->castle.health = 0;
					state->game_over = 1;
				}
			}
		}
	}
}

void move_projectiles(GameState* state) {
	register int i, j;
	for (i = 0; i < state->towers_length; i++) {
		for (j = 0; j < state->towers[i]->tower.ammo; j++) {
			Projectile* proj = &state->towers[i]->tower.projectiles[j];
			int from_x = proj->world_x;
			int from_y = proj->world_y;
			if (proj->target != NULL && proj->live) {
				int to_x = proj->target->world_x;
				int to_y = proj->target->world_y;
				float alpha, a, b, c;
				calc_direction(from_x, from_y, to_x, to_y, &proj->direction_x, &proj->direction_y);

				a = fabs(proj->target->world_x - proj->world_x);
				b = fabs(proj->target->world_y - proj->world_y);
				c = euclidean_distance(proj->target->world_x, proj->target->world_y, proj->world_x, proj->world_y);
				alpha = find_alpha(a, b, c);

				proj->world_x += proj->speed * proj->direction_x;
				proj->world_y += proj->speed * proj->direction_y;
				proj->angle = find_render_angle(alpha, proj->direction_x, proj->direction_y);
			} else{
				//verwijder stilstaande projectielen
				proj->live = 0;
			}
		}
	}
}

void update_movement(GameState * state) {
	register int i;

	if (((state->wave.spawn_counter % 10 == 0 && !state->wave.completed) || (state->wave.cool_down_counter % 10 == 0 && state->wave.cool_down_counter != 0)) && state->mana != MANA_MAX) {
		if (state->mana + MANA_UPDATE < MANA_MAX) {
			state->mana += MANA_UPDATE;
		} else {
			state->mana += MANA_MAX - state->mana;
		}
	}
	//als frost wave actief is is er geen movement
	if (!state->spells.frost_wave_active) {
		//pad moet herberekend worden
		if (state->refresh_paths) {
			for (i = 0; i < state->enemies_length; i++) {
				if (state->enemies[i].enemy.alive) {
					refresh_path(&state->enemies[i].enemy.path, &state->world);
				}
			}
			state->refresh_paths = 0;
		}
		move_enemies(state);
	}
	move_projectiles(state);
}

void out_range(Tower* tower, GameState* state) {
	register int p;

	tower->target = find_target(tower, state->enemies, state->enemies_length);
	for (p = 0; p < tower->ammo;p++) {
		//verwijder projectiel
		if (tower->target == NULL) {
			tower->projectiles[p].live = 0;
		} //laat projectiel naar een nieuw doel vliegen
		else {
			if (tower->projectiles[p].live != 0) {
				tower->projectiles[p].target = tower->target;
			}
		}
	}
}

void in_range(Tower* tower, GameState* state) {
	if (tower->shoot_interval - tower->frames_since_last_shot == 0) {
		shoot(tower);
	} else {
		register int p;
		for (p = 0; p < tower->ammo; p++) {
			if (tower->projectiles[p].live) {
				Entity proj, en;
				proj.projectile = tower->projectiles[p];
				en.enemy = *tower->projectiles[p].target;
				if (is_colliding(&proj, &en)) {
					do_damage(&tower->projectiles[p], tower->projectiles[p].target);
					if (is_dead(tower->projectiles[p].target, state)) {
						tower->target = NULL;
						tower->projectiles[p].target = NULL;
					}
				}
			}
		}
	}
}

void do_tower_attacks(GameState * state) {
	register int i,j;
	for (i = 0; i < state->towers_length; i++) {
		Tower* tower = &state->towers[i]->tower;
		if (tower->target == NULL) {
			tower->target = find_target(tower, state->enemies, state->enemies_length);
			for (j = 0; j < tower->ammo; j++) {
				tower->projectiles[j].target = tower->target;
			}
		}else{
			if (!is_out_of_range(tower->projectiles, tower, &state->world)) {
				out_range(tower, state);
			} else {
				in_range(tower, state);
				tower->frames_since_last_shot = (tower->frames_since_last_shot + 1) % (tower->shoot_interval + 1);
			}
		}
	}
}

void init_game_state(GameState * state) {
	//stel de beginwaarden van eigenschappen in
	init_world_from_file(&state->world, "assets/worlds/world1.world");
	state->game_over = 0;
	state->wave.wave_number = 0;
	state->spells.frost_wave_active = 0;
	state->spells.poison_gas_active = 0;
	state->spells.spell_duration = 0;
	init_wave(state);
	state->enemies = (Entity*) malloc(sizeof(Entity));
	state->towers = (Entity**) malloc(sizeof(Entity*));
	state->enemies_length = 0;
	state->towers_length = 0;
	state->world.castle->castle.health = CASTLE_HEALTH;
	state->mana = 1250;
	state->money = 10000;
	state->score = 0;
	state->play = 0;
	init_buttons(&state->hud);
}

void destroy_game_state(GameState * state) {
	register int i;
	for (i = 0; i < state->enemies_length; i++) {
		destroy_path(&state->enemies[i].enemy.path);
	}
	free(state->enemies);
	for (i = 0; i < state->towers_length; i++) {
		destroy_tower(&state->world, state->towers[i]);
	}
	free(state->towers);
	destroy_world(&state->world);
}

void render_game(GameState * state) {
	/* Variables */
	Color color = {255, 0, 255, 255};
	Color black = {0, 0, 0, 255};
	Color white = {255, 255, 255, 255};
	char buffer [20];
	int i,n;

	/* Set redraw off */
	state->redraw = 0;

	/* Render world */
	render_world(&state->world);

	/* Render pathfinding, if enabled */
	if (SHOW_PATHFINDING)
		render_paths(state);

	/* Render towers */
	for (i = 0; i < state->towers_length; i++) {
		render_entity(state->towers[i]);
	}

	/* Render enemies if they are alive */
	for (i = 0; i < state->enemies_length; i++) {
		if (state->enemies[i].enemy.alive)
			render_entity(&state->enemies[i]);
	}

	/* Render spellscreen */
	render_spellscreen(&state->spells);

	/* Render projectiles */
	for (i = 0; i < state->towers_length; i++) {
		render_projectiles(state->towers[i]);
	}

	/* render selection */
	render_mouse_actions(state);

	/* Render ui on top */
	render_ui(state);

	/* Fps rendering */
	n = sprintf(buffer, "%#.1f FPS", get_current_fps());
	draw_text(buffer, FONT_LARGE, color, SCREEN_WIDTH-20 -100, 6, ALIGN_RIGHT);

	/* Game over? */
	render_game_over(state);

	/* Render to screen */
	flip_display();
	clear_to_color(color);
}

void update_mouse(GameState * state, float screen_x, float screen_y) {
	Mouse * mouse = &state->mouse;
	int prev_tile_x = mouse->tile_x;
	int prev_tile_y = mouse->tile_y;

	mouse->screen_x = screen_x;
	mouse->screen_y = screen_y;
	mouse->world_x = convert_screen2world_x(screen_x);
	mouse->world_y = convert_screen2world_y(screen_y);
	mouse->tile_x = convert_screen2tile_x(screen_x);
	mouse->tile_y = convert_screen2tile_y(screen_y);

	mouse->tile_changed |= (mouse->tile_x != prev_tile_x) || (mouse->tile_y != prev_tile_y);
}


/* === Event handlers === */

void mouse_move(MouseMoveEvent * ev, GameState * state) {
	Mouse * mouse = &state->mouse;

	/* Update mouse pointer location */
	update_mouse(state, ev->screen_x, ev->screen_y);

	if (in_world_screen(mouse->screen_x, mouse->screen_y)) {
		if (state->action == BUILD_TOWER) {
			//er is op een tower-knop geklikt; toon de blueprint
			state->blueprint.entity.all.world_x = mouse->world_x;
			state->blueprint.entity.all.world_y = mouse->world_y;
			state->blueprint.valid = (state->world.entities[mouse->tile_x][mouse->tile_y].type == EMPTY && state->money >= state->blueprint.entity.tower.cost);
			init_tower_blueprint(&state->blueprint.entity, state->blueprint.entity.tower.tower_type);
		}
		/* Already implemented code to reset buttons after hovering. DO NOT CHANGE */
		reset_all_buttons_action(state);
	}

	else { /* Outside of world frame => buttons */
		set_button_hover_state(state); /* DO NOT CHANGE */
	}
}

void move_mouse(GameState* state) {
	MouseMoveEvent e;
	e.screen_x = state->mouse.screen_x;
	e.screen_y = state->mouse.screen_y;
	e.screen_dx = e.screen_x;
	e.screen_dy = e.screen_y;
	e.type = EVENT_MOUSE_MOVE;
	mouse_move(&e, state);
}

void mouse_down(MouseDownEvent * ev, GameState * state) {
	if (ev->button == 1) {
		Mouse * mouse = &state->mouse;

		if (in_world_screen(mouse->screen_x, mouse->screen_y)) {
			if (state->action == BUILD_TOWER) {
				//er wordt een toren neergezet
				Entity* entity;
				entity = place_tower(&state->world, state->blueprint.entity.tower.tower_type, mouse->world_x, mouse->world_y);
				if (entity != NULL) {
					if (state->money - entity->tower.cost >= 0) {
						//pas state aan nu de toren geplaatst is
						state->money -= entity->tower.cost;
						state->towers_length++;
						state->towers = (Entity**) realloc(state->towers, state->towers_length * sizeof(Entity*));
						state->towers[state->towers_length - 1] = entity; 
						state->world.entities[mouse->tile_x][mouse->tile_y] = *entity;
						state->refresh_paths = 1;
						//beweeg de muis om te zien of aan te tonen dat hier nu geen toren meer kan staan
						move_mouse(state);
					} else{
						//niet genoeg geld
						destroy_tower(&state->world, entity);
					}
				}
			} else if (state->action == DESTROY_TOWER) {
				//toren moet verwijderd worden
				if (state->world.entities[mouse->tile_x][mouse->tile_y].type == TOWER) {
					register int i = 0;
					Entity *tower = state->towers[i];
					while (!(convert_world2tile_x(tower->all.world_x) == mouse->tile_x && convert_world2tile_y(tower->all.world_y) == mouse->tile_y)) {
						tower = state->towers[i];
						i++;
					}
					//je krijgt de helft van de kost terug als je de toren terug "verkoopt"
					state->money += tower->tower.cost / 2;
					destroy_tower(&state->world, tower);
					if (i != 0) {
						i--;
					}
					//schuif in de array alle elementen na de verwijderde toren één plaats terug
					for (i = i; i < state->towers_length - 1; i++) {
						state->towers[i] = state->towers[i + 1];
					}
					state->towers_length--;
					if (state->towers_length != 0) {
						state->towers = (Entity**) realloc(state->towers, state->towers_length * sizeof(Entity*));
					}
					state->refresh_paths = 1;
				}
			}
		}
		else { /* Outside of world frame => buttons */
			set_button_action(state); /* DO NOT CHANGE */
		}
	}
	else if (ev->button == 2) {
		state->action = NONE;
	}
}

void mouse_up(MouseUpEvent * ev, GameState * state) {
	reset_button_action(state);
}


/* === Implemented methods, DO NOT CHANGE === */

void set_button_hover_state(GameState * state) {
	int i;
	Button * buttons = state->hud.buttons;
	Mouse * mouse = &state->mouse;

	for (i = 0; i < BUTTON_AMOUNT; i++) {
		if (in_bounds(mouse->screen_x, mouse->screen_y, buttons[i].bounds)) {
			buttons[i].state = BUTTON_HOVER;
		} 
		else {
			buttons[i].state = BUTTON_UP;
		}
	}
}

void set_button_action(GameState * state) {
	int i;
	Button * buttons = state->hud.buttons;
	Mouse * mouse = &state->mouse;

	for (i = 0; i < BUTTON_AMOUNT; i++) {
		if (in_bounds(mouse->screen_x, mouse->screen_y, buttons[i].bounds)) {
			buttons[i].state = BUTTON_DOWN;
			switch (i) {
			case BUTTON_PLAY:
				if (!(*state->hud.play)){
					*state->hud.play = 1;
				}
				break;
			case BUTTON_TOWER_MACHINEGUN:
				init_tower_blueprint(&state->blueprint.entity, MACHINE_GUN);
				state->action = BUILD_TOWER;
				break;
			case BUTTON_TOWER_ROCKET:
				init_tower_blueprint(&state->blueprint.entity, ROCKET_LAUNCHER);
				state->action = BUILD_TOWER;
				break;

			case BUTTON_TOWER_FLAK:
				init_tower_blueprint(&state->blueprint.entity, FLAK_CANNON);
				state->action = BUILD_TOWER;
				break;
			case BUTTON_DESTROY_TOWER:
				state->action = DESTROY_TOWER;
				break;
			case BUTTON_SPELL_FREEZE:
				state->action = GENERATE_FROST_WAVE;
				break;
			case BUTTON_SPELL_KILL:
				state->action = RELEASE_POISON_GAS;
				break;
			}
		}
	}
}

void reset_button_action(GameState * state) {
	Mouse * mouse = &state->mouse;
	Button * buttons = state->hud.buttons;
	int i;

	for (i = 0; i < BUTTON_AMOUNT; i++) {
		if (in_bounds(mouse->screen_x, mouse->screen_y, buttons[i].bounds)) {
			buttons[i].state = BUTTON_UP;
		}
	}
}

void reset_all_buttons_action(GameState * state) {
	Button * buttons = state->hud.buttons;
	int i;

	for (i = 0; i < BUTTON_AMOUNT; i++)
		buttons[i].state = BUTTON_UP;
}

//hulpmethode die kijkt of de gegeven enemy dood is 
int is_dead(Enemy* enemy, GameState* state) {
	if (enemy->health <= 0 && enemy->alive) {
		enemy->alive = 0;
		state->money += INCOME_FEE * (enemy->enemy_type + 1);
		state->score += STD_SCORE * (enemy->enemy_type + 1);
		//Kijk of het vakje moet herkleurd worden indien je niet genoeg geld had om de huidige toren te bouwen
		move_mouse(state);
		return 1;
	}
	return 0;
}