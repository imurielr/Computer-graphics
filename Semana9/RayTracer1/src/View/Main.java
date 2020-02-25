/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import Math.Point;
import Math.Sphere;

import Scene.Scene;
import Scene.Colour;
import Scene.AmbientLight;
import Scene.PointLight;
import Scene.Material;

/**
 *
 * @author htrefftz
 */
public class Main extends JPanel implements KeyListener{

    public Main(){
        this.addKeyListener(this);
        this.setFocusable(true); 
    }

    Image image;
    private final boolean DEBUG = false;
    
    static int width;
    static int height;
    
    int l1x = 100;
    int l1y = 100;
    int l1z = 100;

    public void setImage(Image image) {
        this.image = image;
    }
    
    /**
     * Create all elements in the scene
     * Could be read from a text file
     */
    public void createScene() {
        AmbientLight al = new AmbientLight(new Colour(.2, .2, .2));
        Scene.setAmbientLight(al);
        
        PointLight pl1 = new PointLight(new Point(l1x, l1y, l1z), new Colour(1, 1, 1));
        //Scene.addPointLight(pl1);
        Scene.addPointLight(pl1);
        
        // A yellow reflective sphere
        double Ka = .2;        // ambient
        double Kd = .8;        // difuse
        double Ks = .7;          // specular
        int n = 32;
        Colour color = new Colour(1, 0, 1);     // object's color
        double Ko = 1;          // Weight of this object's color
        double Kr = 0;          // Weight of the reflected color
        double Kt = 0;          // Weight of the refracted color
        Material material1 = new Material(Ka, Kd, Ks, n, color, Ko, Kr, Kt);
        
        //Sphere sp1 = new Sphere(new Point(-25, 0, -100), 20, material1);
        Sphere sp1 = new Sphere(new Point(0, 0, -100), 20, material1);
        Scene.addIntersectable(sp1);
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        Graphics2D g2d = (Graphics2D) g;

        Dimension size = getSize();

        int w = width;
        int h = height;      
        
        image = new Image(w, h);
        image.generateImage();      // Compute the colors of the scene
        this.setImage(image);

        
        Colour colour;
        for(int i = 0; i < image.height; i++) {
            for(int j = 0; j < image.width; j++) {
                colour = image.image[i][j];
                int red = (int) (colour.getR() * 255);
                int green = (int) (colour.getG() * 255);
                int blue = (int) (colour.getB() * 255);
                // Clamp out of range colors
                if(red > 255) red = 255;
                if(green > 255) green = 255;
                if(blue > 255) blue = 255;
                g2d.setColor(new Color(red, green, blue));
                if(DEBUG)
                    System.out.println(red + " " + green + " " + blue);
                g2d.drawLine(i, j, i, j);
            }
        }        
    }
    
    public void moveLight(int x, int y, int z){
        l1x += x;
        l1y += y;
        l1z += z;
        for(PointLight pl: Scene.pointLights) {
            pl.point = new Point(l1x, l1y, l1z);
        }
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        int tecla = e.getKeyCode();
        switch(tecla){
            case KeyEvent.VK_RIGHT:    // Trasladar X en sentido positivo
                moveLight(10, 0, 0);
                break;
            case KeyEvent.VK_LEFT:     // Trasladar X en sentido negativo
                moveLight(-10, 0, 0);
                break;
            case KeyEvent.VK_UP:        // Trasladar Y en sentido positivo
                moveLight(0, 10, 0);
                break;
            case KeyEvent.VK_DOWN:      // Trasladar Y en sentido negativo
                moveLight(0, -10, 0);
                break;
            case KeyEvent.VK_M:         // Trasladar en Z en sentido positivo
                moveLight(0, 0, 10);
                break;
            case KeyEvent.VK_N:         // Trasladar en Z en sentido negativo
                moveLight(0, 0, -10);
                break;
        }
    }
    
    public static void main(String [] args) {
        JFrame frame = new JFrame("Ray Tracer 2019");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        width = 500;
        height = 500;
        
        // Create this JPanel
        Main main = new Main();
        main.createScene();         // Add lights, objects, etc.
        // Image image = new Image(width, height);
        // image.generateImage();      // Compute the colors of the scene
        // main.setImage(image);        
     
        // Draw the result
        frame.add(main);
        frame.setSize(width, height);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
