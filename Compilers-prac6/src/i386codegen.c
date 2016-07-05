#include "codegen.h"
#include "table.h"
#include "frame.h"
#include <stdlib.h>

/*
  To get the temporaries representing the framepointer and returnvalue use:
  Temp_temp F_FP();
  Temp_temp F_RV();
*/

void munchStm(T_stm stm);
void munchExp(T_exp exp);

int munchArgs(T_expList args) {
    int i = 0;
    T_expList inv_args = NULL;

    for (; args; args = args->tail) {
        inv_args = T_ExpList(args->head, inv_args);
    }
    for (args = inv_args; args; args = args->tail) {
        munchExp(args->head);
        i++;
    }
    return i;
}

AS_instrList F_codegen(F_frame f, T_stmList stmList) {
    T_stmList iter = NULL;

    /* Procedure entry, to implement stack frame */
    printf("\tpushl\t%%ebp\n");
    printf("\tmovl\t%%esp,%%ebp\n");

    /* reserve space for the locals */
    if (f->n_locals)
        printf("\tsubl\t$%d,%%esp\n", 4 * f->n_locals);

    for (iter = stmList; iter; iter = iter->tail) {
        munchStm(iter->head);
    }

    /* clean space of the locals */
    if (f->n_locals) {
        printf("\taddl\t$%d,%%esp\n", 4 * (f->n_locals));
    }

    /* Procedure exit */
    printf("\tleave\n");
    printf("\tret\n");
    printf("\t\n");
    return 0;
}

int munchSetStackVariable(T_stm stm) {

    if (stm->kind != T_MOVE) return 0;

    T_exp dst = stm->u.MOVE.dst;
    if (dst->kind != T_MEM) return 0;

    T_exp address = dst->u.MEM;
    if (address->kind != T_BINOP) return 0;

    // Check if left side is ebp
    if (address->u.BINOP.left->kind == T_TEMP 
        && address->u.BINOP.left->u.TEMP == F_FP()
        && address->u.BINOP.right->kind == T_CONST) 
    {
        if (address->u.BINOP.op != T_plus 
            && address->u.BINOP.op == T_minus) {
            return 0;
        }
        // Value to set
        munchExp(stm->u.MOVE.src);
        // Set value
        printf("\tpopl %%eax\n");
        printf("\tmovl %%eax,  %s%d(%%ebp)\n", 
            ((address->u.BINOP.op == T_plus ? "" : "-")),
            address->u.BINOP.right->u.CONST);
        return 1;
    }

    return 0;
}

int munchSetArrayIndex(T_stm stm) {

    if (stm->kind != T_MOVE) return 0;

    T_exp dst = stm->u.MOVE.dst;
    if (dst->kind != T_MEM) return 0;

    T_exp address = dst->u.MEM;
    if (address->kind != T_BINOP 
        || address->u.BINOP.op != T_plus) 
        return 0;
    T_exp base = address->u.BINOP.left;

    T_exp index_offset = address->u.BINOP.right;
    if (index_offset->kind != T_BINOP 
        || index_offset->u.BINOP.op != T_mul
        || index_offset->u.BINOP.right->kind != T_CONST) 
        return 0;

    // Calculate value
    munchExp(stm->u.MOVE.src);

    // Base
    munchExp(base); 

    // Index offset
    int scalar = index_offset->u.BINOP.right->u.CONST;
    munchExp(index_offset->u.BINOP.left);  // index

    printf("\tpopl %%ebx\n"); // index
    printf("\tpopl %%ecx\n"); // base
    printf("\tpopl %%edx\n"); // value

    printf("\tmovl %%edx, (%%ecx, %%ebx, %d)\n", scalar);

    return 1;
}


void munchMove(T_stm stm) {

    if (munchSetStackVariable(stm)
        || munchSetArrayIndex(stm)) return;

    munchExp(stm->u.MOVE.src);
        
    if (stm->u.MOVE.dst->kind == T_TEMP) {

        printf("\tpopl %%ebx\n");
        Temp_temp temp = stm->u.MOVE.dst->u.TEMP;

        if (temp == F_FP()) {
            printf("\tmovl %%ebx, %%ebp\n");
        } else if (temp == F_RV()) {
            printf("\tmovl %%ebx, %%eax\n");
        } else {
            printf("\tUnknown Temp!\n");
        }
    } 
    else {
        munchExp(stm->u.MOVE.dst->u.MEM);  // address
        printf("\tpopl %%ebx\n");
        printf("\tpopl %%eax\n");
        printf("\tmovl %%eax, (%%ebx)\n");
    }
}

void munchRelop(T_relOp op, Temp_label truelabel) {
    switch(op) {
    case T_eq:
        printf("\tje %s\n", Temp_labelstring(truelabel));
        break;
    case T_ne:
        printf("\tjne %s\n", Temp_labelstring(truelabel));
        break;
    case T_lt:
        printf("\tjl %s\n", Temp_labelstring(truelabel));
        break;
    case T_gt:
        printf("\tjg %s\n", Temp_labelstring(truelabel));
        break;
    case T_le:
        printf("\tjle %s\n", Temp_labelstring(truelabel));
        break;
    case T_ge:
        printf("\tjge %s\n", Temp_labelstring(truelabel));
        break;
    case T_ult:
        printf("\tjb %s\n", Temp_labelstring(truelabel));
        break;
    case T_ule:
        printf("\tjnae %s\n", Temp_labelstring(truelabel));
        break;
    case T_ugt:
        printf("\tja %s\n", Temp_labelstring(truelabel));
        break;
    case T_uge:
        printf("\tjnbe %s\n", Temp_labelstring(truelabel));
        break;
    }
}

void munchStm(T_stm stm) {
    switch (stm->kind) {
    case T_MOVE:
        munchMove(stm);
        break;
    case T_LABEL:
        printf("%s:\n", Temp_labelstring(stm->u.LABEL));
        break;
    case T_JUMP:
        printf("\tjmp %s\n", Temp_labelstring(stm->u.JUMP.jumps->head));
        break;
    case T_EXP:
        munchExp(stm->u.EXP);
        printf("\tpopl %%ebx\n");
        break;
    case T_CJUMP:
        munchExp(stm->u.CJUMP.left);
        munchExp(stm->u.CJUMP.right);
        printf("\tpopl %%ebx\n");
        printf("\tpopl %%eax\n");
        printf("\tcmp %%ebx, %%eax\n");
        munchRelop(stm->u.CJUMP.op, stm->u.CJUMP.true);
        printf("\tjmp %s\n", Temp_labelstring(stm->u.CJUMP.false));
        break;
    default:
        printf("\tNot implemented yet! %d\n", stm->kind);
    }
}

int munchgetArrayIndex(T_exp exp) {

    if (exp->kind != T_MEM) return 0;

    T_exp address = exp->u.MEM;
    if (address->kind != T_BINOP 
        || address->u.BINOP.op != T_plus) 
        return 0;
    T_exp base = address->u.BINOP.left;

    T_exp index_offset = address->u.BINOP.right;
    if (index_offset->kind != T_BINOP 
        || index_offset->u.BINOP.op != T_mul
        || index_offset->u.BINOP.right->kind != T_CONST) 
        return 0;

    // Base
    munchExp(base); 

    // Index offset
    int scalar = index_offset->u.BINOP.right->u.CONST;
    munchExp(index_offset->u.BINOP.left);  // index

    printf("\tpopl %%eax\n"); // index
    printf("\tpopl %%ebx\n"); // base

    printf("\tmovl (%%ebx, %%eax, %d), %%eax\n", scalar);
    printf("\tpushl %%eax\n");

    return 1;
}

void munchBinop(T_exp exp) {

    munchExp(exp->u.BINOP.left);
    munchExp(exp->u.BINOP.right);

    printf("\tpopl %%ebx\n");
    printf("\tpopl %%eax\n");

    switch(exp->u.BINOP.op) {
        case T_plus:
            printf("\taddl %%ebx, %%eax\n");
            break;
        case T_minus:
            printf("\tsubl %%ebx, %%eax\n");
            break;
        case T_mul:
            printf("\timull %%ebx, %%eax\n");
            break;
        case T_div:
            printf("\tdivl %%ebx, %%eax\n");
            break;
        case T_and:
            printf("\tandl %%ebx, %%eax\n");
            break;
        case T_or:
            printf("\torl %%ebx, %%eax\n");
            break;
        case T_lshift:
            printf("\tshll %%ebx, %%eax\n");
            break;
        case T_rshift:
            printf("\tshrl %%ebx, %%eax\n");
            break;
        case T_arshift:
            // TODO: sall or sarl?
            printf("\tsall %%ebx, %%eax\n");
            break;
        case T_xor:
            printf("\txorl %%ebx, %%eax\n");
            break;
        default:
            printf("\tNot handled yet, implement binop! %d\n", exp->u.BINOP.op);  
    }

    printf("\tpushl %%eax\n");
}

void munchTemp(T_exp exp) {

    Temp_temp temp = exp->u.TEMP;
    if (temp == F_FP()) {
        printf("\tpushl %%ebp\n");
    } 
    else if (temp == F_RV()) {
        printf("\tpushl %%eax\n");
    } 
    else {
        printf("\tUnknown Temp!\n");
    }
}

void munchExp(T_exp exp) {
    int nr_args;
    switch (exp->kind) {
    case T_CONST:
        printf("\tpushl $%d\n", exp->u.CONST);
        break;
    case T_BINOP:
        munchBinop(exp);
        break;
    case T_MEM:
        //if (munchgetArrayIndex(exp)) break;
        munchExp(exp->u.MEM);
        printf("\tpopl %%ebx\n");
        printf("\tmovl (%%ebx), %%eax\n");    // movl [%ebx], %%eax
        printf("\tpushl %%eax\n");
        break;
    case T_TEMP:
        munchTemp(exp);
        break;
    case T_CALL:
        nr_args = munchArgs(exp->u.CALL.args);
        printf("\tcall _%s\n", Temp_labelstring(exp->u.CALL.fun->u.NAME));
        printf("\tsubl $%d, %%esp\n", nr_args*4);  // Clean stack
        printf("\tpushl %%eax\n");
        break;
    case T_NAME:
        printf("\tleal %s, %%eax\n", Temp_labelstring(exp->u.NAME));
        printf("\tpush %%eax\n");
        break;
    case T_ESEQ:
        printf("\tEseq found! Impossible after canonicalize");
        exit(1);
        break;
    default:
        printf("\tNot handled yet, implement! %d\n", exp->kind);
        // exit(1);
    }
}

/* *** DO NOT ADJUST ANYTHING BELOW ***
 * All the information needed is available above
 */

static int tempPresentInList(Temp_tempList lst, Temp_temp temp) {
    for (; lst != 0; lst = lst->tail) {
        if (lst->head == temp)
            return 1;
    }
    return 0;
}

static Temp_tempList addTemp(Temp_tempList temporariesInStmts, Temp_temp temp) {
    if (!tempPresentInList(temporariesInStmts, temp))
        return Temp_TempList(temp, temporariesInStmts);
    else
        return temporariesInStmts;
}

static Temp_tempList collectTemporariesInExpr(Temp_tempList temporariesInStmts,
                                              T_exp exp);

static Temp_tempList collectTemporariesInArgs(Temp_tempList temporariesInStmts,
                                              T_expList args) {
    for (; args; args = args->tail) {
        temporariesInStmts =
            collectTemporariesInExpr(temporariesInStmts, args->head);
    }
    return temporariesInStmts;
}

static Temp_tempList collectTemporariesInExpr(Temp_tempList temporariesInStmts,
                                              T_exp exp) {
    switch (exp->kind) {
    case T_CONST:
        break;
    case T_BINOP:
        temporariesInStmts =
            collectTemporariesInExpr(temporariesInStmts, exp->u.BINOP.left);
        temporariesInStmts =
            collectTemporariesInExpr(temporariesInStmts, exp->u.BINOP.right);
        switch (exp->u.BINOP.op) {
        case T_plus:
        case T_minus:
        case T_mul:
        case T_div:
            break;
        default:
            printf("\tUnexpected operator!!");
            exit(1);
        }
        break;
    case T_MEM:
        temporariesInStmts =
            collectTemporariesInExpr(temporariesInStmts, exp->u.MEM);
        break;
    case T_TEMP:
        if (exp->u.TEMP != F_FP() && exp->u.TEMP != F_RV())
            temporariesInStmts = addTemp(temporariesInStmts, exp->u.TEMP);
        break;
    case T_CALL:
        temporariesInStmts =
            collectTemporariesInArgs(temporariesInStmts, exp->u.CALL.args);
        break;
    case T_NAME:
        break;
    case T_ESEQ:
        printf("\tEseq found! Impossible after canonicalize");
        exit(1);
        break;
    default:
        printf("\tNot handled yet, implement! %d\n", exp->kind);
    }
    return temporariesInStmts;
}

static Temp_tempList collectTemporariesInStmt(Temp_tempList temporariesInStmts,
                                              T_stm stm) {
    switch (stm->kind) {
    case T_MOVE:
        temporariesInStmts =
            collectTemporariesInExpr(temporariesInStmts, stm->u.MOVE.dst);
        temporariesInStmts =
            collectTemporariesInExpr(temporariesInStmts, stm->u.MOVE.src);
        break;
    case T_LABEL:
        break;
    case T_JUMP:
        break;
    case T_EXP:
        temporariesInStmts =
            collectTemporariesInExpr(temporariesInStmts, stm->u.EXP);
        break;
    case T_CJUMP:
        temporariesInStmts =
            collectTemporariesInExpr(temporariesInStmts, stm->u.CJUMP.left);
        temporariesInStmts =
            collectTemporariesInExpr(temporariesInStmts, stm->u.CJUMP.right);
        switch (stm->u.CJUMP.op) {
        case T_eq:
        case T_ne:
        case T_gt:
        case T_ge:
        case T_le:
        case T_lt:
            break;
        default:
            printf("\tImplement relOp %d\n", stm->u.CJUMP.op);
        }
        break;
    default:
        printf("\tNot implemented yet! %d\n", stm->kind);
        // exit(1);
    }
    return temporariesInStmts;
}

static Temp_tempList getListOfTemporaries(T_stmList stmList) {
    /** temporariesInStms is a list that is used to collect the
        set of temporaries in the stmList. */
    Temp_tempList temporariesInStmts = 0;
    T_stmList iter;
    for (iter = stmList; iter; iter = iter->tail) {
        temporariesInStmts =
            collectTemporariesInStmt(temporariesInStmts, iter->head);
    }
    /*#if 0
      {
        Temp_tempList i;
        printf ("Found following temporaries:");
        for(i=temporariesInStmts; i!=0; i=i->tail)
          printf ("T%d, ", *((int*)i->head));
        printf ("\n");
      }
    #endif*/
    return temporariesInStmts;
}

static TAB_table allocTemporaries(F_frame frame,
                                  Temp_tempList temporariesInStmts) {
    Temp_tempList i;
    TAB_table temp2F_access = TAB_empty();
    for (i = temporariesInStmts; i != 0; i = i->tail) {
        F_access tempAccess = F_allocLocal(frame, TRUE);
        TAB_enter(temp2F_access, i->head, tempAccess);
    }
    return temp2F_access;
}

static T_exp replaceTemporariesInExpr(T_exp exp, TAB_table temp2F_access);

static T_expList replaceTemporariesInArgs(T_expList args,
                                          TAB_table temp2F_access) {
    T_expList newList = 0;
    T_expList inv_args = 0;
    for (; args; args = args->tail) {
        inv_args = T_ExpList(args->head, inv_args);
    }
    for (; inv_args; inv_args = inv_args->tail) {
        newList = T_ExpList(
            replaceTemporariesInExpr(inv_args->head, temp2F_access), newList);
    }
    return newList;
}

static T_exp replaceTemporariesInExpr(T_exp exp, TAB_table temp2F_access) {
    switch (exp->kind) {
    case T_CONST:
        break;
    case T_BINOP:
        exp->u.BINOP.left =
            replaceTemporariesInExpr(exp->u.BINOP.left, temp2F_access);
        exp->u.BINOP.right =
            replaceTemporariesInExpr(exp->u.BINOP.right, temp2F_access);
        switch (exp->u.BINOP.op) {
        case T_plus:
        case T_minus:
        case T_mul:
        case T_div:
            break;
        default:
            printf("\tUnexpected operator!!");
            exit(1);
        }
        break;
    case T_MEM:
        exp->u.MEM = replaceTemporariesInExpr(exp->u.MEM, temp2F_access);
        break;
    case T_TEMP:
        if (exp->u.TEMP != F_FP() && exp->u.TEMP != F_RV()) {
            F_access acc = (F_access)TAB_look(temp2F_access, exp->u.TEMP);
            assert(acc != 0);
            return F_Exp(acc, T_Temp(F_FP()));
        }
        break;
    case T_CALL:
        exp->u.CALL.args =
            replaceTemporariesInArgs(exp->u.CALL.args, temp2F_access);
        break;
    case T_NAME:
        break;
    case T_ESEQ:
        printf("\tEseq found! Impossible after canonicalize");
        exit(1);
        break;
    default:
        printf("\tNot handled yet, implement! %d\n", exp->kind);
    }
    return exp;
}

static void replaceTemporariesInStmt(T_stm stm, TAB_table temp2F_access) {
    switch (stm->kind) {
    case T_MOVE:
        stm->u.MOVE.dst =
            replaceTemporariesInExpr(stm->u.MOVE.dst, temp2F_access);
        stm->u.MOVE.src =
            replaceTemporariesInExpr(stm->u.MOVE.src, temp2F_access);
        break;
    case T_LABEL:
        break;
    case T_JUMP:
        break;
    case T_EXP:
        stm->u.EXP = replaceTemporariesInExpr(stm->u.EXP, temp2F_access);
        break;
    case T_CJUMP:
        stm->u.CJUMP.left =
            replaceTemporariesInExpr(stm->u.CJUMP.left, temp2F_access);
        stm->u.CJUMP.right =
            replaceTemporariesInExpr(stm->u.CJUMP.right, temp2F_access);
        switch (stm->u.CJUMP.op) {
        case T_eq:
        case T_ne:
        case T_gt:
        case T_ge:
        case T_le:
        case T_lt:
            break;
        default:
            printf("\tImplement relOp %d\n", stm->u.CJUMP.op);
        }
        break;
    default:
        printf("\tNot implemented yet! %d\n", stm->kind);
        // exit(1);
    }
}


static void changeTemp2localAccess(T_stmList stmList, TAB_table temp2F_access) {
    T_stmList iter;
    for (iter = stmList; iter; iter = iter->tail) {
        replaceTemporariesInStmt(iter->head, temp2F_access);
    }
}

void C_eliminateTemporariesAfterScheduling(F_frame frame, T_stmList stmList) {
    Temp_tempList temporariesInStmts = getListOfTemporaries(stmList);
    TAB_table temp2F_access = allocTemporaries(frame, temporariesInStmts);
    changeTemp2localAccess(stmList, temp2F_access);
}
