/*****************************************
* priority_queue.h
* Titouan Vervack en Caroline De Brouwer
* Groep 24
******************************************/

#ifndef H_PRIORITY_QUEUE
#define H_PRIORITY_QUEUE

#include "entities.h"

typedef struct _Element {
	int priority;
	Node * value;
	struct _Element * previous;
	struct _Element * next;
} Element;

typedef struct {
	int size;
	Element * head;
	Element * tail;
} PriorityQueue;

void pq_init(PriorityQueue * pq);

void pq_offer(PriorityQueue * pq, Node * node);

int pq_is_empty(PriorityQueue * pq);

Node * pq_poll(PriorityQueue * pq);

Node * pq_peek(PriorityQueue * pq);

int pq_contains(PriorityQueue * pq, Node * node);

void pq_remove(PriorityQueue * pq, Node * node);

void pq_destroy(PriorityQueue * pq);

#endif