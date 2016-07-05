functor
   import
      System
      Application
      Property
   define

      % This function parse the input arguments
      fun lazy {ParseArgs L}
         case L
         of nil then {System.showInfo 'Please enter the upperbound for the Sieve!'}
                  {Application.exit 0}
                  nil
         [] X|_ then X end
      end

      % Calculate the sqrt of an int
      fun lazy {SqrtInt F}
         {Sqrt {IntToFloat F}}
      end

      % Convert a float to a floored int
      fun lazy {FToI F}
         {FloatToInt {Floor F}}
      end

      % The condition used to generate a list of integers
      fun lazy {GCond C M}
         C =< M
      end

      % This function generates a list of Max elements
      fun lazy {Generate F Max Cur}
         if {F Cur Max} then Cur|{Generate F Max Cur+1} else nil end
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
      fun lazy {UCond X U}
         X > U
      end

      % The Sieve
      fun lazy {Sieve F U N}
         % The loop that gets work done
         fun lazy {SieveLoop F U L}
            case L
            of nil then nil
            [] X|Xr then if {F X U} then L else X|{SieveLoop F U {Filter FCond Xr X}} end
            end
         end
      in
         local L S in
            L = {Generate GCond N 2}
            S = {SieveLoop F U L}
            {Touch S}
            S
         end
      end

      % This procedure forces the list to be calculated
      proc {Touch L}
         case L
         of nil then skip
         else {Touch L.2}
         end
      end

   local X UpperBound S in
      X = {StringToInt {ParseArgs {Application.getArgs list('in'(type: int))}}}
      UpperBound = {FToI {SqrtInt X}}
      {Property.put print foo(width:999999 depth:999999)}
      S = {Sieve UCond UpperBound X}
      case S
      of nil then {System.show nil}
      else {System.show S.1|S.2}
      end
      {Application.exit 0}
   end
end
