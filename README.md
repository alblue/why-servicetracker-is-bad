Why ServiceTracker is bad
=========================

This repository contains sample code that demonstrates a problem with using
`ServiceTracker` at the same time as OSGi Declarative Services.

For more information and background, see the corresponding blog post at:
https://alblue.bandlem.com/2020/07/why-servicetracker-is-bad

Client
------

This contains an [`Activator`](Client/src/client/Activator.java) that, when
started, creates a `ServiceTracker` scanning for `Runnable` services, and
`open()`s the connection.

Runner
------

This contains a DS component (aka SCR component) that registers a
[`Runner`](Runner/src/runner/Runner.java) service that implements the
`Runnable` interface. It is enabled when the component is started, but as it is
not an immediate service, it should not exist until it is required.

Launching
---------

The project can be launched by using the Client+Runner.launch, which adds the
Client (marking it as 'started') and Runner (also marking it as 'started').  It
includes the `org.apache.felix.scr` bundle, along with the
`org.eclipse.equinox.console` bundle and their dependencies.

When run, the `serviceTracker.open()` call occurs, the side-effect will
cause "Component instantiated" to be printed on the console.

Running the launch and specifying `-DdisableOpen=true` the call to
`serviceTracker.open()` will be elided and the component will not be
instantiated.
