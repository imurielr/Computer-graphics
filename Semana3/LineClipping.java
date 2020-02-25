/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package line.clipping;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;

import javax.swing.JPanel;
import javax.swing.JFrame;

public class LineClipping extends JPanel {
    static int xMin;
    static int xMax;
    static int yMin;
    static int yMax;

     @Override
  public void paintComponent(Graphics g) {
      super.paintComponent(g);

      Graphics2D g2d = (Graphics2D) g;

      g2d.setColor(Color.BLACK);

      // size es el tamaño de la ventana.
      Dimension size = getSize();
      // Insets son los bordes y los títulos de la ventana.
      Insets insets = getInsets();

      int w =  size.width - insets.left - insets.right;
      int h =  size.height - insets.top - insets.bottom;
      
      g2d.drawLine(w/2, 0, w/2, h);
      g2d.drawLine(0, h/2, w, h/2);
      
      g2d.setColor(Color.BLUE);
      int wr = 300;
      int hr = 200;
      int x = (w/2) - (wr/2);
      int y = (h/2) - (hr/2);
      
      g2d.drawRect(x, y, wr, hr);

      xMin = x;
      yMin = y;
      xMax = x + wr;
      yMax = y + hr;
      
      int x0 = 50;
      int y0 = 10;
      int x1 = 400;
      int y1 = 500;
      
      int r[] = liangBarsky(g2d, x0, y0, x1, y1, h);

      if(r == null){
          g.setColor(Color.RED);
          drawLine(g2d, x0, y0, x1, y1, h);
      }
      else{
          g.setColor(Color.RED);
          drawLine(g2d, x0, y0, r[0], r[1], h);
          drawLine(g2d, r[2], r[3], x1, y1, h);
          g.setColor(Color.GREEN);
          drawLine(g2d, r[0], r[1], r[2], r[3], h);
      }
  }
  
  public static void drawLine(Graphics2D g, int x0, int y0, int x1, int y1, int h){
      g.drawLine(x0, h - y0, x1, h - y1);
  }
  
  public static int[] liangBarsky(Graphics2D g, int x0, int y0, int x1, int y1, int h){ 
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
                System.out.println(u);
                if(p[i] < 0){
                    u1 = Math.max(u, u1);
                    System.out.println("u1:  " + u1);
                }
                else{
                    u2 = Math.min(u, u2);
                    System.out.println("u2:  " + u2);
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

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
      // Crear un nuevo Frame
      JFrame frame = new JFrame("LiangBarsky");
      // Al cerrar el frame, termina la ejecución de este programa
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      // Agregar un JPanel que se llama Points (esta clase)
      frame.add(new LineClipping());
      // Asignarle tamaño
      frame.setSize(500, 500);
      // Poner el frame en el centro de la pantalla
      frame.setLocationRelativeTo(null);
      // Mostrar el frame
      frame.setVisible(true);
    }
    
}
