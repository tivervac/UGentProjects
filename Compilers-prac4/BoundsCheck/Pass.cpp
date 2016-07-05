#include <llvm/IR/DebugInfo.h>
#include <llvm/IR/Function.h>
#include <llvm/IR/Instructions.h>
#include <llvm/IR/IRBuilder.h>
#include <llvm/Pass.h>
#include <llvm/Support/Debug.h>

#include <list>

#define DEBUG_TYPE "cheetah::boundscheck"

using namespace llvm;

namespace {
struct BoundsCheck : public FunctionPass {
private:
  IRBuilder<> *Builder;
  Function *Assert = nullptr;

public:
  static char ID;
  BoundsCheck() : FunctionPass(ID) {
    Builder = new IRBuilder<>(getGlobalContext());
  }

  /// Entry point of the pass; this function performs the actual analysis or
  /// transformation, and is called for each function in the module.
  ///
  /// The returned boolean should be `true` if the function was modified,
  /// `false` if it wasn't.
  bool runOnFunction(Function &F) override {
    DEBUG({
      dbgs() << "BoundsCheck: processing function '";
      dbgs().write_escaped(F.getName()) << "'\n";
    });

    // Instantiate the assert function once per module
    if (Assert == nullptr || Assert->getParent() != F.getParent())
      Assert = getAssertFunction(F.getParent());

    // Find all GEP instructions
    // NOTE: we need to do this first, because the iterators get invalidated
    //       when modifying underlying structures
    std::list<GetElementPtrInst *> WorkList;
    for (auto &FI : F) {    // Iterate function -> basic blocks
      for (auto &BI : FI) { // Iterate basic block -> instructions
        if (auto *GEP = dyn_cast<GetElementPtrInst>(&BI))
          WorkList.push_back(GEP);
      }
    }

    // Process any GEP instructions
    bool Changed = false;
    for (auto *GEP : WorkList) {
      DEBUG(dbgs() << "BoundsCheck: found a GEP, " << *GEP << "\n");

      // Get type GEP in indexing
      auto *PtrType = GEP->getPointerOperandType();
      // Only do boundchecks on arrays
      if (PtrType->isPointerTy()) {

        auto *ArrType = PtrType->getPointerElementType();
        if (ArrType->isArrayTy()) {
          // Check if base index is constant zero
          Value *BaseOp = GEP->getOperand(1);
          if (ConstantInt *Base = dyn_cast<ConstantInt>(BaseOp)) {
            // Check if base is zero
            assert(Base->isZero());
          }
          else {
            // Not supported yet: length of array type should be constant
            DebugLoc Loc = GEP->getDebugLoc();
            std::string Error = "";
            if (!Loc.isUnknown()) {
                std::string Input;
                auto Stream = new raw_string_ostream(Input);
                Loc.print(getGlobalContext(), *Stream);
                Error = Stream->str();
            }

            report_fatal_error(Error += "Base of element indexing not a constant!", true);
          }

          // Get bound & index
          uint64_t NumElems = ArrType->getArrayNumElements();
          Value *IndexOp = GEP->getOperand(2);

          // Check bound
          if (ConstantInt *Index = dyn_cast<ConstantInt>(IndexOp)) {
            // Static check
            APInt IndexValue = Index->getValue();
            if (IndexValue.sge(NumElems) || IndexValue.isNegative()) {
              DebugLoc Loc = GEP->getDebugLoc();

              std::string Error = "";
              if (!Loc.isUnknown()) {
                 std::string Input;
                 auto Stream = new raw_string_ostream(Input);
                 Loc.print(getGlobalContext(), *Stream);
                 *Stream << " ";
                 Error = Stream->str();
              }
              report_fatal_error(Error += "out-of-bounds array access", true);
            }
          }
          else {
            // Runtime check

            // Make sure index value is of integer type
            Type *IndexTy = IndexOp->getType();
            Type *IntegerTy = Builder->getInt32Ty();
            assert(IndexTy == IntegerTy);

            // Split Basic block at GEP instruction
            auto *CurrentBlock = GEP->getParent();
            BasicBlock *TrueBlock = CurrentBlock->splitBasicBlock(GEP, Twine("bound_ok"));
            CurrentBlock->getTerminator()->eraseFromParent();

            // Create false block
            BasicBlock *FalseBlock = BasicBlock::Create(getGlobalContext(), Twine("out_of_bound"), TrueBlock->getParent());
            Builder->SetInsertPoint(FalseBlock);
            // Create constant arguments
            Value *Assertion = Builder->CreateGlobalStringPtr("out-of-bounds array access", Twine("out_of_bounds_message"));

            // Get source code info
            DebugLoc Loc = GEP->getDebugLoc();
            unsigned Linenumber = 0;
            std::string FileName = "File";
            if (!Loc.isUnknown()) {
                MDNode *LocNode = Loc.getAsMDNode(getGlobalContext());
                DILocation LocInfo = DILocation(LocNode);
                FileName = LocInfo.getFilename();
                Linenumber = Loc.getLine();
            }

            Value *File = Builder->CreateGlobalStringPtr(FileName, Twine("file"));
            Value *Line = ConstantInt::get(IntegerTy, Linenumber);
            Builder->CreateCall3(Assert, Assertion, File, Line);
            Builder->CreateUnreachable();

            // Check for index > 0
            BasicBlock *SecondCheck = BasicBlock::Create(getGlobalContext(), Twine("underflow_check"), TrueBlock->getParent());
            Builder->SetInsertPoint(SecondCheck);
            Value *GtZeroCond = Builder->CreateICmpSGE(IndexOp, ConstantInt::get(IntegerTy, 0));
            Builder->CreateCondBr(GtZeroCond, TrueBlock, FalseBlock);

            // Add check & cond branch at end of current block
            Builder->SetInsertPoint(CurrentBlock);
            Value *NumElemsValue = Builder->getInt32(NumElems);
            Value *Cond = Builder->CreateICmpSLT(IndexOp, NumElemsValue);
            Builder->CreateCondBr(Cond, SecondCheck, FalseBlock);

            Changed = true;
          }
        }
      }
    }

    return Changed;
  }

private:
  /// Get a function object pointing to the Sys V '__assert' function.
  ///
  /// This function displays a failed assertion, together with the source
  /// location (file name and line number). Afterwards, it abort()s the program.
  Function *getAssertFunction(Module *Mod) {
    Type *CharPtrTy = Type::getInt8PtrTy(getGlobalContext());
    Type *IntTy = Type::getInt32Ty(getGlobalContext());
    Type *VoidTy = Type::getVoidTy(getGlobalContext());

    std::vector<Type *> assert_arg_types;
    assert_arg_types.push_back(CharPtrTy); // const char *__assertion
    assert_arg_types.push_back(CharPtrTy); // const char *__file
    assert_arg_types.push_back(IntTy);     // int __line

    FunctionType *assert_type =
        FunctionType::get(VoidTy, assert_arg_types, true);

    Function *F = Function::Create(assert_type, Function::ExternalLinkage,
                                   Twine("__assert"), Mod);
    F->addFnAttr(Attribute::NoReturn);
    F->setCallingConv(CallingConv::C);
    return F;
  }
};
}

char BoundsCheck::ID = 0;
static RegisterPass<BoundsCheck> X("cheetah-bc",
                                   "Cheetah Bounds Check Pass", false, false);
