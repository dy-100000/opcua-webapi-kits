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
    UaDataTypes,
    UaMethod,
    UaModellingRule,
    UaObject,
    UaVariable,
    UaVariableTypes,
} from "opcua-webservice-node";
import { DigitalTwinSpace,SubmodelType } from "opcua-webservice-node";
import {
    GetDescriptorRequest,
    GetDescriptorResponse,
    GetElementsRequest,
    GetElementsResponse,
    InvokeOperationRequest,
    InvokeOperationResponse,
    ReadPropertyHistoryValuesRequest,
    ReadPropertyHistoryValuesResponse,
    ReadPropertyValuesRequest,
    ReadPropertyValuesResponse,
    WritePropertyValuesRequest,
    WritePropertyValuesResponse,
} from "opcua-webservice-node";
import { ElementCollectionTestAType } from "./ElementCollectionTestAType";
import { ElementCollectionTestBType } from "./ElementCollectionTestBType";
import { ElementListTestType } from "./ElementListTestType";
import { EventElementTestType } from "./EventElementTestType";
import { ReferenceElementTestType } from "./ReferenceElementTestType";

export class SubmodelTestType extends SubmodelType {
    private readonly boolElement: UaVariable;
    private readonly doubleElement: UaVariable;
    private readonly methodElement: UaMethod;
    private readonly elementCollectionTestA: UaObject;
    private readonly elementCollectionTestB: UaObject;
    private readonly referenceElement: UaObject;
    private readonly elementList: UaObject;
    private readonly eventElement: UaObject;

    constructor(
        elementCollectionTestAType: ElementCollectionTestAType,
        elementCollectionTestBType: ElementCollectionTestBType,
        referenceElementTestType: ReferenceElementTestType,
        elementListTestType: ElementListTestType,
        eventElementTestType: EventElementTestType,
        space: DigitalTwinSpace,
    ) {
        super("SubmodelTestType", new UaLocalizedText("SubmodelTestType"), space);
        this.description = new UaLocalizedText("SubmodelTestType");

        this.boolElement = this.addPropertyElement(
            "Bool",
            new UaLocalizedText("Bool"),
            new UaLocalizedText("Bool member"),
            UaDataTypes.Boolean,
            true,
        );

        this.doubleElement = this.addPropertyElement(
            "Double",
            new UaLocalizedText("Double"),
            new UaLocalizedText("Double member"),
            UaDataTypes.Double,
            true,
            false,            
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

        const inputArguments = [
            new UaArgument(
                "In",
                UaNodeId.from(DataTypeIds.String),
                UaValueRank.Scalar,
                UaLocalizedText.nullText,
            ),
        ];

        const outputArguments = [
            new UaArgument(
                "Out",
                UaNodeId.from(DataTypeIds.String),
                UaValueRank.Scalar,
                UaLocalizedText.nullText,
            ),
        ];

        this.methodElement = this.addOperationElement(
            "Method",
            new UaLocalizedText("Method"),
            new UaLocalizedText("Test method interface"),
            inputArguments,
            outputArguments,
            false,
        );

        this.elementCollectionTestA = this.addElementCollection(
            elementCollectionTestAType,
            "CollectionA",
            new UaLocalizedText("CollectionA"),
            new UaLocalizedText("CollectionA member"),
            true,
        );

        this.elementCollectionTestB = this.addElementCollection(
            elementCollectionTestBType,
            "CollectionB",
            new UaLocalizedText("CollectionB"),
            new UaLocalizedText("CollectionB member"),
            true,
        );

        this.referenceElement = this.addReferenceElement(
            referenceElementTestType,
            "Reference",
            new UaLocalizedText("Reference"),
            new UaLocalizedText("Reference member"),
            false,
        );

        this.elementList = this.addElementList(
            elementListTestType,
            "ElementList",
            new UaLocalizedText("ElementList"),
            new UaLocalizedText("Element list member"),
            false,
        );

        this.eventElement = this.addEventElement(
            eventElementTestType,
            "EventElement",
            new UaLocalizedText("EventElement"),
            new UaLocalizedText("EventElement member"),
            true,
        );
    }

    override async onReadPropertyValues(request: ReadPropertyValuesRequest): Promise<ReadPropertyValuesResponse> {
        const response = new ReadPropertyValuesResponse();
        response.setValue(this.boolElement.name, UaVariant.boolean(true));
        response.setValue(this.doubleElement.name, UaVariant.double(25.5));
        return response;
    }

    override async onWritePropertyValues(request: WritePropertyValuesRequest): Promise<WritePropertyValuesResponse> {
        console.log(`Write object, ${request.id}`);

        const response = new WritePropertyValuesResponse();
        for (const [key, value] of request.propertyNamesAndValues) {
            console.log(key, value.value);
            response.setWriteValueResult(key, makeUaStatusCode(StatusCodes.Good));
        }

        return response;
    }

    override async onInvokeOperation(request: InvokeOperationRequest): Promise<InvokeOperationResponse> {
        const outputArguments: Array<UaVariant> = [];

        if (this.methodElement.browseName === request.operationName) {
            console.log(`Object id: ${request.id}`);
            console.log(`Method call: ${request.operationName}`);

            let output = `Object ${request.id} receive value `;
            if (request.inputArguments.length > 0) {
                output += String(request.inputArguments[0].value);
            }

            outputArguments.push(UaVariant.string(output));
        }

        return new InvokeOperationResponse(outputArguments);
    }

    override async onGetElements(request: GetElementsRequest): Promise<GetElementsResponse> {
        const response = new GetElementsResponse();
        response.add(this.boolElement.browseName);
        response.add(this.elementCollectionTestA.browseName);
        response.add(this.elementCollectionTestB.browseName);
        response.add(this.eventElement.browseName);

        if (request.id === "1") {
            response.add(this.referenceElement.browseName);
        } else if (request.id === "2") {
            response.add(this.methodElement.browseName);
        } else {
            response.add(this.doubleElement.browseName);
            response.add(this.methodElement.browseName);
            response.add(this.referenceElement.browseName);
            response.add(this.elementList.browseName);
        }

        return response;
    }

    override async onGetDescriptor(request: GetDescriptorRequest): Promise<GetDescriptorResponse> {
        return new GetDescriptorResponse(
            new UaLocalizedText(`Submodel-${request.id}`),
            new UaLocalizedText(`Submodel with id ${request.id}`),
        );
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