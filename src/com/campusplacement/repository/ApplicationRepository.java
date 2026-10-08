package com.campusplacement.repository;

import com.campusplacement.exception.StorageException;
import com.campusplacement.model.JobApplication;
import com.campusplacement.util.FileManager;
import java.util.List;

public class ApplicationRepository {
	private final FileManager fileManager;

	public ApplicationRepository(FileManager fileManager) {
		this.fileManager = fileManager;
	}

	public List<JobApplication> findAll() throws StorageException {
		return fileManager.loadApplications();
	}

	public void saveAll(List<JobApplication> applications) throws StorageException {
		fileManager.saveApplications(applications);
	}

	public void saveReport(int students, int jobs, int applications, long pending, long selected, long rejected)
			throws StorageException {
		fileManager.saveReport(students, jobs, applications, pending, selected, rejected);
	}
}
