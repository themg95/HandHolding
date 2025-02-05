package dev.mg95.handholding;

import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Objects;

public class HandHold {
    public ServerPlayerEntity holder;
    public ServerPlayerEntity holdee;
    public boolean leftHand;

    public HandHold(ServerPlayerEntity holder, ServerPlayerEntity holdee, boolean leftHand) {
        this.holder = holder;
        this.holdee = holdee;
        this.leftHand = leftHand;
    }

    @Override
    public int hashCode() {
        return (holder.getUuidAsString() + ":" + holdee.getUuidAsString() + ":" + leftHand).hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof HandHold handHold)) return false;
        return leftHand == handHold.leftHand && Objects.equals(holder, handHold.holder) && Objects.equals(holdee, handHold.holdee);
    }
}
