/*****************************************
* hash_set.h
* Titouan Vervack en Caroline De Brouwer
* Groep 24
******************************************/

#ifndef H_HASH_SET
#define H_HASH_SET

#include "entities.h"

#define DEFAULT_BUCKETS 16

typedef struct _HashEntry {
	Node * value;
	struct _HashEntry* next;
} HashEntry;

typedef struct {
	int nr_of_buckets;
	int size;
	HashEntry ** buckets;
} HashSet;

void hs_init(HashSet * hs);

void hs_add(HashSet * hs, Node * node);

int hs_contains(HashSet *hs, Node * node);

void hs_remove(HashSet * hs, Node * node);

/* Geen standaard hash set functie, maar nuttig bij implementatie A* */
Node * hs_get_node(HashSet * hs, int tile_x, int tile_y);

void hs_rehash(HashSet *hs);

unsigned int hs_calc_hash(Node * node);

void hs_destroy(HashSet * hs);

#endif