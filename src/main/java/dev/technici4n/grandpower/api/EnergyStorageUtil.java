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
package dev.technici4n.grandpower.api;

import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

/**
 * Helper methods to work with {@link ILongEnergyStorage}s.
 */
public final class EnergyStorageUtil {
    private EnergyStorageUtil() {}

    private static final Logger LOGGER = LogUtils.getLogger();

    /**
     * Moves energy from one storage to another.
     *
     * @param source storage that energy is taken from
     * @param target storage that receives energy
     * @param maxAmount the maximum amount of energy to move
     * @return how much energy was moved
     */
    public static long move(ILongEnergyStorage source, ILongEnergyStorage target, long maxAmount) {
        long simulatedExtract = source.extract(maxAmount, true);
        long simulatedInsert = target.receive(simulatedExtract, true);

        long extractedAmount = source.extract(simulatedInsert, false);
        long insertedAmount = target.receive(extractedAmount, false);

        if (insertedAmount < extractedAmount) {
            // Try to give the remainder back
            long leftover = source.receive(extractedAmount - insertedAmount, false);
            if (leftover > 0) {
                LOGGER.error("Energy storage {} did not accept {} leftover energy from {}! Voiding it.", source, leftover, target);
            }
        }

        return insertedAmount;
    }
}
