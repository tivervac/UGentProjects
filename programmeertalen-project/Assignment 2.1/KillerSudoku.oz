functor
   import
      System
      Application
      Open
      FD
      Search
   define

      % This function parses the input arguments
      fun {ParseArgs L}
         case L
         of nil then {System.showInfo 'Please pass a filename of the sudoku to solve!'}
                     {Application.exit 0}
                     nil
         [] Y|nil then Y
         [] _|_ then {System.showInfo 'You can only use 1 file!'}
                     {Application.exit 0}
                     nil
         end
      end

      % Get the sum tuples from the csv
      fun {ParseSums L Sums}
         case L
         of X|Xr then
            case Xr
            of Y|Yr then
               Sums|{ParseSums Yr X#Y}
            [] nil then {System.showInfo 'Invalid file!'}
                        {Application.exit 0}
                        nil
            end
         [] nil then Sums
         end
      end

      % Get the sudoku rows out of the csv
      fun {ParseRows A Sudoku L} Ys Zs in
         if A < 9 then
            {List.takeDrop L 9 Ys Zs}
            {ParseRows A+1 {List.append Sudoku [Ys]} Zs}
         else
            L#Sudoku
         end
      end

      % Parse the csv
      fun {ParseSudoku L Sudoku Sums} S Sudo Su in
         S#Sudo = {ParseRows 0 Sudoku L}
         Su = {ParseSums S Sums}
         {List.drop Sudo 1}#{List.drop Su 1}
      end

      % Helper function to append lists
      fun {Append Xr Yr}
         if Xr == nil then Yr
         elseif Yr == nil then Xr
         else Xr.1|{Append Xr.2 Yr}
         end
      end

      % Returns a list of 9 lists representing the columns
      fun {Columns S}
         {Map [1 2 3 4 5 6 7 8 9] fun {$ I} {Column S I} end}
      end

      % Get the I'th element in each row
      % S is a row
      fun {Column S I}
         {Map S fun {$ Col} {Nth Col I} end}
      end

      % Returns a list of 9 lists representing the flattened boxes using the map trick again
      fun {Boxes S}
         {Map [1 2 3 4 5 6 7 8 9] fun {$ I} {Box S I} end}
      end

      % S is the entire sudoku, flatten it so we can use arithmetics on it
      fun {Box S I}
         % Index starts at 1
         Index = I - 1
         Squares = {List.flatten S}
         % This calculates where we have to start to get a box
         Start = (Index div 3) * 27 + (Index mod 3) * 3
      in
         % Now return the flattened list of boxes
         {List.flatten
            for J in 0..2 collect:C do
               {C {List.take {List.drop Squares Start+J*9} 3}}
            end
         }
      end

      fun {FCond K X}
         K == X
      end

      % Iterate over the elements in a row of Sol and Sudoku
      % If one matches K, add it to a list, and search for others in the rest of Sol/Sudoku
      % Once we're done searching, return this list
      fun {BinaryFilter Sol Sudoku F K}
         case Sol#Sudoku
         of (X|Xr)#(Y|Yr) then if {F K Y} then {Append [X] {BinaryFilter Xr Yr F K}} else {BinaryFilter Xr Yr F K} end
         [] nil#nil then nil
         end
      end

      % Iterate over the rows of Sol and Sudoku
      % And find al the occurences of K
      fun {FindOcc Res Sol F K Sudoku}
         case Sol#Sudoku
         of (X|nil)#(Y|nil) then {Append Res {BinaryFilter X Y F K}}
         [] (X|Xr)#(Y|Yr) then {FindOcc {Append Res {BinaryFilter X Y F K}} Xr F K Yr}
         end
      end

      % Add the sum(cage) constraints to the right elements
      proc {ConstrainSumsLoop X Sol Sudoku Xr} S K V in
         X = K#V
         % Have to find these elements first though
         S = {List.flatten {FindOcc nil Sol FCond K Sudoku}}
         {FD.sum S '=:' {String.toInt V}}
         {ConstrainSums Sol Sudoku Xr}
      end

      % Loop over all the sums and add the constraints for each one to the sudoku
      proc {ConstrainSums Sol Sudoku Sums}
         case Sums
         of nil then skip
         [] X|Xr then {ConstrainSumsLoop X Sol Sudoku Xr}
         else {ConstrainSumsLoop Sums Sol Sudoku nil}
         end
      end

      fun {BuildConstraints Sudoku#Sums}
         proc {$ Sol}
            % Sudokus only use the numbers 1-9
            % Use a nifty Map trick
            Sol = {Map [1 2 3 4 5 6 7 8 9] fun {$ _} {FD.list 9 1#9} end}

            % Rows of a sudoku have distinct numbers
            {ForAll Sol FD.distinct}
            % Columns of a sudoku have distinct numbers
            {ForAll {Columns Sol} FD.distinct}
            % The boxes in a sudoku have distinct numbers
            {ForAll {Boxes Sol} FD.distinct}
            % Killersudokus have cages(sums)
            {ConstrainSums Sol Sudoku Sums}

            % Naively look for the solution
            {FD.distribute naive {List.flatten Sol}}
         end
      end

   % Print the sudoku in a nicer format
   proc {PrettyPrint L}
      case L
      of nil then {System.show 'No solution found'}
      [] X|nil then {System.show X}
      [] X|Xr then {System.show X} {PrettyPrint Xr}
      end
   end

   local X F L Sudoku Sums in
      X = {ParseArgs {Application.getArgs list('in'(type: string))}}
      F = {New Open.file init(url: X)}
      L = {F read(list:$)}
      Sudoku#Sums = {ParseSudoku {String.tokens L &,} [nil] [nil]}
      {PrettyPrint {Search.base.one {BuildConstraints Sudoku#Sums}}.1}
      {Application.exit 0}
   end
end
