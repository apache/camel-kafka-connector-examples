# Camel-Kafka-connector AWS S3 to JMS demo

## Introduction

This is an example for Camel-Kafka-connector

## What is needed

- An Artemis Broker 2.9.0 running
- A Kafka Cluster 2.4.0 running 
- An AWS S3 bucket

## Running Kafka

```
$KAFKA_HOME/bin/zookeeper-server-start.sh config/zookeeper.properties
$KAFKA_HOME/bin/kafka-server-start.sh config/server.properties
$KAFKA_HOME/bin/kafka-topics.sh --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic mytopic
```

## Running Artemis

```
$ARTEMIS_HOME/bin/$BROKER_NAME/bin/artemis run
```

## Setting up the needed bits and running the example

You'll need to setup the plugin.path property in your kafka

Open the `$KAFKA_HOME/config/connect-standalone.properties`

and set the `plugin.path` property to your choosen location

In this example we'll use `/home/oscerd/connectors/`

```
> cd /home/oscerd/connectors/
> wget https://repo1.maven.org/maven2/org/apache/camel/kafkaconnector/camel-sjms2-kafka-connector/0.1.0/camel-sjms2-kafka-connector-0.2.0-package.zip
> unzip camel-sjms2-kafka-connector-0.2.0-package.zip
> wget https://repo1.maven.org/maven2/org/apache/camel/kafkaconnector/camel-aws-s3-kafka-connector/0.1.0/camel-aws-s3-kafka-connector-0.2.0-package.zip
> unzip camel-aws-s3-kafka-connector-0.2.0-package.zip
```

Now it's time to setup the connectors

Open the AWSS3 properties file 

```
name=CamelAWSS3SourceConnector
connector.class=org.apache.camel.kafkaconnector.awss3.CamelAwss3SourceConnector
key.converter=org.apache.kafka.connect.storage.StringConverter
value.converter=org.apache.camel.kafkaconnector.awss3.converters.S3ObjectConverter

camel.source.maxPollDuration=10000

camel.source.kafka.topic=mytopic

camel.source.url=aws-s3://camel-kafka-connector?autocloseBody=false

camel.component.aws-s3.access-key=xxxx
camel.component.aws-s3.secret-key=yyyy
camel.component.aws-s3.region=EU_WEST_1
```

and add the correct credentials for AWS.

For the JMS Sink connector you don't need to modify anything, so it is

```
name=CamelJmsSinkConnector
topics=mytopic
tasks.max=1
connector.class=org.apache.camel.kafkaconnector.sjms2.CamelSjms2SinkConnector
key.converter=org.apache.kafka.connect.storage.StringConverter
value.converter=org.apache.kafka.connect.storage.StringConverter

camel.sink.url=sjms2://queue:myqueue

camel.component.sjms2.connection-factory=#class:org.apache.activemq.ActiveMQConnectionFactory
camel.component.sjms2.connection-factory.brokerURL=tcp://localhost:61616
```

Now you can run the example

```
$KAFKA_HOME/bin/connect-standalone.sh $KAFKA_HOME/config/connect-standalone.properties config/CamelAWSS3SourceConnector.properties config/CamelJmsSinkConnector.properties
```

Just connect to your AWS Console and upload a text file to your bucket (we provide the example element.txt in this repo), in the Artemis console you should see a message with the file content in a bit.

