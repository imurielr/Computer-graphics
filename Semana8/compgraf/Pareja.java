package compgraf;

public class Pareja{
    private static double res1;
    private static double res2;

    public Pareja(double res1, double res2){
        this.res1 = res1;
        this.res2 = res2;
    }

    public static double getRes1(){
        return res1;
    }

    public static double getRes2(){
        return res2;
    }

    public String toString(){
        String a = "ux = " +  res1 + "  uy = " + res2;
        return a;
    }
}