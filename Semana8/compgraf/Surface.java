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

public class Surface extends JPanel implements KeyListener {
    Graphics2D g2d;
    int w, h;
    ArrayList<Edge2> bordes3D;    // Bordes del objeto
    ArrayList<Edge2> bordesCamara;   // Bordes a dibujar desde el punto de vista de la camara
    double distancia;    // Distancia desde la que se objerva el objeto

    double radio = 500;  // Distancia de la camara al objeto
    double theta = 0;
    double phi = 0;
    double xCamera = 0;
    double yCamera = 0;
    double zCamera = 100;

    Point3[][] puntos = { 
                            { 
                                new Point3(-150.0, -100.0, 1100.0, 1),
                                new Point3(-50.0, 0.0, 1100.0, 1),
                                new Point3(50.0, 0.0, 1100.0, 1),
                                new Point3(150.0, 100.0, 1100.0, 1),
                            },
                            {
                                new Point3(-150.0, 0.0, 1000.0, 1),
                                new Point3(-50.0, 0.0, 1000.0, 1),
                                new Point3(50.0, 0.0, 1000.0, 1),
                                new Point3(150.0, 0.0, 1000.0, 1)
                            },
                            {
                                new Point3(-150.0, -100.0, 900.0, 1),
                                new Point3(-50.0, 0.0, 900.0, 1),
                                new Point3(50.0, 0.0, 900.0, 1),
                                new Point3(150.0, 100.0, 900.0, 1)
                            }
                        };

    public Surface() {
        this.setFocusable(true);
        this.requestFocusInWindow();
        this.addKeyListener(this);

        // this.bordes3D = Dibujar.getEdges3D(archivo);
        this.bordes3D = new ArrayList<>();
        this.bordesCamara = new ArrayList<>();
        this.distancia = -800;
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
        
        g2d.setColor(Color.BLUE);

        puntosControl();
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
            case KeyEvent.VK_C:         // Rotar camara a la izquierda
                theta -= 10;
                break;
            case KeyEvent.VK_B:         // Rotar camara a la derecha
                theta += 10;
                break;
            case KeyEvent.VK_V:         // Rotar camara hacia abajo
                if (phi > -79) {
                    phi -= 10;
                }else if (phi>-89){
                    phi-= 89+phi;
                }
                break;
            case KeyEvent.VK_F:        // Rotar camara hacia arriba
                if (phi < 79) {
                    phi += 10;
                }else if (phi<89){
                    phi+=(89-phi);
                }
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

        for(Edge2 borde : bordesCamara){
            Point3 p1 = borde.point1;
            Point3 p2 = borde.point2;

            p1 = Matrix4x4.times(mPerspective, p1);
            p2 = Matrix4x4.times(mPerspective, p2);

            // Normalizar puntos
            p1.normalize();
            p2.normalize();

            Line2D line = new Line2D.Double(p1.x, p1.y, p2.x, p2.y);
            dibujar(line);
        }
        bordesCamara.clear();
    }

    /**
     * Visualiza el objeto a traves de la camara
     */
    public void centerCamera(){
        // Coordenadas de la camara
        double r = radio * Math.cos(Math.toRadians(phi));
        xCamera = r * Math.sin(Math.toRadians(theta));
        yCamera = radio * Math.sin(Math.toRadians(phi));
        zCamera = r * Math.cos(Math.toRadians(theta));

        // Centro del objeto
        Point3 pivote = findCentroid();

        // Ubico la camara mirando hacia el objeto
        xCamera += pivote.x;
        yCamera += pivote.y;
        zCamera += pivote.z;

        // Puntos para generar el vector lookAt
        double x = xCamera - pivote.x;
        double y = yCamera - pivote.y;
        double z = zCamera - pivote.z;

        // Vectores necesarios para generar la matriz de traslación de la camara
        Vector3 n = new Vector3(x, y, z).normalize();  // vector lookAt = N/|N|     N = vector objeto - vector camara
        Vector3 upVector = new Vector3(0, 1, 0);  // vector V
        Vector3 u = Vector3.crossProduct(upVector, n).normalize();   // u = (V x n) / (|V x n|)

        Vector3 v = Vector3.crossProduct(n, u); // v = n x u
        Vector3 p0 = new Vector3(xCamera, yCamera, zCamera);  // Punto inicial de la camara
        
        Matrix4x4 mTraslacion = new Matrix4x4();
        mTraslacion.matriz[0][0] = u.u;
        mTraslacion.matriz[0][1] = u.v;
        mTraslacion.matriz[0][2] = u.w;
        mTraslacion.matriz[0][3] = - Vector3.dotProduct(u, p0);
        mTraslacion.matriz[1][0] = v.u;
        mTraslacion.matriz[1][1] = v.v;
        mTraslacion.matriz[1][2] = v.w;
        mTraslacion.matriz[1][3] = - Vector3.dotProduct(v, p0);
        mTraslacion.matriz[2][0] = n.u;
        mTraslacion.matriz[2][1] = n.v;
        mTraslacion.matriz[2][2] = n.w;
        mTraslacion.matriz[2][3] = - Vector3.dotProduct(n, p0);
        mTraslacion.matriz[3][0] = 0;
        mTraslacion.matriz[3][1] = 0;
        mTraslacion.matriz[3][2] = 0;
        mTraslacion.matriz[3][3] = 1;

        // Realizo la rotación de la camara
        multPoints(mTraslacion);
        // Proyecto el objeto
        perspective(distancia);
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

        // Recorre la matriz de puntos y multiplica cada uno de los puntos por la matriz de transformacion
        for(int i = 0; i < puntos.length; ++i){
            for(int j = 0; j < puntos[0].length; ++j){
                puntos[i][j] = Matrix4x4.times(mTraslacion, puntos[i][j]);
            }
        }
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

        // Recorre la matriz de puntos y multiplica cada uno de los puntos por la matriz de transformacion
        for(int i = 0; i < puntos.length; ++i){
            for(int j = 0; j < puntos[0].length; ++j){
                puntos[i][j] = Matrix4x4.times(mRotacion, puntos[i][j]);
            }
        }

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

        // Recorre la matriz de puntos y multiplica cada uno de los puntos por la matriz de transformacion
        for(int i = 0; i < puntos.length; ++i){
            for(int j = 0; j < puntos[0].length; ++j){
                puntos[i][j] = Matrix4x4.times(mRotacion, puntos[i][j]);
            }
        }

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

        // Recorre la matriz de puntos y multiplica cada uno de los puntos por la matriz de transformacion
        for(int i = 0; i < puntos.length; ++i){
            for(int j = 0; j < puntos[0].length; ++j){
                puntos[i][j] = Matrix4x4.times(mRotacion, puntos[i][j]);
            }
        }

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

        // Recorre la matriz de puntos y multiplica cada uno de los puntos por la matriz de transformacion
        for(int i = 0; i < puntos.length; ++i){
            for(int j = 0; j < puntos[0].length; ++j){
                puntos[i][j] = Matrix4x4.times(mEscalamiento, puntos[i][j]);
            }
        }

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
        for (int i = 0; i < bordes3D.size(); ++i) {
            Edge2 borde = bordes3D.get(i);
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
     * Multiplica todos los puntos del objeto por la matriz y los almacena en la lista de bordes a dibujar
     * @param matrix    Matriz por la que se multiplican los puntos
     */
    private void multPoints(Matrix4x4 matrix){
        //Método que multiplica cada punto del dibujo por la matriz dada
        for (Edge2 borde : bordes3D) {
            //Obtengo puntos de inicio y fin de cada punto
            Point3 in = borde.point1;
            Point3 fin = borde.point2;
            //Multiplico los puntos por matriz
            Point3 punto1 = Matrix4x4.times(matrix, in);
            Point3 punto2 = Matrix4x4.times(matrix, fin);

            bordesCamara.add(new Edge2(punto1, punto2));
        }
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

    /**
     * C(n, k) = n! / (k!(n-k)!)
     * @param n
     * @param k
     * @return result  -->  Valor de C(n,u)
     */
    private Double C(int n, int k){
        Double result = factorial(n) / (factorial(k) * factorial(n - k));
        return result;
    }

    /**
     * BEZ_k,n(u) = C(n, k)u^k(1-u)^(n-k)
     * @param k
     * @param n
     * @param u
     * @return result --> Valor de BEZ_k,n(u)
     */
    private Double BEZ(int k, int n, double u){
        double result = C(n, k) * Math.pow(u, k) * Math.pow(1 - u, n - k);
        return result;
    }

    /**
     * Toma los puntos de control y calcula los puntos de la superficie
     * @param u
     * @param v
     * @return punto --> punto calculado utilizando la funcion de Bezier
     */
    private Point3 P(double u, double v){
        int n = puntos.length - 1;     // Numero de filas
        int m = puntos[0].length - 1;  // Numero de columnas

        Point3 punto = new Point3(0, 0, 0, 1);
        for(int j = 0; j <= m; ++j){
            for(int k = 0; k <= n; ++k){
                punto.x += (puntos[k][j].x * BEZ(j, m, u) * BEZ(k, n, v));
                punto.y += (puntos[k][j].y * BEZ(j, m, u) * BEZ(k, n, v));
                punto.z += (puntos[k][j].z * BEZ(j, m, u) * BEZ(k, n, v));
            }
        }
        return punto;
    }

    /**
     * Toma los puntos por los que pasa la superficie y añade a la lista de bordes cada uno de las lineas de la superficie
     */
    private void puntosControl(){
        bordes3D.clear();    // Limpia la lista de bordes para actualizarla con los nuevos puntos
        for(double u = 0; u < 1; u += 0.01){
            for(double v = 0; v < 1; v += 0.01){
                Point3 p1 = P(u, v);  // Punto inicial de la linea
                Point3 p2 = P(u+0.025, v+0.025);   // Punto final de la linea
                bordes3D.add(new Edge2(p1, p2));   // Crea el nuevo borde y lo añade a la lista
            }
        }
        centerCamera();    // Centra la camara en la superficie y le aplica la proyecta
    }

    /**
     * Calcula n!
     * @param n
     * @return result --> Resultado obtenido al calcular n!
     */
    private Double factorial(int n){
        double result = 1;
        for(; n > 0; --n){
            result *= n;
        }
        return result;
    }

    public static void main(String[] args) {
        // Crear un nuevo Frame
        JFrame frame = new JFrame("Surface");
        // Al cerrar el frame, termina la ejecución de este programa
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Agregar un JPanel que se llama Points (esta clase)
        Surface et = new Surface();
        frame.add(et);
        // Asignarle tamaño
        frame.setSize(900, 900);
        // Poner el frame en el centro de la pantalla
        frame.setLocationRelativeTo(null);
        // Mostrar el frame
        frame.setVisible(true);
    }
}