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

/**
 * Immutable representation of IPv4 and IPv6 address configuration for a Network Adapter on a Windows system.
 * <p>
 * Fields correspond to properties retrieved from the {@code MSFT_NetIPAddress} class in the
 * {@code root/StandardCimv2} namespace.
 * </p>
 * <p>
 * Together, with {@link MsftDnsClientServerAddress}, this class aims to be a
 * replacement for {@link Win32NetworkAdapterConfiguration}
 * </p>
 *
 * <p>See {@link MsftNetAdapter}, for network adapter information.</p>
 * <p>See {@link MsftDnsClientServerAddress}, for information regarding the connected DNS servers of a network adapter.</p>
 * <p>See {@link MsftNetConnectionProfile}, for information regarding the current profile of a network adapter.</p>
 *
 *
 * @see <a href="https://learn.microsoft.com/en-us/windows/win32/fwp/wmi/nettcpipprov/msft-netipaddress">MSFT_NetIPAddress Documentation</a>
 * @since 1.0.0
 */
@WmiClass(className = "MSFT_NetIPAddress")
@NullMarked
@Value.Immutable
@ImmutableEntityStyle
public abstract class AbstractMsftNetIpAddress {

    /**
     * Index of the network interface associated with this IP configuration.
     */
    @JsonProperty("InterfaceIndex")
    @Nullable
    public abstract Long interfaceIndex();

    /**
     * User-friendly name of the network interface.
     */
    @JsonProperty("InterfaceAlias")
    @Nullable
    public abstract String interfaceAlias();

    /**
     * Address family of the IP address.
     * <ul>
     *     <li>2 - IPv4</li>
     *     <li>23 - IPv6</li>
     * </ul>
     */
    @JsonProperty("AddressFamily")
    @Nullable
    public abstract Long addressFamily();

    /**
     * The IP address assigned to the interface (can be IPv4 or IPv6 or both).
     */
    @JsonProperty("IPAddress")
    @Nullable
    public abstract String ipAddress();

    /**
     * The IPv4 address assigned to the interface, if applicable.
     */
    @JsonProperty("IPv4Address")
    @Nullable
    public abstract String ipv4Address();

    /**
     * The IPv6 address assigned to the interface, if applicable.
     */
    @JsonProperty("IPv6Address")
    @Nullable
    public abstract String ipv6Address();

    /**
     * Type of IP address.
     * <ul>
     *     <li>1 - Unicast</li>
     *     <li>2 - Anycast</li>
     * </ul>
     */
    @JsonProperty("Type")
    @Nullable
    public abstract Integer type();

    /**
     * Source of the prefix for this IP Address.
     * <ul>
     *     <li>0 - Other</li>
     *     <li>1 - Manual</li>
     *     <li>2 - WellKnown</li>
     *     <li>3 - DHCP</li>
     *     <li>4 - RouterAdvertisement</li>
     * </ul>
     */
    @JsonProperty("PrefixOrigin")
    @Nullable
    public abstract Long prefixOrigin();

    /**
     * Source of the suffix for the IP address.
     * <ul>
     *     <li>0 - Other</li>
     *     <li>1 - Manual</li>
     *     <li>2 - WellKnown</li>
     *     <li>3 - DHCP</li>
     *     <li>4 - Link</li>
     *     <li>5 - Random</li>
     * </ul>
     */
    @JsonProperty("SuffixOrigin")
    @Nullable
    public abstract Long suffixOrigin();

    /**
     * Length of the network prefix, in bits.
     */
    @JsonProperty("PrefixLength")
    @Nullable
    public abstract Long prefixLength();

    /**
     * Lifetime during which the address is preferred for use.
     * The default value is infinite.
     */
    @JsonProperty("PreferredLifetime")
    @Nullable
    public abstract AbstractDatetime preferredLifetime();

    /**
     * Total lifetime during which the address is valid.
     * The default value is infinite.
     */
    @JsonProperty("ValidLifetime")
    @Nullable
    public abstract AbstractDatetime validLifeTime();

    /**
     * Prints the entity in a JSON pretty-print format
     *
     * @return the {@link String} value of the object in JSON pretty-print format
     */
    @Override
    public String toString() {
        return new ObjectMapper()
                .writerWithDefaultPrettyPrinter()
                .writeValueAsString(this);
    }

    /**
     * Lifetime over which the address is preferred. The default value is infinite.
     */
    @NullMarked
    @Value.Immutable
    @ImmutableEntityStyle
    public abstract static class AbstractDatetime {

        @JsonProperty("Days")
        @Nullable
        public abstract Long days();

        @JsonProperty("Hours")
        @Nullable
        public abstract Long hours();

        @JsonProperty("Minutes")
        @Nullable
        public abstract Long minutes();

        @JsonProperty("Seconds")
        @Nullable
        public abstract Long seconds();

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
}
