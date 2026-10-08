package com.campusplacement.service;

import com.campusplacement.exception.PlacementException;
import com.campusplacement.exception.StorageException;
import com.campusplacement.model.*;
import com.campusplacement.repository.*;
import com.campusplacement.util.FileManager;
import java.util.*;

public class PlacementService {
	private final List<Student> students;
	private final List<Job> jobs;
	private final List<JobApplication> applications;
	private final StudentRepository studentRepository;
	private final JobRepository jobRepository;
	private final ApplicationRepository applicationRepository;

	public PlacementService(StudentRepository studentRepository, JobRepository jobRepository,
			ApplicationRepository applicationRepository) throws StorageException {
		this.studentRepository = studentRepository;
		this.jobRepository = jobRepository;
		this.applicationRepository = applicationRepository;
		this.students = new ArrayList<>(studentRepository.findAll());
		this.jobs = new ArrayList<>(jobRepository.findAll());
		this.applications = new ArrayList<>(applicationRepository.findAll());
	}

	public static PlacementService createDefault() throws StorageException {
		FileManager fileManager = new FileManager();
		fileManager.ensureDataFiles();
		return new PlacementService(new StudentRepository(fileManager), new JobRepository(fileManager),
				new ApplicationRepository(fileManager));
	}

	public Student registerStudent(String name, String department, int[] marks, Set<String> skills)
			throws PlacementException, StorageException {
		try {
			Student student = new Student(nextStudentId(), name, department, marks, skills);
			students.add(student);
			studentRepository.saveAll(students);
			return student;
		} catch (IllegalArgumentException e) {
			throw new PlacementException(e.getMessage());
		}
	}

	public Job addJob(String company, String title, double minPercentage) throws PlacementException, StorageException {
		try {
			Job job = new Job(nextJobId(), company, title, minPercentage);
			jobs.add(job);
			jobRepository.saveAll(jobs);
			return job;
		} catch (IllegalArgumentException e) {
			throw new PlacementException(e.getMessage());
		}
	}

	public JobApplication applyForJob(int studentId, int jobId) throws PlacementException, StorageException {
		Student student = findStudent(studentId);
		if (student == null)
			throw new PlacementException("Student not found.");
		Job job = findJob(jobId);
		if (job == null)
			throw new PlacementException("Job not found.");
		if (student.getAverage() < job.getMinPercentage()) {
			throw new PlacementException(String.format("Not eligible. Your average is %.2f%%; minimum is %.2f%%.",
					student.getAverage(), job.getMinPercentage()));
		}
		for (JobApplication a : applications) {
			if (a.getStudentId() == studentId && a.getJobId() == jobId)
				throw new PlacementException("You have already applied for this job.");
		}
		JobApplication application = new JobApplication(nextApplicationId(), studentId, jobId,
				ApplicationStatus.PENDING);
		applications.add(application);
		applicationRepository.saveAll(applications);
		return application;
	}

	public void updateStatus(int applicationId, ApplicationStatus status) throws PlacementException, StorageException {
		JobApplication application = findApplication(applicationId);
		if (application == null)
			throw new PlacementException("Application not found.");
		try {
			application.updateStatus(status);
		} catch (IllegalArgumentException | IllegalStateException e) {
			throw new PlacementException(e.getMessage());
		}
		applicationRepository.saveAll(applications);
	}

	public List<Student> getStudents() {
		return Collections.unmodifiableList(students);
	}

	public List<Job> getJobs() {
		return Collections.unmodifiableList(jobs);
	}

	public List<JobApplication> getApplications() {
		return Collections.unmodifiableList(applications);
	}

	public Student findStudent(int id) {
		for (Student s : students)
			if (s.getId() == id)
				return s;
		return null;
	}

	public Job findJob(int id) {
		for (Job j : jobs)
			if (j.getId() == id)
				return j;
		return null;
	}

	public JobApplication findApplication(int id) {
		for (JobApplication a : applications)
			if (a.getId() == id)
				return a;
		return null;
	}

	public List<JobApplication> getApplicationsForStudent(int studentId) {
		List<JobApplication> result = new ArrayList<>();
		for (JobApplication a : applications)
			if (a.getStudentId() == studentId)
				result.add(a);
		return result;
	}

	public Report getReport() {
		long pending = 0, selected = 0, rejected = 0;
		for (JobApplication a : applications)
			switch (a.getStatus()) {
			case PENDING -> pending++;
			case SELECTED -> selected++;
			case REJECTED -> rejected++;
			}
		return new Report(students.size(), jobs.size(), applications.size(), pending, selected, rejected);
	}

	public void saveReport() throws StorageException {
		Report r = getReport();
		applicationRepository.saveReport(r.students(), r.jobs(), r.applications(), r.pending(), r.selected(),
				r.rejected());
	}

	private int nextStudentId() {
		return students.stream().mapToInt(Student::getId).max().orElse(0) + 1;
	}

	private int nextJobId() {
		return jobs.stream().mapToInt(Job::getId).max().orElse(0) + 1;
	}

	private int nextApplicationId() {
		return applications.stream().mapToInt(JobApplication::getId).max().orElse(0) + 1;
	}

	public record Report(int students, int jobs, int applications, long pending, long selected, long rejected) {
	}
}
