package com.campusplacement.model;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Student extends User {
	public static final int SEMESTER_COUNT = 6;

	private final String department;
	private final int[] marks;
	private final Set<String> skills;

	public Student(int id, String name, String department, int[] marks, Set<String> skills) {
		super(id, name);
		if (department == null || department.trim().isEmpty()) {
			throw new IllegalArgumentException("Department cannot be blank.");
		}
		if (marks == null || marks.length != SEMESTER_COUNT) {
			throw new IllegalArgumentException("Exactly six semester marks are required.");
		}
		for (int mark : marks) {
			if (mark < 0 || mark > 100) {
				throw new IllegalArgumentException("Marks must be between 0 and 100.");
			}
		}
		this.department = department.trim();
		this.marks = marks.clone();
		this.skills = new HashSet<>();
		if (skills != null) {
			for (String skill : skills) {
				if (skill != null && !skill.trim().isEmpty()) {
					String normalized = skill.trim().toUpperCase(java.util.Locale.ROOT);
					if (normalized.contains(",") || normalized.contains("|") || normalized.contains("\n")
							|| normalized.contains("\r")) {
						throw new IllegalArgumentException("Skills cannot contain commas, pipes, or line breaks.");
					}
					this.skills.add(normalized);
				}
			}
		}
	}

	public String getDepartment() {
		return department;
	}

	public int[] getMarks() {
		return marks.clone();
	}

	public Set<String> getSkills() {
		return Collections.unmodifiableSet(skills);
	}

	public double getAverage() {
		int total = 0;
		for (int mark : marks)
			total += mark;
		return (double) total / marks.length;
	}

	@Override
	public void showProfile() {
		System.out.println("Student ID: " + getId());
		System.out.println("Name: " + getName());
		System.out.println("Department: " + department);
		System.out.println("Semester Marks: " + java.util.Arrays.toString(marks));
		System.out.printf("Average: %.2f%%%n", getAverage());
		System.out.println("Skills: " + (skills.isEmpty() ? "None" : String.join(", ", skills)));
	}
}
