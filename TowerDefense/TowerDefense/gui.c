/*****************************************
* gui.c
* Titouan Vervack en Caroline De Brouwer
* Groep 24
******************************************/

#include <allegro5\allegro.h>
#include <allegro5\allegro_primitives.h>
#include <allegro5\allegro_font.h>
#include <allegro5\allegro_ttf.h>
#include <allegro5\allegro_image.h>
#include <allegro5\allegro_direct3d.h>
#include <stdio.h>
#include "config.h"
#include "pathfinding.h"
#include "gui.h"

/* Variables */
ALLEGRO_DISPLAY *display = NULL;
ALLEGRO_TIMER *timer = NULL;
ALLEGRO_EVENT_QUEUE *eventqueue = NULL;
ALLEGRO_FONT *fonts[5];
ALLEGRO_BITMAP **sprites;
double old_time;
int frames;
int fps;

/* Prototypes */
ALLEGRO_BITMAP * get_sprite(SPRITE_TYPE type);
ALLEGRO_COLOR get_color(Color color);

int init_gui(int width, int height) {
	/* Initialize Allegro */
	if (!al_init())
		return -1;

	/* Create display */
	display = al_create_display(width, height);	

	/* Initialize allegro addons */
	al_init_primitives_addon();
	al_init_font_addon();
	al_init_ttf_addon();
	al_init_image_addon();

	/* Install systems */
	al_install_mouse();

	return 0;
}

void init_game_loop(int fps) {
	/* Fonts */
	fonts[FONT_SMALL] = al_load_ttf_font("assets/fonts/verdana.ttf", 12, 0);
	fonts[FONT_MEDIUM] = al_load_ttf_font("assets/fonts/verdana.ttf", 14, 0);
	fonts[FONT_LARGE] = al_load_ttf_font("assets/fonts/verdana.ttf", 18, 0);
	fonts[FONT_LARGER] = al_load_ttf_font("assets/fonts/verdana.ttf", 45, 0);
	fonts[FONT_HUGE] = al_load_ttf_font("assets/fonts/verdana.ttf", 75, 0);

	/* Create timer */
	timer = al_create_timer(1.0 / fps);
	eventqueue = al_create_event_queue();

	/* Register event sources */
	al_register_event_source(eventqueue, al_get_timer_event_source(timer));
	al_register_event_source(eventqueue, al_get_display_event_source(display));
	al_register_event_source(eventqueue, al_get_mouse_event_source());

	/* Init fps */
	frames = 0;
	fps = 0;
	old_time = al_get_time();

	/* LAST: Start timer */
	al_start_timer(timer);
}

void cleanup_game_loop(void) {
	/* Cleanup fonts */
	al_destroy_font(fonts[FONT_SMALL]);
	al_destroy_font(fonts[FONT_MEDIUM]);
	al_destroy_font(fonts[FONT_LARGE]);
	al_destroy_font(fonts[FONT_LARGER]);
	al_destroy_font(fonts[FONT_HUGE]);
	fonts[FONT_SMALL] = NULL;
	fonts[FONT_MEDIUM] = NULL;
	fonts[FONT_LARGE] = NULL;
	fonts[FONT_LARGER] = NULL;
	fonts[FONT_HUGE] = NULL;

	/* Cleanup eventqueue */
	al_destroy_event_queue(eventqueue);
	eventqueue = NULL;

	/* Cleanup timer */
	al_destroy_timer(timer);
	timer = NULL;

	/* Cleanup display */
	al_destroy_display(display);
	display = NULL;

	/* Shutdown addons */
	al_shutdown_image_addon();
	al_shutdown_ttf_addon();
	al_shutdown_font_addon();
	al_shutdown_primitives_addon();

	/* Uninstall systems */
	al_uninstall_mouse();
}


/* === Drawing === */
void update_frame_animation(FrameAnimator * animator) {
	if (++animator->frameCounter >= animator->frameDelay) {
		animator->curFrame += animator->animationDirection;
		if (animator->curFrame >= animator->maxFrame)
			animator->curFrame = 0;
		else if (animator->curFrame <= 0)
			animator->curFrame = animator->maxFrame - 1;

		animator->frameCounter = 0;
	}
}

void draw_sprite(SPRITE_TYPE type, float screen_x, float screen_y, float angle_radians) {
	int half = TILE_SIZE/2;
	int sub_used = 0;
	ALLEGRO_BITMAP * subbitmap;

	switch (type) {
	case SPRITE_CASTLE:
	case SPRITE_SPAWN:
	case SPRITE_NORMAL:
	case SPRITE_ELITE:
	case SPRITE_FAST:
	case SPRITE_AIR :
	case SPRITE_BOSS:
		subbitmap = al_create_sub_bitmap(get_sprite(type),0, 0, TILE_SIZE, TILE_SIZE);
		sub_used = 1;
	}

	if (angle_radians == 0)
		al_draw_bitmap(sub_used ? subbitmap : get_sprite(type), screen_x, screen_y, 0);
	else
		al_draw_rotated_bitmap(sub_used ? subbitmap : get_sprite(type), half, half, screen_x + half, screen_y + half, angle_radians, 0);

	if (sub_used)
		al_destroy_bitmap(subbitmap);
}


void draw_sprite_animated(SPRITE_TYPE type, FrameAnimator * animator, float screen_x, float screen_y, float angle_radians) {
	int half = TILE_SIZE/2;
	int fx;
	int fy;
	ALLEGRO_BITMAP * subbitmap;

	update_frame_animation(animator);

	fx = (animator->curFrame % animator->animationColumns) * animator->frameWidth;
	fy = (animator->curFrame / animator->animationColumns) * animator->frameHeight;	

	subbitmap = al_create_sub_bitmap(get_sprite(type),fx, fy, animator->frameWidth, animator->frameHeight);

	if (angle_radians == 0)
		al_draw_bitmap(subbitmap, screen_x, screen_y, 0);
	else
		al_draw_rotated_bitmap(subbitmap, half, half, screen_x + half, screen_y + half, angle_radians, 0);


	al_destroy_bitmap(subbitmap);
}

void draw_line(Color color, float screen_sx, float screen_sy, float screen_dx, float screen_dy, float thickness) {
	al_draw_line(screen_sx, screen_sy, screen_dx, screen_dy, get_color(color),thickness);
}

void draw_rectangle(float screen_sx, float screen_sy, float screen_dx, float screen_dy, Color color) {
	al_draw_filled_rectangle(screen_sx, screen_sy, screen_dx, screen_dy, get_color(color));
}

void draw_circle(float screen_x, float screen_y, float r, Color color) {
	al_draw_filled_circle(screen_x, screen_y, r, get_color(color));
}

void draw_triangle(float screen_x1, float screen_y1, float screen_x2, float screen_y2, float screen_x3, float screen_y3, Color color) {
	al_draw_filled_triangle(screen_x1, screen_y1, screen_x2, screen_y2, screen_x3, screen_y3, get_color(color));
}

void draw_text(char* txt, Font font, Color color, float screen_x, float screen_y, ALIGN align) {
	int flag = 0;

	switch (align) {
	case ALIGN_LEFT:
		flag = ALLEGRO_ALIGN_LEFT;
		break;
	case ALIGN_CENTER:
		flag = ALLEGRO_ALIGN_CENTRE;
		break;
	case ALIGN_RIGHT:
		flag = ALLEGRO_ALIGN_RIGHT;
		break;
	default:
		flag = ALLEGRO_ALIGN_LEFT;
	}

	al_draw_text(fonts[font], get_color(color), screen_x, screen_y, flag, txt);
}

void set_transparency_on() {
	al_set_blender(ALLEGRO_ADD, ALLEGRO_ALPHA, ALLEGRO_INVERSE_ALPHA);
}

void set_transparency_off() {
	al_set_blender(ALLEGRO_ADD, ALLEGRO_ONE, ALLEGRO_INVERSE_ALPHA);
}

void clear_to_color(Color color) {
	al_clear_to_color(get_color(color));
}

void flip_display() {
	al_flip_display();
}

void start_drawing_world() {
	al_set_target_bitmap(sprites[SPRITE_WORLD]);
}

void stop_drawing_world() {
	al_set_target_backbuffer(display);
}


ALLEGRO_COLOR get_color(Color color) {
	return al_map_rgba(color.r, color.g, color.b, color.a);
}

/* === Events === */

void wait_for_event(Event *ev) {
	ALLEGRO_EVENT al_evt;

	al_wait_for_event(eventqueue, &al_evt);

	switch (al_evt.type) {
	case ALLEGRO_EVENT_TIMER:
		ev->timerEvent.type = EVENT_TIMER;
		break;
	case ALLEGRO_EVENT_MOUSE_BUTTON_UP:
		ev->mouseUpEvent.type = EVENT_MOUSE_UP;
		ev->mouseUpEvent.button = al_evt.mouse.button;
		ev->mouseUpEvent.screen_x = al_evt.mouse.x;
		ev->mouseUpEvent.screen_y = al_evt.mouse.y;
		break;
	case ALLEGRO_EVENT_MOUSE_BUTTON_DOWN:
		ev->mouseDownEvent.type = EVENT_MOUSE_DOWN;
		ev->mouseDownEvent.button = al_evt.mouse.button;
		ev->mouseDownEvent.screen_x = al_evt.mouse.x;
		ev->mouseDownEvent.screen_y = al_evt.mouse.y;
		break;
	case ALLEGRO_EVENT_MOUSE_AXES:
		ev->mouseMoveEvent.type = EVENT_MOUSE_MOVE;
		ev->mouseMoveEvent.screen_dx = al_evt.mouse.dx;
		ev->mouseMoveEvent.screen_dy = al_evt.mouse.dy;
		ev->mouseMoveEvent.screen_x = al_evt.mouse.x;
		ev->mouseMoveEvent.screen_y = al_evt.mouse.y;
		break;
	case ALLEGRO_EVENT_DISPLAY_CLOSE:
		ev->displayCloseEvent.type = EVENT_DISPLAY_CLOSE;
		break;
	}
}

int all_events_processed(void) {
	return al_is_event_queue_empty(eventqueue);
}


/* === FPS === */

double get_current_fps() {
	double game_time = al_get_time();
	if (game_time - old_time >= 1.0) {
		fps = frames / (game_time - old_time);

		frames = 0;
		old_time = game_time;
	}

	frames++;

	return fps;
}


/* === Sprite caching === */

void init_sprite_cache() {
	/* Dynamic allocation */
	sprites = (ALLEGRO_BITMAP**) malloc(NR_OF_SPRITES * sizeof(ALLEGRO_BITMAP*));

	/* World */
	sprites[SPRITE_WORLD] = al_create_bitmap(SCREEN_WIDTH, SCREEN_HEIGHT);

	/* Preloading of sprites */
	sprites[SPRITE_SPAWN] = al_load_bitmap("assets/sprites/spawn.png");
	sprites[SPRITE_CASTLE] = al_load_bitmap("assets/sprites/castle.png");
	sprites[SPRITE_GRASS] = al_load_bitmap("assets/sprites/grass.png");
	sprites[SPRITE_TOWER_MACHINE_GUN] = al_load_bitmap("assets/sprites/pillbox.png");
	sprites[SPRITE_TOWER_ROCKET_LAUNCHER] = al_load_bitmap("assets/sprites/rocket_tower.png");
	sprites[SPRITE_TOWER_FLAK_CANNON] = al_load_bitmap("assets/sprites/anti-air.png");
	sprites[SPRITE_DESTROY_TOWER] = al_load_bitmap("assets/sprites/destroy_tower.png");
	sprites[SPRITE_MOUTAIN] = al_load_bitmap("assets/sprites/mountain.png");
	sprites[SPRITE_WATER] = al_load_bitmap("assets/sprites/water.png");
	sprites[SPRITE_UI] = al_load_bitmap("assets/sprites/ui.png");
	sprites[SPRITE_NORMAL] = al_load_bitmap("assets/sprites/normal.png");
	sprites[SPRITE_ELITE] = al_load_bitmap("assets/sprites/elite.png");
	sprites[SPRITE_FAST] = al_load_bitmap("assets/sprites/ninja.png");
	sprites[SPRITE_AIR] = al_load_bitmap("assets/sprites/jet.png");
	sprites[SPRITE_BOSS] = al_load_bitmap("assets/sprites/killer_robot.png");
	sprites[SPRITE_BUTTON_BLANCO_UP] = al_load_bitmap("assets/sprites/button_blanco_up.png");
	sprites[SPRITE_BUTTON_BLANCO_HOVER] = al_load_bitmap("assets/sprites/button_blanco_hover.png");
	sprites[SPRITE_BUTTON_BLANCO_DOWN] = al_load_bitmap("assets/sprites/button_blanco_down.png");
	sprites[SPRITE_AMMO_BULLET] = al_load_bitmap("assets/sprites/bullet.png");
	sprites[SPRITE_AMMO_ROCKET] = al_load_bitmap("assets/sprites/rocket.png");
	sprites[SPRITE_AMMO_FLAK] = al_load_bitmap("assets/sprites/flak.png");
	sprites[SPRITE_SPELL_FREEZE] = al_load_bitmap("assets/sprites/spell_frost.png");
	sprites[SPRITE_SPELL_POISON] = al_load_bitmap("assets/sprites/spell_poison.png");

	/* Alpha blending where needed */
	al_convert_mask_to_alpha(sprites[SPRITE_SPAWN], al_map_rgb(255,0,255));
	al_convert_mask_to_alpha(sprites[SPRITE_CASTLE], al_map_rgb(255,0,255));
	al_convert_mask_to_alpha(sprites[SPRITE_NORMAL], al_map_rgb(255,0,255));
	al_convert_mask_to_alpha(sprites[SPRITE_AIR], al_map_rgb(255,0,255));
	al_convert_mask_to_alpha(sprites[SPRITE_BOSS], al_map_rgb(255,0,255));
}

ALLEGRO_BITMAP * get_sprite(SPRITE_TYPE type) {
	return sprites[type];
}

void cleanup_sprite_cache() {
	int i;
	for (i = 0; i < NR_OF_SPRITES; i++) {
		al_destroy_bitmap(sprites[i]);
		sprites[i] = NULL;
	}
	free(sprites);
	sprites = NULL;
}