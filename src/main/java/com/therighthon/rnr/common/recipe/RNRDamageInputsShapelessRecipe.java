package com.therighthon.rnr.common.recipe;

import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.level.Level;
import javax.annotation.Nullable;

public final class RNRDamageInputsShapelessRecipe implements CraftingRecipe {
    private final ResourceLocation id;
    private final ShapelessRecipe delegate;
    public RNRDamageInputsShapelessRecipe(ResourceLocation id, ShapelessRecipe delegate) { this.id=id; this.delegate=delegate; }
    @Override public ResourceLocation getId() { return id; }
    @Override public boolean matches(CraftingContainer c, Level l) { return delegate.matches(c,l); }
    @Override public ItemStack assemble(CraftingContainer c, RegistryAccess a) { return delegate.assemble(c,a); }
    @Override public boolean canCraftInDimensions(int w,int h) { return delegate.canCraftInDimensions(w,h); }
    @Override public ItemStack getResultItem(RegistryAccess a) { return delegate.getResultItem(a); }
    @Override public NonNullList<ItemStack> getRemainingItems(CraftingContainer c) {
        NonNullList<ItemStack> out = delegate.getRemainingItems(c);
        for (int i=0;i<c.getContainerSize();i++) {
            ItemStack in=c.getItem(i);
            if (in.isEmpty() || !in.isDamageableItem()) continue;
            ItemStack damaged=in.copyWithCount(1);
            int d=damaged.getDamageValue()+1;
            if (d>=damaged.getMaxDamage()) out.set(i, ItemStack.EMPTY);
            else { damaged.setDamageValue(d); out.set(i,damaged); }
        }
        return out;
    }
    @Override public NonNullList<net.minecraft.world.item.crafting.Ingredient> getIngredients() { return delegate.getIngredients(); }
    @Override public boolean isSpecial() { return delegate.isSpecial(); }
    @Override public String getGroup() { return delegate.getGroup(); }
    @Override public CraftingBookCategory category() { return delegate.category(); }
    @Override public RecipeSerializer<?> getSerializer() { return RNRRecipeSerializers.DAMAGE_INPUTS_SHAPELESS.get(); }
    @Override public RecipeType<?> getType() { return RNRRecipeTypes.DAMAGE_INPUTS_SHAPELESS.get(); }

    public static final class Serializer implements RecipeSerializer<RNRDamageInputsShapelessRecipe> {
        @Override public RNRDamageInputsShapelessRecipe fromJson(ResourceLocation id, JsonObject json) {
            ShapelessRecipe recipe=new ShapelessRecipe.Serializer().fromJson(id,json.getAsJsonObject("recipe"));
            return new RNRDamageInputsShapelessRecipe(id,recipe);
        }
        @Override public RNRDamageInputsShapelessRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            ShapelessRecipe recipe=new ShapelessRecipe.Serializer().fromNetwork(id,buf);
            return new RNRDamageInputsShapelessRecipe(id,recipe);
        }
        @Override public void toNetwork(FriendlyByteBuf buf,RNRDamageInputsShapelessRecipe recipe) { ((RecipeSerializer<ShapelessRecipe>)recipe.delegate.getSerializer()).toNetwork(buf,recipe.delegate); }
    }
}
