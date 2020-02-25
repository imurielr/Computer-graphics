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
public class Point3 {
    protected double x;
    protected double y;
    protected double z;
    protected double w;
    
    public Point3(double x, double y,double z, double w){
    this.x=x;
    this.y=y;
    this.z=z;
    this.w=w;
    }

    public Point3 normalize(){
        x /= w;
        y /= w;
        z /= w;
        w /= w;
        return new Point3(x, y, z, w);
    }

    public void print(){
        System.out.println(x + ", " + y + ", " + z + ", " + w);
    }
}
