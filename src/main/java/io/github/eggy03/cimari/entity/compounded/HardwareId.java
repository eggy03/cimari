/*
 * SPDX-License-Identifier: MIT
 * SPDX-FileCopyrightText: 2025 The ferrumx-windows contributors
 * SPDX-FileCopyrightText: 2026 Cimari contributors
 */
package io.github.eggy03.cimari.entity.compounded;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.eggy03.cimari.annotation.ImmutableEntityStyle;
import org.immutables.value.Value;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.annotation.JsonDeserialize;
import tools.jackson.databind.annotation.JsonSerialize;

/**
 * Immutable representation of a hardware identity (HWID) in a Windows system.
 * <p>This entity represents a compound identifier derived from several
 * hardware components of a Windows system. It corresponds to the deserialized
 * output of the associated PowerShell script responsible for HWID generation.</p>
 *
 * <h2>Data sources</h2>
 * <ul>
 *     <li>{@code Win32_Processor.ProcessorID} — Unique identifier(s) for the system’s physical processor(s).</li>
 *     <li>{@code Win32_BIOS} — Includes the following properties:
 *         <ul>
 *             <li>{@code SMBIOSBIOSVersion}</li>
 *             <li>{@code SMBIOSMajorVersion}</li>
 *             <li>{@code SMBIOSMinorVersion}</li>
 *             <li>{@code SystemBiosMajorVersion}</li>
 *             <li>{@code SystemBiosMinorVersion}</li>
 *         </ul>
 *     </li>
 *     <li>{@code Win32_BaseBoard} — Includes the following properties:
 *         <ul>
 *             <li>{@code Manufacturer}</li>
 *             <li>{@code Model}</li>
 *             <li>{@code OtherIdentifyingInfo}</li>
 *             <li>{@code PartNumber}</li>
 *             <li>{@code SerialNumber}</li>
 *             <li>{@code SKU}</li>
 *             <li>{@code Version}</li>
 *             <li>{@code Product}</li>
 *         </ul>
 *     </li>
 * </ul>
 *
 * <h2>ID generation</h2>
 * <p>
 * The associated PowerShell script performs the following operations:
 * </p>
 * <ol>
 *   <li>Retrieves the combined IDs from the aforementioned classes</li>
 *   <li>Trims whitespaces and filters out <code>null</code> or empty values.</li>
 *   <li>Sorts all identifiers.</li>
 *   <li>Joins them using a <code>|</code> delimiter to form {@link #rawHWID}.</li>
 *   <li>Computes the SHA-256 hash of this concatenated string.</li>
 *   <li>Formats the hash into a <code>8-4-4-4-12-16-16</code> grouped representation stored in {@link #hashHWID}.</li>
 * </ol>
 *
 * <p>
 * While the generated HWID is designed to be unique per system, its uniqueness depends on the values provided by the
 * hardware manufacturers. In cases where components report generic or missing
 * information (e.g., baseboards using <code>"Default String"</code> as a serial number, <code>null</code> for
 * model or SKU, or placeholder versions like <code>"x.x"</code>), multiple systems may end up sharing the
 * same HWID. In such cases, the {@code Win32_Processor.ProcessorID} property becomes the source of uniqueness, provided
 * this value can be detected during runtime.
 * </p>
 *
 * @since 1.0.0
 */
@NullMarked
@Value.Immutable
@ImmutableEntityStyle
@JsonSerialize(as = ImmutableHardwareId.class)
@JsonDeserialize(as = ImmutableHardwareId.class)
public abstract class HardwareId {

    /**
     * Collection of IDs for several components, grouped together by a de-limiter.
     * <p>Should be used for debug purposes only</p>
     * <p>For production applications, use {@link #hashHWID}</p>
     * <p>Read the class level documentation to know more about how this information is collected</p>
     */
    @JsonProperty("HWIDRaw")
    @Nullable
    public abstract String rawHWID();

    /**
     * SHA-256 hash of the {@link #rawHWID}
     * <p>Read the class level documentation to know more about how this information is collected</p>
     */
    @JsonProperty("HWIDHash")
    @Nullable
    public abstract String hashHWID();

    /**
     * Retrieves the entity in a JSON pretty-print formatted string
     *
     * @return the {@link String} value of the object in JSON pretty-print format
     */
    public String toJson() {
        return new ObjectMapper()
                .writerWithDefaultPrettyPrinter()
                .writeValueAsString(this);
    }
}
