package com.therighthon.rnr.compat.emi;

import com.therighthon.rnr.common.RNRTags;
import com.therighthon.rnr.common.recipe.BlockModRecipe;
import com.therighthon.rnr.common.recipe.MattockRecipe;
import com.therighthon.rnr.common.recipe.RNRBlockIngredient;
import com.therighthon.rnr.common.recipe.RNRRecipeTypes;
import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiWorldInteractionRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.world.item.ItemStack;
import java.util.List;

@EmiEntrypoint
public final class RNREmiPlugin implements EmiPlugin {
    @Override
    public void register(EmiRegistry registry) {
        registry.getRecipeManager().getAllRecipesFor(RNRRecipeTypes.BLOCK_MOD_RECIPE.get())
            .forEach(recipe -> registry.addRecipe(blockMod(recipe)));
        registry.getRecipeManager().getAllRecipesFor(RNRRecipeTypes.MATTOCK_RECIPE.get())
            .forEach(recipe -> registry.addRecipe(mattock(recipe)));
        registry.addRecipe(EmiWorldInteractionRecipe.builder()
            .id(new net.minecraft.resources.ResourceLocation("rnr", "concrete_road_curing"))
            .leftInput(EmiIngredient.of(net.minecraft.world.item.crafting.Ingredient.of(net.minecraftforge.registries.ForgeRegistries.ITEMS.getValue(new net.minecraft.resources.ResourceLocation("rnr:wet_concrete_road")))))
            .output(EmiStack.of(new ItemStack(net.minecraftforge.registries.ForgeRegistries.ITEMS.getValue(new net.minecraft.resources.ResourceLocation("rnr:concrete_road")))))
            .supportsRecipeTree(false)
            .build());
    }

    private static EmiWorldInteractionRecipe blockMod(BlockModRecipe recipe) {
        return EmiWorldInteractionRecipe.builder()
            .id(recipe.getId())
            .leftInput(blocks(recipe.getBlockIngredient()))
            .rightInput(EmiIngredient.of(recipe.getInputItem()), !recipe.consumesItem())
            .output(EmiStack.of(new ItemStack(recipe.getOutputBlock().getBlock())))
            .supportsRecipeTree(false)
            .build();
    }

    private static EmiWorldInteractionRecipe mattock(MattockRecipe recipe) {
        EmiIngredient tool = recipe.getItemIngredient() == null
            ? EmiIngredient.of(RNRTags.Items.MATTOCKS)
            : EmiIngredient.of(recipe.getItemIngredient());

        return EmiWorldInteractionRecipe.builder()
            .id(recipe.getId())
            .leftInput(blocks(recipe.getBlockIngredient()))
            .rightInput(tool, true)
            .output(EmiStack.of(new ItemStack(recipe.getBlockCraftingResult(recipe.getBlockIngredient().blocks().get(0).defaultBlockState()).getBlock())))
            .supportsRecipeTree(false)
            .build();
    }

    private static EmiIngredient blocks(RNRBlockIngredient ingredient) {
        List<EmiIngredient> stacks = ingredient.blocks().stream()
            .map(block -> EmiStack.of(block.asItem()))
            .filter(stack -> !stack.isEmpty())
            .map(stack -> (EmiIngredient) stack)
            .toList();
        return EmiIngredient.of(stacks);
    }
}
