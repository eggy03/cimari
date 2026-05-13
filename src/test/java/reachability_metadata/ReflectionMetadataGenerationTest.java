package reachability_metadata;

import io.github.eggy03.cimari.service.CommonServiceInterface;
import io.github.eggy03.cimari.service.OptionalCommonServiceInterface;
import io.github.eggy03.cimari.service.compounded.HardwareIdService;
import io.github.eggy03.cimari.service.compounded.MsftNetAdapterToIpAndDnsAndProfileService;
import io.github.eggy03.cimari.service.compounded.Win32DiskDriveToPartitionAndLogicalDiskService;
import io.github.eggy03.cimari.service.compounded.Win32DiskPartitionToLogicalDiskService;
import io.github.eggy03.cimari.service.compounded.Win32NetworkAdapterToConfigurationService;
import io.github.eggy03.cimari.service.compounded.Win32ProcessorToCacheMemoryService;
import io.github.eggy03.cimari.service.display.Win32DesktopMonitorService;
import io.github.eggy03.cimari.service.display.Win32VideoControllerService;
import io.github.eggy03.cimari.service.mainboard.Win32BaseboardService;
import io.github.eggy03.cimari.service.mainboard.Win32BiosService;
import io.github.eggy03.cimari.service.mainboard.Win32PortConnectorService;
import io.github.eggy03.cimari.service.memory.Win32PhysicalMemoryService;
import io.github.eggy03.cimari.service.network.MsftDnsClientServerAddressService;
import io.github.eggy03.cimari.service.network.MsftNetAdapterService;
import io.github.eggy03.cimari.service.network.MsftNetConnectionProfileService;
import io.github.eggy03.cimari.service.network.MsftNetIpAddressService;
import io.github.eggy03.cimari.service.network.Win32NetworkAdapterConfigurationService;
import io.github.eggy03.cimari.service.network.Win32NetworkAdapterService;
import io.github.eggy03.cimari.service.network.Win32NetworkAdapterSettingService;
import io.github.eggy03.cimari.service.peripheral.Win32BatteryService;
import io.github.eggy03.cimari.service.peripheral.Win32PrinterService;
import io.github.eggy03.cimari.service.peripheral.Win32SoundDeviceService;
import io.github.eggy03.cimari.service.processor.Win32AssociatedProcessorMemoryService;
import io.github.eggy03.cimari.service.processor.Win32CacheMemoryService;
import io.github.eggy03.cimari.service.processor.Win32ProcessorService;
import io.github.eggy03.cimari.service.storage.Win32DiskDriveService;
import io.github.eggy03.cimari.service.storage.Win32DiskDriveToDiskPartitionService;
import io.github.eggy03.cimari.service.storage.Win32DiskPartitionService;
import io.github.eggy03.cimari.service.storage.Win32LogicalDiskService;
import io.github.eggy03.cimari.service.storage.Win32LogicalDiskToPartitionService;
import io.github.eggy03.cimari.service.system.Win32ComputerSystemService;
import io.github.eggy03.cimari.service.system.Win32EnvironmentService;
import io.github.eggy03.cimari.service.system.Win32OperatingSystemService;
import io.github.eggy03.cimari.service.system.Win32PnPEntityService;
import io.github.eggy03.cimari.service.system.Win32ProcessService;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@EnabledOnOs(OS.WINDOWS)
@Tag("reachability-metadata-generation")
class ReflectionMetadataGenerationTest {

    private static final Logger log = LoggerFactory.getLogger(ReflectionMetadataGenerationTest.class);
    private final long timeout = 5L;

    @Test
    void createEntityMapperServiceMetadata_successPath() {

        runOptionalCommonService(new HardwareIdService(), new Win32ComputerSystemService());

        runCommonService(
                new MsftNetAdapterToIpAndDnsAndProfileService(),
                new Win32DiskDriveToPartitionAndLogicalDiskService(),
                new Win32DiskPartitionToLogicalDiskService(),
                new Win32NetworkAdapterToConfigurationService(),
                new Win32ProcessorToCacheMemoryService(),

                new Win32DesktopMonitorService(),
                new Win32VideoControllerService(),

                new Win32BaseboardService(),
                new Win32BiosService(),
                new Win32PortConnectorService(),

                new Win32PhysicalMemoryService(),

                new MsftDnsClientServerAddressService(),
                new MsftNetAdapterService(),
                new MsftNetConnectionProfileService(),
                new MsftNetIpAddressService(),
                new Win32NetworkAdapterService(),
                new Win32NetworkAdapterConfigurationService(),
                new Win32NetworkAdapterSettingService(),

                new Win32BatteryService(),
                new Win32PrinterService(),
                new Win32SoundDeviceService(),

                new Win32AssociatedProcessorMemoryService(),
                new Win32CacheMemoryService(),
                new Win32ProcessorService(),

                new Win32DiskDriveService(),
                new Win32DiskDriveToDiskPartitionService(),
                new Win32DiskPartitionService(),
                new Win32LogicalDiskService(),
                new Win32LogicalDiskToPartitionService(),

                new Win32EnvironmentService(),
                new Win32OperatingSystemService(),
                new Win32PnPEntityService(),
                new Win32ProcessService());
    }

    public void runCommonService(CommonServiceInterface<?> @NonNull ... services) {
        for (CommonServiceInterface<?> service : services) {
            service.get(timeout).forEach(obj -> log.info(obj.toString()));
        }
    }

    public void runOptionalCommonService(OptionalCommonServiceInterface<?> @NonNull ... services) {
        for (OptionalCommonServiceInterface<?> service : services) {
            service.get(timeout).ifPresent(obj -> log.info(obj.toString()));
        }
    }
}
