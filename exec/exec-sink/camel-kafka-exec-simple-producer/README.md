## Camel Simple Exec Producer

To run the producer:

mvn compile exec:exec -Dkafka.topic.name=mytopic -Dkafka.key=1 -Dcamel.body="FileName" -Dcamel.header.detail="detail1"
