functor
   import
      System
      Application
      Property
   define

      % This function parse the input arguments
      fun lazy {ParseArgs L}
         case L
         of nil then {System.showInfo 'Please enter the amount of numbers to generate and the amount of primes to use!'}
                  {Application.exit 0}
                  nil
         [] _|nil then {System.showInfo 'Please enter the amount of numbers to generate and the amount of primes to use!'}
                    {Application.exit 0}
                    nil
         [] _|_ then L end
      end

      % This function generates a list of integers
      fun lazy {Generate Cur}
         Cur | {Generate Cur+1}
      end

      % The condition used to filter a list of integers
      fun lazy {FCond Y X}
         Y mod X == 0
      end

      % This function filters elements out of Xr according to F
      fun lazy {Filter F Xr X}
         case Xr
         of nil then nil
         [] Y|Yr then if {F Y X} then {Filter F Yr X} else Y|{Filter F Yr X} end
         end
      end

      % The condition used to know how long to filter elements for the sieve
      fun lazy {UCond K A}
         K > A
      end

      % The Sieve
      fun lazy {Sieve F K}
         % The loop that gets work done
         fun lazy {SieveLoop F K A L}
            case L
            of nil then nil
            [] X|Xr then if {F K A} then X|{SieveLoop F K A+1 {Filter FCond Xr X}} else nil end
            end
         end
      in
         local L in
            L = {Generate 2}
            {SieveLoop F K 0 L}
         end
      end

      % N = Maximum amount of numbers to return
      % A = Amount of calculated numbers
      % Xs = The first list
      % Ys = The second list
      fun lazy {Merge Xs Ys}
         case Xs#Ys of (X|Xr)#(Y|Yr) then
            if X<Y then X|{Merge Xr Ys}
            elseif X>Y then Y|{Merge Xs Yr}
            else X|{Merge Xr Yr}
            end
         end
      end

      fun lazy {Times N H}
         case H
         of X|H2 then N*X|{Times N H2}
         [] nil then nil
         end
      end

      fun lazy {Merger H L}
         case L
         of nil then nil
         [] X|nil then {Times X H}
         [] X|Xr then {Merge {Times X H} {Merger H Xr}}
         end
      end

      proc {Touch N H}
         if N > 0 then {Touch N-1 H.2} else skip end
      end

      fun lazy {Hamming N K}
         if N=<0 orelse K=<0 then
            {System.showInfo 'Please enter numbers above 0!'}
            {Application.exit 0}
            nil
         else
            local L H in
               L = {Sieve UCond K}
               H = 1|{Merger H L}
               {Touch N H}
               H
            end
         end
      end

   local X N K L in
      X = {ParseArgs {Application.getArgs list('in'(type: int) 'in2'(type: int))}}
      N = {StringToInt X.1}
      K = {StringToInt X.2.1}
      {Property.put print foo(width:999999 depth:999999)}
      L = {Hamming N K}
      {System.show L.1|L.2}
      {Application.exit 0}
   end
end
