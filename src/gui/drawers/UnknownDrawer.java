package gui.drawers;

import gui.drawables.Drawable;

import java.awt.*;
import java.util.Vector;

public class UnknownDrawer extends ComponentDrawer{
    public Vector<Drawable> toDrawable(){
        return new Vector<>();
    }
}