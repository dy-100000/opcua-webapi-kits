import { DefaultApi, ViewDescription, BrowseDescription, RequestHeader, BrowseRequestFromJSON, 
    BrowseResult, ReadValueId, ReadRequestFromJSON, TimestampsToReturn, BrowseNextRequestFromJSON, 
    WriteValue, StatusCode, WriteRequestFromJSON, CallMethodRequest, CallRequestFromJSON, 
    CallMethodResult, TranslateBrowsePathsToNodeIdsRequestFromJSON, BrowsePath, BrowsePathResult, FindServersRequestFromJSON, 
    ApplicationDescription, NodeClass, BrowseDirection, ReferenceTypeIds, Attributes, EndpointDescription, 
    GetEndpointsRequestFromJSON, DataValue,  
    StatusCodes,
    Variant} from "opcua-webapi";
import { UaPayloadMapper, makeUaStatusCode, UaBrowseResult, UaError, UaNodeAttributes, UaVariableAttributes, UaNodeId, UaDataValue, 
    UaVariantType, UaVariant, UaStatusCode, UaWriteValue, UaArgument, UaArrayType, UaExtensionObject } from "../common"
import { UaClientConfiguration, UaClientParameters } from "..";

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

    getNativeApi() : DefaultApi
    {
        return this.api;
    }

    // API wrappers by use cases
    async browseChild(
        nodeId: UaNodeId, 
        nodeClass?: number,
        maxReferences? : number) : Promise<UaBrowseResult>
    {
        let nodesToBrowse : BrowseDescription = {
            NodeId: nodeId.toString(),
            BrowseDirection: BrowseDirection.Forward,
            ReferenceTypeId: ReferenceTypeIds.HierarchicalReferences,
            IncludeSubtypes: true,
            NodeClassMask: 
                (nodeClass) ? nodeClass : Number(NodeClass.Object | NodeClass.Variable | NodeClass.Method),
            ResultMask: 63
        };

        let results = await this.browse([ nodesToBrowse ], maxReferences);

        if (results.length != 1) throw new UaError(makeUaStatusCode(StatusCodes.BadUnexpectedError));

        let statusCode = UaPayloadMapper.statusCodeFromWebApi(results[0].StatusCode);
        if (statusCode.isNotGood()) throw new UaError(statusCode);

        return UaPayloadMapper.browseResultFromWebApi(results[0]);
    }

    async browseReference(
        nodeId: UaNodeId,                 
        nodeClass?: number,        
        direction? : BrowseDirection,
        referenceTypeId? : UaNodeId,
        maxReferences? : number) : Promise<UaBrowseResult>
    {
        let nodesToBrowse : BrowseDescription = {
            NodeId: nodeId.toString(),
            BrowseDirection: (direction) ? direction : BrowseDirection.Forward,
            ReferenceTypeId: (referenceTypeId) ? referenceTypeId.toString() : ReferenceTypeIds.NonHierarchicalReferences,
            IncludeSubtypes: true,
            NodeClassMask: 
                (nodeClass) ? nodeClass : Number(NodeClass.Object | NodeClass.Variable | NodeClass.Method),
            ResultMask: 63
        };

        let results = await this.browse([ nodesToBrowse ], maxReferences);

        if (results.length != 1) throw new UaError(makeUaStatusCode(StatusCodes.BadUnexpectedError));

        let statusCode = UaPayloadMapper.statusCodeFromWebApi(results[0].StatusCode);
        if (statusCode.isNotGood()) throw new UaError(statusCode);

        return UaPayloadMapper.browseResultFromWebApi(results[0]);
    }

    async browseNextByCP(continuationPoint: string) : Promise<UaBrowseResult>
    {
        let results = await this.browseNext([continuationPoint], false);

        if (results.length != 1) throw new UaError(makeUaStatusCode(StatusCodes.BadUnexpectedError));
        let statusCode = UaPayloadMapper.statusCodeFromWebApi(results[0].StatusCode);
        if (statusCode.isNotGood()) throw new UaError(statusCode);

        return UaPayloadMapper.browseResultFromWebApi(results[0]);
    }

    async readValues(nodeIds : Array<UaNodeId>, timestampsToReturn?: number) : Promise<Array<UaDataValue>>
    {
        if (nodeIds.length == 0) throw new UaError(makeUaStatusCode(StatusCodes.BadNothingToDo));

        let nodesToRead: Array<ReadValueId> = [];

        for (let item of nodeIds)
        {
            nodesToRead.push({ NodeId: item.toString(), AttributeId: Attributes.Value });
        }

        let results = await this.read(nodesToRead, timestampsToReturn);

        let dataValues : Array<UaDataValue> = [];
                
        for (let item of results)
        {
            dataValues.push(UaPayloadMapper.dataValueFromWebApi(item));
        }

        return dataValues;
    }

    async writeValues(nodesToWrite : Array<UaWriteValue>) : Promise<Array<UaStatusCode>>
    {
        if (nodesToWrite.length == 0) throw new UaError(makeUaStatusCode(StatusCodes.BadNothingToDo));

        let writeValues: Array<WriteValue> = [];

        for (let item of nodesToWrite)
        {
            let variant = UaPayloadMapper.variantToWebApi(item.value);
            writeValues.push({ 
                NodeId: item.nodeId.toString(), 
                Value: {
                    UaType: variant.UaType,
                    Value: variant.Value
                },
                AttributeId: Attributes.Value });
        }

        let results = await this.write(writeValues);

        let statusCodes : Array<UaStatusCode> = [];
                
        for (let item of results)
        {
            statusCodes.push(UaPayloadMapper.statusCodeFromWebApi(item));
        }

        return statusCodes;
    }

    async readNodeAttributes(nodeId : UaNodeId, returnDescription? : boolean) : Promise<UaNodeAttributes>
    {
        let nodeIdStr = nodeId.toString();

        let nodesToRead: Array<ReadValueId> = [
            {  NodeId: nodeIdStr, AttributeId: Attributes.NodeClass },
            {  NodeId: nodeIdStr, AttributeId: Attributes.BrowseName },
            {  NodeId: nodeIdStr, AttributeId: Attributes.DisplayName },
            {  NodeId: nodeIdStr, AttributeId: Attributes.UserWriteMask }
        ];

        if (returnDescription)
        {
            nodesToRead.push({ NodeId: nodeIdStr, AttributeId: Attributes.Description });
        }

        let results = await this.read(nodesToRead);

        let dataValues : Array<UaDataValue> = [];
                
        for (let item of results)
        {
            dataValues.push(UaPayloadMapper.dataValueFromWebApi(item));
        }          

        if (dataValues[0].statusCode.isNotGood()) throw new UaError(dataValues[0].statusCode);  
        if (dataValues[1].statusCode.isNotGood()) throw new UaError(dataValues[1].statusCode);
        if (dataValues[2].statusCode.isNotGood()) throw new UaError(dataValues[2].statusCode);

        let nodeClassValue = dataValues[0].value;
        let browseNameValue = dataValues[1].value;
        let displayNameValue = dataValues[2].value;

        if (UaVariantType.Int32 != nodeClassValue.type ||
            UaVariantType.QualifiedName != browseNameValue.type ||
            UaVariantType.LocalizedText != displayNameValue.type) throw new UaError(makeUaStatusCode(StatusCodes.BadNodeAttributesInvalid));
        
        let writeMaskValue = (dataValues[3].statusCode.isGood() &&
                dataValues[3].value.type == UaVariantType.UInt32) ? dataValues[3].value : undefined;

        let descriptionValue = 
                (returnDescription && 
                dataValues[4].statusCode.isGood() &&
                dataValues[4].value.type == UaVariantType.LocalizedText) ? dataValues[4].value : undefined;

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

        let nodesToRead: Array<ReadValueId> = [];        
        
        for (let item of nodeIds)
        {
            let nodeIdString = item.toString();

            nodesToRead.push({ NodeId: nodeIdString, AttributeId: Attributes.NodeClass });
            nodesToRead.push({ NodeId: nodeIdString, AttributeId: Attributes.DataType });
            nodesToRead.push({ NodeId: nodeIdString, AttributeId: Attributes.ValueRank });
            nodesToRead.push({ NodeId: nodeIdString, AttributeId: Attributes.AccessLevel });
            nodesToRead.push({ NodeId: nodeIdString, AttributeId: Attributes.UserAccessLevel });
            nodesToRead.push({ NodeId: nodeIdString, AttributeId: Attributes.Historizing });
        }

        let results = await this.read(nodesToRead);

        let dataValues : Array<UaDataValue> = [];
        let ret : Array<UaVariableAttributes> = [];

        for (let item of results)
        {
            dataValues.push(UaPayloadMapper.dataValueFromWebApi(item));

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

    async methodCall(objectId: UaNodeId, methodId: UaNodeId, inputArguments: Array<UaVariant>) : Promise<Array<UaVariant>>
    {
        let inputs : Array<Variant> = [];

        for (let item of inputArguments)
        {
            inputs.push(UaPayloadMapper.variantToWebApi(item));
        }

        let methodsToCall: Array<CallMethodRequest> = [
            { ObjectId: objectId.toString(), MethodId: methodId.toString(), InputArguments: inputs }
        ];

        let results = await this.call(methodsToCall);

        if (results.length != 1) throw new UaError(makeUaStatusCode(StatusCodes.BadUnexpectedError));
        let statusCode = UaPayloadMapper.statusCodeFromWebApi(results[0].StatusCode);
        if (statusCode.isNotGood()) throw new UaError(statusCode);

        let outputs : Array<UaVariant> = [];
        for (let item of results[0].OutputArguments)
        {
            outputs.push(UaPayloadMapper.variantFromWebApi(item));
        }

        return outputs;
    }

    // Native APIs
    async browse(
        nodesToBrowse: Array<BrowseDescription>, 
        maxReferencesPerNode? : number,
        view?: ViewDescription,
        additionalParameters?: UaClientParameters) : Promise<Array<BrowseResult>>
    {
        let request = BrowseRequestFromJSON({
            RequestHeader: this.requestHeader(this.clientConfig, additionalParameters),
            NodesToBrowse: nodesToBrowse,
            RequestedMaxReferencesPerNode: (maxReferencesPerNode && maxReferencesPerNode > 0) ? maxReferencesPerNode : undefined,
            View: view }); 
            
        let response = await this.api.browse({browseRequest: request});

        if (response?.ResponseHeader?.ServiceResult?.Code) 
            throw new UaError(makeUaStatusCode(response.ResponseHeader.ServiceResult.Code));

        if (!response.Results || nodesToBrowse.length != response.Results.length) 
            throw new UaError(makeUaStatusCode(StatusCodes.BadDataLost));
        
        return response.Results;
    }

    async browseNext(
        continuationPoints: Array<string>, 
        releaseContinuationPoints: boolean,
        additionalParameters?: UaClientParameters) : Promise<Array<BrowseResult>>
    {
        let request = BrowseNextRequestFromJSON({
            RequestHeader: this.requestHeader(this.clientConfig, additionalParameters),
            ContinuationPoints: continuationPoints,
            ReleaseContinuationPoints: releaseContinuationPoints }); 

        let response = await this.api.browseNext({browseNextRequest: request});

        if (response?.ResponseHeader?.ServiceResult?.Code) 
            throw new UaError(makeUaStatusCode(response.ResponseHeader.ServiceResult.Code));

        if (!response.Results || continuationPoints.length != response.Results.length) 
            throw new UaError(makeUaStatusCode(StatusCodes.BadDataLost));
        
        return response.Results;
    }

    async translate(
        browsePaths: Array<BrowsePath>,
        additionalParameters?: UaClientParameters) : Promise<Array<BrowsePathResult>>
    {
        let request = TranslateBrowsePathsToNodeIdsRequestFromJSON({
            RequestHeader: this.requestHeader(this.clientConfig, additionalParameters),
            BrowsePaths: browsePaths }); 

        let response = await this.api.translateBrowsePathsToNodeIds({translateBrowsePathsToNodeIdsRequest: request});

        if (response?.ResponseHeader?.ServiceResult?.Code) 
            throw new UaError(makeUaStatusCode(response.ResponseHeader.ServiceResult.Code));

        if (!response.Results || browsePaths.length != response.Results.length) 
            throw new UaError(makeUaStatusCode(StatusCodes.BadUnknownResponse));
        
        return response.Results;
    }

    async read(
        nodesToRead: Array<ReadValueId>,
        timestampsToReturn?: number,
        maxAge?: number,        
        additionalParameters?: UaClientParameters) : Promise<Array<DataValue>>
    {
        let request = ReadRequestFromJSON({
            RequestHeader: this.requestHeader(this.clientConfig, additionalParameters),
            NodesToRead: nodesToRead,
            MaxAge: (maxAge && maxAge > 0) ? maxAge : undefined,
            TimestampsToReturn: 
               (timestampsToReturn && 
                timestampsToReturn >= TimestampsToReturn.Source && 
                timestampsToReturn < TimestampsToReturn.Invalid) ? timestampsToReturn : undefined }); 

        let response = await this.api.read({readRequest: request});

        if (response?.ResponseHeader?.ServiceResult?.Code) 
            throw new UaError(makeUaStatusCode(response.ResponseHeader.ServiceResult.Code));

        if (!response.Results || nodesToRead.length != response.Results.length) 
            throw new UaError(makeUaStatusCode(StatusCodes.BadDataLost));
        
        return response.Results;
    }

    async write(
        nodesToWrite: Array<WriteValue>,
        additionalParameters?: UaClientParameters) : Promise<Array<StatusCode>>
    {
        let request = WriteRequestFromJSON({
            RequestHeader: this.requestHeader(this.clientConfig, additionalParameters),
            NodesToWrite: nodesToWrite }); 

        let response = await this.api.write({writeRequest: request});

        if (response?.ResponseHeader?.ServiceResult?.Code) 
            throw new UaError(makeUaStatusCode(response.ResponseHeader.ServiceResult.Code));

        if (!response.Results || nodesToWrite.length != response.Results.length) 
            throw new UaError(makeUaStatusCode(StatusCodes.BadDataLost));
        
        return response.Results;
    }

    async call(
        methodsToCall: Array<CallMethodRequest>,
        additionalParameters?: UaClientParameters) : Promise<Array<CallMethodResult>>
    {
        let request = CallRequestFromJSON({
            RequestHeader: this.requestHeader(this.clientConfig, additionalParameters),
            MethodsToCall: methodsToCall }); 

        let response = await this.api.call({callRequest: request});

        if (response?.ResponseHeader?.ServiceResult?.Code) 
            throw new UaError(makeUaStatusCode(response.ResponseHeader.ServiceResult.Code));

        if (!response.Results || methodsToCall.length != response.Results.length) 
            throw new UaError(makeUaStatusCode(StatusCodes.BadDataLost));
        
        return response.Results;
    }
    
    async findServer(
        serverUris: Array<string>, 
        endpointUrl : string,
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

export type UaMethodArguments = {
    inputArguments : Array<UaArgument>;
    outputArguments : Array<UaArgument>;
}

