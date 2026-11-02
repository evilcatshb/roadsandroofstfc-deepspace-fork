package com.therighthon.rnr.common.recipe;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;

public final class RNRBlockIngredient {
    private final List<Block> blocks; private final List<TagKey<Block>> tags;
    private RNRBlockIngredient(List<Block> b,List<TagKey<Block>> t){blocks=b;tags=t;}
    public static RNRBlockIngredient of(Block b){return new RNRBlockIngredient(List.of(b),List.of());}
    public static RNRBlockIngredient fromJson(JsonElement e){
        List<Block> bs=new ArrayList<>(); List<TagKey<Block>> ts=new ArrayList<>();
        if(e.isJsonPrimitive()){Block b=ForgeRegistries.BLOCKS.getValue(ResourceLocation.tryParse(e.getAsString())); if(b!=null)bs.add(b);}
        else if(e.isJsonObject()){JsonObject o=e.getAsJsonObject(); if(o.has("item")){Block b=ForgeRegistries.BLOCKS.getValue(ResourceLocation.tryParse(o.get("item").getAsString()));if(b!=null)bs.add(b);} if(o.has("tag")){ts.add(TagKey.create(Registries.BLOCK,ResourceLocation.tryParse(o.get("tag").getAsString())));}}
        else if(e.isJsonArray()) for(JsonElement x:e.getAsJsonArray()){RNRBlockIngredient q=fromJson(x);bs.addAll(q.blocks);ts.addAll(q.tags);}
        return new RNRBlockIngredient(bs,ts);
    }
    public boolean test(BlockState s){if(blocks.contains(s.getBlock()))return true; for(TagKey<Block> t:tags)if(s.is(t))return true;return false;}
    public List<Block> blocks(){return blocks;}
    public void toNetwork(FriendlyByteBuf b){b.writeVarInt(blocks.size());for(Block x:blocks)b.writeResourceLocation(ForgeRegistries.BLOCKS.getKey(x));b.writeVarInt(tags.size());for(TagKey<Block> t:tags)b.writeResourceLocation(t.location());}
    public static RNRBlockIngredient fromNetwork(FriendlyByteBuf b){List<Block> bs=new ArrayList<>();int n=b.readVarInt();for(int i=0;i<n;i++){Block x=ForgeRegistries.BLOCKS.getValue(b.readResourceLocation());if(x!=null)bs.add(x);}List<TagKey<Block>> ts=new ArrayList<>();int m=b.readVarInt();for(int i=0;i<m;i++)ts.add(TagKey.create(Registries.BLOCK,b.readResourceLocation()));return new RNRBlockIngredient(bs,ts);}
}
