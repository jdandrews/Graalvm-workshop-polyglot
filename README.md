# Metadata repository and polyglot programming demo

This really only works on unix systems as of 08 dec 2022

Install Oracle GraalVM Enterprise Edition.

## Metadata

To run the app:
    ./gradlew run

    ./gradlew nativeRun

to show that the JDBC driver doesn't work.

Note class forName to get the JDBC driver; there’s no real way for the compiler to include the H2 stuff,
because there’s no direct class behavior. 

Navigate to (GraalVM Reachability Matrix)[https://github.com/oracle/graalvm-reachability-metadata];
drill in to H2 and note the version supported.

Update build.gradle; set metadataRepository = true

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
