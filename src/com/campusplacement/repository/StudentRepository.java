package com.campusplacement.repository;

import com.campusplacement.exception.StorageException;
import com.campusplacement.model.Student;
import com.campusplacement.util.FileManager;
import java.util.List;

public class StudentRepository {
	private final FileManager fileManager;

	public StudentRepository(FileManager fileManager) {
		this.fileManager = fileManager;
	}

	public List<Student> findAll() throws StorageException {
		return fileManager.loadStudents();
	}

	public void saveAll(List<Student> students) throws StorageException {
		fileManager.saveStudents(students);
	}
}
