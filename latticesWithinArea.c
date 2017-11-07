//
//  euler504.c
//  
//
//  Created by Micah Shute on 7/4/17.
//
//

#include <stdio.h>
#include <math.h>

int numOfLattices(int a, int b, int c, int d){
    int total = 0;
    //get axis points
    total += 1; //centroid point
    total += a-1; //theta = 0 axis
    total += b-1; //theta = pi/2 axis
    total += c-1; //theta = pi axis
    total += d-1; // theta = 3pi/2 axis
    
    //get non-axis points
    //0<theta<pi/2
    for(int quadrant = 0; quadrant < 4; quadrant++){
        int rise = 0;
        int run = 0;
        switch (quadrant) {
            case 0:
                rise = b;
                run = a;
                break;
            case 1:
                rise = b;
                run = c;
                break;
            case 2:
                rise = d;
                run = c;
                break;
            case 3:
                rise = d;
                run = a;
                break;

            default:
                printf("THERE IS A FATAL ERROR\n");
                break;
        }
        for(int row = 1; row < run; row++){
            //printf("Calculating Slope\n");
            float m = (float)rise/(float)run;

            //check to see if rise/run * row will intersect a point. if it does, do not include it
            if((int)((m)*(float)row*10000) == ((int)10000 * ((int)((m)*(float)row)))){
                total += (int)(m*(float)row - 1);
            }else{
                total += (int)(m*(float)row);
            }
            
            
        }
    }

    return total;
}

int isSquare(int number){
    float floatNumber = (float)number;

    if(sqrt(floatNumber) - (float)(int)sqrt(floatNumber) == 0){

        return 1;
    }else{
        return 0;
    }
}

int generateQuads(int maxPoint){
    int total = 0;
    int numOfQuads = 0;
    for(int aAxisPoint = 1; aAxisPoint <= maxPoint; aAxisPoint++){
        for(int bAxisPoint = 1; bAxisPoint <= maxPoint; bAxisPoint++){
            for(int cAxisPoint = 1; cAxisPoint <= maxPoint; cAxisPoint++){
                for(int dAxisPoint = 1; dAxisPoint <= maxPoint; dAxisPoint++){
                    numOfQuads ++;
                    int numberOfLattices = numOfLattices(aAxisPoint, bAxisPoint, cAxisPoint, dAxisPoint);
                    if(isSquare(numberOfLattices)){
                        total ++;
                    }
                }
            }
        }
    }
    return total;
    
}





int main(void){
    
    printf("Answer: %d\n", generateQuads(100));

    
    return 0;
}







