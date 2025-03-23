package org.example.projectvoucher.domain.employee;

import org.example.projectvoucher.app.controller.response.EmployeeResponse;
import org.example.projectvoucher.storage.employee.EmployeeEntity;
import org.example.projectvoucher.storage.employee.EmployeeRepository;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService {

  private final EmployeeRepository employeeRepository;

  public EmployeeService(EmployeeRepository employeeRepository) {
    this.employeeRepository = employeeRepository;
  }

  // 사원 생성
  public Long create(final String name, final String position, final String department) {

    EmployeeEntity employeeEntity = employeeRepository.save(new EmployeeEntity(name, position, department));

    return employeeEntity.id();
  }

  // 사원 조회
  public EmployeeResponse get(final Long no) {
    final EmployeeEntity employeeEntity = employeeRepository.findById(no)
        .orElseThrow(() -> new IllegalArgumentException("사원이 없습니다."));

    return new EmployeeResponse(employeeEntity.id(), employeeEntity.name(), employeeEntity.position()
        , employeeEntity.department(), employeeEntity.createAt(), employeeEntity.updateAt());
  }
}
