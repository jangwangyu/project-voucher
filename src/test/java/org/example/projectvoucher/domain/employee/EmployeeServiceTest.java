package org.example.projectvoucher.domain.employee;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.example.projectvoucher.app.controller.response.EmployeeResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class EmployeeServiceTest {
  @Autowired
  private EmployeeService employeeService;

  @DisplayName("사원 생성 후 조회가 가능하다.")
  @Test
  public void test1() {
    // Given
    String name = "홍길동";
    String position = "사원";
    String department = "개발";
    // When
    Long no = employeeService.create(name, position, department);
    EmployeeResponse response = employeeService.get(no);
    // Then
    assertThat(response).isNotNull();
    assertThat(response.no()).isEqualTo(no);
    assertThat(response.name()).isEqualTo(name);
    assertThat(response.position()).isEqualTo(position);
    assertThat(response.department()).isEqualTo(department);

  }


}