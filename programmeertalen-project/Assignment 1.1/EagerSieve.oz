functor
   import
      System
      Application
      Property
   define

      % This function parse the input arguments
      fun {ParseArgs L}
         case L
         of nil then {System.showInfo 'Please enter the upperbound for the Sieve!'}
                  {Application.exit 0}
                  nil
         [] X|_ then X end
      end

      % Calculate the sqrt of an int
      fun {SqrtInt F}
         {Sqrt {IntToFloat F}}
      end

      % Convert a float to a floored int
      fun {FToI F}
         {FloatToInt {Floor F}}
      end

      % The condition used to generate a list of integers
      fun {GCond C M}
         C =< M
      end

      % This function generates a list of Max elements
      fun {Generate F Max Cur}
         if {F Cur Max} then Cur|{Generate F Max Cur+1} else nil end
      end

      % The condition used to filter a list of integers
      fun {FCond Y X}
         Y mod X == 0
      end

      % This function filters elements out of Xr according to F
      fun {Filter F Xr X}
         case Xr
         of nil then nil
         [] Y|Yr then if {F Y X} then {Filter F Yr X} else Y|{Filter F Yr X} end
         end
      end

      % The condition used to know how long to filter elements for the sieve
      fun {UCond X U}
         X > U
      end

      % The Sieve
      fun {Sieve F U N}
         % The loop that gets work done
         fun {SieveLoop F U L}
            case L
            of nil then nil
            [] X|Xr then if {F X U} then L else X|{SieveLoop F U {Filter FCond Xr X}} end
            end
         end
      in
         local L in
            L = {Generate GCond N 2}
            {SieveLoop F U L}
         end
      end

   local X UpperBound in
      X = {StringToInt {ParseArgs {Application.getArgs list('in'(type: int))}}}
      UpperBound = {FToI {SqrtInt X}}
      {Property.put print foo(width:999999 depth:999999)}
      {System.show {Sieve UCond UpperBound X}}
      {Application.exit 0}
   end
end
