-- Stored procedures for common data warehouse queries
-- Using bind variables for better performance through query plan reuse

-- Get module pass/fail rates for a specific academic year
CREATE OR REPLACE PROCEDURE get_module_pass_fail_rates(
    p_year IN NUMBER,
    p_cursor OUT SYS_REFCURSOR
)
AS
BEGIN
OPEN p_cursor FOR
SELECT
    m.module_id,
    m.module_name,
    c.course_name,
    d.department_name,
    COUNT(p.performance_id) as total_students,
    SUM(CASE WHEN p.grade = 'F' THEN 1 ELSE 0 END) as fail_count,
    SUM(CASE WHEN p.grade != 'F' THEN 1 ELSE 0 END) as pass_count,
    ROUND(SUM(CASE WHEN p.grade != 'F' THEN 1 ELSE 0 END) * 100.0 / COUNT(p.performance_id), 2) as pass_rate,
    ROUND(SUM(CASE WHEN p.grade = 'F' THEN 1 ELSE 0 END) * 100.0 / COUNT(p.performance_id), 2) as fail_rate
FROM
    PERFORMANCE_FACT_DW p
        JOIN MODULE_DIM_DW m ON p.module_id = m.module_id
        JOIN COURSE_DIM_DW c ON m.course_id = c.course_id
        JOIN DEPARTMENT_DIM_DW d ON c.department_id = d.department_id
WHERE
    p.year = p_year
GROUP BY
    m.module_id, m.module_name, c.course_name, d.department_name
HAVING
    COUNT(p.performance_id) > 5
ORDER BY
    fail_rate DESC;
END get_module_pass_fail_rates;
/

-- Get enrollment trends by year and faculty
CREATE OR REPLACE PROCEDURE get_enrollment_trends(
    p_start_year IN NUMBER,
    p_end_year IN NUMBER,
    p_cursor OUT SYS_REFCURSOR
)
AS
BEGIN
OPEN p_cursor FOR
SELECT
    EXTRACT(YEAR FROM e.enrollment_date) as year,
            f.faculty_id,
            f.faculty_name,
            COUNT(e.enrollment_id) as enrollment_count
FROM
    ENROLLMENT_FACT_DW e
    JOIN MODULE_DIM_DW m ON e.module_id = m.module_id
    JOIN COURSE_DIM_DW c ON m.course_id = c.course_id
    JOIN FACULTY_DIM_DW f ON c.faculty_id = f.faculty_id
WHERE
    EXTRACT(YEAR FROM e.enrollment_date) BETWEEN p_start_year AND p_end_year
GROUP BY
    EXTRACT(YEAR FROM e.enrollment_date), f.faculty_id, f.faculty_name
ORDER BY
    year, f.faculty_id;
END get_enrollment_trends;
/

-- Get admissions statistics by program
CREATE OR REPLACE PROCEDURE get_admissions_statistics(
    p_year IN NUMBER,
    p_cursor OUT SYS_REFCURSOR
)
AS
BEGIN
OPEN p_cursor FOR
SELECT
    p.program_id,
    p.program_name,
    d.department_name,
    COUNT(CASE WHEN a.status = 'APPLIED' THEN 1 END) as applicant_count,
    COUNT(CASE WHEN a.status = 'ADMITTED' THEN 1 END) as admitted_count,
    ROUND(COUNT(CASE WHEN a.status = 'ADMITTED' THEN 1 END) * 100.0 /
          NULLIF(COUNT(CASE WHEN a.status = 'APPLIED' THEN 1 END), 0), 2) as admission_rate
FROM
    ADMISSIONS_FACT_DW a
        JOIN PROGRAM_DIM_DW p ON a.program_id = p.program_id
        JOIN DEPARTMENT_DIM_DW d ON p.department_id = d.department_id
WHERE
    EXTRACT(YEAR FROM a.admission_date) = p_year
GROUP BY
    p.program_id, p.program_name, d.department_name
ORDER BY
    applicant_count DESC;
END get_admissions_statistics;
/

-- Get finance statistics by year and payment method
CREATE OR REPLACE PROCEDURE get_finance_statistics(
    p_year IN NUMBER,
    p_cursor OUT SYS_REFCURSOR
)
AS
BEGIN
OPEN p_cursor FOR
SELECT
    EXTRACT(YEAR FROM f.transaction_date) as year,
            f.payment_method,
            COUNT(f.finance_id) as transaction_count,
            SUM(f.amount) as total_amount,
            ROUND(AVG(f.amount), 2) as average_amount,
            ROUND(MIN(f.amount), 2) as min_amount,
            ROUND(MAX(f.amount), 2) as max_amount
FROM
    FINANCE_FACT_DW f
WHERE
    EXTRACT(YEAR FROM f.transaction_date) = p_year
GROUP BY
    EXTRACT(YEAR FROM f.transaction_date), f.payment_method
ORDER BY
    f.payment_method;
END get_finance_statistics;
/

-- Get student performance by module (with bind variables)
CREATE OR REPLACE PROCEDURE get_student_performance(
    p_student_id IN NUMBER,
    p_year IN NUMBER,
    p_cursor OUT SYS_REFCURSOR
)
AS
BEGIN
OPEN p_cursor FOR
SELECT
    s.student_id,
    s.name,
    m.module_id,
    m.module_name,
    c.course_name,
    p.grade,
    p.year
FROM
    PERFORMANCE_FACT_DW p
        JOIN STUDENT_DIM_DW s ON p.student_id = s.student_id
        JOIN MODULE_DIM_DW m ON p.module_id = m.module_id
        JOIN COURSE_DIM_DW c ON m.course_id = c.course_id
WHERE
    p.student_id = p_student_id
  AND p.year = p_year
ORDER BY
    m.module_name;
END get_student_performance;
/

-- Get grade distribution by module
CREATE OR REPLACE PROCEDURE get_grade_distribution(
    p_module_id IN NUMBER,
    p_year IN NUMBER,
    p_cursor OUT SYS_REFCURSOR
)
AS
BEGIN
OPEN p_cursor FOR
SELECT
    p.grade,
    COUNT(*) as count,
            ROUND(COUNT(*) * 100.0 / SUM(COUNT(*)) OVER(), 2) as percentage
FROM
    PERFORMANCE_FACT_DW p
WHERE
    p.module_id = p_module_id
  AND p.year = p_year
GROUP BY
    p.grade
ORDER BY
    p.grade;
END get_grade_distribution;
/

-- Get enrollment demographics
CREATE OR REPLACE PROCEDURE get_enrollment_demographics(
    p_year IN NUMBER,
    p_gender_cursor OUT SYS_REFCURSOR,
    p_age_cursor OUT SYS_REFCURSOR,
    p_nationality_cursor OUT SYS_REFCURSOR
)
AS
BEGIN
    -- Gender distribution
OPEN p_gender_cursor FOR
SELECT
    s.gender,
    COUNT(e.enrollment_id) as count,
            ROUND(COUNT(e.enrollment_id) * 100.0 / SUM(COUNT(e.enrollment_id)) OVER(), 2) as percentage
FROM
    ENROLLMENT_FACT_DW e
    JOIN STUDENT_DIM_DW s ON e.student_id = s.student_id
WHERE
    EXTRACT(YEAR FROM e.enrollment_date) = p_year
GROUP BY
    s.gender;

-- Age distribution
OPEN p_age_cursor FOR
SELECT
    CASE
        WHEN s.age < 20 THEN 'Under 20'
        WHEN s.age BETWEEN 20 AND 24 THEN '20-24'
        WHEN s.age BETWEEN 25 AND 29 THEN '25-29'
        ELSE '30+'
        END as age_group,
    COUNT(e.enrollment_id) as count,
            ROUND(COUNT(e.enrollment_id) * 100.0 / SUM(COUNT(e.enrollment_id)) OVER(), 2) as percentage
FROM
    ENROLLMENT_FACT_DW e
    JOIN STUDENT_DIM_DW s ON e.student_id = s.student_id
WHERE
    EXTRACT(YEAR FROM e.enrollment_date) = p_year
GROUP BY
    CASE
    WHEN s.age < 20 THEN 'Under 20'
    WHEN s.age BETWEEN 20 AND 24 THEN '20-24'
    WHEN s.age BETWEEN 25 AND 29 THEN '25-29'
    ELSE '30+'
END
ORDER BY
            age_group;

    -- Nationality distribution
OPEN p_nationality_cursor FOR
SELECT
    s.nationality,
    COUNT(e.enrollment_id) as count,
            ROUND(COUNT(e.enrollment_id) * 100.0 / SUM(COUNT(e.enrollment_id)) OVER(), 2) as percentage
FROM
    ENROLLMENT_FACT_DW e
    JOIN STUDENT_DIM_DW s ON e.student_id = s.student_id
WHERE
    EXTRACT(YEAR FROM e.enrollment_date) = p_year
GROUP BY
    s.nationality
ORDER BY
    count DESC;
END get_enrollment_demographics;
/

-- Get scholarship statistics by faculty
CREATE OR REPLACE PROCEDURE get_scholarship_statistics(
    p_year IN NUMBER,
    p_cursor OUT SYS_REFCURSOR
)
AS
BEGIN
OPEN p_cursor FOR
SELECT
    f.faculty_id,
    f.faculty_name,
    COUNT(DISTINCT fin.student_id) as total_students,
    COUNT(DISTINCT CASE WHEN fin.payment_method = 'SCHOLARSHIP' THEN fin.student_id END) as scholarship_count,
    ROUND(COUNT(DISTINCT CASE WHEN fin.payment_method = 'SCHOLARSHIP' THEN fin.student_id END) * 100.0 /
          COUNT(DISTINCT fin.student_id), 2) as scholarship_percentage,
    SUM(fin.amount) as total_fees,
    SUM(CASE WHEN fin.payment_method = 'SCHOLARSHIP' THEN fin.amount ELSE 0 END) as scholarship_amount
FROM
    FINANCE_FACT_DW fin
        JOIN STUDENT_DIM_DW s ON fin.student_id = s.student_id
        JOIN ENROLLMENT_FACT_DW e ON s.student_id = e.student_id
        JOIN MODULE_DIM_DW m ON e.module_id = m.module_id
        JOIN COURSE_DIM_DW c ON m.course_id = c.course_id
        JOIN FACULTY_DIM_DW f ON c.faculty_id = f.faculty_id
WHERE
    EXTRACT(YEAR FROM fin.transaction_date) = p_year
GROUP BY
    f.faculty_id, f.faculty_name
ORDER BY
    scholarship_percentage DESC;
END get_scholarship_statistics;
/

-- Get first-year dropout rate by faculty
CREATE OR REPLACE PROCEDURE get_dropout_rate_by_faculty(
    p_year IN NUMBER,
    p_cursor OUT SYS_REFCURSOR
)
AS
BEGIN
OPEN p_cursor FOR
SELECT
    f.faculty_id,
    f.faculty_name,
    COUNT(e.enrollment_id) as total_enrollments,
    COUNT(CASE WHEN e.status = 'DROPPED' THEN 1 END) as dropout_count,
    ROUND(COUNT(CASE WHEN e.status = 'DROPPED' THEN 1 END) * 100.0 / COUNT(e.enrollment_id), 2) as dropout_rate
FROM
    ENROLLMENT_FACT_DW e
        JOIN MODULE_DIM_DW m ON e.module_id = m.module_id
        JOIN COURSE_DIM_DW c ON m.course_id = c.course_id
        JOIN FACULTY_DIM_DW f ON c.faculty_id = f.faculty_id
WHERE
    EXTRACT(YEAR FROM e.enrollment_date) = p_year
GROUP BY
    f.faculty_id, f.faculty_name
ORDER BY
    dropout_rate DESC;
END get_dropout_rate_by_faculty;
/