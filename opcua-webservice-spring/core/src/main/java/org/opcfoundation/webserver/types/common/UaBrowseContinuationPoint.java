package org.opcfoundation.webserver.types.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.BrowseDirection;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowseDescription;
import org.springframework.lang.Nullable;

public class UaBrowseContinuationPoint {
    private static final ObjectMapper jsonMapper = new ObjectMapper();

    private String id;
    private Integer bDir;
    private String refId;
    private Boolean includeSt;
    private Integer ncMask;
    private Integer rltMask;
    private Integer maxRef;
    private Integer offset;
    private Integer taskMsk;

    public UaBrowseContinuationPoint()
    {
        id = "";
        bDir = 0;
        refId = "";
        includeSt = false;
        ncMask = 0;
        rltMask = 0;
        maxRef = 0;
        offset = 0;
        taskMsk = 0;
    }

    public UaBrowseContinuationPoint(
            BrowseDescription description,
            UaBrowseAdditionalInfo additionalInfo)
    {
        id = description.getNodeId().toParseableString();
        bDir = description.getBrowseDirection().getValue();
        refId = description.getReferenceTypeId().toParseableString();
        includeSt = description.getIncludeSubtypes();
        ncMask = description.getNodeClassMask().intValue();
        rltMask = description.getResultMask().intValue();
        maxRef = additionalInfo.getMaxReferencesPerNode();
        offset = additionalInfo.getReferenceOffset();
        taskMsk = additionalInfo.getTaskCheckListMasks();
    }

    public @Nullable BrowseDescription browseDescription()
    {
        NodeId nodeId = NodeId.parseOrNull(id);
        BrowseDirection browseDirection = BrowseDirection.from(bDir);
        NodeId referenceTypeId = NodeId.parseOrNull(refId);
        if (null == nodeId || null == browseDirection || 0 > ncMask || 0 > rltMask) return null;
        return new BrowseDescription(nodeId,browseDirection, referenceTypeId, includeSt, UInteger.valueOf(ncMask), UInteger.valueOf(rltMask));
    }

    public UaBrowseAdditionalInfo additionalInfo()
    {
        return new UaBrowseAdditionalInfo(maxRef, offset, taskMsk);
    }

    public ByteString toByteString()
    {
        try
        {
            return ByteString.of(jsonMapper.writeValueAsBytes(this));
        } catch (Exception e) {
            return ByteString.NULL_VALUE;
        }
    }

    public static @Nullable UaBrowseContinuationPoint fromByteString(ByteString continuationPoint)
    {
        try {
            if (continuationPoint.isNull()) return null;
            return jsonMapper.readValue(continuationPoint.bytes(), UaBrowseContinuationPoint.class);
        } catch (Exception e) {
            return null;
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getbDir() {
        return bDir;
    }

    public void setbDir(Integer bDir) {
        this.bDir = bDir;
    }

    public String getRefId() {
        return refId;
    }

    public void setRefId(String refId) {
        this.refId = refId;
    }

    public Boolean getIncludeSt() {
        return includeSt;
    }

    public void setIncludeSt(Boolean includeSt) {
        this.includeSt = includeSt;
    }

    public Integer getRltMask() {
        return rltMask;
    }

    public void setRltMask(Integer rltMask) {
        this.rltMask = rltMask;
    }

    public Integer getNcMask() {
        return ncMask;
    }

    public void setNcMask(Integer ncMask) {
        this.ncMask = ncMask;
    }

    public Integer getMaxRef() {
        return Math.max(maxRef,0);
    }

    public void setMaxRef(Integer maxRef) {
        this.maxRef = maxRef;
    }

    public Integer getOffset() { return offset; }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getTaskMsk() { return taskMsk; }

    public void setTaskMsk(Integer taskMsk) { this.taskMsk = taskMsk; }
}
