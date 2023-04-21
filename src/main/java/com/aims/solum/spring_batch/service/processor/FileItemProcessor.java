package com.aims.solum.spring_batch.service.processor;

import com.aims.solum.spring_batch.model.StudentCsv;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class FileItemProcessor implements ItemProcessor<StudentCsv, StudentCsv> {

    @Override
    public StudentCsv process(StudentCsv item) {
        if (item.getId() == 8) {
            System.out.println("Exception");
            throw new NullPointerException();
        }
        if (item.getId() > 6 && item.getId() <= 9) {
            System.out.println(item.getId() + " Hello");
        }
        return item;
    }

}