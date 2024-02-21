package dev.technici4n.grandpower.api;

import com.google.common.primitives.Ints;
import dev.technici4n.grandpower.impl.GrandPowerImpl;
import dev.technici4n.grandpower.impl.NonLongWrapper;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.EntityCapability;
import net.neoforged.neoforge.capabilities.ItemCapability;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.energy.IEnergyStorage;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

/**
 * {@code long}-based energy storage capability,
 * full interoperable with the {@link IEnergyStorage} capability.
 */
public interface ILongEnergyStorage extends IEnergyStorage {
    /**
     * The standard block capability for {@link ILongEnergyStorage}.
     * Will seamlessly adapt to and from {@link Capabilities.EnergyStorage#BLOCK}.
     */
    BlockCapability<ILongEnergyStorage, @Nullable Direction> BLOCK = BlockCapability.createSided(
            GrandPowerImpl.id("long_energy_storage"), ILongEnergyStorage.class);

    /**
     * The standard entity capability for {@link ILongEnergyStorage}.
     * Will seamlessly adapt to and from {@link Capabilities.EnergyStorage#ENTITY}.
     */
    EntityCapability<ILongEnergyStorage, @Nullable Direction> ENTITY = EntityCapability.createSided(
            GrandPowerImpl.id("long_energy_storage"), ILongEnergyStorage.class);

    /**
     * The standard item capability for {@link ILongEnergyStorage}.
     * Will seamlessly adapt to and from {@link Capabilities.EnergyStorage#ITEM}.
     */
    ItemCapability<ILongEnergyStorage, Void> ITEM = ItemCapability.createVoid(
            GrandPowerImpl.id("long_energy_storage"), ILongEnergyStorage.class);

    /**
     * Wraps an existing {@link IEnergyStorage} into an {@link ILongEnergyStorage}.
     */
    @Contract("null -> null;!null -> !null")
    static @Nullable ILongEnergyStorage of(@Nullable IEnergyStorage energyStorage) {
        return energyStorage == null ? null : new NonLongWrapper(energyStorage);
    }

    /**
     * Receives some energy.
     *
     * @param maxReceive the maximum amount to receive
     * @param simulate {@code true} to simulate, {@code false} to actually perform the operation
     * @return the amount of energy received
     */
    long receive(long maxReceive, boolean simulate);

    /**
     * Extracts some energy.
     *
     * @param maxExtract the maximum amount to extract
     * @param simulate {@code true} to simulate, {@code false} to actually perform the operation
     * @return the amount of energy extracted
     */
    long extract(long maxExtract, boolean simulate);

    /**
     * Returns the amount of energy that this storage currently holds.
     */
    long getAmount();

    /**
     * Returns the maximum amount of energy that this storage can hold.
     */
    long getCapacity();

    // Default implementations below, do not override!

    @Override
    @ApiStatus.NonExtendable
    @Deprecated // don't call this method on an ILongEnergyStorage
    default int receiveEnergy(int maxReceive, boolean simulate) {
        return (int) receive(maxReceive, simulate);
    }

    @Override
    @ApiStatus.NonExtendable
    @Deprecated // don't call this method on an ILongEnergyStorage
    default int extractEnergy(int maxExtract, boolean simulate) {
        return (int) extract(maxExtract, simulate);
    }

    @Override
    @ApiStatus.NonExtendable
    @Deprecated // don't call this method on an ILongEnergyStorage
    default int getEnergyStored() {
        return Ints.saturatedCast(getAmount());
    }

    @Override
    @ApiStatus.NonExtendable
    @Deprecated // don't call this method on an ILongEnergyStorage
    default int getMaxEnergyStored() {
        return Ints.saturatedCast(getCapacity());
    }
}
