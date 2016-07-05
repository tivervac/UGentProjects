#ifndef SAFE_ALLOC_H
#define SAFE_ALLOC_H

#include <stdlib.h>

void* s_malloc(size_t size);

void* s_calloc(size_t num, size_t size);

void* s_realloc(void* ptr, size_t size);

void s_free(void* ptr);

#endif