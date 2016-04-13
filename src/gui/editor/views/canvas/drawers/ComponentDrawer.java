package gui.editor.views.canvas.drawers;

import java.awt.*;
import java.util.Vector;

import gui.editor.views.canvas.drawables.DrawableTikzComponent;
import models.TikzComponent;
import constants.Models;
import gui.editor.views.canvas.drawables.Drawable;
import gui.editor.views.canvas.drawables.DrawableText;


public abstract class ComponentDrawer implements TikzDrawer {

    public DrawableTikzComponent toDrawable(TikzComponent component){
        DrawableTikzComponent drawableComponent = new DrawableTikzComponent(component);
        return drawableComponent;
    }
}
