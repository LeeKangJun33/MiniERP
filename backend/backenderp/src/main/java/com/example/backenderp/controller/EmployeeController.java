package com.example.backenderp.controller;

import com.example.backenderp.entity.Employee;
import com.example.backenderp.service.EmployeeService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployees(Employee employee) {
        return ResponseEntity.ok(employeeService.findByAllEmployees());
    }

    //Id로 직원 조회
    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id) {
        return employeeService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    //부서별 직원조회
    @GetMapping("/department/{departmentId}")
    public ResponseEntity<List<Employee>> getEmployeeByDepartmentId(@PathVariable Long departmentId) {
        return ResponseEntity.ok(employeeService.findByDepartmentId(departmentId));
    }

    @PostMapping
    public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee) {
        return ResponseEntity.ok(employeeService.saveEmployee(employee));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }
}
