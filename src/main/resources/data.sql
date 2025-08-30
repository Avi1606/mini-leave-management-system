-- Sample data for Mini Leave Management System

-- Insert employees (with joining_date and annual_leave_balance calculated pro-rata)
INSERT INTO employees (name, email, department, role, manager_id, joining_date, annual_leave_balance) VALUES
('John Smith', 'john.smith@company.com', 'Engineering', 'Engineering Manager', NULL, '2023-01-01', 20),
('Alice Johnson', 'alice.johnson@company.com', 'Engineering', 'Senior Developer', 1, '2023-03-15', 16), -- Pro-rated from March
('Bob Wilson', 'bob.wilson@company.com', 'Engineering', 'Developer', 1, '2023-05-10', 13), -- Pro-rated from May
('Carol Brown', 'carol.brown@company.com', 'Engineering', 'QA Engineer', 1, '2023-06-01', 12), -- Pro-rated from June
('David Miller', 'david.miller@company.com', 'HR', 'HR Manager', NULL, '2022-11-20', 20),
('Emma Davis', 'emma.davis@company.com', 'HR', 'HR Specialist', 5, '2023-02-01', 18), -- Pro-rated from February
('Frank Garcia', 'frank.garcia@company.com', 'Marketing', 'Marketing Manager', NULL, '2022-10-10', 20),
('Grace Lee', 'grace.lee@company.com', 'Marketing', 'Marketing Specialist', 7, '2023-01-20', 19), -- Pro-rated
('Henry Clark', 'henry.clark@company.com', 'Finance', 'Finance Manager', NULL, '2022-12-15', 20),
('Ivy Rodriguez', 'ivy.rodriguez@company.com', 'Finance', 'Accountant', 9, '2023-04-05', 15); -- Pro-rated from April

-- Insert public holidays
INSERT INTO holidays (name, holiday_date, description, recurring) VALUES
('New Years Day', '2024-01-01', 'New Years Day Holiday', true),
('Martin Luther King Jr Day', '2024-01-15', 'Federal Holiday', true),
('Presidents Day', '2024-02-19', 'Federal Holiday', true),
('Memorial Day', '2024-05-27', 'Federal Holiday', true),
('Independence Day', '2024-07-04', 'Independence Day Holiday', true),
('Labor Day', '2024-09-02', 'Federal Holiday', true),
('Columbus Day', '2024-10-14', 'Federal Holiday', true),
('Veterans Day', '2024-11-11', 'Veterans Day Holiday', true),
('Thanksgiving', '2024-11-28', 'Thanksgiving Holiday', true),
('Christmas Day', '2024-12-25', 'Christmas Holiday', true),
('Good Friday', '2024-03-29', 'Good Friday Holiday', false),
('Easter Monday', '2024-04-01', 'Easter Monday Holiday', false);

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

-- Insert audit trail entries for existing leave requests
INSERT INTO leave_audit (leave_request_id, action, performed_by, performed_by_name, action_timestamp, old_status, new_status, comments, details) VALUES
(1, 'SUBMITTED', 2, 'Alice Johnson', '2024-01-15 10:00:00', NULL, 'PENDING', NULL, 'Leave request submitted for 5 working days'),
(1, 'APPROVED', 1, 'John Smith', '2024-01-16 14:30:00', 'PENDING', 'APPROVED', 'Enjoy your vacation!', 'Leave request approved by John Smith'),
(2, 'SUBMITTED', 3, 'Bob Wilson', '2024-01-22 09:15:00', NULL, 'PENDING', NULL, 'Leave request submitted for 2 working days'),
(2, 'APPROVED', 1, 'John Smith', '2024-01-22 09:45:00', 'PENDING', 'APPROVED', 'Get well soon', 'Leave request approved by John Smith'),
(3, 'SUBMITTED', 4, 'Carol Brown', '2024-02-20 11:00:00', NULL, 'PENDING', NULL, 'Leave request submitted for 2 working days'),
(4, 'SUBMITTED', 6, 'Emma Davis', '2024-02-25 16:20:00', NULL, 'PENDING', NULL, 'Leave request submitted for 6 working days'),
(5, 'SUBMITTED', 8, 'Grace Lee', '2024-01-17 13:00:00', NULL, 'PENDING', NULL, 'Leave request submitted for 2 working days'),
(5, 'REJECTED', 7, 'Frank Garcia', '2024-01-18 08:30:00', 'PENDING', 'REJECTED', 'Please reschedule for less busy period', 'Leave request rejected by Frank Garcia'),
(6, 'SUBMITTED', 10, 'Ivy Rodriguez', '2024-02-05 10:45:00', NULL, 'PENDING', NULL, 'Leave request submitted for 1 working day'),
(6, 'APPROVED', 9, 'Henry Clark', '2024-02-06 12:00:00', 'PENDING', 'APPROVED', 'Good luck with the move!', 'Leave request approved by Henry Clark'),
(7, 'SUBMITTED', 2, 'Alice Johnson', '2024-03-01 15:30:00', NULL, 'PENDING', NULL, 'Leave request submitted for 1 working day'),
(8, 'SUBMITTED', 3, 'Bob Wilson', '2024-03-10 09:00:00', NULL, 'PENDING', NULL, 'Leave request submitted for 6 working days');