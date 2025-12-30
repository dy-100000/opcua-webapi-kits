package org.opcfoundation.webapi.mapper;

import org.eclipse.milo.opcua.stack.core.*;
import org.eclipse.milo.opcua.stack.core.types.builtin.*;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.BrowseDirection;
import org.opcfoundation.webapi.mapper.extensionobjects.ExtensionObjectEncoder;
import org.opcfoundation.webapi.model.ExtensionObject;
import org.springframework.lang.Nullable;
import org.opcfoundation.webapi.model.*;
import org.opcfoundation.webapi.model.StatusCode;
import org.opcfoundation.webapi.model.Variant;

import java.time.OffsetDateTime;
import java.util.*;

public class UaPayloadMapper {
    public static org.eclipse.milo.opcua.stack.core.types.structured.RequestHeader requestHeaderFromWebApi(@Nullable RequestHeader header)
    {
        NodeId authenticationToken = NodeId.NULL_VALUE;
        DateTime timestamp = DateTime.NULL_VALUE;
        UInteger requestHandle = UInteger.MIN;
        UInteger timeoutHint = UInteger.valueOf(0);

        if (null != header)
        {
            authenticationToken = UaTypeMapper.nodeIdFromWebApi(header.getAuthenticationToken());
            if (null == authenticationToken) authenticationToken = NodeId.NULL_VALUE;

            timestamp = UaTypeMapper.dateTimeFromWebApi(header.getTimestamp());
            if (null == timestamp) timestamp = DateTime.MIN_VALUE;

            if (null != header.getRequestHandle())
            {
                requestHandle = UInteger.valueOf(header.getRequestHandle());
            }

            if (header.getTimeoutHint() <= 0)
            {
                timeoutHint = UInteger.valueOf(5000L);
            } else if (header.getTimeoutHint() < 1000L)
            {
                timeoutHint = UInteger.valueOf(1000L);
            } else {
                timeoutHint = UInteger.valueOf(header.getTimeoutHint());
            }
        }

        return new org.eclipse.milo.opcua.stack.core.types.structured.RequestHeader(
                authenticationToken,
                timestamp,
                requestHandle,
                UInteger.MIN,
                null,
                timeoutHint,
                null);
    }

    public static ResponseHeader responseHeaderFromMilo(
            @Nullable RequestHeader requestHeader,
            org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode statusCode)
    {
        ResponseHeader responseHeader = new ResponseHeader();
        responseHeader.setServiceResult(UaTypeMapper.statusCodeFromMilo(statusCode));
        responseHeader.setTimestamp(OffsetDateTime.now());
        responseHeader.setRequestHandle(
                (null == requestHeader) ? 0L : requestHeader.getRequestHandle());
        responseHeader.setStringTable(new ArrayList<>());

        return responseHeader;
    }

    public static List<org.eclipse.milo.opcua.stack.core.types.structured.ReadValueId> nodesToReadFromWebApi(List<ReadValueId> nodesToRead) throws UaRuntimeException
    {
        ArrayList<org.eclipse.milo.opcua.stack.core.types.structured.ReadValueId> results = new ArrayList<>();

        for (ReadValueId item: nodesToRead)
        {
            NodeId nodeId = UaTypeMapper.nodeIdFromWebApi(item.getNodeId());
            if (null == nodeId) throw new UaRuntimeException(StatusCodes.Bad_InvalidArgument);

            org.eclipse.milo.opcua.stack.core.types.structured.ReadValueId nodeToRead =
                    new org.eclipse.milo.opcua.stack.core.types.structured.ReadValueId(
                            nodeId,
                            UInteger.valueOf(item.getAttributeId()),
                            (null == item.getIndexRange()) ? "" : item.getIndexRange(),
                            QualifiedName.NULL_VALUE);

            results.add(nodeToRead);
        }

        return results;
    }

    public static List<org.eclipse.milo.opcua.stack.core.types.structured.WriteValue> nodesToWriteFromWebApi(List<WriteValue> nodesToWrite) throws UaRuntimeException
    {
        ArrayList<org.eclipse.milo.opcua.stack.core.types.structured.WriteValue> results = new ArrayList<>();

        for (WriteValue item: nodesToWrite)
        {
            NodeId nodeId = UaTypeMapper.nodeIdFromWebApi(item.getNodeId());
            org.eclipse.milo.opcua.stack.core.types.builtin.DataValue dataValue =
                    (null == item.getValue()) ? null : UaTypeMapper.dataValueFromWebApi(item.getValue());

            if (null == nodeId || null == dataValue) throw new UaRuntimeException(StatusCodes.Bad_InvalidArgument);

            org.eclipse.milo.opcua.stack.core.types.structured.WriteValue nodeToWrite =
                    new org.eclipse.milo.opcua.stack.core.types.structured.WriteValue(
                            nodeId,
                            UInteger.valueOf(item.getAttributeId()),
                            (null == item.getIndexRange()) ? "" : item.getIndexRange(),
                            dataValue);

            results.add(nodeToWrite);
        }

        return results;
    }

    public static List<org.eclipse.milo.opcua.stack.core.types.structured.BrowseDescription> nodesToBrowseFromWebApi(List<BrowseDescription> nodesToBrowse) throws UaRuntimeException
    {
        ArrayList<org.eclipse.milo.opcua.stack.core.types.structured.BrowseDescription> results = new ArrayList<>();;

        for (BrowseDescription item: nodesToBrowse)
        {
            NodeId nodeId = UaTypeMapper.nodeIdFromWebApi(item.getNodeId());
            BrowseDirection direction = (null == item.getBrowseDirection()) ? null : BrowseDirection.from(item.getBrowseDirection());
            NodeId referenceTypeId = UaTypeMapper.nodeIdFromWebApi(item.getReferenceTypeId());

            if (null == nodeId || null == direction || null == referenceTypeId) throw new UaRuntimeException(StatusCodes.Bad_InvalidArgument);

            org.eclipse.milo.opcua.stack.core.types.structured.BrowseDescription nodeToBrowse =
                    new org.eclipse.milo.opcua.stack.core.types.structured.BrowseDescription(
                            nodeId,
                            direction,
                            referenceTypeId,
                            item.getIncludeSubtypes(),
                            UInteger.valueOf(item.getNodeClassMask()),
                            UInteger.valueOf(item.getResultMask()));

            results.add(nodeToBrowse);
        }

        return results;
    }

    public static org.eclipse.milo.opcua.stack.core.types.structured.ViewDescription viewDescriptionFromWebApi(@Nullable ViewDescription viewDescription)
    {
        org.eclipse.milo.opcua.stack.core.types.structured.ViewDescription result = new org.eclipse.milo.opcua.stack.core.types.structured.ViewDescription(
                NodeId.NULL_VALUE,
                DateTime.NULL_VALUE,
                UInteger.MIN);

        if (null != viewDescription)
        {
            NodeId viewId = UaTypeMapper.nodeIdFromWebApi(viewDescription.getViewId());
            DateTime timestamp = UaTypeMapper.dateTimeFromWebApi(viewDescription.getTimestamp());

            if (null == viewId) viewId = NodeId.NULL_VALUE;

            result = new org.eclipse.milo.opcua.stack.core.types.structured.ViewDescription(
                    viewId,
                    timestamp,
                    UInteger.valueOf(viewDescription.getViewVersion()));
        }

        return result;
    }

    public static ReferenceDescription referenceDescriptionsFromMilo(org.eclipse.milo.opcua.stack.core.types.structured.ReferenceDescription description)
    {
        ReferenceDescription ret = new ReferenceDescription();

        ret.setReferenceTypeId(description.getReferenceTypeId().toParseableString());
        ret.setIsForward(description.getIsForward());
        ret.setNodeId(description.getNodeId().toParseableString());
        ret.setNodeClass(description.getNodeClass().getValue());
        ret.setBrowseName(description.getBrowseName().getName());
        ret.setDisplayName(UaTypeMapper.localizedTextFromMilo(description.getDisplayName()));
        ret.setTypeDefinition(description.getTypeDefinition().toParseableString());

        return ret;
    }

    public static List<BrowseResult> browseResultsFromMilo(List<org.eclipse.milo.opcua.stack.core.types.structured.BrowseResult> browseResults)
    {
        ArrayList<BrowseResult> ret = new ArrayList<>();

        for (org.eclipse.milo.opcua.stack.core.types.structured.BrowseResult item : browseResults)
        {
            BrowseResult result = new BrowseResult();
            ArrayList<ReferenceDescription> descriptions = new ArrayList<>();

            if (null != item.getReferences())
            {
                for (org.eclipse.milo.opcua.stack.core.types.structured.ReferenceDescription itemL2 : item.getReferences())
                {
                    descriptions.add(referenceDescriptionsFromMilo(itemL2));
                }
            }

            result.setReferences(descriptions);
            result.setStatusCode(UaTypeMapper.statusCodeFromMilo(item.getStatusCode()));
            if (item.getContinuationPoint().isNotNull()) result.setContinuationPoint(item.getContinuationPoint().bytes());

            ret.add(result);
        }

        return ret;
    }

    public static org.eclipse.milo.opcua.stack.core.types.structured.RelativePath relativePathFromWebApi(@Nullable RelativePath path) throws UaRuntimeException
    {
        if (null == path) return new org.eclipse.milo.opcua.stack.core.types.structured.RelativePath(null);

        List<RelativePathElement> pathElements = path.getElements();
        org.eclipse.milo.opcua.stack.core.types.structured.RelativePathElement[] elements = new org.eclipse.milo.opcua.stack.core.types.structured.RelativePathElement[pathElements.size()];

        if (pathElements.isEmpty()) throw new UaRuntimeException(StatusCodes.Bad_InvalidArgument);

        int index = 0;
        for (RelativePathElement item : pathElements)
        {
            NodeId referenceTypeId = UaTypeMapper.nodeIdFromWebApi(item.getReferenceTypeId());
            QualifiedName targetName = (null == item.getTargetName()) ? null : new QualifiedName(0,item.getTargetName());

            if (null == referenceTypeId || null == targetName) throw new UaRuntimeException(StatusCodes.Bad_InvalidArgument);

            elements[index] = new org.eclipse.milo.opcua.stack.core.types.structured.RelativePathElement(
                    referenceTypeId,
                    item.getIsInverse(),
                    item.getIncludeSubtypes(),
                    targetName);

            index++;
        }

        return new org.eclipse.milo.opcua.stack.core.types.structured.RelativePath(elements);
    }

    public static List<org.eclipse.milo.opcua.stack.core.types.structured.BrowsePath> browsePathsFromWebApi(List<BrowsePath> paths) throws UaRuntimeException
    {
        ArrayList<org.eclipse.milo.opcua.stack.core.types.structured.BrowsePath> results = new ArrayList<>();

        for (BrowsePath item: paths)
        {
            NodeId nodeId = UaTypeMapper.nodeIdFromWebApi(item.getStartingNode());
            if (null == nodeId) throw new UaRuntimeException(StatusCodes.Bad_InvalidArgument);

            org.eclipse.milo.opcua.stack.core.types.structured.RelativePath relativePath = relativePathFromWebApi(item.getRelativePath());

            org.eclipse.milo.opcua.stack.core.types.structured.BrowsePath path =
                    new org.eclipse.milo.opcua.stack.core.types.structured.BrowsePath(
                            nodeId,
                            relativePath);

            results.add(path);
        }

        return results;
    }

    public static BrowsePathTarget browsePathTargetFromMilo(org.eclipse.milo.opcua.stack.core.types.structured.BrowsePathTarget target)
    {
        BrowsePathTarget result = new BrowsePathTarget();
        result.setTargetId(target.getTargetId().toParseableString());
        result.setRemainingPathIndex(target.getRemainingPathIndex().longValue());
        return result;
    }

    public static List<BrowsePathResult> browsePathResultsFromMilo(List<org.eclipse.milo.opcua.stack.core.types.structured.BrowsePathResult> browsePathResults)
    {
        ArrayList<BrowsePathResult> results = new ArrayList<>();

        for (org.eclipse.milo.opcua.stack.core.types.structured.BrowsePathResult item : browsePathResults)
        {
            ArrayList<BrowsePathTarget> target = new ArrayList<>();
            if (null != item.getTargets())
            {
                for (org.eclipse.milo.opcua.stack.core.types.structured.BrowsePathTarget itemL2 : item.getTargets())
                {
                    target.add(browsePathTargetFromMilo(itemL2));
                }
            }

            BrowsePathResult result = new BrowsePathResult();
            result.setStatusCode(UaTypeMapper.statusCodeFromMilo(item.getStatusCode()));
            result.setTargets(target);

            results.add(result);
        }

        return results;
    }

    public static List<org.eclipse.milo.opcua.stack.core.types.structured.CallMethodRequest> callMethodRequestsFromWebApi(List<CallMethodRequest> methodCalls) throws UaRuntimeException
    {
        ArrayList<org.eclipse.milo.opcua.stack.core.types.structured.CallMethodRequest> ret = new ArrayList<>();

        for (CallMethodRequest item: methodCalls)
        {
            NodeId objectId = UaTypeMapper.nodeIdFromWebApi(item.getObjectId());
            NodeId methodId = UaTypeMapper.nodeIdFromWebApi(item.getMethodId());
            List<org.eclipse.milo.opcua.stack.core.types.builtin.Variant> inputArguments = UaTypeMapper.variantsFromWebApi(item.getInputArguments());

            if (null == objectId || null == methodId) throw new UaRuntimeException(StatusCodes.Bad_InvalidArgument);

            org.eclipse.milo.opcua.stack.core.types.structured.CallMethodRequest methodCall = new org.eclipse.milo.opcua.stack.core.types.structured.CallMethodRequest(
                    objectId,
                    methodId,
                    inputArguments.toArray(new org.eclipse.milo.opcua.stack.core.types.builtin.Variant[0]));

            ret.add(methodCall);
        }

        return ret;
    }

    public static List<CallMethodResult> callMethodResultsFromMilo(List<org.eclipse.milo.opcua.stack.core.types.structured.CallMethodResult> callResults)
    {
        ArrayList<CallMethodResult> results = new ArrayList<>();

        for (org.eclipse.milo.opcua.stack.core.types.structured.CallMethodResult item: callResults)
        {
            StatusCode statusCode = UaTypeMapper.statusCodeFromMilo(item.getStatusCode());
            List<Variant> outputArguments = new ArrayList<>();
            List<StatusCode> inputArgumentResults = new ArrayList<>();

            if (null != item.getOutputArguments()) outputArguments = UaTypeMapper.variantsFromMilo(Arrays.asList(item.getOutputArguments()));
            if (null != item.getInputArgumentResults()) inputArgumentResults = UaTypeMapper.statusCodesFromMilo(Arrays.asList(item.getInputArgumentResults()));

            CallMethodResult result = new CallMethodResult();
            result.setStatusCode(statusCode);
            result.setOutputArguments(outputArguments);
            result.setInputArgumentResults(inputArgumentResults);

            results.add(result);
        }

        return results;
    }

    public static List<org.eclipse.milo.opcua.stack.core.types.structured.HistoryReadValueId> historyReadValueIdsFromWebApi(List<HistoryReadValueId> historyReadValueIds) throws UaRuntimeException
    {
        ArrayList<org.eclipse.milo.opcua.stack.core.types.structured.HistoryReadValueId> ret = new ArrayList<>();

        for (HistoryReadValueId item: historyReadValueIds)
        {
            NodeId nodeId = UaTypeMapper.nodeIdFromWebApi(item.getNodeId());
            ByteString continuationPoint = (null == item.getContinuationPoint()) ? ByteString.NULL_VALUE : ByteString.of(item.getContinuationPoint());

            if (null == nodeId) throw new UaRuntimeException(StatusCodes.Bad_DecodingError);

            org.eclipse.milo.opcua.stack.core.types.structured.HistoryReadValueId historyReadValueId = new org.eclipse.milo.opcua.stack.core.types.structured.HistoryReadValueId(
                    nodeId,
                    "",
                    QualifiedName.NULL_VALUE,
                    continuationPoint);

            ret.add(historyReadValueId);
        }

        return ret;
    }

    public static List<HistoryReadResult> historyReadResultsFromMilo(List<org.eclipse.milo.opcua.stack.core.types.structured.HistoryReadResult> results)
    {
        List<HistoryReadResult> ret = new ArrayList<>();

        for (org.eclipse.milo.opcua.stack.core.types.structured.HistoryReadResult item : results)
        {
            HistoryReadResult result = new HistoryReadResult();
            result.setStatusCode(UaTypeMapper.statusCodeFromMilo(item.getStatusCode()));
            result.setContinuationPoint (item.getContinuationPoint().isNull() ? null : item.getContinuationPoint().bytes());
            result.setHistoryData(ExtensionObjectEncoder.Encoder.toExtensionObjectWebApi(item.getHistoryData()));
            ret.add(result);
        }

        return ret;
    }

    public static ApplicationDescription applicationDescriptionFromMilo(org.eclipse.milo.opcua.stack.core.types.structured.ApplicationDescription description)
    {
        ApplicationDescription result = new ApplicationDescription();

        result.setApplicationType(description.getApplicationType().getValue());

        result.setApplicationUri(
                (null == description.getApplicationUri()) ? "" : description.getApplicationUri());
        result.setApplicationName(
                (null == description.getApplicationName()) ?  null : UaTypeMapper.localizedTextFromMilo(description.getApplicationName()));
        result.setProductUri(
                (null == description.getProductUri()) ? "" : description.getProductUri());
        result.setDiscoveryUrls(
                (null == description.getDiscoveryUrls()) ? new ArrayList<>() : Arrays.asList(description.getDiscoveryUrls()));
        result.setDiscoveryProfileUri(
                (null == description.getDiscoveryProfileUri()) ? "" : description.getDiscoveryProfileUri());
        result.setGatewayServerUri(
                (null == description.getGatewayServerUri()) ? "" : description.getGatewayServerUri());

        return result;
    }

    public static List<ApplicationDescription> applicationDescriptionsFromMilo(List<org.eclipse.milo.opcua.stack.core.types.structured.ApplicationDescription> descriptions)
    {
        ArrayList<ApplicationDescription> results = new ArrayList<>();
        for (org.eclipse.milo.opcua.stack.core.types.structured.ApplicationDescription item : descriptions)
        {
            results.add(applicationDescriptionFromMilo(item));
        }
        return results;
    }

    public static EndpointDescription endpointDescriptionFromMilo(org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription description)
    {
        EndpointDescription endpointDescription = new EndpointDescription();
        endpointDescription.setEndpointUrl(
                (null == description.getEndpointUrl()) ? "" : description.getEndpointUrl());
        endpointDescription.setServer(applicationDescriptionFromMilo(description.getServer()));

        endpointDescription.setSecurityMode(description.getSecurityMode().getValue());
        endpointDescription.setSecurityPolicyUri(
                (null == description.getSecurityPolicyUri()) ? "" : description.getSecurityPolicyUri());

        endpointDescription.setServerCertificate(
                (description.getServerCertificate().isNull()) ? new byte[0] : description.getServerCertificate().bytes());

        endpointDescription.setTransportProfileUri(
                (null == description.getTransportProfileUri()) ? "" : description.getTransportProfileUri());

        endpointDescription.setUserIdentityTokens(new ArrayList<>());
        /*
        if (null != description.getUserIdentityTokens())
        {
            for (org.eclipse.milo.opcua.stack.core.types.structured.UserTokenPolicy item : description.getUserIdentityTokens())
            {
                UserTokenPolicy policy = userTokenPolicyFromMilo(item);
                endpointDescription.addUserIdentityTokensItem(policy);
            }
        } */

        return endpointDescription;
    }

    public static List<EndpointDescription> endpointDescriptionsFromMilo(List<org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription> descriptions)
    {
        ArrayList<EndpointDescription> results = new ArrayList<>();
        for (org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription item : descriptions)
        {
            results.add(endpointDescriptionFromMilo(item));
        }
        return results;
    }

    /*
    public static UserTokenPolicy userTokenPolicyFromMilo(org.eclipse.milo.opcua.stack.core.types.structured.UserTokenPolicy userTokenPolicy) {
        UserTokenPolicy ret = new UserTokenPolicy();
        ret.setTokenType(userTokenPolicy.getTokenType().getValue());
        ret.setPolicyId(userTokenPolicy.getPolicyId());
        ret.setIssuedTokenType(userTokenPolicy.getIssuedTokenType());
        ret.setSecurityPolicyUri(userTokenPolicy.getSecurityPolicyUri());
        ret.setIssuerEndpointUrl(userTokenPolicy.getIssuerEndpointUrl());
        return ret;
    }
    */
}
