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

/**
 * Immutable representation of a connection profile for a particular network adapter on a Windows system.
 * <p>
 * Fields correspond to properties retrieved from the {@code MSFT_NetConnectionProfile} class in the
 * {@code root/StandardCimv2} namespace.
 * </p>
 *
 * <p>See {@link MsftNetAdapter}, for network adapter information.</p>
 * <p>See {@link MsftDnsClientServerAddress}, for information regarding the connected DNS servers of a network adapter.</p>
 * <p>See {@link MsftNetIpAddress}, for IP address configuration information of a network adapter.</p>
 *
 * @see <a href="https://wutils.com/wmi/root/standardcimv2/msft_netconnectionprofile/">MSFT_NetConnectionProfile Documentation</a>
 * @since 1.0.0
 */
@WmiClass(className = "MSFT_NetConnectionProfile")
@NullMarked
@Value.Immutable
@ImmutableEntityStyle
@JsonSerialize(as = ImmutableMsftNetConnectionProfile.class)
@JsonDeserialize(as = ImmutableMsftNetConnectionProfile.class)
public abstract class MsftNetConnectionProfile {

    /**
     * The interface index of the network interface on which the profile is connected.
     */
    @JsonProperty("InterfaceIndex")
    @Nullable
    public abstract Long interfaceIndex();

    /**
     * The name of the network interface on which the profile is connected.
     * <p>Example: "Ethernet0"</p>
     */
    @JsonProperty("InterfaceAlias")
    @Nullable
    public abstract String interfaceAlias();

    /**
     * The network category of the connected profile.
     * <p>Data type: uint32</p>
     * <p>Access type: Read-only</p>
     * <p>Possible values:</p>
     * <ul>
     *     <li>0 - Public</li>
     *     <li>1 - Private</li>
     *     <li>2 - DomainAuthenticated</li>
     * </ul>
     */
    @JsonProperty("NetworkCategory")
    @Nullable
    public abstract Long networkCategory();

    /**
     * Indicates the domain authentication kind associated with the profile.
     * <p>WARNING: No existing documentation found about this field</p>
     */
    @JsonProperty("DomainAuthenticationKind")
    @Nullable
    public abstract Long domainAuthenticationKind();

    /**
     * The IPv4 connectivity status of the connected profile.
     * <p>Possible values:</p>
     * <ul>
     *     <li>0 - Disconnected</li>
     *     <li>1 - NoTraffic</li>
     *     <li>2 - Subnet</li>
     *     <li>3 - LocalNetwork</li>
     *     <li>4 - Internet</li>
     * </ul>
     */
    @JsonProperty("IPv4Connectivity")
    @Nullable
    public abstract Long ipv4Connectivity();

    /**
     * The IPv6 connectivity status of the connected profile.
     * <p>Possible values:</p>
     * <ul>
     *     <li>0 - Disconnected</li>
     *     <li>1 - NoTraffic</li>
     *     <li>2 - Subnet</li>
     *     <li>3 - LocalNetwork</li>
     *     <li>4 - Internet</li>
     * </ul>
     */
    @JsonProperty("IPv6Connectivity")
    @Nullable
    public abstract Long ipv6Connectivity();

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
