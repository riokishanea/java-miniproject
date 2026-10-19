CREATE DATABASE IF NOT EXISTS student_skill_passport;
USE student_skill_passport;

CREATE TABLE IF NOT EXISTS users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    role ENUM('STUDENT', 'FACULTY') NOT NULL
);

CREATE TABLE IF NOT EXISTS student_profiles (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL UNIQUE,
    admission_no VARCHAR(30) NOT NULL UNIQUE,
    department VARCHAR(100) NOT NULL,
    semester INT NOT NULL,
    phone VARCHAR(20),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS skills (
    id INT PRIMARY KEY AUTO_INCREMENT,
    student_id INT NOT NULL,
    skill_name VARCHAR(100) NOT NULL,
    skill_level ENUM('Beginner', 'Intermediate', 'Advanced') NOT NULL,
    FOREIGN KEY (student_id) REFERENCES student_profiles(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS projects (
    id INT PRIMARY KEY AUTO_INCREMENT,
    student_id INT NOT NULL,
    title VARCHAR(150) NOT NULL,
    technology VARCHAR(150),
    description VARCHAR(500),
    FOREIGN KEY (student_id) REFERENCES student_profiles(id) ON DELETE CASCADE
);

INSERT IGNORE INTO users (id, name, email, password, role) VALUES
    (1, 'College Faculty', 'faculty@college.com', 'faculty123', 'FACULTY'),
    (2, 'Aarav Sharma', 'aarav@student.com', 'student123', 'STUDENT'),
    (3, 'Diya Patel', 'diya@student.com', 'student123', 'STUDENT'),
    (4, 'Rohan Verma', 'rohan@student.com', 'student123', 'STUDENT');

INSERT IGNORE INTO student_profiles
    (id, user_id, admission_no, department, semester, phone)
VALUES
    (1, 2, 'CSE001', 'Computer Science', 5, '9876543210'),
    (2, 3, 'ECE002', 'Electronics', 3, '9876501234'),
    (3, 4, 'ME003', 'Mechanical', 7, '9876512340');

INSERT IGNORE INTO skills (id, student_id, skill_name, skill_level) VALUES
    (1, 1, 'Java', 'Intermediate'),
    (2, 1, 'MySQL', 'Beginner'),
    (3, 2, 'Embedded C', 'Intermediate'),
    (4, 3, 'AutoCAD', 'Advanced');

INSERT IGNORE INTO projects
    (id, student_id, title, technology, description)
VALUES
    (1, 1, 'Library System', 'Java, MySQL', 'A desktop system for managing library books.'),
    (2, 2, 'Smart Irrigation', 'Arduino, C', 'A sensor-based automatic watering project.'),
    (3, 3, 'Gear Design', 'AutoCAD', 'A basic mechanical gear design and simulation.');

