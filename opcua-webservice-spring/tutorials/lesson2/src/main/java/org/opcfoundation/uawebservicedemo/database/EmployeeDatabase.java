package org.opcfoundation.uawebservicedemo.database;

import org.opcfoundation.uawebservicedemo.database.mapper.DepartmentMapper;
import org.opcfoundation.uawebservicedemo.database.mapper.ProjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class EmployeeDatabase implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    @Autowired
    private DepartmentMapper departmentMapper;
    @Autowired
    private ProjectMapper projectMapper;

    public static DepartmentMapper getDepartmentMapper() {
        return getInstance().departmentMapper;
    }

    public static ProjectMapper getProjectMapper() {
        return getInstance().projectMapper;
    }

    @Override
    public void setApplicationContext(ApplicationContext context) {
        applicationContext = context;
    }

    public static EmployeeDatabase getInstance() {
        return applicationContext.getBean(EmployeeDatabase.class);
    }
}
