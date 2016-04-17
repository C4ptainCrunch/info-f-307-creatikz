package models.tikz;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import parser.NodeParser;

public class TikzGraph extends Observable implements Iterable<TikzNode> {
    private List<TikzEdge> edges = new Vector<>();
    private List<TikzNode> nodes = new Vector<>();

    public TikzGraph() {
    }

    public TikzGraph(String filePath) throws IOException {
        String stringGraph = new String(Files.readAllBytes(Paths.get(filePath)));
        NodeParser.parseDocument(this).parse(stringGraph);
    }

    public int size() {
        return nodes.size();
    }

    public Iterator<TikzNode> iterator() {
        return nodes.iterator();
    }

    public void replace(TikzGraph other) {
        this.edges = other.edges;
        this.nodes = other.nodes;
        setChanged();
        notifyObservers();
    }

    /**
     * @param node
     *            key to map to empty TIkzEdge vector.
     * @return true if no key is replaced, else false.
     */
    public void add(TikzNode node) {
        if (!this.nodes.contains(node)) {
            nodes.add(node);
            setChanged();
        }
        notifyObservers();
    }

    public void add(TikzEdge edge) {
        this.add(edge.getFirstNode());
        this.add(edge.getSecondNode());
        if (!this.edges.contains(edge)) {
            this.edges.add(edge);
            setChanged();
        }
        notifyObservers();
    }

    public void add(TikzGraph graph){
        this.addAllNodes(graph.getNodes());
        this.addAllEdges(graph.getEdges());
        notifyObservers();
    }


    /**
     * @param edges
     *            Vector of TikzEdge to append
     */
    public void addAllEdges(Collection<TikzEdge> edges) {
        for (TikzEdge edge : edges) {
            this.add(edge);
        }
        notifyObservers();
    }

    public void addAllNodes(Collection<TikzNode> nodes) {
        for (TikzNode node : nodes) {
            this.add(node);
        }
        notifyObservers();
    }

    public List<TikzNode> getNodes() {
        return Collections.unmodifiableList(this.nodes);
    }

    public List<TikzEdge> getEdges() {
        return Collections.unmodifiableList(this.edges);
    }

    public List<TikzEdge> get(TikzNode node) {
        List<TikzEdge> edges = new ArrayList<>();
        for (TikzEdge edge : this.edges) {
            if (node == edge.getFirstNode() || node == edge.getSecondNode()) {
                edges.add(edge);
            }
        }
        return edges;
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();
        for (TikzNode node : getNodes()) {
            res.append(node);
            res.append(";\n");
        }
        res.append('\n');
        for (TikzEdge edge : this.getEdges()) {
            res.append(edge);
            res.append(";\n");
        }
        return res.toString();
    }

    public void remove(TikzEdge edge) {
        this.edges.remove(edge);
    }

    public List<TikzEdge> remove(TikzNode node) {
        ArrayList<TikzEdge> edges = new ArrayList<>();
        for (int i = 0; i < this.edges.size(); i++) {
            TikzEdge edge = this.edges.get(i);
            if (node == edge.getFirstNode() || node == edge.getSecondNode()) {
                edges.add(edge);
                this.edges.remove(i);
                i--;
            }
        }
        nodes.remove(node);
        return edges;
    }
}