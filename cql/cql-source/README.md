# Camel-Kafka-connector CQL Source

## Introduction

This is an example for Camel-Kafka-connector CQL

## What is needed

- A Cassandra instance

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
> wget https://repo1.maven.org/maven2/org/apache/camel/kafkaconnector/camel-cql-kafka-connector/0.2.0/camel-cql-kafka-connector-0.2.0-package.zip
> unzip camel-cql-kafka-connector-0.2.0-package.zip
```

## Setting up Apache Cassandra

This examples require a running Cassandra instance, for simplicity the steps below show how to start Cassandra using Docker. First you'll need to run a Cassandra instance:

[source,bash]
----
docker run --name master_node --env MAX_HEAP_SIZE='800M' -dt oscerd/cassandra
----

Next, check and make sure Cassandra is running:

[source,bash]
----
docker exec -ti master_node /opt/cassandra/bin/nodetool status
Datacenter: datacenter1
=======================
Status=Up/Down
|/ State=Normal/Leaving/Joining/Moving
--  Address     Load       Tokens       Owns (effective)  Host ID                               Rack
UN  172.17.0.2  251.32 KiB  256          100.0%            5126aaad-f143-43e9-920a-0f9540a93967  rack1
----

To populate the database using to the `cqlsh` tool, you'll need a local installation of Cassandra. Download and extract the Apache Cassandra distribution to a directory. We reference the Cassandra installation directory with `LOCAL_CASSANDRA_HOME`. Here we use version 3.11.4 to connect to the Cassandra instance we started using Docker.

[source,bash]
----
<LOCAL_CASSANDRA_HOME>/bin/cqlsh $(docker inspect --format='{{ .NetworkSettings.IPAddress }}' master_node)
----

Next, execute the following script to create keyspace `test`, the table `users` and insert one row into it.

[source,bash]
----
create keyspace test with replication = {'class':'SimpleStrategy', 'replication_factor':3};
use test;
create table users ( id int primary key, name text );
insert into users (id,name) values (1, 'oscerd');
quit;
----

In the configuration `.properties` file we use below the IP address of the Cassandra master node needs to be configured, replace the value `172.17.0.2` configuration property with the IP of the master node obtained from Docker. Each example uses a different `.properties` file shown in the command line to run the example.

[source,bash]
----
docker inspect --format='{{ .NetworkSettings.IPAddress }}' master_node
----

Now it's time to setup the connectors

Open the CQL Source configuration file

```
name=CamelCassandraQLSourceConnector
connector.class=org.apache.camel.kafkaconnector.cql.CamelCqlSourceConnector
key.converter=org.apache.kafka.connect.storage.StringConverter
value.converter=org.apache.kafka.connect.storage.StringConverter

camel.source.kafka.topic=mytopic

camel.source.path.hosts=172.17.0.2
camel.source.path.keyspace=9042/test
camel.source.endpoint.cql=select * from users
```

Now you can run the example

```
$KAFKA_HOME/bin/connect-standalone.sh $KAFKA_HOME/config/connect-standalone.properties config/CamelCassandraQLSourceConnector.properties
```

On a different terminal run the kafka-consumer and you should see messages to Kafka from Cassandra

```
bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic mytopic --from-beginning
[Row[1, oscerd]]
```
You can verify the behavior through the following command

