# Resources
* [Pluralsight](https://app.pluralsight.com/library/courses/apache-kafka-getting-started/table-of-contents)
* [Kafka documentation](https://kafka.apache.org/documentation/)

# Common shell commands

Start zookeeper
```bash
bin/zookeeper-server-start.sh config/zookeeper.properties
```
Create a topic
```bash
bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic my-topic
```
List topics
```bash
bin/kafka-topics.sh --zookeeper localhost:2181 --list
```

Producer: `src/main/java/KafkaProducerApp`
Consumer: `src/main/java/KafkaConsumerApp`
