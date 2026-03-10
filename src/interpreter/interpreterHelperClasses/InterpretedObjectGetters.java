package interpreter.interpreterHelperClasses;

import interpreter.interpretedObjects.blocks.InterpretedBlock;
import interpreter.interpretedObjects.blocks.InterpretedOre;
import interpreter.interpretedObjects.blocks.special.InterpretedComplexBlock;
import interpreter.interpretedObjects.blocks.special.InterpretedSimpleBlock;
import interpreter.interpretedObjects.blocks.special.InterpretedSpecialBlock;
import interpreter.interpretedObjects.creativeTabs.InterpretedCreativeTab;
import interpreter.interpretedObjects.items.InterpretedItem;
import interpreter.interpretedObjects.recipes.InterpretedRecipe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

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

    public static ArrayList<InterpretedItem> getAllItems() {
        return null;
    }

    public static ArrayList<InterpretedCreativeTab> getAllCreativeTabs() {
        return null;
    }

    public static ArrayList<InterpretedRecipe> getAllRecipes() {
        return null;
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
