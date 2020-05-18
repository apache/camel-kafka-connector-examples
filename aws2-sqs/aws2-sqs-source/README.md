# Camel-Kafka-connector AWS2 SQS Source

## Introduction

This is an example for Camel-Kafka-connector AW2-SQS

## What is needed

- An AWS SQS queue

## Running Kafka

```
$KAFKA_HOME/bin/zookeeper-server-start.sh config/zookeeper.properties
$KAFKA_HOME/bin/kafka-server-start.sh config/server.properties
$KAFKA_HOME/bin/kafka-topics.sh --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic test
```

## Setting up the needed bits and running the example

You'll need to setup the plugin.path property in your kafka

Open the `$KAFKA_HOME/config/connect-standalone.properties`

and set the `plugin.path` property to your choosen location

In this example we'll use `/home/oscerd/connectors/`

```
> cd /home/oscerd/connectors/
> wget https://repo1.maven.org/maven2/org/apache/camel/kafkaconnector/camel-aws2-sqs-kafka-connector/0.1.0/camel-aws2-sqs-kafka-connector-0.1.0-package.zip
> unzip camel-aws2-sqs-kafka-connector-0.1.0-package.zip
```

Now it's time to setup the connectors

Open the AWS2 SQS configuration file

```
name=CamelAWS2SQSSourceConnector
connector.class=org.apache.camel.kafkaconnector.aws2sqs.CamelAws2sqsSourceConnector
key.converter=org.apache.kafka.connect.storage.StringConverter
value.converter=org.apache.kafka.connect.storage.StringConverter

camel.source.maxPollDuration=10000

camel.source.kafka.topic=mytopic

camel.source.url=aws2-sqs://camel-1?deleteAfterRead=false&deleteIfFiltered=true

camel.component.aws2-sqs.access-key=xxxx
camel.component.aws2-sqs.secret-key=yyyy
camel.component.aws2-sqs.region=eu-west-1
```

and add the correct credentials for AWS.

Now you can run the example

```
$KAFKA_HOME/bin/connect-standalone.sh $KAFKA_HOME/config/connect-standalone.properties config/CamelAWSS3SourceConnector.properties config/CamelAWS2SQSSourceConnector.properties
```

Just connect to your AWS Console and send message to the camel-1 queue, through the AWS Console.

On a different terminal run the kafka-consumer and you should see messages from the SQS queue arriving through Kafka Broker.

```
bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic mytopic --from-beginning
SQS to Kafka through Camel
SQS to Kafka through Camel
```

