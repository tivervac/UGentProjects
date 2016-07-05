classdef TheCloud
% This is the cloud class template for the project of 'Information Theory'
% Please implement the empty functions in order to make a working
% distributed storage system
%
% Author: Samuel Van de Velde, Jan 2015   
%
% Example usage:
%

% data = a.read_data();
%
    
    properties(GetAccess='public', SetAccess='private')
        m = 8;                          % determines the galois field: GF(2^m)
        data_on_disks = [];             % this matrix with gf elements represents the data on the disks: 
        n = 18;                         % every row is a codeword of length n 
        k = 12;                         % (so there are n columns. the number of rows depends on the amount of data stored).   
        t = 3;
        r = 256-18-1;
        % feel free to add more variables to this class    
    end     
        
    methods
        %% constructor
        function obj=TheCloud()            
            obj.data_on_disks = [];
        end     
        
        %% testfunction to see output of write_data for random bitstream
        function obj=test_write_read(obj, bitstreamsize)
            obj = TheCloud;
            testinput = randi([0 1], 1, bitstreamsize);
            obj=write_data(obj, testinput);
            testoutput = read_data(obj);
            if(isequal(testinput, testoutput(1:bitstreamsize)))
                display('Input matches output')
            else
                display('no match!')
            end
        end
      
        %% functions for RS data processing           
        function obj=write_data(obj, bitstream)
            % INPUT: 
            %    obj: TheCloud object, 
            %    bitstream: row-vector of arbitrary length containing the binary data
            % OUTPUT: TheCloud object  
            
            % this function adds padding to the bitstream
            padding = (obj.m * obj.k) - mod(length(bitstream), obj.m * obj.k);
            paddedstream = [bitstream zeros(1,padding)];
            bytearray=reshape(paddedstream, obj.m, [])';
            % converts to byte symbols + splits them into blocks (k per row)
            bytes=bi2de(bytearray, 'left-msb');
            bytes=gf(reshape(bytes, obj.k, [])',8);
            [rows, cols] = size(bytes);
            
            % performs RS-coding: the result is saved on: data_on_disks
            % generator (from ReedSolomon.m) -> generate code words
            gen = gf([1 126 4 158 58 49 117],8);
            codewords = [bytes gf(zeros(rows,obj.n - obj.k),obj.m)];
            for i = 1:rows
                % multiply message with x^(n-k)
                codewords(i,:) = [bytes(i,:) zeros(1, obj.n - obj.k)];
                [result, remainder] = deconv(codewords(i,:), gen);
                codewords(i,:) = codewords(i,:) + remainder;
            end             
            obj.data_on_disks = [obj.data_on_disks; codewords];
        end  
               
        function bitstream = read_data(obj)
            % this function returns the actual data that was once written on the disks
            % INPUT: obj: TheCloud object
            % OUTPUT: bitstream (this should match the bitstream from the
            % function write_data()      
            
            % assuming there were no errors -> error polynomial = 0
            % generator (from ReedSolomon.m) -> decode code words
            gen = gf([1 126 4 158 58 49 117],8);
            [rows, cols] = size(obj.data_on_disks);
            resultinfo = gf(zeros(size(obj.data_on_disks,1),obj.k),obj.m);
            for i = 1:rows
               [result, remainder] = deconv(obj.data_on_disks(i,:), gen);
               if remainder == 0
                    resultinfo(i,:) = obj.data_on_disks(i,1:obj.k);
               else
                   % there was an error in the codeword
               end
            end
            % convert result to bitstream
            resultinfo_normal = reshape(resultinfo.x',1,[]);
            bitstream = de2bi(resultinfo_normal, 'left-msb');
            % final bitstream is original + 0's at the end
            bitstream = reshape(bitstream',1,[]);
        end                
        
        function obj=repairFailedNodes(obj, failed_node_IDs)
            % this function performs erasure decoding
            % INPUT: 
            %    obj: TheCloud object, 
            %    failed_node_IDs: row-vector containing the numbers of
            %    failed disks (for example [1 3 10], means disks 1,3 and 10
            %    have failed)
            % OUTPUT: TheCloud object (corrected) 
            
            num_of_errors = numel(failed_node_IDs);
            root = gf(2, obj.m);
            
            for i = 1:size(obj.data_on_disks,1)
                word = obj.data_on_disks(i,:);
                % calculate syndromes
                syndroms = gf(zeros(num_of_errors,1), obj.m);
                for j = 1:num_of_errors
                    syndroms(j) =  polyval(word, root^(j));
                end
                
                alpha_matrix = gf(zeros(num_of_errors, num_of_errors), obj.m);
                for j = 1:num_of_errors
                    for l = 1:num_of_errors
                        alpha_matrix(j, l) = root^(j * (obj.n-failed_node_IDs(l)));
                    end
                end
            
                error_values = alpha_matrix\syndroms;
                
                new_word = word;
                
                for j = 1:num_of_errors
                    new_word(failed_node_IDs(j)) = word(failed_node_IDs(j)) + error_values(j);
                end
                obj.data_on_disks(i,:) = new_word;
                
            end
        end
        
        function obj=selfHeal(obj)
            
             function e=findErrorPolynomial(syn)
                K = 1;
                L = 0;
                N = 2*obj.t;
                C = gf([zeros(1, N-1) 1 0], obj.m);
                lambda = gf([zeros(1, N) 1], obj.m);
                new_lambda = lambda;
                
                while K <= 2*obj.t
                    %calculate discrepancy d
                    d = syn(1);
                    for ii = 1:L 
                        d = d + lambda(ii)*syn(K-ii);
                    end
                    if d==0
                        % dont do calculations
                        new_lambda = lambda;
                    else
                        mul = d*C;
                        el = mul.x;
                        mul = gf(el(find(mul.x,1):end),obj.m);
                        if numel(mul.x)<numel(lambda.x)
                            mul = [zeros(1,numel(lambda.x)-numel(mul.x)) mul];
                        end
                        
                        
                        new_lambda = lambda + mul;
                        if (2*L < K)
                            L = K-L;
                            C = lambda/d;
                        end
                    end
                    % calculate new C
                    C = conv(C, [1 0]);
                    lambda = new_lambda;
                    K = K+1;
                end
                e = lambda;
             end
            
            function roots=chienSearch(root, poly)
                roots = []
                for ii=0:obj.n-1
                    val = polyval(poly,root^ii);
                    if val == 0
                        roots = [roots val];
                    end
                end
            end
             
            % This functions performs self healing: it will do error
            % decoding on the data 
            % INPUT: obj: TheCloud object
            % OUTPUT: obj: TheCloud object
            root = gf(2, obj.m, 285);
            
            % for every row
            for i = 1:size(obj.data_on_disks,1)
                word = obj.data_on_disks(i,:);
                
                syn = gf(zeros(1,obj.t*2),obj.m);
                %calculate syndromes
                for j = 1:2*obj.t
                    % substitute alpha^j in received polynomial
                    syn(j) = polyval(word, root^(j));
                end
                
                %if all syndromes are 0, no errors occured
                if any (syn ~= 0)
                    e = findErrorPolynomial(syn);
                    roots = chienSearch(root, e);
                end
            end
        end    
        
       
           
        function obj=disasterStrikes(obj, node_IDs)
           % this functions simulates failures to some of the nodes
           % INPUT: 
           %    obj: TheCloud object
           %    node_IDs: row-vector containing the numbers of nodes that
           %    fail
           % OUTPUT:
           %    obj: TheCloud object
           
           if(~isempty(obj.data_on_disks))
               nrows = size(obj.data_on_disks, 1);
               nfailed = length(node_IDs);
               obj.data_on_disks(:, node_IDs) = gf(zeros(nrows, nfailed), obj.m);
           end  
        end
        
        function obj=bitRotStrikes(obj, bit_rot_probability)
           % this functions simulates failures to some of the nodes
           % INPUT: 
           %    obj: TheCloud object
           %    node_IDs: row-vector containing the numbers of nodes that
           %    fail
           % OUTPUT:
           %    obj: TheCloud object
           
           if(~isempty(obj.data_on_disks))
               [nrows, ncols] = size(obj.data_on_disks);
               
               % generate some binary errors
               bits = rand(nrows*ncols, obj.m);
               bits(bits<(1-bit_rot_probability)) = 0;
               bits(bits>0) = 1;
               
               % convert to elements in gf(2^m)
               errors = gf(bi2de(bits), obj.m)   ;          
               
               % add errors to the data on the disks
               obj.data_on_disks = obj.data_on_disks + reshape(errors, nrows, ncols);
           end              
           
        end
        
    end          
  
    
end