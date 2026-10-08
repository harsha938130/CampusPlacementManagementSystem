package com.campusplacement.model;

public abstract class User {
	private final int id;
	private final String name;

	protected User(int id, String name) {
		if (name == null || name.trim().isEmpty()) {
			throw new IllegalArgumentException("Name cannot be blank.");
		}
		this.id = id;
		this.name = name.trim();
	}

	public final int getId() {
		return id;
	}

	public final String getName() {
		return name;
	}

	public abstract void showProfile();
}
