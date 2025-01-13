# Client Appointment Tracker

A comprehensive **Client Appointment Tracker** built using Java, designed to efficiently manage patient records and appointment scheduling.

---

## Features

### 1. Robust Domain Design
- Implemented domain entities:
  - **Patient**: Includes attributes like ID, first name, last name, and age.
  - **Appointment**: Includes attributes like ID, associated `Patient`, date, and purpose.
- Ensured accurate real-world relationships:
  - Each **Appointment** is uniquely associated with one **Patient**.
  - A **Patient** can have multiple **Appointments**.
- Enforced constraints to prevent overlapping **Appointments**.

---

### 2. Flexible Data Storage
- Developed **generic repositories** supporting multiple storage backends:
  - **TextFileRepository**: Stores entities in plain text files.
  - **BinaryFileRepository**: Uses Java's serialization mechanism for binary file storage.
  - **SQL Repository**: Manages entities in a relational database, with 100+ pseudo-randomly generated entries using tools like Java Faker.

---

### 3. Dynamic Configuration
- Used a `settings.properties` file for runtime configuration:
  - Allows seamless switching between storage backends.
  - Configurable file locations for input/output data.

Example configuration:
```properties
Repository = binary
Patients = “patients.bin”
Appointments = “appointments.bin”
```

### 4. Advanced Exception Handling
- Created a custom exception hierarchy for error management:
  - Examples: `RepositoryException`, `DuplicateIDException`, `ObjectNotFoundException`.
- Handles validation errors, duplicate IDs, and file input/output errors gracefully.

---

### 5. Comprehensive CRUD Operations
- Provided full **CRUD** functionality:
  - Create, Read, Update, and Delete operations for both **Patient** and **Appointment** entities.
- User-friendly interface for efficient data management with JavaFX.
