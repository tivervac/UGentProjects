/*****************************************
* util.c
* Titouan Vervack en Caroline De Brouwer
* Groep 24
******************************************/

#include "config.h"
#include "util.h"
#include <math.h>

float convert_world2screen_x(float world_x) {
	return world_x + SCREEN_START_X;
}

float convert_world2screen_y(float world_y) {
	return world_y + SCREEN_START_Y;
}

float convert_tile2screen_x(float tile_x) {
	return (tile_x * TILE_SIZE) + SCREEN_START_X;
}

float convert_tile2screen_y(float tile_y) {
	return (tile_y * TILE_SIZE) + SCREEN_START_Y;
}

float convert_tile2world_x(int tile_x) {
	return tile_x * TILE_SIZE;
}

float convert_tile2world_y(int tile_y) {
	return tile_y * TILE_SIZE;
}

float convert_screen2world_x(float screen_x) {
	return screen_x - SCREEN_START_X;
}

float convert_screen2world_y(float screen_y) {
	return screen_y - SCREEN_START_Y;
}

int convert_screen2tile_x(float screen_x) {
	return (screen_x - SCREEN_START_X) / TILE_SIZE;
}

int convert_screen2tile_y(float screen_y) {
	return (screen_y - SCREEN_START_Y) / TILE_SIZE;
}

int convert_world2tile_x(float world_x) {
	return world_x / TILE_SIZE;
}

int convert_world2tile_y(float world_y) {
	return world_y / TILE_SIZE;
}


float euclidean_distance(float x1, float y1, float x2, float y2) {
	return sqrt(pow((x1 - x2), 2) + pow((y1 - y2), 2));
}

float find_alpha(float horizontal_length, float vertical_length, float diagonal_length) {
	float a = vertical_length * vertical_length;
	float b = diagonal_length * diagonal_length;
	float c = horizontal_length * horizontal_length;
	float d = 2 * vertical_length * diagonal_length;
	return acos((a + b - c) / d);
}

void dissolve_speed(float alpha_radians, float speed, float * horizontal, float * vertical) {
	*horizontal = sin(alpha_radians) * speed;
	*vertical = cos(alpha_radians) * speed;
}

void calc_direction(float from_x, float from_y, float to_x, float to_y, int * direction_x, int * direction_y) {
	if (from_x == to_x) {
		*direction_x = 0;
	}
	else{
		*direction_x = from_x < to_x ? 1 : -1;
	}

	if (from_y == to_y) {
		*direction_y = 0;
	}
	else{
		*direction_y = from_y < to_y ? 1 : -1;
	}
}

float find_render_angle(float alpha_radians, int direction_x, int direction_y) {
	if (direction_y < 0) {
		if (direction_x >= 0 && direction_x <= 1) {
			//eerste kwadrant
			return alpha_radians;
		}
		else{
			//vierde kwadrant
			return 2 * PI - alpha_radians;
		}
	}
	else if (direction_y == 0) {
		if (direction_x < 0) {
			//west
			return 3 * PI / 2;
		}
		else{
			//oost
			return PI / 2;
		}
	}
	else{
		if (direction_x >= 0 && direction_x <= 1) {
			//tweede kwadrant
			return PI - alpha_radians;
		}
		else{
			//derde kwadrant
			return PI + alpha_radians;
		}
	}
}

/* === Already implemented, DO NOT CHANGE */

int in_world_screen(float screen_x, float screen_y) {
	return (screen_x >= SCREEN_START_X && screen_x <= SCREEN_WIDTH &&
		screen_y >= SCREEN_START_Y && screen_y <= SCREEN_HEIGHT);
}