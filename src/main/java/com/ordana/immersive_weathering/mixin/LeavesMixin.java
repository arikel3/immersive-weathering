package com.ordana.immersive_weathering.mixin;

import com.ordana.immersive_weathering.registry.blocks.LeafPileBlock;
import com.ordana.immersive_weathering.registry.blocks.WeatheringHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(LeavesBlock.class)
public abstract class LeavesMixin extends Block implements BonemealableBlock {

    public LeavesMixin(Properties settings) {
        super(settings);
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return true;
    }

    @Inject(method = "randomTick", at = @At("HEAD"))
    public void randomTick(BlockState state, ServerLevel world, BlockPos pos, Random random, CallbackInfo ci) {

        //Drastically reduced this chance to help lag
        if (!state.getValue(LeavesBlock.PERSISTENT) && random.nextFloat() < 0.1f) {

            var leafPile = WeatheringHelper.getFallenLeafPile(state).orElse(null);
            if (leafPile != null && world.getBlockState(pos.below()).isAir()) {
                if (!world.isAreaLoaded(pos, 2)) return;
                BlockPos targetPos = world.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, pos);
                int maxFallenLeavesReach = 16;
                if (pos.getY() - targetPos.getY() < maxFallenLeavesReach) {

                    BlockState replaceState = world.getBlockState(targetPos);

                    boolean isOnLeaf = replaceState.getBlock() instanceof LeafPileBlock;

                    BlockPos belowPos = targetPos.below();
                    BlockState below = world.getBlockState(belowPos);

                    //if we find a non-air block we check if its upper face is sturdy. Given previous iteration if we are not on the first cycle blocks above must be air
                    if (isOnLeaf ||
                            (replaceState.isAir() && below.isFaceSturdy(world, belowPos, Direction.UP)
                                    && !WeatheringHelper.hasEnoughBlocksAround(targetPos, 2, 1, 2,
                                    world, b -> b.getBlock() instanceof LeafPileBlock, 7))) {


                        int pileHeight = 0;
                        for (Direction direction : Direction.Plane.HORIZONTAL) {
                            BlockState neighbor = world.getBlockState(targetPos.relative(direction));
                            if (!isOnLeaf && neighbor.getBlock() instanceof LeafPileBlock) {
                                pileHeight = 1;
                            } else if (neighbor.is(BlockTags.LOGS) && (!neighbor.hasProperty(RotatedPillarBlock.AXIS) ||
                                    neighbor.getValue(RotatedPillarBlock.AXIS) == Direction.Axis.Y)) { //TODO: replace with mod tag
                                pileHeight = isOnLeaf ? 2 : 1;
                                break;
                            }

                        }
                        if (pileHeight > 0) {
                            world.setBlock(targetPos, leafPile.defaultBlockState()
                                    .setValue(LeafPileBlock.LAYERS, pileHeight), 2);
                        }
                    }

                }
            }
        }
    }

    @Override
    public boolean isValidBonemealTarget(BlockGetter world, BlockPos pos, BlockState state, boolean isClient) {
        return state.is(Blocks.FLOWERING_AZALEA_LEAVES);
    }

    @Override
    public boolean isBonemealSuccess(Level world, Random random, BlockPos pos, BlockState state) {
        return state.is(Blocks.FLOWERING_AZALEA_LEAVES);
    }

    @Override
    public void performBonemeal(ServerLevel world, Random random, BlockPos pos, BlockState state) {
        for (var direction : Direction.values()) {
            if (random.nextFloat() > 0.5f) {
                var targetPos = pos.relative(direction);
                BlockState targetBlock = world.getBlockState(targetPos);
                WeatheringHelper.getAzaleaGrowth(targetBlock).ifPresent(s ->
                        world.setBlockAndUpdate(targetPos, s)
                );
            }
        }
    }
}
