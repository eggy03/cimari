/*
 * SPDX-License-Identifier: MIT
 * SPDX-FileCopyrightText: 2025 The ferrumx-windows contributors
 * SPDX-FileCopyrightText: 2026 Cimari contributors
 */
package io.github.eggy03.cimari.entity.peripheral;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.eggy03.cimari.annotation.ImmutableEntityStyle;
import io.github.eggy03.cimari.annotation.WmiClass;
import org.immutables.value.Value;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

/**
 * Immutable representation of a battery device on a Windows system.
 * <p>
 * Fields correspond to properties retrieved from the {@code Win32_Battery} WMI class.
 * </p>
 *
 * @see <a href="https://learn.microsoft.com/en-us/windows/win32/cimwin32prov/win32-battery">Win32_Battery Documentation</a>
 * @since 1.0.0
 */
@WmiClass(className = "Win32_Battery")
@NullMarked
@Value.Immutable
@ImmutableEntityStyle
public abstract class AbstractWin32Battery {

    /**
     * Identifier of the battery.
     */
    @JsonProperty("DeviceID")
    @Nullable
    public abstract String deviceId();

    /**
     * Short, one-line description of the battery object.
     */
    @JsonProperty("Caption")
    @Nullable
    public abstract String caption();

    /**
     * Description of the battery.
     */
    @JsonProperty("Description")
    @Nullable
    public abstract String description();

    /**
     * Label by which the battery is known.
     */
    @JsonProperty("Name")
    @Nullable
    public abstract String name();

    /**
     * Current operational status of the battery device.
     * <p>Possible OPERATIONAL values:</p>
     * <ul>
     *   <li>"OK"</li>
     *   <li>"Degraded"</li>
     *   <li>"Pred Fail"</li>
     * </ul>
     * <p>Possible NON-OPERATIONAL values:</p>
     * <ul>
     *   <li>"Unknown"</li>
     *   <li>"Error"</li>
     *   <li>"Starting"</li>
     *   <li>"Stopping"</li>
     *   <li>"Service"</li>
     * </ul>
     * <p>Possible OTHER values:</p>
     * <ul>
     *   <li>"Stressed"</li>
     *   <li>"NonRecover"</li>
     *   <li>"No Contact"</li>
     *   <li>"Lost Comm"</li>
     * </ul>
     */
    @JsonProperty("Status")
    @Nullable
    public abstract String status();

    /**
     * Array of specific power-related capabilities supported by the battery.
     * <p>Possible values:</p>
     * <ul>
     *   <li>0 - Unknown</li>
     *   <li>1 - Not Supported</li>
     *   <li>2 - Disabled</li>
     *   <li>3 - Enabled</li>
     *   <li>4 - Power Saving Modes Entered Automatically</li>
     *   <li>5 - Power State Settable</li>
     *   <li>6 - Power Cycling Supported</li>
     *   <li>7 - Timed Power On Supported</li>
     * </ul>
     */
    @JsonProperty("PowerManagementCapabilities")
    @Nullable
    public abstract List<@Nullable Integer> powerManagementCapabilities();

    /**
     * Indicates whether the battery can be power-managed.
     */
    @JsonProperty("PowerManagementSupported")
    @Nullable
    public abstract Boolean powerManagementSupported();
    /**
     * Status of the battery.
     * <p>Possible values:</p>
     * <ul>
     *   <li>1 - Discharging</li>
     *   <li>2 - AC present, not charging</li>
     *   <li>3 - Fully Charged</li>
     *   <li>4 - Low</li>
     *   <li>5 - Critical</li>
     *   <li>6 - Charging</li>
     *   <li>7 - Charging and High</li>
     *   <li>8 - Charging and Low</li>
     *   <li>9 - Charging and Critical</li>
     *   <li>10 - Undefined</li>
     *   <li>11 - Partially Charged</li>
     * </ul>
     */
    @JsonProperty("BatteryStatus")
    @Nullable
    public abstract Integer batteryStatus();
    /**
     * Type of battery chemistry.
     * <p>Possible values:</p>
     * <ul>
     *   <li>1 - Other</li>
     *   <li>2 - Unknown</li>
     *   <li>3 - Lead Acid</li>
     *   <li>4 - Nickel Cadmium</li>
     *   <li>5 - Nickel Metal Hydride</li>
     *   <li>6 - Lithium-ion</li>
     *   <li>7 - Zinc Air</li>
     *   <li>8 - Lithium Polymer</li>
     * </ul>
     */
    @JsonProperty("Chemistry")
    @Nullable
    public abstract Integer chemistry();
    /**
     * Design capacity of the battery in milliwatt-hours.
     */
    @JsonProperty("DesignCapacity")
    @Nullable
    public abstract Integer designCapacity();
    /**
     * Design voltage of the battery in millivolts.
     */
    @JsonProperty("DesignVoltage")
    @Nullable
    public abstract Integer designVoltage();
    /**
     * Estimated percentage of full charge remaining.
     */
    @JsonProperty("EstimatedChargeRemaining")
    @Nullable
    public abstract Long estimatedChargeRemaining();
    /**
     * Estimated remaining runtime of the battery in minutes.
     */
    @JsonProperty("EstimatedRunTime")
    @Nullable
    public abstract Long estimatedRunTime();

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