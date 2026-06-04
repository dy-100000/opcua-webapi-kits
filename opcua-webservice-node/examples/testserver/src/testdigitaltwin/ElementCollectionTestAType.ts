import { StatusCodes } from "opcua-webapi";
import {
    DataTypeIds,
    makeUaStatusCode,
    UaArgument,
    UaDataValue,
    UaEUInformation,
    UaLocalizedText,
    UaNodeId,
    UaRange,
    UaValueRank,
    UaVariant,
    UaVariantType,
} from "opcua-webapi-ts";
import {
    ReadPropertyHistoryValuesRequest,
    ReadPropertyHistoryValuesResponse,
    UaDataTypes,
    UaMethod,
    UaModellingRule,
    UaVariable,
    UaVariableTypes,
} from "opcua-webservice-node";
import { DigitalTwinSpace } from "opcua-webservice-node";
import { ElementCollectionType } from "opcua-webservice-node";
import {
    GetElementsRequest,
    GetElementsResponse,
    InvokeOperationRequest,
    InvokeOperationResponse,
    ReadPropertyValuesRequest,
    ReadPropertyValuesResponse,
    WritePropertyValuesRequest,
    WritePropertyValuesResponse,
} from "opcua-webservice-node";
import { EnumTestDataType } from "./EnumTestDataType";

export class ElementCollectionTestAType extends ElementCollectionType {
    private readonly boolElement: UaVariable;
    private readonly enumElement: UaVariable;
    private readonly doubleElement: UaVariable;
    private readonly rangeElement: UaVariable;
    private readonly methodElement: UaMethod;

    constructor(
        enumTestDataType: EnumTestDataType,
        space: DigitalTwinSpace,
    ) {
        super("ElementCollectionTestAType", new UaLocalizedText("ElementCollectionTestAType"), space);
        this.description = new UaLocalizedText("ElementCollectionTestAType");

        this.boolElement = this.addPropertyElement(
            "Bool",
            new UaLocalizedText("Bool"),
            new UaLocalizedText("Bool member"),
            UaDataTypes.Boolean,
            true,
        );

        this.enumElement = this.addPropertyElement(
            "Enum",
            new UaLocalizedText("Enum"),
            new UaLocalizedText("Enum member"),
            enumTestDataType,            
            false,
            true,            
            -1,
            false);

        this.doubleElement = this.addPropertyElement(
            "Double",
            new UaLocalizedText("Double"),
            new UaLocalizedText("Double member"),
            UaDataTypes.Double,
            true,
            true,            
            -1,
            false,
            UaVariableTypes.BaseAnalogItemType,
        );

        this.addSubElementOfProperty(
            this.doubleElement,
            "EURange",
            UaVariant.extensionObject(new UaRange(10.0, 50.0).toExtensionObject()),
        );

        this.addSubElementOfProperty(
            this.doubleElement,
            "EngineeringUnits",
            UaVariant.extensionObject(
                new UaEUInformation(
                    1,
                    new UaLocalizedText("C"),
                    new UaLocalizedText("Temperature C"),
                    null,
                ).toExtensionObject(),
            ),
        );

        this.rangeElement = this.addPropertyElement(
            "Range",
            new UaLocalizedText("Range"),
            new UaLocalizedText("Range member"),
            UaDataTypes.Range,
            true,
        );

        const inputArguments: Array<UaArgument> = [];
        inputArguments.push(
            new UaArgument(
                "In1",
                UaNodeId.from(DataTypeIds.Int32),
                UaValueRank.Scalar,
                UaLocalizedText.nullText,
            ),
        );
        inputArguments.push(
            new UaArgument(
                "In2",
                UaNodeId.from(DataTypeIds.Double),
                UaValueRank.Scalar,
                UaLocalizedText.nullText,
            ),
        );

        const outputArguments: Array<UaArgument> = [];
        outputArguments.push(
            new UaArgument(
                "Out",
                UaNodeId.from(DataTypeIds.Double),
                UaValueRank.Scalar,
                UaLocalizedText.nullText,
            ),
        );

        this.methodElement = this.addOperationElement(
            "Method",
            new UaLocalizedText("Method"),
            new UaLocalizedText("Test method interface"),
            inputArguments,
            outputArguments,
            false,
        );
    }

    override async onReadPropertyValues(request: ReadPropertyValuesRequest): Promise<ReadPropertyValuesResponse> {        
        const response = new ReadPropertyValuesResponse();
        response.setValue(this.boolElement.name, UaVariant.boolean(true));
        response.setValue(this.enumElement.name, UaVariant.integer(2, UaVariantType.Int32));
        response.setValue(this.doubleElement.name, UaVariant.double(11.5));
        response.setValue(
                this.rangeElement.name,
                UaVariant.extensionObject(new UaRange(100.0, 150.0).toExtensionObject()));

        return response;
    }

    override async onWritePropertyValues(request: WritePropertyValuesRequest): Promise<WritePropertyValuesResponse> {
        console.log(`Write object, ${request.id}`);

        const response = new WritePropertyValuesResponse();

        for (const [key, value] of request.propertyNamesAndValues) {
            if (value.type !== UaVariantType.ExtensionObject) {
                console.log(`Key: ${key} Value:`, value.value);
            } else {
                console.log(`Key: ${key} Value:`, value.toExtensionObject());
            }

            response.setWriteValueResult(key, makeUaStatusCode(StatusCodes.Good));
        }

        return response;
    }

    override async onInvokeOperation(request: InvokeOperationRequest): Promise<InvokeOperationResponse> {
        console.log(`Object id: ${request.id}`);
        console.log(`Method call: ${request.operationName}`);
        console.log("Input arguments:", request.inputArguments);

        const outputArguments = [UaVariant.double(255.5)];
        return new InvokeOperationResponse(outputArguments);
    }

    override async onGetElements(request: GetElementsRequest): Promise<GetElementsResponse> {
        const response = new GetElementsResponse();
        response.add(this.boolElement.name);
        response.add(this.rangeElement.name);

        if (request.id === "2") {
            response.add(this.doubleElement.name);
        } else if (request.id === "3") {
            response.add(this.methodElement.name);
        } else {            
            response.add(this.enumElement.name);
            response.add(this.doubleElement.name);
            response.add(this.methodElement.name);            
        }

        return response;
    }

    override async onReadPropertyHistoryValues(request: ReadPropertyHistoryValuesRequest): Promise<ReadPropertyHistoryValuesResponse> {
        const response = new ReadPropertyHistoryValuesResponse();
        let time = new Date();

        if (request.readRawDetails !== null) {
            console.log(request.readRawDetails.toStruct());
        } else if (request.readAtTimeDetails !== null) {
            console.log(request.readAtTimeDetails.toStruct());
        } else if (request.readProcessedDetails !== null) {
            console.log(request.readProcessedDetails.toStruct());
        }

        for (let index = 0; index < 20; ++index) {
            time = new Date(time.getTime() + 5000);
            response.addDataValue(
                new UaDataValue(
                    UaVariant.double(index),
                    makeUaStatusCode(StatusCodes.Good),
                    time,
                ),
            );
        }

        return response;
    }
}