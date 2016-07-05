/*****************************************
* entities.h
* Titouan Vervack en Caroline De Brouwer
* Groep 24
******************************************/
#ifndef H_ENTITIES
#define H_ENTITIES

#include "util.h"

typedef enum {EMPTY, PROJECTILE, ENEMY, SPAWN_LOCATION, CASTLE, TOWER, OBSTACLE} EntityType;
typedef enum {BULLET, ROCKET, FLAK} ProjectileType;
typedef enum {NORMAL, ELITE, FAST, AIR, BOSS} EnemyType;
typedef enum {MACHINE_GUN, ROCKET_LAUNCHER, FLAK_CANNON} TowerType;
typedef enum {WATER, MOUNTAIN} ObstacleType;
typedef enum {OVER_LAND, OVER_AIR} TrajectoryType;

typedef struct {
	int f;
	int g;
	int h;
} NodeScore;

typedef struct _Node {
	int tile_x;
	int tile_y;
	NodeScore score;
	struct _Node * parent;
} Node;

typedef struct {
	Node * nodes;
	int current_node_index;
	int length;
	TrajectoryType trajectory_type;
} Path;

typedef struct {
	int maxFrame;
	int curFrame;	
	int frameCounter;
	int frameDelay;
	int frameWidth;
	int frameHeight;
	int animationColumns;
	int animationDirection;
} FrameAnimator;

typedef struct {
	EntityType type;
	float world_x;
	float world_y;
	int width;
	int height;
	FrameAnimator frameAnimator;
} BaseEntity;

typedef struct {
	EntityType type;
	float world_x;
	float world_y;
	int width;
	int height;
	FrameAnimator frameAnimator;
	EnemyType enemy_type;
	float angle;
	int direction_x;
	int direction_y;
	float speed;
	int damage;
	int health;
	int health_max;
	int alive;
	Path path;
} Enemy;

typedef struct {
	EntityType type;
	float world_x;
	float world_y;
	int width;
	int height;
	FrameAnimator frameAnimator;
	ProjectileType projectile_type;
	Enemy * target;
	float angle;
	int direction_x;
	int direction_y;
	int damage;
	int live;
	float speed;
} Projectile;

typedef struct {
	EntityType type;
	float world_x;
	float world_y;
	int width;
	int height;
	FrameAnimator frameAnimator;
} SpawnLocation;

typedef struct {
	EntityType type;
	float world_x;
	float world_y;
	int width;
	int height;
	FrameAnimator frameAnimator;
	int health;
} Castle;

typedef struct {
	EntityType type;
	float world_x;
	float world_y;
	int width;
	int height;
	FrameAnimator frameAnimator;
	TowerType tower_type;
	float range;
	int shoot_interval;
	int frames_since_last_shot;
	int ammo;
	int cost;
	Enemy * target;
	Projectile* projectiles;
} Tower;

typedef struct {
	EntityType type;
	float world_x;
	float world_y;
	int width;
	int height;
	FrameAnimator frameAnimator;
	ObstacleType obstacle_type;
} Obstacle;

typedef union {
	EntityType type;
	BaseEntity all;
	Projectile projectile;
	Enemy enemy;
	SpawnLocation spawn_location;
	Castle castle;
	Tower tower;
	Obstacle obstacle;
} Entity;

/*
 * Initialises an Entity.
 */
void init_entity(Entity * entity, EntityType type, int world_x, int world_y, int width, int height);

/*
 * Initialize an enemy.
 */
void init_enemy(Entity * enemy, EnemyType type);

/*
 * Checks if two Entities are colliding.
 * If colliding, returns 1.
 */
int is_colliding(Entity * e1, Entity * e2);


/* === Tower factory functions === */

/*
 * Initialises a blueprint of a certain tower type.
 */
void init_tower_blueprint(Entity * entity, TowerType);

/*
 * Initialises a machine gun tower.
 */
void init_machine_gun(Entity * entity, float world_x, float world_y);

/*
 * Initialises a rocket launcher tower.
 */
void init_rocket_launcher(Entity * entity, float world_x, float world_y);

/*
 * Initialises a flak cannon tower.
 */
void init_flak_cannon(Entity * entity, float world_x, float world_y);

/*
 * Initialises a frameAnimator for use with animated sprites.
 */
void init_frameAnimator(FrameAnimator * animator, int animationColumns, int maxFrame, int frameDelay, int frameHeight, int frameWidth);

#endif