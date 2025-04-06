import java.util.Scanner;

public class Number {
    @SuppressWarnings("resource")
    public static void main(String arg[]){

        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        int num = in.nextInt();
        
        // reoccuring digit in a number 

        int count = 0;
        while(n > 0){  
            int rem = n%10;
            if(rem==5){
                count ++ ;
            } n = n/10;
        }System.out.println(count);

        // print reverse3 of a given number

        int i = 0;
        while(num>0){
            int temp = num%10;
            i = i*10 + temp;
            num = num/10;
        }System.out.println(i);

    }
}