package com.djb.martial_cultivation.containers;

import com.djb.martial_cultivation.Registrar;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.world.World;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;

public class ModContainers {
    public static final RegistryObject<ContainerType<Container>> SKILL_CONTAINER =
            Registrar.CONTAINERS.register(SkillContainer.name, () -> IForgeContainerType.create((windowId, inv, data) -> {
                World world = inv.player.getEntityWorld();
                return new SkillContainer(windowId, world, inv, inv.player);
            }));
}
