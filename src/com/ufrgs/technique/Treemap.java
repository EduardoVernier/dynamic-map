package com.ufrgs.technique;

import com.ufrgs.model.Entity;
import com.ufrgs.model.Point;
import com.ufrgs.model.Rectangle;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static java.lang.Double.max;
import static java.lang.Math.pow;

public class Treemap {

    public Treemap(Entity root, double width, double height) {
        treemapMultidimensional(root.getChildren(), new Rectangle(0, 0, width, height));
    }

    // Use recursion to compute single dimensional treemaps from a hierarchical dataset
    private void treemapMultidimensional(List<Entity> entityList, Rectangle rectangle) {

        // Sort using entities weight -- layout tends to turn out better
        entityList.sort(Comparator.comparing(Entity::getWeight).reversed());
        // Make a copy of data, as the original is destroyed during treemapSingledimensional computation
        List<Entity> entityCopy = new ArrayList<>();
        entityCopy.addAll(entityList);

        treemapSingledimensional(entityList, rectangle);

        // Recursive calls for the children
        for (Entity entity : entityCopy) {
            if (!entity.isLeaf()) {
                List<Entity> newEntityList = new ArrayList<>();
                newEntityList.addAll(entity.getChildren());
                treemapMultidimensional(newEntityList, entity.getRectangle());
            }
        }

        for (Entity entity : entityCopy) {
            Point r = entity.getPoint();
            System.out.println("ctx.rect(" + r.x + ", " + r.y + ", " + 1 + ", " + 1 + ");");
        }
    }

    private void treemapSingledimensional(List<Entity> entityList, Rectangle rectangle) {

        // Bruls' algorithm assumes that the data is normalized
        normalize(entityList, rectangle.width * rectangle.height);

        List<Entity> currentRow = new ArrayList<Entity>();
        squarify(entityList, currentRow, rectangle);
    }

    private void squarify(List<Entity> entityList, List<Entity> currentRow, Rectangle rectangle) {

        // If all elements have been placed, save coordinates into objects
        if (entityList.size() == 0) {
            saveCoordinates(currentRow, rectangle);
            return;
        }

        // Test if new element should be included in current row
        if (improvesRatio(currentRow, entityList.get(0).getNormalizedWeight(), rectangle.getShortEdge())) {
            currentRow.add(entityList.get(0));
            entityList.remove(0);
            squarify(entityList, currentRow, rectangle);
        } else {
            // New row must be created, subtract area of previous row from container
            double sum = 0;
            for (Entity entity : currentRow) {
                sum += entity.getNormalizedWeight();
            }

            Rectangle newRectangle = rectangle.cutArea(sum);
            saveCoordinates(currentRow, rectangle);
            List<Entity> newCurrentRow = new ArrayList<>();
            squarify(entityList, newCurrentRow, newRectangle);
        }
    }

    private void saveCoordinates(List<Entity> currentRow, Rectangle rectangle) {

        double normalizedSum = 0;
        for (Entity entity : currentRow) {
            normalizedSum += entity.getNormalizedWeight();
        }

        double subxOffset = rectangle.x, subyOffset = rectangle.y; // Offset within the container
        double areaWidth = normalizedSum / rectangle.height;
        double areaHeight = normalizedSum / rectangle.width;

        if (rectangle.width > rectangle.height) {
            for (Entity entity : currentRow) {
                double x = subxOffset;
                double y = subyOffset;
                double width = areaWidth;
                double height = entity.getNormalizedWeight() / areaWidth;
                entity.setRectangle(new Rectangle(x, y, width, height));
                subyOffset += entity.getNormalizedWeight() / areaWidth;
                // Save center as we'll be using it as input to the nmap algorithm
                entity.setPoint(new Point(x + width/2, y + height/2));
            }
        } else {
            for (Entity entity : currentRow) {
                double x = subxOffset;
                double y = subyOffset;
                double width = entity.getNormalizedWeight() / areaHeight;
                double height = areaHeight;
                entity.setRectangle(new Rectangle(x, y, width, height));
                subxOffset += entity.getNormalizedWeight() / areaHeight;
                // Save center as we'll be using it as input to the nmap algorithm
                entity.setPoint(new Point(x + width/2, y + height/2));
            }
        }
    }

    private void normalize(List<Entity> entityList, double area) {

        double sum = 0;
        for (Entity entity : entityList) {
            sum += entity.getWeight();
        }

        for (Entity entity : entityList) {
            entity.setNormalizedWeight(entity.getWeight() * (area / sum));
        }
    }

    // Test if adding a new entity to row improves ratios (get closer to 1)
    boolean improvesRatio(List<Entity> currentRow, double nextEntity, double length) {

        if (currentRow.size() == 0) {
            return true;
        }

        double minCurrent = Double.MAX_VALUE, maxCurrent = Double.MIN_VALUE;
        for (Entity entity : currentRow) {
            if (entity.getNormalizedWeight() > maxCurrent) {
                maxCurrent = entity.getNormalizedWeight();
            }

            if (entity.getNormalizedWeight() < minCurrent) {
                minCurrent = entity.getNormalizedWeight();
            }
        }

        double minNew = (nextEntity < minCurrent) ? nextEntity : minCurrent;
        double maxNew = (nextEntity > maxCurrent) ? nextEntity : maxCurrent;

        double sumCurrent = 0;
        for (Entity entity : currentRow) {
            sumCurrent += entity.getNormalizedWeight();
        }
        double sumNew = sumCurrent + nextEntity;

        double currentRatio = max(pow(length, 2) * maxCurrent / pow(sumCurrent, 2),
                pow(sumCurrent, 2) / (pow(length, 2) * minCurrent));
        double newRatio = max(pow(length, 2) * maxNew / pow(sumNew, 2),
                pow(sumNew, 2) / (pow(length, 2) * minNew));

        return currentRatio >= newRatio;
    }

}

