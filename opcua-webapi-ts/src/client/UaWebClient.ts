import { DefaultApi, ViewDescription, BrowseDescription, RequestHeader, BrowseRequestFromJSON, 
    ReadValueId, ReadRequestFromJSON, TimestampsToReturn, BrowseNextRequestFromJSON, 
    WriteValue, WriteRequestFromJSON, CallMethodRequest, CallRequestFromJSON, 
    FindServersRequestFromJSON, 
    ApplicationDescription, NodeClass, BrowseDirection, Attributes, EndpointDescription, 
    GetEndpointsRequestFromJSON,   
    StatusCodes,
    Variant,
    HistoryReadRequestFromJSON,
    HistoryReadValueId } from "opcua-webapi";
import { UaPayloadMapper, makeUaStatusCode, UaBrowseResult, UaError, UaNodeAttributes, UaVariableAttributes, UaNodeId, UaDataValue, 
    UaVariantType, UaVariant, UaStatusCode, UaArgument, UaArrayType, UaExtensionObject, 
    UaHistoryDataResult,
    UaHistoryData,
    UaQuery,
    UaReadEventDetails,
    UaEventFilter,
    UaSimpleAttributeOperand,
    UaContentFilter,
    UaObjectAttributes,
    UaMethodArguments,
    UaApplicationDescriptor,
    UaBrowseDescription,
    ReferenceTypeIds,
    UaReadValueId,
    UaWriteValue,
    UaHistoryReadValueId,
    UaHistoryReadResult} from "../common"
import { UaClientConfiguration, UaClientParameters } from "..";
import { UaReadRawModifiedDetails, UaReadAtTimeDetails, UaHistoryEvent, UaHistoryEventResult, UaCallMethodRequest, UaCallMethodResult} from "../common";

export class UaWebClient
{
    private api : DefaultApi;
    private clientConfig : UaClientConfiguration;
    private requestHandle : number;    
    protected authenticationToken : string | undefined;

    constructor(clientConfig: UaClientConfiguration)
    {
        this.api = new DefaultApi(clientConfig.apiConfig);
        this.clientConfig = clientConfig;
        this.requestHandle = 1;
    }

    getUrl() : string
    {
        return this.clientConfig.apiConfig.basePath;
    }

    getNativeApi() : DefaultApi
    {
        return this.api;
    }   

    async find(applicationUris?: Array<string>) : Promise<Array<UaApplicationDescriptor>>
    {
        let results = await this.findServer(
            this.clientConfig.apiConfig.basePath, 
            (applicationUris) ? applicationUris : []);

        let ret : Array<UaApplicationDescriptor> = [];

        for (let item of results)
        {
            ret.push({
                applicationUri: (item.ApplicationUri) ? item.ApplicationUri : "",
                applicationName: UaPayloadMapper.localizedTextFromWebApi(item.ApplicationName),
                productUri: (item.ProductUri) ? item.ProductUri : "",
                urls: (item.DiscoveryUrls) ? item.DiscoveryUrls : []
            });
        }

        return ret;
    }

    async browseChild(
        nodeId: UaNodeId, 
        nodeClass?: number,
        maxReferences? : number) : Promise<UaBrowseResult>
    {
        let nodesToBrowse = new UaBrowseDescription(
            nodeId,
            BrowseDirection.Forward,
            UaNodeId.from(ReferenceTypeIds.HierarchicalReferences),
            true,
            (nodeClass) ? nodeClass : Number(NodeClass.Object | NodeClass.Variable | NodeClass.Method),
            63);  

        let results = await this.browse([ nodesToBrowse ], maxReferences);
        let statusCode = results[0].statusCode;
        if (statusCode.isNotGood()) throw new UaError(statusCode);
        return results[0];
    }

    async browseReference(
        nodeId: UaNodeId,                 
        nodeClass?: number,        
        direction? : BrowseDirection,
        referenceTypeId? : UaNodeId,
        maxReferences? : number) : Promise<UaBrowseResult>
    {
        let nodesToBrowse = new UaBrowseDescription(
            nodeId,
            (direction) ? direction : BrowseDirection.Forward,
            (referenceTypeId) ? referenceTypeId : UaNodeId.from(ReferenceTypeIds.NonHierarchicalReferences),            
            true,
            (nodeClass) ? nodeClass : Number(NodeClass.Object | NodeClass.Variable | NodeClass.Method),
            63);  

        let results = await this.browse([ nodesToBrowse ], maxReferences);
        let statusCode = results[0].statusCode;
        if (statusCode.isNotGood()) throw new UaError(statusCode);
        return results[0];
    }

    async browseNextByCP(continuationPoint: string) : Promise<UaBrowseResult>
    {
        let results = await this.browseNext([continuationPoint], false);
        let statusCode = results[0].statusCode;
        if (statusCode.isNotGood()) throw new UaError(statusCode);
        return results[0];
    }

    async readValues(
        nodeIds : Array<UaNodeId>, 
        timestampsToReturn?: number) : Promise<Array<UaDataValue>>
    {
        if (nodeIds.length == 0) throw new UaError(makeUaStatusCode(StatusCodes.BadNothingToDo));

        let nodesToRead: Array<UaReadValueId> = [];

        for (let item of nodeIds)
        {
            let nodeToRead = new UaReadValueId(item, Attributes.Value);
            nodesToRead.push(nodeToRead);
        }

        let results = await this.read(nodesToRead, (timestampsToReturn) ? timestampsToReturn : TimestampsToReturn.Neither);
        return results;
    }

    async writeValue(nodeId: UaNodeId, value: UaVariant) : Promise<void>
    {
        let writeValue = new UaWriteValue(nodeId, value);
        let results = await this.write([writeValue]);
        if (results[0].isNotGood()) throw new UaError(results[0]);
    }

    async readNodeAttributes(nodeId : UaNodeId, returnDescription? : boolean) : Promise<UaNodeAttributes>
    {        
        let nodesToRead: Array<UaReadValueId> = [
            new UaReadValueId(nodeId, Attributes.NodeClass),
            new UaReadValueId(nodeId, Attributes.BrowseName),
            new UaReadValueId(nodeId, Attributes.DisplayName),
            new UaReadValueId(nodeId, Attributes.UserWriteMask)
        ];

        if (returnDescription)
        {
            nodesToRead.push(new UaReadValueId(nodeId, Attributes.Description));
        }

        let results = await this.read(nodesToRead);              

        if (results[0].statusCode.isNotGood()) throw new UaError(results[0].statusCode);  
        if (results[1].statusCode.isNotGood()) throw new UaError(results[1].statusCode);
        if (results[2].statusCode.isNotGood()) throw new UaError(results[2].statusCode);

        let nodeClassValue = results[0].value;
        let browseNameValue = results[1].value;
        let displayNameValue = results[2].value;

        if (UaVariantType.Int32 != nodeClassValue.type ||
            UaVariantType.QualifiedName != browseNameValue.type ||
            UaVariantType.LocalizedText != displayNameValue.type) throw new UaError(makeUaStatusCode(StatusCodes.BadNodeAttributesInvalid));
        
        let writeMaskValue = (results[3].statusCode.isGood() &&
                results[3].value.type == UaVariantType.UInt32) ? results[3].value : undefined;

        let descriptionValue = 
                (returnDescription && 
                results[4].statusCode.isGood() &&
                results[4].value.type == UaVariantType.LocalizedText) ? results[4].value : undefined;

        let ret : UaNodeAttributes = {
                nodeClass : nodeClassValue.value,
                browseName: browseNameValue.value,
                displayName: displayNameValue.value,
                writeMask: (writeMaskValue) ? writeMaskValue.value : 0,
                description: (descriptionValue) ? descriptionValue.value : undefined
            };

        return ret;
    }
    
    async readVariableAttributes(nodeIds : Array<UaNodeId>) : Promise<Array<UaVariableAttributes>>
    {
        if (nodeIds.length == 0) return [];

        let nodesToRead: Array<UaReadValueId> = [];        
        
        for (let item of nodeIds)
        {
            nodesToRead.push(new UaReadValueId(item, Attributes.NodeClass));
            nodesToRead.push(new UaReadValueId(item, Attributes.DataType));
            nodesToRead.push(new UaReadValueId(item, Attributes.ValueRank));
            nodesToRead.push(new UaReadValueId(item, Attributes.AccessLevel));
            nodesToRead.push(new UaReadValueId(item, Attributes.UserAccessLevel));
            nodesToRead.push(new UaReadValueId(item, Attributes.Historizing));
        }

        let results = await this.read(nodesToRead);

        let dataValues : Array<UaDataValue> = [];
        let ret : Array<UaVariableAttributes> = [];

        for (let item of results)
        {
            dataValues.push(item);

            if (dataValues.length == 6)
            {
                let dataType = UaNodeId.nullNodeId;
                let valueRank = -1;
                let accessLevel = 0;
                let userAccessLevel = 0;
                let historizing = false;

                if (dataValues[0].statusCode.isGood() && dataValues[0].value.value == NodeClass.Variable)
                {
                    if (dataValues[1].statusCode.isGood() && 
                            dataValues[1].value.type == UaVariantType.NodeId) dataType = dataValues[1].value.value;

                    if (dataValues[2].statusCode.isGood() &&
                            dataValues[2].value.type == UaVariantType.Int32) valueRank = dataValues[2].value.value;

                    if (dataValues[3].statusCode.isGood() &&
                            dataValues[3].value.type == UaVariantType.Byte) accessLevel = dataValues[3].value.value;
                   
                    if (dataValues[4].statusCode.isGood() &&
                            dataValues[4].value.type == UaVariantType.Byte) userAccessLevel = dataValues[4].value.value;

                    if (dataValues[5].statusCode.isGood() &&
                            dataValues[5].value.type == UaVariantType.Boolean) historizing = dataValues[5].value.value;
                }

                ret.push({
                    dataType : dataType,  
                    valueRank : valueRank, 
                    accessLevel : accessLevel, 
                    userAccessLevel : userAccessLevel,
                    historizing : historizing
                });
                
                dataValues = [];
            }
        }

        return ret;
    }

    async readMethodArguments(nodeId: UaNodeId) : Promise<UaMethodArguments>
    {
        let ret : UaMethodArguments = { inputArguments:[], outputArguments: [] };
        let browseResult = await this.browseChild(nodeId, NodeClass.Variable);

        let inputArgumentsId : UaNodeId = null;
        let outputArgumentsId : UaNodeId = null;

        for (let item of browseResult.results)
        {
            if ("InputArguments" == item.browseName) inputArgumentsId = item.nodeId.getNodeId();
            if ("OutputArguments" == item.browseName) outputArgumentsId = item.nodeId.getNodeId();
        }      

        let nodesToRead : Array<UaNodeId> = [];
        if (inputArgumentsId) nodesToRead.push(inputArgumentsId);
        if (outputArgumentsId) nodesToRead.push(outputArgumentsId);

        if (0 == nodesToRead.length) return ret;

        let readResults = await this.readValues(nodesToRead);

        let argsArr : Array<Array<UaArgument>> = [];

        for (let item of readResults)
        {
            if (item.statusCode.isNotGood() ||
                UaVariantType.ExtensionObject != item.value.type ||
                UaArrayType.Array != item.value.arrayType) throw new UaError(makeUaStatusCode(StatusCodes.BadInvalidArgument));

            let extensionObjects = item.value.value as Array<UaExtensionObject>;
            let args : Array<UaArgument> = [];
            for (let itemL2 of extensionObjects)
            {
                let arg = UaArgument.fromExtensionObject(itemL2);
                if (!arg) throw new UaError(makeUaStatusCode(StatusCodes.BadInvalidArgument));
                args.push(arg);            
            }

            argsArr.push(args);
        }

        if (inputArgumentsId && outputArgumentsId)
        {
            ret.inputArguments = argsArr[0];
            ret.outputArguments = argsArr[1];
        } else if (inputArgumentsId) {
            ret.inputArguments = argsArr[0];
        } else if (outputArgumentsId) {
            ret.outputArguments = argsArr[0];
        }
        
        return ret;
    }

    async readObjectAttributes(nodeId: UaNodeId) : Promise<UaObjectAttributes>
    {
        let nodesToRead: Array<UaReadValueId> = [
            new UaReadValueId(nodeId, Attributes.EventNotifier)
        ];

        let results = await this.read(nodesToRead);   

        if (results[0].statusCode.isNotGood()) throw new UaError(results[0].statusCode);  
        
        let eventNotifierValue = results[0].value;
        if (UaVariantType.Byte != eventNotifierValue.type) throw new UaError(makeUaStatusCode(StatusCodes.BadNodeAttributesInvalid));
                
        let ret : UaObjectAttributes = {
                eventNotifier : eventNotifierValue.value };

        return ret;
    }

    async methodCall(
        objectId: UaNodeId, 
        methodId: UaNodeId, 
        inputArguments: Array<UaVariant>) : Promise<Array<UaVariant>>
    {
        let inputs : Array<Variant> = [];

        for (let item of inputArguments)
        {
            inputs.push(UaPayloadMapper.variantToWebApi(item));
        }

        let methodsToCall: Array<UaCallMethodRequest> = [
            new UaCallMethodRequest(objectId, methodId, inputArguments)
        ];

        let results = await this.call(methodsToCall);
        if (results[0].statusCode.isNotGood()) throw new UaError(results[0].statusCode);

        let outputs = results[0].outputArguments;
        return outputs;
    }

    async getGeneratedEventType(typeId: UaNodeId): Promise<Array<UaNodeId>>
    {
        let nodesToBrowse = new UaBrowseDescription(
            typeId,
            BrowseDirection.Forward,
            UaNodeId.from(ReferenceTypeIds.GeneratesEvent),
            false,
            NodeClass.ObjectType,
            0);

        let results = await this.browse([ nodesToBrowse ]);        

        let statusCode = results[0].statusCode;
        if (statusCode.isNotGood()) throw new UaError(statusCode);

        let browseResult = results[0];

        let ret : Array<UaNodeId> = [];
        for (let item of browseResult.results)
        {
            ret.push(item.nodeId.getNodeId());
        }       

        return ret;
    }

    async historyReadRawData(
        nodeId: UaNodeId, 
        startTime: Date, 
        endTime: Date, 
        numValuesPerNode?: number | null,
        continuationPoint?: string | null,                
        returnBounds?: boolean | null,         
        isReadModified?: boolean | null,
        timestampsToReturn?: number | null,
        releaseContinuationPoints?: boolean | null) : Promise<UaHistoryDataResult>
    {
        let nodeToRead = new UaHistoryReadValueId(nodeId,continuationPoint);

        let details = new UaReadRawModifiedDetails(startTime,endTime, numValuesPerNode,returnBounds,isReadModified);
        let historyReadDetails = details.toExtensionObject();

        let nodesToRead: Array<UaHistoryReadValueId> = [nodeToRead];
        let results = await this.historyRead(nodesToRead, historyReadDetails, timestampsToReturn,releaseContinuationPoints);

        if (results[0].statusCode.isNotGood()) throw new UaError(results[0].statusCode);

        let extensionObject = results[0].historyData;
        let historyData = UaHistoryData.fromExtensionObject(extensionObject);
        if (null == historyData) throw new UaError(makeUaStatusCode(StatusCodes.BadDecodingError));

        let ret = new UaHistoryDataResult(historyData.dataValues, results[0].continuationPoint);        
        return ret;
    }

    async historyReadAtTime(
        nodeId: UaNodeId, 
        requiredTimes: Array<Date>, 
        useSimpleBounds?: boolean | null, 
        continuationPoint?: string | null,
        timestampsToReturn?: number | null,
        releaseContinuationPoints?: boolean | null) : Promise<UaHistoryDataResult>
    {
        if (requiredTimes.length == 0) throw new UaError(makeUaStatusCode(StatusCodes.BadInvalidArgument));

        let nodeToRead = new UaHistoryReadValueId(nodeId,continuationPoint);

        let details = new UaReadAtTimeDetails(requiredTimes,useSimpleBounds);
        let historyReadDetails = details.toExtensionObject();

        let nodesToRead: Array<UaHistoryReadValueId> = [nodeToRead];
        let results = await this.historyRead(nodesToRead, historyReadDetails, timestampsToReturn,releaseContinuationPoints);

        if (results[0].statusCode.isNotGood()) throw new UaError(results[0].statusCode);

        let extensionObject = results[0].historyData;
        let historyData = UaHistoryData.fromExtensionObject(extensionObject);
        if (null == historyData) throw new UaError(makeUaStatusCode(StatusCodes.BadDecodingError));

        let ret = new UaHistoryDataResult(historyData.dataValues, results[0].continuationPoint);        
        return ret;
    }

    async historyReadEvent(
        nodeId: UaNodeId, 
        startTime: Date, 
        endTime: Date, 
        select: Array<string>,
        where?: UaQuery | null,
        numValuesPerNode?: number | null,
        continuationPoint?: string | null,        
        releaseContinuationPoints?: boolean | null) : Promise<UaHistoryEventResult>
    {
        if (select.length == 0) throw new UaError(makeUaStatusCode(StatusCodes.BadInvalidArgument));

        let nodeToRead = new UaHistoryReadValueId(nodeId,continuationPoint);

        let selectClauses : Array<UaSimpleAttributeOperand> = [];   
        let whereClauses : UaContentFilter = (where) ? where.toContentFilter() : null;

        for (let item of select)
        {
            let selectedField = new UaSimpleAttributeOperand([item]);
            selectClauses.push(selectedField);
        }       

        let details = new UaReadEventDetails(
            startTime,
            endTime, 
            new UaEventFilter(selectClauses, whereClauses), 
            numValuesPerNode);
        
        let historyReadDetails = details.toExtensionObject();

        let nodesToRead: Array<UaHistoryReadValueId> = [nodeToRead];
        let results = await this.historyRead(nodesToRead, historyReadDetails, TimestampsToReturn.Both,releaseContinuationPoints);

        if (results[0].statusCode.isNotGood()) throw new UaError(results[0].statusCode);

        let extensionObject = results[0].historyData;
        let historyData = UaHistoryEvent.fromExtensionObject(extensionObject);
        if (null == historyData) throw new UaError(makeUaStatusCode(StatusCodes.BadDecodingError));

        let ret = new UaHistoryEventResult(historyData.events, results[0].continuationPoint);        
        return ret;
    }
 
    // Native APIs
    async browse(
        nodesToBrowse: Array<UaBrowseDescription>, 
        maxReferencesPerNode? : number,
        view?: ViewDescription,
        additionalParameters?: UaClientParameters) : Promise<Array<UaBrowseResult>>
    {
        if (nodesToBrowse.length == 0) throw new UaError(makeUaStatusCode(StatusCodes.BadNothingToDo));

        let NodesToBrowse : Array<BrowseDescription> = [];

        for (let item of nodesToBrowse) {
            NodesToBrowse.push(item.toStruct());
        }

        let request = BrowseRequestFromJSON({
            RequestHeader: this.requestHeader(this.clientConfig, additionalParameters),
            NodesToBrowse: NodesToBrowse,
            RequestedMaxReferencesPerNode: (maxReferencesPerNode && maxReferencesPerNode > 0) ? maxReferencesPerNode : undefined,
            View: view }); 
            
        let response = await this.api.browse({browseRequest: request});

        if (response?.ResponseHeader?.ServiceResult?.Code) 
            throw new UaError(makeUaStatusCode(response.ResponseHeader.ServiceResult.Code));

        if (!response.Results || nodesToBrowse.length != response.Results.length) 
            throw new UaError(makeUaStatusCode(StatusCodes.BadDataLost));
        
        let results: Array<UaBrowseResult> = [];
        if (response.Results)
        {
            for (let item of response.Results) 
            {
                let result = UaBrowseResult.fromStruct(item);
                if (null == result) throw new UaError(makeUaStatusCode(StatusCodes.BadDecodingError));
                results.push(result);
            }
        }
        
        return results;
    }

    async browseNext(
        continuationPoints: Array<string>, 
        releaseContinuationPoints: boolean,
        additionalParameters?: UaClientParameters) : Promise<Array<UaBrowseResult>>
    {
        if (continuationPoints.length == 0) throw new UaError(makeUaStatusCode(StatusCodes.BadNothingToDo));

        let request = BrowseNextRequestFromJSON({
            RequestHeader: this.requestHeader(this.clientConfig, additionalParameters),
            ContinuationPoints: continuationPoints,
            ReleaseContinuationPoints: releaseContinuationPoints }); 

        let response = await this.api.browseNext({browseNextRequest: request});

        if (response?.ResponseHeader?.ServiceResult?.Code) 
            throw new UaError(makeUaStatusCode(response.ResponseHeader.ServiceResult.Code));

        if (!response.Results || continuationPoints.length != response.Results.length) 
            throw new UaError(makeUaStatusCode(StatusCodes.BadDataLost));
        
        let results: Array<UaBrowseResult> = [];
        for (let item of response.Results) 
        {
            let result = UaBrowseResult.fromStruct(item);
            if (null == result) throw new UaError(makeUaStatusCode(StatusCodes.BadDecodingError));
            results.push(result);
        }
        
        return results;
    }

    async read(
        nodesToRead: Array<UaReadValueId>,
        timestampsToReturn?: number,
        maxAge?: number,        
        additionalParameters?: UaClientParameters) : Promise<Array<UaDataValue>>
    {
        if (nodesToRead.length == 0) throw new UaError(makeUaStatusCode(StatusCodes.BadNothingToDo));

        let NodesToRead : Array<ReadValueId> = [];

        for (let item of nodesToRead) {
            NodesToRead.push(item.toStruct());
        }

        let request = ReadRequestFromJSON({
            RequestHeader: this.requestHeader(this.clientConfig, additionalParameters),
            NodesToRead: NodesToRead,
            MaxAge: (maxAge && maxAge > 0) ? maxAge : undefined,
            TimestampsToReturn: 
               (timestampsToReturn && 
                timestampsToReturn >= TimestampsToReturn.Source && 
                timestampsToReturn < TimestampsToReturn.Invalid) ? timestampsToReturn : TimestampsToReturn.Neither }); 

        let response = await this.api.read({readRequest: request});

        if (response?.ResponseHeader?.ServiceResult?.Code) 
            throw new UaError(makeUaStatusCode(response.ResponseHeader.ServiceResult.Code));

        if (!response.Results || nodesToRead.length != response.Results.length) 
            throw new UaError(makeUaStatusCode(StatusCodes.BadDataLost));
        
        let results: Array<UaDataValue> = [];
        for (let item of response.Results)
        {
            let result = UaPayloadMapper.dataValueFromWebApi(item);
            results.push(result);
        }

        return results;
    }

    async write(
        nodesToWrite: Array<UaWriteValue>,
        additionalParameters?: UaClientParameters) : Promise<Array<UaStatusCode>>
    {
        if (nodesToWrite.length == 0) throw new UaError(makeUaStatusCode(StatusCodes.BadNothingToDo));

        let NodesToWrite : Array<WriteValue> = [];

        for (let item of nodesToWrite) {
            NodesToWrite.push(item.toStruct());
        }

        let request = WriteRequestFromJSON({
            RequestHeader: this.requestHeader(this.clientConfig, additionalParameters),
            NodesToWrite: NodesToWrite }); 

        let response = await this.api.write({writeRequest: request});

        if (response?.ResponseHeader?.ServiceResult?.Code) 
            throw new UaError(makeUaStatusCode(response.ResponseHeader.ServiceResult.Code));

        if (!response.Results || nodesToWrite.length != response.Results.length) 
            throw new UaError(makeUaStatusCode(StatusCodes.BadDataLost));
        
        let results: Array<UaStatusCode> = [];
        for (let item of response.Results)
        {
            let result = UaPayloadMapper.statusCodeFromWebApi(item);
            results.push(result);
        }

        return results;
    }

    async call(
        methodsToCall: Array<UaCallMethodRequest>,
        additionalParameters?: UaClientParameters) : Promise<Array<UaCallMethodResult>>
    {
        if (methodsToCall.length == 0) throw new UaError(makeUaStatusCode(StatusCodes.BadNothingToDo));

        let MethodsToCall : Array<CallMethodRequest> = [];

        for (let item of methodsToCall) {
            MethodsToCall.push(item.toStruct());
        }

        let request = CallRequestFromJSON({
            RequestHeader: this.requestHeader(this.clientConfig, additionalParameters),
            MethodsToCall: MethodsToCall }); 

        let response = await this.api.call({callRequest: request});

        if (response?.ResponseHeader?.ServiceResult?.Code) 
            throw new UaError(makeUaStatusCode(response.ResponseHeader.ServiceResult.Code));

        if (!response.Results || methodsToCall.length != response.Results.length) 
            throw new UaError(makeUaStatusCode(StatusCodes.BadDataLost));
        
        let results: Array<UaCallMethodResult> = [];
        for (let item of response.Results)
        {
            let result = UaCallMethodResult.fromStruct(item);
            if (null == result) throw new UaError(makeUaStatusCode(StatusCodes.BadDecodingError));
            results.push(result);
        }

        return results;
    }
    
    async historyRead(
        nodesToRead: Array<UaHistoryReadValueId>,
        historyReadDetails: UaExtensionObject,
        timestampsToReturn?: number,
        releaseContinuationPoints?: boolean,
        additionalParameters?: UaClientParameters) : Promise<Array<UaHistoryReadResult>>
    {
        if (nodesToRead.length == 0) throw new UaError(makeUaStatusCode(StatusCodes.BadNothingToDo));

        let NodesToRead : Array<HistoryReadValueId> = [];
        for (let item of nodesToRead) {
            NodesToRead.push(item.toStruct());
        }

        let request = HistoryReadRequestFromJSON({
            RequestHeader: this.requestHeader(this.clientConfig, additionalParameters),
            HistoryReadDetails: UaPayloadMapper.extensionObjectToWebApi(historyReadDetails),
            TimestampsToReturn: 
               (timestampsToReturn && 
                timestampsToReturn >= TimestampsToReturn.Source && 
                timestampsToReturn < TimestampsToReturn.Invalid) ? timestampsToReturn : undefined,
            ReleaseContinuationPoints: releaseContinuationPoints,
            NodesToRead: NodesToRead
        });

        let response = await this.api.historyRead({historyReadRequest: request});

        if (response?.ResponseHeader?.ServiceResult?.Code) 
            throw new UaError(makeUaStatusCode(response.ResponseHeader.ServiceResult.Code));

        if (!response.Results || nodesToRead.length != response.Results.length) 
            throw new UaError(makeUaStatusCode(StatusCodes.BadDataLost));
        
        let results: Array<UaHistoryReadResult> = [];
        for (let item of response.Results)
        {
            let result = UaHistoryReadResult.fromStruct(item);
            if (null == result) throw new UaError(makeUaStatusCode(StatusCodes.BadDecodingError));
            results.push(result);
        }

        return results;
    }

    async findServer(         
        endpointUrl : string,
        serverUris: Array<string>,
        localeIds?: Array<string>,
        additionalParameters?: UaClientParameters) : Promise<Array<ApplicationDescription>>
    {
        let request = FindServersRequestFromJSON({
            RequestHeader: this.requestHeader(this.clientConfig, additionalParameters),
            ServerUris: serverUris,
            EndpointUrl: endpointUrl,
            LocaleIds: localeIds }); 

        let response = await this.api.findServers({findServersRequest: request});
        
        if (response?.ResponseHeader?.ServiceResult?.Code) 
            throw new UaError(makeUaStatusCode(response.ResponseHeader.ServiceResult.Code));

        if (!response.Servers) 
            throw new UaError(makeUaStatusCode(StatusCodes.BadDataLost));
        
        return response.Servers;
    }

    async getEndpoints(
        endpointUrl?: string,
        localeIds?: Array<string>,
        profileUris?: Array<string>,
        additionalParameters?: UaClientParameters) : Promise<Array<EndpointDescription>>
    {
        let request = GetEndpointsRequestFromJSON({
            RequestHeader: this.requestHeader(this.clientConfig, additionalParameters),
            EndpointUrl: endpointUrl,
            LocaleIds: localeIds,
            ProfileUris: profileUris }); 

        let response = await this.api.getEndpoints({ getEndpointsRequest: request });
        if (response?.ResponseHeader?.ServiceResult?.Code) 
            throw new UaError(makeUaStatusCode(response.ResponseHeader.ServiceResult.Code));
        if (!response.Endpoints) 
            throw new UaError(makeUaStatusCode(StatusCodes.BadDataLost));

        return response.Endpoints;
    }

    private requestHeader(
        clientConfig : UaClientConfiguration,
        additionalParameters?: UaClientParameters) : RequestHeader
    {
        let timeout = clientConfig.defaultTimeout;
        if (additionalParameters && additionalParameters.timeout) timeout = additionalParameters.timeout;

        let ret : RequestHeader = {
            RequestHandle: this.requestHandle,
            TimeoutHint: timeout,
            Timestamp: new Date(),
            AuthenticationToken: this.authenticationToken,
            ReturnDiagnostics: (additionalParameters) ? additionalParameters.returnDiagnostics : undefined,
            AuditEntryId: (additionalParameters) ? additionalParameters.auditEntryId : undefined,
        };

        this.requestHandle++;
        if (0xFFFFFFFF == this.requestHandle) this.requestHandle = 1;
        return ret;
    }
}

