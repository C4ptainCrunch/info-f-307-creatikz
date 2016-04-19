package views.editor.canvas.drawers;

import java.awt.*;

import models.tikz.TikzComponent;
import models.tikz.TikzRectangle;
import views.editor.canvas.drawables.DrawableTikzComponent;

public class RectangleDrawer extends ComponentDrawer {

    public RectangleDrawer() {
        // this was left intentionally blank
    }

    @Override
    public DrawableTikzComponent toDrawable(TikzComponent component) {
        TikzRectangle rectangle = (TikzRectangle) component;
        DrawableTikzComponent drawableComponent = super.toDrawable(rectangle);
        drawableComponent.addShape(new Rectangle(rectangle.getWidth(), rectangle.getLength()));
        drawableComponent.setBackground(rectangle.getBackgroundColor());
        return drawableComponent;

    }
}