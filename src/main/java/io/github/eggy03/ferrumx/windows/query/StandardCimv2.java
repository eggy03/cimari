/*
 * © 2024–2025 The ferrumx-windows contributors
 * Licensed under the MIT License.
 * See the LICENSE file in the project root for more information.
 */
package io.github.eggy03.ferrumx.windows.query;

import com.profesorfalken.jpowershell.PowerShell;
import io.github.eggy03.ferrumx.windows.entity.network.MsftDnsClientServerAddress;
import io.github.eggy03.ferrumx.windows.entity.network.MsftNetAdapter;
import io.github.eggy03.ferrumx.windows.entity.network.MsftNetConnectionProfile;
import io.github.eggy03.ferrumx.windows.entity.network.MsftNetIpAddress;
import io.github.eggy03.ferrumx.windows.query.utility.QueryUtility;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
/**
 * Enum representing the predefined WMI (CIM) queries for the classes available in the {@code root/StandardCimv2} namespace.
 * <p>
 * Each constant holds a PowerShell query that queries a specific class in the namespace
 * and returns the result in JSON format. These queries are typically executed
 * using {@link PowerShell} and mapped to
 * corresponding Java objects.
 * </p>
 *
 * @author Sayan Bhattacharjee (Egg-03/Eggy)
 * @since 3.0.0
 */
@RequiredArgsConstructor
@Getter
public enum StandardCimv2 {

    /**
     * Query to fetch the properties of the {@code MSFT_NetAdapter} class
     * <p>Will not show hidden physical or logical network adapters unless explicitly stated</p>
     *
     * @since 3.0.0
     */
    MSFT_NET_ADAPTER(generateQuery("Get-NetAdapter", MsftNetAdapter.class)),

    /**
     * Query to fetch the properties of the {@code MSFT_NetIPAddress} class
     *
     * @since 3.0.0
     */
    MSFT_NET_IP_ADDRESS(generateQuery("Get-NetIPAddress", MsftNetIpAddress.class)),

    /**
     * Query to fetch the properties of the {@code MSFT_NetDNSClientServerAddress} class
     *
     * @since 3.0.0
     */
    MSFT_NET_DNS_CLIENT_SERVER_ADDRESS(generateQuery("Get-DNSClientServerAddress", MsftDnsClientServerAddress.class)),

    /**
     * Query to fetch the properties of the {@code MSFT_NetConnectionProfile} class
     *
     * @since 3.0.0
     */
    MSFT_NET_CONNECTION_PROFILE(generateQuery("Get-NetConnectionProfile", MsftNetConnectionProfile.class));

    @NonNull
    private final String query;

    @NotNull
    private static <T> String generateQuery(@NonNull String prefix, @NonNull Class<T> wmiEntity) {
        return prefix +
                " | Select-Object -Property " + QueryUtility.getPropertiesFromSerializedNameAnnotation(wmiEntity) +
                " | ConvertTo-Json";

    }
}
