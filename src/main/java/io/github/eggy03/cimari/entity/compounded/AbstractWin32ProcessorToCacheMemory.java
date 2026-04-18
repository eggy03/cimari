/*
 * SPDX-License-Identifier: MIT
 * SPDX-FileCopyrightText: 2025 The ferrumx-windows contributors
 * SPDX-FileCopyrightText: 2026 Cimari contributors
 */
package io.github.eggy03.cimari.entity.compounded;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.eggy03.cimari.annotation.ImmutableEntityStyle;
import io.github.eggy03.cimari.entity.processor.AbstractWin32CacheMemory;
import io.github.eggy03.cimari.entity.processor.AbstractWin32Processor;
import io.github.eggy03.cimari.entity.processor.Win32AssociatedProcessorMemory;
import io.github.eggy03.cimari.entity.processor.Win32CacheMemory;
import io.github.eggy03.cimari.entity.processor.Win32Processor;
import org.immutables.value.Value;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

/**
 * Immutable representation of a {@link Win32Processor} and its associated
 * {@code 1:N} relationship with {@link Win32CacheMemory} in a Windows system.
 * <p>
 * Each instance represents a single processor identified by {@link #deviceId},
 * and maintains a one-to-many mapping with its corresponding cache memory objects
 * (such as L1, L2, and L3 caches).
 * </p>
 *
 * <p>
 * This class is purely a convenience class designed to eliminate the
 * need for using {@link Win32AssociatedProcessorMemory} when fetching a
 * relation between {@link Win32Processor} and {@link Win32CacheMemory} as it stores
 * all instances of cache memories for a particular processor.
 * </p>
 *
 * @see Win32Processor
 * @see Win32CacheMemory
 * @since 1.0.0
 */
@NullMarked
@Value.Immutable
@ImmutableEntityStyle
public abstract class AbstractWin32ProcessorToCacheMemory {

    /**
     * The unique identifier for the {@link Win32Processor} instance.
     * <p>
     * Corresponds to the processor’s {@code DeviceID}, such as {@code "CPU0"} or {@code "CPU1"}.
     * </p>
     */
    @JsonProperty("DeviceID")
    @Nullable
    public abstract String deviceId();

    /**
     * The {@link Win32Processor} entity associated with the {@link #deviceId}.
     * <p>
     * Represents the physical or logical processor containing one or more cache memory units.
     * </p>
     */
    @JsonProperty("Processor")
    @Nullable
    public abstract AbstractWin32Processor processor();

    /**
     * A list of {@link Win32CacheMemory} entities associated with the {@link #deviceId}.
     * <p>
     * Represents the processor’s cache hierarchy (such as L1, L2, and L3 cache levels)
     * linked to the specified {@link #processor}.
     * </p>
     */
    @JsonProperty("CacheMemory")
    @Nullable
    public abstract List<@Nullable AbstractWin32CacheMemory> cacheMemoryList();

    /**
     * Retrieves the entity in a JSON pretty-print formatted string
     *
     * @return the {@link String} value of the object in JSON pretty-print format
     */
    @Override
    public String toString() {
        return new ObjectMapper()
                .writerWithDefaultPrettyPrinter()
                .writeValueAsString(this);
    }
}