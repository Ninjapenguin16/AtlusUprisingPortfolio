#include <stdio.h>
#include <math.h>
#include <time.h>
#include <gmpxx.h>

int inputI = 0;

long double tot = 1;

long double totLast = 0;

time_t start, finish;

double diff_t;

int main()
{
    
    printf("What number would you like the factorial of?: "); // Prompts user
    
    scanf("%d", &inputI); // The & is to get the pointer and not the contents

    time(&start); // Sets the start time
    
    for(int i = 1; i < inputI; i++){ // Does the math for factorials
    
        tot *= i;
        
        if(tot < totLast){
            printf("\n\nNumber is too big to calculate, number overflowed");
            return -1;
        }
        
        totLast = tot;
        
    }
    
    time(&finish); // Sets the end time
    
    diff_t = difftime(finish, start); // Finds time it took to run
    
    printf("\n%Lf\n\nTime to finish was %f seconds", tot, diff_t); // Final print

    return 0;
}
