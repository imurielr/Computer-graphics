/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.event.MouseListener;
import java.awt.geom.Line2D;

import javax.swing.JPanel;

import com.sun.glass.events.MouseEvent;

import org.omg.CORBA.SystemException;

import javax.sound.midi.SysexMessage;
import javax.swing.JComponent;
import javax.swing.JFrame;

public class LineClipping2 extends JPanel implements MouseListener{

    static int w, h, wr = 0, hr = 0;
    static int xMin;
    static int xMax;
    static int yMin;
    static int yMax;

    Line2D.Double linea1;

    public LineClipping2(){
        linea1 = new java.awt.geom.Line2D.Double();
        this.addMouseListener(this);
    }

        @Override
    public void mouseClicked(java.awt.event.MouseEvent e){}

        @Override
    public void mousePressed(java.awt.event.MouseEvent e){
        linea1.x1 = 0;
        linea1.y1 = 0;
        linea1.x2 = 0;
        linea1.y2 = 0;

        xMin = 0;
        yMin = 0;
        xMax = 0;
        yMax = 0;

        linea1.x1 = (int)e.getX();
        linea1.y1 = (int)e.getY();

        xMin = (int)linea1.x1;
        yMin = (int)linea1.y1;
    }

        @Override
    public void mouseReleased(java.awt.event.MouseEvent e){
        linea1.x2 = (int)e.getX();
        linea1.y2 = (int)e.getY();

        xMax = (int)linea1.x2;
        yMax = (int)linea1.y2;
        
        wr = (int)linea1.x2 - (int)linea1.x1;
        hr = (int)linea1.y2 - (int)linea1.y1;

        repaint();
    }

        @Override
    public void mouseEntered(java.awt.event.MouseEvent e){}

        @Override
    public void mouseExited(java.awt.event.MouseEvent e){}
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(Color.BLACK);

        // size es el tamaño de la ventana.
        Dimension size = getSize();
        // Insets son los bordes y los títulos de la ventana.
        Insets insets = getInsets();

        w =  size.width - insets.left - insets.right;
        h =  size.height - insets.top - insets.bottom;
        
        g2d.drawLine(w/2, 0, w/2, h);
        g2d.drawLine(0, h/2, w, h/2);
        
        g2d.setColor(Color.BLUE);

        g2d.drawRect((int)linea1.x1, (int)linea1.y1, wr, hr);

        getPoints(g2d, 0, 0, 150, 20, w, h);
      
    }        

    public static int[] liangBarsky(Graphics2D g, int x0, int y0, int x1, int y1, int w, int h){ 
        double u1 = 0;
        double u2 = 1;

        int dx = x1 - x0;
        int dy = y1 - y0;

        int p[] = {-dx, dx, -dy, dy};
        int q[] = {x0 - xMin, xMax - x0, y0 - yMin, yMax - y0};

        for(int i = 0; i < 4; ++i){
            if(p[i] == 0){
                if(q[i] < 0){
                    return null;
                }
            }
            else{
                double u = (double) q[i] / p[i];
                if(p[i] < 0){
                    u1 = Math.max(u, u1);
                }
                else{
                    u2 = Math.min(u, u2);
                }
            }
        }
        if(u1 > u2){
            return null;
        }
        int nx0 = (int) (x0 + u1 * dx);
        int ny0 = (int) (y0 + u1 * dy);
        int nx1 = (int) (x0 + u2 * dx);
        int ny1 = (int) (y0 + u2 * dy);
        return new int[] {nx0, ny0, nx1, ny1};
    }

    public static void setLineColor(Graphics2D g2d, int[] originalCoordinates, int[] r, int w, int h){
        if(r == null){
            g2d.setColor(Color.RED);
            drawLine(g2d, originalCoordinates[0], originalCoordinates[1], originalCoordinates[2], originalCoordinates[3], w, h);
        }
        else{
            g2d.setColor(Color.RED);
            drawLine(g2d, originalCoordinates[0], originalCoordinates[1], r[0], r[1], w, h);
            drawLine(g2d, r[2], r[3], originalCoordinates[2], originalCoordinates[3], w, h);
            
            g2d.setColor(Color.GREEN);
            drawLine(g2d, r[0], r[1], r[2], r[3], w, h);
        }
    }
  
    public static void drawLine(Graphics2D g, int x0, int y0, int x1, int y1, int w, int h){
        g.drawLine(x0, y0, x1, y1);
    }

    /**
     * Obtiene los puntos en los que corta la circunferencia
     */
    public void getPoints(Graphics2D g, int x1, int y1, int radio, int n, int w, int h){
        double angulo = 0;
        int x[] = new int[n];
        int y[] = new int[n];

        for(int i = 0; i < n; ++i){
            angulo = i * (360 / n);
            x[i] = (int)(x1 + radio * Math.cos(Math.toRadians(angulo)));
            y[i] = (int)(y1 + radio * Math.sin(Math.toRadians(angulo)));
        }
        drawCoordinates(g, x, y, w, h);
    }

    /**
     * Toma las coordenadas y une los puntos correspondientes
     */
    public void drawCoordinates(Graphics2D g, int[] x, int[] y, int w, int h){
        int borderX = w - ((2 * w + 1) + 1);
        int borderY = h - ((2 * h + 1) + 1);
        
        int left0;
        int top0;
        int left1;
        int top1;

        for(int i = 0; i < x.length; ++i){
            for(int j = 1; j < x.length; ++j){

                left0 = (x[i] + w) + borderX / 2;
                top0 = (y[i] + h) + borderY / 2;
                left1 = (x[j] + w) + borderX / 2;
                top1 = (y[j] + h) + borderY / 2;

                int[] r = liangBarsky(g, left0, top0, left1, top1, w, h);
                setLineColor(g, new int[] {left0, top0, left1, top1}, r, w, h);
            }
        }
    }
  
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // Crear un nuevo Frame
        JFrame frame = new JFrame("LiangBarsky");
        // Al cerrar el frame, termina la ejecución de este programa
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Agregar el Mouse Listener
        LineClipping2 evento = new LineClipping2();
        frame.add(evento);
        frame.addMouseListener(evento);

        // Agregar un JPanel que se llama Points (esta clase)
        frame.add(new LineClipping2());
        // Asignarle tamaño
        frame.setSize(500, 500);
        // Poner el frame en el centro de la pantalla
        frame.setLocationRelativeTo(null);
        // Mostrar el frame
        frame.setVisible(true);
    }
}
