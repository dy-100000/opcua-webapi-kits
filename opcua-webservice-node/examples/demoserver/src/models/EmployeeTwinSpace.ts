import { DigitalTwinSpace } from "opcua-webservice-node";
import { EmployeeDigitalTwinType, EmployeeRepositoryType, EmployeeSkillReferenceType, EmployeeSkillSubmodelType } from "./employee";
import { UaLocalizedText } from "opcua-webapi-ts";
import { SexEnumType,PersonalDataSubmodelType,EmployeeDataSubmodelType,EmployeeAttendanceEventElementType,EmployeeCheckInEventType } from "./employee";
import { CompanyDigitalTwinType, CompanyRepositoryType, DepartmentEmployeeReferenceType, DepartmentSubmodelType, DepartmentType } from "./company";
import { SkillClassType, SkillCategoryEnumType,SkillLevelEnumType,SkillClassificationSubmodelType } from "./company";

export class EmployeeTwinSpace extends DigitalTwinSpace {
    static SexEnumType: SexEnumType;
    static PersonalDataSubmodelType: PersonalDataSubmodelType;
    static EmployeeAttendanceEventElementType: EmployeeAttendanceEventElementType;
    static EmployeeCheckInEventType: EmployeeCheckInEventType;
    static EmployeeDataSubmodelType: EmployeeDataSubmodelType;
    static EmployeeSkillReferenceType: EmployeeSkillReferenceType;
    static EmployeeSkillSubmodelType: EmployeeSkillSubmodelType;
    static EmployeeDigitalTwinType: EmployeeDigitalTwinType;
    static EmployeeRepositoryType: EmployeeRepositoryType;

    static SkillCategoryEnumType: SkillCategoryEnumType;
    static SkillLevelEnumType: SkillLevelEnumType;
    static SkillClassType: SkillClassType;
    static SkillClassificationSubmodelType: SkillClassificationSubmodelType;
    static CompanyDigitalTwinType: CompanyDigitalTwinType;
    static CompanyRepositoryType: CompanyRepositoryType;
    static DepartmentEmployeeReferenceType: DepartmentEmployeeReferenceType;
    static DepartmentType: DepartmentType;
    static DepartmentSubmodelType: DepartmentSubmodelType;
    
    constructor() {
        super("EmployeeTwinSpace");
    }

    override async onStartUp(): Promise<void> {
        EmployeeTwinSpace.SexEnumType = new SexEnumType(this);
        EmployeeTwinSpace.PersonalDataSubmodelType = new PersonalDataSubmodelType(EmployeeTwinSpace.SexEnumType,this);
        EmployeeTwinSpace.EmployeeCheckInEventType = new EmployeeCheckInEventType(this);
        EmployeeTwinSpace.EmployeeAttendanceEventElementType = new EmployeeAttendanceEventElementType(EmployeeTwinSpace.EmployeeCheckInEventType,this);
        EmployeeTwinSpace.EmployeeDataSubmodelType = new EmployeeDataSubmodelType(EmployeeTwinSpace.EmployeeAttendanceEventElementType, this);

        EmployeeTwinSpace.DepartmentEmployeeReferenceType = new DepartmentEmployeeReferenceType(this);
        EmployeeTwinSpace.DepartmentType = new DepartmentType(EmployeeTwinSpace.DepartmentEmployeeReferenceType,this);
        EmployeeTwinSpace.DepartmentSubmodelType = new DepartmentSubmodelType(this);

        EmployeeTwinSpace.EmployeeSkillReferenceType = new EmployeeSkillReferenceType(this);
        EmployeeTwinSpace.EmployeeSkillSubmodelType = new EmployeeSkillSubmodelType(EmployeeTwinSpace.EmployeeSkillReferenceType, this);

        EmployeeTwinSpace.EmployeeDigitalTwinType = new EmployeeDigitalTwinType(
            EmployeeTwinSpace.PersonalDataSubmodelType, EmployeeTwinSpace.EmployeeDataSubmodelType, EmployeeTwinSpace.EmployeeSkillSubmodelType, this);
        EmployeeTwinSpace.EmployeeRepositoryType = new EmployeeRepositoryType(this);

        EmployeeTwinSpace.SkillCategoryEnumType = new SkillCategoryEnumType(this);
        EmployeeTwinSpace.SkillLevelEnumType = new SkillLevelEnumType(this);
        EmployeeTwinSpace.SkillClassType = new SkillClassType(EmployeeTwinSpace.SkillCategoryEnumType, EmployeeTwinSpace.SkillLevelEnumType, this);
        EmployeeTwinSpace.SkillClassificationSubmodelType = new SkillClassificationSubmodelType(this);

        EmployeeTwinSpace.CompanyDigitalTwinType = new CompanyDigitalTwinType(
            EmployeeTwinSpace.DepartmentSubmodelType, EmployeeTwinSpace.SkillClassificationSubmodelType, this);
        EmployeeTwinSpace.CompanyRepositoryType = new CompanyRepositoryType(this); 

        this.addRepository(EmployeeTwinSpace.EmployeeRepositoryType, "Employees", new UaLocalizedText("Employees"), new UaLocalizedText("Provides employee information"));
        this.addRepository(EmployeeTwinSpace.CompanyRepositoryType, "Companies", new UaLocalizedText("Companies"), new UaLocalizedText("Provides company information"));
    }
}