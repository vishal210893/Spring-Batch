package com.aims.solum.spring_batch.service.writer;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
public class FirstItemWriter implements ItemWriter<Long> {

    @Override
    public void write(Chunk<? extends Long> chunk) {
        System.out.println("Inside Item writer");
        System.out.println(chunk.getItems());
    }

}