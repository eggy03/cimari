This documentation outlines the principal architecture of the library and aims to provide the viewers
with an understanding of how the internal components work together.

> [!NOTE]
> This documentation reflects the workings of v4.1.0 of the library. Older or newer versions may have altered structure,
> behavior or implementation.
> I will try to update this documentation frequently to reflect the latest changes.

# Project Structure

```
src
├───main
│   ├───java
│   │   └───io.github.eggy.ferrumx.windows
│   │       ├───annotation
│   │       ├───entity
│   │       ├───exception
│   │       ├───mapping
│   │       ├───service
│   │       ├───shell
│   │       └───utility
│   └───resources
└───test
    ├───java
    │   └───unit
    │       ├───mapper
    │       ├───service
    │       ├───shell
    │       └───utility
    └───resources
```

# Intent

The primary goal of the API can be divided into three simple steps:

1) Initialize and manage a PowerShell session to execute WMI queries
2) Retrieve and process the command output
3) Map the processed data into Java entities and return them to the caller

Some of the secondary goals of the API can be described as:

1) Handle scenarios when PowerShell cannot be launched or throws an error during query runs
2) Handle scenarios where queries return incomplete or unexpected results
3) Safely manage null, empty, or malformed results
4) Handle mutability of mapped entity classes and collections

# Working

For this example, we will query the `Win32_Processor` class and
map a subset of its properties into a custom-defined entity class.

### Checking out the Documentation

To start with, we need to understand the structure of the `Win32_Processor` class.
The complete list of available fields can be found in the official Microsoft documentation:

Reference: https://learn.microsoft.com/en-us/windows/win32/cimwin32prov/win32-processor

The section below shows the fields revealed by the `Win32_Processor` class:

```
[Dynamic, Provider("CIMWin32"), UUID("{8502C4BB-5FBB-11D2-AAC1-006008C78BC7}"), AMENDMENT]
class Win32_Processor : CIM_Processor
{
  uint16  AddressWidth;
  uint16  Architecture;
  string  AssetTag;
  uint16  Availability;
  string  Caption;
  uint32  Characteristics;
  uint32  ConfigManagerErrorCode;
  boolean ConfigManagerUserConfig;
  uint16  CpuStatus;
  string  CreationClassName;
  uint32  CurrentClockSpeed;
  uint16  CurrentVoltage;
  uint16  DataWidth;
  string  Description;
  string  DeviceID;
  boolean ErrorCleared;
  string  ErrorDescription;
  uint32  ExtClock;
  uint16  Family;
  datetime InstallDate;
  uint32  L2CacheSize;
  uint32  L2CacheSpeed;
  uint32  L3CacheSize;
  uint32  L3CacheSpeed;
  uint32  LastErrorCode;
  uint16  Level;
  uint16  LoadPercentage;
  string  Manufacturer;
  uint32  MaxClockSpeed;
  string  Name;
  uint32  NumberOfCores;
  uint32  NumberOfEnabledCore;
  uint32  NumberOfLogicalProcessors;
  string  OtherFamilyDescription;
  string  PartNumber;
  string  PNPDeviceID;
  uint16  PowerManagementCapabilities[];
  boolean PowerManagementSupported;
  string  ProcessorId;
  uint16  ProcessorType;
  uint16  Revision;
  string  Role;
  boolean SecondLevelAddressTranslationExtensions;
  string  SerialNumber;
  string  SocketDesignation;
  string  Status;
  uint16  StatusInfo;
  string  Stepping;
  string  SystemCreationClassName;
  string  SystemName;
  uint32  ThreadCount;
  string  UniqueId;
  uint16  UpgradeMethod;
  string  Version;
  boolean VirtualizationFirmwareEnabled;
  boolean VMMonitorModeExtensions;
  uint32  VoltageCaps;
};
```

While the class exposes a large number of properties, it may not be necessary to map all of them.

### Designing the Query

We begin by executing a PowerShell command to retrieve selected properties in JSON format:

```shell
Get-CimInstance -ClassName Win32_Processor | Select-Object Name, NumberOfCores, Manufacturer | ConvertTo-Json
```

Executing this query produces output similar to:

```json
{
  "Name": "AMD Ryzen 5 5600G with Radeon Graphics",
  "NumberOfCores": 6,
  "Manufacturer": "AuthenticAMD"
}
```

Since this executed every time a caller wants to fetch Win32_Processor information, we will store this query in an enum.

Our enum will reside in the `io.github.eggy.ferrumx.windows.shell.query*` package.

We define an enum named `Cimv2`, derived from the namespace to which `Win32_Processor` belongs.

```java

@SuppressWarnings("LombokGetterMayBeUsed")
public enum Cimv2 {

    WIN32_PROCESSOR(
            "Get-CimInstance -ClassName Win32_Processor |" +
                    " Select-Object Name, NumberOfCores, Manufacturer |" +
                    " ConvertTo-Json"
    );

    private final String query;

    Cimv2(String query) {
        this.query = query;
    }

    public String getQuery() {
        return this.query;
    }

}
```

This will allow us to fetch the entire query by just calling `Cimv2.WIN32_PROCESSOR.getQuery()`.

In the next section, we will refactor this enum to dynamically construct the properties and the class name required
to build the query, at runtime, via reflection.
We will also use `Lombok` to reduce boilerplate code.

### Designing the Entity Class

Based on the fields we want, we will now design our entity class.

All our entity classes can be found in the `io.github.eggy.ferrumx.windows.entity.*` package.
For processor-related mappings, a dedicated subpackage `entity.processor` is used.

As of v4.1.0, this package includes:

- `Win32_Processor`
- `Win32_CacheMemory`
- `Win32_AssociatedProcessorMemory`

```java

@Value
@Builder(toBuilder = true)
@Immutable
@WmiClass(className = "Win32_Processor")
public class Win32Processor {

    @SerializedName("Name")
    @Nullable
    String name;

    @SerializedName("NumberOfCores")
    @Nullable
    Integer numberOfCores;

    @SerializedName("Manufacturer")
    @Nullable
    String manufacturer;

    @Override
    @NotNull
    public String toString() {
        return new GsonBuilder()
                .serializeNulls()
                .setPrettyPrinting()
                .create()
                .toJson(this);
    }
}
```

Let's talk about the annotations first.