/*****************************************
* priority_queue.c
* Titouan Vervack en Caroline De Brouwer
* Groep 24
******************************************/

#include <stdlib.h>
#include "entities.h"
#include "priority_queue.h"

void pq_init(PriorityQueue * pq) {
	pq -> head = NULL;
	pq -> tail = NULL;
	pq -> size = 0;
}

void insert_before(PriorityQueue * pq, Element * new_element, Element * target_element) {
	new_element->next = target_element;
	new_element->previous = target_element->previous;
	if (target_element->previous != NULL) {
		target_element->previous->next = new_element;
	}else{
		pq->head = new_element;
	}
	target_element->previous = new_element;
	pq->size++;
}

void insert_after(PriorityQueue * pq, Element * new_element, Element * target_element)
{
	new_element->previous = target_element;
	new_element->next = target_element -> next;
	if (target_element->next != NULL) {
		target_element->next->previous = new_element;
	}else{
		pq->tail = new_element;
	}
	target_element->next = new_element;
	pq -> size++;
}

void pq_offer(PriorityQueue* pq, Node* node) {
	Element* new_element = (Element*) calloc(1, sizeof(Element));
	new_element->value=node;
	if (pq->head != NULL) {
		Element* current_element = pq -> head;
		while (current_element != NULL) {
			if (current_element->value->score.f >= node->score.f) {
				insert_before(pq, new_element, current_element);
				return;
			}
			current_element = current_element->next;
		}
		insert_after(pq, new_element, pq->tail);
	}else{
		pq->head = new_element;
		pq->tail = new_element;
		pq->size++;
	}
}

int pq_is_empty(PriorityQueue * pq) {
	return (pq->size == 0);
}

Node * pq_poll(PriorityQueue * pq) {
	Node* temp;
	if (pq->size == 0) {
		return NULL;
	}
	temp = pq->head->value;
	pq_remove(pq, pq->head->value);
	return temp;
}

Node * pq_peek(PriorityQueue * pq) {
	if (pq->size == 0) {
		return NULL;
	}
	return pq ->head->value;
}

int pq_contains(PriorityQueue * pq, Node * node) {	
	Element * el = pq->head;
	while (el != NULL) {
		if (el->value->tile_x == node->tile_x && el->value->tile_y == node->tile_y) {
			return 1;
		}
		el = el->next;
	}
	return 0;
}

void pq_remove(PriorityQueue* pq, Node* node) {
	Element* current_element = pq->head;
	while (current_element != NULL) {
		if (current_element->value == node) {
			if (current_element->next != NULL) {
				current_element->next->previous = current_element->previous;
			} else {
				pq->tail = current_element->previous;
			}
			if (current_element->previous != NULL) {
				current_element->previous->next = current_element->next;
			} else {
				pq->head = current_element->next;
			}
			free(current_element);
			pq->size--;
			break;
		} else {
			current_element = current_element->next;
		}
	}
}

void pq_destroy(PriorityQueue * pq) {
	Element* current = pq->head;
	while (current != NULL) {
		Element* temp = current;
		current = current->next;
		free(temp->value);
		free(temp);
	}
}