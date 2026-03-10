package interpreter.interpreterHelperClasses;

import interpreter.interpretedObjects.blocks.InterpretedBlock;
import interpreter.interpretedObjects.blocks.InterpretedOre;
import interpreter.interpretedObjects.creativeTabs.InterpretedCreativeTab;
import interpreter.interpretedObjects.items.InterpretedItem;
import interpreter.interpretedObjects.items.special.*;
import interpreter.interpretedObjects.recipes.InterpretedRecipe;
import interpreter.interpretedObjects.toolTiers.InterpretedToolTier;

import java.io.File;
import java.util.ArrayList;
import java.util.function.Consumer;

import static interpreter.RegistryInterpreter.*;
import static interpreter.RegistryInterpreterHelperMethods.*;
import static interpreter.interpreterHelperClasses.FileIO.getContentFromFileAsList;

public class InterpretedObjectGetters {
    public static ArrayList<InterpretedBlock> getAllBlocks() {
        return null;
    }

    public static ArrayList<InterpretedOre> getAllOres() {
        return null;
    }

    public static ArrayList<String> getDifferentGroupTypes(ArrayList<String> text) {
        return (ArrayList<String>) text.stream().filter(s -> s.contains("{")).distinct().toList();
    }

    public static ArrayList<ArrayList<String>> getGroupedText(ArrayList<String> fileText) {
        ArrayList<String> backup = fileText;
        ArrayList<ArrayList<String>> interpretedText = new ArrayList<>();
        ArrayList<String> differentItems = getDifferentGroupTypes(fileText);
        for (int i = 0, j = 0; !fileText.isEmpty() && j < differentItems.size(); i++) {
            int finalJ = j;
            if (fileText.stream().dropWhile(s -> !s.toUpperCase().contains(differentItems.get(finalJ).toUpperCase())).toList().isEmpty()) { //geht bis z.B. {simple, und mocht a sublist bis zum Ende, wenn de leer is muss des nächste z.B. {complex abgeprüft werden
                j++;
                fileText = backup;
            }
            interpretedText.add(new ArrayList<>());
            fileText = (ArrayList<String>) fileText.stream()
                    .dropWhile(s -> !s.toUpperCase().contains(differentItems.get(finalJ).toUpperCase()))
                    .toList();
            interpretedText.set(i, (ArrayList<String>) fileText.stream()
                    .takeWhile(s -> !s.contains("}"))
                    .toList());
            interpretedText.get(i).addFirst(differentItems.get(j));
            fileText = (ArrayList<String>) fileText.stream()
                    .dropWhile(s -> !s.contains("}"))
                    .toList();
        }
        return (ArrayList<ArrayList<String>>) interpretedText.stream().filter(strings -> !strings.subList(1, strings.size()).isEmpty()).toList();
    }

    public static ArrayList<String> getListFromBrackets(int startingIndex, ArrayList<String> list) {
        list = (ArrayList<String>) list.subList((list.get(startingIndex).contains("]") ? startingIndex + 2 : (list.get(startingIndex).contains("[") ? startingIndex + 1 : startingIndex)), list.size());
        return (ArrayList<String>) list.stream().takeWhile(s -> !s.contains("]")).toList();
    }

    public static ArrayList<Object> getInterpretedObjects(File williFile, Consumer<ArrayList<String>> getFunction) {
        ArrayList<Object> objects = new ArrayList<>();
        ArrayList<ArrayList<String>> groupedObjects = getGroupedText(getContentFromFileAsList(williFile, "#"));
        ArrayList<String> differentGroups = getDifferentGroupTypes(getContentFromFileAsList(williFile, "#"));

        for (int i = 0; i < differentGroups.size(); i++) {
            int finalI = i;
            groupedObjects.stream()
                    .filter(strings -> strings.getFirst().equalsIgnoreCase(differentGroups.get(finalI)))
                    .forEach(getFunction);
        }
        return objects;
    }

    public static ArrayList<InterpretedItem> getAllItems() {
        Consumer<ArrayList<String>> getItemsFunction = (interpretedObject, differentGroups, finalI) -> {
                        if (differentGroups.get(finalI).contains("simpleItem")) {
                            items.add(new InterpretedSimpleItem(interpretedObject.get(1), interpretedObject.get(2), interpretedObject.get(3)));
                        } else if (differentGroups.get(finalI).contains("specialItem")) {
                            items.add(new InterpretedSpecialItem(interpretedObject.get(1), interpretedObject.get(2), interpretedObject.get(3)));
                        } else if (differentGroups.get(finalI).contains("simpleSword")) {
                            if (interpretedObject.contains("?]") && interpretedObject.contains("?[E")) {
                                items.add(new InterpretedSwordItem(interpretedObject.get(1), interpretedObject.get(2), interpretedObject.get(3), interpretedObject.get(4), interpretedObject.get(5), interpretedObject.get(6), getListFromBrackets(7, interpretedObject)));
                            } else {
                                items.add(new InterpretedSwordItem(interpretedObject.get(1), interpretedObject.get(2), interpretedObject.get(3), interpretedObject.get(4), interpretedObject.get(5), interpretedObject.get(6)));
                            }
                        } else if (differentGroups.get(finalI).contains("specialSword")) {
                            if (interpretedObject.contains("?]") && interpretedObject.contains("?[E")) {
                                items.add(new InterpretedSpecialSwordItem(interpretedObject.get(1), interpretedObject.get(2), interpretedObject.get(3), interpretedObject.get(4), interpretedObject.get(5), interpretedObject.get(6), getListFromBrackets(7, interpretedObject), getListFromBrackets(7 + 2 + getListFromBrackets(7, interpretedObject).size(), interpretedObject)));
                            } else {
                                items.add(new InterpretedSpecialSwordItem(interpretedObject.get(1), interpretedObject.get(2), interpretedObject.get(3), interpretedObject.get(4), interpretedObject.get(5), interpretedObject.get(6), getListFromBrackets(7, interpretedObject)));
                            }
                        } else if (differentGroups.get(finalI).contains("upgradableSword") || differentGroups.get(finalI).contains("upgradeableSword")) {
                            if (interpretedObject.contains("?]") && interpretedObject.contains("?[E")) {
                                items.add(new InterpretedItemWithUpgradedVariations(interpretedObject.get(1), interpretedObject.get(2), interpretedObject.get(3), interpretedObject.get(4), interpretedObject.get(5), interpretedObject.get(6), getListFromBrackets(7, interpretedObject), getListFromBrackets(7 + 2 + getListFromBrackets(7, interpretedObject).size(), interpretedObject)));
                            } else {
                                items.add(new InterpretedItemWithUpgradedVariations(interpretedObject.get(1), interpretedObject.get(2), interpretedObject.get(3), interpretedObject.get(4), interpretedObject.get(5), interpretedObject.get(6), getListFromBrackets(7, interpretedObject)));
                            }
                        }
                    });

        }
        return items;
    }

    public static ArrayList<InterpretedCreativeTab> getAllCreativeTabs() {
        ArrayList<InterpretedCreativeTab> tabs = new ArrayList<>();
        ArrayList<ArrayList<String>> groupedObjects = getGroupedText(getContentFromFileAsList(creativeTabFile, "#"));
        ArrayList<String> differentGroups = getDifferentGroupTypes(getContentFromFileAsList(creativeTabFile, "#"));

        for (int i = 0; i < differentGroups.size(); i++) {
            int finalI = i;
            groupedObjects.stream()
                    .filter(strings -> strings.getFirst().equalsIgnoreCase(differentGroups.get(finalI)))
                    .forEach(interpretedObject -> {
                        if (differentGroups.get(finalI).contains("simpleItem")) {
                            items.add(new InterpretedSimpleItem(interpretedObject.get(1), interpretedObject.get(2), interpretedObject.get(3)));
                        } else if (differentGroups.get(finalI).contains("specialItem")) {
                            items.add(new InterpretedSpecialItem(interpretedObject.get(1), interpretedObject.get(2), interpretedObject.get(3)));
                        } else if (differentGroups.get(finalI).contains("simpleSword")) {
                            if (interpretedObject.contains("?]") && interpretedObject.contains("?[E")) {
                                items.add(new InterpretedSwordItem(interpretedObject.get(1), interpretedObject.get(2), interpretedObject.get(3), interpretedObject.get(4), interpretedObject.get(5), interpretedObject.get(6), getListFromBrackets(7, interpretedObject)));
                            } else {
                                items.add(new InterpretedSwordItem(interpretedObject.get(1), interpretedObject.get(2), interpretedObject.get(3), interpretedObject.get(4), interpretedObject.get(5), interpretedObject.get(6)));
                            }
                        } else if (differentGroups.get(finalI).contains("specialSword")) {
                            if (interpretedObject.contains("?]") && interpretedObject.contains("?[E")) {
                                items.add(new InterpretedSpecialSwordItem(interpretedObject.get(1), interpretedObject.get(2), interpretedObject.get(3), interpretedObject.get(4), interpretedObject.get(5), interpretedObject.get(6), getListFromBrackets(7, interpretedObject), getListFromBrackets(7 + 2 + getListFromBrackets(7, interpretedObject).size(), interpretedObject)));
                            } else {
                                items.add(new InterpretedSpecialSwordItem(interpretedObject.get(1), interpretedObject.get(2), interpretedObject.get(3), interpretedObject.get(4), interpretedObject.get(5), interpretedObject.get(6), getListFromBrackets(7, interpretedObject)));
                            }
                        } else if (differentGroups.get(finalI).contains("upgradableSword") || differentGroups.get(finalI).contains("upgradeableSword")) {
                            if (interpretedObject.contains("?]") && interpretedObject.contains("?[E")) {
                                items.add(new InterpretedItemWithUpgradedVariations(interpretedObject.get(1), interpretedObject.get(2), interpretedObject.get(3), interpretedObject.get(4), interpretedObject.get(5), interpretedObject.get(6), getListFromBrackets(7, interpretedObject), getListFromBrackets(7 + 2 + getListFromBrackets(7, interpretedObject).size(), interpretedObject)));
                            } else {
                                items.add(new InterpretedItemWithUpgradedVariations(interpretedObject.get(1), interpretedObject.get(2), interpretedObject.get(3), interpretedObject.get(4), interpretedObject.get(5), interpretedObject.get(6), getListFromBrackets(7, interpretedObject)));
                            }
                        }
                    });

        }

        return tabs;
    }

    public static ArrayList<InterpretedRecipe> getAllRecipes() {
        return null;
    }

    public static ArrayList<InterpretedToolTier> getAllToolTiers() {
        return  null;
    }

    public static ArrayList<ArrayList<String>> getDifferentBlockTags() {
        ArrayList<ArrayList<String>> differentTags = new ArrayList<>();
        ArrayList<String> tag;

        for (int i = 0; i < blocks.size(); i++) {
            tag = new ArrayList<>();
            if (blocks.get(i).getTagTool().toUpperCase().contains("shovel".toUpperCase())
                    || blocks.get(i).getTagTool().toUpperCase().contains("axe".toUpperCase())
                    || blocks.get(i).getTagTool().toUpperCase().contains("pickaxe".toUpperCase())
                    || blocks.get(i).getTagTool().toUpperCase().contains("hoe".toUpperCase())) {
                tag.add("BlockTags.");
            } else if (!blocks.get(i).getTagTool().isEmpty()) {
                tag.add("ModTags.Blocks.");
            }
            if (!blocks.get(i).getTagTool().isEmpty()) {
                tag.add(blocks.get(i).getTagTool());
                tag.add("tool");
                differentTags.add(tag);
            }
        }

        for (int i = 0; i < blocks.size(); i++) {
            tag = new ArrayList<>();
            if (blocks.get(i).getTagType().toUpperCase().contains("stone".toUpperCase())
                    || blocks.get(i).getTagType().toUpperCase().contains("diamond".toUpperCase())
                    || blocks.get(i).getTagType().toUpperCase().contains("iron".toUpperCase())) {
                tag.add("BlockTags.");
            } else if (!blocks.get(i).getTagType().isEmpty()) {
                tag.add("ModTags.Blocks.");
            }
            if (!blocks.get(i).getTagType().isEmpty()) {
                tag.add(blocks.get(i).getTagType());
                tag.add("type");
                differentTags.add(tag);
            }
        }
        removeDuplicatesFromTagList(differentTags);
        return differentTags;
    }

    public static ArrayList<ArrayList<String>> getDifferentItemTags() {
        ArrayList<ArrayList<String>> differentTags = new ArrayList<>(); //alles tags
        ArrayList<String> tag; //ein tag objekt (tag prefix, und tag selbst)

        for (int i = 0; i < items.size(); i++) {
            for (int j = 1; j < items.get(i).getTagsOfItem().size(); j++) {
                tag = new ArrayList<>();
                if (isPartOfItemTags(items.get(i).getTagsOfItem().get(j).toUpperCase(), true)) {
                    tag.add("ItemTags.");
                } else {
                    tag.add("ModTags.Items.");
                }
                tag.add(items.get(i).getTagsOfItem().get(j));
                differentTags.add(tag);
            }
        }
        removeDuplicatesFromTagList(differentTags);
        return differentTags;
    }
}
