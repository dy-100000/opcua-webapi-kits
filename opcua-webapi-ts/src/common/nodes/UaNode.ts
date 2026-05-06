import { NodeClass } from "opcua-webapi";
import { UaLocalizedText, UaNodeId } from "../types";

export abstract class UaNode
{
    protected _nodeId: UaNodeId;
    protected _browseName: string;
    protected _displayName: UaLocalizedText;
    protected _description: UaLocalizedText | null;
    protected _writeMask: number;
    protected _parent : UaNode | null;
    protected _children : Array<UaNode>;   
    protected _refToParent : UaNodeId | null; 

    constructor(
        nodeId: UaNodeId,
        browseName: string,
        displayName: UaLocalizedText,
        writeMask?: number | null)
    {
        this._nodeId = nodeId;
        this._browseName = browseName;
        this._displayName = displayName;
        this._description = null;
        this._writeMask = (writeMask) ? writeMask : 0;
        this._parent = null;
        this._children = [];
        this._refToParent = null;
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

    get description() : UaLocalizedText | null
    {
        return this._description;
    }

    set description(description: UaLocalizedText | null)
    {
        this._description = description;
    }

    get refToParent() : UaNodeId | null
    {
        return this._refToParent;
    }

    set refToParent(referenceTypeId: UaNodeId | null)
    {
        this._refToParent = referenceTypeId;
    }

    abstract toJson() : any
}

export abstract class UaDefintionNode extends UaNode
{
    protected _isAbstract : boolean;    

    constructor(
        nodeId: UaNodeId,
        browseName: string,
        displayName: UaLocalizedText,
        isAbstract: boolean,
        writeMask?: number | null)
    {
        super(nodeId, browseName, displayName, writeMask);
        this._isAbstract = isAbstract;
    }

    get isAbstract() : boolean
    {
        return this._isAbstract;
    }

    get refToParent() : UaNodeId | null
    {
        return (this._refToParent) ? this._refToParent : null;
    }

    set refToParent(referenceTypeId: UaNodeId)
    {
        this._refToParent = referenceTypeId;
    }

    setParentType(parentType : UaDefintionNode)
    {
        if (parentType.nodeClass != this.nodeClass ||
            null != this._parent) return;

        this._parent = parentType;
        parentType._children.push(this);
    }

    isSubtypeOf(typeId : UaNodeId) : boolean
    {     
        if (typeId.equal(this._nodeId)) return true;
        if (null == this._parent) return false;
        return (this._parent as UaDefintionNode).isSubtypeOf(typeId);
    }

    parentType() : UaDefintionNode | null
    {
        return this._parent as UaDefintionNode;
    }

    childTypes() : Array<UaDefintionNode>
    {
        let ret : Array<UaDefintionNode> = [];

        for (let item of this._children)
        {
            if (item.nodeClass == this.nodeClass) ret.push(item as UaDefintionNode);
        }

        return ret;
    }
}

export abstract class UaInstanceNode extends UaNode
{
    constructor(
        nodeId: UaNodeId,
        browseName: string,
        displayName: UaLocalizedText,
        writeMask?: number | null)
    {
        super(nodeId, browseName, displayName, writeMask);
    }

    getMembers(nodeClass? : NodeClass) : Array<UaInstanceNode>
    {
        let ret : Array<UaInstanceNode> = [];

        for (let item of this._children)
        {
            if (nodeClass && nodeClass != item.nodeClass) continue;
            ret.push(item as UaInstanceNode);
        }
        
        return ret;
    }
}
