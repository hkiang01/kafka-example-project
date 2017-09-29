import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;

import java.util.ArrayList;
import java.util.Properties;

public class KafkaConsumerApp {
    public static void main(String[] args) {
        // full list of producer configs: http://kafka.apache.org/documentation.html#producerconfigs
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092, localhost:9093");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer"); // type contract extends to consumer
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer"); // type contract extends to consumer

        // single thread per consumer (for simplicity, scalability)
        KafkaConsumer<String, String> myConsumer = new KafkaConsumer<String, String>(props);

        ArrayList<String> topics = new ArrayList<String>();
        topics.add("my-topic");
        topics.add("myOtherTopic");

        /*
          Dynamic partition assignment: eventually pull from every partition in every topic
          Automatically adjusts to available partitions
         */
        myConsumer.subscribe(topics); // NOT incremental

        ArrayList<TopicPartition> partitions = new ArrayList<TopicPartition>();
        TopicPartition partition0 = new TopicPartition("my-topic", 0);
        partitions.add(partition0);
        /*
          Specific partitions
         */
        myConsumer.assign(partitions); // NOT incremental

        myConsumer.unsubscribe();
        myConsumer.subscribe(new ArrayList<String>()); // equivalent to unsubscribe

        // Note: KakfaConsumer idle until poll loop (don't need to handle exceptions until here)
        try {
            while(true) {
                 /*
                     timeout: min ms consumer spends time polling for messages
                     when timed out, fetcher returns batch records to in-memory buffer for processing
                     records parsed, deserialized, and grouped into consumer records
                     by topic and partition. Fetcher finishes this process, then returns objects for processing.
                  */
                ConsumerRecords<String, String> records = myConsumer.poll(100L);
                // processing logic goes here...
                // consumers should be LIGHT WEIGHT (each consumer is a single thread)

                // WATCH:https://app.pluralsight.com/player?course=apache-kafka-getting-started&author=ryan-plant&name=apache-kafka-getting-started-m5&clip=8
                // VERY IMPORTANT to understand "las committed offset", "current position" and gap in between "un-committed offsets" (to avoid inconsistency)
                // default behavior: assumed all records automatically committed (enable.auto.commit=true) after 5000ms (auto.commit.interval=5000)
                // read != committed

                // consumer groups
            }
        } finally {
            myConsumer.close();
        }
    }
}
