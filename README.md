# Messageboard: Applying Acceptance Test Driven Development with Cucumber

## Introduction

I wrote this not entirely trivial example because I am sometimes asked how to get started applying acceptance test
driven development beyond simply picking up a tool and covering a hello world or stack example.

My intentions for this example was to demonstrate specification first and integration last development. As I believe the
most important goal of acceptance test driven development is stakeholder participation as early as possible. Therefor it
must be easy to run the Cucumber specifications and you must be able to run them early for feedback. Ergo, they must be
fast because when they are not fast stakeholder participation is pushed back to the end of the development cycle.

Beyond that this is not meant to be an example of how you should design a system. Even though I obviously made some
design choices to facilitate implementing the Cucumber specifications. Indeed you could implement your own
version by deleting all my code and starting from the Cucumber specifications.

## Let's have a look at the specifications

You can find the specs in $ROOT/messageboard-cucumber/src/test/resources/cucumber/demo/messageboard.feature

Simply put, they describe a messageboard you can post messages to provided you set the appropriate access key. Once the
board is full the oldest message is dropped. Additionally, these specs are incomplete and it should not be hard to come
up gaps in this specification.

The specs are implemented in $ROOT/messageboard-cucumber/src/test/java/cucumber/demo/MessageBoardSteps.java

The first few specifications should not present you with too many surprises. However the last 2 covering constraints
might. More specifically because they simply name the constraints which should be in effect but do not actually describe
how they work.

This is intentional because I've gotten feedback from product owners that they don't really care about that level of
detail. They understand it is important to check for let's say a max length for a string but only really to satisfy
some technical constraint like the reserved field length of a database record.

So in the spirit of stakeholder participation I prefer to word the test in a way they would. Namely, a listing of named
constraints. The people who care about more detail can cover those requirements in their own specification files or
even in regular unit tests.

## Early Feedback: the specifications are fast and standalone

The execution time for this set of specification tests takes about 300 milliseconds. The only dependency the
messageboard-cucumber module has is on the main messageboard module which also contains a reference persistence
strategy based on Java collections.

As such you do not have to deal with bootstrapping servers and databases. There are no transaction frameworks to fight
with or anything more complicated than standard Java to deal with.

I feel like this is important to enable early feedback by allowing you to experiment and satisfy the specifications in
a clean and efficient environment.

## Discovering the persistence API

I believe this approach let's you identify the format of the persistence API you need. It is straightforward to isolate
the Java collection based persistence strategy in their own set of components. Something I did in this example and
captured in a set of compatibility tests. (see package: cucumber.demo.messageboard.persistence in the test source root)

It is then equally straightforward to implement an alternative strategy which meets this same set of tests. Which I did
in the messageboardx-sql module. In this case they have become integration tests as I'm bootstrapping an embedded H2
database for each test.

Obviously an SQL backed implementation has attributes which are different from a Java collections based one. But if it
meets the same behavioral tests would both strategies not be interchangeable? Would they not be compatible? Is there
still a reason to burden stakeholder and development collaboration with this kind of complexity?

I call this approach tested by association.

The acceptance test suite asserts the Java collection based persistence strategy works. The set of persistence
compatibility tests in turn assert that any alternative strategy will also work.

## UI Connectors

In my mind the MessageBoardSteps.java glue code for the Cucumber specifications is the first UI for the system as it
executes the system through the same interface any UI or transport connector would. In that sense, if it is too hard to
implement your Cucumber specs then it is probably too hard to support multiple UI strategies.

For this example I made 2 others:

### the CLI shell

Run by invoking cucumber.demo.messageboardx.cli.MessageBoardCLi

It should be fairly self explanatory. Type ?help to get started.

### a REST transport

Run by invoking cucumber.demo.messageboardx.rest.Main

You'll also find the file $ROOT/messageboardx-rest/postman-examples.json which you can import into Postman (Chrome) for
a collection of example requests you can execute against the embedded web server the application starts.