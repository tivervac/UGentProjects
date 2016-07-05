#include <stdio.h>

int r(int i)
{
	int j,k,ret;
	if (i != 0)
	{
		j = i*i;
		k = r(i-1);
		ret = j+k;
	}
	else
	{
		ret = 0;
	}
	return ret;
}

int main(int argc, char **argv)
{
	printf("%d\n", r(1000));
	return 0;
}
