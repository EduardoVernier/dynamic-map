package com.ufrgs.technique;

import com.ufrgs.model.Entity;
import com.ufrgs.model.Rectangle;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Double.max;
import static java.lang.Math.pow;

public class Treemap {

    private List<Entity> entityCopy = new ArrayList<>();


    public Treemap(Entity root, double width, double height) {

        treemapMultidimensional(root.getChildren().get(0).getChildren(), 0, 0, width, height);
    }


    private void treemapMultidimensional(List<Entity> entityList, double x, double y, double width, double height) {



        entityCopy.addAll(entityList);

        treemapSingledimensional(entityList, x, y, width, height);

        for (Entity entity : entityCopy) {
            Rectangle r = entity.getRectangle();
            System.out.println("ctx.rect(" + r.x + ", " + r.y + ", " + (r.width - r.x) + ", " + (r.height - r.y) + ");");
        }
    }

    private void treemapSingledimensional(List<Entity> entityList, double x, double y, double width, double height) {

        normalize(entityList, width * height);

        List<Entity> currentRow = new ArrayList<Entity>();
        Rectangle rectangle = new Rectangle(x, y, width, height);
        squarify(entityList, currentRow, rectangle);
    }

    private void squarify(List<Entity> entityList, List<Entity> currentRow, Rectangle rectangle) {

        if (entityList.size() == 0) {
            saveCoordinates(currentRow, rectangle);
            return;
        }

        if (improvesRatio(currentRow, entityList.get(0).getNormalizedWeight(), rectangle.getShortEdge())) {
            currentRow.add(entityList.get(0));
            entityList.remove(0);
            squarify(entityList, currentRow, rectangle);
        } else {

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
                double width = subxOffset + areaWidth;
                double height = subyOffset + entity.getNormalizedWeight() / areaWidth;
                entity.setRectangle(new Rectangle(x, y, width, height));
                subyOffset += entity.getNormalizedWeight() / areaWidth;
            }
        } else {
            for (Entity entity : currentRow) {
                double x = subxOffset;
                double y = subyOffset;
                double width = subxOffset + entity.getNormalizedWeight() / areaHeight;
                double height = subyOffset + areaHeight;
                entity.setRectangle(new Rectangle(x, y, width, height));
                subxOffset += entity.getNormalizedWeight() / areaHeight;
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

