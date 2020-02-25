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
public class Point2 {
    protected double x;
    protected double y;
    protected double w;

    public Point2(Double x, double y, double w) {
        this.x = x;
        this.y = y;
        this.w = w;
    }

    public Point2 normalize(){
        x /= w;
        y /= w;
        w /= w;
        return new Point2(x, y, w);
    }
}
