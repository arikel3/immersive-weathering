package com.ordana.immersive_weathering.registry.blocks;

import com.ordana.immersive_weathering.registry.ModTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SlabBlock;
import net.minecraft.fluid.Fluids;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import java.util.Random;

public class MossableSlabBlock extends SlabBlock implements Mossable {
    private final Mossable.MossLevel mossLevel;

    public MossableSlabBlock(Mossable.MossLevel mossLevel, Settings settings) {
        super(settings);
        this.mossLevel = mossLevel;
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random){
        for (Direction direction : Direction.values()) {
            var targetPos = pos.offset(direction);
            BlockState neighborState = world.getBlockState(targetPos);
            if (neighborState.getFluidState().getFluid() == Fluids.LAVA || neighborState.getFluidState().getFluid() == Fluids.FLOWING_LAVA) {
                return;
            }
        }
        for (Direction direction : Direction.values()) {
            var targetPos = pos.offset(direction);
            BlockState neighborState = world.getBlockState(targetPos);
            if ((world.getBlockState(pos.offset(direction)).isIn(ModTags.MOSS_SOURCE) || (neighborState.contains(Properties.WATERLOGGED)) && neighborState.get(Properties.WATERLOGGED))) {
                float f = 0.5f;
                if (random.nextFloat() > 0.5f) {
                    this.tryDegrade(state, world, pos, random);
                }
            }
            if (BlockPos.streamOutwards(pos, 1, 1, 1)
                    .map(world::getBlockState)
                    .anyMatch(e -> (e.contains(Properties.WATERLOGGED) && e.get(Properties.WATERLOGGED)) || e.isIn(ModTags.MOSS_SOURCE))) {
                if (BlockPos.streamOutwards(pos, 2, 2, 2)
                        .map(world::getBlockState)
                        .filter(b -> b.isIn(ModTags.MOSSY))
                        .toList().size() <= 20) {
                    float f = 0.4f;
                    if (random.nextFloat() > 0.4f) {
                        this.tryDegrade(state, world, pos, random);
                    }
                }
            }
            if (BlockPos.streamOutwards(pos, 2, 2, 2)
                    .map(world::getBlockState)
                    .anyMatch(e -> (e.contains(Properties.WATERLOGGED) && e.get(Properties.WATERLOGGED)) || e.isIn(ModTags.MOSS_SOURCE))) {
                if (BlockPos.streamOutwards(pos, 2, 2, 2)
                        .map(world::getBlockState)
                        .filter(b -> b.isIn(ModTags.MOSSY))
                        .toList().size() <= 15) {
                    float f = 0.3f;
                    if (random.nextFloat() > 0.3f) {
                        this.tryDegrade(state, world, pos, random);
                    }
                }
            }
            if (BlockPos.streamOutwards(pos, 3, 3, 3)
                    .map(world::getBlockState)
                    .anyMatch(e -> (e.contains(Properties.WATERLOGGED) && e.get(Properties.WATERLOGGED)) || e.isIn(ModTags.MOSS_SOURCE))) {
                if (BlockPos.streamOutwards(pos, 2, 2, 2)
                        .map(world::getBlockState)
                        .filter(b -> b.isIn(ModTags.MOSSY))
                        .toList().size() <= 8) {
                    float f = 0.2f;
                    if (random.nextFloat() > 0.2f) {
                        this.tryDegrade(state, world, pos, random);
                    }
                }
            }
            if (BlockPos.streamOutwards(pos, 4, 4, 4)
                    .map(world::getBlockState)
                    .anyMatch(e -> (e.contains(Properties.WATERLOGGED) && e.get(Properties.WATERLOGGED)) || e.isIn(ModTags.MOSS_SOURCE))) {
                if (BlockPos.streamOutwards(pos, 2, 2, 2)
                        .map(world::getBlockState)
                        .filter(b -> b.isIn(ModTags.MOSSY))
                        .toList().size() <= 6) {
                    float f = 0.1f;
                    if (random.nextFloat() > 0.1f) {
                        this.tryDegrade(state, world, pos, random);
                    }
                }
            }
            if (BlockPos.streamOutwards(pos, 5, 5, 5)
                    .map(world::getBlockState)
                    .anyMatch(e -> (e.contains(Properties.WATERLOGGED) && e.get(Properties.WATERLOGGED)) || e.isIn(ModTags.MOSS_SOURCE))) {
                if (BlockPos.streamOutwards(pos, 2, 2, 2)
                        .map(world::getBlockState)
                        .filter(b -> b.isIn(ModTags.MOSSY))
                        .toList().size() <= 3) {
                    float f = 0.09f;
                    if (random.nextFloat() > 0.09f) {
                        this.tryDegrade(state, world, pos, random);
                    }
                }
            }
        }
    }
    @Override
    public boolean hasRandomTicks(BlockState state) {
        return Mossable.getIncreasedMossBlock(state.getBlock()).isPresent();
    }

    @Override
    public Mossable.MossLevel getDegradationLevel() {
        return this.mossLevel;
    }
}