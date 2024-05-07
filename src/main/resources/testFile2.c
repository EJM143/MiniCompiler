/*
* Test File 2 - Fibonacci series
* Completed by Edale Miguel
*/
n=100;
        F f1 = 0;
        F f2 = 1;
        F fi;

        if(n == 0)
            print(0);
        if(n == 1)
            print (1);
i = 2
        while( i <= n)
        {
            fi = f1 + f2;
            f1 = f2;
            f2 = fi;
            i++;
        }
        print(fi);