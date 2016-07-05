int times = 10000;

int n = 100000;
int fibs[100000];

fibs[0] = 0;
fibs[1] = 1;

for (int time=0;time < times; time = time +1) {
	for (int i = 2; i < n; i = i + 1) {
		fibs[i] = fibs[i-1] + fibs[i-1];
	}
}