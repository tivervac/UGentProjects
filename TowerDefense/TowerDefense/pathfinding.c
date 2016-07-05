/*****************************************
* pathfinding.c
* Titouan Vervack en Caroline De Brouwer
* Groep 24
******************************************/

#include <stdlib.h>
#include "entities.h"
#include "world.h"
#include "util.h"
#include "pathfinding.h"
#include "config.h"
#include "priority_queue.h"
#include "hash_set.h"
#include <math.h>
#include <stdio.h>

#define DEFAULT_MOVE_COST 10

/* Useful definitions for neighbour calculation */
#define NR_OF_DIRECTIONS 4
typedef enum {UP, DOWN, LEFT, RIGHT} Directions;
const int x_movement[NR_OF_DIRECTIONS] = {0, 0, -1, 1};
const int y_movement[NR_OF_DIRECTIONS] = {1, -1, 0, 0}; 

PriorityQueue open_pq;
HashSet closed_hs;

int find_path(World * world, Node * start, Node * goal, Node ** path_array, TrajectoryType trajectory_type) {
	register int i;
	//check de buren in alle richtingen naast de gegeven node
	for (i = 0; i < NR_OF_DIRECTIONS; i++) {
		int new_x = start->tile_x - x_movement[i];
		int new_y = start->tile_y - y_movement[i];
		Node* neighbour = (Node*) calloc(1, sizeof(Node));
		//kijken of deze buur wel in de wereld ligt
		if (new_x >= 0 && new_x < world->width_in_tiles && new_y >= 0 && new_y < world->height_in_tiles) {
			Entity ent = world->entities[new_x][new_y];
			//controleren dat deze buur valid is
			if ((ent.type == EMPTY || ent.type == SPAWN_LOCATION || ent.type == CASTLE) 
				|| (ent.type == OBSTACLE && trajectory_type == OVER_AIR && ent.obstacle.obstacle_type == WATER)) {
				neighbour->tile_x = new_x;
				neighbour->tile_y = new_y;
			}else{
				free(neighbour);
				neighbour = NULL;
			}
		} else{
			free(neighbour);
			neighbour = NULL;
		}
		path_array[i] = neighbour;
	}
	return 1;
}

void fix_goal(Entity* to, Node* goal) {
	NodeScore goal_score;

	goal->tile_x = convert_world2tile_x(to->castle.world_x);
	goal->tile_y = convert_world2tile_y(to->castle.world_y);

	goal_score.g = 0;
	goal_score.h = 0;
	goal_score.f = goal_score.h + goal_score.g;

	goal->score = goal_score;
	goal->parent = NULL;
}

int create_path(Path * path, World * world, Entity * from, Entity * to, TrajectoryType trajectory_type) {
	register int i;
	int result;
	Node* start = (Node*)malloc(sizeof(Node));
	Node* goal = (Node*)malloc(sizeof(Node));
	Node* current_element;
	Node* path_array[NR_OF_DIRECTIONS];
	NodeScore start_score;
	fix_goal(to, goal);

	//initialiseer de startwaarden
	pq_init(&open_pq);
	hs_init(&closed_hs);
	start->tile_x = convert_world2tile_x(from->all.world_x);
	start->tile_y = convert_world2tile_y(from->all.world_y);
	start_score.g = 0;
	start_score.h = (abs(start->tile_x - goal->tile_x) + abs(start->tile_y - goal->tile_y)) * DEFAULT_MOVE_COST;
	start_score.f = start_score.h + start_score.g;
	start->parent = NULL;
	start->score = start_score;
	pq_offer(&open_pq, start);
	current_element = pq_poll(&open_pq);
	while (current_element != NULL) {
		hs_add(&closed_hs, current_element);
		find_path(world, current_element, goal, path_array, trajectory_type);
		//find_path geeft 4 buren terug in path_array, NULL als de buur niet valid is
		for (i = 0; i < NR_OF_DIRECTIONS; i++) {
			if (path_array[i] != NULL) {
				if (!hs_contains(&closed_hs, path_array[i])) {
					int g = current_element->score.g + DEFAULT_MOVE_COST;
					int h = (abs(path_array[i]->tile_x - goal->tile_x) + abs(path_array[i]->tile_y - goal->tile_y)) * DEFAULT_MOVE_COST;
					int f = g + h;

					if (pq_contains(&open_pq, path_array[i])) {
						Element *current = open_pq.head;
						while (!(current->value->tile_x == path_array[i]->tile_x && current->value->tile_y == path_array[i]->tile_y)) {
							current = current->next;
						}
						if (g < current->value->score.g) {
							Node* temp = current->value;
							path_array[i]->parent = current_element;
							path_array[i]->score.g = g;
							path_array[i]->score.h = h;
							path_array[i]->score.f = f;
							pq_remove(&open_pq, current->value);
							free(temp);
							pq_remove(&open_pq, path_array[i]);
							pq_offer(&open_pq, path_array[i]);
						} else {
							free(path_array[i]);
						}
					} else{
						Node* new_element = (Node*)malloc(sizeof(Node));
						new_element->parent = current_element;
						new_element->score.g = g;
						new_element->score.h = h;
						new_element->score.f = f;
						new_element->tile_x = path_array[i]->tile_x;
						new_element->tile_y = path_array[i]->tile_y;
						free(path_array[i]);
						pq_offer(&open_pq,new_element);
					}
				} else{
					free(path_array[i]);
				}
			}
		}
		if (!pq_contains(&open_pq, goal)) {
			current_element = pq_poll(&open_pq);
		} else {
			goal->parent = current_element;
			current_element = NULL;
		}
	}
	result = pq_is_empty(&open_pq) ? 0 : 1;

	//Vul Path
	{
		Node* current_node = goal;
		while (current_node != NULL) {
			path->nodes[path->length] = *current_node; 
			path->length++;
			path->nodes = (Node*) realloc(path->nodes, (path->length + 1) * sizeof(Node));
			current_node = current_node->parent; 
		}
		path->current_node_index = path->length - 1;
	}
	pq_destroy(&open_pq);
	hs_destroy(&closed_hs);
	free(goal);
	return result;
}

int refresh_path(Path * path, World * world) {
	Entity start;
	TrajectoryType titi = path->trajectory_type;
	start.all.world_x = convert_tile2world_x(path->nodes[path->current_node_index].tile_x);
	start.all.world_y = convert_tile2world_y(path->nodes[path->current_node_index].tile_y);
	start.all.type = ENEMY;
	destroy_path(path);
	path->nodes = (Node*) malloc(sizeof(Node));
	path->length = 0;
	path->trajectory_type = titi;
	return create_path(path, world, &start, world->castle, titi);
}

void destroy_path(Path * path) {
	free(path->nodes);
}