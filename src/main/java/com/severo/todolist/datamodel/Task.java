package com.severo.todolist.datamodel;

import java.time.LocalDate;

public class Task {
	private String description;
	private String details;
	private LocalDate expirationDate;

	public Task(String description, String details, LocalDate expirationDate) {
		this.description = description;
		this.details = details;
		this.expirationDate = expirationDate;
	}

	@Override
	public String toString() {
		return description;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public LocalDate getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(LocalDate expirationDate) {
		this.expirationDate = expirationDate;
	}
}
