/*****************************************
* world.c
* Titouan Vervack en Caroline De Brouwer
* Groep 24
******************************************/

#include "entities.h"
#include "world.h"
#include "util.h"
#include "pathfinding.h"
#include "config.h"
#include <stdlib.h>
#include <stdio.h>
#include <string.h>

#define BUFFER_LENGTH 8

void init_world(World * world, int width_in_tiles, int height_in_tiles) {
	register int i, j;
	world->width_in_tiles = width_in_tiles;
	world->height_in_tiles = height_in_tiles;
	world->entities = (Entity**) malloc(width_in_tiles * sizeof(Entity*));
	for (i = 0; i < width_in_tiles; i++) {
		world->entities[i] = (Entity*) malloc(height_in_tiles * sizeof(Entity));
		for (j = 0; j < height_in_tiles; j++) {
			world->entities[i][j].type = EMPTY;
		}
	}
	world->castle = (Entity*) malloc(sizeof(Entity));
	world->castle->type = CASTLE;
	world->spawn = (Entity*) malloc(sizeof(Entity));
	world->spawn->type = SPAWN_LOCATION;
}

void init_world_from_file(World * world, char * worldFileName) {
	FILE* file;

	file = fopen(worldFileName, "r");
	if (file != NULL) {
		char* buffer = (char*) malloc(BUFFER_LENGTH * sizeof(char));
		int line = 0;
		char type;
		int x;
		int y;
		FrameAnimator fa_castle;
		FrameAnimator fa_spawn;

		while (fgets(buffer, BUFFER_LENGTH, file)) {
			switch (line) {
			case 0:
				sscanf(buffer, "%i %i", &x, &y);
				init_world(world, x, y);
				break;
			case 1:
				sscanf(buffer, "%i %i", &x, &y);
				world->spawn->spawn_location.world_x = convert_tile2world_x(x);
				world->spawn->spawn_location.world_y = convert_tile2world_y(y);
				init_frameAnimator(&fa_spawn, 3, 9, 5, TILE_SIZE, TILE_SIZE);
				world->spawn->all.frameAnimator = fa_spawn;
				break;
			case 2:
				sscanf(buffer, "%i %i", &x, &y);
				world->castle->castle.world_x = convert_tile2world_x(x);
				world->castle->castle.world_y = convert_tile2world_y(y);
				init_frameAnimator(&fa_castle, 3, 36, 4, TILE_SIZE, TILE_SIZE);
				world->castle->all.frameAnimator = fa_castle;
				break;
			default:
				sscanf(buffer, "%c %i %i", &type, &x, &y);
				world->entities[x][y].obstacle.world_x = convert_tile2world_x(x);
				world->entities[x][y].obstacle.world_y = convert_tile2world_y(y);
				world->entities[x][y].type = OBSTACLE;
				world->entities[x][y].obstacle.type = OBSTACLE;
				if (type == 'W') {
					world->entities[x][y].obstacle.obstacle_type = WATER;					
				} else if (type == 'M') {
					world->entities[x][y].obstacle.obstacle_type = MOUNTAIN;
				} 
			}
			line++;
		}
		free(buffer);
	} else{
		printf("File kan niet ingelezen worden!");
		init_world(world, 0, 0);
	}
}

Entity * place_tower(World * world, TowerType type, float world_x, float world_y) {
	int tile_x = convert_world2tile_x(world_x);
	int tile_y = convert_world2tile_y(world_y);
	if (world->entities[tile_x][tile_y].type == EMPTY) {
		Path path;
		Entity* tower = (Entity*) malloc(sizeof(Entity));
		init_entity(tower, TOWER, world_x, world_y, TILE_SIZE, TILE_SIZE);
		tower->tower.tower_type = type;
		switch (type) {
		case MACHINE_GUN:
			init_machine_gun(tower, world_x, world_y);
			break;
		case ROCKET_LAUNCHER:
			init_rocket_launcher(tower, world_x, world_y);
			break;
		case FLAK_CANNON:
			init_flak_cannon(tower, world_x, world_y);
			break;
		}
		tower->tower.target = NULL;
		tower->tower.projectiles = (Projectile*) calloc(tower->tower.ammo, sizeof(Projectile));
		world->entities[tile_x][tile_y] = *tower;
		path.current_node_index = 0;
		path.length = 0;
		path.trajectory_type = OVER_LAND;
		path.nodes = (Node*) malloc(sizeof(Node));
		if (create_path(&path, world, world->spawn, world->castle, OVER_LAND) != 0) {
			destroy_path(&path);
			return tower;
		} else {
			world->entities[convert_world2tile_x(tower->all.world_x)][convert_world2tile_y(tower->all.world_y)].type = EMPTY;
			destroy_tower(world, tower);
			destroy_path(&path);
			return NULL;
		}
	}
	return NULL;
}

void destroy_tower(World * world, Entity * tower) {
	int tile_x = convert_world2tile_x(tower->tower.world_x);
	int tile_y = convert_world2tile_y(tower->tower.world_y);
	world->entities[tile_x][tile_y].type = EMPTY;
	free(tower->tower.projectiles);
	free(tower);
}

void destroy_world(World * world) {
	register int i;
	for (i = 0; i < world->width_in_tiles; i++) {
		free(world->entities[i]);
	}
	free(world->entities);
	free(world->castle);
	free(world->spawn);
}