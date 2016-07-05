/*****************************************
* config.h
* Titouan Vervack en Caroline De Brouwer
* Groep 24
******************************************/

#ifndef H_CONFIG
#define H_CONFIG

#include <allegro5\allegro.h>

#define FPS 60

/* Debug veriables */
#define RUN_TEST 0
#define SHOW_PATHFINDING 0
#define SHOW_GRID 0

/* Map size variables */
#define TILE_SIZE 32
#define SCREEN_WIDTH 1026
#define SCREEN_HEIGHT 576
#define BUTTON_START_X 0
#define BUTTON_START_Y 32
#define BUTTON_WIDTH 162
#define BUTTON_HEIGHT 64
#define BUTTON_AMOUNT 7
#define SCREEN_START_X 162
#define SCREEN_START_Y 32

/* Gameplay variables */

/* 1. Spells */
#define MANA_MAX 5000
#define MANA_UPDATE 12
#define STD_SCORE 24
#define POISON_DAMAGE 11
#define POISON_GAS_COST 5000
#define FROST_WAVE_COST 3000
#define FROST_WAVE_DURATION (FPS * 10)
#define POISON_GAS_DURATION (FPS * 5)

/* 2. Machine gun tower */
#define BULLET_DAMAGE 5
#define BULLET_SPEED 10
#define BULLET_SIZE 2
#define MACHINE_GUN_AMMO 50
#define MACHINE_GUN_RANGE (TILE_SIZE*2)
#define MACHINE_GUN_COST 50
#define MACHINE_GUN_SHOOT_INTERVAL (FPS/4)

/* 3. Rocket launcher tower */
#define ROCKET_DAMAGE 50
#define ROCKET_SPEED 1.4
#define ROCKET_SIZE 6
#define ROCKET_LAUNCHER_AMMO 2
#define ROCKET_LAUNCHER_RANGE (TILE_SIZE*4)
#define ROCKET_LAUNCHER_COST 200
#define ROCKET_LAUNCHER_INTERVAL (FPS*2)

/* 4. Flak cannon tower */
#define FLAK_DAMAGE 20
#define FLAK_SPEED 5
#define FLAK_SIZE 6
#define FLAK_CANNON_AMMO 3
#define FLAK_CANNON_RANGE (TILE_SIZE*6)
#define FLAK_CANNON_COST 400
#define FLAK_CANNON_INTERVAL (FPS/2)

/* Enemy */
#define ENEMY_SIZE 14
#define AIR_SPAWN_RATIO 0.1
#define FAST_SPAWN_RATIO 0.05

/* 1. Normal */
#define ENEMY_NORMAL_DAMAGE 1
#define ENEMY_NORMAL_HEALTH 100
#define ENEMY_NORMAL_SPEED 1

/* 2. Elite */
#define ENEMY_ELITE_DAMAGE 5
#define ENEMY_ELITE_HEALTH 200
#define ENEMY_ELITE_SPEED 1

/* 3. Fast */
#define ENEMY_FAST_DAMAGE 5
#define ENEMY_FAST_HEALTH 60
#define ENEMY_FAST_SPEED 2

/* 4. Air */
#define ENEMY_AIR_DAMAGE 5
#define ENEMY_AIR_HEALTH 80
#define ENEMY_AIR_SPEED 1

/* 5. Boss */
#define ENEMY_BOSS_DAMAGE 20
#define ENEMY_BOSS_HEALTH 500
#define ENEMY_BOSS_SPEED 1

#define CASTLE_HEALTH 150
#define INCOME_FEE 35

#define PI ALLEGRO_PI

#endif