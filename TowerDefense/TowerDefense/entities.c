/*****************************************
* entities.c
* Titouan Vervack en Caroline De Brouwer
* Groep 24
******************************************/

#include <stdlib.h>
#include "entities.h"
#include "config.h"
#include "util.h"

void init_entity(Entity * entity, EntityType type, int world_x, int world_y, int width, int height) {
	entity->type = type;
	entity->all.world_x = world_x;
	entity->all.world_y = world_y;
	entity->all.width = width;
	entity->all.height = height;
}

int is_colliding(Entity * e1, Entity * e2) {
	//Laat een kleine speling toe
	return (euclidean_distance(e1->all.world_x, e1->all.world_y, e2->all.world_x, e2->all.world_y) <= 5);
}

void init_tower_blueprint(Entity * entity, TowerType tower_type) {
	entity->tower.tower_type = tower_type;
	switch (tower_type) {
	case MACHINE_GUN:
		init_machine_gun(entity, entity->all.world_x, entity->all.world_y);
		break;
	case ROCKET_LAUNCHER:
		init_rocket_launcher(entity, entity->all.world_x, entity->all.world_y);
		break;
	case FLAK_CANNON:
		init_flak_cannon(entity, entity->all.world_x, entity->all.world_y);
		break;
	}
}

void init_machine_gun(Entity * entity, float world_x, float world_y) {
	entity->tower.ammo = MACHINE_GUN_AMMO;
	entity->tower.cost = MACHINE_GUN_COST;
	entity->tower.range = MACHINE_GUN_RANGE;
	entity->tower.shoot_interval = MACHINE_GUN_SHOOT_INTERVAL;
	entity->tower.frames_since_last_shot = MACHINE_GUN_SHOOT_INTERVAL;
}

void init_rocket_launcher(Entity * entity, float world_x, float world_y) {
	entity->tower.ammo = ROCKET_LAUNCHER_AMMO;
	entity->tower.cost = ROCKET_LAUNCHER_COST;
	entity->tower.range = ROCKET_LAUNCHER_RANGE;
	entity->tower.shoot_interval = ROCKET_LAUNCHER_INTERVAL;
	entity->tower.frames_since_last_shot = ROCKET_LAUNCHER_INTERVAL;
}

void init_flak_cannon(Entity * entity, float world_x, float world_y) {
	entity->tower.ammo = FLAK_CANNON_AMMO;
	entity->tower.cost = FLAK_CANNON_COST;
	entity->tower.range = FLAK_CANNON_RANGE;
	entity->tower.shoot_interval = FLAK_CANNON_INTERVAL;
	entity->tower.frames_since_last_shot = FLAK_CANNON_INTERVAL;
}

void init_enemy(Entity* enemy, EnemyType type) {
	FrameAnimator fa;
	enemy->enemy.path.current_node_index = 0;
	enemy->enemy.path.length = 0;
	enemy->enemy.path.trajectory_type = (type == AIR) ? OVER_AIR : OVER_LAND;
	enemy->enemy.path.nodes = (Node*) malloc(sizeof(Node));
	enemy->enemy.alive = 1;
	enemy->enemy.enemy_type = type;
	enemy->enemy.angle = 0;
	//Orienteer richting oost
	enemy->enemy.direction_x = 1;
	enemy->enemy.direction_y = 0;
	switch (type) {
	case NORMAL:
		enemy->enemy.damage = ENEMY_NORMAL_DAMAGE;
		enemy->enemy.speed  = ENEMY_NORMAL_SPEED;
		init_frameAnimator(&fa, 2, 4, 5, TILE_SIZE, TILE_SIZE);
		break;
	case FAST:
		enemy->enemy.damage = ENEMY_FAST_DAMAGE;
		enemy->enemy.speed = ENEMY_FAST_SPEED;
		init_frameAnimator(&fa, 2, 4, 5, TILE_SIZE, TILE_SIZE);
		break;
	case AIR:
		enemy->enemy.damage = ENEMY_AIR_DAMAGE;
		enemy->enemy.speed = ENEMY_AIR_SPEED;
		init_frameAnimator(&fa, 2, 4, 5, TILE_SIZE, TILE_SIZE);
		break;
	case ELITE:
		enemy->enemy.damage = ENEMY_ELITE_DAMAGE;
		enemy->enemy.speed = ENEMY_ELITE_SPEED;
		init_frameAnimator(&fa, 3, 4, 5, TILE_SIZE, TILE_SIZE);
		break;
	case BOSS:
		enemy->enemy.damage = ENEMY_BOSS_DAMAGE;
		enemy->enemy.speed = ENEMY_BOSS_SPEED;
		init_frameAnimator(&fa, 3, 4, 5, TILE_SIZE, TILE_SIZE);
		break;
	}
	enemy->all.frameAnimator = fa;
}

/* === Implemented functions, DO NOT CHANGE! */

void init_frameAnimator(FrameAnimator * animator, int animationColumns, int maxFrame, int frameDelay, int frameHeight, int frameWidth) {
	animator->animationColumns = animationColumns;
	animator->animationDirection = 1;
	animator->curFrame = 0;
	animator->frameCounter = 0;
	animator->frameDelay = frameDelay;
	animator->frameHeight = frameHeight;
	animator->frameWidth =  frameWidth;
	animator->maxFrame = maxFrame;
}