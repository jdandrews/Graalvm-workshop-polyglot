# Metadata repository and polyglot programming demo

## Preparation
This really only works on Linux systems as of 08 Dec 2022

Start with a recent Linux distribution; this is tested on Linux 8.9 and 9.x.
* Install Oracle GraalVM Enterprise Edition v 22.3 or later.
* Install gradle
* Clone this repository.

## Metadata

GraalVM compilation depends on the idea that the compiler can trace the main entry point and identify all the classes required
to execute the code. It works pretty well unless the design firewalls some part of the code, making it impossible for the 
compiler to determine what classes are air-gapped. There are 2 strategies for dealing with this:

1. run the code once using the JIT compiler and instrument the code. The instrumentation can tell what classes are loaded and 
   mark them for inclusion.
2. use similar data, pre-built from standard distributions.

Oracle maintains a metadata repository, allowing users to post data needed to compile many standard open source packages. One such
package is the H2 in-memory database. Since the JDBC driver is instantiated with Class.forName(...), the compiler can't trace down
into H2's client to determine what needs to be included in the final executable. With the Metadata repository, that information can
be looked up at compile time, and a complete executable generated.

Inspect the code:  In src/main/java/com/jrandrews/jsc/polyglot/App.java, you can see that we instantiate an H2 JDBC driver using 
Class.forName(...), then use it to perform some basic database operations.  Inspecting the build.gradle file, you can see how we 
have configured the GraalVM plugin to compile the code.

Run the app:
    ./gradlew run

Note that a database file is created in the logged in user's home directory.  Now build and run a native image:

    ./gradlew nativeRun

The build succeeds, but the resulting application throws a null pointer exception, because the JDBC client code is not included
in the executable.

Navigate to (GraalVM Reachability Matrix)[https://github.com/oracle/graalvm-reachability-metadata];
drill in to H2 and note the version supported.

Update build.gradle; set metadataRepository = true

Now build and run again:

    ./gradlew nativeRun

...to show that the JDBC driver works this time.

## Polyglot

Install runtimes:

    gu available        # to see what's available
    gu install python
    gu install r

    cp settings.polyglot settings.gradle

This changes the included project from “app” to “polyglot”. Now build and run using the JIT compiler:

    ./gradlew run

Then build the native image and run it. The R native image build is experimental so for now, comment out the R
code by editing the App.java source code, and commenting out the line that says "app.rFromPythonGraph();". Then:

    ./gradlew clean run --args="-agentlib:native-image-agent” # to get a set of configuration files. ‘find . –name ““.json”’ to see the files.
    ./gradlew nativeRun
