package views.editor;

import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.io.File;

import javax.swing.*;

import misc.CanvasSelection;
import misc.drag.handler.CanvasTransferHandler;
import misc.drag.transfereddata.TransferTikz;
import models.tikz.TikzComponent;
import models.tikz.TikzGraph;
import views.editor.canvas.drawables.DrawableTikzComponent;
import views.editor.canvas.popup.PopupMenuView;
import views.editor.canvas.popup.SelectionPopupMenuView;
import controllers.editor.CanvasController;

/**
 * Implementation of the View (from the MVC architectural pattern) for the
 * Canvas. The Canvas is the area of the GUI where the graph is painted.
 */
public class CanvasView extends JPanel {
    private final EditorView parentView;
    private final TikzGraph graph;

    private final CanvasController controller;
    private boolean isFocused;
    private PopupMenuView popupMenu;
    private SelectionPopupMenuView selectionPopupMenu;
    private CanvasSelection selection;

    /**
     * Constructs a new View for the Canvas, with a given TikzGraph and
     * parentView
     *
     * @param parentView
     *            The view which contains this view (ie. EditorView)
     * @param graph
     *            The TikzGraph
     */
    public CanvasView(final EditorView parentView, final TikzGraph graph) {
        this.parentView = parentView;
        this.graph = graph;
        this.controller = new CanvasController(this, graph);
        this.isFocused = isFocusOwner();
        this.popupMenu = new PopupMenuView(controller);
        this.selectionPopupMenu = new SelectionPopupMenuView(controller);
        this.selection = null;
        this.render();
        this.addListeners();
        this.enableDrag();
        this.setVisible(true);
    }

    /**
     * Renders the view by putting it on focus
     */
    private void render() {
        setFocusable(true);
    }

    public CanvasController getController() {
        return controller;
    }

    private void highlightTextArea() {
        if (selection != null) {
            parentView.highlightTextZone(controller.getSelectedComponents(selection.getSelectionRectangle()));
        }
    }

    private void addMouseListener() {
        this.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(final MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    requestFocusInWindow();
                    CanvasView view = (CanvasView) e.getSource();
                    TikzComponent component = view.getSelectedComponent();
                    if ((selection != null) && selection.contains(new Point2D.Float(e.getX(), e.getY()))) {
                        selectionPopupMenu.show(view, e.getX(), e.getY());
                    } else if (component != null) {
                        popupMenu.show(view, e.getX(), e.getY());
                        popupMenu.setComponent(component);
                    }
                } else {
                    parentView.highlightTextLine(controller.findComponentByPosition(new Point2D.Float(e.getX(), e.getY())));
                    CanvasView view = (CanvasView) e.getSource();
                    controller.unselectComponents();
                    view.resetSelection();
                    if (controller.hasComponentAtPosition(new Point2D.Float(e.getX(), e.getY()))) {
                        TransferHandler handler = view.getTransferHandler();
                        handler.exportAsDrag(view, e, TransferHandler.MOVE);
                    }
                    controller.mousePressed(e, parentView.getSelectedTool());
                }
            }

            @Override
            public void mouseReleased(final MouseEvent e) {
                highlightTextArea();
            }
        });
    }

    private void addMouseMotionListener() {
        this.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(final MouseEvent e) {
                CanvasView view = (CanvasView) e.getSource();
                view.resizeSelection(new Point2D.Float(e.getX(), e.getY()));
                view.repaint();
            }
        });
    }

    private void addFocusListener() {
        this.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(final FocusEvent focusEvent) {
                controller.focusGained();
            }

            @Override
            public void focusLost(final FocusEvent focusEvent) {
                controller.focusLost();
            }
        });
    }

    /**
     * Adds listeners for mouse events to the view
     */
    private void addListeners() {
        addMouseListener();
        addFocusListener();
        addMouseMotionListener();
    }

    private void enableDrag() {
        this.setTransferHandler(new CanvasTransferHandler());
    }

    /**
     * Paints the components composing the graph
     *
     * @param g
     *            Graphics to be drawn
     */
    public void paintComponent(final Graphics g) {
        super.paintComponent(g);
        controller.updateDrawables();
        for (DrawableTikzComponent drawable : controller.getDrawables()) {
            drawable.draw((Graphics2D) g);
        }

        if (!getIsFocused()) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            Stroke old_stroke = g2d.getStroke();
            Color old_color = g2d.getColor();
            g2d.setStroke(new BasicStroke(0));
            g2d.setColor(new Color(0, 0, 0, 64));
            Shape s = new Rectangle(this.getWidth(), this.getHeight());
            g2d.fill(s);
            g2d.setColor(Color.white);
            g2d.draw(s);
            g2d.setColor(old_color);
            g2d.setStroke(old_stroke);
        }
    }

    /**
     * Returns true if this view is focused
     *
     * @return true if this view is focused
     */
    public boolean getIsFocused() {
        return isFocused;
    }

    /**
     * Sets whether the view is focused or not
     *
     * @param isFocused
     *            The boolean to set whether the view is focused or not
     */
    public void setIsFocused(final boolean isFocused) {
        this.isFocused = isFocused;
    }

    public TikzComponent getSelectedComponent() {
        return controller.findComponentByPosition(new Point2D.Float(getMousePosition().x, getMousePosition().y));
    }

    /**
     * Informs the controller where the component being dragged and dropped has
     * been dropped on the canvas
     *
     * @param transfer_data
     *            The selected component being dragged and dropped
     * @param location
     *            The location where the component has been dropped
     */

    public void dragEvent(final TransferTikz transfer_data, final Point2D.Float location) {
        this.requestFocus();
        controller.mouseDropped(transfer_data, location);
    }

    public void resetTool() {
        parentView.resetTool();
    }

    /**
     * Resize the selection rectangle displayed during drag process.
     *
     * @param bottom_right
     *            The bottom right corner of the selection rectangle.
     */

    public void resizeSelection(final Point2D.Float bottom_right) {
        if (selection == null) {
            selection = new CanvasSelection(bottom_right);
            this.add(selection);
        } else {
            selection.resize(bottom_right);
        }
        selection.repaint();
    }

    /**
     * Reset the selection rectangle to null.
     */

    public void resetSelection() {
        if (selection != null) {
            this.remove(selection);
            this.selection = null;
            parentView.removeHighlights();
            this.repaint();
        }
    }

    public CanvasSelection getSelection() {
        return selection;
    }

    public void addTemplateToParentView(final File file) {
        parentView.addTemplateToToolBox(file);
    }
}
