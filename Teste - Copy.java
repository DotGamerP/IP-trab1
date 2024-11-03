public class Teste{
    public static void main(String[] args){
        isPerfect(28);
    }

    public static void isPerfect(int n){

        int somaDivisores = 0;
        boolean divisorInteiro = false;
        int divisor = 1;

        while (!(divisorInteiro)){

            divisor++;
            divisorInteiro = n % divisor == 0;
        }

        somaDivisores += divisor;
        maxDivisor = n / divisor;

        for (int i = divisor, divisor <= maxDivisor, divisor++){

        }


        System.out.println(divisor);

    }
}
