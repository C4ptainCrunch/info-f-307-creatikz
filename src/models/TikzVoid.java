package models;

public class TikzVoid extends TikzNode{
    public TikzVoid(){
        super();
    }

    @Override
    public String toString() {
        String options = String.join(", ", new String[]{}); //TODO: do this
        return String.format("\\node[%s](%s) at (%.0f,%.0f){%s}", options, "", getPosition().getX(), getPosition().getY(), getLabel());
    }
}
