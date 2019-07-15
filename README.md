Hackathon: Kafka Streams
========================

A task to show Java and Kafka Streams coding skills, nothing more...


Task description
----------------

See [assignment.pdf](doc/assignment.pdf).


Usage
-----

[Download and start](http://kafka.apache.org/documentation/#quickstart) Apache Kafka.

```
bin/zookeeper-server-start.sh config/zookeeper.properties
bin/kafka-server-start.sh config/server.properties
```

Produce sample data to Kafka. The first execution also creates the topic.

```
cat kafka-messages.jsonline | bin/kafka-console-producer.sh --broker-list localhost:9092 --topic hackathon-in
```

Build the application.

```
./gradlew clean build
```

Execute the application.

- Main class: `com.github.mixalturek.hackhathon.HackathonServer`.
- [Typesafe config](https://github.com/lightbend/config) configuration file (optional):
    - `-Dconfig.file=application.conf`
    - Reference configuration for development on `localhost`.
- [Logback](https://logback.qos.ch/) configuration file (optional):
    - `-Dlogback.configurationFile=logback.xml`
    - Logging to `stdout` for local development by default.
- The application gracefully terminates on `SIGTERM` signal.
