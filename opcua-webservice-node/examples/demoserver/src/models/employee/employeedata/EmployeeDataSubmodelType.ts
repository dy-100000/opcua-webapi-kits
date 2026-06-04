import { StatusCodes } from "opcua-webapi";
import {
    UaArgument,
    UaEUInformation,
    UaError,
    UaLocalizedText,
    UaVariant,
    UaVariantType,
} from "opcua-webapi-ts";
import {
    InvokeOperationRequest,
    InvokeOperationResponse,
    ReadPropertyValuesRequest,
    ReadPropertyValuesResponse,
    SubmodelType,
    UaDataTypes,
    type UaMethod,
    type UaObject,
    type UaVariable,
    UaVariableTypes,
} from "opcua-webservice-node";

import { prisma } from "../../../connectors/prismaClient";
import type { employeeinfo } from "../../../connectors/prisma/client";
import { EmployeeTwinSpace } from "../../EmployeeTwinSpace";
import { EmployeeAttendanceEventElementType } from "./EmployeeAttendanceEventElementType";

export class EmployeeDataSubmodelType extends SubmodelType {
    private readonly startTime: UaVariable;
    private readonly salary: UaVariable;
    private readonly getCumulatedSalaryMethod: UaMethod;
    private readonly employeeAttendanceEventElement: UaObject;

    constructor(employeeAttendanceEventElementType: EmployeeAttendanceEventElementType, twinSpace: EmployeeTwinSpace) {
        super("EmployeeDataSubmodelType", new UaLocalizedText("EmployeeDataModel"), twinSpace);

        this.startTime = this.addPropertyElement(
            "StartTime",
            new UaLocalizedText("StartTime"),
            new UaLocalizedText("The date this employee start to work in this company"),
            UaDataTypes.DateTime,
            false,
        );

        this.salary = this.addPropertyElement(
            "Salary",
            new UaLocalizedText("Salary"),
            new UaLocalizedText("Employee salary"),
            UaDataTypes.Float,
            false,
            false,
            undefined,
            true,
            UaVariableTypes.BaseAnalogItemType,
        );

        this.addSubElementOfProperty(
            this.salary,
            "EngineeringUnits",
            UaVariant.extensionObject(
                new UaEUInformation(
                    -1,
                    new UaLocalizedText("RMB/M"),
                    new UaLocalizedText("RMB per month"),
                ).toExtensionObject(),
            ),
        );

        this.getCumulatedSalaryMethod = this.addCumulatedSalaryOperation();
        this.employeeAttendanceEventElement = this.addEventElement(
            employeeAttendanceEventElementType,
            "Attendance",
            new UaLocalizedText("Attendance"),
            new UaLocalizedText("Log of check in and check out"),
            true,
        );
    }

    override async onReadPropertyValues(
        request: ReadPropertyValuesRequest,
    ): Promise<ReadPropertyValuesResponse> {
        const employee = await this.getEmployeeData(request.id);
        const response = new ReadPropertyValuesResponse();

        response.setValue(this.startTime.name, UaVariant.dateTime(employee.StartTime));
        response.setValue(this.salary.name, UaVariant.float(employee.Salary));

        return response;
    }

    override async onInvokeOperation(request: InvokeOperationRequest): Promise<InvokeOperationResponse> {
        const employee = await this.getEmployeeData(request.id);

        if (request.operationName !== this.getCumulatedSalaryMethod.name) {
            throw UaError.from(StatusCodes.BadMethodInvalid);
        }

        return new InvokeOperationResponse([
            UaVariant.integer(this.getCumulatedSalary(employee), UaVariantType.Int32),
        ]);
    }

    private addCumulatedSalaryOperation(): UaMethod {
        return this.addOperationElement(
            "GetCumulatedSalary",
            new UaLocalizedText("GetCumulatedSalary"),
            new UaLocalizedText("Calculate the cumulated salary the company gives to employee"),
            null,
            [
                new UaArgument(
                    "CumulatedSalary",
                    UaDataTypes.Int32.nodeId,
                    -1,
                    new UaLocalizedText("CumulatedSalary"),
                ),
            ],
            true,
        );
    }

    private async getEmployeeData(employeeIdText: string): Promise<employeeinfo> {
        const employeeId = Number.parseInt(employeeIdText, 10);

        if (Number.isNaN(employeeId)) {
            throw UaError.from(StatusCodes.BadNodeIdUnknown);
        }

        const employee = await prisma.employeeinfo.findFirst({
            where: { EmployeeId: employeeId },
        });

        if (employee === null) {
            throw UaError.from(StatusCodes.BadNodeIdUnknown);
        }

        return employee;
    }

    private getCumulatedSalary(employeeData: employeeinfo): number {
        const startDate = new Date(employeeData.StartTime);
        const now = new Date();

        let totalMonths = (now.getFullYear() - startDate.getFullYear()) * 12;
        totalMonths += now.getMonth() - startDate.getMonth();

        if (now.getDate() < startDate.getDate()) {
            totalMonths -= 1;
        }

        return employeeData.Salary * Math.max(totalMonths, 0);
    }
}