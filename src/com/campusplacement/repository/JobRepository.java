package com.campusplacement.repository;

import com.campusplacement.exception.StorageException;
import com.campusplacement.model.Job;
import com.campusplacement.util.FileManager;
import java.util.List;

public class JobRepository {
	private final FileManager fileManager;

	public JobRepository(FileManager fileManager) {
		this.fileManager = fileManager;
	}

	public List<Job> findAll() throws StorageException {
		return fileManager.loadJobs();
	}

	public void saveAll(List<Job> jobs) throws StorageException {
		fileManager.saveJobs(jobs);
	}
}
