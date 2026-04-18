/*
 * SPDX-License-Identifier: MIT
 * SPDX-FileCopyrightText: 2025 The ferrumx-windows contributors
 * SPDX-FileCopyrightText: 2026 Cimari contributors
 */
package io.github.eggy03.cimari.entity.network;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.eggy03.cimari.annotation.ImmutableEntityStyle;
import io.github.eggy03.cimari.annotation.WmiClass;
import org.immutables.value.Value;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.annotation.JsonDeserialize;
import tools.jackson.databind.annotation.JsonSerialize;

import java.util.List;

/**
 * Immutable representation of a network adapter configuration on a Windows system.
 * <p>
 * Fields correspond to properties retrieved from the {@code Win32_NetworkAdapterConfiguration} WMI class.
 * </p>
 * <p>
 * See {@link Win32NetworkAdapter} for the corresponding adapter entity
 *
 * @see <a href="https://learn.microsoft.com/en-us/windows/win32/cimwin32prov/win32-networkadapterconfiguration">Win32_NetworkAdapterConfiguration Documentation</a>
 * @since 1.0.0
 */
@WmiClass(className = "Win32_NetworkAdapterConfiguration")
@NullMarked
@Value.Immutable
@ImmutableEntityStyle
@JsonSerialize(as = ImmutableWin32NetworkAdapterConfiguration.class)
@JsonDeserialize(as = ImmutableWin32NetworkAdapterConfiguration.class)
public abstract class Win32NetworkAdapterConfiguration {

    /**
     * Index number of the Windows network adapter configuration.
     * Used when multiple configurations exist.
     */
    @JsonProperty("Index")
    @Nullable
    public abstract Integer index();

    /**
     * Textual description of the network adapter configuration.
     */
    @JsonProperty("Description")
    @Nullable
    public abstract String description();

    /**
     * Short textual caption describing the object.
     */
    @JsonProperty("Caption")
    @Nullable
    public abstract String caption();

    /**
     * Unique identifier by which the configuration instance is known.
     */
    @JsonProperty("SettingID")
    @Nullable
    public abstract String settingId();

    /**
     * Indicates whether TCP/IP is bound and enabled on this adapter.
     */
    @JsonProperty("IPEnabled")
    @Nullable
    public abstract Boolean ipEnabled();

    /**
     * List of IP addresses associated with this network adapter.
     * May contain IPv4 and/or IPv6 addresses.
     */
    @JsonProperty("IPAddress")
    @Nullable
    public abstract List<@Nullable String> ipAddress();

    /**
     * Subnet masks associated with each IP address on this adapter.
     */
    @JsonProperty("IPSubnet")
    @Nullable
    public abstract List<@Nullable String> ipSubnet();

    /**
     * List of default gateway IP addresses used by this system.
     */
    @JsonProperty("DefaultIPGateway")
    @Nullable
    public abstract List<@Nullable String> defaultIpGateway();

    /**
     * Indicates whether DHCP is enabled for this adapter.
     */
    @JsonProperty("DHCPEnabled")
    @Nullable
    public abstract Boolean dhcpEnabled();

    /**
     * IP address of the DHCP server that assigned this configuration.
     */
    @JsonProperty("DHCPServer")
    @Nullable
    public abstract String dhcpServer();

    /**
     * Date and time when the DHCP lease was obtained.
     */
    @JsonProperty("DHCPLeaseObtained")
    @Nullable
    public abstract String dhcpLeaseObtained();

    /**
     * Date and time when the DHCP lease expires.
     */
    @JsonProperty("DHCPLeaseExpires")
    @Nullable
    public abstract String dhcpLeaseExpires();

    /**
     * Host name used to identify this computer on the network.
     */
    @JsonProperty("DNSHostName")
    @Nullable
    public abstract String dnsHostName();

    /**
     * List of DNS server IP addresses used for name resolution.
     */
    @JsonProperty("DNSServerSearchOrder")
    @Nullable
    public abstract List<@Nullable String> dnsServerSearchOrder();

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