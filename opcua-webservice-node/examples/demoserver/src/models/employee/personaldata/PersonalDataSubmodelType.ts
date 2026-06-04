import { StatusCodes } from "opcua-webapi";
import { UaError, UaLocalizedText, UaVariant } from "opcua-webapi-ts";
import { ReadPropertyValuesRequest, ReadPropertyValuesResponse, SubmodelType, UaDataTypes, type UaVariable } from "opcua-webservice-node";

import { prisma } from "../../../connectors/prismaClient";
import { EmployeeTwinSpace } from "../../EmployeeTwinSpace";
import { SexEnumType } from "./SexEnumType";

export class PersonalDataSubmodelType extends SubmodelType {
    private readonly sex: UaVariable;
    private readonly birthday: UaVariable;
    private readonly phoneNumber: UaVariable;
    private readonly address: UaVariable;

    constructor(sexEnumType: SexEnumType, twinSpace: EmployeeTwinSpace) {
        super("PersonalDataSubmodelType", new UaLocalizedText("PersonalDataModel"), twinSpace);

        this.sex = this.addPropertyElement(
            "Sex",
            new UaLocalizedText("Sex"),
            new UaLocalizedText("Sex of person"),
            sexEnumType,
            false,
        );

        this.birthday = this.addPropertyElement(
            "Birthday",
            new UaLocalizedText("Birthday"),
            new UaLocalizedText("Birthday of person"),
            UaDataTypes.DateTime,
            false,
        );

        this.phoneNumber = this.addPropertyElement(
            "PhoneNumber",
            new UaLocalizedText("PhoneNumber"),
            new UaLocalizedText("PhoneNumber of person"),
            UaDataTypes.String,
            false,
        );

        this.address = this.addPropertyElement(
            "Address",
            new UaLocalizedText("Address"),
            new UaLocalizedText("Address of person"),
            UaDataTypes.String,
            false,
        );
    }

    override async onReadPropertyValues(
        request: ReadPropertyValuesRequest,
    ): Promise<ReadPropertyValuesResponse> {
        const employeeId = Number.parseInt(request.id, 10);

        if (Number.isNaN(employeeId)) {
            throw UaError.from(StatusCodes.BadNodeIdUnknown);
        }

        const employee = await prisma.employeeinfo.findFirst({
            where: { EmployeeId: employeeId },
            select: {
                Sex: true,
                Birthday: true,
                PhoneNumber: true,
                Address: true,
            },
        });

        if (employee === null) {
            throw UaError.from(StatusCodes.BadNodeIdUnknown);
        }

        const response = new ReadPropertyValuesResponse();
        response.setValue(this.sex.name, UaVariant.integer((employee.Sex) ? 0:1));
        response.setValue(this.birthday.name, UaVariant.dateTime(employee.Birthday));
        response.setValue(this.phoneNumber.name, UaVariant.string(employee.PhoneNumber));
        response.setValue(this.address.name, UaVariant.string(employee.Address));

        return response;
    }
}