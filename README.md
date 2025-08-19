# Mini Leave Management System

A comprehensive Spring Boot application for managing employee leave requests with a RESTful API backend.

## 🚀 Features

- **Employee Management**: CRUD operations for employee records
- **Leave Request Management**: Submit, approve, and reject leave requests
- **Manager Authorization**: Only managers can approve/reject requests for their team members
- **Business Logic**: Prevents overlapping leave requests and validates dates
- **Working Days Calculation**: Automatically excludes weekends
- **API Documentation**: Swagger/OpenAPI documentation
- **Database Console**: H2 console for database inspection

## 🛠️ Technology Stack

- **Java**: 17
- **Spring Boot**: 3.2.1
- **Spring Data JPA**: Database operations
- **Spring Validation**: Input validation
- **H2 Database**: In-memory database for development
- **Swagger/OpenAPI**: API documentation
- **Maven**: Build tool

## 📁 Project Structure

```
src/main/java/com/avi/leavemgmt/
├── MiniLeaveManagementSystemApplication.java  # Main application class
├── model/
│   ├── Employee.java                          # Employee entity
│   ├── LeaveRequest.java                      # Leave request entity
│   └── LeaveType.java                         # Leave type enum
├── repository/
│   ├── EmployeeRepository.java                # Employee data access
│   └── LeaveRequestRepository.java            # Leave request data access
├── service/
│   ├── EmployeeService.java                   # Employee business logic
│   └── LeaveRequestService.java               # Leave request business logic
├── controller/
│   ├── EmployeeController.java                # Employee REST endpoints
│   └── LeaveRequestController.java            # Leave request REST endpoints
├── dto/
│   ├── EmployeeDTO.java                       # Employee data transfer object
│   └── LeaveRequestDTO.java                   # Leave request data transfer object
└── config/
    └── SwaggerConfig.java                     # API documentation configuration

src/main/resources/
├── application.yml                            # Application configuration
└── data.sql                                   # Sample data for testing
```

## 🏃‍♂️ Quick Start

### Prerequisites

- Java 17 or higher
- Maven 3.6 or higher

### Running the Application

1. **Clone the repository**
   ```bash
   git clone https://github.com/Avi1606/mini-leave-management-system.git
   cd mini-leave-management-system
   ```

2. **Build the application**
   ```bash
   mvn clean compile
   ```

3. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

4. **Access the application**
   - API Base URL: `http://localhost:8080/api`
   - Swagger UI: `http://localhost:8080/swagger-ui.html`
   - H2 Console: `http://localhost:8080/h2-console`
     - JDBC URL: `jdbc:h2:mem:testdb`
     - Username: `sa`
     - Password: `password`

## 📚 API Endpoints

### Employee Management

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/employees` | Get all employees |
| GET | `/api/employees/{id}` | Get employee by ID |
| POST | `/api/employees` | Create new employee |
| PUT | `/api/employees/{id}` | Update employee |
| DELETE | `/api/employees/{id}` | Delete employee |
| GET | `/api/employees/department/{dept}` | Get employees by department |
| GET | `/api/employees/team/{managerId}` | Get team members |

### Leave Request Management

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/leave-requests` | Get all leave requests |
| GET | `/api/leave-requests/{id}` | Get leave request by ID |
| GET | `/api/leave-requests/employee/{employeeId}` | Get requests by employee |
| POST | `/api/leave-requests` | Submit new leave request |
| PUT | `/api/leave-requests/{id}/approve` | Approve leave request |
| PUT | `/api/leave-requests/{id}/reject` | Reject leave request |
| GET | `/api/leave-requests/manager/{managerId}` | Get requests for manager's team |

## 📊 Sample Data

The application includes sample data with:
- 10 employees across different departments
- Various leave requests with different statuses
- Proper manager-employee relationships

### Sample Employees
- John Smith (Engineering Manager)
- Alice Johnson (Senior Developer, reports to John)
- Bob Wilson (Developer, reports to John)
- And more...

## 🔧 Configuration

### Application Properties
The application uses `application.yml` for configuration:
- Server port: 8080
- Database: H2 in-memory
- JPA: Auto DDL creation
- Logging: Debug level for application packages

### Leave Types
- **SICK**: Sick Leave
- **CASUAL**: Casual Leave
- **ANNUAL**: Annual Leave

### Leave Status
- **PENDING**: Awaiting approval
- **APPROVED**: Approved by manager
- **REJECTED**: Rejected by manager

## 💡 Business Rules

1. **Date Validation**: Start date cannot be after end date or in the past
2. **No Overlapping**: Employees cannot have overlapping approved leaves
3. **Manager Authorization**: Only direct managers can approve/reject requests
4. **Weekend Exclusion**: Working days calculation excludes weekends
5. **Email Uniqueness**: Employee emails must be unique

## 🧪 Testing

### Using Swagger UI
1. Navigate to `http://localhost:8080/swagger-ui.html`
2. Try the various endpoints with sample data
3. Create new employees and leave requests

### Using cURL

**Create an employee:**
```bash
curl -X POST http://localhost:8080/api/employees \
  -H "Content-Type: application/json" \
  -d '{"name":"Jane Doe","email":"jane.doe@company.com","department":"IT","role":"Developer","managerId":1}'
```

**Submit a leave request:**
```bash
curl -X POST http://localhost:8080/api/leave-requests \
  -H "Content-Type: application/json" \
  -d '{"employeeId":2,"leaveType":"ANNUAL","startDate":"2024-06-10","endDate":"2024-06-14","reason":"Summer vacation"}'
```

**Approve a leave request:**
```bash
curl -X PUT http://localhost:8080/api/leave-requests/1/approve \
  -H "Content-Type: application/json" \
  -d '{"managerId":1,"comments":"Approved for summer vacation"}'
```

## 🐛 Error Handling

The application provides proper HTTP status codes:
- `200`: Success
- `201`: Created
- `204`: No Content (for deletions)
- `400`: Bad Request (validation errors)
- `403`: Forbidden (authorization errors)
- `404`: Not Found
- `409`: Conflict (duplicate email)

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👨‍💻 Author

**Avi**
- GitHub: [@Avi1606](https://github.com/Avi1606)
- Email: avi@example.com

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Open a Pull Request