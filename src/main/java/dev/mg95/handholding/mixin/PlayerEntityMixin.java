package dev.mg95.handholding.mixin;

import dev.mg95.handholding.PlayerEntityMixinAccess;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.UUID;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin implements PlayerEntityMixinAccess {
    @Unique
    private PlayerEntity holdingHandsWithLeft;

    @Unique
    private PlayerEntity holdingHandsWithRight;


    @Override
    public PlayerEntity handHolding$getHoldingHandsWithLeft() {
        return holdingHandsWithLeft;
    }

    @Override
    public void handHolding$setHoldingHandsWithLeft(PlayerEntity holdingHandsWithLeft) {
        this.holdingHandsWithLeft = holdingHandsWithLeft;
    }

    @Override
    public PlayerEntity handHolding$getHoldingHandsWithRight() {
        return holdingHandsWithRight;
    }

    @Override
    public void handHolding$setHoldingHandsWithRight(PlayerEntity holdingHandsWithRight) {
        this.holdingHandsWithRight = holdingHandsWithRight;
    }
}
