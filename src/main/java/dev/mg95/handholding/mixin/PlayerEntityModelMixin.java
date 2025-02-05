package dev.mg95.handholding.mixin;

import dev.mg95.handholding.PlayerEntityMixinAccess;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntityModel.class)
public class PlayerEntityModelMixin {
    @Inject(method = "setAngles(Lnet/minecraft/entity/LivingEntity;FFFFF)V", at = @At("TAIL"))
    private void setAngles(LivingEntity holder, float f, float g, float h, float i, float j, CallbackInfo ci) {
        var model = (PlayerEntityModel) (Object) this;

        positionArm(holder ,((PlayerEntityMixinAccess) holder).handHolding$getHoldingHandsWithLeft(), model.leftArm);
        positionArm(holder, ((PlayerEntityMixinAccess) holder).handHolding$getHoldingHandsWithRight(), model.rightArm);

        model.rightSleeve.copyTransform(model.rightArm);
        model.leftSleeve.copyTransform(model.leftArm);
    }

    private void positionArm(LivingEntity holder, PlayerEntity holdee, ModelPart arm) {
        if (holdee == null) return;


        double deltaX = holder.getX() - holdee.getX();
        double deltaY = holder.getEyeY() - holdee.getPos().y;
        double deltaZ = holder.getZ() - holdee.getZ();

        float distance = holder.distanceTo(holdee);

        float pitch = (float) (Math.atan2(deltaY, distance) - 1.5);


        float holderYaw = (float) Math.toRadians(holder.prevBodyYaw);
        holderYaw = (float) (((holderYaw + Math.PI) % (2 * Math.PI)) - Math.PI);

        float targetYaw = (float) Math.atan2(deltaZ, deltaX);
        float yaw = targetYaw - holderYaw + 1.5f;

        arm.pitch = pitch;
        arm.yaw = yaw;
    }
}
