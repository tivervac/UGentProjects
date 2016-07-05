/*****************************************
* pathfinding.h
* Titouan Vervack en Caroline De Brouwer
* Groep 24
******************************************/

#ifndef H_PATH_FINDING
#define H_PATH_FINDING

#include "world.h"

int create_path(Path * path, World * world, Entity * from, Entity * to, TrajectoryType trajectory_type);

int refresh_path(Path * path, World * world);

void destroy_path(Path * path);

#endif