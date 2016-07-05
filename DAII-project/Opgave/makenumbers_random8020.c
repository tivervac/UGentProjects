
/* 
compile: cc -O4 -o mn_random8020 makenumbers_random8020.c -lm

De bedoeling van dit programma is eerst een boom in een random volgorde op te bouwen
en dan erin te zoeken. Het schrijft altijd eerst een charakter (byte) en dan een integer.
Als de charakter "t" is betekent dat "toevoegen" van de volgende integer, als hij "z" is 
"zoeken" van de volgende integer. Andere programma's hebben ook nog "v" voor verwijderen.

Usage: "programname" aantal_toe_te_voegen aantal_zoek_operaties ongelijkmatikheid [seed]

b.v. makenumbers 1000000 20000000 8

geeft eerst 1000000 keer "t" x uit met verschillende x om een boom met 1000000 toppen op
te bouwen en dan 20000000 keer "z" y met een random getal "y" dat in de boom zit. De integer
"ongelijkmatigheid" bepaald hoe gelijkverdeeld de kans van de getallen is opgezocht te worden.

ongelijkmatigheid=1 betekent dat alle getallen (ongeveer) gelijke kans hebben. 
Het resultaat van ongelijkmatigheid=8 is dat de bovenste 20% ongeveer 80% van de keren 
opgezocht worden en ongelijkmatigheid=30 leidt ertoe dat bovenste 10% meer dan 90%
van de keren opgezocht worden

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


double round();

main(int argc, char*argv[])
{
  int j,buffer,doel, positie, alleen_toevoegen=0, ongelijk, hoeveel;
  long long int i, aantal_calls_zoeken;
  int *numbers, seed;
  double d;

  //fprintf(stderr,"RAND_MAX=%d\n",RAND_MAX);

  for (i=0;i<argc;i++) fprintf(stderr,"%s ",argv[i]); fprintf(stderr,"\n");

  if (argc<4) { fprintf(stderr,"Usage: %s howmanynumbers howmanysearchoperations ongelijkmatikheid [seed]\n",argv[0]); exit(1); }

  hoeveel=atoi(argv[1]);
  aantal_calls_zoeken=atoll(argv[2]);
  ongelijk=atoi(argv[3]);

  if (ongelijk<1) { fprintf(stderr,"Ongelijkmatigheid moet tenminste 1 zijn\n"); exit(0); }

  if (argc==5) seed=atoi(argv[4]); else seed=(int)hoeveel;

  numbers=(int *) calloc(hoeveel,sizeof(int));
  if (numbers==NULL) { fprintf(stderr,"Can't allocate enough memory -- exiting.\n"); exit(1); }

  for (i=0; i<hoeveel; i++) numbers[i]=i;

  srand48(seed);

  /* de getallen vermengen */
  shuffle(numbers,hoeveel);
 
  for (i=0; i<hoeveel; i++)
    { 
      putc_unlocked((int)'t',stdout);
      fwrite_unlocked(numbers+i,sizeof(int), 1,stdout);
    } 
  
  /* de getallen opnieuw vermengen */
  shuffle(numbers,hoeveel);
 
  for (i=0; i<aantal_calls_zoeken; i++)
    {
      d=drand48();
      d=pow(d,(double)1/(double)ongelijk);
      positie=(int)round(d*hoeveel)%hoeveel;
      putc_unlocked((int)'z',stdout);
      fwrite_unlocked(numbers+positie,sizeof(int), 1,stdout);
      //fprintf(stderr,"%d  %d\n",numbers[positie], positie);
    }



}
