package controllers.editor;

import static org.junit.Assert.*;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import misc.utils.ClientTest;
import models.project.Diagram;
import models.project.Project;
import models.tikz.*;

import org.junit.Before;
import org.junit.Test;

import views.editor.CanvasView;
import views.editor.EditorView;
import views.editor.canvas.drawables.DrawableTikzComponent;
import views.editor.canvas.drawers.Drawer;

/**
 * Created by jhellinckx on 12/05/16.
 */
public class CanvasControllerTest extends ClientTest {

    private TikzGraph testedGraph;
    private CanvasView view;
    private CanvasController controller;
    private Project project;
    private Diagram diagram;
    private EditorView parentView;
    private boolean hasHead = true;

    @Before
    public void setUp() throws Exception {
        try {
            project = new Project();
            diagram = new Diagram("slt", project);
            parentView = new EditorView(diagram, false);
            testedGraph = diagram.getGraph();
            view = parentView.getCanvasView();
            controller = view.getController();
        } catch (HeadlessException e) {
            hasHead = false;
        }
    }

    private List<TikzComponent> drawablesToComponents(List<DrawableTikzComponent> drawables) {
        List<TikzComponent> components = new ArrayList<>();
        for (DrawableTikzComponent drawable : drawables) {
            components.add(drawable.getComponent());
        }
        return components;
    }

    @Test
    public void testUpdateDrawables() throws Exception {
        if (hasHead) {
            testedGraph.add(new TikzCircle());
            assertNotEquals(drawablesToComponents(controller.getDrawables()), testedGraph.getComponents());
            controller.updateDrawables();
            assertEquals(drawablesToComponents(controller.getDrawables()), testedGraph.getComponents());
        }
    }

    @Test
    public void testAddDrawableComponent() throws Exception {
        if (hasHead) {
            TikzComponent component = new TikzCircle();
            testedGraph.add((TikzNode) component);
            DrawableTikzComponent drawable = Drawer.toDrawable(component, view);
            controller.addDrawableComponent(drawable);
            assertEquals(drawablesToComponents(controller.getDrawables()), testedGraph.getComponents());
        }
    }

    @Test
    public void testDeleteItem() throws Exception {
        if (hasHead) {
            TikzNode node = new TikzCircle();
            testedGraph.add(node);
            controller.deleteItem(node);
            assertTrue(testedGraph.isEmpty());
        }
    }

    @Test
    public void testAddNodeToGraph() throws Exception {
        if (hasHead) {
            float testedX = 1993.0f;
            float testedY = 1991.0f;
            Point2D.Float testedPoint = new Point2D.Float(testedX, testedY);
            TikzNode node = new TikzCircle();
            assertFalse(testedGraph.contains(node));
            controller.addNodeToGraph(node, testedPoint);
            assertTrue(testedGraph.contains(node));
        }
    }

    @Test
    public void testAddEdgeToGraph() throws Exception {
        if (hasHead) {
            TikzNode firstNode = new TikzCircle();
            TikzNode secondNode = new TikzCircle();
            testedGraph.add(firstNode);
            testedGraph.add(secondNode);
            TikzEdge edge = new TikzDirectedEdge();
            assertFalse(testedGraph.contains(edge));
            controller.addEdgeToGraph(edge, firstNode, secondNode);
            assertTrue(testedGraph.contains(edge));
        }
    }

    @Test
    public void testAddGraph() throws Exception {
        if (hasHead) {
            TikzGraph graph = new TikzGraph();
            TikzNode firstNode = new TikzCircle();
            TikzNode secondNode = new TikzPolygon();
            graph.add(firstNode);
            graph.add(secondNode);
            controller.addGraph(graph, new Point2D.Float(0, 0));
            assertEquals(graph.getNodes(), testedGraph.getNodes());
        }
    }
}
