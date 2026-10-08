package com.campusplacement.controller;

import com.campusplacement.exception.*;
import com.campusplacement.model.*;
import com.campusplacement.service.PlacementService;
import java.util.*;

public class ApplicationController {
	private final PlacementService service;

	public ApplicationController(PlacementService service) {
		this.service = service;
	}

	public JobApplication apply(int studentId, int jobId) throws PlacementException, StorageException {
		return service.applyForJob(studentId, jobId);
	}

	public void updateStatus(int id, ApplicationStatus status) throws PlacementException, StorageException {
		service.updateStatus(id, status);
	}

	public List<JobApplication> getAll() {
		return service.getApplications();
	}

	public List<JobApplication> getForStudent(int studentId) {
		return service.getApplicationsForStudent(studentId);
	}

	public PlacementService.Report getReport() {
		return service.getReport();
	}

	public void saveReport() throws StorageException {
		service.saveReport();
	}

	public String getJobTitle(int jobId) {
		Job job = service.findJob(jobId);
		return job == null ? "Unknown job" : job.getTitle();
	}
}
