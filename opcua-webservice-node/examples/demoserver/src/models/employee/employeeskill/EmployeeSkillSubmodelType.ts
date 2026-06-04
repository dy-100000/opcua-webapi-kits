import { UaLocalizedText } from "opcua-webapi-ts";
import { SubmodelType } from "opcua-webservice-node";
import { EmployeeTwinSpace } from "../../EmployeeTwinSpace";
import type { EmployeeSkillReferenceType } from "./EmployeeSkillReferenceType";

export class EmployeeSkillSubmodelType extends SubmodelType {
    public constructor(employeeSkillReferenceType: EmployeeSkillReferenceType, twinSpace: EmployeeTwinSpace) {
        super("EmployeeSkillSubmodelType", new UaLocalizedText("EmployeeSkillModel"), twinSpace);

        this.addReferenceElement(
                employeeSkillReferenceType,
                "Skills",
                new UaLocalizedText("Skills"),
                new UaLocalizedText("Skills of employee"),
                true);
    }
}