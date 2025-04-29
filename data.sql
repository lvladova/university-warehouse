-- Sample data for University Warehouse System - Dimension Tables
-- This script populates the dimension tables in the operational database

-- Clear existing dimension data
DELETE FROM MODULE_DIM;
DELETE FROM COURSE_DIM;
DELETE FROM PROGRAM_DIM;
DELETE FROM DEPARTMENT_DIM;
DELETE FROM FACULTY_DIM;
DELETE FROM STUDENT_DIM;
DELETE FROM TIME_DIM;
DELETE FROM BOOK_DIM;

-- Insert data into FACULTY_DIM
INSERT INTO FACULTY_DIM (faculty_id, faculty_name) VALUES (1, 'Faculty of Science');
INSERT INTO FACULTY_DIM (faculty_id, faculty_name) VALUES (2, 'Faculty of Arts');
INSERT INTO FACULTY_DIM (faculty_id, faculty_name) VALUES (3, 'Faculty of Engineering');
INSERT INTO FACULTY_DIM (faculty_id, faculty_name) VALUES (4, 'Faculty of Business');

-- Insert data into DEPARTMENT_DIM
INSERT INTO DEPARTMENT_DIM (department_id, department_name, faculty_id) VALUES (101, 'Computer Science', 1);
INSERT INTO DEPARTMENT_DIM (department_id, department_name, faculty_id) VALUES (102, 'Mathematics', 1);
INSERT INTO DEPARTMENT_DIM (department_id, department_name, faculty_id) VALUES (103, 'Physics', 1);
INSERT INTO DEPARTMENT_DIM (department_id, department_name, faculty_id) VALUES (104, 'Chemistry', 1);
INSERT INTO DEPARTMENT_DIM (department_id, department_name, faculty_id) VALUES (201, 'English', 2);
INSERT INTO DEPARTMENT_DIM (department_id, department_name, faculty_id) VALUES (202, 'History', 2);
INSERT INTO DEPARTMENT_DIM (department_id, department_name, faculty_id) VALUES (203, 'Philosophy', 2);
INSERT INTO DEPARTMENT_DIM (department_id, department_name, faculty_id) VALUES (301, 'Mechanical Engineering', 3);
INSERT INTO DEPARTMENT_DIM (department_id, department_name, faculty_id) VALUES (302, 'Electrical Engineering', 3);
INSERT INTO DEPARTMENT_DIM (department_id, department_name, faculty_id) VALUES (303, 'Civil Engineering', 3);
INSERT INTO DEPARTMENT_DIM (department_id, department_name, faculty_id) VALUES (401, 'Accounting', 4);
INSERT INTO DEPARTMENT_DIM (department_id, department_name, faculty_id) VALUES (402, 'Marketing', 4);
INSERT INTO DEPARTMENT_DIM (department_id, department_name, faculty_id) VALUES (403, 'Finance', 4);

-- Insert data into PROGRAM_DIM
INSERT INTO PROGRAM_DIM (program_id, program_name, department_id) VALUES (1001, 'BSc Computer Science', 101);
INSERT INTO PROGRAM_DIM (program_id, program_name, department_id) VALUES (1002, 'BSc Data Science', 101);
INSERT INTO PROGRAM_DIM (program_id, program_name, department_id) VALUES (1003, 'BSc Mathematics', 102);
INSERT INTO PROGRAM_DIM (program_id, program_name, department_id) VALUES (1004, 'BSc Physics', 103);
INSERT INTO PROGRAM_DIM (program_id, program_name, department_id) VALUES (1005, 'BSc Chemistry', 104);
INSERT INTO PROGRAM_DIM (program_id, program_name, department_id) VALUES (2001, 'BA English Literature', 201);
INSERT INTO PROGRAM_DIM (program_id, program_name, department_id) VALUES (2002, 'BA History', 202);
INSERT INTO PROGRAM_DIM (program_id, program_name, department_id) VALUES (2003, 'BA Philosophy', 203);
INSERT INTO PROGRAM_DIM (program_id, program_name, department_id) VALUES (3001, 'BEng Mechanical Engineering', 301);
INSERT INTO PROGRAM_DIM (program_id, program_name, department_id) VALUES (3002, 'BEng Electrical Engineering', 302);
INSERT INTO PROGRAM_DIM (program_id, program_name, department_id) VALUES (3003, 'BEng Civil Engineering', 303);
INSERT INTO PROGRAM_DIM (program_id, program_name, department_id) VALUES (4001, 'BSc Accounting', 401);
INSERT INTO PROGRAM_DIM (program_id, program_name, department_id) VALUES (4002, 'BSc Marketing', 402);
INSERT INTO PROGRAM_DIM (program_id, program_name, department_id) VALUES (4003, 'BSc Finance', 403);

-- Insert data into COURSE_DIM
INSERT INTO COURSE_DIM (course_id, course_name, department_id) VALUES (10001, 'Programming Fundamentals', 101);
INSERT INTO COURSE_DIM (course_id, course_name, department_id) VALUES (10002, 'Database Systems', 101);
INSERT INTO COURSE_DIM (course_id, course_name, department_id) VALUES (10003, 'Data Structures and Algorithms', 101);
INSERT INTO COURSE_DIM (course_id, course_name, department_id) VALUES (10004, 'Web Development', 101);
INSERT INTO COURSE_DIM (course_id, course_name, department_id) VALUES (10005, 'Artificial Intelligence', 101);
INSERT INTO COURSE_DIM (course_id, course_name, department_id) VALUES (10201, 'Calculus', 102);
INSERT INTO COURSE_DIM (course_id, course_name, department_id) VALUES (10202, 'Linear Algebra', 102);
INSERT INTO COURSE_DIM (course_id, course_name, department_id) VALUES (10203, 'Discrete Mathematics', 102);
INSERT INTO COURSE_DIM (course_id, course_name, department_id) VALUES (10301, 'Mechanics', 103);
INSERT INTO COURSE_DIM (course_id, course_name, department_id) VALUES (10302, 'Quantum Physics', 103);
INSERT INTO COURSE_DIM (course_id, course_name, department_id) VALUES (10401, 'Organic Chemistry', 104);
INSERT INTO COURSE_DIM (course_id, course_name, department_id) VALUES (10402, 'Inorganic Chemistry', 104);
INSERT INTO COURSE_DIM (course_id, course_name, department_id) VALUES (20101, 'English Literature', 201);
INSERT INTO COURSE_DIM (course_id, course_name, department_id) VALUES (20201, 'World History', 202);
INSERT INTO COURSE_DIM (course_id, course_name, department_id) VALUES (20301, 'Ethics', 203);
INSERT INTO COURSE_DIM (course_id, course_name, department_id) VALUES (30101, 'Mechanical Design', 301);
INSERT INTO COURSE_DIM (course_id, course_name, department_id) VALUES (30201, 'Circuit Theory', 302);
INSERT INTO COURSE_DIM (course_id, course_name, department_id) VALUES (30301, 'Structural Engineering', 303);
INSERT INTO COURSE_DIM (course_id, course_name, department_id) VALUES (40101, 'Financial Accounting', 401);
INSERT INTO COURSE_DIM (course_id, course_name, department_id) VALUES (40201, 'Marketing Principles', 402);
INSERT INTO COURSE_DIM (course_id, course_name, department_id) VALUES (40301, 'Financial Management', 403);

-- Insert data into MODULE_DIM
INSERT INTO MODULE_DIM (module_id, module_name, course_id) VALUES (100001, 'Introduction to Programming', 10001);
INSERT INTO MODULE_DIM (module_id, module_name, course_id) VALUES (100002, 'Object-Oriented Programming', 10001);
INSERT INTO MODULE_DIM (module_id, module_name, course_id) VALUES (100003, 'Database Design', 10002);
INSERT INTO MODULE_DIM (module_id, module_name, course_id) VALUES (100004, 'SQL Programming', 10002);
INSERT INTO MODULE_DIM (module_id, module_name, course_id) VALUES (100005, 'Data Structures', 10003);
INSERT INTO MODULE_DIM (module_id, module_name, course_id) VALUES (100006, 'Algorithms', 10003);
INSERT INTO MODULE_DIM (module_id, module_name, course_id) VALUES (100007, 'HTML and CSS', 10004);
INSERT INTO MODULE_DIM (module_id, module_name, course_id) VALUES (100008, 'JavaScript', 10004);
INSERT INTO MODULE_DIM (module_id, module_name, course_id) VALUES (100009, 'Machine Learning', 10005);
INSERT INTO MODULE_DIM (module_id, module_name, course_id) VALUES (100010, 'Neural Networks', 10005);
INSERT INTO MODULE_DIM (module_id, module_name, course_id) VALUES (102001, 'Calculus I', 10201);
INSERT INTO MODULE_DIM (module_id, module_name, course_id) VALUES (102002, 'Calculus II', 10201);
INSERT INTO MODULE_DIM (module_id, module_name, course_id) VALUES (102003, 'Linear Algebra I', 10202);
INSERT INTO MODULE_DIM (module_id, module_name, course_id) VALUES (103001, 'Classical Mechanics', 10301);
INSERT INTO MODULE_DIM (module_id, module_name, course_id) VALUES (103002, 'Quantum Mechanics', 10302);
INSERT INTO MODULE_DIM (module_id, module_name, course_id) VALUES (104001, 'Organic Chemistry I', 10401);
INSERT INTO MODULE_DIM (module_id, module_name, course_id) VALUES (104002, 'Inorganic Chemistry I', 10402);
INSERT INTO MODULE_DIM (module_id, module_name, course_id) VALUES (201001, 'Shakespeare', 20101);
INSERT INTO MODULE_DIM (module_id, module_name, course_id) VALUES (202001, 'Modern History', 20201);
INSERT INTO MODULE_DIM (module_id, module_name, course_id) VALUES (203001, 'Moral Philosophy', 20301);
INSERT INTO MODULE_DIM (module_id, module_name, course_id) VALUES (301001, 'Engineering Drawing', 30101);
INSERT INTO MODULE_DIM (module_id, module_name, course_id) VALUES (302001, 'Electronic Circuits', 30201);
INSERT INTO MODULE_DIM (module_id, module_name, course_id) VALUES (303001, 'Concrete Structures', 30301);
INSERT INTO MODULE_DIM (module_id, module_name, course_id) VALUES (401001, 'Financial Statements', 40101);
INSERT INTO MODULE_DIM (module_id, module_name, course_id) VALUES (402001, 'Marketing Strategy', 40201);
INSERT INTO MODULE_DIM (module_id, module_name, course_id) VALUES (403001, 'Investment Analysis', 40301);

-- Insert data into STUDENT_DIM
-- Computer Science Students
INSERT INTO STUDENT_DIM (student_id, name, age, gender, nationality) VALUES (1, 'John Smith', 21, 'Male', 'British');
INSERT INTO STUDENT_DIM (student_id, name, age, gender, nationality) VALUES (2, 'Emma Johnson', 20, 'Female', 'British');
INSERT INTO STUDENT_DIM (student_id, name, age, gender, nationality) VALUES (3, 'Mohammed Ali', 22, 'Male', 'Pakistani');
INSERT INTO STUDENT_DIM (student_id, name, age, gender, nationality) VALUES (4, 'Li Wei', 21, 'Male', 'Chinese');
INSERT INTO STUDENT_DIM (student_id, name, age, gender, nationality) VALUES (5, 'Sophia Chen', 19, 'Female', 'Chinese');
-- Mathematics Students
INSERT INTO STUDENT_DIM (student_id, name, age, gender, nationality) VALUES (6, 'David Wilson', 20, 'Male', 'British');
INSERT INTO STUDENT_DIM (student_id, name, age, gender, nationality) VALUES (7, 'Olivia Brown', 21, 'Female', 'British');
INSERT INTO STUDENT_DIM (student_id, name, age, gender, nationality) VALUES (8, 'Elena Petrova', 22, 'Female', 'Russian');
-- Physics Students
INSERT INTO STUDENT_DIM (student_id, name, age, gender, nationality) VALUES (9, 'Thomas Williams', 21, 'Male', 'British');
INSERT INTO STUDENT_DIM (student_id, name, age, gender, nationality) VALUES (10, 'James Davis', 20, 'Male', 'American');
-- Chemistry Students
INSERT INTO STUDENT_DIM (student_id, name, age, gender, nationality) VALUES (11, 'Michael Thompson', 22, 'Male', 'British');
INSERT INTO STUDENT_DIM (student_id, name, age, gender, nationality) VALUES (12, 'Emily Wilson', 21, 'Female', 'British');
-- English Students
INSERT INTO STUDENT_DIM (student_id, name, age, gender, nationality) VALUES (13, 'Charlotte Taylor', 20, 'Female', 'British');
INSERT INTO STUDENT_DIM (student_id, name, age, gender, nationality) VALUES (14, 'Oliver White', 21, 'Male', 'British');
-- History Students
INSERT INTO STUDENT_DIM (student_id, name, age, gender, nationality) VALUES (15, 'Sophie Martin', 22, 'Female', 'French');
INSERT INTO STUDENT_DIM (student_id, name, age, gender, nationality) VALUES (16, 'Benjamin Clark', 20, 'Male', 'British');
-- Philosophy Students
INSERT INTO STUDENT_DIM (student_id, name, age, gender, nationality) VALUES (17, 'Isabella Rodriguez', 21, 'Female', 'Spanish');
INSERT INTO STUDENT_DIM (student_id, name, age, gender, nationality) VALUES (18, 'William Evans', 22, 'Male', 'British');
-- Mechanical Engineering Students
INSERT INTO STUDENT_DIM (student_id, name, age, gender, nationality) VALUES (19, 'Liam Johnson', 21, 'Male', 'British');
INSERT INTO STUDENT_DIM (student_id, name, age, gender, nationality) VALUES (20, 'Amara Okafor', 20, 'Female', 'Nigerian');
-- Electrical Engineering Students
INSERT INTO STUDENT_DIM (student_id, name, age, gender, nationality) VALUES (21, 'Raj Patel', 22, 'Male', 'Indian');
INSERT INTO STUDENT_DIM (student_id, name, age, gender, nationality) VALUES (22, 'Zoe Campbell', 21, 'Female', 'British');
-- Civil Engineering Students
INSERT INTO STUDENT_DIM (student_id, name, age, gender, nationality) VALUES (23, 'Daniel Kim', 20, 'Male', 'Korean');
INSERT INTO STUDENT_DIM (student_id, name, age, gender, nationality) VALUES (24, 'Maya Singh', 21, 'Female', 'Indian');
-- Accounting Students
INSERT INTO STUDENT_DIM (student_id, name, age, gender, nationality) VALUES (25, 'Samuel Thompson', 22, 'Male', 'British');
INSERT INTO STUDENT_DIM (student_id, name, age, gender, nationality) VALUES (26, 'Victoria Nguyen', 21, 'Female', 'Vietnamese');
-- Marketing Students
INSERT INTO STUDENT_DIM (student_id, name, age, gender, nationality) VALUES (27, 'Alexander Wright', 20, 'Male', 'British');
INSERT INTO STUDENT_DIM (student_id, name, age, gender, nationality) VALUES (28, 'Grace Lee', 21, 'Female', 'Korean');
-- Finance Students
INSERT INTO STUDENT_DIM (student_id, name, age, gender, nationality) VALUES (29, 'Ethan Clark', 22, 'Male', 'British');
INSERT INTO STUDENT_DIM (student_id, name, age, gender, nationality) VALUES (30, 'Chloe Martinez', 21, 'Female', 'Spanish');

-- Insert data into TIME_DIM
INSERT INTO TIME_DIM (date_id, year, semester, month) VALUES (1, 2019, 'Fall', 'September');
INSERT INTO TIME_DIM (date_id, year, semester, month) VALUES (2, 2019, 'Fall', 'October');
INSERT INTO TIME_DIM (date_id, year, semester, month) VALUES (3, 2019, 'Fall', 'November');
INSERT INTO TIME_DIM (date_id, year, semester, month) VALUES (4, 2019, 'Fall', 'December');
INSERT INTO TIME_DIM (date_id, year, semester, month) VALUES (5, 2020, 'Spring', 'January');
INSERT INTO TIME_DIM (date_id, year, semester, month) VALUES (6, 2020, 'Spring', 'February');
INSERT INTO TIME_DIM (date_id, year, semester, month) VALUES (7, 2020, 'Spring', 'March');
INSERT INTO TIME_DIM (date_id, year, semester, month) VALUES (8, 2020, 'Spring', 'April');
INSERT INTO TIME_DIM (date_id, year, semester, month) VALUES (9, 2020, 'Spring', 'May');
INSERT INTO TIME_DIM (date_id, year, semester, month) VALUES (10, 2020, 'Fall', 'September');
INSERT INTO TIME_DIM (date_id, year, semester, month) VALUES (11, 2020, 'Fall', 'October');
INSERT INTO TIME_DIM (date_id, year, semester, month) VALUES (12, 2020, 'Fall', 'November');
INSERT INTO TIME_DIM (date_id, year, semester, month) VALUES (13, 2020, 'Fall', 'December');
INSERT INTO TIME_DIM (date_id, year, semester, month) VALUES (14, 2021, 'Spring', 'January');
INSERT INTO TIME_DIM (date_id, year, semester, month) VALUES (15, 2021, 'Spring', 'February');
INSERT INTO TIME_DIM (date_id, year, semester, month) VALUES (16, 2021, 'Spring', 'March');
INSERT INTO TIME_DIM (date_id, year, semester, month) VALUES (17, 2021, 'Spring', 'April');
INSERT INTO TIME_DIM (date_id, year, semester, month) VALUES (18, 2021, 'Spring', 'May');
INSERT INTO TIME_DIM (date_id, year, semester, month) VALUES (19, 2021, 'Fall', 'September');
INSERT INTO TIME_DIM (date_id, year, semester, month) VALUES (20, 2021, 'Fall', 'October');
INSERT INTO TIME_DIM (date_id, year, semester, month) VALUES (21, 2021, 'Fall', 'November');
INSERT INTO TIME_DIM (date_id, year, semester, month) VALUES (22, 2021, 'Fall', 'December');
INSERT INTO TIME_DIM (date_id, year, semester, month) VALUES (23, 2022, 'Spring', 'January');
INSERT INTO TIME_DIM (date_id, year, semester, month) VALUES (24, 2022, 'Spring', 'February');
INSERT INTO TIME_DIM (date_id, year, semester, month) VALUES (25, 2022, 'Spring', 'March');
INSERT INTO TIME_DIM (date_id, year, semester, month) VALUES (26, 2022, 'Spring', 'April');
INSERT INTO TIME_DIM (date_id, year, semester, month) VALUES (27, 2022, 'Spring', 'May');
INSERT INTO TIME_DIM (date_id, year, semester, month) VALUES (28, 2022, 'Fall', 'September');
INSERT INTO TIME_DIM (date_id, year, semester, month) VALUES (29, 2022, 'Fall', 'October');
INSERT INTO TIME_DIM (date_id, year, semester, month) VALUES (30, 2022, 'Fall', 'November');
INSERT INTO TIME_DIM (date_id, year, semester, month) VALUES (31, 2022, 'Fall', 'December');
INSERT INTO TIME_DIM (date_id, year, semester, month) VALUES (32, 2023, 'Spring', 'January');
INSERT INTO TIME_DIM (date_id, year, semester, month) VALUES (33, 2023, 'Spring', 'February');
INSERT INTO TIME_DIM (date_id, year, semester, month) VALUES (34, 2023, 'Spring', 'March');
INSERT INTO TIME_DIM (date_id, year, semester, month) VALUES (35, 2023, 'Spring', 'April');
INSERT INTO TIME_DIM (date_id, year, semester, month) VALUES (36, 2023, 'Spring', 'May');
INSERT INTO TIME_DIM (date_id, year, semester, month) VALUES (37, 2023, 'Fall', 'September');
INSERT INTO TIME_DIM (date_id, year, semester, month) VALUES (38, 2023, 'Fall', 'October');
INSERT INTO TIME_DIM (date_id, year, semester, month) VALUES (39, 2023, 'Fall', 'November');
INSERT INTO TIME_DIM (date_id, year, semester, month) VALUES (40, 2023, 'Fall', 'December');

-- Insert data into BOOK_DIM
INSERT INTO BOOK_DIM (book_id, title, author, category) VALUES (1, 'Introduction to Algorithms', 'Cormen et al.', 'Computer Science');
INSERT INTO BOOK_DIM (book_id, title, author, category) VALUES (2, 'Database System Concepts', 'Silberschatz et al.', 'Computer Science');
INSERT INTO BOOK_DIM (book_id, title, author, category) VALUES (3, 'Calculus: Early Transcendentals', 'Stewart', 'Mathematics');
INSERT INTO BOOK_DIM (book_id, title, author, category) VALUES (4, 'Linear Algebra and Its Applications', 'Lay', 'Mathematics');
INSERT INTO BOOK_DIM (book_id, title, author, category) VALUES (5, 'Physics for Scientists and Engineers', 'Serway and Jewett', 'Physics');
INSERT INTO BOOK_DIM (book_id, title, author, category) VALUES (6, 'Organic Chemistry', 'Wade', 'Chemistry');
INSERT INTO BOOK_DIM (book_id, title, author, category) VALUES (7, 'The Norton Anthology of English Literature', 'Greenblatt', 'English');
INSERT INTO BOOK_DIM (book_id, title, author, category) VALUES (8, 'A History of Western Philosophy', 'Russell', 'Philosophy');
INSERT INTO BOOK_DIM (book_id, title, author, category) VALUES (9, 'Engineering Mechanics', 'Hibbeler', 'Mechanical Engineering');
INSERT INTO BOOK_DIM (book_id, title, author, category) VALUES (10, 'Electric Circuits', 'Nilsson and Riedel', 'Electrical Engineering');
INSERT INTO BOOK_DIM (book_id, title, author, category) VALUES (11, 'Principles of Foundation Engineering', 'Das', 'Civil Engineering');
INSERT INTO BOOK_DIM (book_id, title, author, category) VALUES (12, 'Financial Accounting', 'Harrison', 'Accounting');
INSERT INTO BOOK_DIM (book_id, title, author, category) VALUES (13, 'Marketing Management', 'Kotler', 'Marketing');
INSERT INTO BOOK_DIM (book_id, title, author, category) VALUES (14, 'Principles of Corporate Finance', 'Brealey et al.', 'Finance');
INSERT INTO BOOK_DIM (book_id, title, author, category) VALUES (15, 'Artificial Intelligence: A Modern Approach', 'Russell and Norvig', 'Computer Science');