package com.camdigikey.smsservice.kafka;

import io.confluent.kafka.serializers.KafkaAvroSerializer;
import io.confluent.kafka.serializers.KafkaAvroSerializerConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
@Slf4j
public class KafkaProducerConfig {

  @Value("${spring.kafka.bootstrap-servers}")
  private String bootstrapServers;

  @Value("${spring.kafka.properties.schema.registry.url}")
  private String registry;

  public Map<String, Object> producerConfig() {
    Map<String, Object> props = new HashMap<>();
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class.getName());
    props.put(KafkaAvroSerializerConfig.SCHEMA_REGISTRY_URL_CONFIG, registry);

    return props;
  }

  // generic
  @Bean
  public <V> ProducerFactory<String, V> genericProducerFactory() {
    return new DefaultKafkaProducerFactory<>(producerConfig());
  }

  @Bean
  public <V> KafkaTemplate<String, V> genericKafkaTemplate(ProducerFactory<String, V> genericProducerFactory) {
    return new KafkaTemplate<>(genericProducerFactory);
  }
}
