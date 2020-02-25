package compgraf;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Ellipse2D;
import java.io.IOException;

import javax.swing.JPanel;
import javax.swing.JFrame;

public class EventoTeclado extends JPanel implements KeyListener {
    Graphics2D g2d;
    int w, h;
    Point2[] puntos;
    Edge[] bordes;
    Point2 centroide;
    String archivo =  "/Users/isamuriel/Documents/Proyectos Computacion grafica/Semana4/compgraf/casa.txt";

    public EventoTeclado() {
        this.setFocusable(true);
        this.requestFocusInWindow();
        this.addKeyListener(this);

        try {
            this.puntos = Dibujar.getPoints(archivo);
            this.bordes = Dibujar.getEdges(puntos, archivo);
            this.centroide = findCentroid();
        } catch (IOException e) {}
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);

        g2d = (Graphics2D) g;

        // size es el tamaño de la ventana.
        Dimension size = getSize();
        // Insets son los bordes y los títulos de la ventana.
        Insets insets = getInsets();

        w =  size.width - insets.left - insets.right;
        h =  size.height - insets.top - insets.bottom;
        
        g2d.drawLine(w/2, 0, w/2, h);
        g2d.drawLine(0, h/2, w, h/2);

        g2d.setColor(Color.BLUE);

        try{
            Dibujar.dibujar(g2d, h, w, bordes);
        }
        catch(IOException e){}

    }

    @Override
    public void keyPressed(KeyEvent e){
        int tecla = e.getKeyCode();
        switch (tecla) {
            case KeyEvent.VK_UP:
                translate(0, 10);                
                break;
            case KeyEvent.VK_DOWN:
                translate(0, -10);
                break;
            case KeyEvent.VK_RIGHT:
                translate(10, 0);
                break;
            case KeyEvent.VK_LEFT:
                translate(-10, 0);
                break;
            case KeyEvent.VK_A:
                rotateRight(10);
                break;
            case KeyEvent.VK_D:
                rotateLeft(10);
                break;
            case KeyEvent.VK_W:
                scaleUp(1.1, 1.1);
                break;
            case KeyEvent.VK_S:
                scaleDown(1.1, 1.1);
                break;
            default:
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}

    /**
     * Traslada la figura 
     * @param dx   Numero de pixeles a cambiar en x
     * @param dy   Numero de pixeles a cambiar en y
     */
    public void translate(double dx, double dy){
        Matrix3x3 mTraslacion = new Matrix3x3();
        mTraslacion.matriz[0][0] = 1;
        mTraslacion.matriz[0][1] = 0;
        mTraslacion.matriz[0][2] = dx;
        mTraslacion.matriz[1][0] = 0;
        mTraslacion.matriz[1][1] = 1;
        mTraslacion.matriz[1][2] = dy;
        mTraslacion.matriz[2][0] = 0;
        mTraslacion.matriz[2][1] = 0;
        mTraslacion.matriz[2][2] = 1;

        try{
            for(int i = 0; i < puntos.length; ++i){
                puntos[i] = Matrix3x3.times(mTraslacion, puntos[i]);
            }
            bordes = Dibujar.getEdges(puntos, archivo);
            Dibujar.dibujar(g2d, h, w, bordes);
            repaint();
        }
        catch(IOException e){}
    }

    /**
     * Rota la figura hacía la derecha
     * @param grad   Angulo de rotación en grados
     */
    public void rotateRight(int grad){
        double rad = grad * Math.PI/180;    // Conversion de grados a radianes
        Matrix3x3 mRotacion = new Matrix3x3();
        mRotacion.matriz[0][0] = Math.cos(rad);
        mRotacion.matriz[0][1] = - Math.sin(rad);
        mRotacion.matriz[0][2] = 0;
        mRotacion.matriz[1][0] = Math.sin(rad);
        mRotacion.matriz[1][1] = Math.cos(rad);
        mRotacion.matriz[1][2] = 0;
        mRotacion.matriz[2][0] = 0;
        mRotacion.matriz[2][1] = 0;
        mRotacion.matriz[2][2] = 1;

        try{
            for(int i = 0; i < puntos.length; ++i){
                puntos[i] = Matrix3x3.times(mRotacion, puntos[i]);
            }
            bordes = Dibujar.getEdges(puntos, archivo);
            Dibujar.dibujar(g2d, h, w, bordes);
            repaint();
        }
        catch(IOException e){}
    }

    /**
     * Rota la figura hacía la izquierda
     * @param grad  Angulo de rotacion en grados
     */
    public void rotateLeft(int grad){
        double rad = grad * Math.PI/180;   // Conversion de grados a radianes
        Matrix3x3 mRotacion = new Matrix3x3();
        mRotacion.matriz[0][0] = Math.cos(rad);
        mRotacion.matriz[0][1] = Math.sin(rad);
        mRotacion.matriz[0][2] = 0;
        mRotacion.matriz[1][0] = - Math.sin(rad);
        mRotacion.matriz[1][1] = Math.cos(rad);
        mRotacion.matriz[1][2] = 0;
        mRotacion.matriz[2][0] = 0;
        mRotacion.matriz[2][1] = 0;
        mRotacion.matriz[2][2] = 1;

        try{
            for(int i = 0; i < puntos.length; ++i){
                puntos[i] = Matrix3x3.times(mRotacion, puntos[i]);
            }
            bordes = Dibujar.getEdges(puntos, archivo);
            Dibujar.dibujar(g2d, h, w, bordes);
            repaint();
        }
        catch(IOException e){}
    }

    /**
     * Aumenta el tamaño de la figura
     * @param sx
     * @param sy
     */
    public void scaleUp(double sx, double sy){
        int borderX = w - ((2 * w + 1) + 1);
        int borderY = h - ((2 * h + 1) + 1);
        centroide = findCentroid();
        Point origen = new Point(w + borderX/2, h + borderY/2);

        double distanciaX = origen.x - centroide.x ;
        double distanciaY = origen.y - centroide.y;

        translate(distanciaX, distanciaY);
        
        Matrix3x3 mEscalamiento = new Matrix3x3();
        mEscalamiento.matriz[0][0] = sx;
        mEscalamiento.matriz[0][1] = 0;
        mEscalamiento.matriz[0][2] = 0;
        mEscalamiento.matriz[1][0] = 0;
        mEscalamiento.matriz[1][1] = sy;
        mEscalamiento.matriz[1][2] = 0;
        mEscalamiento.matriz[2][0] = 0;
        mEscalamiento.matriz[2][1] = 0;
        mEscalamiento.matriz[2][2] = 1;

        try{
            for(int i = 0; i < puntos.length; ++i){
                puntos[i] = Matrix3x3.times(mEscalamiento, puntos[i]);
            }
            
            bordes = Dibujar.getEdges(puntos, archivo);
            translate(- distanciaX, - distanciaY);
        }
        catch(IOException e){}      
    }

    /**
     * Disminuye el tamaño de la figura
     * @param sx
     * @param sy
     */
    public void scaleDown(double sx, double sy){
        int borderX = w - ((2 * w + 1) + 1);
        int borderY = h - ((2 * h + 1) + 1);
        centroide = findCentroid();
        Point origen = new Point(w + borderX/2, h + borderY/2);

        double distanciaX = origen.x - centroide.x;
        double distanciaY = origen.y - centroide.y;
        
        translate(distanciaX, distanciaY);   // Traslada la figura al origen
        
        Matrix3x3 mEscalamiento = new Matrix3x3();
        mEscalamiento.matriz[0][0] = 1/sx;
        mEscalamiento.matriz[0][1] = 0;
        mEscalamiento.matriz[0][2] = 0;
        mEscalamiento.matriz[1][0] = 0;
        mEscalamiento.matriz[1][1] = 1/sy;
        mEscalamiento.matriz[1][2] = 0;
        mEscalamiento.matriz[2][0] = 0;
        mEscalamiento.matriz[2][1] = 0;
        mEscalamiento.matriz[2][2] = 1;

        try{
            for(int i = 0; i < puntos.length; ++i){
                puntos[i] = Matrix3x3.times(mEscalamiento, puntos[i]);
            }
            bordes = Dibujar.getEdges(puntos, archivo);
            translate(- distanciaX, - distanciaY);   // Devuelve la figura a su posición original
        }
        catch(IOException e){}      
    }

    /**
     * Encuentra el centroide de la figura
     * @return Punto centroide
     */
    public Point2 findCentroid(){
        int borderX = w - ((2 * w + 1) + 1);
        int borderY = h - ((2 * h + 1) + 1);
        double centroidX = 0;
        double centroidY = 0;
        for(int i = 0; i < puntos.length; ++i){
            centroidX += puntos[i].x;
            centroidY += puntos[i].y;
        }
        centroidX /= puntos.length;
        centroidY /= puntos.length;
        return centroide = new Point2((centroidX + w) + borderX/2, (centroidY + h) + borderY/2, 1);
    }

    public static void main(String[] args) {
        // Crear un nuevo Frame
        JFrame frame = new JFrame("Evento Teclado");
        // Al cerrar el frame, termina la ejecución de este programa
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Agregar un JPanel que se llama Points (esta clase)
        EventoTeclado et = new EventoTeclado();
        frame.add(et);
        // Asignarle tamaño
        frame.setSize(500, 500);
        // Poner el frame en el centro de la pantalla
        frame.setLocationRelativeTo(null);
        // Mostrar el frame
        frame.setVisible(true);
    }
  
}