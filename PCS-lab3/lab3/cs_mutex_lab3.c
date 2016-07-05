/**
 * Experimenting with synchronization and memory consistency.
 * Algorithm2 (mutex_lab3) version of critical sections.
 *
 * Author: Andreas Sandberg
 *
 */

#include <assert.h>

#include "lab3.h"

#if defined(__GNUC__) && defined(__SSE2__)
/** Macro to insert memory fences */
#define MFENCE() __builtin_ia32_mfence()
#else
#error Memory fence macros not implemented for this platform.
#endif

static volatile int flag[2] = { 0, 0 };
static volatile int turn = 0;

/**
 * Enter a critical section. Implementation using Algorithm2 (mutex_lab3).
 *
 * \param thread Thread ID, either 0 or 1.
 */
static void
impl_enter_critical(int thread)
{
        assert(thread == 0 || thread == 1);

        /* HINT: Since Algorithm2 only works for 2 threads,
         * with the ID 0 and 1, you may use !thread to get the ID the
         * other thread. */
        flag[thread] = 1;
        MFENCE();
        while (flag[!thread]) {
            if (turn != thread) {
                flag[thread] = 0;
                while (turn != thread);
                flag[thread] = 1;
                MFENCE();
            }
        }
}

/**
 * Exit from a critical section.
 *
 * \param thread Thread ID, either 0 or 1.
 */
static void
impl_exit_critical(int thread)
{
        assert(thread == 0 || thread == 1);

        turn = !thread;
        flag[thread] = 0;
}


critical_section_impl_t cs_impl_mutex_lab3 = {
        .name = "mutex_lab3",
        .desc = "Algorithm2 mutex_lab3",

        .max_threads = 2,

        .enter = &impl_enter_critical,
        .exit = &impl_exit_critical
};

/*
 * Local Variables:
 * mode: c
 * c-basic-offset: 8
 * indent-tabs-mode: nil
 * c-file-style: "linux"
 * End:
 */
