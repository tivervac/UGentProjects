 
/* gebruikt de veralgemeende Zipf-distributie uit het artikel van Bell en Gupta */

/* 


compile: cc -O4 -o mn_random8020_v makenumbers_random8020_vary.c -lm

De bedoeling van dit programma is eerst een boom in een random volgorde op te bouwen
en dan erin te zoeken. Het schrijft altijd eerst een charakter (byte) en dan een integer.
Als de charakter "t" is betekent dat "toevoegen" van de volgende integer, als hij "z" is 
"zoeken" van de volgende integer. Andere programma's hebben ook nog "v" voor verwijderen.

Usage: "programname" aantal_toe_te_voegen aantal_zoek_operaties ongelijkmatikheid delen [seed]

b.v. makenumbers 1000000 20000000 0.9 3

geeft eerst 1000000 keer "t" x uit met verschillende x om een boom met 1000000 toppen op
te bouwen en dan 20000000 keer "z" y met een random getal "y" dat in de boom zit. De integer
"ongelijkmatigheid" bepaald hoe gelijkverdeeld de kans van de getallen is opgezocht te worden.

ongelijkmatigheid=0 betekent dat alle getallen (ongeveer) gelijke kans hebben.
ongelijkmatigheid=1 is al heel sterk gebiased -- maar de biasedness groeit met aantal_toe_te_voegen.

Delen betekent dat naar een "delen-de" van alle getallen ze opnieuw door elkaar gegooid 
worden -- zodat de toegangspattern verandert.

*/ 

#include<stdio.h>
#include<stdlib.h>
#include<math.h>



void shuffle(int numbers[], int hoeveel)
{
  int i, buffer;
  long int doel;

  for (i=1; i<hoeveel; i++)
    { doel= lrand48()%((long int)(i+1));
      buffer=numbers[i]; numbers[i]=numbers[doel]; numbers[doel]=buffer;
    }
}

int getindex(long double d,long double kans[],int hoeveel)
{
  int found, left, right, index;

  found=0; left=1; right=hoeveel; index=hoeveel/2;

  while (!found)
    { if (kans[index]>=d) { left=index; index=(left+right+1)/2; }
    else
      {
	if (kans[index-1]<d) { right=index; index=(left+right)/2; }
	else // kans[index-1]>=d en kans[index]<d
	  found=1;
      }
    }

  return index-1;

}



int main(int argc, char*argv[])
{
  int positie,j,k, hoeveel;
  long double ongelijk;
  long long int i, aantal_calls_zoeken, wanneermengen;
  int *numbers, seed, delen;
  long double d, C, dbuffer, *kans;
  long double C_100;

  //fprintf(stderr,"RAND_MAX=%d\n",RAND_MAX);

  //fprintf(stderr,"%d\n",sizeof(long double));

  for (i=0;i<argc;i++) fprintf(stderr,"%s ",argv[i]); fprintf(stderr,"\n");

  if (argc<5) { fprintf(stderr,"Usage: %s howmanynumbers howmanysearchoperations ongelijkmatikheid delen [seed]\n",argv[0]); exit(1); }

  hoeveel=atoi(argv[1]);
  aantal_calls_zoeken=atoll(argv[2]);
  ongelijk=atof(argv[3]);
  delen=atoi(argv[4]);
  if (delen==0) delen=1;
  wanneermengen=aantal_calls_zoeken/delen;

  for (C=0, i=1; i<=hoeveel; i++) C += (1.0/powl((long double)i,ongelijk));
#ifdef PRINT
  for (C_100=0,j=k=1; j<=100; j++)
    {
      for ( i=1; i<=hoeveel/100; i++,k++) C_100 += (1.0/powl((long double)k,ongelijk));
      fprintf(stderr,"De bovenste  %d%% worden %1.2Lf%% van de keren bezocht.\n",j,(100.0*C_100)/C);
    }
#endif
  C= 1/C;
  kans=(long double *) malloc(sizeof(long double)*(hoeveel+1));
  if (kans==NULL) { fprintf(stderr,"Can't allocate enough memory (1) -- exiting.\n"); exit(1); }
  for (kans[0]=1.0, i=1; i<=hoeveel; i++) { dbuffer= C/powl((long double)i,ongelijk);
                                            kans[i]=kans[i-1]-dbuffer;
                                          }

  //fprintf(stderr,"--------- %lld\n",wanneermengen);

  //fprintf(stderr,"\n");

  if (ongelijk<0) { fprintf(stderr,"Ongelijkmatigheid moet positief zijn\n"); exit(0); }

  if (argc==6) seed=atoi(argv[5]); else seed=(int)hoeveel;

  numbers=(int *) calloc(hoeveel,sizeof(int));
  if (numbers==NULL) { fprintf(stderr,"Can't allocate enough memory (2) -- exiting.\n"); exit(1); }

  for (i=0; i<hoeveel; i++) numbers[i]=i;

  srand48(seed);

  /* de getallen vermengen */
  shuffle(numbers,hoeveel);
 
  for (i=0; i<hoeveel; i++)
    { 
      putc_unlocked((int)'t',stdout);
      fwrite_unlocked(numbers+i,sizeof(int), 1,stdout);
    } 
  
  for (i=0; i<aantal_calls_zoeken; i++)
    {
      if (i%wanneermengen ==0) shuffle(numbers,hoeveel); 

      d=drand48();

      positie=getindex(d,kans,hoeveel);

      putc_unlocked((int)'z',stdout);
      fwrite_unlocked(numbers+positie,sizeof(int), 1,stdout);
      //fprintf(stderr,"%d  %d\n",numbers[positie], positie);
    }

  return 0;

}
