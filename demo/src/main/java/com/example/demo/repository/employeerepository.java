package com.example.demo.repository;

import com.example.demo.model.employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface employeerepository extends JpaRepository<employee, Long> {
}
