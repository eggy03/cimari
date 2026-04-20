For Version: 0.1.0

Last Updated: April 20, 2026

The following example works more or less for every available entity that can be called.

```java
import io.github.eggy03.cimari.entity.processor.ImmutableWin32Processor;
import io.github.eggy03.cimari.entity.processor.Win32Processor;
import io.github.eggy03.cimari.service.processor.Win32ProcessorService;

import java.util.List;

@SuppressWarnings("all") // do not add this in your code
public class Win32ProcessorExample {

  public static void main(String[] args) {
    // use the service to get a list of Win32Processor instances  
    List<Win32Processor> cpuList = new Win32ProcessorService().get(15);
    cpuList.forEach(cpu -> System.out.println(cpu.toJson())); // print each of them via it's toJson() method

    // Access individual fields like this
    Win32Processor cpuFirst = cpuList.getFirst(); // java 21+ (equivalent of get(0))
    cpuFirst.addressWidth();
    cpuFirst.processorId();
    // there are more accessors available

    // If you want to create a copy of your received Win32Processor instance and then replace with your own data
    // you can do so via
    Win32Processor immutableCustomCpu = ImmutableWin32Processor
            .copyOf(cpuFirst) // create a copy first
            .withCaption("NEW VALUE") // then make edits
            .withDeviceId("NEW VALUE TWO");

    // You can also create your custom instance from scratch, using a builder
    Win32Processor freshCpu = new ImmutableWin32Processor.Builder()
             .name("value")
             .caption("value")
             .build();
  }
}
```
Some entities are not available as lists because there cannot be more than one instance of it. One such example is
Win32ComputerSystem. In such cases, we wrap them up in an Optional.

```java
import io.github.eggy03.cimari.entity.system.ImmutableWin32ComputerSystem;
import io.github.eggy03.cimari.entity.system.Win32ComputerSystem;
import io.github.eggy03.cimari.service.system.Win32ComputerSystemService;

import java.util.Optional;

@SuppressWarnings("all") // do not add this in your code
public class Win32ComputerSystemExample {

    public static void main(String[] args) {
        // use the service to get a Win32ComputerSystem instance wrapped in Optional
        Optional<Win32ComputerSystem> computerSystemOptional = new Win32ComputerSystemService().get(15);
        computerSystemOptional.ifPresent(cs -> System.out.println(cs.toJson()));

        // Access individual fields like this
        Win32ComputerSystem computerSystem = computerSystemOptional.get();
        // always use ifPresent() or equivalent checks before using get()
        // or else a NoSuchElementException will be thrown if there is no or null value wrapped in Optional
        computerSystem.name();
        computerSystem.caption();
        computerSystem.chassisSKUNumber();
        // there are more accessors available

        // If you want to create a copy of your received Win32ComputerSystem instance and then replace with your own data
        // you can do so via
        Win32ComputerSystem immutableCustomCs = ImmutableWin32ComputerSystem
                .copyOf(computerSystem) // create a copy first
                .withBootStatus(1, 1, 1) // then make edits
                .withRoles("role1", "role2");
        
        // You can also create your custom instance from scratch, using a builder
        Win32ComputerSystem freshCs = new ImmutableWin32ComputerSystem.Builder()
                .name("value")
                .caption("value")
                .build();
    }
}

```
The above examples are more or less identical for the [rest of the entities](https://javadoc.io/doc/io.github.eggy03/cimari/latest/io/github/eggy03/cimari/entity/package-summary.html)