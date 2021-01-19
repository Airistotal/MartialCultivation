package com.djb.martial_cultivation.data.network.messages;

import com.djb.martial_cultivation.Main;
import com.djb.martial_cultivation.capabilities.Cultivator;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.io.*;
import java.util.Base64;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class QiAmountChanged implements Serializable {
    private final static long serialVersionUID = 1;

    public int qiAmount;
    public int playerId;

    public QiAmountChanged(int qiAmount, int playerId)
    {
        this.qiAmount = qiAmount;
        this.playerId = playerId;
    }

    public static BiConsumer<QiAmountChanged, PacketBuffer> getQiAmountChangedEncoder() {
        return (qiAmountChanged, packetBuffer) -> {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            String objectString = "";

            try {
                ObjectOutputStream oos = new ObjectOutputStream(baos);
                oos.writeObject(qiAmountChanged);
                oos.close();

                objectString = Base64.getEncoder().encodeToString(baos.toByteArray());
            } catch (IOException e) {
                e.printStackTrace();
            }

            packetBuffer.writeString(objectString);
        };
    }

    public static Function<PacketBuffer, QiAmountChanged> getQiAmountChangedDecoder() {
        return packetBuffer -> {
            String objectString = packetBuffer.readString();

            byte [] data = Base64.getDecoder().decode(objectString);
            try {
                ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
                Object o = ois.readObject();
                ois.close();

                return (QiAmountChanged)o;
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }

            return null;
        };
    }

    public static void handleMessage(QiAmountChanged message, Supplier<NetworkEvent.Context> ctx) {
        PlayerEntity player = Minecraft.getInstance().player;

        ctx.get().enqueueWork(() -> TrySetQiForPlayer(player, message, ctx));

        ctx.get().setPacketHandled(true);
    }

    private static void TrySetQiForPlayer(PlayerEntity player, QiAmountChanged message, Supplier<NetworkEvent.Context> ctx) {
        try {
            if(player.getEntityId() == message.playerId) {
                Cultivator cultivator = Cultivator.getCultivatorFrom(player);

                cultivator.setQi(message.qiAmount);

                Main.LOGGER.debug("Setting qi for " + player.getScoreboardName() + " to " + message.qiAmount);
            }
        } catch (Exception e) {
            Main.LOGGER.debug("Error setting qi sent by " + ctx.get().toString() +
                              " to " + message.qiAmount + " for " + player.getScoreboardName());
        }
    }
}
