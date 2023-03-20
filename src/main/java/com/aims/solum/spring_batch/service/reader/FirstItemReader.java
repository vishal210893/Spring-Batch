package com.aims.solum.spring_batch.service.reader;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.IntStream;

@Component
public class FirstItemReader implements ItemReader<Integer> {

    List<Integer> list = IntStream.rangeClosed(1, 10).boxed().toList();
    int i = 0;

    @Override
    public Integer read() throws ParseException, NonTransientResourceException {
        System.out.println("Inside item reader");
        if (i < list.size()) {
            return list.get(i++);
        }
        i = 0;
        return null;
    }

}