This documentation outlines the principal architecture of the library
and provides an overview of how its internal components work together.

_Last Updated: April 19, 2026_

> [!NOTE]
> This documentation reflects the workings of v1.0.0-alpha1 of the library. Older or newer versions may have altered
> structure,
> behavior, or implementation.
> This documentation may be updated periodically to reflect the latest changes.

# Project Structure

```
src
├───main
│   ├───java
│   │   └───io.github.eggy.cimari
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
    │   └───io.github.eggy.cimari
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
4) Address mutability concerns of mapped entity classes and collections

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
Get-CimInstance -ClassName Win32_Processor | Select-Object DeviceID, Name, NumberOfCores, Manufacturer | ConvertTo-Json
```

Executing this query produces output similar to:

```json
{
  "DeviceID": "CPU0",
  "Name": "AMD Ryzen 5 5600G with Radeon Graphics",
  "NumberOfCores": 6,
  "Manufacturer": "AuthenticAMD"
}
```

Since this query is executed every time a caller requests Win32_Processor information, we store it in an enum.

Our enum will reside in the `io.github.eggy.cimari.shell.query` package.

We define an enum named `Cimv2`, derived from the namespace to which `Win32_Processor` belongs.

```java
package io.github.eggy.cimari.shell.query;

public enum Cimv2 {

    WIN32_PROCESSOR(
            "Get-CimInstance -ClassName Win32_Processor |" +
                    " Select-Object DeviceID, Name, NumberOfCores, Manufacturer |" +
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

### Designing the Entity Class

Based on the fields we want, we will now design our entity class.

All entity classes can be found in the `io.github.eggy.cimari.entity.*` package.
For processor-related mappings, a dedicated subpackage `entity.processor` is used.

As of v1.0.0-alpha1, this package includes:

- `Win32_Processor`
- `Win32_CacheMemory`
- `Win32_AssociatedProcessorMemory`

Our entity class is going to be an abstract class and based on this class, Immutables will generate an immutable
representation of this class which extends the abstract class.

```java
package io.github.eggy03.cimari.entity.processor;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.eggy03.cimari.annotation.ImmutableEntityStyle;
import io.github.eggy03.cimari.annotation.WmiClass;
import org.immutables.value.Value;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.annotation.JsonDeserialize;
import tools.jackson.databind.annotation.JsonSerialize;

@WmiClass(className = "Win32_Processor")
@NullMarked
@Value.Immutable
@ImmutableEntityStyle
@JsonSerialize(as = ImmutableWin32Processor.class)
@JsonDeserialize(as = ImmutableWin32Processor.class)
public abstract class Win32Processor {

  @JsonProperty("DeviceID")
  @Nullable
  public abstract String deviceId();

  @JsonProperty("Name")
  @Nullable
  public abstract String name();

  @JsonProperty("NumberOfCores")
  @Nullable
  public abstract Integer numberOfCores();

  @JsonProperty("Manufacturer")
  @Nullable
  public abstract String manufacturer();

  public String toJson() {
    return new ObjectMapper()
            .writerWithDefaultPrettyPrinter()
            .writeValueAsString(this);
  }
}
```

What we have here is an annotation heavy entity class that's designed based on the properties we want to map.

Let's talk about the annotations and their purpose:

`@WmiClass` is a custom annotation, and it serves the following purposes:

- It holds information about the actual WMI class name.
- The annotation allows us to get the class name during dynamic construction of queries during runtime.

`@Value.Immutable`

- Instructs the Immutables annotation processor to generate immutable implementation of abstract value type.

`@ImmutableEntityStyle`

- A custom annotation that uses `@Value.Style` from Immutables to control how the implementations of the abstract
  entity classes are generated.

```java
package io.github.eggy03.cimari.annotation;

import org.immutables.value.Value;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.PACKAGE, ElementType.TYPE})
@Retention(RetentionPolicy.CLASS) // Make it class retention for incremental compilation
@Value.Style(
        typeAbstract = {"*"}, // No prefix or suffix will be detected and trimmed
        typeImmutable = "Immutable*", // generated immutable types will have Immutable as prefix
        visibility = Value.Style.ImplementationVisibility.PUBLIC, // Generated class will be always public
        builder = "new", // construct builder using 'new' instead of factory method (required for Jackson).
        // Generated builders will have attributes annotated with @JsonProperty so deserialization will work properly.
        defaults = @Value.Immutable(copy = true), // Enable copy methods
        passAnnotations = WmiClass.class // this annotation is needed to build queries at runtime from @JsonProperty values
)
@SuppressWarnings("all") // only included in this example. actual implementation does not have this
public @interface ImmutableEntityStyle {
}
```

`@JsonSerialize(as = ImmutableWin32Processor.class)` and `@JsonDeserialize(as = ImmutableWin32Processor.class)`

- These annotations, especially `@JsonDeserialize`, tells our mapping layer which generated implementation it should use
  during JSON deserialization into entity classes. If you take a look at the interface,`CommonMappingInterface`
  and it's implementations in `Win32ProcessorMapper`, both of which are found in `io.github.eggy03.cimari.mapper`, you
  will
  notice that the deserialization class type is of the abstract type, the one we wrote here, i.e., the `Win32Processor`.
  However, without a concrete implementation, deserialization cannot occur. `@JsonDeserialize` internally tells Jackson
  to deserialize JSON into an instance of `ImmutableWin32Processor.class` and then convert it to our abstract version
  since,the latter extends from the abstract class.
  You will get to know more about this during mapping layer discussions.

`@JsonProperty` is from Jackson and it serves the following purposes:

- It holds information about the actual WMI property name that the abstract methods correspond to. Due to different
  naming conventions, we need to explicitly tell Jackson that a property with the given name must be mapped to the given
  method.

- The annotation allows us to get the property name during dynamic construction of queries during runtime.

`@Nullable` and `@NullMarked`

- These annotations from Jspecify, and they're strictly used to tell users and static analysis
  tools about the nullity of the given abstract methods. It is always safe to assume that all the abstract methods which
  are explicitly marked with `@Nullable`, for a given entity may be
  null at some point or for different systems. On the other hand `@NullMarked` allows us to assume that unless something
  is explicitly marked as `@Nullable`, we can safely assume that it will be non-null by contract.

We also have a `toJson()` function that returns a pretty printed JSON representation of the abstract class.

### Dynamically Generating Queries

We will now refactor our [Cimv2](#designing-the-query) enum such that it can dynamically fetch the class name
and the properties to be queried.

Take a look at the following refactor:

```java
package io.github.eggy03.cimari.shell.query;

import io.github.eggy03.cimari.entity.processor.Win32Processor;
import io.github.eggy03.cimari.shell.query.QueryUtility;
import org.jspecify.annotations.NonNull;

import java.util.Objects;

public enum Cimv2 {

  WIN32_PROCESSOR(generateQuery(Win32Processor.class));

  private final @NonNull String query;

  Cimv2(@NonNull String query) {
    this.query = Objects.requireNonNull(query, "query cannot be null");
  }

  private static <T> @NonNull String generateQuery(@NonNull Class<T> wmiClass) {

    Objects.requireNonNull(wmiClass, "wmiClass cannot be null");

    return "Get-CimInstance -ClassName " + QueryUtility.getClassNameFromWmiClass(wmiClass) +
            " | Select-Object -Property " + QueryUtility.getPropertiesFromJsonProperty(wmiClass) +
            " | ConvertTo-Json";

  }

  public @NonNull String getQuery() {
    return this.query;
  }
}
```

We have introduced a Utility Class called `QueryUtility` which uses reflection to get the class name and properties
from our `Win32Processor` class during runtime.

`@WmiClass(className = "Win32_Processor")` and `@JsonProperty("...")` provides all the data required to fetch the
class name and the properties to be queried, respectively.

We just need to pass `Win32Processor.class` as an argument and the utility will inspect the class.
The utility resides in the `io.github.eggy.cimari.shell.query*` package.

### Designing the Mapper

The mappers are responsible for deserializing the JSON output from PowerShell into typed entities.

All entity mappers reside in the `io.github.eggy.cimari.mapping*` package.
We have an interface called `CommonMappingInterface` with default methods which are usually enough to deserialize any
JSON into typed entities, using Jackson

The following pseudocode shows the interface with the default methods.
For full definition and implementation, check out `io.github.eggy.cimari.mapping.CommonMappingInterface`.

```java
package io.github.eggy03.cimari.mapping;

import org.jspecify.annotations.NonNull;

import java.util.List;
import java.util.Optional;

// simplified representation
// actual implementation may vary
public interface CommonMappingInterface<S> {

  default @NonNull ObjectMapper configureObjectMapper() {
    // allow for plugging in a custom configuration of Jackson's ObjectMapper or rely on the default implementation
  }

  default @NonNull List<S> mapToList(@NonNull String json, @NonNull Class<S> objectClass) {
        /*
         if JSON starts with "[" it means it's an array, and therefore we will serialize it into a List of type S
         However, there might be cases wherein, you expect an array of objects but get a single object instead.
         For example, WMI lists CPUs as an array of objects but in cases where there is only 1 CPU, it may
         either return as an array of single object or just the object, which will then start with { key = value, key2=value2}.
         
         In the latter case, we will wrap it in a singleton list.
         
         In any case, the returned List of objects is unmodifiable.
         Note that the returned List is unmodifiable,
         but the contained objects may still be mutable depending on their implementation.
        */
  }

  default @NonNull Optional<S> mapToObject(@NonNull String json, @NonNull Class<S> objectClass) {
    // You can use this method if you are 100% sure that the JSON will always serialize into a single object
    // One such example is Win32_ComputerSystem. It's documentation states that it always returns a single object.
  }

  // Refer to the actual implementation for details on null handling and edge-case behavior.
}
```

Till now, the mappers for all the entities have used the default implementation of the interface.
We will define our mapper by creating an empty class that implements the interface.

```java
package io.github.eggy03.cimari.mapping.processor;

import io.github.eggy03.cimari.entity.processor.Win32Processor;
import io.github.eggy03.cimari.mapping.CommonMappingInterface;

public class Win32ProcessorMapper implements CommonMappingInterface<Win32Processor> {
  // usually not needed, but you can write your custom implementations here
  // otherwise, the default methods from the interface are sufficient
}
```

This mapper will reside in the `mapping.processor` sub-package and can be simply called
via: `new Win32ProcessorMapper().mapToList(...) or mapToObject(...)`.

### Designing the Service

Service classes are responsible for orchestrating the full workflow of:

- executing PowerShell queries
- retrieving output
- mapping results into entities

All service classes reside in the `io.github.eggy.cimari.service*` package.

At a minimum, a service method should:

1) Initialize or receive a PowerShell session
2) Generate and Execute the required query
3) Retrieve the output
4) Delegate deserialization to the mapper
5) Return the mapped result to the caller

Service classes may also:

1) Manage the lifecycle of PowerShell sessions
2) Handle failures in session creation or execution
3) Handle malformed, empty, or unexpected outputs (usually handled by the mappers)
4) Clearly document concurrency characteristics (e.g., whether a session is reusable across threads)

Let's take a look at the two interfaces that all the service classes implement.

```java
package io.github.eggy03.cimari.service;

import java.util.List;

public interface CommonServiceInterface<S> {

  List<S> get(long timeout);
}
```

```java
package io.github.eggy03.cimari.service;

import java.util.Optional;

public interface OptionalCommonServiceInterface<S> {

  Optional<S> get(long timeout);
}
```

Unlike `CommonMappingInterface`, these two interfaces do not have default methods and the classes that
implement these interfaces must override these methods to have their own definitions.

Let's create a `service.processor` subpackage and provide a concrete definition for `Win32Processor`.

```java
package io.github.eggy03.cimari.service.processor;

import io.github.eggy03.cimari.entity.processor.Win32Processor;
import io.github.eggy03.cimari.mapping.processor.Win32ProcessorMapper;
import io.github.eggy03.cimari.service.CommonServiceInterface;
import io.github.eggy03.cimari.shell.query.Cimv2;
import io.github.eggy03.cimari.terminal.TerminalResult;
import io.github.eggy03.cimari.terminal.TerminalService;
import org.jspecify.annotations.NonNull;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Win32ProcessorService implements CommonServiceInterface<Win32Processor> {

  private final @NonNull TerminalService terminalService;
  private final @NonNull Win32ProcessorMapper mapper;


  public Win32ProcessorService() {
    this(new TerminalService(), new Win32ProcessorMapper());
  }

  Win32ProcessorService(TerminalService terminalService, Win32ProcessorMapper mapper) {
    this.terminalService = Objects.requireNonNull(terminalService, "terminalService cannot be null");
    this.mapper = Objects.requireNonNull(mapper, "mapper cannot be null");
  }


  @Override
  public @NonNull List<Win32Processor> get(long timeout) {
    TerminalResult result = terminalService.executeQuery(Cimv2.WIN32_PROCESSOR, timeout);
    return mapper.mapToList(result.getResult(), Win32Processor.class);
  }
}

```

`get(long timeout)`

- Creates a scope-limited PowerShell session via `TerminalService`
- Session is scoped within the current method call and cannot be re-used once the script or command passed to it has
  been executed
- Deserializes the terminal output to a list of Win32Processor instances

`TerminalService` and `TerminalResult` are found in the `io.github.eggy03.cimari.terminal` package

### Terminal Service

The `io.github.eggy03.cimari.terminal` package consists of two classes:

- `TerminalService`
- `TerminalResult`

TerminalResult wraps PowerShell's stdout and stderr and provides convenient getters to access them.

```java
package io.github.eggy03.cimari.terminal;

import org.jspecify.annotations.NonNull;

import java.util.Objects;

@SuppressWarnings("all") // actual implementation does not include this
public class TerminalResult {

  private final @NonNull String result;
  private final @NonNull String error;

  public TerminalResult(@NonNull String result, @NonNull String error) {
    this.result = Objects.requireNonNull(result, "result cannot be null");
    this.error = Objects.requireNonNull(error, "error cannot be null");
  }

  public @NonNull String getResult() {
    return result;
  }

  public @NonNull String getError() {
    return error;
  }

}
```

TerminalService handles PowerShell sessions and query executions

```java
package io.github.eggy03.cimari.terminal;

import io.github.eggy03.cimari.exception.TerminalIOException;
import io.github.eggy03.cimari.shell.query.Cimv2;
import io.github.eggy03.cimari.shell.query.StandardCimv2;
import io.github.eggy03.cimari.shell.script.ScriptEnum;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.PumpStreamHandler;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Objects;

// simplified implementation
// actual implementation may vary
public class TerminalService {

  public @NonNull TerminalResult executeQuery(@NonNull Cimv2 queryEnum, long timeoutSeconds) {
    Objects.requireNonNull(queryEnum, "queryEnum cannot be null");
    return execute(queryEnum.getQuery(), timeoutSeconds);
  }

  @NonNull TerminalResult execute(@NonNull String command, long timeout) {
    // code for launching, managing PowerShell sessions, executing commands and returning the wrapped result
  }
}

```

### Writing Tests

Unit tests for the library can be found under the `src/test` directory. Check them out to see what cases are handled
and suggest improvements or write your own tests.

### Putting Everything Together

With the service layer in place, we can finally call our service class.

The following example retrieves processor information using an isolated PowerShell execution with a timeout of 15
seconds.

```java
import io.github.eggy03.cimari.entity.processor.ImmutableWin32Processor;
import io.github.eggy03.cimari.entity.processor.Win32Processor;
import io.github.eggy03.cimari.entity.processor.Win32Processor;
import io.github.eggy03.cimari.service.processor.Win32ProcessorService;

import java.util.List;

@SuppressWarnings("all")
public class Win32ProcessorExample {

  public static void main(String[] args) {
    List<Win32Processor> cpuList = new Win32ProcessorService().get(15);
    cpuList.forEach(cpu -> System.out.println(cpu.toJson()));

    // You can access individual fields like this
    Win32Processor cpuFirst = cpuList.getFirst(); // java 21+
    cpuFirst.addressWidth();
    cpuFirst.processorId();

    // If you want to create a copy of your received Win32Processor instance and then replace with your own data
    // you can do so via

    ImmutableWin32Processor immutableCustomCpu = ImmutableWin32Processor
            .copyOf(cpuFirst) // create a copy first
            .withCaption("NEW VALUE") // then make edits
            .withDeviceId("NEW VALUE TWO");
  }
}
```

# References

- [Javadocs](https://javadoc.io/doc/io.github.eggy03/cimari)
- [Examples](// todo)
- [Microsoft Docs for Win32_Processor](https://learn.microsoft.com/en-us/windows/win32/cimwin32prov/win32-processor)
- [Additional Helpful Docs from `powershell.one`](https://powershell.one/wmi/root/cimv2)