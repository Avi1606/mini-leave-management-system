package com.avi.leavemanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class LeaveManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(LeaveManagementApplication.class, args);
        System.out.println("🚀 Mini Leave Management System is running!");
        System.out.println("📄 API Documentation: http://localhost:8080/swagger-ui.html");
    }
}