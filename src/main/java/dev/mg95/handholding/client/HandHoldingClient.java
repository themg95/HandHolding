package dev.mg95.handholding.client;

import dev.mg95.handholding.HandHolding;
import dev.mg95.handholding.PlayerEntityMixinAccess;
import dev.mg95.handholding.networking.ToggleHandHoldPayload;
import dev.mg95.handholding.networking.SyncHandHoldPayload;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.player.PlayerEntity;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.UUID;

public class HandHoldingClient implements ClientModInitializer {
    private static KeyBinding handHoldKeybindLeft;
    private static KeyBinding handHoldKeybindRight;
    private ArrayList<Runnable> worldLoadQueue = new ArrayList<>();

    @Override
    public void onInitializeClient() {
        networking();

        handHoldKeybindLeft = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                HandHolding.NAMESPACE + ".hold_hand_left",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_G,
                HandHolding.NAMESPACE + ".category"
        ));

        handHoldKeybindRight = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                HandHolding.NAMESPACE + ".hold_hand_right",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_H,
                HandHolding.NAMESPACE + ".category"
        ));
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            assert client.player != null;

            while (handHoldKeybindLeft.wasPressed()) {
                var holdee = ((PlayerEntityMixinAccess) client.player).handHolding$getHoldingHandsWithLeft();
                if (holdee != null) {
                    ClientPlayNetworking.send(new ToggleHandHoldPayload(holdee.getUuidAsString(), true));
                    return;
                }

                if (client.targetedEntity == null || !client.targetedEntity.isPlayer()) return;
                ClientPlayNetworking.send(new ToggleHandHoldPayload(client.targetedEntity.getUuidAsString(), true));
            }
            while (handHoldKeybindRight.wasPressed()) {
                var holdee = ((PlayerEntityMixinAccess) client.player).handHolding$getHoldingHandsWithRight();
                if (holdee != null) {
                    ClientPlayNetworking.send(new ToggleHandHoldPayload(holdee.getUuidAsString(), false));
                    return;
                }

                if (client.targetedEntity == null || !client.targetedEntity.isPlayer()) return;
                ClientPlayNetworking.send(new ToggleHandHoldPayload(client.targetedEntity.getUuidAsString(), false));
            }

            if (client.world != null && client.world.getPlayers().size() > 1 && !worldLoadQueue.isEmpty()) {
                var runnable = worldLoadQueue.getFirst();
                runnable.run();
                worldLoadQueue.remove(runnable);
            }
        });
    }

    private void networking() {
        ClientPlayNetworking.registerGlobalReceiver(SyncHandHoldPayload.ID, (payload, context) -> {
            if (context.client().world.getPlayers().size() <= 1) {
                Runnable runnable = () -> parsePacket(context, payload);
                worldLoadQueue.add(runnable);
                return;
            }

            parsePacket(context, payload);
        });
    }

    private void parsePacket(ClientPlayNetworking.Context context, SyncHandHoldPayload payload) {
        PlayerEntity holder = context.client().world.getPlayerByUuid(UUID.fromString(payload.holderUuid()));
        var holdee = context.client().world.getPlayerByUuid(UUID.fromString(payload.holdeeUuid()));
        var access = ((PlayerEntityMixinAccess) holder);
        if (payload.leftHand()) {
            if (!payload.holding())
                access.handHolding$setHoldingHandsWithLeft(null);
            else access.handHolding$setHoldingHandsWithLeft(holdee);
        } else {
            if (!payload.holding())
                access.handHolding$setHoldingHandsWithRight(null);
            else access.handHolding$setHoldingHandsWithRight(holdee);
        }

    }

}
