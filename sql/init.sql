DROP TABLE IF EXISTS Grade;
DROP TABLE IF EXISTS Student;

CREATE TABLE Student (
    id INTEGER PRIMARY KEY,
    name VARCHAR(50),
    surname VARCHAR(50),
    birthYear INTEGER,
    groupType VARCHAR(20) 
);

CREATE TABLE Grade (
    student_id INTEGER,
    grade INTEGER,
    FOREIGN KEY (student_id) REFERENCES Student(id)
);
