package com.therighthon.rnr.common.recipe;

import com.google.gson.JsonObject;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class BlockModRecipe extends RNRSimpleBlockRecipe {
    private final Ingredient inputItem;
    private final boolean consumesItem;
    public static final List<BlockModRecipe> CACHE = new ArrayList<>();

    public BlockModRecipe(ResourceLocation id, Ingredient item, RNRBlockIngredient block, BlockState out, boolean consume) {
        super(id, block, out);
        inputItem = item;
        consumesItem = consume;
    }

    /** Keep the legacy fast lookup cache synchronized with the active recipe manager. */
    private static void cache(BlockModRecipe recipe) {
        CACHE.removeIf(existing -> existing.getId().equals(recipe.getId()));
        CACHE.add(recipe);
    }

    public static BlockModRecipe getRecipe(BlockState state, ItemStack item) {
        for (BlockModRecipe recipe : CACHE) {
            if (recipe.matches(state, item)) return recipe;
        }
        return null;
    }

    public boolean matches(BlockState state, ItemStack item) {
        return ingredient.test(state) && inputItem.test(item);
    }

    public Ingredient getInputItem() { return inputItem; }
    public BlockState getOutputBlock() { return outputState; }
    public boolean consumesItem() { return consumesItem; }

    @Override public RecipeSerializer<?> getSerializer() { return RNRRecipeSerializers.BLOCK_MOD_RECIPE.get(); }
    @Override public RecipeType<?> getType() { return RNRRecipeTypes.BLOCK_MOD_RECIPE.get(); }

    public static class Serializer implements RecipeSerializer<BlockModRecipe> {
        @Override
        public BlockModRecipe fromJson(ResourceLocation id, JsonObject j) {
            Ingredient i = Ingredient.fromJson(j.get("input_item"));
            RNRBlockIngredient b = RNRBlockIngredient.fromJson(j.get("input_block"));
            BlockState o = ForgeRegistries.BLOCKS.getValue(ResourceLocation.tryParse(GsonHelper.getAsString(j, "output_block"))).defaultBlockState();
            BlockModRecipe recipe = new BlockModRecipe(id, i, b, o, j.has("consume_ingredient") ? j.get("consume_ingredient").getAsBoolean() : true);
            cache(recipe);
            return recipe;
        }

        @Override
        @Nullable
        public BlockModRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf b) {
            Ingredient i = Ingredient.fromNetwork(b);
            RNRBlockIngredient bi = RNRBlockIngredient.fromNetwork(b);
            Block x = ForgeRegistries.BLOCKS.getValue(b.readResourceLocation());
            BlockModRecipe recipe = new BlockModRecipe(id, i, bi, x.defaultBlockState(), b.readBoolean());
            cache(recipe);
            return recipe;
        }

        @Override
        public void toNetwork(FriendlyByteBuf b, BlockModRecipe r) {
            r.inputItem.toNetwork(b);
            r.ingredient.toNetwork(b);
            b.writeResourceLocation(ForgeRegistries.BLOCKS.getKey(r.outputState.getBlock()));
            b.writeBoolean(r.consumesItem);
        }
    }
}
