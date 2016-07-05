% Calculate table for primitive polynomial (with every power of alpha)

function[gen, check, hamming] = ReedSolomon()
    q = 256; m =8; n = 18; k = 12; t = 3; r = 256-18-1;

    alpha = gf(2,m,285);
    for i=1:q-1
        p = alpha^i;
        if p.x==1
            if i==q-1 
                fprintf('Alpha^i = 1 if i is 255 so the polynomial is primitive\n');
            else
                fprintf('Not a primitive polynomial')
            end
        end
    end

    % Generator polynomial 
    gen = 1;
    for i=1:6
        gen = conv(gen, [1 -alpha^i]);
    end
    fprintf('The generator polynomial in vector form is:');
    display(gen.x);

    % check polynomial = all elements that are not in the generator polynomial
    check = 1;
    for i=7:255
        check = conv(check, [1 alpha^i]);
    end
    fprintf('The check polynomial in vector form is:');
    display(check.x);

    % Check polynomial: g(x)h(x) = x^n -1
    x = zeros(1, 256);
    x(1) = 1;
    x(256) = 1;
    if conv(gen,check) == x
        fprintf('Check g*h: correct\n');
    end

    % (n,k) MDS code met hamming distance d voldoet aan k + d = n + 1
    hamming = n + 1 - k;
    fprintf('The Hamming distance is %i\n', hamming);
end

