package com.watermelon0117.mergeablechests.network;

import com.watermelon0117.mergeablechests.menu.BigChestMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class BigChestScrollPacket {
    private final int containerId;
    private final int rowOffset;

    public BigChestScrollPacket(int containerId, int rowOffset) {
        this.containerId = containerId;
        this.rowOffset = rowOffset;
    }

    public static void encode(BigChestScrollPacket packet, FriendlyByteBuf buffer) {
        buffer.writeVarInt(packet.containerId);
        buffer.writeVarInt(packet.rowOffset);
    }

    public static BigChestScrollPacket decode(FriendlyByteBuf buffer) {
        return new BigChestScrollPacket(buffer.readVarInt(), buffer.readVarInt());
    }

    public static void handle(BigChestScrollPacket packet, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player != null && player.containerMenu.containerId == packet.containerId
                    && player.containerMenu instanceof BigChestMenu bigChestMenu) {
                bigChestMenu.setScrollRowOffset(packet.rowOffset);
            }
        });
        context.setPacketHandled(true);
    }
}
