/*****************************************
* hud.c
* Titouan Vervack en Caroline De Brouwer
* Groep 24
******************************************/

#include <stdio.h>
#include "config.h"
#include "gui.h"
#include "hud.h"

void init_buttons(Hud * hud) {
	int i;
	Button * buttons = hud->buttons;
	for (i = 0; i < BUTTON_AMOUNT; i++) {

		buttons[i].bounds.screen_sx = BUTTON_START_X;
		buttons[i].bounds.screen_sy = BUTTON_START_Y + i*BUTTON_HEIGHT;
		buttons[i].bounds.screen_dx = BUTTON_START_X + BUTTON_WIDTH;
		buttons[i].bounds.screen_dy = BUTTON_START_Y + (i+1)*BUTTON_HEIGHT;
		buttons[i].state = BUTTON_UP;
	}
}

void render_buttons(Hud * hud) {
	Color black = {0,0,0,255};
	Color grey = {64,64,64,255};
	Color washedout = {48,48,48, 80};
	Button * buttons = hud->buttons;
	char buffer[20];
	int i;

	for (i = 0; i < BUTTON_AMOUNT; i++) {
		int cost;

		/* Render button background */
		switch (buttons[i].state) {
		case BUTTON_UP:
			draw_sprite(SPRITE_BUTTON_BLANCO_UP, buttons[i].bounds.screen_sx, buttons[i].bounds.screen_sy,0);
			break;
		case BUTTON_DOWN:
			draw_sprite(SPRITE_BUTTON_BLANCO_DOWN, buttons[i].bounds.screen_sx, buttons[i].bounds.screen_sy,0);
			break;
		case BUTTON_HOVER:
			draw_sprite(SPRITE_BUTTON_BLANCO_HOVER, buttons[i].bounds.screen_sx, buttons[i].bounds.screen_sy,0);
			break;
		}

		/* Render button text */
		switch (i) {
		case BUTTON_PLAY:
			cost = 0;
			draw_text("Play", FONT_LARGER, black, buttons[i].bounds.screen_sx + 30,  buttons[i].bounds.screen_sy, ALIGN_LEFT);
			break;
		case BUTTON_TOWER_MACHINEGUN:
			cost = MACHINE_GUN_COST;
			draw_text("Machine gun", FONT_MEDIUM, black, buttons[i].bounds.screen_sx+6,  buttons[i].bounds.screen_sy +8, ALIGN_LEFT);
			sprintf(buffer, "$ %d,-", cost);
			draw_text(buffer, FONT_MEDIUM, grey, buttons[i].bounds.screen_sx+6,  buttons[i].bounds.screen_sy +38, ALIGN_LEFT);
			draw_text("Tower", FONT_SMALL, grey, buttons[i].bounds.screen_dx-8,  buttons[i].bounds.screen_sy +40, ALIGN_RIGHT);
			draw_sprite(SPRITE_AMMO_BULLET, buttons[i].bounds.screen_dx - 32, buttons[i].bounds.screen_sy+4, 0); 
			break;
		case BUTTON_TOWER_ROCKET:
			cost = ROCKET_LAUNCHER_COST;
			draw_text("Rocket launcher", FONT_MEDIUM, black, buttons[i].bounds.screen_sx+6,  buttons[i].bounds.screen_sy +8, ALIGN_LEFT);
			sprintf(buffer, "$ %d,-", cost);
			draw_text(buffer, FONT_MEDIUM, grey, buttons[i].bounds.screen_sx+6,  buttons[i].bounds.screen_sy +38, ALIGN_LEFT);
			draw_text("Tower", FONT_SMALL, grey, buttons[i].bounds.screen_dx-8,  buttons[i].bounds.screen_sy +40, ALIGN_RIGHT);
			draw_sprite(SPRITE_AMMO_ROCKET, buttons[i].bounds.screen_dx - 32, buttons[i].bounds.screen_sy+4, 0); 
			break;
		case BUTTON_TOWER_FLAK:
			cost = FLAK_CANNON_COST;
			draw_text("Flak cannon", FONT_MEDIUM, black, buttons[i].bounds.screen_sx+6,  buttons[i].bounds.screen_sy +8, ALIGN_LEFT);
			sprintf(buffer, "$ %d,-", cost);
			draw_text(buffer, FONT_MEDIUM, grey, buttons[i].bounds.screen_sx+6,  buttons[i].bounds.screen_sy +38, ALIGN_LEFT);
			draw_text("Tower", FONT_SMALL, grey, buttons[i].bounds.screen_dx-8,  buttons[i].bounds.screen_sy +40, ALIGN_RIGHT);
			draw_sprite(SPRITE_AMMO_FLAK, buttons[i].bounds.screen_dx - 32, buttons[i].bounds.screen_sy+4, 0); 
			break;
		case BUTTON_DESTROY_TOWER:
			cost = 0;
			draw_text("Destroy", FONT_MEDIUM, black, buttons[i].bounds.screen_sx+6,  buttons[i].bounds.screen_sy +8, ALIGN_LEFT);
			sprintf(buffer, "$ %d,-", cost);
			draw_text(buffer, FONT_MEDIUM, grey, buttons[i].bounds.screen_sx+6,  buttons[i].bounds.screen_sy +38, ALIGN_LEFT);
			draw_text("Tower", FONT_SMALL, grey, buttons[i].bounds.screen_dx-8,  buttons[i].bounds.screen_sy +40, ALIGN_RIGHT);
			draw_sprite(SPRITE_DESTROY_TOWER, buttons[i].bounds.screen_dx - 32, buttons[i].bounds.screen_sy+4, 0); 
			break;
		case BUTTON_SPELL_FREEZE:
			cost = FROST_WAVE_COST;
			draw_text("Frost wave", FONT_MEDIUM, black, buttons[i].bounds.screen_sx+6,  buttons[i].bounds.screen_sy +8, ALIGN_LEFT);
			sprintf(buffer, "%d mana", cost);
			draw_text(buffer, FONT_MEDIUM, grey, buttons[i].bounds.screen_sx+6,  buttons[i].bounds.screen_sy +38, ALIGN_LEFT);
			draw_text("Spell", FONT_SMALL, grey, buttons[i].bounds.screen_dx-8,  buttons[i].bounds.screen_sy +40, ALIGN_RIGHT);
			draw_sprite(SPRITE_SPELL_FREEZE, buttons[i].bounds.screen_dx - 38, buttons[i].bounds.screen_sy+6, 0); 
			break;
		case BUTTON_SPELL_KILL:
			cost = POISON_GAS_COST;
			draw_text("Poison gas", FONT_MEDIUM, black, buttons[i].bounds.screen_sx+6,  buttons[i].bounds.screen_sy +8, ALIGN_LEFT);
			sprintf(buffer, "%d mana", cost);
			draw_text(buffer, FONT_MEDIUM, grey, buttons[i].bounds.screen_sx+6,  buttons[i].bounds.screen_sy +38, ALIGN_LEFT);
			draw_text("Spell", FONT_SMALL, grey, buttons[i].bounds.screen_dx-8,  buttons[i].bounds.screen_sy +40, ALIGN_RIGHT);
			draw_sprite(SPRITE_SPELL_POISON, buttons[i].bounds.screen_dx - 38, buttons[i].bounds.screen_sy+6, 0); 
			break;
		}

		/* Draw veil if can't afford */
		if (i < 3 && *(hud->money) < cost) {
			set_transparency_on();
			draw_rectangle(buttons[i].bounds.screen_sx, buttons[i].bounds.screen_sy,buttons[i].bounds.screen_dx, buttons[i].bounds.screen_dy, washedout);
			set_transparency_off();
		}
		else if (i >= 3 && *(hud->mana) < cost) {
			set_transparency_on();
			draw_rectangle(buttons[i].bounds.screen_sx, buttons[i].bounds.screen_sy,buttons[i].bounds.screen_dx, buttons[i].bounds.screen_dy, washedout);
			set_transparency_off();
		}
	}
}

void update_hud(Hud * hud, long * score, long * mana, long * money, int * castle_health, int * wave_number, int * play) {
	hud->score = score;
	hud->mana = mana;
	hud->money = money;
	hud->castle_health = castle_health;
	hud->wave_number = wave_number;
	hud->play = play;
}

int in_bounds(float screen_x, float screen_y, Bounds bounds) {
	return (screen_x >= bounds.screen_sx &&	screen_x < bounds.screen_dx &&
		screen_y >= bounds.screen_sy && screen_y < bounds.screen_dy);
}