PRAGMA foreign_keys = ON;

CREATE TABLE IF NOT EXISTS Student (
    id INTEGER PRIMARY KEY,
    name TEXT NOT NULL,
    surname TEXT NOT NULL,
    birthYear INTEGER NOT NULL,
    groupType TEXT NOT NULL CHECK (groupType IN ('telecom', 'cyber'))
);

CREATE TABLE IF NOT EXISTS Grade (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    student_id INTEGER NOT NULL,
    grade INTEGER NOT NULL CHECK (grade BETWEEN 1 AND 5),
    FOREIGN KEY (student_id) REFERENCES Student(id) ON DELETE CASCADE
);
