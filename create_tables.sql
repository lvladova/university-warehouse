-- Drop Tables if Exist
BEGIN
EXECUTE IMMEDIATE 'DROP TABLE Student_Dim';
EXECUTE IMMEDIATE 'DROP TABLE Faculty_Dim';
EXECUTE IMMEDIATE 'DROP TABLE Department_Dim';
EXECUTE IMMEDIATE 'DROP TABLE Program_Dim';
EXECUTE IMMEDIATE 'DROP TABLE Course_Dim';
EXECUTE IMMEDIATE 'DROP TABLE Module_Dim';
EXECUTE IMMEDIATE 'DROP TABLE Time_Dim';
EXECUTE IMMEDIATE 'DROP TABLE Book_Dim';
EXECUTE IMMEDIATE 'DROP TABLE Enrollment_Fact';
EXECUTE IMMEDIATE 'DROP TABLE Student_Performance_Fact';
EXECUTE IMMEDIATE 'DROP TABLE Admissions_Fact';
EXECUTE IMMEDIATE 'DROP TABLE Finance_Fact';
EXECUTE IMMEDIATE 'DROP TABLE Library_Usage_Fact';
EXCEPTION
    WHEN OTHERS THEN
        NULL;
END;
/

-- Create Fact Tables
CREATE TABLE Enrollment_Fact (
                                 enrollment_id NUMBER PRIMARY KEY,
                                 student_id NUMBER,
                                 faculty_id NUMBER,
                                 program_id NUMBER,
                                 year NUMBER,
                                 status VARCHAR2(20)
) TABLESPACE S4108689;

CREATE TABLE Student_Performance_Fact (
                                          performance_id NUMBER PRIMARY KEY,
                                          student_id NUMBER,
                                          course_id NUMBER,
                                          module_id NUMBER,
                                          year NUMBER,
                                          grade VARCHAR2(5),
                                          status VARCHAR2(20)
) TABLESPACE S4108689;

CREATE TABLE Admissions_Fact (
                                 application_id NUMBER PRIMARY KEY,
                                 program_id NUMBER,
                                 admitted_flag CHAR(1),
                                 year NUMBER
) TABLESPACE S4108689;

CREATE TABLE Finance_Fact (
                              finance_id NUMBER PRIMARY KEY,
                              student_id NUMBER,
                              faculty_id NUMBER,
                              amount_paid NUMBER,
                              scholarship_flag CHAR(1),
                              year NUMBER
) TABLESPACE S4108689;

CREATE TABLE Library_Usage_Fact (
                                    usage_id NUMBER PRIMARY KEY,
                                    student_id NUMBER,
                                    book_id NUMBER,
                                    department_id NUMBER,
                                    borrow_date DATE
) TABLESPACE S4108689;

-- Create Dimension Tables
CREATE TABLE Student_Dim (
                             student_id NUMBER PRIMARY KEY,
                             name VARCHAR2(100),
                             age NUMBER,
                             gender VARCHAR2(10),
                             nationality VARCHAR2(50)
) TABLESPACE S4108689;

CREATE TABLE Faculty_Dim (
                             faculty_id NUMBER PRIMARY KEY,
                             faculty_name VARCHAR2(100)
) TABLESPACE S4108689;

CREATE TABLE Department_Dim (
                                department_id NUMBER PRIMARY KEY,
                                department_name VARCHAR2(100),
                                faculty_id NUMBER
) TABLESPACE S4108689;

CREATE TABLE Program_Dim (
                             program_id NUMBER PRIMARY KEY,
                             program_name VARCHAR2(100),
                             faculty_id NUMBER
) TABLESPACE S4108689;

CREATE TABLE Course_Dim (
                            course_id NUMBER PRIMARY KEY,
                            course_name VARCHAR2(100),
                            department_id NUMBER
) TABLESPACE S4108689;

CREATE TABLE Module_Dim (
                            module_id NUMBER PRIMARY KEY,
                            module_name VARCHAR2(100),
                            course_id NUMBER
) TABLESPACE S4108689;

CREATE TABLE Time_Dim (
                          date_id NUMBER PRIMARY KEY,
                          year NUMBER,
                          semester VARCHAR2(20),
                          month VARCHAR2(20)
) TABLESPACE S4108689;

CREATE TABLE Book_Dim (
                          book_id NUMBER PRIMARY KEY,
                          title VARCHAR2(200),
                          author VARCHAR2(100),
                          category VARCHAR2(50)
) TABLESPACE S4108689;
/
