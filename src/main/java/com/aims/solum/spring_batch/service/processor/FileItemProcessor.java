package com.aims.solum.spring_batch.service.processor;

import com.aims.solum.spring_batch.model.StudentCsv;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class FileItemProcessor implements ItemProcessor<StudentCsv, StudentCsv> {

	@Override
	public StudentCsv process(StudentCsv item) {
		if (item.getId() == 12) {
			System.out.println("Exception");
			throw new NullPointerException();
		}
		return item;
	}

}