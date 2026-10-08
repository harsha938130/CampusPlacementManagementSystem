package com.campusplacement.util;

import com.campusplacement.exception.StorageException;
import com.campusplacement.model.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

public class FileManager {
    private final Path dataDir;
    private final Path studentsFile;
    private final Path jobsFile;
    private final Path applicationsFile;
    private final Path reportFile;

    public FileManager() {
        this(Paths.get("data"));
    }

    public FileManager(Path dataDir) {
        this.dataDir = dataDir;
        this.studentsFile = dataDir.resolve("students.txt");
        this.jobsFile = dataDir.resolve("jobs.txt");
        this.applicationsFile = dataDir.resolve("applications.txt");
        this.reportFile = dataDir.resolve("report.txt");
    }

    public void ensureDataFiles() throws StorageException {
        try {
            Files.createDirectories(dataDir);
            createIfMissing(studentsFile);
            createIfMissing(jobsFile);
            createIfMissing(applicationsFile);
        } catch (IOException e) {
            throw new StorageException("Unable to initialize data files.", e);
        }
    }

    private void createIfMissing(Path file) throws IOException {
        if (!Files.exists(file)) Files.createFile(file);
    }

    public List<Student> loadStudents() throws StorageException {
        List<Student> list = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(studentsFile, StandardCharsets.UTF_8)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) continue;
                String[] p = line.split("\\|", -1);
                if (p.length != 5) throw new IOException("Invalid student record: " + line);
                int id = Integer.parseInt(p[0]);
                String[] markParts = p[3].split(",", -1);
                if (markParts.length != Student.SEMESTER_COUNT) throw new IOException("Invalid marks for student " + id);
                int[] marks = new int[Student.SEMESTER_COUNT];
                for (int i = 0; i < marks.length; i++) marks[i] = Integer.parseInt(markParts[i]);
                Set<String> skills = parseSkills(p[4]);
                list.add(new Student(id, p[1], p[2], marks, skills));
            }
            return list;
        } catch (Exception e) {
            throw new StorageException("Unable to load students.txt.", e);
        }
    }

    public List<Job> loadJobs() throws StorageException {
        List<Job> list = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(jobsFile, StandardCharsets.UTF_8)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) continue;
                String[] p = line.split("\\|", -1);
                if (p.length != 4) throw new IOException("Invalid job record: " + line);
                list.add(new Job(Integer.parseInt(p[0]), p[1], p[2], Double.parseDouble(p[3])));
            }
            return list;
        } catch (Exception e) {
            throw new StorageException("Unable to load jobs.txt.", e);
        }
    }

    public List<JobApplication> loadApplications() throws StorageException {
        List<JobApplication> list = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(applicationsFile, StandardCharsets.UTF_8)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) continue;
                String[] p = line.split("\\|", -1);
                if (p.length != 4) throw new IOException("Invalid application record: " + line);
                list.add(new JobApplication(Integer.parseInt(p[0]), Integer.parseInt(p[1]), Integer.parseInt(p[2]), ApplicationStatus.valueOf(p[3])));
            }
            return list;
        } catch (Exception e) {
            throw new StorageException("Unable to load applications.txt.", e);
        }
    }

    public void saveStudents(List<Student> students) throws StorageException {
        try (BufferedWriter writer = Files.newBufferedWriter(studentsFile, StandardCharsets.UTF_8)) {
            for (Student s : students) {
                String marks = Arrays.stream(s.getMarks()).mapToObj(String::valueOf).reduce((a,b) -> a + "," + b).orElse("");
                String skills = String.join(",", new TreeSet<>(s.getSkills()));
                writer.write(s.getId() + "|" + s.getName() + "|" + s.getDepartment() + "|" + marks + "|" + skills);
                writer.newLine();
            }
        } catch (IOException e) { throw new StorageException("Unable to save students.txt.", e); }
    }

    public void saveJobs(List<Job> jobs) throws StorageException {
        try (BufferedWriter writer = Files.newBufferedWriter(jobsFile, StandardCharsets.UTF_8)) {
            for (Job j : jobs) {
                writer.write(j.getId() + "|" + j.getCompanyName() + "|" + j.getTitle() + "|" + j.getMinPercentage());
                writer.newLine();
            }
        } catch (IOException e) { throw new StorageException("Unable to save jobs.txt.", e); }
    }

    public void saveApplications(List<JobApplication> applications) throws StorageException {
        try (BufferedWriter writer = Files.newBufferedWriter(applicationsFile, StandardCharsets.UTF_8)) {
            for (JobApplication a : applications) {
                writer.write(a.getId() + "|" + a.getStudentId() + "|" + a.getJobId() + "|" + a.getStatus());
                writer.newLine();
            }
        } catch (IOException e) { throw new StorageException("Unable to save applications.txt.", e); }
    }

    public void saveReport(int studentCount, int jobCount, int applicationCount, long pending, long selected, long rejected) throws StorageException {
        try (BufferedWriter writer = Files.newBufferedWriter(reportFile, StandardCharsets.UTF_8)) {
            writer.write("Campus Placement Management System Report"); writer.newLine();
            writer.write("Students: " + studentCount); writer.newLine();
            writer.write("Jobs: " + jobCount); writer.newLine();
            writer.write("Applications: " + applicationCount); writer.newLine();
            writer.write("PENDING applications: " + pending); writer.newLine();
            writer.write("SELECTED applications: " + selected); writer.newLine();
            writer.write("REJECTED applications: " + rejected); writer.newLine();
        } catch (IOException e) { throw new StorageException("Unable to save report.txt.", e); }
    }

    private Set<String> parseSkills(String raw) {
        Set<String> skills = new HashSet<>();
        if (raw == null || raw.isBlank()) return skills;
        for (String skill : raw.split(",")) if (!skill.isBlank()) skills.add(skill.trim());
        return skills;
    }
}
