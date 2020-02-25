package compgraf;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.function.DoubleUnaryOperator;

import javax.swing.JPanel;

import javafx.geometry.VerticalDirection;
import transformaciones3D.Edge3D;

import javax.swing.JFrame;

public class Curva2D extends JPanel{

    public Curva2D() {
        this.setFocusable(true);
        this.requestFocusInWindow();
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        // // size es el tamaño de la ventana.
        // Dimension size = getSize();
        // // Insets son los bordes y los títulos de la ventana.
        // Insets insets = getInsets();

        // int w =  size.width - insets.left - insets.right;
        // int h =  size.height - insets.top - insets.bottom;
        
        // g2d.drawLine(w/2, 0, w/2, h);
        // g2d.drawLine(0, h/2, w, h/2);

        g2d.setColor(Color.BLUE);
        puntosControl(g2d);
    }

    private Double permutacion(int n, int k){
        Double result = factorial(n) / (factorial(k) * factorial(n - k));
        return result;
    }

    private Double bez(int k, int n, double u){
        double result = permutacion(n, k) * Math.pow(u, k) * Math.pow(1 - u, n - k);
        return result;
    }

    private void puntosControl(Graphics2D g2d){
        Point[] puntos = { new Point(-100, -100), 
                           new Point(-100, 100),
                           new Point(100, -100),
                           new Point(100, 100)
                         };
        
        int n = puntos.length - 1;
        Point anterior = puntos[0];
         
        for(double u = 0; u <= 1; u += 0.001){
            double resultX = 0;
            double resultY = 0;
            for(int k = 0; k <= n; ++k){
                resultX += puntos[k].x * bez(k, n, u);
                resultY += puntos[k].y * bez(k, n, u);
            }
            Point siguiente = new Point(resultX, resultY);
            Line2D linea = new Line2D.Double(anterior.x, anterior.y, siguiente.x, siguiente.y);
            anterior = siguiente;
            dibujar(g2d, linea);
        }
    }

    private Double factorial(int n){
        double result = 1;
        for(; n > 0; --n){
            result *= n;
        }
        return result;
    }

    /**
     * Dibuja las lineas de la figura
     * @param linea   Linea a dibujar
     */
    public void dibujar(Graphics2D g2d, Line2D linea){
        int borderX = getWidth() / 2;
        int borderY = getHeight() / 2;

        double x0 = linea.getX1() + borderX;
        double y0 = borderY - linea.getY1();
        double x1 = linea.getX2() + borderX;
        double y1 = borderY - linea.getY2();

        linea.setLine(x0, y0, x1, y1);
        g2d.draw(linea);
    }

    public static void main(String[] args) {
        // Crear un nuevo Frame
        JFrame frame = new JFrame("Curva 2D");
        // Al cerrar el frame, termina la ejecución de este programa
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Agregar un JPanel que se llama Points (esta clase)
        Curva2D et = new Curva2D();
        frame.add(et);
        // Asignarle tamaño
        frame.setSize(500, 500);
        // Poner el frame en el centro de la pantalla
        frame.setLocationRelativeTo(null);
        // Mostrar el frame
        frame.setVisible(true);
    }
}