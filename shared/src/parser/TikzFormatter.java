package parser;

import constants.Models;
import models.tikz.*;
import java.awt.*;
import java.util.ArrayList;

public class TikzFormatter {
    private TikzFormatter(){}

    public static String tikzSource(TikzNode node, String options){
        Point position = node.getPosition();
        return String.format("\\node[%s](%s) at (%.0f,%.0f){%s};\n", options, node.getReference(), position.getX(), position.getY(), node.getLabel());
    }

    public static String tikzSource(TikzEdge edge, String options){
        String first = edge.getFirstNode().getReference();
        String second = edge.getSecondNode().getReference();
        return String.format("\\draw[%s] (%s) -- (%s);\n", options, first, second);
    }

    public static String format(TikzComponent c){
        return "";
    }

    public static String format(TikzCircle circle) {
        String radiusOption = circle.getRadius() == Models.DEFAULT.LENGTH ? "" : ", radius="+Integer.toString(circle.getRadius());
        return tikzSource(circle, String.join(", ",  "circle", "draw" + radiusOption, getCommonOptions(circle), getShapeOptions(circle)));
    }

    public static String format(TikzRectangle rectangle){
        String widthOption = rectangle.getWidth() == Models.DEFAULT.LENGTH ? "" : ", minimum width=" + Integer.toString(rectangle.getWidth());
        String heightOption = rectangle.getLength() == Models.DEFAULT.LENGTH ? "" : ", minimum height=" + Integer.toString(rectangle.getLength());
        return tikzSource(rectangle, String.join(", ", "rectangle", "draw" + widthOption + heightOption, getCommonOptions(rectangle), getShapeOptions(rectangle)));
    }

    public static String format(TikzPolygon polygon){
        StringBuilder options = new StringBuilder("regular polygon, draw");
        options.append(polygon.getLength() == Models.DEFAULT.LENGTH ? "" : ", minimum size=" + Integer.toString(polygon.getLength()));
        options.append(polygon.getSides() == Models.DEFAULT.SIDES ? "" : ", regular polygon sides=" + Integer.toString(polygon.getSides()));
        String commonOptions = getCommonOptions(polygon);
        options.append(commonOptions.isEmpty() ? "" :  ", " + commonOptions);
        String shapeOptions = getShapeOptions(polygon);
        options.append(shapeOptions.isEmpty() ? "" : ", " + shapeOptions);
        return tikzSource(polygon, options.toString());
    }

    public static String format(TikzDirectedEdge edge){
        String commonOptions = getCommonOptions(edge);
        return tikzSource(edge, commonOptions.isEmpty() ? "->" : String.join(", ",  "->", commonOptions));
    }

    public static String format(TikzUndirectedEdge edge){
        String commonOptions = getCommonOptions(edge);
        return tikzSource(edge, commonOptions.isEmpty() ? "" : String.join(", ", commonOptions));
    }

    public static String format(TikzVoid tikzVoid){
        return "";
    }

    public static String format(TikzText text){
        throw new RuntimeException("Not implemented");
    }

    private static String getCommonOptions(TikzComponent comp) {
        ArrayList<String> options = new ArrayList<>();
        if (comp.getStrokeColor() != Models.DEFAULT.COLOR) {options.add("color=" + TikzColors.ColorToString(comp.getStrokeColor()));}
        if (comp.getStroke() != Models.DEFAULT.STROKE) {options.add("line width=" + Integer.toString(comp.getStroke()));}
        return String.join(", ", options);
    }

    private static String getShapeOptions(TikzShape shape){
        return shape.getBackgroundColor() == Models.DEFAULT.BACKGROUND_COLOR ? "" : String.join(", ", "fill=" + TikzColors.ColorToString(shape.getBackgroundColor()));
    }
}

