package org.example.projectvoucher.app.controller.employee;

import org.example.projectvoucher.app.controller.employee.request.EmployeeCreateRequest;
import org.example.projectvoucher.app.controller.employee.response.EmployeeResponse;
import org.example.projectvoucher.domain.employee.EmployeeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmployeeController {
  private final EmployeeService employeeService;

  public EmployeeController(EmployeeService employeeService) {
    this.employeeService = employeeService;
  }

  // 사원 생성
  @PostMapping("api/v1/employee")
  public Long create(@RequestBody final EmployeeCreateRequest request) {
    return employeeService.create(request.name(), request.position(), request.department());

  }
  // 사원 조회
  @GetMapping("/api/v1/employee/{no}")
  public EmployeeResponse get(@PathVariable(value = "no") Long no) {
    return employeeService.get(no);
  }

}
