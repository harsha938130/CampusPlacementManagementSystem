# Campus Placement Management System

Java console-based application using a layered architecture.

## Project Structure

```text
CampusPlacementManagementSystem
│
└── src
    └── com.campusplacement
        │
        ├── controller
        │   ├── Main.java
        │   ├── StudentController.java
        │   ├── JobController.java
        │   └── ApplicationController.java
        │
        ├── service
        │   └── PlacementService.java
        │
        ├── repository
        │   ├── StudentRepository.java
        │   ├── JobRepository.java
        │   └── ApplicationRepository.java
        │
        ├── model
        │   ├── User.java
        │   ├── Admin.java
        │   ├── Student.java
        │   ├── Job.java
        │   ├── JobApplication.java
        │   └── ApplicationStatus.java
        │
        ├── exception
        │   ├── PlacementException.java
        │   └── StorageException.java
        │
        └── util
            └── FileManager.java
```

## Layers

`Main/Controllers → PlacementService → Repositories → FileManager → data files`

Models are used by all required layers; exceptions handle business and storage errors.

## Features

- Student registration
- Six semester marks and average calculation
- Unique skills using `HashSet`
- Admin job creation
- Eligibility checking
- Duplicate application prevention
- Pending/Selected/Rejected application status
- Reports
- Persistent text-file storage

## Run

Use JDK 21. In Eclipse, import the project as an existing Java project and run:

`com.campusplacement.controller.Main`
