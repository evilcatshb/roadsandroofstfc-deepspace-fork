package com.therighthon.rnr;

import com.therighthon.rnr.common.RNRPlayerData;
import com.therighthon.rnr.common.RNRTags;
import com.therighthon.rnr.common.recipe.BlockModRecipe;
import com.therighthon.rnr.common.recipe.MattockRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.registries.ForgeRegistries;

public final class RNRHelpers
{
    public static InteractionResult blockModRecipeCompatible(BlockState state, Level level, BlockPos pos, Player player,
            InteractionHand hand, BlockHitResult hit)
    {
        BlockModRecipe r = BlockModRecipe.getRecipe(state, player.getItemInHand(hand));
        if (r == null || player.blockPosition().equals(pos))
            return InteractionResult.FAIL;

        if (!level.isClientSide && r.consumesItem() && !player.isCreative())
        {
            ItemStack s = player.getItemInHand(hand);
            boolean concreteBucket = s.is(net.minecraftforge.registries.ForgeRegistries.ITEMS
                    .getValue(new ResourceLocation("gtceu:concrete_bucket")));
            if (s.isDamageableItem())
                s.hurtAndBreak(1, player, p -> {});
            else
            {
                s.shrink(1);
                if (concreteBucket)
                {
                    if (s.isEmpty())
                        player.setItemInHand(hand, new ItemStack(net.minecraft.world.item.Items.BUCKET));
                    else
                        player.getInventory().placeItemBackInInventory(new ItemStack(net.minecraft.world.item.Items.BUCKET));
                }
            }
        }

        level.setBlock(pos, r.getOutputBlock(), 3);
        level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(player, state));
        return InteractionResult.SUCCESS;
    }

    /**
     * Applies the Roads & Roofs Mattock's in-world shape transformations.
     *
     * Any RNR block with a matching <base>_stairs and <base>_slab registration
     * can be converted base -> stairs. Any RNR *_stairs block with a matching
     * *_slab registration can be converted stairs -> slab. This deliberately
     * uses the registry rather than a hard-coded list so newly registered RNR
     * road/roof variants receive the same behaviour automatically.
     */
    private static BlockState getRoadOrRoofShapeResult(Player player, Level level, BlockPos pos,
            BlockHitResult hit)
    {
        BlockState current = level.getBlockState(pos);
        ResourceLocation currentId = ForgeRegistries.BLOCKS.getKey(current.getBlock());
        if (currentId == null || !RoadsAndRoofs.MOD_ID.equals(currentId.getNamespace()))
            return null;

        String path = currentId.getPath();
        ResourceLocation targetId;

        if (path.endsWith("_stairs"))
        {
            targetId = new ResourceLocation(currentId.getNamespace(),
                    path.substring(0, path.length() - "_stairs".length()) + "_slab");
        }
        else
        {
            targetId = new ResourceLocation(currentId.getNamespace(), path + "_stairs");
            ResourceLocation slabId = new ResourceLocation(currentId.getNamespace(), path + "_slab");
            if (!ForgeRegistries.BLOCKS.containsKey(targetId) || !ForgeRegistries.BLOCKS.containsKey(slabId))
                return null;
        }

        if (!ForgeRegistries.BLOCKS.containsKey(targetId))
            return null;

        Block target = ForgeRegistries.BLOCKS.getValue(targetId);
        if (target == null || target == current.getBlock())
            return null;

        BlockState result;
        if (path.endsWith("_stairs"))
        {
            result = target.defaultBlockState();
        }
        else
        {
            result = target.getStateForPlacement(new BlockPlaceContext(player, InteractionHand.MAIN_HAND,
                    new ItemStack(target), hit));
            if (result == null)
                return null;
        }

        // Preserve waterlogging when both the source and destination expose it.
        if (current.hasProperty(net.minecraft.world.level.block.state.properties.BlockStateProperties.WATERLOGGED)
                && result.hasProperty(net.minecraft.world.level.block.state.properties.BlockStateProperties.WATERLOGGED))
        {
            result = result.setValue(
                    net.minecraft.world.level.block.state.properties.BlockStateProperties.WATERLOGGED,
                    current.getValue(net.minecraft.world.level.block.state.properties.BlockStateProperties.WATERLOGGED));
        }

        return result;
    }

    public static InteractionResult useMattockOn(Player player, Level level, BlockPos pos,
            BlockHitResult hit, InteractionHand hand)
    {
        // The shape conversion is intentionally checked first. It is independent
        // of the selectable MattockRecipe mode, so base -> stairs and stairs -> slab
        // always work with the Mattock when the corresponding RNR variants exist.
        BlockState shapeResult = getRoadOrRoofShapeResult(player, level, pos, hit);
        if (shapeResult != null)
        {
            if (!level.isClientSide)
                player.getItemInHand(hand).hurtAndBreak(1, player, p -> {});
            level.setBlockAndUpdate(pos, shapeResult);
            level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(player, level.getBlockState(pos)));
            return InteractionResult.SUCCESS;
        }

        // Preserve the existing recipe-driven Mattock behaviour for all other
        // transformations (tamping, concrete processing, etc.).
        var result = MattockRecipe.computeResult(player, level.getBlockState(pos), hit, true);
        return result.map(out -> {
            if (!level.isClientSide)
            {
                MattockRecipe r = MattockRecipe.getRecipe(level.getBlockState(pos), player.getItemInHand(hand),
                        RNRPlayerData.getMode(player));
                if (r != null && !r.getExtraDrop(player.getItemInHand(hand)).isEmpty())
                    player.getInventory().placeItemBackInInventory(r.getExtraDrop(player.getItemInHand(hand)));
                player.getItemInHand(hand).hurtAndBreak(1, player, x -> {});
            }
            level.setBlockAndUpdate(pos, out);
            return InteractionResult.SUCCESS;
        }, x -> x);
    }

    public static void updateWetCrackingConcrete(Level level, BlockPos pos) {}

    private RNRHelpers() {}
}
