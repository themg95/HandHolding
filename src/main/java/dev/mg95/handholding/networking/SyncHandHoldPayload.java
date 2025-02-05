package dev.mg95.handholding.networking;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

public record SyncHandHoldPayload(String holderUuid, String holdeeUuid, boolean leftHand, boolean holding) implements CustomPayload {
    public static final Id<SyncHandHoldPayload> ID = new Id<>(PacketConstants.SYNC_HAND_HOLD_PACKET_ID);
    public static final PacketCodec<ByteBuf, SyncHandHoldPayload> CODEC = PacketCodec.tuple(PacketCodecs.STRING, SyncHandHoldPayload::holderUuid, PacketCodecs.STRING, SyncHandHoldPayload::holdeeUuid, PacketCodecs.BOOL, SyncHandHoldPayload::leftHand, PacketCodecs.BOOL, SyncHandHoldPayload::holding, SyncHandHoldPayload::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

}