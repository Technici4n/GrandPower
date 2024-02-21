package dev.technici4n.grandpower.impl;

import dev.technici4n.grandpower.api.ILongEnergyStorage;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.ICapabilityProvider;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.energy.IEnergyStorage;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

@ApiStatus.Internal
@Mod(GrandPowerImpl.MOD_ID)
public class GrandPowerImpl {
    public static final String MOD_ID = "grandpower";

    public static ResourceLocation id(String path) {
        return new ResourceLocation(MOD_ID, path);
    }

    public GrandPowerImpl(IEventBus modBus) {
        modBus.addListener(GrandPowerImpl::initCapabilities);
    }

    private static void initCapabilities(RegisterCapabilitiesEvent event) {
        registerBlockWrapper(event);
        registerEntityWrapper(event);
        registerItemWrapper(event);
    }

    private static void registerBlockWrapper(RegisterCapabilitiesEvent event) {
        // Block capability
        var allBlocks = BuiltInRegistries.BLOCK.stream().toArray(Block[]::new);
        ThreadLocal<Boolean> inBlockCompat = ThreadLocal.withInitial(() -> false);

        event.registerBlock(ILongEnergyStorage.BLOCK, (level, pos, state, be, direction) -> {
            if (inBlockCompat.get()) {
                return null;
            }

            inBlockCompat.set(Boolean.TRUE);
            try {
                return ILongEnergyStorage.of(level.getCapability(Capabilities.EnergyStorage.BLOCK, pos, state, be, direction));
            } finally {
                inBlockCompat.set(Boolean.FALSE);
            }
        }, allBlocks);

        event.registerBlock(Capabilities.EnergyStorage.BLOCK, (level, pos, state, be, direction) -> {
            if (inBlockCompat.get()) {
                return null;
            }

            inBlockCompat.set(Boolean.TRUE);
            try {
                return level.getCapability(ILongEnergyStorage.BLOCK, pos, state, be, direction);
            } finally {
                inBlockCompat.set(Boolean.FALSE);
            }
        }, allBlocks);
    }

    private static void registerEntityWrapper(RegisterCapabilitiesEvent event) {
        // Entity capability
        var allEntities = BuiltInRegistries.ENTITY_TYPE.stream().toArray(EntityType[]::new);
        ThreadLocal<Boolean> inEntityCompat = ThreadLocal.withInitial(() -> false);

        ICapabilityProvider<Entity, @Nullable Direction, ILongEnergyStorage> fromForgeEnergy = (entity, direction) -> {
            if (inEntityCompat.get()) {
                return null;
            }

            inEntityCompat.set(Boolean.TRUE);
            try {
                return ILongEnergyStorage.of(entity.getCapability(Capabilities.EnergyStorage.ENTITY, direction));
            } finally {
                inEntityCompat.set(Boolean.FALSE);
            }
        };

        ICapabilityProvider<Entity, @Nullable Direction, IEnergyStorage> toForgeEnergy = (entity, direction) -> {
            if (inEntityCompat.get()) {
                return null;
            }

            inEntityCompat.set(Boolean.TRUE);
            try {
                return entity.getCapability(ILongEnergyStorage.ENTITY, direction);
            } finally {
                inEntityCompat.set(Boolean.FALSE);
            }
        };

        for (var entityType : BuiltInRegistries.ENTITY_TYPE) {
            event.registerEntity(ILongEnergyStorage.ENTITY, entityType, fromForgeEnergy);
            event.registerEntity(Capabilities.EnergyStorage.ENTITY, entityType, toForgeEnergy);
        }
    }

    private static void registerItemWrapper(RegisterCapabilitiesEvent event) {
        // Item capability
        var allItems = BuiltInRegistries.ITEM.stream().toArray(Item[]::new);
        ThreadLocal<Boolean> inItemCompat = ThreadLocal.withInitial(() -> false);

        event.registerItem(ILongEnergyStorage.ITEM, (stack, ignored) -> {
            if (inItemCompat.get()) {
                return null;
            }

            inItemCompat.set(Boolean.TRUE);
            try {
                return ILongEnergyStorage.of(stack.getCapability(Capabilities.EnergyStorage.ITEM));
            } finally {
                inItemCompat.set(Boolean.FALSE);
            }
        }, allItems);

        event.registerItem(Capabilities.EnergyStorage.ITEM, (stack, ignored) -> {
            if (inItemCompat.get()) {
                return null;
            }

            inItemCompat.set(Boolean.TRUE);
            try {
                return stack.getCapability(ILongEnergyStorage.ITEM);
            } finally {
                inItemCompat.set(Boolean.FALSE);
            }
        }, allItems);
    }
}
