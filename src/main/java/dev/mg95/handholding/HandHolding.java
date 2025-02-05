package dev.mg95.handholding;

import dev.mg95.handholding.networking.ToggleHandHoldPayload;
import dev.mg95.handholding.networking.SyncHandHoldPayload;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.ArrayList;
import java.util.UUID;

public class HandHolding implements ModInitializer {
    public static final String NAMESPACE = "handholding";
    public static ArrayList<HandHold> handHoldings = new ArrayList<>();

    @Override
    public void onInitialize() {
        PayloadTypeRegistry.playC2S().register(ToggleHandHoldPayload.ID, ToggleHandHoldPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(SyncHandHoldPayload.ID, SyncHandHoldPayload.CODEC);

        ServerPlayNetworking.registerGlobalReceiver(ToggleHandHoldPayload.ID, (payload, context) -> {
            var holdee = context.server().getPlayerManager().getPlayer(UUID.fromString(payload.uuid()));

            if (payload.leftHand()) {
                var handHold = new HandHold(context.player(), holdee, true);
                if (handHoldings.contains(handHold)) {
                    handHoldings.remove(handHold);
                    syncHandHoldPayloadToEveryone(context.server().getPlayerManager(), context.player().getUuidAsString(), payload.uuid(), true, false);
                } else {
                    handHoldings.add(handHold);
                    syncHandHoldPayloadToEveryone(context.server().getPlayerManager(), context.player().getUuidAsString(), payload.uuid(), true, true);
                }
            } else {
                var handHold = new HandHold(context.player(), holdee, false);
                if (handHoldings.contains(handHold)) {
                    handHoldings.remove(handHold);
                    syncHandHoldPayloadToEveryone(context.server().getPlayerManager(), context.player().getUuidAsString(), payload.uuid(), false, false);
                } else {
                    handHoldings.add(handHold);
                    syncHandHoldPayloadToEveryone(context.server().getPlayerManager(), context.player().getUuidAsString(), payload.uuid(), false, true);
                }
            }
        });

        ServerPlayConnectionEvents.JOIN.register(((handler, sender, server) -> {
            for (var handHold : handHoldings) {
                syncHandHoldPayload(handler.player, handHold.holder, handHold.holdee, handHold.leftHand, true);
            }
        }));

        ServerPlayConnectionEvents.DISCONNECT.register(((handler, server) -> {
            handHoldings.removeIf(handHold -> handHold.holder.equals(handler.player) || handHold.holdee.equals(handler.player));
        }));
    }

    public static void syncHandHoldPayload(ServerPlayerEntity receiver, ServerPlayerEntity holder, ServerPlayerEntity holdee, boolean leftHand, boolean holding) {
        syncHandHoldPayload(receiver, holder.getUuidAsString(), holdee.getUuidAsString(), leftHand, holding);
    }

    public static void syncHandHoldPayloadToEveryone(PlayerManager playerManager, String holder, String holdee, boolean leftHand, boolean holding) {
        for (var player : playerManager.getPlayerList()) {
            syncHandHoldPayload(player, holder, holdee, leftHand, holding);
        }
    }

    public static void syncHandHoldPayload(ServerPlayerEntity receiver, String holder, String holdee, boolean leftHand, boolean holding) {
        ServerPlayNetworking.send(receiver, new SyncHandHoldPayload(holder, holdee, leftHand, holding));
    }
}
