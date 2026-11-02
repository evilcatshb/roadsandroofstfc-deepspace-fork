package com.therighthon.rnr.common.material;

import java.util.Locale;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.tags.BlockTags;

public final class RNRMaterials {
    public enum Rock {
        GRANITE(MapColor.STONE, 3.5f), DIORITE(MapColor.STONE, 3.5f), GABBRO(MapColor.STONE, 3.5f), SHALE(MapColor.COLOR_GRAY, 2.5f), CLAYSTONE(MapColor.COLOR_GRAY, 2.5f), LIMESTONE(MapColor.STONE, 2.5f), CONGLOMERATE(MapColor.STONE, 3f), DOLOMITE(MapColor.STONE, 3f), CHERT(MapColor.STONE, 3f), CHALK(MapColor.QUARTZ, 1.5f), RHYOLITE(MapColor.STONE, 3.5f), BASALT(MapColor.COLOR_BLACK, 3.5f), ANDESITE(MapColor.STONE, 3.5f), DACITE(MapColor.STONE, 3.5f), QUARTZITE(MapColor.QUARTZ, 4f), SLATE(MapColor.COLOR_GRAY, 3f), PHYLLITE(MapColor.COLOR_GRAY, 3f), SCHIST(MapColor.COLOR_GRAY, 3f), GNEISS(MapColor.STONE, 4f), MARBLE(MapColor.QUARTZ, 3f);
        public static final Rock[] VALUES = values();
        private final MapColor color; private final float hardness;
        Rock(MapColor c,float h){color=c;hardness=h;}
        public MapColor color(){return color;} public Category category(){return new Category(hardness);} public String getSerializedName(){return name().toLowerCase(Locale.ROOT);}
        public static final class Category { private final float h; Category(float h){this.h=h;} public float hardness(float fallback){return h;} }
        public java.util.function.Supplier<Block> getBlock(BlockType type){ return () -> new Block(BlockBehaviour.Properties.of().mapColor(color).strength(hardness)); }
        public enum BlockType { RAW }
    }
    public enum Wood { ACACIA, ASH, ASPEN, BIRCH, BLACKWOOD, CHESTNUT, DOUGLAS_FIR, HICKORY, KAPOK, MANGROVE, MAPLE, OAK, PALM, PINE, ROSEWOOD, SEQUOIA, SPRUCE, SYCAMORE, WHITE_CEDAR, WILLOW;
        public String getSerializedName(){return name().toLowerCase(Locale.ROOT);} }
    public enum SandBlockType { BLACK, BROWN, GREEN, PINK, RED, WHITE, YELLOW; public String getSerializedName(){return name().toLowerCase(Locale.ROOT);} }
    public enum Metal { BISMUTH_BRONZE(Tiers.IRON), BLACK_BRONZE(Tiers.IRON), BRONZE(Tiers.IRON), COPPER(Tiers.IRON), WROUGHT_IRON(Tiers.IRON), STEEL(Tiers.DIAMOND), BLACK_STEEL(Tiers.DIAMOND), BLUE_STEEL(Tiers.NETHERITE), RED_STEEL(Tiers.NETHERITE);
        private final Tier tier; Metal(Tier t){tier=t;} public Tier toolTier(){return tier;} public boolean hasUtilities(){return true;} public String getSerializedName(){return name().toLowerCase(Locale.ROOT);} public Item.Properties properties(){return new Item.Properties().durability( tier.getUses()*2 );}
    }
    private RNRMaterials(){}
}
