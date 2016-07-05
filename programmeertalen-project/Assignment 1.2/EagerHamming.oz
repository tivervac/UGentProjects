functor
   import
      System
      Application
      Property
   define

      % This function parse the input arguments
      fun {ParseArgs L}
         case L
         of nil then {System.showInfo 'Please enter the amount of numbers to generate and the amount of primes to use!'}
                  {Application.exit 0}
                  nil
         [] _|nil then {System.showInfo 'Please enter the amount of numbers to generate and the amount of primes to use!'}
                    {Application.exit 0}
                    nil
         [] _|_ then L
         end
      end

      % This function generates a list of Max elements
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

      % The Sieve
      fun {Sieve K}
         % The loop that gets work done
         fun {SieveLoop L K A}
            case L
            of nil then nil
            [] X|Xr then if K > A then X|{SieveLoop {Filter FCond Xr X} K A+1} else nil end
            end
         end
      in
         local L in
            L = {Generate 2}
            {SieveLoop L K 0}
         end
      end

      % Loop over H and find the minimal X
      fun {GetMinimaLoop H X Last}
         case H
         of [Y] then Y*X
         [] nil then nil
         [] Y|Yr then if Y*X > Last then Y*X else {GetMinimaLoop Yr X Last} end
         end
      end

      % Loop over all primes
      fun {GetMinima M L H Last}
         case L
         of nil then M
         [] X|Xr then {GetMinima {GetMinimaLoop H X Last}|M Xr H Last} end
      end

      fun {GetMinimum M Min}
         case M
         of nil then [Min]
         [] X|Xr then if X < Min then {GetMinimum Xr X} else {GetMinimum Xr Min} end
         end
      end

      % N = Amount of numbers to generate
      % A = Amount of number already generated
      % H = List containing the numbers
      % L = List containg the needed primes
      % Last = Last element in H
      fun {HammingLoop N A H L Last}
         if A < N then
            % M = List of minima: 2w, 3x, 5y, 7z, ...
            % Min = Minimal element of M, will be added to H
            local M Min Y in
               M = {GetMinima nil L H Last}
               % Infinity
               Min = 999999999999999999999999999999999999999999999999999999999
               Y = {GetMinimum M Min}
               {HammingLoop N A+1 {List.append H Y} L Y.1}
            end
         else
            H
         end
      end

      fun {Hamming N K}
         if N=<0 orelse K=<0 then
            {System.showInfo 'Please enter numbers above 0!'}
            {Application.exit 0}
            nil
         else
            local L in
               L = {Sieve K}
               {HammingLoop N 1 [1] L 1}
            end
         end
      end

   local X N K in
      X = {ParseArgs {Application.getArgs list('in'(type: int) 'in2'(type: int))}}
      N = {StringToInt X.1}
      K = {StringToInt X.2.1}
      {Property.put print foo(width:999999 depth:999999)}
      {System.show {Hamming N K}}
      {Application.exit 0}
   end
end
