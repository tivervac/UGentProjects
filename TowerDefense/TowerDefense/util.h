/*****************************************
* util.h
* Titouan Vervack en Caroline De Brouwer
* Groep 24
******************************************/

#ifndef H_UTIL
#define H_UTIL

/* Helpful struct typedefs */
typedef struct {
	unsigned char r;
	unsigned char g;
	unsigned char b;
	unsigned char a;
} Color;

typedef enum {FONT_SMALL, FONT_MEDIUM, FONT_LARGE, FONT_LARGER, FONT_HUGE} Font;

typedef enum {ALIGN_LEFT, ALIGN_CENTER, ALIGN_RIGHT} ALIGN;
typedef enum {RECTANGLE, CIRCLE, TRIANGLE} PRIMITIVE;
typedef enum {KEY_NONE, KEY_E, KEY_T, KEY_SPACE, KEY_F, KEY_K} KEY;

/* === Events === */

typedef enum {EVENT_TIMER, EVENT_MOUSE_MOVE, EVENT_MOUSE_DOWN, EVENT_MOUSE_UP, EVENT_DISPLAY_CLOSE} EVENT_TYPE;

typedef struct {
	EVENT_TYPE type;
} TimerEvent;

typedef struct {
	EVENT_TYPE type;
} DisplayCloseEvent;

typedef struct {
	EVENT_TYPE type;
	int screen_x;
	int screen_y;
	int screen_dx;
	int screen_dy;
} MouseMoveEvent;

typedef struct {
	EVENT_TYPE type;
	int screen_x;
	int screen_y;
	int button;
} MouseDownEvent;

typedef struct {
	EVENT_TYPE type;
	int screen_x;
	int screen_y;
	int button;
} MouseUpEvent;

typedef union {
	EVENT_TYPE type;
	TimerEvent timerEvent;
	DisplayCloseEvent displayCloseEvent;
	MouseMoveEvent mouseMoveEvent;
	MouseDownEvent mouseDownEvent;
	MouseUpEvent mouseUpEvent;
} Event;




/* === Conversion methods === */

float convert_world2screen_x(float world_x);
float convert_world2screen_y(float world_y);

float convert_tile2screen_x(float tile_x);
float convert_tile2screen_y(float tile_y);

float convert_tile2world_x(int tile_x);
float convert_tile2world_y(int tile_y);

float convert_screen2world_x(float screen_x);
float convert_screen2world_y(float screen_y);

int convert_screen2tile_x(float screen_x);
int convert_screen2tile_y(float screen_y);

int convert_world2tile_x(float world_x);
int convert_world2tile_y(float world_y);


/* === Goniometrical functions === */

/*
 * Calculate the euclidian distance between two points.
 */
float euclidean_distance(float x1, float y1, float x2, float y2);

/*
 * Find the alph angle in the origin given the following side lengths and quadrant.
 */
float find_alpha(float horizontal_length, float vertical_length, float diagonal_length);

/*
 * Dissolve the speed in horizontal en vertical components.
 */
void dissolve_speed(float alpha_radians, float speed, float * horizontal, float * vertical);

/*
 * Infers the directions and puts them in the respective variables.
 */
void calc_direction(float from_x, float from_y, float to_x, float to_y, int * direction_x, int * direction_y);

/*
 * Convert alpha to an angle in [0,2*PI] for rendering.
 * In radians
 */
float find_render_angle(float alpha_radians, int direction_x, int direction_y);


/* === Other util methods === */

int in_world_screen(float screen_x, float screen_y);

#endif