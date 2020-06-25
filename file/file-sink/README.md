# Camel-Kafka-connector File Sink

## Introduction

This is an example for Camel-Kafka-connector File Sink 

## What is needed

- A File System

## Running Kafka

```
$KAFKA_HOME/bin/zookeeper-server-start.sh config/zookeeper.properties
$KAFKA_HOME/bin/kafka-server-start.sh config/server.properties
$KAFKA_HOME/bin/kafka-topics.sh --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic mytopic
```

## Setting up the needed bits and running the example

You'll need to setup the plugin.path property in your kafka

Open the `$KAFKA_HOME/config/connect-standalone.properties`

and set the `plugin.path` property to your choosen location

In this example we'll use `/home/oscerd/connectors/`

```
> cd /home/oscerd/connectors/
> wget https://repo1.maven.org/maven2/org/apache/camel/kafkaconnector/camel-file-kafka-connector/0.3.0/camel-file-kafka-connector-0.3.0-package.zip
> unzip camel-file-kafka-connector-0.3.0-package.zip
```

Now it's time to setup the connectors

Open the File Sink configuration file

```
name=CamelFileSinkConnector
topics=mytopic
tasks.max=1
connector.class=org.apache.camel.kafkaconnector.file.CamelFileSinkConnector
key.converter=org.apache.kafka.connect.storage.StringConverter
value.converter=org.apache.kafka.connect.storage.StringConverter

camel.sink.path.directoryName=/tmp/
camel.sink.endpoint.fileName=kafkaconnect.txt
camel.sink.endpoint.fileExist=Append
```

Now you can run the example

```
$KAFKA_HOME/bin/connect-standalone.sh $KAFKA_HOME/config/connect-standalone.properties config/CamelFileSinkConnector.properties
```

On a different terminal run the kafka-producer and send messages to your Kafka Broker.

```
bin/kafka-console-producer.sh --bootstrap-server localhost:9092 --topic mytopic
Test 
Test 1
```

You should see the messages appended to kafkaconnect.txt in /tmp/ folder.

