package gui.editor.views.canvas.drawers;

import java.awt.*;
import java.util.Vector;

import models.TikzRectangle;
import gui.editor.views.canvas.drawables.Drawable;
import gui.editor.views.canvas.drawables.DrawableShape;

public class RectangleDrawer extends ComponentDrawer {

    public RectangleDrawer(TikzRectangle component) {
            this.component = component;
    }

    public TikzRectangle getComponent(){
        return (TikzRectangle) this.component;
    }

    public Vector<Drawable> toDrawable(){
        Vector<Drawable> vec = super.toDrawable();

        Drawable shape = new DrawableShape(
                new Rectangle(getComponent().getWidth(), getComponent().getLength()),
                new BasicStroke(2),
                getComponent().getColor(),
                getComponent().getBackground(),
                true
        );
        vec.add(shape);
        return vec;
    }
}