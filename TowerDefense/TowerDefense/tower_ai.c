/*****************************************
* tower_ai.c
* Titouan Vervack en Caroline De Brouwer
* Groep 24
******************************************/

#include "entities.h"
#include "config.h"
#include "tower_ai.h"
#include "world.h"
#include "util.h"
#include <math.h>

int is_valid_target(Tower * t, Enemy * e) {
	return ((t->tower_type == FLAK_CANNON && e->enemy_type == AIR) || (t->tower_type != FLAK_CANNON && e->enemy_type != AIR));
}

Enemy * find_target(Tower * t, Entity * enemies, int enemies_length) {
	register int i;
	int from_x = convert_tile2world_x(convert_world2tile_x(t->world_x)) + (TILE_SIZE / 2);
	int	from_y = convert_tile2world_y(convert_world2tile_y(t->world_y)) + (TILE_SIZE / 2);

	for (i = 0; i < enemies_length; i++) {
		int to_x = convert_tile2world_x(convert_world2tile_x(enemies[i].all.world_x)) + (TILE_SIZE / 2);
		int to_y = convert_tile2world_y(convert_world2tile_y(enemies[i].all.world_y)) + (TILE_SIZE / 2);
		if (enemies[i].enemy.alive) {
			float distance = euclidean_distance(from_x, from_y, to_x, to_y);
			if (distance <= t->range && is_valid_target(t, &enemies[i].enemy)) {
				return &enemies[i].enemy;
			}
		}
	}
	return NULL;
}

void shoot(Tower * t) {
	register int i;
	Projectile proj;
	float alpha, horizontal, vertical;
	int to_x, to_y;
	if (t->target->alive) {
		//Opzettelijke loss-of-data
		proj.world_x = convert_tile2world_x(convert_world2tile_x(t->world_x));
		proj.world_y = convert_tile2world_y(convert_world2tile_y(t->world_y));
		proj.live = 1;
		proj.target = t->target;
		proj.type = PROJECTILE;
		//Opzettelijke loss-of-data
		to_x = convert_tile2world_x(convert_world2tile_x(proj.target->world_x)) + (TILE_SIZE / 2);
		to_y = convert_tile2world_y(convert_world2tile_y(proj.target->world_y)) + (TILE_SIZE / 2);
		calc_direction(proj.world_x, proj.world_y, to_x, to_y, &proj.direction_x, &proj.direction_y);
		horizontal = euclidean_distance(proj.world_x, to_y, to_x, to_y);
		vertical = euclidean_distance(proj.world_x, proj.world_y, proj.world_x, to_y);
		alpha = find_alpha(horizontal, vertical, euclidean_distance(proj.world_x, proj.world_y, to_x, to_y));
		proj.angle = find_render_angle(alpha, proj.direction_x, proj.direction_y);
		switch (t->tower_type) {
		case MACHINE_GUN:
			proj.damage = BULLET_DAMAGE;
			proj.height = BULLET_SIZE;
			proj.width = BULLET_SIZE;
			proj.speed = BULLET_SPEED;
			proj.projectile_type = BULLET;
			break;
		case ROCKET_LAUNCHER:
			proj.damage = ROCKET_DAMAGE;
			proj.height = ROCKET_SIZE;
			proj.width = ROCKET_SIZE;
			proj.speed = ROCKET_SPEED;
			proj.projectile_type = ROCKET;
			break;
		case FLAK_CANNON:
			proj.damage = FLAK_DAMAGE;
			proj.height = FLAK_SIZE;
			proj.width = FLAK_SIZE;
			proj.speed = FLAK_SPEED;
			proj.projectile_type = FLAK;
			break;
		}
		for (i = 0; i < t->ammo; i++) {
			if (t->projectiles[i].live == 0) {
				t->projectiles[i] = proj;
				break;
			}
		}
	} else {
		t->target = NULL;
	}
}

int is_out_of_range(Projectile * p, Tower * t, World * w) {
	float distance;
	int from_x = convert_tile2world_x(convert_world2tile_x(t->world_x)) + (TILE_SIZE / 2);
	int	from_y = convert_tile2world_y(convert_world2tile_y(t->world_y)) + (TILE_SIZE / 2);
	int to_x, to_y;

	if (p->target == NULL) {
		return 0;
	}
	to_x = convert_tile2world_x(convert_world2tile_x(p->target->world_x)) + (TILE_SIZE / 2);
	to_y = convert_tile2world_y(convert_world2tile_y(p->target->world_y)) + (TILE_SIZE / 2);
	distance = euclidean_distance(from_x, from_y, to_x, to_y);
	if (distance <= t->range && is_valid_target(t, p->target) && to_x >= 0 && to_y < convert_tile2world_x(w->width_in_tiles) + TILE_SIZE) {
		return 1;
	} 
	return 0;
}

void do_damage(Projectile * p, Enemy * e) {
	if ((p->projectile_type == FLAK && e->enemy_type == AIR) || (p->projectile_type != FLAK && e->enemy_type != AIR)) {
		e->health -= p->damage;
	}
	p->live = 0;
}