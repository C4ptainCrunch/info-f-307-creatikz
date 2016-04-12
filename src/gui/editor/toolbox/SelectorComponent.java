package gui.editor.toolbox;

import gui.editor.views.canvas.drawables.Drawable;
import gui.editor.views.canvas.drawers.Drawer;
import models.TikzNode;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.MouseEvent;

public class SelectorComponent extends JPanel {

    private Drawer drawer;
    private TikzNode component;
    private SelectorComponentListener listener;

    public SelectorComponent(Drawer d, TikzNode comp, SelectorComponentListener lis){
        drawer = d;
        component = comp;
        listener = lis;

        initMouseListener();
    }

    public interface SelectorComponentListener{
        void componentSelected(Drawer draw);
    }

    private void initMouseListener(){
        this.addMouseListener(new MouseInputAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                if(SwingUtilities.isLeftMouseButton(mouseEvent)){
                    listener.componentSelected(drawer);
                }
            }
        });
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);

        for (Drawable drawable: drawer.toDrawable()){
            drawable.translate(new Point(component.getPosition()), this);
            drawable.draw((Graphics2D)(g));
        }
    }
}
