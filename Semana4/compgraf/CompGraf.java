/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compgraf;

/**
 *
 * @author isamuriel
 */
public class CompGraf {

        public static void main1(String[] args) {
        Matrix4x4 m =new Matrix4x4();
        m.matriz[0][0] = 3;
        m.matriz[0][1] = 5;
        m.matriz[0][2] = 7;
        m.matriz[0][3] = 8;
        m.matriz[1][0] = 1;
        m.matriz[1][1] = 2;
        m.matriz[1][2] = 8;
        m.matriz[1][3] = 7;
        m.matriz[2][0] = 4;
        m.matriz[2][1] = 5;
        m.matriz[2][2] = 3;
        m.matriz[2][3] = -2;
        m.matriz[3][0] = 1;
        m.matriz[3][1] = 6;
        m.matriz[3][2] = 7;
        m.matriz[3][3] = 9;
        
        m.print2D();
        System.out.println("");
        Matrix4x4 m2 =new Matrix4x4();
        m2.matriz[0][0] = 2;
        m2.matriz[0][1] = 8;
        m2.matriz[0][2] = 6;
        m2.matriz[0][3] = 9;
        m2.matriz[1][0] = 3;
        m2.matriz[1][1] = -5;
        m2.matriz[1][2] = 6;
        m2.matriz[1][3] = 7;
        m2.matriz[2][0] = 1;
        m2.matriz[2][1] = 4;
        m2.matriz[2][2] = 9;
        m2.matriz[2][3] = -3;
        m2.matriz[3][0] = 10;
        m2.matriz[3][1] = -2;
        m2.matriz[3][2] = 5;
        m2.matriz[3][3] = 2;
        
        // m2.print2D();
        //  System.out.println("");
        // Matrix4x4 sol = Matrix4x4.times(m, m2);
        // System.out.println("Solucion: \n");
        // sol.print2D();

        Point3 p = new Point3(4, 2, 1, 1);
        Point3 solP = Matrix4x4.times(m, p);
        System.out.println("Solucion: \n");
        System.out.println(solP.x + ", " + solP.y + ", " + solP.z + ", " + solP.w);
        
    }
    
    public static void main2(String[] args) {
        Point2 p1 = new Point2(2,1,1);
        Matrix3x3 m1 = new Matrix3x3();
        m1.matriz[0][0] = 0;
        m1.matriz[0][1] = -1;
        m1.matriz[0][2] = 0;
        m1.matriz[1][0] = 1;
        m1.matriz[1][1] = 0;
        m1.matriz[1][2] = 0;
        m1.matriz[2][0] = 0;
        m1.matriz[2][1] = 0;
        m1.matriz[2][2] = 1;
         
        Point2 r = Matrix3x3.times(m1, p1);
        
        System.out.println(r.x);
        System.out.println(r.y);
        System.out.println(r.w);
        
        
    }

    public static void main3(String[] args){
        EcParSegReg ec1 = new EcParSegReg(1, 2, 3, 4);
        EcParSegReg ec2 = new EcParSegReg(5, 1, 1, 4);
        System.out.println(EcParSegReg.solve(ec1, ec2).toString());
    }
    
    public static void main(String[] args){
        Vector3 v1 = new Vector3(1, 2, 1);
        Vector3 v2 = new Vector3(0, 4, 2);

        Vector3 cp = Vector3.crossProduct(v1, v2);
        System.out.println("v1 x v2  " + cp.toString());

        System.out.println("v1 * v2  " + Vector3.dotProduct(v1, v2));

        System.out.println("|v1|  " + v1.magnitude());
        System.out.println("|v2|  " + v2.magnitude());

        v1.normalize();
        System.out.println("n v1   " + v1.toString());

        v2.normalize();
        System.out.println("n v2  " + v2.toString());

    }
}
