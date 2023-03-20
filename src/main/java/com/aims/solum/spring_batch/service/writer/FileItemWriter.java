package com.aims.solum.spring_batch.service.writer;

import com.aims.solum.spring_batch.model.StudentCsv;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
public class FileItemWriter implements ItemWriter<StudentCsv> {

    @Override
    public void write(Chunk<? extends StudentCsv> chunk) throws Exception {
        System.out.println("---------------------- Inside Item Writer ----------------------");
        chunk.getItems().stream().forEach(System.out::println);
    }
}