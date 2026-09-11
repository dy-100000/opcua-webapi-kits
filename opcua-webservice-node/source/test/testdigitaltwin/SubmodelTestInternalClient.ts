import {
    DataTypeIds,
    UaArgument,
    UaLocalizedText,
    UaNodeId,
    UaValueRank,
    UaVariant,
    UaError,
    parseUaNodeIdOrNull,
    UaObjectBrowser,
    UaObjectReader,
    parseUaNodeId
} from "opcua-webapi-ts";
import {
    UaMethod,
    DigitalTwinSpace,
    SubmodelType,
    UaInternalClient,
    UaErrorCodes
} from "../../src";
import {
    InvokeOperationRequest,
    InvokeOperationResponse
} from "../../src";

export class SubmodelTestInteralClientType extends SubmodelType {
    private readonly internalClient: UaInternalClient;
    private readonly testBrowser: UaMethod;
    private readonly testRead: UaMethod;

    constructor(space: DigitalTwinSpace) {
        super("SubmodelTestInternalClientType", new UaLocalizedText("SubmodelTestInternalClientType"), space);
        this.internalClient = new UaInternalClient();

        let outputArguments = [
            new UaArgument("Result", UaNodeId.from(DataTypeIds.String),UaValueRank.OneDimension),
        ];

        this.testBrowser = this.addOperationElement(
            "TestBrowser",
            new UaLocalizedText("TestBrowser"),
            new UaLocalizedText("TestBrowser operation"),
            null,
            outputArguments
        );

        this.testRead = this.addOperationElement(
            "TestRead",
            new UaLocalizedText("TestRead"),
            new UaLocalizedText("TestRead operation"),
            null,
            outputArguments
        );
    }

    override async onInvokeOperation(request: InvokeOperationRequest): Promise<InvokeOperationResponse> {
        if (request.operationName === this.testBrowser.name) {
            let objectId = parseUaNodeId("ns=2;b=eyJvaSI6eyJpIjoiRW50cnkiLCJ0IjoibnM9MjtzPVRlc3REaWdpdGFsVHdpbkRpcmVjdG9yeSJ9fQ==");
            let browser = new UaObjectBrowser(this.internalClient, [objectId]);
            await browser.browse();
            let results = browser.results();

            let childNames : Array<string> = [];
            for (let item of results) {
                for (let reference of item.references) {
                    childNames.push(reference.displayName.text);
                }
            }

            return new InvokeOperationResponse([UaVariant.strings(childNames)]);
        } else if (request.operationName === this.testRead.name) {
            let objectId = parseUaNodeId("ns=2;b=eyJvaSI6eyJpIjoiMCIsImlkIjoibnM9MjtzPVRlc3REaWdpdGFsVHdpbi1TdWJtb2RlbCJ9fQ==");
            let reader = new UaObjectReader(this.internalClient);
            let objects = await reader.read([objectId]);

            let childNames : Array<string> = [];
            for (let item of objects) {
                let members = item.getMembers();
                for (let member of members) {
                    childNames.push(member.displayName.text);
                }
            }

            return new InvokeOperationResponse([UaVariant.strings(childNames)]);
        } else {
            throw new UaError(UaErrorCodes.BadNotImplemented);
        }
    }
}