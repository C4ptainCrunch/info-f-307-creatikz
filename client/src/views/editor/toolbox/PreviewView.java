package views.editor.toolbox;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;
import javax.swing.border.EtchedBorder;

import misc.drag.handler.PreviewTransferHandler;
import models.ToolModel;
import models.tikz.TikzComponent;
import models.tikz.TikzGraph;
import views.editor.canvas.drawables.DrawableTikzComponent;
import views.editor.canvas.drawers.Drawer;
import controllers.editor.toolbox.PreviewController;

/**
 * Implementation of the View (from the MVC architectural pattern) for the
 * Preview. The Preview is part of the toolbox used to show a preview of the
 * component being edited.
 */
public class PreviewView extends JPanel {
    TikzComponent component;
    PreviewController controller;

    /**
     * Constructs a new view for the Preview with a given ToolModel
     *
     * @param model
     *            the tool model
     */
    public PreviewView(ToolModel model) {
        this.setBackground(Color.WHITE);
        this.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
        this.controller = new PreviewController(this, model);
        enableDrag();
    }

    /**
     * Enables the drag&drop action for the preview component
     */
    private void enableDrag() {
        this.setTransferHandler(new PreviewTransferHandler());
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                if (SwingUtilities.isLeftMouseButton(mouseEvent)) {
                    PreviewView view = (PreviewView) mouseEvent.getSource();
                    TransferHandler handler = view.getTransferHandler();
                    handler.exportAsDrag(view, mouseEvent, TransferHandler.MOVE);
                }
            }
        });
    }

    /**
     * Getter for the component being previewed
     *
     * @return The component being previewed
     */
    public TikzGraph getComponentAsGraph() {
        return this.controller.getModelAsGraph();
    }

    public void reset() {
        controller.resetModel();
    }

    /**
     * Setter for the component being previewed
     *
     * @param component
     *            the component to be set for the preview
     */
    public void setComponent(TikzComponent component) {
        this.component = component;
    }

    /**
     * Paints the component being previewed
     *
     * @param g
     *            The Graphics to be painted
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (component != null) {
            DrawableTikzComponent drawable = Drawer.toDrawable(component, this);
            drawable.draw((Graphics2D) g);
        }
    }
}
