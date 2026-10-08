package com.campusplacement.controller;

import com.campusplacement.exception.*;
import com.campusplacement.model.Student;
import com.campusplacement.service.PlacementService;
import java.util.*;

public class StudentController {
	private final PlacementService service;

	public StudentController(PlacementService service) {
		this.service = service;
	}

	public Student register(String name, String department, int[] marks, Set<String> skills)
			throws PlacementException, StorageException {
		return service.registerStudent(name, department, marks, skills);
	}

	public Student find(int id) {
		return service.findStudent(id);
	}

	public List<Student> getAll() {
		return service.getStudents();
	}
}
