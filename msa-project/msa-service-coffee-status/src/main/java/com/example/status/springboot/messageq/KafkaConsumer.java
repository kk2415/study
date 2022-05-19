package com.example.status.springboot.messageq;

import com.example.status.springboot.repository.Status;
import com.example.status.springboot.repository.StatusDto;
import com.example.status.springboot.repository.StatusJpaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class KafkaConsumer {

    @Autowired
    private StatusJpaRepository statusJpaRepository;

    @KafkaListener(topics = "kafka-test")
    public void processMessage(String kafkaMessage) {
        log.info("kafkaMesasge : {}", kafkaMessage);

        statusJpaRepository.save(new Status(kafkaMessage));
    }

}
