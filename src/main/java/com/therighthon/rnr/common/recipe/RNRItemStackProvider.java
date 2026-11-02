package com.therighthon.rnr.common.recipe;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.registries.ForgeRegistries;
public final class RNRItemStackProvider { private final ItemStack stack; private RNRItemStackProvider(ItemStack s){stack=s;} public static RNRItemStackProvider empty(){return new RNRItemStackProvider(ItemStack.EMPTY);} public static RNRItemStackProvider fromJson(JsonObject o){if(o==null)return empty();String id=o.has("item")?o.get("item").getAsString():o.has("result")?o.getAsJsonObject("result").get("item").getAsString():null;int c=o.has("count")?o.get("count").getAsInt():1;if(id==null)return empty();Item i=ForgeRegistries.ITEMS.getValue(ResourceLocation.tryParse(id));return new RNRItemStackProvider(i==null?ItemStack.EMPTY:new ItemStack(i,c));} public ItemStack getSingleStack(ItemStack ignored){return stack.copy();} public void toNetwork(FriendlyByteBuf b){b.writeItem(stack);} public static RNRItemStackProvider fromNetwork(FriendlyByteBuf b){return new RNRItemStackProvider(b.readItem());} }
