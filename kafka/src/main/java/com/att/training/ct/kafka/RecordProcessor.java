package com.att.training.ct.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RecordProcessor {
    public void process(Object key, Object value) {
        log.info("Processed record: key={}, value={}", key, value);
    }
}
