/*
 * SPDX-License-Identifier: MIT
 * SPDX-FileCopyrightText: 2025 The ferrumx-windows contributors
 * SPDX-FileCopyrightText: 2026 Cimari contributors
 */
package io.github.eggy03.cimari.entity.network;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.eggy03.cimari.annotation.ImmutableEntityStyle;
import io.github.eggy03.cimari.annotation.WmiClass;
import io.github.eggy03.cimari.shell.query.Cimv2;
import org.immutables.value.Value;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import tools.jackson.databind.ObjectMapper;

/**
 * Immutable representation of the association between a {@link Win32NetworkAdapter} and it's {@link Win32NetworkAdapterConfiguration}.
 * <p>
 * Fields <b>indirectly</b> correspond to properties retrieved from the {@code Win32_NetworkAdapterSetting} WMI class
 * and represent an association between {@code Win32_NetworkAdapter} and {@code Win32_NetworkAdapterConfiguration}.
 * </p>
 * <p>Links {@link Win32NetworkAdapter} with {@link Win32NetworkAdapterConfiguration} via their device IDs and indexes respectively</p>
 *
 * <p>This class has the following two fields:</p>
 * <ul>
 *     <li>{@code networkAdapterDeviceId} - contains the value of the {@code deviceId} field of {@link Win32NetworkAdapter}</li>
 *     <li>{@code networkAdapterConfigurationIndex} - contains the value of the {@code index} field of {@link Win32NetworkAdapterConfiguration}</li>
 * </ul>
 *
 * <p>
 *     <b>Extra Notes:</b> The {@code Win32_NetworkAdapterSetting} WMI class itself does not directly expose
 *     the {@code DeviceID} (from {@code Win32_NetworkAdapter}) or the {@code Index}
 *     (from {@code Win32_NetworkAdapterConfiguration}) as standalone properties.
 *     Instead, these values are nested within its references: {@code Element} and {@code Setting}, respectively.
 * </p>
 * <p>
 *     To simplify data mapping, the PowerShell query defined in
 *     {@link Cimv2#WIN32_NETWORK_ADAPTER_SETTING} constructs a custom {@code PSObject}
 *     that maps {@code Element.DeviceID} to {@code networkAdapterDeviceId} and {@code Setting.Index} to {@code networkAdapterConfigurationIndex}
 *     and the resulting JSON returned is deserialized into this entity class.
 * </p>
 *
 * <p>See {@link Win32NetworkAdapter} for adapter info.</p>
 * <p>See {@link Win32NetworkAdapterConfiguration} for related adapter config info.</p>
 *
 *
 * @see <a href="https://learn.microsoft.com/en-us/windows/win32/cimwin32prov/win32-networkadaptersetting">Win32_NetworkAdapterSetting Documentation</a>
 * @since 1.0.0
 */
@WmiClass(className = "Win32_NetworkAdapterSetting")
@NullMarked
@Value.Immutable
@ImmutableEntityStyle
public abstract class AbstractWin32NetworkAdapterSetting {

    /**
     * The {@code deviceId} field value of {@link Win32NetworkAdapter}
     */
    @JsonProperty("NetworkAdapterDeviceID")
    @Nullable
    public abstract String networkAdapterDeviceId();

    /**
     * The {@code index} field value of {@link Win32NetworkAdapterConfiguration}
     */
    @JsonProperty("NetworkAdapterConfigurationIndex")
    @Nullable
    public abstract Integer networkAdapterConfigurationIndex();

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
