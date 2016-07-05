int n = 1000000000;
int foo[1000];
foo[0] = 0;
int j = 1;

for (int i = 1; i < n; i = i+1) {
	foo[i%1000] = i;// + foo[(i-1)%1000];
}
