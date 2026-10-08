package com.campusplacement.model;

public class JobApplication {
    private final int id;
    private final int studentId;
    private final int jobId;
    private ApplicationStatus status;

    public JobApplication(int id, int studentId, int jobId, ApplicationStatus status) {
        if (status == null) throw new IllegalArgumentException("Status cannot be null.");
        this.id = id;
        this.studentId = studentId;
        this.jobId = jobId;
        this.status = status;
    }

    public int getId() { return id; }
    public int getStudentId() { return studentId; }
    public int getJobId() { return jobId; }
    public ApplicationStatus getStatus() { return status; }

    public void updateStatus(ApplicationStatus newStatus) {
        if (newStatus == null) throw new IllegalArgumentException("Status cannot be null.");
        if (status != ApplicationStatus.PENDING) throw new IllegalStateException("A decided application cannot be changed.");
        if (newStatus == ApplicationStatus.PENDING) throw new IllegalArgumentException("New status must be SELECTED or REJECTED.");
        status = newStatus;
    }

    public void restoreStatus(ApplicationStatus restoredStatus) {
        if (restoredStatus == null) throw new IllegalArgumentException("Status cannot be null.");
        status = restoredStatus;
    }
}
