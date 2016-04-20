package views.editor.canvas.drawers;

import java.awt.*;
import java.awt.geom.Line2D;

import constants.Models;
import misc.utils.Converter;
import models.tikz.TikzComponent;
import models.tikz.TikzEdge;
import views.editor.canvas.drawables.DrawableTikzEdge;

import javax.swing.*;

public abstract class EdgeDrawer extends ComponentDrawer {

    public EdgeDrawer() {
        // this was left intentionally blank
    }

    @Override
    public DrawableTikzEdge toDrawable(TikzComponent component, JComponent panel) {
        TikzEdge edge = (TikzEdge) component;
        DrawableTikzEdge drawableComponent = new DrawableTikzEdge(component);
        drawableComponent.addShape(new Line2D.Float(Converter.tikz2swing(fromPosition(edge), panel), Converter.tikz2swing(toPosition(edge), panel)));
        return drawableComponent;
    }

    public Point fromPosition(TikzEdge edge){
        Point start;
        if(edge.getFirstNode() == null || edge.getSecondNode() == null){
            start = new Point(-Models.DEFAULT.EDGE_X_LENGTH/2, 0);
        }
        else{
            start = edge.getFromPosition();
        }
      return start;
    }

    public Point toPosition(TikzEdge edge){
        Point end;
        if(edge.getFirstNode() == null || edge.getSecondNode() == null){
            end = new Point(Models.DEFAULT.EDGE_X_LENGTH/2, 0);
        }
        else{
            end = edge.getToPosition();
        }
        return end;
    }
}
