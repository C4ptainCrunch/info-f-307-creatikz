package gui.editor.views.canvas.drawers;

import gui.editor.views.canvas.drawables.Drawable;
import gui.editor.views.canvas.drawables.DrawableShape;
import models.TikzTriangle;

import java.awt.*;
import java.util.Vector;

/**
 * Created by aurelien on 12/04/16.
 */
public class TriangleDrawer extends ComponentDrawer {

    public TriangleDrawer(TikzTriangle component) {
        this.component = component;
    }

    public TikzTriangle getComponent(){
        return (TikzTriangle) this.component;
    }

    public Vector<Drawable> toDrawable(){
        Vector<Drawable> vec = super.toDrawable();

        Drawable shape = new DrawableShape(
                getAwtPolygon(),
                new BasicStroke(2),
                getComponent().getColor(),
                getComponent().getBackground(),
                true
        );
        vec.add(shape);
        return vec;
    }

    private double[] getAngles(TikzTriangle comp){
        double [] angles = new double[3];
        double a = comp.getSideA();
        double b = comp.getSideB();
        double c = comp.getSideC();
        angles[0] = Math.acos((-Math.pow(a,2)+Math.pow(b, 2)+Math.pow(c,2))/(2*b*c));
        angles[1] = Math.acos((-Math.pow(b,2)+Math.pow(a, 2)+Math.pow(c,2))/(2*a*c));
        angles[2] = Math.acos((-Math.pow(c,2)+Math.pow(a, 2)+Math.pow(b,2))/(2*a*b));
        return angles;
    }


    private Polygon getAwtPolygon() {

        int[] x = new int[3];
        int[] y = new int[3];
        int[] sides = ((TikzTriangle) component).getSides();
        double[] angles = getAngles((TikzTriangle) component);
        x[0] = 0;
        y[0] = (int)(Math.sin(angles[1])*sides[1]);
        x[1] = sides[0];
        y[1] = (int)(Math.sin(angles[1])*sides[1]);;
        x[2] = (int)(Math.cos(angles[0])*sides[2]);
        y[2] = 0;
        Polygon p = new Polygon(x, y, 3);
        return p;
    }

}