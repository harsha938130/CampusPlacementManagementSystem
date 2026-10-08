package com.campusplacement.controller;

import com.campusplacement.exception.*;
import com.campusplacement.model.*;
import com.campusplacement.service.PlacementService;
import java.util.*;

public class Main {
	private final Scanner scanner = new Scanner(System.in);
	private final StudentController students;
	private final JobController jobs;
	private final ApplicationController applications;

	public Main(PlacementService service) {
		students = new StudentController(service);
		jobs = new JobController(service);
		applications = new ApplicationController(service);
	}

	public static void main(String[] args) {
		try {
			new Main(PlacementService.createDefault()).run();
		} catch (StorageException e) {
			System.out.println("Startup failed: " + e.getMessage());
		}
	}

	private void run() {
		while (true) {
			System.out.println("\n=== CAMPUS PLACEMENT MANAGEMENT SYSTEM ===");
			System.out.println("1. Register student\n2. Admin menu\n3. Student menu\n0. Exit");
			int choice = readInt("Choose: ");
			try {
				switch (choice) {
				case 1 -> register();
				case 2 -> adminMenu();
				case 3 -> studentMenu();
				case 0 -> {
					System.out.println("Thank you. Goodbye!");
					return;
				}
				default -> System.out.println("Invalid choice.");
				}
			} catch (PlacementException | StorageException e) {
				System.out.println("Operation failed: " + e.getMessage());
			}
		}
	}

	private void register() throws PlacementException, StorageException {
		String name = readNonBlank("Name: ");
		String department = readNonBlank("Department: ");
		int[] marks = readMarks();
		Set<String> skills = readSkills();
		Student s = students.register(name, department, marks, skills);
		System.out.println("Student saved. Student ID: " + s.getId());
		System.out.printf("Average: %.2f%%%n", s.getAverage());
	}

	private void adminMenu() throws PlacementException, StorageException {
		while (true) {
			System.out.println(
					"\n--- ADMIN MENU ---\n1. View all students\n2. Add a job\n3. View all jobs\n4. View all applications\n5. Update application status\n6. View reports\n0. Back");
			int choice = readInt("Choose: ");
			switch (choice) {
			case 1 -> showStudents();
			case 2 -> addJob();
			case 3 -> showJobs();
			case 4 -> showApplications(applications.getAll());
			case 5 -> updateStatus();
			case 6 -> showReport();
			case 0 -> {
				return;
			}
			default -> System.out.println("Invalid choice.");
			}
		}
	}

	private void studentMenu() throws PlacementException, StorageException {
		int id = readInt("Enter your student ID: ");
		if (students.find(id) == null) {
			System.out.println("Student not found.");
			return;
		}
		while (true) {
			System.out.println("\n--- STUDENT MENU (ID " + id
					+ ") ---\n1. View my profile\n2. View jobs\n3. Apply for a job\n4. View my applications\n0. Back");
			int choice = readInt("Choose: ");
			switch (choice) {
			case 1 -> students.find(id).showProfile();
			case 2 -> showJobs();
			case 3 -> apply(id);
			case 4 -> showApplications(applications.getForStudent(id));
			case 0 -> {
				return;
			}
			default -> System.out.println("Invalid choice.");
			}
		}
	}

	private void showStudents() {
		if (students.getAll().isEmpty()) {
			System.out.println("No records found.");
			return;
		}
		for (Student s : students.getAll()) {
			System.out.println();
			s.showProfile();
		}
	}

	private void addJob() throws PlacementException, StorageException {
		Job j = jobs.addJob(readNonBlank("Company name: "), readNonBlank("Job title: "),
				readPercentage("Minimum average percentage: "));
		System.out.println("Job saved. Job ID: " + j.getId());
	}

	private void showJobs() {
		if (jobs.getAll().isEmpty()) {
			System.out.println("No records found.");
			return;
		}
		for (Job j : jobs.getAll())
			j.showDetails();
	}

	private void showApplications(List<JobApplication> list) {
		if (list.isEmpty()) {
			System.out.println("No records found.");
			return;
		}
		for (JobApplication a : list)
			System.out.println("Application " + a.getId() + " | Student " + a.getStudentId() + " | Job " + a.getJobId()
					+ " - " + applications.getJobTitle(a.getJobId()) + " | " + a.getStatus());
	}

	private void apply(int studentId) throws PlacementException, StorageException {
		JobApplication a = applications.apply(studentId, readInt("Job ID: "));
		System.out.println("Application " + a.getId() + " saved. Status: " + a.getStatus());
	}

	private void updateStatus() throws PlacementException, StorageException {
		int id = readInt("Application ID: ");
		System.out.println("1. SELECTED\n2. REJECTED");
		int c = readInt("Choose new status: ");
		if (c != 1 && c != 2) {
			System.out.println("Invalid status choice.");
			return;
		}
		applications.updateStatus(id, c == 1 ? ApplicationStatus.SELECTED : ApplicationStatus.REJECTED);
		System.out.println("Application status saved.");
	}

	private void showReport() throws StorageException {
		PlacementService.Report r = applications.getReport();
		System.out.println("\n--- REPORT ---");
		System.out.println("Students: " + r.students());
		System.out.println("Jobs: " + r.jobs());
		System.out.println("Applications: " + r.applications());
		System.out.println("PENDING applications: " + r.pending());
		System.out.println("SELECTED applications: " + r.selected());
		System.out.println("REJECTED applications: " + r.rejected());
		applications.saveReport();
		System.out.println("Report saved to data/report.txt");
	}

	private int readInt(String prompt) {
		while (true) {
			System.out.print(prompt);
			try {
				return Integer.parseInt(scanner.nextLine().trim());
			} catch (NumberFormatException e) {
				System.out.println("Please enter a valid integer.");
			}
		}
	}

	private double readPercentage(String prompt) {
		while (true) {
			System.out.print(prompt);
			try {
				double v = Double.parseDouble(scanner.nextLine().trim());
				if (v >= 0 && v <= 100)
					return v;
			} catch (NumberFormatException ignored) {
			}
			System.out.println("Enter a number from 0 to 100.");
		}
	}

	private String readNonBlank(String prompt) {
		while (true) {
			System.out.print(prompt);
			String s = scanner.nextLine().trim();
			if (!s.isEmpty() && !s.contains("|") && !s.contains("\n") && !s.contains("\r"))
				return s;
			System.out.println("This field cannot be blank or contain |.");
		}
	}

	private int[] readMarks() {
		while (true) {
			System.out.print("Enter 6 semester marks separated by spaces: ");
			String[] p = scanner.nextLine().trim().split("\\s+");
			if (p.length != 6) {
				System.out.println("Enter exactly 6 marks.");
				continue;
			}
			int[] m = new int[6];
			try {
				for (int i = 0; i < 6; i++) {
					m[i] = Integer.parseInt(p[i]);
					if (m[i] < 0 || m[i] > 100)
						throw new NumberFormatException();
				}
				return m;
			} catch (NumberFormatException e) {
				System.out.println("Each mark must be an integer from 0 to 100.");
			}
		}
	}

	private Set<String> readSkills() {
		System.out.print("Enter skills separated by commas (or press Enter): ");
		Set<String> s = new HashSet<>();
		String line = scanner.nextLine().trim();
		if (!line.isEmpty())
			for (String x : line.split(",")) {
				String n = x.trim().toUpperCase(Locale.ROOT);
				if (!n.isEmpty())
					s.add(n);
			}
		return s;
	}
}
