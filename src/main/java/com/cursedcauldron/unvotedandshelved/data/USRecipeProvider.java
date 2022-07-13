package com.cursedcauldron.unvotedandshelved.data;

import com.cursedcauldron.unvotedandshelved.UnvotedAndShelved;
import com.cursedcauldron.unvotedandshelved.init.USBlocks;
import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.data.recipes.SingleItemRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Consumer;

public class USRecipeProvider extends RecipeProvider {

    public USRecipeProvider(DataGenerator dataGenerator) {
        super(dataGenerator);
    }

    @Override
    protected void buildCraftingRecipes(Consumer<FinishedRecipe> consumer) {
        buildCopperButton(consumer, USBlocks.COPPER_BUTTON.get(), Items.COPPER_INGOT);
        waxing(consumer, USBlocks.WAXED_COPPER_BUTTON.get(), USBlocks.COPPER_BUTTON.get());
        waxing(consumer, USBlocks.WAXED_EXPOSED_COPPER_BUTTON.get(), USBlocks.EXPOSED_COPPER_BUTTON.get());
        waxing(consumer, USBlocks.WAXED_WEATHERED_COPPER_BUTTON.get(), USBlocks.WEATHERED_COPPER_BUTTON.get());
        waxing(consumer, USBlocks.WAXED_OXIDIZED_COPPER_BUTTON.get(), USBlocks.OXIDIZED_COPPER_BUTTON.get());
        waxing(consumer, USBlocks.WAXED_COPPER_PILLAR.get(), USBlocks.COPPER_PILLAR.get());
        waxing(consumer, USBlocks.WAXED_EXPOSED_COPPER_PILLAR.get(), USBlocks.EXPOSED_COPPER_PILLAR.get());
        waxing(consumer, USBlocks.WAXED_WEATHERED_COPPER_PILLAR.get(), USBlocks.WEATHERED_COPPER_PILLAR.get());
        waxing(consumer, USBlocks.WAXED_OXIDIZED_COPPER_PILLAR.get(), USBlocks.OXIDIZED_COPPER_PILLAR.get());
        stonecuttingPillar(consumer, USBlocks.COPPER_PILLAR.get(), Blocks.COPPER_BLOCK);
        stonecuttingPillar(consumer, USBlocks.EXPOSED_COPPER_PILLAR.get(), Blocks.EXPOSED_COPPER);
        stonecuttingPillar(consumer, USBlocks.WEATHERED_COPPER_PILLAR.get(), Blocks.WEATHERED_COPPER);
        stonecuttingPillar(consumer, USBlocks.OXIDIZED_COPPER_PILLAR.get(), Blocks.OXIDIZED_COPPER);
        stonecuttingPillar(consumer, USBlocks.WAXED_COPPER_PILLAR.get(), Blocks.WAXED_COPPER_BLOCK);
        stonecuttingPillar(consumer, USBlocks.WAXED_EXPOSED_COPPER_PILLAR.get(), Blocks.WAXED_EXPOSED_COPPER);
        stonecuttingPillar(consumer, USBlocks.WAXED_WEATHERED_COPPER_PILLAR.get(), Blocks.WAXED_WEATHERED_COPPER);
        stonecuttingPillar(consumer, USBlocks.WAXED_OXIDIZED_COPPER_PILLAR.get(), Blocks.WAXED_OXIDIZED_COPPER);
    }

    private static void stonecuttingPillar(Consumer<FinishedRecipe> consumer, ItemLike result, ItemLike ingredient) {
        stonecutting(consumer, result, ingredient, 4);
    }

    private static void stonecutting(Consumer<FinishedRecipe> consumer, ItemLike result, ItemLike ingredient, int count) {
        SingleItemRecipeBuilder.stonecutting(Ingredient.of(ingredient), result, count).unlockedBy(getHasName(ingredient), has(ingredient)).save(consumer, new ResourceLocation(UnvotedAndShelved.MODID, getConversionRecipeName(result, ingredient) + "_stonecutting"));
    }

    protected static String getConversionRecipeName(ItemLike result, ItemLike ingredient) {
        return getItemName(result) + "_from_" + getItemName(ingredient);
    }

    protected static String getItemName(ItemLike item) {
        return ForgeRegistries.ITEMS.getKey(item.asItem()).getPath();
    }

    protected static String getHasName(ItemLike item) {
        return "has_" + getItemName(item);
    }

    private static void waxing(Consumer<FinishedRecipe> consumer, ItemLike result, ItemLike ingredient) {
        ShapelessRecipeBuilder.
                shapeless(result)
                .requires(ingredient)
                .requires(Items.HONEYCOMB)
                .unlockedBy("has_" + Registry.ITEM.getKey(ingredient.asItem()).getPath(), has(ingredient))
                .save(consumer);
    }

    private static void buildCopperButton(Consumer<FinishedRecipe> consumer, ItemLike result, ItemLike ingredient) {
        ShapedRecipeBuilder.shaped(result, 4)
                .define('#', ingredient)
                .pattern("##")
                .pattern("##")
                .unlockedBy("has_" + Registry.ITEM.getKey(ingredient.asItem()).getPath(), has(ingredient))
                .save(consumer);
    }

}
