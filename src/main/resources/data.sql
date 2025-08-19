-- Sample data for Mini Leave Management System

-- Insert employees
INSERT INTO employees (name, email, department, role, manager_id) VALUES
('John Smith', 'john.smith@company.com', 'Engineering', 'Engineering Manager', NULL),
('Alice Johnson', 'alice.johnson@company.com', 'Engineering', 'Senior Developer', 1),
('Bob Wilson', 'bob.wilson@company.com', 'Engineering', 'Developer', 1),
('Carol Brown', 'carol.brown@company.com', 'Engineering', 'QA Engineer', 1),
('David Miller', 'david.miller@company.com', 'HR', 'HR Manager', NULL),
('Emma Davis', 'emma.davis@company.com', 'HR', 'HR Specialist', 5),
('Frank Garcia', 'frank.garcia@company.com', 'Marketing', 'Marketing Manager', NULL),
('Grace Lee', 'grace.lee@company.com', 'Marketing', 'Marketing Specialist', 7),
('Henry Clark', 'henry.clark@company.com', 'Finance', 'Finance Manager', NULL),
('Ivy Rodriguez', 'ivy.rodriguez@company.com', 'Finance', 'Accountant', 9);

-- Insert sample leave requests
INSERT INTO leave_requests (employee_id, leave_type, start_date, end_date, reason, status, applied_date, approved_by, approved_date, comments) VALUES
(2, 'ANNUAL', '2024-02-15', '2024-02-19', 'Family vacation to Hawaii', 'APPROVED', '2024-01-15', 1, '2024-01-16', 'Enjoy your vacation!'),
(3, 'SICK', '2024-01-22', '2024-01-23', 'Flu symptoms, need rest', 'APPROVED', '2024-01-22', 1, '2024-01-22', 'Get well soon'),
(4, 'CASUAL', '2024-02-28', '2024-03-01', 'Personal work to be completed', 'PENDING', '2024-02-20', NULL, NULL, NULL),
(6, 'ANNUAL', '2024-03-15', '2024-03-22', 'Spring break vacation with family', 'PENDING', '2024-02-25', NULL, NULL, NULL),
(8, 'SICK', '2024-01-18', '2024-01-19', 'Medical appointment and recovery', 'REJECTED', '2024-01-17', 7, '2024-01-18', 'Please reschedule for less busy period'),
(10, 'CASUAL', '2024-02-12', '2024-02-12', 'Moving to new apartment', 'APPROVED', '2024-02-05', 9, '2024-02-06', 'Good luck with the move!'),
(2, 'CASUAL', '2024-03-25', '2024-03-25', 'Attending wedding ceremony', 'PENDING', '2024-03-01', NULL, NULL, NULL),
(3, 'ANNUAL', '2024-04-10', '2024-04-17', 'Easter holidays with extended family', 'PENDING', '2024-03-10', NULL, NULL, NULL);