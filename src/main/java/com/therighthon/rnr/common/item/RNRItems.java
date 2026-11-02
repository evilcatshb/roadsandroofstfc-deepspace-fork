package com.therighthon.rnr.common.item;
import com.therighthon.rnr.common.RNRUtil;

import java.util.Locale;
import java.util.Map;
import java.util.function.Supplier;
import com.therighthon.rnr.RoadsAndRoofs;

import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import com.therighthon.rnr.common.material.RNRMaterials.Rock;
import com.therighthon.rnr.common.material.RNRMaterials.SandBlockType;
import com.therighthon.rnr.common.material.RNRMaterials.Wood;
import com.therighthon.rnr.common.material.RNRMaterials.Metal;


public class RNRItems
{
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, RoadsAndRoofs.MOD_ID);

    //Metal Items
    public static final Map<Metal, RegistryObject<Item>> MATTOCK_HEADS = RNRUtil.mapOfKeys(Metal.class, Metal::hasUtilities, metal -> register("metal/mattock_head/" + metal.getSerializedName()));
    public static final Map<Metal, RegistryObject<Item>> MATTOCKS = RNRUtil.mapOfKeys(Metal.class, Metal::hasUtilities, metal -> register("metal/mattock/" + metal.getSerializedName(), () -> new MattockItem(metal.toolTier(), 1.0f, -1.5F, metal.properties())));

    //Stone items
    public static final Map<Rock, RegistryObject<Item>> FLAGSTONE = RNRUtil.mapOfKeys(Rock.class, type ->
        register("flagstone/" + type.getSerializedName())
    );
    public static final Map<Rock, RegistryObject<Item>> GRAVEL_FILL = RNRUtil.mapOfKeys(Rock.class, type ->
        register("gravel_fill/" + type.getSerializedName())
    );
    public static final Map<SandBlockType, RegistryObject<Item>> SANDSTONE_FLAGSTONE = RNRUtil.mapOfKeys(SandBlockType.class, type ->
        register("flagstone/" + type.getSerializedName() + "_sandstone")
    );

    public static final RegistryObject<Item> UNFIRED_ROOF_TILE = register("unfired_roof_tile");
    public static final RegistryObject<Item> UNFIRED_TERRACOTTA_ROOF_TILE = register("unfired_terracotta_roof_tile");
    public static final RegistryObject<Item> CERAMIC_ROOF_TILE = register("ceramic_roof_tile");
    public static final RegistryObject<Item> TERRACOTTA_ROOF_TILE = register("terracotta_roof_tile");
    public static final Map<Wood, RegistryObject<Item>> WOOD_SHINGLE = RNRUtil.mapOfKeys(Wood.class, wood -> register("wood/shingle/" + wood.getSerializedName()));

    //Misc items
    public static final RegistryObject<Item> CRUSHED_BASE_COURSE = register("crushed_base_course");
    public static final RegistryObject<Item> HOGGIN_MIX = register("hoggin_mix");

    private static RegistryObject<Item> register(String name)
    {
        return register(name, () -> new Item(new Item.Properties()));
    }

    private static <T extends Item> RegistryObject<T> register(String name, Supplier<T> item)
    {
        return ITEMS.register(name.toLowerCase(Locale.ROOT), item);
    }

}
