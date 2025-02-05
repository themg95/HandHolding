package dev.mg95.handholding.networking;

import net.minecraft.util.Identifier;

import static dev.mg95.handholding.HandHolding.NAMESPACE;

public class PacketConstants {
    public static final Identifier REQUEST_HAND_HOLD_PACKET_ID = Identifier.of(NAMESPACE, "request_hand_hold");
    public static final Identifier SYNC_HAND_HOLD_PACKET_ID = Identifier.of(NAMESPACE, "sync_hand_hold");
}
