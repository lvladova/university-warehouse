-- Create indexes on dimension tables
-- These indexes will improve join performance

-- Student dimension indexes
CREATE INDEX idx_student_nationality ON STUDENT_DIM_DW (nationality);
CREATE INDEX idx_student_gender ON STUDENT_DIM_DW (gender);

-- Course dimension indexes
CREATE INDEX idx_course_faculty ON COURSE_DIM_DW (faculty_id);

-- Department dimension indexes
CREATE INDEX idx_department_name ON DEPARTMENT_DIM_DW (department_name);

-- Module dimension indexes
CREATE INDEX idx_module_course ON MODULE_DIM_DW (course_id);

-- Program dimension indexes
CREATE INDEX idx_program_department ON PROGRAM_DIM_DW (department_id);

-- Create indexes on fact tables
-- These indexes will improve query performance for analytical queries

-- Enrollment fact indexes
CREATE INDEX idx_enrollment_student ON ENROLLMENT_FACT_DW (student_id);
CREATE INDEX idx_enrollment_module ON ENROLLMENT_FACT_DW (module_id);
CREATE INDEX idx_enrollment_date ON ENROLLMENT_FACT_DW (enrollment_date);

-- Performance fact indexes
CREATE INDEX idx_performance_student ON PERFORMANCE_FACT_DW (student_id);
CREATE INDEX idx_performance_module ON PERFORMANCE_FACT_DW (module_id);
CREATE INDEX idx_performance_year ON PERFORMANCE_FACT_DW (year);
CREATE INDEX idx_performance_grade ON PERFORMANCE_FACT_DW (grade);

-- Admissions fact indexes
CREATE INDEX idx_admissions_student ON ADMISSIONS_FACT_DW (student_id);
CREATE INDEX idx_admissions_program ON ADMISSIONS_FACT_DW (program_id);
CREATE INDEX idx_admissions_date ON ADMISSIONS_FACT_DW (admission_date);
CREATE INDEX idx_admissions_status ON ADMISSIONS_FACT_DW (status);

-- Finance fact indexes
CREATE INDEX idx_finance_student ON FINANCE_FACT_DW (student_id);
CREATE INDEX idx_finance_date ON FINANCE_FACT_DW (transaction_date);
CREATE INDEX idx_finance_method ON FINANCE_FACT_DW (payment_method);

-- Create partitions for large fact tables
-- This improves query performance by only scanning relevant data

-- Partition Performance fact table by year
CREATE TABLE PERFORMANCE_FACT_DW_PART (
                                          performance_id NUMBER PRIMARY KEY,
                                          student_id NUMBER,
                                          module_id NUMBER,
                                          grade VARCHAR2(5),
                                          year NUMBER
)
    PARTITION BY RANGE (year) (
    PARTITION perf_2019 VALUES LESS THAN (2020),
    PARTITION perf_2020 VALUES LESS THAN (2021),
    PARTITION perf_2021 VALUES LESS THAN (2022),
    PARTITION perf_2022 VALUES LESS THAN (2023),
    PARTITION perf_2023 VALUES LESS THAN (2024),
    PARTITION perf_future VALUES LESS THAN (MAXVALUE)
);

-- Partition Enrollment fact table by enrollment date
CREATE TABLE ENROLLMENT_FACT_DW_PART (
                                         enrollment_id NUMBER PRIMARY KEY,
                                         student_id NUMBER,
                                         module_id NUMBER,
                                         enrollment_date DATE,
                                         status VARCHAR2(50)
)
    PARTITION BY RANGE (enrollment_date) (
    PARTITION enroll_2019 VALUES LESS THAN (TO_DATE('01-01-2020', 'DD-MM-YYYY')),
    PARTITION enroll_2020 VALUES LESS THAN (TO_DATE('01-01-2021', 'DD-MM-YYYY')),
    PARTITION enroll_2021 VALUES LESS THAN (TO_DATE('01-01-2022', 'DD-MM-YYYY')),
    PARTITION enroll_2022 VALUES LESS THAN (TO_DATE('01-01-2023', 'DD-MM-YYYY')),
    PARTITION enroll_2023 VALUES LESS THAN (TO_DATE('01-01-2024', 'DD-MM-YYYY')),
    PARTITION enroll_future VALUES LESS THAN (MAXVALUE)
);

-- Partition Finance fact table by transaction date
CREATE TABLE FINANCE_FACT_DW_PART (
                                      finance_id NUMBER PRIMARY KEY,
                                      student_id NUMBER,
                                      amount NUMBER(10,2),
                                      transaction_date DATE,
                                      payment_method VARCHAR2(50)
)
    PARTITION BY RANGE (transaction_date) (
    PARTITION finance_2019 VALUES LESS THAN (TO_DATE('01-01-2020', 'DD-MM-YYYY')),
    PARTITION finance_2020 VALUES LESS THAN (TO_DATE('01-01-2021', 'DD-MM-YYYY')),
    PARTITION finance_2021 VALUES LESS THAN (TO_DATE('01-01-2022', 'DD-MM-YYYY')),
    PARTITION finance_2022 VALUES LESS THAN (TO_DATE('01-01-2023', 'DD-MM-YYYY')),
    PARTITION finance_2023 VALUES LESS THAN (TO_DATE('01-01-2024', 'DD-MM-YYYY')),
    PARTITION finance_future VALUES LESS THAN (MAXVALUE)
);

-- Partition Admissions fact table by admission date
CREATE TABLE ADMISSIONS_FACT_DW_PART (
                                         admission_id NUMBER PRIMARY KEY,
                                         student_id NUMBER,
                                         admission_date DATE,
                                         program_id NUMBER,
                                         status VARCHAR2(50)
)
    PARTITION BY RANGE (admission_date) (
    PARTITION admissions_2019 VALUES LESS THAN (TO_DATE('01-01-2020', 'DD-MM-YYYY')),
    PARTITION admissions_2020 VALUES LESS THAN (TO_DATE('01-01-2021', 'DD-MM-YYYY')),
    PARTITION admissions_2021 VALUES LESS THAN (TO_DATE('01-01-2022', 'DD-MM-YYYY')),
    PARTITION admissions_2022 VALUES LESS THAN (TO_DATE('01-01-2023', 'DD-MM-YYYY')),
    PARTITION admissions_2023 VALUES LESS THAN (TO_DATE('01-01-2024', 'DD-MM-YYYY')),
    PARTITION admissions_future VALUES LESS THAN (MAXVALUE)
);

-- Create bitmap indexes for data warehouse (good for low cardinality columns)
CREATE BITMAP INDEX bix_performance_grade ON PERFORMANCE_FACT_DW (grade);
CREATE BITMAP INDEX bix_enrollment_status ON ENROLLMENT_FACT_DW (status);
CREATE BITMAP INDEX bix_admissions_status ON ADMISSIONS_FACT_DW (status);
CREATE BITMAP INDEX bix_finance_payment ON FINANCE_FACT_DW (payment_method);

-- Create function-based indexes for common expressions
CREATE INDEX idx_student_age_group ON STUDENT_DIM_DW (
                                                      CASE
                                                          WHEN age < 20 THEN 'Under 20'
        WHEN age BETWEEN 20 AND 24 THEN '20-24'
        WHEN age BETWEEN 25 AND 29 THEN '25-29'
        ELSE '30+'
    END
    );

-- Create composite indexes for common query patterns
CREATE INDEX idx_perf_student_module ON PERFORMANCE_FACT_DW (student_id, module_id, year);
CREATE INDEX idx_enroll_student_date ON ENROLLMENT_FACT_DW (student_id, enrollment_date);
CREATE INDEX idx_finance_student_date ON FINANCE_FACT_DW (student_id, transaction_date);
CREATE INDEX idx_admissions_program_date ON ADMISSIONS_FACT_DW (program_id, admission_date, status);