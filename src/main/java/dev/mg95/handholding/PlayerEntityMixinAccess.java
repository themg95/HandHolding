package dev.mg95.handholding;

import net.minecraft.entity.player.PlayerEntity;

import java.util.UUID;

public interface PlayerEntityMixinAccess {
    PlayerEntity handHolding$getHoldingHandsWithLeft();

    void handHolding$setHoldingHandsWithLeft(PlayerEntity holdingHandsWithLeft);

    PlayerEntity handHolding$getHoldingHandsWithRight();

    void handHolding$setHoldingHandsWithRight(PlayerEntity holdingHandsWithRight);

}
