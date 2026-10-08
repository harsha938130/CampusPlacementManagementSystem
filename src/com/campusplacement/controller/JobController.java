package com.campusplacement.controller;

import com.campusplacement.exception.*;
import com.campusplacement.model.Job;
import com.campusplacement.service.PlacementService;
import java.util.*;

public class JobController {
	private final PlacementService service;

	public JobController(PlacementService service) {
		this.service = service;
	}

	public Job addJob(String company, String title, double min) throws PlacementException, StorageException {
		return service.addJob(company, title, min);
	}

	public Job find(int id) {
		return service.findJob(id);
	}

	public List<Job> getAll() {
		return service.getJobs();
	}
}
