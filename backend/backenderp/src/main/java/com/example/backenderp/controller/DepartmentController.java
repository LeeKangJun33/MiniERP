package com.example.backenderp.controller;

import com.example.backenderp.entity.Department;
import com.example.backenderp.repository.DepartmentRepository;
import com.example.backenderp.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/departments")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;

    @GetMapping
    public ResponseEntity<List<Department>> getAllDepartments() {
        return ResponseEntity.ok(departmentService.findAllDepartments());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Department> getDepartmentById(@PathVariable Long id) {
        return departmentService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    //부서명으로 조회
    @GetMapping("/search")
    public ResponseEntity<Department> getDepartmentByName(@RequestParam String name){
        return departmentService.findByName(name)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    //부서등록
    @PostMapping
    public ResponseEntity<Department> createDepartment(@RequestBody Department department) {
        return ResponseEntity.ok(departmentService.saveDepartment(department));
    }

    @DeleteMapping
    //부서 삭제
    public ResponseEntity<Void> deleteDepartment(Long id) {
       departmentService.deleteDepartment(id);
       return ResponseEntity.noContent().build();
    }

}
