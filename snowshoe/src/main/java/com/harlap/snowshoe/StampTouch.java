package com.harlap.snowshoe;

import android.util.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * StampTouch represents the touch data from using a SnowShoe stamp.
 */
public class StampTouch {
    private final List<float[]> points = new ArrayList<float[]>(5);

    /**
     * getPoints returns a list of x,y point pairs containing all the points of the stamp.
     *
     * @return The list of points.
     */
    public List<float[]> getPoints() {
        return points;
    }

    /**
     * addPoint adds a single x,y coordinate pair to the touch coordinate set. Returns this so can be chained.
     * <p/>
     * Note that a StampTouch should have exactly 5 points, and a runtime exception will be thrown if more than 5 points are added.
     *
     * @param x The X coordinate of the touch point.
     * @param y The Y coordinate of the touch point.
     * @return This StampTouch so that invocations can be chained.
     * @throws RuntimeException
     */
    public StampTouch addPoint(Float x, Float y) {
        if (points.size() >= 5) {
            throw new RuntimeException("StampTouch can only have 5 points!");
        }

        points.add(new float[] {x, y});
        return this;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("StampTouch [");
        for (int i = 0; i < points.size(); i++) {
            float[] point = points.get(i);
            builder.append("[").append(point[0]).append(",").append(point[1]).append("]");
            if (points.size() > i + 1) builder.append(", ");
        }
        builder.append("]");
        return builder.toString();
    }
}
