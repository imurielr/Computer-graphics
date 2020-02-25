package compgraf;

class Edge2{
    Point3 point1;
    Point3 point2;

    public Edge2(Point3 point1, Point3 point2){
        this.point1 = point1;
        this.point2 = point2;
    }

    public void print(){
        System.out.println("");
        System.out.println(point1.x+" "+point1.y+" "+point1.z);
        System.out.println(point2.x+" "+point2.y+" "+point2.z);
    }
}