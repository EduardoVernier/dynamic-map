package com.ufrgs.model;

import com.ufrgs.view.Colormap;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class Entity {

    private String id;
    private List<Double> weightList;
    public List<Double> distanceList;
    private Point movingPoint, anchorPoint;
    private Rectangle rectangle, pastRectangle;
    private List<Entity> children;

    public Entity(String id, int numberOfRevisions) {

        this.id = id;
        // Initialize lists
        children = new ArrayList<>();
        weightList = new ArrayList<>(numberOfRevisions);
        distanceList = new ArrayList<>(numberOfRevisions);
        for (int i = 0; i < numberOfRevisions; ++i) {
            weightList.add(0.0);
            distanceList.add(0.0);
        }
    }

    public String getId() {
        return id;
    }

    public int getNumberOfRevisions() {
        return weightList.size();
    }

    public double getWeight(int index) {
        return weightList.get(index);
    }

    public void setWeight(double weight, int revision) {
        weightList.set(revision, weight);
    }

    public double getMaximumWeight() {
        return weightList.stream().max(Double::compare).get();
    }

    public Point getAnchorPoint() {
        return anchorPoint;
    }

    public void setAnchorPoint(double x, double y) {
        anchorPoint.setValues(x, y);
    }

    public Point getMovingPoint() {
        return movingPoint;
    }

    public void initPoint(Point point) {
        anchorPoint = new Point(point.x, point.y);
        movingPoint = new Point(point.x, point.y);
    }

    public void setMovingPoint(double x, double y) {
        //pastPoint.setValues(point.x, point.y);
        movingPoint.setValues(x, y);
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    public Rectangle getPastRectangle() {
        return pastRectangle;
    }

    public float getAspectRatio() {

        return (float) Double.min(rectangle.width / rectangle.height,
                rectangle.height / rectangle.width);
    }

    public void setRectangle(Rectangle newRectangle, int revision) {

        if (this.rectangle == null) {
            pastRectangle = newRectangle;
        } else {
            pastRectangle = this.rectangle;
        }
        this.rectangle = newRectangle;
        // Compute metric
        if (newRectangle != null) {
            double pastCenterX = (pastRectangle.x + pastRectangle.width)/2;
            double currentCenterX = (rectangle.x + rectangle.width)/2;
            double pastCenterY = (pastRectangle.y + pastRectangle.height)/2;
            double currentCenterY = (rectangle.y + rectangle.height)/2;
            double distance = sqrt(pow(pastCenterX - currentCenterX, 2) + pow(pastCenterY - currentCenterY, 2));
            this.distanceList.set(revision, distance);
        }
    }

    public void addChild(Entity entity) {
        children.add(entity);
    }

    public List<Entity> getChildren() {
        return children;
    }

    public boolean isLeaf() {
        return children.size() == 0;
    }

    public void draw(Graphics2D graphics, double progress) {

        if (rectangle == null  && pastRectangle == null) {
            return;
        }

        double x = rectangle.x * progress + pastRectangle.x * (1 - progress);
        double y = rectangle.y * progress + pastRectangle.y * (1 - progress);
        double width = rectangle.width * progress + pastRectangle.width * (1 - progress);
        double height = rectangle.height * progress + pastRectangle.height * (1 - progress);

        graphics.setColor(Colormap.sequentialColormap(1 - getAspectRatio()));
        graphics.fill(new Rectangle2D.Double(x, y, width, height));
    }

    public void drawBorder(Graphics2D graphics, double progress) {

        if (rectangle == null  && pastRectangle == null) {
            return;
        }

        double x = rectangle.x * progress + pastRectangle.x * (1 - progress);
        double y = rectangle.y * progress + pastRectangle.y * (1 - progress);
        double width = rectangle.width * progress + pastRectangle.width * (1 - progress);
        double height = rectangle.height * progress + pastRectangle.height * (1 - progress);

        if (isLeaf()) {
            graphics.setColor(new Color(0,0,0,30));
            graphics.setStroke(new BasicStroke(1, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER));
            graphics.draw(new Rectangle2D.Double(x, y, width, height));
        } else {
            graphics.setColor(Color.white);
            graphics.setStroke(new BasicStroke(4, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER));
            graphics.draw(new Rectangle2D.Double(x, y, width, height));
        }
    }

    public void drawLabel(Graphics2D graphics, double progress, double scale) {

        if (rectangle == null  && pastRectangle == null) {
            return;
        }

        if (rectangle.height > 20) {
            graphics.setPaint(Color.BLACK);
            String split[] = getId().split("/");
            String id = split[split.length - 1];

            int x = (int) (rectangle.x * progress + pastRectangle.x * (1 - progress)) + 4;
            int y = (int) (rectangle.y * progress + pastRectangle.y * (1 - progress)) + 20;
            int width = (int) (rectangle.width * progress + pastRectangle.width * (1 - progress)) - 5;
            while (id.length() > 0 && graphics.getFontMetrics().stringWidth(id) > width) {
                id = id.substring(0, id.length() > 2 ? id.length() - 2 : 0);
            }

            graphics.drawString(id, x, y);
        }
    }

    @Override
    public String toString() {
        return "Entity{" +
                "id='" + id + '\'' +
                ", rectangle=" + rectangle + '}';
    }

    public List<Double> getWeightList() {
        return weightList;
    }
}
