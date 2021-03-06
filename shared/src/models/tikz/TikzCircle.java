package models.tikz;

import parser.TikzFormatter;
import constants.Models;

/**
 * Implementation of the Circle Model (from the MVC architectural pattern) This
 * class represents a circle that is linked to a tikz code
 */
public class TikzCircle extends TikzShape {
    private float radius;

    /**
     * Constructs a default tikz circle
     */
    public TikzCircle() {
        super();
        radius = Models.DEFAULT.LENGTH;
    }

    /**
     * Constructs a tikz circle with a given radius
     *
     * @param radius
     *            The radius of the circle
     */
    public TikzCircle(float radius) {
        super();
        this.radius = radius;
    }

    /**
     * Constructs a tikz circle by copying an other tikz circle
     *
     * @param o_circle
     *            The tikz circle to be copied from
     */
    public TikzCircle(TikzCircle o_circle) {
        super(o_circle);
        this.radius = o_circle.getRadius();
    }

    /**
     * Constructs a default tikz circle with a given reference
     *
     * @param reference
     *            the reference
     */
    public TikzCircle(String reference) {
        super(reference);
        radius = Models.DEFAULT.LENGTH;
    }

    /**
     * Constructs a tikz circle with a given radius and reference
     *
     * @param radius
     *            The radius of the circle
     * @param reference
     *            the reference
     */
    public TikzCircle(float radius, String reference) {
        super(reference);
        this.radius = radius;
    }

    /**
     * Constructs a tikz circle by copying an other tikz circle with a given
     * reference
     *
     * @param o_circle
     *            The tikz circle to be copied from
     * @param reference
     *            the reference
     */
    public TikzCircle(TikzCircle o_circle, String reference) {
        super(o_circle, reference);
        this.radius = o_circle.getRadius();
    }

    /**
     * Getter for the radius of the tikz circle
     *
     * @return The radius
     */
    public float getRadius() {
        return radius;
    }

    /**
     * Setter for the radius of the tikz circle
     *
     * @param radius
     *            the radius
     */
    public void setRadius(float radius) {
        this.radius = radius;
    }

    /**
     * Getter for a clone (ie. copy of the current tikz circle)
     *
     * @return A new tikz circle that is the copy of the current tikz circle
     */
    @Override
    public TikzCircle getClone() {
        return new TikzCircle(this);
    }

    @Override
    public String toString() {
        return TikzFormatter.format(this);
    }

    @Override
    public boolean isCircle() {
        return true;
    }

}
