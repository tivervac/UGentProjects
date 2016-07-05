#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>


static const char characters[256][2] = {
    "\x00", "\x01", "\x02", "\x03", "\x04", "\x05", "\x06", "\x07", "\x08",
    "\x09", "\x0a", "\x0b", "\x0c", "\x0d", "\x0e", "\x0f", "\x10", "\x11",
    "\x12", "\x13", "\x14", "\x15", "\x16", "\x17", "\x18", "\x19", "\x1a",
    "\x1b", "\x1c", "\x1d", "\x1e", "\x1f", "\x20", "\x21", "\x22", "\x23",
    "\x24", "\x25", "\x26", "\x27", "\x28", "\x29", "\x2a", "\x2b", "\x2c",
    "\x2d", "\x2e", "\x2f", "\x30", "\x31", "\x32", "\x33", "\x34", "\x35",
    "\x36", "\x37", "\x38", "\x39", "\x3a", "\x3b", "\x3c", "\x3d", "\x3e",
    "\x3f", "\x40", "\x41", "\x42", "\x43", "\x44", "\x45", "\x46", "\x47",
    "\x48", "\x49", "\x4a", "\x4b", "\x4c", "\x4d", "\x4e", "\x4f", "\x50",
    "\x51", "\x52", "\x53", "\x54", "\x55", "\x56", "\x57", "\x58", "\x59",
    "\x5a", "\x5b", "\x5c", "\x5d", "\x5e", "\x5f", "\x60", "\x61", "\x62",
    "\x63", "\x64", "\x65", "\x66", "\x67", "\x68", "\x69", "\x6a", "\x6b",
    "\x6c", "\x6d", "\x6e", "\x6f", "\x70", "\x71", "\x72", "\x73", "\x74",
    "\x75", "\x76", "\x77", "\x78", "\x79", "\x7a", "\x7b", "\x7c", "\x7d",
    "\x7e", "\x7f", "\x80", "\x81", "\x82", "\x83", "\x84", "\x85", "\x86",
    "\x87", "\x88", "\x89", "\x8a", "\x8b", "\x8c", "\x8d", "\x8e", "\x8f",
    "\x90", "\x91", "\x92", "\x93", "\x94", "\x95", "\x96", "\x97", "\x98",
    "\x99", "\x9a", "\x9b", "\x9c", "\x9d", "\x9e", "\x9f", "\xa0", "\xa1",
    "\xa2", "\xa3", "\xa4", "\xa5", "\xa6", "\xa7", "\xa8", "\xa9", "\xaa",
    "\xab", "\xac", "\xad", "\xae", "\xaf", "\xb0", "\xb1", "\xb2", "\xb3",
    "\xb4", "\xb5", "\xb6", "\xb7", "\xb8", "\xb9", "\xba", "\xbb", "\xbc",
    "\xbd", "\xbe", "\xbf", "\xc0", "\xc1", "\xc2", "\xc3", "\xc4", "\xc5",
    "\xc6", "\xc7", "\xc8", "\xc9", "\xca", "\xcb", "\xcc", "\xcd", "\xce",
    "\xcf", "\xd0", "\xd1", "\xd2", "\xd3", "\xd4", "\xd5", "\xd6", "\xd7",
    "\xd8", "\xd9", "\xda", "\xdb", "\xdc", "\xdd", "\xde", "\xdf", "\xe0",
    "\xe1", "\xe2", "\xe3", "\xe4", "\xe5", "\xe6", "\xe7", "\xe8", "\xe9",
    "\xea", "\xeb", "\xec", "\xed", "\xee", "\xef", "\xf0", "\xf1", "\xf2",
    "\xf3", "\xf4", "\xf5", "\xf6", "\xf7", "\xf8", "\xf9", "\xfa", "\xfb",
    "\xfc", "\xfd", "\xfe", "\xff"
};

static void run_time_error() {
    fprintf(stderr, "Run-time error; I'm halting execution.\n");
    exit(-1);
}


/************************************************
 * Definitions of the build-in tiger functions. *
 ************************************************/

/*
 * I/O
 * */

void _print(void *staticlink, const char *s) {
    printf(s);
    fflush(stdout);
}

void _flush(void *staticlink) { fflush(stdout); }

const char *_getchar(void *staticlink) {
    char *result = 0;
    int character = getchar();
    result = malloc(sizeof(char));
    if (character == EOF) {
        *result = '\0';
    } else {
        *result = (char)character;
    }
    return result;
}


/*
 * char & string manipulation
 * */

int _ord(void *staticlink, const char *s) {
    if (*s == '\0')
        return -1;
    else
        return (int)*s;
}

const char *_chr(void *staticlink, int i) {
    if (0 <= i && i <= 255) {
        fflush(stdout);
        return characters[i];
    } else {
        fprintf(stderr, "Integer argument %d to _chr() out of bounds!\n", i);
        run_time_error();
    }
    return 0;
}

int _size(void *staticlink, const char *s) { return strlen(s); }

const char *_substring(void *staticlink, const char *s, int first, int n) {
    const int len = strlen(s);
    if (n < 0) {
        fprintf(stderr,
                "Requesting substring of negative length %d, in _substring!\n",
                n);
        run_time_error();
    }
    if (len < first + n) {
        fprintf(stderr,
                "Requesting substring containing characters %d to %d,"
                " in _substring, for string containing only %d characters.\n",
                first, first + n, len);
        run_time_error();
    }
    return s + first;
}

const char *_concat(void *staticlink, const char *s1, const char *s2) {
    const int len1 = strlen(s1), len2 = strlen(s2);
    char *result = malloc(sizeof(char) * (len1 + len2 + 1));
    strcpy(result, s1);
    strcpy(result + len1, s2);
    return result;
}

char *_int2string(void *staticlink, int i) {
    int len = 1; // ending \0
    int j = i;

    if (i == 0)
        return "0";
    else
        for (; j >= 1; j /= 10)
            len++;

    char *s = malloc(sizeof(char) * len);
    sprintf(s, "%d", i);
    return s;
}


/*
 * mathematical & logical operations
 * */

int _not(void *staticlink, int i) { return i == 0; }

int _mod(void *staticlink, int i, int d) { return i % d; }


/*
 * 'record' (i.e. structure) allocation
 * */

void *_malloc(int size) {
    void *result = malloc(size);

    if (size < 0) {
        fprintf(stderr, "Requesting a chunk of memory of negative size %d!",
                size);
        run_time_error();
    }

    return result;
}


/*
 * int array allocation and bounds checking
 * */

/* for now, only single-dimensional integer arrays are allowed. */
void *_initArray(int size, int init) {
    int *result = malloc(sizeof(int) * (size + 1));
    int i;

    if (size < 0) {
        fprintf(stderr, "Requesting an array with negative size %d!", size);
        run_time_error();
    }
    result[0] = size;
    result++;
    for (i = 0; i < size; i++) {
        result[i] = init;
    }
    return result;
}

int _arraySize(void *staticlink, int *array) { return array[-1]; }

void _boundsCheckFailed(void *staticlink, int index, int *array,
                        const char *filename, int linenr, int colnr) {
    fprintf(
        stderr,
        "array out of bounds at \"%s\":%d:%d, size of array was %d, index %d\n",
        filename, linenr, colnr, _arraySize(staticlink, array), index);
    exit(-2);
}

int _boundsCheck(void *staticlink, int index, int *array, const char *filename,
                 int linenr, int colnr) {
    const int arraySize = _arraySize(staticlink, array);
    if (index < 0 || arraySize <= index) {
        _boundsCheckFailed(staticlink, index, array, filename, linenr, colnr);
        return 0;
    } else
        return index;
}


/*
 * time measuring
 * */
static clock_t clk = 0;

void _init_clock(void *staticlink) { clk = clock(); }

void _diff_clock(void *staticlink) {
    clock_t t = clock();
    fprintf(stderr, "elapsed time : %ld ticks = %.2lf seconds\n", (t - clk),
            (double)(t - clk) / CLOCKS_PER_SEC);
}


/*
 * programm exit
 * */

void _tig_exit(void *staticlink, int i) { exit(i); }


/*
 * main program: calls the tiger _outerprogram
 * */

extern void _outerprogram(void *staticlink);

int main() {
#if 0
  !defined(__APPLE__)
  clock_t t1=0,t2=0;   
  t1=clock();
#endif

    _outerprogram(0);

#if 0 
  !defined(__APPLE__)
  /* FIXME : MacOSX : upon exit of _outerprogram a bus error occurs : corrupted stack, illegal return address ?!?!?  */
  t2=clock();
  fprintf (stderr, "elapsed time: %ld ticks = %.2lf seconds\n", t2-t1, (double)(t2-t1)/CLOCKS_PER_SEC);
#endif

    return 0;
}
