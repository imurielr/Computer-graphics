package compgraf;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import javax.swing.JPanel;

import transformaciones3D.Edge3D;

import javax.swing.JFrame;

public class Dibujar extends JPanel {
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

        g2d.setColor(Color.RED);
        dibujar(g2d, h, w, leerArchivo("/Users/isamuriel/Documents/Proyectos Computacion grafica/Semana4/compgraf/barco.txt"));
        g2d.setColor(Color.RED);
    }

    public static Edge[] leerArchivo(String archivo){
        try{
            String fileName = archivo;
            File file = new File(fileName);
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String line = br.readLine();
            int numPuntos = Integer.parseInt(line);
            Point[] puntos = new Point[numPuntos];
            for(int i = 0; i < numPuntos; ++i){
                line = br.readLine();
                String[] punto = line.split(" ");
                puntos[i] = new Point(Double.parseDouble(punto[0]), Double.parseDouble(punto[1]));
            }
            line = br.readLine();
            int numEdges = Integer.parseInt(line);
            Edge[] bordes = new Edge[numEdges];
            for(int i = 0; i < numEdges; ++i){
                line = br.readLine();
                String[] edge = line.split(" ");
                bordes[i] = new Edge(puntos[Integer.parseInt(edge[0])], puntos[Integer.parseInt(edge[1])]);
            }
            return bordes;
        }
        catch(IOException e){}
        return null;
    }

    public static Point2[] getPoints(String archivo){
        try{
            String fileName = archivo;
            File file = new File(fileName);
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String line = br.readLine();
            int numPuntos = Integer.parseInt(line);
            Point2[] puntos = new Point2[numPuntos];
            for(int i = 0; i < numPuntos; ++i){
                line = br.readLine();
                String[] punto = line.split(" ");
                puntos[i] = new Point2(Double.parseDouble(punto[0]), Double.parseDouble(punto[1]), 1);
            }
            return puntos;
        }
        catch(IOException e){}
        return null;
    }

    public static void dibujar(Graphics2D g, int h, int w, Edge[] bordes){
        int borderX = w - ((2 * w + 1) + 1);
        int borderY = h - ((2 * h + 1) + 1);
        int x0;
        int y0;
        int x1;
        int y1;
        for(int i = 0; i < bordes.length; ++i){
            x0 = ((int)bordes[i].point1.x + w) + borderX / 2;
            y0 = ((int)bordes[i].point1.y + h) + borderY / 2;
            x1 = ((int)bordes[i].point2.x + w) + borderX / 2;
            y1 = ((int)bordes[i].point2.y + h) + borderY / 2;

            g.drawLine((int)x0, h - (int)y0, (int)x1, h - (int)y1);
        }
    }

    public static Edge[] getEdges(Point2[] puntos, String archivo){
        try{
            String fileName = archivo;
            File file = new File(fileName);
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String line = br.readLine();
            int numPuntos = Integer.parseInt(line);
            for(int i = 0; i < numPuntos; ++i){
                line = br.readLine();
            }
            line = br.readLine();
            int numEdges = Integer.parseInt(line);
            Edge[] bordes = new Edge[numEdges];
            for(int i = 0; i < numEdges; ++i){
                line = br.readLine();
                String[] edge = line.split(" ");
                bordes[i] = new Edge(new Point(puntos[Integer.parseInt(edge[0])].x, puntos[Integer.parseInt(edge[0])].y), new Point(puntos[Integer.parseInt(edge[1])].x, puntos[Integer.parseInt(edge[1])].y));
            }
            return bordes;
        }
        catch(IOException e){}
        return null;
    }

    public static Point3[] getPoints3D(String archivo){
        try{
            String fileName = archivo;
            File file = new File(fileName);
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String line = br.readLine();
            int numPuntos = Integer.parseInt(line);
            Point3[] puntos = new Point3[numPuntos];
            for(int i = 0; i < numPuntos; ++i){
                line = br.readLine();
                String[] punto = line.split(" ");
                puntos[i] = new Point3(Double.parseDouble(punto[0]), Double.parseDouble(punto[1]), Double.parseDouble(punto[2]), 1);
            }
            return puntos;
        }
        catch(IOException e){}
        return null;
    }

    public static Edge2[] getEdges3D(Point3[] puntos, String archivo){
        try{
            String fileName = archivo;
            File file = new File(fileName);
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String line = br.readLine();
            int numPuntos = Integer.parseInt(line);
            for(int i = 0; i < numPuntos; ++i){
                line = br.readLine();
            }
            line = br.readLine();
            int numEdges = Integer.parseInt(line);
            Edge2[] bordes = new Edge2[numEdges];
            for(int i = 0; i < numEdges; ++i){
                line = br.readLine();
                String[] edge = line.split(" ");
                bordes[i] = new Edge2(new Point3(puntos[Integer.parseInt(edge[0])].x, puntos[Integer.parseInt(edge[0])].y, puntos[Integer.parseInt((edge[0]))].z, puntos[Integer.parseInt((edge[0]))].w), new Point3(puntos[Integer.parseInt(edge[1])].x, puntos[Integer.parseInt(edge[1])].y, puntos[Integer.parseInt(edge[1])].z, puntos[Integer.parseInt(edge[1])].w));
            }
            return bordes;
        }
        catch(IOException e){}
        return null;
    }

    public static Edge2[] leerArchivo3D(String archivo){
        Point3[] puntos = getPoints3D(archivo);
        Edge2[] bordes = getEdges3D(puntos, archivo);
        return bordes;
    }

    public static void main(String[] args){
    // Crear un nuevo Frame
    JFrame frame = new JFrame("Dibujo");
    // Al cerrar el frame, termina la ejecución de este programa
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    // Agregar un JPanel que se llama Points (esta clase)
    frame.add(new Dibujar());
    // Asignarle tamaño
    frame.setSize(500, 500);
    // Poner el frame en el centro de la pantalla
    frame.setLocationRelativeTo(null);
    // Mostrar el frame
    frame.setVisible(true);
    }
}