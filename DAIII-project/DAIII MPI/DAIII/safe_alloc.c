#include "safe_alloc.h"

/*
* This function malloc's size bytes and checks if the malloc was successful
* @arg size: the size to malloc
* @return ptr: the checked pointer
*/
void* s_malloc(size_t size) {
	void* ptr;
	ptr = malloc(size);
	if (ptr == NULL) {
		exit(1);
	}

	return ptr;
}

/*
* This function calloc's size bytes for num elements and checks if the calloc was successful
* @arg num: the number of elements to allocate
* @arg size: the size of each element
* @return ptr: the checked pointer
*/
void* s_calloc(size_t num, size_t size) {
	void* ptr;
	ptr = calloc(num, size);
	if (ptr == NULL) {
		exit(1);
	}

	return ptr;
}

/*
* This function realloc's ptr to size bytes and checks if the realloc was successful
* @arg ptr: the pointer to a preallocated memory block or a null pointer
* @arg size: the new size for the memory block
* @return ptr: the checked pointer
*/
void* s_realloc(void* ptr, size_t size) {
	ptr = realloc(ptr, size);
	if (ptr == NULL) {
		exit(1);
	}

	return ptr;
}

/*
* This function free's the memory and sets it to NULL
* @arg ptr: the pointer to free
*/
void s_free(void* ptr) {
	free(ptr);
	ptr = NULL;
}