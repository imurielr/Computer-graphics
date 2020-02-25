import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import java.math.*;
 
class Bresenham extends JPanel {

    private final int pixelSize = 1;
 
    Bresenham() {
        setPreferredSize(new Dimension(600, 500));
        setBackground(Color.WHITE);
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        getPoints(g, 0, 0, 150, 5);
    }
 
    private void plot(Graphics g, int x, int y) {
        Dimension size = getSize();
        Insets insets = getInsets();

        int w = size.width - insets.left - insets.right;
        int h = size.height - insets.top - insets.bottom;
 
        int borderX = w - ((2 * w + 1) * pixelSize + 1);
        int borderY = h - ((2 * h + 1) * pixelSize + 1);
        int left = (x + w) * pixelSize + borderX / 2;
        int top = (y + h) * pixelSize + borderY / 2;
        
        g.setColor(Color.BLACK);

        g.drawLine(left, top, left, top);
    }
 
    private void drawLine(Graphics g, int x1, int y1, int x2, int y2) {
        // delta of exact value and rounded value of the dependent variable
        int d = 0;
 
        int dx = Math.abs(x2 - x1);
        int dy = Math.abs(y2 - y1);
 
        int dx2 = 2 * dx; // slope scaling factors to
        int dy2 = 2 * dy; // avoid floating point
 
        int ix = x1 < x2 ? 1 : -1; // increment direction
        int iy = y1 < y2 ? 1 : -1;
 
        int x = x1;
        int y = y1;
 
        if (dx >= dy) {
            while (true) {
                plot(g, x, y);
                if (x == x2)
                    break;
                x += ix;
                d += dy2;
                if (d > dx) {
                    y += iy;
                    d -= dx2;
                }
            }
        } else {
            while (true) {
                plot(g, x, y);
                if (y == y2)
                    break;
                y += iy;
                d += dx2;
                if (d > dy) {
                    x += ix;
                    d -= dy2;
                }
            }
        }
    }

    /**
     * Toma las coordenadas y une los puntos correspondientes
     */
    public void drawCoordinates(Graphics g, int[] x, int[] y){
        for(int i = 0; i < x.length - 2; ++i){
            drawLine(g, x[i], y[i], x[i+2], y[i+2]);
        }
        drawLine(g, x[0], y[0], x[x.length-2], y[y.length-2]);
        drawLine(g, x[1], y[1], x[x.length-1], y[y.length-1]);
    }

    /**
     * Obtener las coordenadas de cada uno de los puntos por donde corta a la circunferencia
     */
    public void getPoints(Graphics g, int x1, int y1, int radio, int n){
        double angulo = 0;
        int x[] = new int[n];
        int y[] = new int[n];

        for(int i = 0; i < n; ++i){
            angulo = i * (360 / n);
            x[i] = (int)(x1 + radio * Math.cos(Math.toRadians(angulo)));
            y[i] = (int)(y1 + radio * Math.sin(Math.toRadians(angulo)));
        }
        drawCoordinates(g, x, y);
    }

    public static void main(String[] args) {
        // Crear un nuevo Frame
        JFrame frame = new JFrame("Bresenham");
        // Al cerrar el frame, termina la ejecución de este programa
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Agregar un JPanel que se llama Points (esta clase)
        frame.add(new Bresenham());
        // Asignarle tamaño
        frame.setSize(500, 500);
        // Poner el frame en el centro de la pantalla
        frame.setLocationRelativeTo(null);
        // Mostrar el frame
        frame.setVisible(true);
    }
}