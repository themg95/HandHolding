package dev.mg95.handholding.networking;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

public record ToggleHandHoldPayload(String uuid, boolean leftHand) implements CustomPayload {
    public static final CustomPayload.Id<ToggleHandHoldPayload> ID = new CustomPayload.Id<>(PacketConstants.REQUEST_HAND_HOLD_PACKET_ID);
    public static final PacketCodec<ByteBuf, ToggleHandHoldPayload> CODEC = PacketCodec.tuple(PacketCodecs.STRING, ToggleHandHoldPayload::uuid, PacketCodecs.BOOL, ToggleHandHoldPayload::leftHand, ToggleHandHoldPayload::new);

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }
}