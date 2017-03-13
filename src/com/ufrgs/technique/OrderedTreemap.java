package com.ufrgs.technique;

import com.ufrgs.model.Entity;
import com.ufrgs.model.Rectangle;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.min;

public class OrderedTreemap {

    private Entity root;
    private int revision;
    private double normalizer;

    public OrderedTreemap(Entity root, Rectangle rectangle, int revision) {

        this.root = root;
        this.revision = revision;

        root.setRectangle(rectangle, revision);

        treemapMultidimensional(root.getChildren(), rectangle);
    }

    private void treemapMultidimensional(List<Entity> entityList, Rectangle rectangle) {

        treemapSingledimensional(entityList, rectangle);
    }

    private void treemapSingledimensional(List<Entity> entityList, Rectangle rectangle) {

        entityList.removeIf(entity -> entity.getWeight(revision) == 0.0);
        normalize(entityList, rectangle.width * rectangle.height);

        // Pivot-by-middle
        if (entityList.size() == 0) {

        } else if (entityList.size() == 1) {

        } else if (entityList.size() == 2) {

        } else if (entityList.size() == 3) {

        } else {

            int pivotIndex = entityList.size()/2;
            Rectangle rMinusR1 = null;

            if (rectangle.width > rectangle.height) {

                List<Entity> l1 = entityList.subList(0, pivotIndex - 1);

                double totalWeight = 0;
                for (Entity entity : entityList) {
                    totalWeight += entity.getWeight(revision);
                }

                double l1Weight = 0;
                for (Entity entity : l1) {
                    l1Weight += entity.getWeight(revision);
                }

                double r1Width = (l1Weight / totalWeight) * rectangle.width;
                rMinusR1 = new Rectangle(rectangle.x + r1Width, rectangle.y, rectangle.width - r1Width, rectangle.height);
            }

            Entity pivot = entityList.get(pivotIndex);
            List<Entity> l2 = new ArrayList<>();
            List<Entity> l3 = entityList.subList(pivotIndex + 1, entityList.size());

            double pInitialWidth = getNormalizedWeight(pivot) / rMinusR1.height;
            double pInitialHeight = rMinusR1.height;
            double pPreviousAspectRatio = min(pInitialWidth/pInitialHeight, pInitialHeight/pInitialWidth);

            double pNewWidth = 0, pNewHeight = 0, pNewAspectRatio;
            while (l3.size() > 2) {

                l2.add(0, l3.get(0));
                l3.remove(0);

                double l2Weight = 0;
                for (Entity entity : l2) {
                    l2Weight += getNormalizedWeight(entity);
                }

                if (rectangle.width > rectangle.height) {

                    pNewWidth = (getNormalizedWeight(pivot) + l2Weight) / rMinusR1.height;
                    pNewHeight = (getNormalizedWeight(pivot)/ (getNormalizedWeight(pivot) + l2Weight)) * rMinusR1.height;
                    pNewAspectRatio = min(pNewWidth/pNewHeight, pNewHeight/pNewWidth);

                    if (pNewAspectRatio < pPreviousAspectRatio) {
                        l3.add(0, l2.get(l2.size()));
                        break;
                    }
                } else {

                    pNewHeight = (getNormalizedWeight(pivot) + l2Weight) / rMinusR1.width;
                    pNewWidth = (getNormalizedWeight(pivot)/ (getNormalizedWeight(pivot) + l2Weight)) * rMinusR1.width;
                    pNewAspectRatio = min(pNewWidth/pNewHeight, pNewHeight/pNewWidth);

                    if (pNewAspectRatio < pPreviousAspectRatio) {
                        l3.add(0, l2.get(l2.size()));
                        break;
                    }
                }
            }

            if (rectangle.width > rectangle.height) {
                treemapSingledimensional(l2,
                        new Rectangle(rMinusR1.x, rMinusR1.y + pNewHeight, pNewWidth, rMinusR1.height - pNewHeight));
                treemapSingledimensional(l3,
                        new Rectangle(rMinusR1.x + pNewWidth, rMinusR1.y, rMinusR1.width - pNewWidth, rMinusR1.height));
            } else {
                treemapSingledimensional(l2,
                        new Rectangle(rMinusR1.x + pNewWidth, rMinusR1.y, rMinusR1.width - pNewWidth, pNewHeight));
                treemapSingledimensional(l3,
                        new Rectangle(rMinusR1.x, rMinusR1.y + pNewHeight, rMinusR1.width, rMinusR1.height - pNewHeight));
            }
        }

    }


    private void normalize(List<Entity> entityList, double area) {

        double sum = 0;
        for (Entity entity : entityList) {
            sum += entity.getWeight(revision);
        }
        normalizer = area / sum;
    }

    private double getNormalizedWeight(Entity entity) {
        return entity.getWeight(revision) * normalizer;
    }

}
