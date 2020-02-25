package compgraf;

import javafx.util.Pair;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author isamuriel
 */
public class EcParSegReg {
    protected double x1;
    protected double y1;
    protected double x2;
    protected double y2;
    
    public EcParSegReg(double x1, double y1, double x2, double y2){
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }
    
    public static Pareja solve(EcParSegReg ec1, EcParSegReg ec2){
        double u1 = (ec1.x1 - ec2.x1) / ((ec2.x2 - ec2.x1) - (ec1.x2 - ec1.x1));
        double u2 = (ec1.y1 - ec2.y1) / ((ec2.y2 - ec2.y1) - (ec1.y2 - ec1.y1));

        return new Pareja(u1, u2);
    }
}
