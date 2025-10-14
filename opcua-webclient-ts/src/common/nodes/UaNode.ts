import { NodeClass } from "opcua-webapi";
import { UaLocalizedText, UaNodeId } from "../types";

export abstract class UaNode
{
    protected _nodeId: UaNodeId;
    protected _browseName: string;
    protected _displayName: UaLocalizedText;
    protected _writeMask: number;

    constructor(
        nodeId: UaNodeId,
        browseName: string,
        displayName: UaLocalizedText,
        writeMask?: number | null)
    {
        this._nodeId = nodeId;
        this._browseName = browseName;
        this._displayName = displayName;
        this._writeMask = (writeMask) ? writeMask : 0;
    }

    abstract get nodeClass() : NodeClass;

    get nodeId() : UaNodeId
    {
        return this._nodeId;
    }

    get browseName() : string
    {
        return this._browseName;
    }

    get displayName() : UaLocalizedText
    {
        return this._displayName;
    }

    get writeMask() : number
    {
        return this._writeMask;
    }
}

export abstract class UaDefintionNode extends UaNode
{
    protected _isAbstract : boolean;
    protected _parentType : UaDefintionNode | null;
    protected _childTypes : Array<UaDefintionNode>;

    constructor(
        nodeId: UaNodeId,
        browseName: string,
        displayName: UaLocalizedText,
        isAbstract: boolean,
        writeMask?: number | null)
    {
        super(nodeId, browseName, displayName, writeMask);
        this._isAbstract = isAbstract;
        this._parentType = null;
        this._childTypes = [];
    }

    get isAbstract() : boolean
    {
        return this._isAbstract;
    }

    setParentType(parentType : UaDefintionNode)
    {
        if (parentType.nodeClass != this.nodeClass ||
            null != this._parentType) return;

        this._parentType = parentType;
        parentType._childTypes.push(this);
    }
}
