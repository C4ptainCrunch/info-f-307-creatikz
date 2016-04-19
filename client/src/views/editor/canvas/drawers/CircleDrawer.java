package views.editor.canvas.drawers;

import java.awt.geom.Ellipse2D;

import models.tikz.TikzCircle;
import models.tikz.TikzComponent;
import views.editor.canvas.drawables.DrawableTikzComponent;

public class CircleDrawer extends ComponentDrawer {

    public CircleDrawer() {
        // this was left intentionally blank
    }

    @Override
    public DrawableTikzComponent toDrawable(TikzComponent component) {
        TikzCircle circle = (TikzCircle) component;
        DrawableTikzComponent drawableComponent = super.toDrawable(circle);

        float size = circle.getRadius() * 2;
        drawableComponent.addShape(new Ellipse2D.Float(0, 0, size, size));
        drawableComponent.setBackground(circle.getBackgroundColor());
        return drawableComponent;
    }
}