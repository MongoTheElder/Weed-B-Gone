package tv.mongotheelder.weedbgone.datagen;

import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.Tags;
import tv.mongotheelder.weedbgone.setup.Registration;

import java.util.concurrent.CompletableFuture;

public class RecipeGenerator extends RecipeProvider {

    public RecipeGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider);
    }

    @Override
    protected void buildRecipes(RecipeOutput consumer) {
        // Motor part
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Registration.MOTOR.get())
                .pattern("ici")
                .pattern("c c")
                .pattern("ici")
                .define('c', Tags.Items.INGOTS_COPPER)
                .define('i', Tags.Items.INGOTS_IRON)
                .group("weedbgone")
                .unlockedBy("has_iron", InventoryChangeTrigger.TriggerInstance.hasItems(Items.IRON_INGOT))
                .save(consumer);

        // Spool part
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Registration.SPOOL.get())
                .pattern("   ")
                .pattern("sis")
                .pattern("   ")
                .define('s', Tags.Items.STRING)
                .define('i', Tags.Items.INGOTS_IRON)
                .group("weedbgone")
                .unlockedBy("has_iron", InventoryChangeTrigger.TriggerInstance.hasItems(Items.IRON_INGOT))
                .save(consumer);

        // String trimmer
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Registration.STRING_TRIMMER.get())
                .pattern("m  ")
                .pattern(" s ")
                .pattern("  p")
                .define('m', Registration.MOTOR.get())
                .define('s', Items.STICK)
                .define('p', Registration.SPOOL.get())
                .group("weedbgone")
                .unlockedBy("has_iron", InventoryChangeTrigger.TriggerInstance.hasItems(Items.IRON_INGOT))
                .save(consumer);
    }
}
