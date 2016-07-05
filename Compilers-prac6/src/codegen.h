#include "frame.h"
#include "tree.h"
#include "assem.h"

AS_instrList F_codegen(F_frame f, T_stmList stmList);
/** C_eliminateTemporariesAfterScheduling
    reserves frame slots for temporaries introduced after
    canonalization and construction basic blocks. The following
    procedure also replaces reference to temporaries with code
    that explicitly fetches or stores to the locations on the stack
    of the temporaries. As a result, after executing the following
    lines, the only remaining temporaries in the stmList should
    be the frame pointer or the return value.

    This function is not present in the discussion in the Tiger book.
    It provides a hack to avoid register allocation (as briefly discussed
    in the Tiger book in chapter 12 "Putting it all together").
    @author Kristof Beyls
*/
void C_eliminateTemporariesAfterScheduling(F_frame frame, T_stmList stmList);

void munchStm(T_stm stm);
void munchExp(T_exp exp);

#if defined(__linux__)
/* linux */
#define PREPEND_GLOBAL_SYMBOLS "_"
#define PREPEND_TIG_GLOBAL_SYMBOLS "_tig_"
#elif defined(__CYGWIN__) || defined(__APPLE__)
/* cygwin and macOSX; gcc 3.3.x, need two underscores before global names. */
#define PREPEND_GLOBAL_SYMBOLS "__"
#define PREPEND_TIG_GLOBAL_SYMBOLS "__tig_"
#endif
