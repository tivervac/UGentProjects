% Given 
n = 18;
k = 12;
% Storage in bits
storage = 36 * 10^15 * 8;
% Data points
raw_storage = ones(n) * 36;
RS_storage = zeros(n);
LRC_storage = zeros(n);
own_storage = zeros(n);

for r = 1:n
    % REED-SOLOMON
    % Find the amount of redundant bits
    RS_waste = n - k;
    % Find how many symbols fit in the storage
    no_of_symbols = storage / n;
    % Find the total capacity in petabytes
    RS_storage(r) = ((storage - (no_of_symbols * RS_waste)) / 8) / (10^15);

    % LRC
    % Find the amount of data in a repair group
    repair_group_size = (r + 1) * (r + 1) * n;
    % Check how many repair groups fit in the storage
    no_of_repair_groups = storage / repair_group_size;
    % Find the amount of redundant bits per repair group
    % First there is the RS coding
    LRC_RS_waste = r * (r + 1) * RS_waste;
    % And there is also the syndroms
    LRC_syndrom_waste = (r + 1) * n;
    % Find the redundant bits per repair group
    LRC_waste = (LRC_RS_waste + LRC_syndrom_waste) * no_of_repair_groups;
    % Find the total capacity in petabytes
    LRC_storage(r) = ((storage - LRC_waste) / 8) / (10^15);

    % Own code
    % Find the amount of data in a repair group again
    repair_group_size = (r + 2) * (r + 1) * n;
    % Check how many repair groups fit in the storage
    no_of_repair_groups = storage / repair_group_size;
    % We already know how much RS wastes, find and we just waste double on
    % syndroms compared to LRC
    own_waste = (LRC_RS_waste + (LRC_syndrom_waste * 2)) * no_of_repair_groups;
    % Find the total capacity in petabytes
    own_storage(r) = ((storage - own_waste) / 8) / (10^15);
end

x = 1:18;
plot(x, raw_storage, x, RS_storage, x, LRC_storage, x, own_storage);
xlabel('r')
ylabel('Storage (petabytes)')
legend('Raw Storage', 'RS','LRC', 'Own code', 'Location','southeast')