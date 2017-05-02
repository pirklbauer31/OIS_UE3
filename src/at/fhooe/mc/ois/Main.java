package at.fhooe.mc.ois;

import java.awt.*;
import java.util.Arrays;

/**
 * Created by Pirklbauer on 06.04.2017.
 */
public class Main {
    public static void main(String[] args) {
        Matrix m1 = new Matrix(1,2,2,4,5,6,7,8,9);
        System.out.println(m1.toString());
        System.out.println(m1.invers().toString());
        Matrix m2 = new Matrix(2,3,4,5,5,6,7,8,9);
        System.out.println(m2.invers().toString());
        System.out.println(m1.multiply(m2).toString());
        Point p1 = new Point(3,4);
        System.out.println(m1.multiply(p1).toString());
        Rectangle r1 = new Rectangle(2,3);
        r1.add(p1);
        System.out.println(m1.multiply(r1).toString());
        Polygon poly1 = new Polygon();
        poly1.addPoint(1,1);
        poly1.addPoint(2,2);
        poly1.addPoint(3,3);
        System.out.println(Arrays.toString(m1.multiply(poly1).xpoints));
        System.out.println(Arrays.toString(m1.multiply(poly1).ypoints) + "\n");

        System.out.println(Matrix.translate(2,3).toString());
        System.out.println(Matrix.translate(new Point(4,5)).toString());
        System.out.println(Matrix.scale(3).toString());
        System.out.println(Matrix.mirrorX().toString());
        System.out.println(Matrix.mirrorY().toString());
        System.out.println(Matrix.rotate(3).toString());

        testZTF();

        GISModel model = new GISModel();
        GISController controller = new GISController(model);
        GISView view = new GISView(controller);

        model.addObserver(view);
    }

    public static void testZTF() {
        Rectangle world = new Rectangle(47944531, 608091485, 234500, 213463);
        Rectangle window = new Rectangle(0,0,640,480);

        double zoomX = Matrix.getZoomFactorX(world,window);
        double zoomY = Matrix.getZoomFactorY(world,window);
        System.out.println("zoomX  = " + zoomX + " zoom Y = " + zoomY);

        Matrix zoom = Matrix.zoomToFit(world,window);
        System.out.println(zoom.toString());
        Rectangle test = zoom.multiply(world);
        //System.out.println(test.getX() + " " + test.getY() + " " + test.getWidth() + " " + test.getHeight());
        System.out.println(test.toString());
        zoom = zoom.invers();
        test = zoom.multiply(test);
        System.out.println(test.toString());
    }
}
