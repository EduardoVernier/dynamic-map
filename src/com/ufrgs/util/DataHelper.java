package com.ufrgs.util;

import com.ufrgs.model.Entity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class DataHelper {

    private static int numberOfRevisions;

    private static List<Entity> parseCSVs(String directory) {

        List<Entity> entityList = new ArrayList<>();

        File[] fileList = new File(directory).listFiles();
        List<String> fileNames = new ArrayList<>();

        if (fileList != null) {
            for (File file : fileList) {
                if (file.isFile()) {
                    try {
                        fileNames.add(file.getCanonicalPath());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            System.err.println("Invalid dir path.");
            System.exit(-1);
        }

        fileNames.sort(String::compareTo);
        fileNames.sort((o1, o2) -> Integer.valueOf(o1.length()).compareTo(Integer.valueOf(o2.length())));

        try {

            numberOfRevisions = fileNames.size();
            for (int revision = 0; revision < numberOfRevisions; ++revision) {

                BufferedReader bufferedReader = new BufferedReader(new FileReader(fileNames.get(revision)));
                String currentLine = bufferedReader.readLine();
                String[] header = currentLine.split(",");

                if (!header[0].equals("id") || !header[1].equals("weight")) {
                    System.err.println("Error parsing csv file");
                    System.exit(-1);
                }

                while ((currentLine = bufferedReader.readLine()) != null) {

                    String[] split = currentLine.split(",");

                    if (split.length != 2) {
                        System.err.println("Error parsing csv file");
                        System.exit(-1);
                    } else {
                        String id = split[0];
                        double weight = Double.parseDouble(split[1]);
                        if (contains(entityList, id)) {
                            Entity entity = entityList.get(find(entityList, id));
                            entity.setWeight(weight, revision);
                        } else {
                            Entity entity = new Entity(id, numberOfRevisions);
                            entity.setWeight(weight, revision);
                            entityList.add(entity);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return entityList;
    }

    public static Entity buildHierarchy(String csvFile) {

        List<Entity> entityList = parseCSVs(csvFile);

        List<Entity> auxList = new ArrayList<>();
        Entity root = new Entity("", numberOfRevisions);
        auxList.add(root);

        // Lexicographic sort
        entityList.sort(Comparator.comparing(Entity::getId));

        // Add children to direct parent
        for (int i = 0; i < entityList.size(); ++i) {
            Entity entity = entityList.get(i);
            int dividerIndex = entity.getId().lastIndexOf("/");

            if (dividerIndex != -1) {
                String prefix = entity.getId().substring(0, dividerIndex);

                if (contains(auxList, prefix)) {
                    // Add child to parent known parent
                    int parentIndex = find(auxList, prefix);
                    Entity parentEntity = auxList.get(parentIndex);
                    parentEntity.addChild(entity);
                    // Remove child from list
                    entityList.remove(i);
                    --i;
                } else {
                    // Initialize package with its first found child
                    Entity newParent = new Entity(prefix, numberOfRevisions);
                    newParent.addChild(entity);
                    // Remove child from list
                    entityList.remove(i);
                    --i;
                    auxList.add(newParent);
                }
            } else {
                // Add to root
                root.addChild(entity);
                entityList.remove(i);
                --i;
            }
        }

        // Make root carry full tree
        Collections.reverse(auxList);
        for (int i = 0; i < auxList.size(); ++i) {
            for (int j = i + 1; j < auxList.size(); ++j) {
                Entity entityA = auxList.get(i);
                Entity entityB = auxList.get(j);

                if (entityA.getId().contains(entityB.getId())) {
                    entityB.addChild(entityA);
                    auxList.remove(i);
                    i--;
                    break;
                }
            }
        }

        return root;
    }

    private static int find(List<Entity> entityList, String entityId) {

        for (int i = 0; i < entityList.size(); ++i) {
            if (entityList.get(i).getId().equals(entityId)){
                return i;
            }
        }
        return -1;
    }

    private static boolean contains(List<Entity> entityList, String prefix) {

        for (Entity entity : entityList) {
            if (entity.getId().equals(prefix)){
                return true;
            }
        }
        return false;
    }
}
