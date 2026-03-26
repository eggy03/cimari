package unit.shell.query;

import io.github.eggy03.ferrumx.windows.shell.query.StandardCimv2;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("query_integrity")
class StandardCimv2EnumTest {

    private String generateExpectedQuery(String wmiClassAlias, String wmiProperties) {
        final String prefix = "Get-";
        final String middle = " | Select-Object -Property ";
        final String suffix = " | ConvertTo-Json";

        return prefix + wmiClassAlias + middle + wmiProperties + suffix;
    }

    @Test
    void validateMsftNetAdapterQuery() {
        String wmiClassAlias = "NetAdapter";
        String wmiProperties =
                "DeviceID, DriverDate, DriverName, DriverVersion, FullDuplex, Hidden, InterfaceAlias," +
                        " InterfaceDescription, InterfaceIndex, InterfaceName, InterfaceOperationalStatus, InterfaceType," +
                        " LinkLayerAddress, LinkSpeed, MediaConnectState, MediaType, MtuSize, NdisMedium, NdisPhysicalMedium," +
                        " PhysicalMediaType, PnPDeviceID, ReceiveLinkSpeed, Status, TransmitLinkSpeed, Virtual";

        assertThat(generateExpectedQuery(wmiClassAlias, wmiProperties))
                .isEqualTo(StandardCimv2.MSFT_NET_ADAPTER.getQuery());
    }

    @Test
    void validateMsftNetIpAddressQuery() {
        String wmiClassAlias = "NetIPAddress";
        String wmiProperties =
                "AddressFamily, IPAddress, IPv4Address, IPv6Address, InterfaceAlias, InterfaceIndex," +
                        " PreferredLifetime, PrefixLength, PrefixOrigin, SuffixOrigin, Type, ValidLifetime";

        assertThat(generateExpectedQuery(wmiClassAlias, wmiProperties))
                .isEqualTo(StandardCimv2.MSFT_NET_IP_ADDRESS.getQuery());
    }

    @Test
    void validateMsftDnsClientServerAddressQuery() {
        String wmiClassAlias = "DNSClientServerAddress";
        String wmiProperties = "AddressFamily, InterfaceAlias, InterfaceIndex, ServerAddresses";

        assertThat(generateExpectedQuery(wmiClassAlias, wmiProperties))
                .isEqualTo(StandardCimv2.MSFT_NET_DNS_CLIENT_SERVER_ADDRESS.getQuery());
    }

    @Test
    void validateMsftNetConnectionProfileQuery() {
        String wmiClassAlias = "NetConnectionProfile";
        String wmiProperties =
                "DomainAuthenticationKind, IPv4Connectivity, IPv6Connectivity, InterfaceAlias," +
                        " InterfaceIndex, NetworkCategory";

        assertThat(generateExpectedQuery(wmiClassAlias, wmiProperties))
                .isEqualTo(StandardCimv2.MSFT_NET_CONNECTION_PROFILE.getQuery());
    }
}
