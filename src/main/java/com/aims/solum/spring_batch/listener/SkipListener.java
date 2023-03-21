package com.aims.solum.spring_batch.listener;

import com.aims.solum.spring_batch.model.StudentCsv;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.annotation.OnSkipInProcess;
import org.springframework.batch.core.annotation.OnSkipInRead;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.util.Date;

@Component
@Slf4j
public class SkipListener {

	@OnSkipInRead
	public void skipInRead(Throwable th) {
		if (th instanceof FlatFileParseException) {
			createFile("D:\\Spring Batch\\src\\main\\resources\\SkipInRead.txt", ((FlatFileParseException) th).getInput());
		}
	}

	@OnSkipInProcess
	public void skipInProcess(StudentCsv studentCsv, Throwable th) {
		log.error(studentCsv + "\nError Message " + th.getMessage());

	}

	public void createFile(String filePath, String data) {
		try (FileWriter fileWriter = new FileWriter(new File(filePath), true)) {
			fileWriter.write(data + "," + new Date() + "\n");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

}