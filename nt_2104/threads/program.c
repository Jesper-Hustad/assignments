#include<stdio.h>
#include <stdbool.h>
#include <stdlib.h>
#include <pthread.h>


// Globalb cars
int prime_lower_bound;
int prime_upper_bound;
int thread_count;

int *primeNumbers;


bool isPrime(int n){
    for (int c = 2; c <= n/2; c++){
        if (n%c == 0){
            return false;
        }
    }
    return true;
}

void addNumber(int n){
    int size = (sizeof(primeNumbers) / sizeof(int)) + 1;
    primeNumbers = realloc(primeNumbers, (size)*sizeof(int));
    primeNumbers[size] = n;
}

void *calc_primes(void *vargp) 
{ 
    // Store the value argument passed to this thread 
    int *calculation_data = (int *)vargp; 

    for (int i = prime_lower_bound; i < prime_upper_bound; i += thread_count){
        if(isPrime(i)){
            addNumber(i);
        }
    }

} 


void main(int argc, char *argv[] )  {  
  
    prime_lower_bound = atoi(argv[1]);
    prime_upper_bound = atoi(argv[2]);
    thread_count = atoi(argv[3]);


    printf("Calculating primes from %d to %d with %d thread(s)\n", prime_lower_bound, prime_upper_bound, thread_count);  

    primeNumbers =  malloc(1*sizeof(int));

    pthread_t tid[thread_count];
    for (int i = 0; i < thread_count; i++) {
        int calculation_data[] = {prime_lower_bound, prime_upper_bound, i};
        calc_primes(calculation_data);
        // pthread_create(&tid[i], NULL, calc_primes, (void *)&calculation_data);
    }
    // for (int i = 0; i < thread_count; i++)
    //    pthread_join(tid[i], NULL);


    int amount_of_primes = sizeof(primeNumbers) / sizeof(int);

    addNumber(69);
    addNumber(420);

    for (int i = 0; i < amount_of_primes; i++)
        printf("%d ", primeNumbers[i]);

    
    free(primeNumbers);
    // return 0;


//    if(argc < 2){  
//       printf("No argument passed through command line.\n");  
//    }  
//    else{  
//       printf("First argument is: %s\n", argv[1]);  
//    }  
}  