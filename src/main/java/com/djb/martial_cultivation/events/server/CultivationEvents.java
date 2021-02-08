package com.djb.martial_cultivation.events.server;

import com.djb.martial_cultivation.Main;
import com.djb.martial_cultivation.capabilities.Cultivator;
import com.djb.martial_cultivation.network.messages.LoadCultivator;
import com.djb.martial_cultivation.network.messages.QiAmountChanged;
import com.djb.martial_cultivation.network.messages.SaveCultivator;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.PacketDistributor;

@Mod.EventBusSubscriber
public class CultivationEvents {
    private static int tickCount = 0;

    @SubscribeEvent
    public static void saveCultivator(PlayerEvent.PlayerLoggedOutEvent event) {
        PlayerEntity player = event.getPlayer();
        Cultivator cultivator = Cultivator.getCultivatorFrom(player);

        Main.NETWORK_CHANNEL.sendToServer(new SaveCultivator(player.getEntityId(), cultivator));
    }

    @SubscribeEvent
    public static void loadCultivator(PlayerEvent.PlayerLoggedInEvent event) {
        PlayerEntity player = event.getPlayer();
        Cultivator cultivator = Cultivator.getCultivatorFrom(player);

        Main.NETWORK_CHANNEL.send(
                PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player),
                new LoadCultivator(player.getEntityId(), cultivator));
    }

    @SubscribeEvent
    public static void regenerateQi(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.START && event.side == LogicalSide.SERVER) {
            tickCount += 1;

            if (tickCount % 20 == 0) {
                TryStoreQi(event.player);
            }
        }
    }

    private static void TryStoreQi(PlayerEntity player) {
        try {
                Cultivator cultivator = Cultivator.getCultivatorFrom(player);

                cultivator.regenerateQi();

                Main.NETWORK_CHANNEL.send(
                        PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player),
                        new QiAmountChanged(cultivator.getStoredQi(), player.getEntityId()));

                Main.LOGGER.debug("Storing qi for " + player.getScoreboardName() + ". Current qi:" + cultivator.getStoredQi());
        } catch (Exception e) {
            Main.LOGGER.warn("Error storing qi for " + player.getScoreboardName());
        }
    }
}
