package com.ordana.immersive_weathering.mixin;

import com.ordana.immersive_weathering.registry.entities.FollowFlowerCrownGoal;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BeeEntity.class)
public abstract class BeeMixin extends AnimalEntity {

    protected BeeMixin(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "initGoals", at = @At("TAIL"))
    protected void initGoals(CallbackInfo ci) {
        this.beeGoalHelper(this);
    }

    private void beeGoalHelper(AnimalEntity animal){
        this.goalSelector.add(3, new FollowFlowerCrownGoal(animal, 1D, null, false));
    }

    @Nullable
    @Shadow
    public abstract PassiveEntity createChild(ServerWorld world, PassiveEntity entity);
}
