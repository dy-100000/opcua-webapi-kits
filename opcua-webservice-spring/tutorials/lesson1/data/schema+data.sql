CREATE TABLE Department (
    ID INT PRIMARY KEY,
    Name VARCHAR(50) NOT NULL,
    Comment VARCHAR(200) NULL,
    ParentDepartmentId INT NULL,
    CONSTRAINT FK_Department_ParentDepartment 
        FOREIGN KEY (ParentDepartmentId) 
        REFERENCES Department(ID)
        ON DELETE RESTRICT
        ON UPDATE CASCADE
);

CREATE TABLE Project (
    ID INT PRIMARY KEY,
    Name VARCHAR(50) NOT NULL,
    MissionDescription VARCHAR(200) NULL,
    StartTime DATETIME NOT NULL,
    EndTime DATETIME NULL,
    Budget INT NOT NULL,
    DepartmentId INT NOT NULL,
    CONSTRAINT FK_Project_Department 
        FOREIGN KEY (DepartmentId) 
        REFERENCES Department(ID)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
	CONSTRAINT CK_Project_Time 
        CHECK (EndTime IS NULL OR EndTime >= StartTime)
);

CREATE TABLE Skill (
    ID INT PRIMARY KEY AUTO_INCREMENT,
    SkillName VARCHAR(64) NOT NULL,
    Description VARCHAR(200) NULL,
    Level INT NOT NULL,
    Category VARCHAR(32) NOT NULL,
    YearOfExperience INT NOT NULL
);

CREATE TABLE Employee (
    ID INT PRIMARY KEY AUTO_INCREMENT,
    Name VARCHAR(64) NOT NULL
);

CREATE TABLE EmployeeInfo (
    ID INT PRIMARY KEY AUTO_INCREMENT,
    Sex boolean NOT NULL,
    Birthday DATETIME NOT NULL,
    PhoneNumber VARCHAR(16) NOT NULL,
    Address VARCHAR(64) NOT NULL,
    EmployeeId INT NOT NULL,
    CONSTRAINT FK_EmployeeInfo_Employee 
        FOREIGN KEY (EmployeeId)
        REFERENCES Employee(ID)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

CREATE TABLE EmployeeSkill (
    EmployeeId INT NOT NULL,
    SkillId INT NOT NULL,
    CONSTRAINT FK_EmployeeSkill_Employee
        FOREIGN KEY (EmployeeId)
        REFERENCES Employee(ID)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    CONSTRAINT FK_EmployeeSkill_Skill
        FOREIGN KEY (SkillId)
        REFERENCES Skill(ID)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

CREATE TABLE EmployeeDepartment (
    EmployeeId INT NOT NULL,
    DepartmentId INT NOT NULL,
    CONSTRAINT FK_EmployeeDepartment_Employee
        FOREIGN KEY (EmployeeId)
        REFERENCES Employee(ID)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    CONSTRAINT FK_EmployeeDepartment_Department
        FOREIGN KEY (DepartmentId)
        REFERENCES Department(ID)
        ON DELETE RESTRICT
        ON UPDATE CASCADE
);

INSERT INTO Department (ID,Name, Comment, ParentDepartmentId)
VALUES 
    (1,'研发中心', 'New tech R&D, including DCS/PLC/Industry software', null),
    (2,'平台所', 'Software and platform development', 1),
    (3,'控制所', 'PLC programing tool and runtime development', 1),
    (4,'通讯所', 'Industry protocal R&D', 1);

INSERT INTO Project (ID,Name,MissionDescription,StartTime,EndTime,Budget,DepartmentId)
VALUES 
	(1,"开放PLC","New generation controller platform","2024-01-01 00:00:00", '2026-12-31 18:00:00', 15, 3),
	(2,"数字孪生","Digital twin platform based on information model","2024-05-01 00:00:00", '2027-06-30 18:00:00', 10, 2),
    (3,"WebHMI","WebBased HMI","2024-03-01 00:00:00", '2025-9-30 18:00:00', 5, 2),
    (4,"Profinet协议","Supporting Profinet in PLC","2024-06-01 00:00:00", '2025-9-30 18:00:00', 3, 4);

INSERT INTO Skill(ID,SkillName,Description,Level,Category,YearOfExperience)
VALUES
	(1,"BE-DEV-J","Junior backend developer",0,"BE_DEV",1),
	(2,"BE-DEV-M","Middle level backend developer",1,"BE_DEV",3),
    (3,"BE-DEV-S","Senior backend developer",2,"BE_DEV",5),
    (4,"UI-DEV-J","Junior UI developer",0,"UI_DEV",1),
    (5,"UI-DEV-M","Middle level UI developer",1,"UI_DEV",3),
    (6,"UI-DEV-S","Senior UI developer",2,"UI_DEV",5),
    (7,"S-TST-J","Junior software tester",0,"TST",1),
    (8,"S-TST-M","Middle software tester",1,"TST",3),
    (9,"S-TST-S","Senior software tester",2,"TST",5);

INSERT INTO employee(ID,Name)
VALUES 
	(1,"Zhang"),
	(2,"Li"),
    (3,"Wang"),
    (4,"Zhao"),
    (5,"Sun");

INSERT INTO employeeinfo(Sex,Birthday,PhoneNumber,Address,EmployeeId)
VALUES 
	(true,"1990-06-01 00:00:00","XXXXXXXXXXXX","XX,Beijing",1),
	(false,"1993-07-12 00:00:00","XXXXXXXXXXXX","XX,Shanghai",2),
    (true,"1987-04-23 00:00:00","XXXXXXXXXXXX","XX,Hangzhou",3),
    (false,"1999-1-25 00:00:00","XXXXXXXXXXXX","XX,HaErBin",4),
    (true,"1997-12-19 00:00:00","XXXXXXXXXXXX","XX,Tianjin",5);
    
INSERT INTO employeedepartment(EmployeeId,DepartmentId)
values
	(1,2),
    (2,2),
    (3,3),
    (4,3),
    (5,3);
    
INSERT INTO employeeskill(EmployeeId,SkillId)
values
	(1,1),
    (2,3),
    (2,5),
    (3,3),
    (4,2),
    (4,6),
    (4,7),
    (5,8);
