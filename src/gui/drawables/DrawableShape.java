package gui.drawables;
import java.awt.*;
import java.awt.geom.AffineTransform;

public class DrawableShape implements Drawable{
    private Shape shape;
    private Stroke stroke;
    private Color color;

    public DrawableShape(Shape shape, Stroke stroke, Color color){
        this.shape = shape;
        this.stroke = stroke;
        this.color = color;
    }

    public void draw(Graphics2D g){
        Stroke old_stroke = g.getStroke();
        Color old_color = g.getColor();

        g.setStroke(this.stroke);
        g.setColor(this.color);

        g.draw(this.shape);

        g.setColor(old_color);
        g.setStroke(old_stroke);
    }

    public void translate(Point translation){
        AffineTransform transform = new AffineTransform(
                1, 0, 0, 1, translation.getX(), translation.getY()
        );
        this.shape = transform.createTransformedShape(this.shape);
    }
}
