int n = 100000;
int numbers[100000];

for (int i=0;i<n;n=n+1) {
	numbers[i] = 1;
}

// Nested loop 
for (int i=0; i<n; i = i+1) {	
	int tmp = numbers[i];
	for (int j=i; j>0; j = j-1) {
		tmp = tmp + numbers[j];
	}
	numbers[i] = tmp;
}