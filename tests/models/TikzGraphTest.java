package models;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by jhellinckx on 01/03/16.
 */
public class TikzGraphTest {
    TikzGraph graph;

    @Before
    public void setUp() throws Exception {
        graph = new TikzGraph();
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testSize() throws Exception {
        for (int i = 0; i < 6; i++) {
            TikzNode node = new TikzCircle();
            graph.add(node);
            assertEquals(graph.size(), i+1);
        }
    }

    @Test
    public void testIterator() throws Exception {
        List<TikzNode> nodes = new ArrayList<TikzNode>();
        for (int i = 0; i < 5; i++) {
            TikzNode circle = new TikzCircle();
            nodes.add(circle);
            graph.add(circle);
        }
        Iterator<TikzNode> it = graph.iterator();
        while(it.hasNext()){
            TikzNode next = it.next();
            assertTrue(nodes.contains(next));
            nodes.remove(next);
        }
    }

    @Test
    public void testAddWithoutEdge() throws Exception {
        TikzNode key = new TikzCircle();
        assertTrue(graph.add(key));
        assertFalse(graph.add(key));
        assertEquals(graph.get(key), null);
    }

    @Test
    public void testAddAllEdges() throws Exception {
        TikzNode firstNode = new TikzRectangle();
        TikzNode secondNode = new TikzCircle();
        List<TikzEdge> edges = new ArrayList<TikzEdge>();
        for (int i = 0; i < 5; i++) {
            edges.add(new TikzUndirectedEdge(firstNode, secondNode));
        }

        graph.addAll(firstNode, edges);
        assertEquals(edges.toArray(), graph.get(firstNode).toArray());

    }

    @Test
    public void testAddWithEdge() throws Exception {

    }

    @Test
    public void testGetAll() throws Exception {

    }

    @Test
    public void testGetNodes() throws Exception {

    }

    @Test
    public void testGet() throws Exception {

    }

    @Test
    public void testRemove() throws Exception {

    }

    @Test
    public void testRemove1() throws Exception {
        TikzNode node = new TikzRectangle();
        List<TikzEdge> edges = new ArrayList<TikzEdge>();
        for (int i = 0; i < 5; i++) {
            TikzNode node1 = new TikzRectangle();
            TikzNode node2 = new TikzCircle();
            edges.add(new TikzDirectedEdge(node1,node2));
        }
        graph.addAll(node, edges);
        for (int i = edges.size()-1; i >= 0 ; i--) {
            graph.remove(node, i);
            edges.remove(i);
            assertArrayEquals(graph.get(node).toArray(), edges.toArray());
        }
    }

    @Test
    public void testRemove2() throws Exception {
        TikzNode node = new TikzRectangle();
        List<TikzEdge> edges = new ArrayList<TikzEdge>();
        for (int i = 0; i < 5; i++) {
            TikzNode node1 = new TikzRectangle();
            TikzNode node2 = new TikzCircle();
            edges.add(new TikzDirectedEdge(node1,node2));
        }
        graph.addAll(node, edges);
        for (int i = edges.size()-1; i >= 0 ; i--) {
            graph.remove(node, edges.get(i));
            edges.remove(i);
            assertArrayEquals(graph.get(node).toArray(), edges.toArray());
        }
    }
}