package com.campusplacement.model;

public class Job {
	private final int id;
	private final String companyName;
	private final String title;
	private final double minPercentage;

	public Job(int id, String companyName, String title, double minPercentage) {
		if (companyName == null || companyName.trim().isEmpty())
			throw new IllegalArgumentException("Company name cannot be blank.");
		if (title == null || title.trim().isEmpty())
			throw new IllegalArgumentException("Job title cannot be blank.");
		if (minPercentage < 0 || minPercentage > 100)
			throw new IllegalArgumentException("Minimum percentage must be between 0 and 100.");
		validateText(companyName, "Company name");
		validateText(title, "Job title");
		this.id = id;
		this.companyName = companyName.trim();
		this.title = title.trim();
		this.minPercentage = minPercentage;
	}

	private static void validateText(String value, String label) {
		if (value.contains("|") || value.contains("\n") || value.contains("\r"))
			throw new IllegalArgumentException(label + " contains invalid characters.");
	}

	public int getId() {
		return id;
	}

	public String getCompanyName() {
		return companyName;
	}

	public String getTitle() {
		return title;
	}

	public double getMinPercentage() {
		return minPercentage;
	}

	public void showDetails() {
		System.out.printf("Job ID: %d | %s | %s | Minimum: %.2f%%%n", id, companyName, title, minPercentage);
	}
}
