/*****************************************
* hash_set.c
* Titouan Vervack en Caroline De Brouwer
* Groep 24
******************************************/

#include <stdlib.h>
#include "hash_set.h"

void hs_init(HashSet * hs) {
	register int i;
	hs->nr_of_buckets = DEFAULT_BUCKETS;
	hs->size = 0;
	hs->buckets = (HashEntry**) malloc(sizeof(HashEntry*) * DEFAULT_BUCKETS);
	for (i = 0; i < DEFAULT_BUCKETS; i++) {
		hs->buckets[i] = (HashEntry*) calloc(1, sizeof(HashEntry));
	}
}

void hs_add(HashSet * hs, Node * node) {
	HashEntry* current_entry;
	if (!hs_contains(hs, node)) {
		//bereken de hashwaarde en voeg toe op de juiste positie
		int hash_waarde = hs_calc_hash(node) % hs -> nr_of_buckets;
		HashEntry* new_entry = (HashEntry*) malloc(sizeof(HashEntry));
		new_entry->value = node;
		new_entry->next = NULL;
		current_entry = hs->buckets[hash_waarde];
		if (current_entry->value == NULL) {
			free(current_entry);
			hs->buckets[hash_waarde] = new_entry;
		} else {
			while (current_entry->next != NULL) {
				current_entry = current_entry->next;
			}
			current_entry->next = new_entry;
		}
		hs->size++;
		hs_rehash(hs);
	}
}

int hs_contains(HashSet *hs, Node * node) {
	HashEntry* current_entry = hs -> buckets[hs_calc_hash(node) % hs -> nr_of_buckets];
	//overloop alle elementen in deze bucket
	while (current_entry->value != NULL) {
		if (current_entry->value->tile_x == node->tile_x && current_entry->value->tile_y == node->tile_y) {
			return 1;
		} else {
			current_entry = current_entry->next;
			if (current_entry == NULL) {
				break;
			}
		}
	}
	return 0;
}

void hs_remove(HashSet * hs, Node * node) {
	int hashwaarde = hs_calc_hash(node) % hs -> nr_of_buckets;
	HashEntry* current_entry = hs->buckets[hashwaarde];
	HashEntry* previous_entry = current_entry;
	while (current_entry != NULL) {
		if (current_entry->value == node) {
			if (previous_entry != current_entry) {
				previous_entry->next = current_entry->next;
			}
			hs -> size--;
			break;
		} else {
			//element niet gevonden, verzet current naar de volgende entry
			previous_entry = current_entry;
			current_entry = current_entry->next;
		}
	}
}

Node * hs_get_node(HashSet * hs, int tile_x, int tile_y) {
	HashEntry* current_entry = hs -> buckets[(tile_x << DEFAULT_BUCKETS | tile_y & 0xFFFF) % hs -> nr_of_buckets];
	while (current_entry != NULL) {
		int x = current_entry -> value -> tile_x;
		int y = current_entry -> value -> tile_y;
		if (x == tile_x && y == tile_y) {
			return current_entry->value;
		} else {
			current_entry = current_entry->next;
		}
	}
	return NULL;
}

/* Geeft 0 terug als er niet opnieuw gehasht moet worden
Geeft de nieuwe aantal buckets terug als er wel opnieuw gehasht moet worden 
Already implemented, do not change!
*/
int rehash_nr_of_buckets(const HashSet* hs) {
	if (hs->size > 8 * hs->nr_of_buckets) {
		return 16 * hs->nr_of_buckets;
	}
	else if (hs->nr_of_buckets > DEFAULT_BUCKETS && hs->nr_of_buckets > 8 * hs->size) {
		return hs->nr_of_buckets / 16;
	}
	return 0;
}

void hs_rehash(HashSet * hs) {
	register int i;
	int rehash;
	if ((rehash = rehash_nr_of_buckets(hs)) != 0) {
		hs->buckets = (HashEntry**) realloc(hs->buckets, rehash * sizeof(HashEntry*));
		for (i = hs->nr_of_buckets; i < rehash; i++) {
			hs->buckets[i] = (HashEntry*) calloc(1, sizeof(HashEntry));
		}
		hs->nr_of_buckets = rehash;
	}
}

unsigned int hs_calc_hash(Node * node) {
	return ((node->tile_x) << DEFAULT_BUCKETS) | ((node->tile_y) & 0xFFFF);
}

void hs_destroy(HashSet * hs) {
	register int i;

	for (i = 0; i < hs->nr_of_buckets; i++) {
		HashEntry* current_entry = hs->buckets[i];
		if (current_entry == NULL) {
			free(current_entry);
		} else {
			while (current_entry != NULL) {
				HashEntry* temp = current_entry;
				current_entry = current_entry->next;
				free(temp->value);
				free(temp);
			}
			free(current_entry);
		}
	}
	free(hs -> buckets);
}