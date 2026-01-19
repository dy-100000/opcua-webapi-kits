package org.opcfoundation.uawebservicedemo.database.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.opcfoundation.uawebservicedemo.database.entity.Employee;
import org.opcfoundation.uawebservicedemo.database.entity.EmployeeCheckIn;
import org.opcfoundation.uawebservicedemo.database.entity.EmployeeData;
import org.springframework.lang.Nullable;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface EmployeeMapper {
    Employee getEmployee(Integer id);

    List<Employee> getEmployeeOfDepartment(Integer departmentId);

    EmployeeData getEmployeeData(Integer employeeId);

    List<Employee> getAllEmployee();

    List<EmployeeCheckIn> getCheckInData(
            @Param("employeeId") Integer employeeId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime,
            @Param("checkIn") @Nullable Boolean checkIn);
}
