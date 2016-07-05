functor
   import
      System
      Application
      Property
      OS
   define

   % This function parse the input arguments
   fun lazy {ParseArgs L}
      case L
      of nil then {System.showInfo 'Please pass an amount of numbers to sort'}
                  {Application.exit 0}
                  nil
      [] Y|nil then Y
      [] _|_ then {System.showInfo 'You can only use 1 file!'}
                  {Application.exit 0}
                  nil
      end
   end

   fun lazy {MCond A B}
      A =< B
   end

   fun lazy {Merge Ll Lr}
      case Ll#Lr
      of nil#(_|_) then Lr
      [] (_|_)#nil then Ll
      [] (X|Xr)#(Y|Yr) then if {MCond X Y} then X|{Merge Xr Lr} else Y|{Merge Ll Yr} end
      end
   end

   fun lazy {Div A B}
      A div B
   end

   % Don't have to check on L, it is never nil
   proc {Split L ?Ll ?Lr} Length in
      Length = {List.length L}
      Ll = {List.take L {Div Length 2}}
      Lr = {List.drop L {Div Length 2}}
   end

   fun lazy {Sort L} Ll Lr in
      case L of nil then nil
      [] [X] then [X]
      else
         {Split L Ll Lr}
         {Merge {Sort Ll} {Sort Lr}}
      end
   end

   fun lazy {GenerateRandomlist N}
      if N > 0 then {OS.rand}|{GenerateRandomlist N-1} else nil end
   end

   proc {Touch N L}
      if N > 0 then {Touch N-1 L.2} else skip end
   end

   local X S L in
      X = {String.toInt {ParseArgs {Application.getArgs list('in'(type: int))}}}
      {OS.srand X}
      L = {GenerateRandomlist X}
      S = {Sort L}
      {Property.put print foo(width:999999999999999999999999 depth:999999999999999999999)}
      if X == 0 then S = nil else {Touch {List.length L} S} end
      {System.show S}
      {Application.exit 0}
   end
end
