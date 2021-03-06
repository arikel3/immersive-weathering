package com.ordana.immersive_weathering.mixin;

import com.ordana.immersive_weathering.ImmersiveWeathering;
import com.ordana.immersive_weathering.registry.ModParticles;
import com.ordana.immersive_weathering.registry.blocks.WeatheringHelper;
import net.minecraft.block.*;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;


@Mixin(FireBlock.class)
public abstract class FireMixin extends AbstractFireBlock{

    @Unique
    private BlockState bs;

    public FireMixin(Settings settings, float damage) {
        super(settings, damage);
    }

    @Inject(method = "trySpreadingFire",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/World;removeBlock(Lnet/minecraft/util/math/BlockPos;Z)Z",
                    shift = At.Shift.AFTER))
    private void afterRemoveBlock(World world, BlockPos pos, int spreadFactor, Random rand, int currentAge, CallbackInfo ci) {
        if(ImmersiveWeathering.getConfig().fireAndIceConfig.fireCharsWood) {
            WeatheringHelper.tryCharBlock(world, pos, bs);
        }
    }

    @Inject(method = "trySpreadingFire",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/World;removeBlock(Lnet/minecraft/util/math/BlockPos;Z)Z"))
    private void beforeRemoveBlock(World world, BlockPos pos, int spreadFactor, Random rand, int currentAge, CallbackInfo ci) {
        bs = world.getBlockState(pos);
    }
}