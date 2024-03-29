package com.aims.solum.spring_batch.service.processor;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class FirstItemProcessor implements ItemProcessor<Integer, Long> {

    @Override
    public Long process(Integer integer) throws Exception {
        System.out.println("Inside Item processor");
        return Long.valueOf(integer) * 5;
    }

}