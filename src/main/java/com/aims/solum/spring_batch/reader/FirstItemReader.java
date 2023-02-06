package com.aims.solum.spring_batch.reader;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.IntStream;

@Component
public class FirstItemReader implements ItemReader<Integer> {

    List<Integer> list = IntStream.rangeClosed(1, 10).boxed().toList();
    int i = 0;

    @Override
    public Integer read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        System.out.println("Inside item reader");
        if (i < list.size()) {
            return list.get(i++);
        } else {
            return null;
        }
    }

}