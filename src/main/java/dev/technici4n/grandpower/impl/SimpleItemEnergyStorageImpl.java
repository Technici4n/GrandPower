/*
 * MIT License
 *
 * Copyright (c) 2020 Azercoco & Technici4n
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package dev.technici4n.grandpower.impl;

import dev.technici4n.grandpower.api.ILongEnergyStorage;
import dev.technici4n.grandpower.api.ISimpleEnergyItem;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.ApiStatus;

/**
 * Do not use this class directly,
 * obtain instances from {@link ISimpleEnergyItem#createStorage(ItemStack, DataComponentType, long, long, long)}.
 */
// TODO unclear if we should account for stack size or not
@ApiStatus.Internal
public final class SimpleItemEnergyStorageImpl implements ILongEnergyStorage {
    private final ItemStack stack;
    private final DataComponentType<Long> component;
    private final long capacity;
    private final long maxInsert, maxExtract;

    public SimpleItemEnergyStorageImpl(ItemStack stack, DataComponentType<Long> component, long capacity, long maxInsert, long maxExtract) {
        this.stack = stack;
        this.component = component;
        this.capacity = capacity;
        this.maxInsert = maxInsert;
        this.maxExtract = maxExtract;
    }

    @Override
    public long receive(long maxReceive, boolean simulate) {
        long count = stack.getCount();

        long maxAmountPerCount = maxReceive / count;
        long currentAmountPerCount = getAmount() / count;
        long insertedPerCount = Math.min(maxInsert, Math.min(maxAmountPerCount, capacity - currentAmountPerCount));

        if (insertedPerCount > 0) {
            if (!simulate) {
                ISimpleEnergyItem.setStoredEnergyUnchecked(stack, component, currentAmountPerCount + insertedPerCount);
            }
            return insertedPerCount * count;
        }

        return 0;
    }

    @Override
    public long extract(long maxExtract, boolean simulate) {
        long count = stack.getCount();

        long maxAmountPerCount = maxExtract / count;
        long currentAmountPerCount = getAmount() / count;
        long extractedPerCount = Math.min(maxExtract, Math.min(maxAmountPerCount, currentAmountPerCount));

        if (extractedPerCount > 0) {
            if (!simulate) {
                ISimpleEnergyItem.setStoredEnergyUnchecked(stack, component, currentAmountPerCount - extractedPerCount);
            }
            return extractedPerCount * count;
        }

        return 0;
    }

    @Override
    public long getAmount() {
        return stack.getCount() * ISimpleEnergyItem.getStoredEnergyUnchecked(stack, component);
    }

    @Override
    public long getCapacity() {
        return capacity;
    }

    @Override
    public boolean canExtract() {
        return maxExtract > 0;
    }

    @Override
    public boolean canReceive() {
        return maxInsert > 0;
    }
}
