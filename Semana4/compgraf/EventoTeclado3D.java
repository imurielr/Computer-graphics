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
import java.util.function.DoubleUnaryOperator;

import javax.swing.JPanel;

import transformaciones3D.Edge3D;

import javax.swing.JFrame;

public class EventoTeclado3D extends JPanel implements KeyListener {
    Graphics2D g2d;
    int w, h;
    Point3[] puntos3D;
    Edge2[] bordes3D;
    double distancia;
    String archivo =  "/Users/isamuriel/Documents/Proyectos Computacion grafica/Semana4/compgraf/cubo.txt";

    double radio = 500;
    double theta = Math.toRadians(0);
    double phi = Math.toRadians(0);

    public EventoTeclado3D() {
        this.setFocusable(true);
        this.requestFocusInWindow();
        this.addKeyListener(this);

        this.puntos3D = Dibujar.getPoints3D(archivo);
        this.bordes3D = Dibujar.getEdges3D(puntos3D, archivo);
        this.distancia = -500;
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

        perspective(distancia);
        // camera();
    }

    @Override
    public void keyPressed(KeyEvent e){
        int tecla = e.getKeyCode();
        switch(tecla){
            case KeyEvent.VK_RIGHT:    // Trasladar X en sentido positivo
                translate(10,0,0);
                break;
            case KeyEvent.VK_LEFT:     // Trasladar X en sentido negativo
                translate(-10,0,0);
                break;
            case KeyEvent.VK_UP:        // Trasladar Y en sentido positivo
                translate(0,10,0);
                break;
            case KeyEvent.VK_DOWN:      // Trasladar Y en sentido negativo
                translate(0,-10,0);
                break;
            case KeyEvent.VK_M:         // Trasladar en Z en sentido positivo
                translate(0,0,10);
                break;
            case KeyEvent.VK_N:         // Trasladar en Z en sentido negativo
                translate(0,0,-10);
                break;
            case KeyEvent.VK_A:         // Rotar en X en sentido positivo
                rotateX(10);
                break;
            case KeyEvent.VK_D:         // Rotar en X en sentido negativo
                rotateX(-10);
                break;
            case KeyEvent.VK_W:         // Rotar en Y en sentido positivo
                rotateY(10);
                break;
            case KeyEvent.VK_S:         // Rotar en Y en sentido negativo
                rotateY(-10);
                break;
            case KeyEvent.VK_Q:         // Rotar en Z en sentido positivo
                rotateZ(10);
                break;
            case KeyEvent.VK_E:         // Rotar en Z en sentido negativo
                rotateZ(-10);
                break;
            case KeyEvent.VK_J:         // Escalar en X en sentido positivo
                scale(1.1, 1, 1);
                break;
            case KeyEvent.VK_L:         // Escalar en X en sentido negativo
                scale(0.9, 1, 1);
                break;
            case KeyEvent.VK_I:         // Escalar en Y en sentido positivo
                scale(1, 1.1, 1);
                break;
            case KeyEvent.VK_K:         // Escalar en Y en sentido negativo
                scale(1, 0.9, 1);
                break;
            case KeyEvent.VK_O:         // Escalar en Z en sentido positivo
                scale(1, 1, 1.1);
                break;
            case KeyEvent.VK_U:         // Escalar en Z en sentido negativo
                scale(1, 1, 0.9);
                break;
            case KeyEvent.VK_H:         // Rebaja la perspectiva
                perspective(distancia -= 20);
                break;
            case KeyEvent.VK_G:         // Aumenta la perspectiva
                perspective(distancia += 20);
                break;
        }
        repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}

    /**
     * Proyecta la figura 
     * @param d  Distancia desde la que se observa la figura
    */
    public void perspective(double d){
        Matrix4x4 mPerspective = new Matrix4x4();
        mPerspective.matriz[0][0] = 1;
        mPerspective.matriz[0][1] = 0;
        mPerspective.matriz[0][2] = 0;
        mPerspective.matriz[0][3] = 0;
        mPerspective.matriz[1][0] = 0;
        mPerspective.matriz[1][1] = 1;
        mPerspective.matriz[1][2] = 0;
        mPerspective.matriz[1][3] = 0;
        mPerspective.matriz[2][0] = 0;
        mPerspective.matriz[2][1] = 0;
        mPerspective.matriz[2][2] = 1;
        mPerspective.matriz[2][3] = 0;
        mPerspective.matriz[3][0] = 0;
        mPerspective.matriz[3][1] = 0;
        mPerspective.matriz[3][2] = (double)1/d;
        mPerspective.matriz[3][3] = 0;

        for(Edge2 linea : bordes3D){
            Point3 p1 = linea.point1;
            Point3 p2 = linea.point2;

            p1 = Matrix4x4.times(mPerspective, p1);
            p2 = Matrix4x4.times(mPerspective, p2);

            // Normalizar puntos
            p1.x /= p1.w;
            p1.y /= p1.w;
            p1.z /= p1.w;
            p1.w /= p1.w;

            p2.x /= p2.w;
            p2.y /= p2.w;
            p2.z /= p2.w;
            p2.w /= p2.w;

            Line2D line = new Line2D.Double(p1.x, p1.y, p2.x, p2.y);
            dibujar(line);
        }
    }

    /**
     * Traslada la figura 
     * @param dx   Numero de pixeles a cambiar en x
     * @param dy   Numero de pixeles a cambiar en y
     * @param dz   Numero de pixeles a cambiar en z
     */
    public void translate(double dx, double dy, double dz){
        Matrix4x4 mTraslacion = new Matrix4x4();
        mTraslacion.matriz[0][0] = 1;
        mTraslacion.matriz[0][1] = 0;
        mTraslacion.matriz[0][2] = 0;
        mTraslacion.matriz[0][3] = dx;
        mTraslacion.matriz[1][0] = 0;
        mTraslacion.matriz[1][1] = 1;
        mTraslacion.matriz[1][2] = 0;
        mTraslacion.matriz[1][3] = dy;
        mTraslacion.matriz[2][0] = 0;
        mTraslacion.matriz[2][1] = 0;
        mTraslacion.matriz[2][2] = 1;
        mTraslacion.matriz[2][3] = dz;
        mTraslacion.matriz[3][0] = 0;
        mTraslacion.matriz[3][1] = 0;
        mTraslacion.matriz[3][2] = 0;
        mTraslacion.matriz[3][3] = 1;

        for(int i = 0; i < puntos3D.length; ++i){
            puntos3D[i] = Matrix4x4.times(mTraslacion, puntos3D[i]);
        }
        bordes3D = Dibujar.getEdges3D(puntos3D, archivo);
        // repaint();
    }

    /**
     * Rota la figura en torno al eje x
     * @param grad   Angulo de rotación en grados
     */
    public void rotateX(int grad){
        double rad = grad * Math.PI/180;    // Conversion de grados a radianes

        Matrix4x4 mRotacion = new Matrix4x4();
        mRotacion.matriz[0][0] = 1;
        mRotacion.matriz[0][1] = 0;
        mRotacion.matriz[0][2] = 0;
        mRotacion.matriz[0][3] = 0;
        mRotacion.matriz[1][0] = 0;
        mRotacion.matriz[1][1] = Math.cos(rad);
        mRotacion.matriz[1][2] = - Math.sin(rad);
        mRotacion.matriz[1][3] = 0;
        mRotacion.matriz[2][0] = 0;
        mRotacion.matriz[2][1] = Math.sin(rad);
        mRotacion.matriz[2][2] = Math.cos(rad);
        mRotacion.matriz[2][3] = 0;
        mRotacion.matriz[3][0] = 0;
        mRotacion.matriz[3][1] = 0;
        mRotacion.matriz[3][2] = 0;
        mRotacion.matriz[3][3] = 1;

        Point3 pivote = findCentroid();

        translate(- pivote.x, - pivote.y, - pivote.z);   // Traslada la figura al origen

        for(int i = 0; i < puntos3D.length; ++i){
            puntos3D[i] = Matrix4x4.times(mRotacion, puntos3D[i]);
        }
        bordes3D = Dibujar.getEdges3D(puntos3D, archivo);
        translate(pivote.x, pivote.y, pivote.z);  // Devuelve la figura a su posición original
    }

    /**
     * Rota la figura en torno al eje y
     * @param grad   Angulo de rotación en grados
     */
    public void rotateY(int grad){
        double rad = grad * Math.PI/180;    // Conversion de grados a radianes

        Matrix4x4 mRotacion = new Matrix4x4();
        mRotacion.matriz[0][0] = Math.cos(rad);
        mRotacion.matriz[0][1] = 0;
        mRotacion.matriz[0][2] = Math.sin(rad);
        mRotacion.matriz[0][3] = 0;
        mRotacion.matriz[1][0] = 0;
        mRotacion.matriz[1][1] = 1;
        mRotacion.matriz[1][2] = 0;
        mRotacion.matriz[1][3] = 0;
        mRotacion.matriz[2][0] = - Math.sin(rad);
        mRotacion.matriz[2][1] = 0;
        mRotacion.matriz[2][2] = Math.cos(rad);
        mRotacion.matriz[2][3] = 0;
        mRotacion.matriz[3][0] = 0;
        mRotacion.matriz[3][1] = 0;
        mRotacion.matriz[3][2] = 0;
        mRotacion.matriz[3][3] = 1;

        Point3 pivote = findCentroid();

        translate(- pivote.x, - pivote.y, - pivote.z);   // Traslada la figura al origen

        for(int i = 0; i < puntos3D.length; ++i){
            puntos3D[i] = Matrix4x4.times(mRotacion, puntos3D[i]);
        }
        bordes3D = Dibujar.getEdges3D(puntos3D, archivo);
        translate(pivote.x, pivote.y, pivote.z);  // Devuelve la figura a su posición original
    }

    /**
     * Rota la figura en torno al eje z
     * @param grad   Angulo de rotación en grados
     */
    public void rotateZ(int grad){
        double rad = grad * Math.PI/180;    // Conversion de grados a radianes

        Matrix4x4 mRotacion = new Matrix4x4();
        mRotacion.matriz[0][0] = Math.cos(rad);
        mRotacion.matriz[0][1] = - Math.sin(rad);
        mRotacion.matriz[0][2] = 0;
        mRotacion.matriz[0][3] = 0;
        mRotacion.matriz[1][0] = Math.sin(rad);
        mRotacion.matriz[1][1] = Math.cos(rad);
        mRotacion.matriz[1][2] = 0;
        mRotacion.matriz[1][3] = 0;
        mRotacion.matriz[2][0] = 0;
        mRotacion.matriz[2][1] = 0;
        mRotacion.matriz[2][2] = 1;
        mRotacion.matriz[2][3] = 0;
        mRotacion.matriz[3][0] = 0;
        mRotacion.matriz[3][1] = 0;
        mRotacion.matriz[3][2] = 0;
        mRotacion.matriz[3][3] = 1;

        Point3 pivote = findCentroid();

        translate(- pivote.x, - pivote.y, - pivote.z);   // Traslada la figura al origen

        for(int i = 0; i < puntos3D.length; ++i){
            puntos3D[i] = Matrix4x4.times(mRotacion, puntos3D[i]);
        }
        bordes3D = Dibujar.getEdges3D(puntos3D, archivo);
        translate(pivote.x, pivote.y, pivote.z);  // Devuelve la figura a su posición original
    }

    /**
     * Aumenta el tamaño de la figura
     * @param sx  Numero de pixeles a aumentar en x
     * @param sy  Numero de pixeles a aumentar en y
     * @param sz  Numero de pixeles a aumentar en z
     */
    public void scale(double sx, double sy, double sz){
        Matrix4x4 mEscalamiento = new Matrix4x4();
        mEscalamiento.matriz[0][0] = sx;
        mEscalamiento.matriz[0][1] = 0;
        mEscalamiento.matriz[0][2] = 0;
        mEscalamiento.matriz[0][3] = 0;
        mEscalamiento.matriz[1][0] = 0;
        mEscalamiento.matriz[1][1] = sy;
        mEscalamiento.matriz[1][2] = 0;
        mEscalamiento.matriz[1][3] = 0;
        mEscalamiento.matriz[2][0] = 0;
        mEscalamiento.matriz[2][1] = 0;
        mEscalamiento.matriz[2][2] = sz;
        mEscalamiento.matriz[2][3] = 0;
        mEscalamiento.matriz[3][0] = 0;
        mEscalamiento.matriz[3][1] = 0;
        mEscalamiento.matriz[3][2] = 0;
        mEscalamiento.matriz[3][3] = 1;

        Point3 pivote = findCentroid();

        translate(- pivote.x, - pivote.y, - pivote.z);   // Traslada la figura al origen

        for(int i = 0; i < puntos3D.length; ++i){
            puntos3D[i] = Matrix4x4.times(mEscalamiento, puntos3D[i]);
        }
        bordes3D = Dibujar.getEdges3D(puntos3D, archivo);
        translate(pivote.x, pivote.y, pivote.z);    // Devuelve la figura a su posición original  
    }

    /**
     * Encuentra el centroide de la figura
     * @return Punto centroide
     */
    public Point3 findCentroid(){
        double xMax = Integer.MIN_VALUE;
        double yMax = Integer.MIN_VALUE;
        double yMin = Integer.MAX_VALUE;
        double xMin = Integer.MAX_VALUE;
        double zMax = Integer.MIN_VALUE;
        double zMin = Integer.MAX_VALUE;
        //Recorre cada punto de la figura y busca el maximo y minimo de cada variable
        for (int i = 0; i < bordes3D.length; ++i) {
            Edge2 borde = bordes3D[i];
            if (borde.point1.x > xMax) {
                xMax = borde.point1.x;
            }
            if (borde.point2.x > xMax) {
                xMax = borde.point2.x;
            }
            if (borde.point1.y > yMax) {
                yMax = borde.point1.y;
            }
            if (borde.point2.y > yMax) {
                yMax = borde.point1.y;
            }
            if (borde.point1.x < xMin) {
                xMin = borde.point1.x;
            }
            if (borde.point2.x < xMin) {
                xMin = borde.point2.x;
            }
            if (borde.point1.y < yMin) {
                yMin = borde.point1.y;
            }
            if (borde.point2.y < yMin) {
                yMin = borde.point2.y;
            }
            if (borde.point1.z > zMax) {
                zMax = borde.point1.z;
            }
            if (borde.point2.z > zMax) {
                zMax = borde.point2.z;
            }
            if (borde.point1.z < zMin) {
                zMin = borde.point1.z;
            }
            if (borde.point2.z < zMin) {
                zMin = borde.point2.z;
            }
        }
        // Crea el punto centroide
        Point3 centroid = new Point3(xMax - ((xMax - xMin) / 2), yMax - ((yMax - yMin) / 2), zMax - ((zMax - zMin) / 2), 1);
        return centroid;
    }

    /**
     * Dibuja las lineas de la figura
     * @param linea   Linea a dibujar
     */
    public void dibujar(Line2D linea){
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
        JFrame frame = new JFrame("Evento Teclado 3D");
        // Al cerrar el frame, termina la ejecución de este programa
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Agregar un JPanel que se llama Points (esta clase)
        EventoTeclado3D et = new EventoTeclado3D();
        frame.add(et);
        // Asignarle tamaño
        frame.setSize(1000, 1000);
        // Poner el frame en el centro de la pantalla
        frame.setLocationRelativeTo(null);
        // Mostrar el frame
        frame.setVisible(true);
    }
}