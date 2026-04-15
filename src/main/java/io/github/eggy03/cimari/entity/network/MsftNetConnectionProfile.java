/*
 * SPDX-License-Identifier: MIT
 * SPDX-FileCopyrightText: 2025 The ferrumx-windows contributors
 * SPDX-FileCopyrightText: 2026 Cimari contributors
 */
package io.github.eggy03.cimari.entity.network;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.eggy03.cimari.annotation.ShallowImmutable;
import io.github.eggy03.cimari.annotation.WmiClass;
import lombok.Builder;
import lombok.Value;
import org.jetbrains.annotations.Nullable;
import tools.jackson.databind.ObjectMapper;

/**
 * Immutable representation of a connection profile for a particular network adapter on a Windows system.
 * <p>
 * Fields correspond to properties retrieved from the {@code MSFT_NetConnectionProfile} class in the
 * {@code root/StandardCimv2} namespace.
 * </p>
 *
 * <h2>Usage example</h2>
 * <pre>{@code
 * MsftNetConnectionProfile profile = MsftNetConnectionProfile.builder()
 *     .interfaceIndex(1)
 *     .networkCategory(0)
 *     .ipv4Connectivity(4)
 *     .ipv6Connectivity(1)
 *     .build();
 *
 * // Create a modified copy
 * MsftNetConnectionProfile updated = profile.toBuilder()
 *     .networkCategory(1)
 *     .build();
 * }</pre>
 *
 * <p>See {@link MsftNetAdapter}, for network adapter information.</p>
 * <p>See {@link MsftDnsClientServerAddress}, for information regarding the connected DNS servers of a network adapter.</p>
 * <p>See {@link MsftNetIpAddress}, for IP address configuration information of a network adapter.</p>
 *
 *
 * @see <a href="https://wutils.com/wmi/root/standardcimv2/msft_netconnectionprofile/">MSFT_NetConnectionProfile Documentation</a>
 * @since 1.0.0
 */
@Value
@Builder(toBuilder = true)
@ShallowImmutable
@WmiClass(className = "MSFT_NetConnectionProfile")
public class MsftNetConnectionProfile {

    /**
     * The interface index of the network interface on which the profile is connected.
     */
    @JsonProperty("InterfaceIndex")
    @Nullable
    Long interfaceIndex;

    /**
     * The name of the network interface on which the profile is connected.
     * <p>Example: "Ethernet0"</p>
     */
    @JsonProperty("InterfaceAlias")
    @Nullable
    String interfaceAlias;

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
    Long networkCategory;

    /**
     * Indicates the domain authentication kind associated with the profile.
     * <p>WARNING: No existing documentation found about this field</p>
     */
    @JsonProperty("DomainAuthenticationKind")
    @Nullable
    Long domainAuthenticationKind;

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
    Long ipv4Connectivity;

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
    Long ipv6Connectivity;

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
