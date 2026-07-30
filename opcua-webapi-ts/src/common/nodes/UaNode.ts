import { NodeClass } from "opcua-webapi";
import { UaLocalizedText, UaModellingRule, UaNodeId } from "../types";
import { ObjectIds } from ".";

export abstract class UaNode
{
    protected _nodeId: UaNodeId;
    protected _browseName: string;
    protected _displayName: UaLocalizedText;
    protected _description: UaLocalizedText | null;
    protected _writeMask: number | null;
    protected _parent : UaNode | null;
    protected _children : Array<UaNode>;   
    protected _refToParent : UaNodeId | null; 

    constructor(
        nodeId: UaNodeId,
        browseName: string,
        displayName: UaLocalizedText)
    {
        this._nodeId = nodeId;
        this._browseName = browseName;
        this._displayName = displayName;
        this._description = null;
        this._writeMask = null;
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

    get writeMask() : number | null
    {
        return this._writeMask;
    }

    set writeMask(writeMask: number)
    {
        this._writeMask = writeMask;
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
        isAbstract: boolean)
    {
        super(nodeId, browseName, displayName);
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
    private _modellingRule : UaModellingRule;

    constructor(
        nodeId: UaNodeId,
        browseName: string,
        displayName: UaLocalizedText)
    {
        super(nodeId, browseName, displayName);
        this._modellingRule = UaModellingRule.None;
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

    get modellingRule() : UaModellingRule
    {
        return this._modellingRule;
    }

    setModellingRule(modellingRuleId : UaNodeId)
    {
        if (modellingRuleId.equal(UaNodeId.from(ObjectIds.ModellingRule_Mandatory))) this._modellingRule = UaModellingRule.Mandatory;
        else if (modellingRuleId.equal(UaNodeId.from(ObjectIds.ModellingRule_Optional))) this._modellingRule = UaModellingRule.Optional;
        else if (modellingRuleId.equal(UaNodeId.from(ObjectIds.ModellingRule_OptionalPlaceholder))) this._modellingRule = UaModellingRule.PlaceHolder;
        else if (modellingRuleId.equal(UaNodeId.from(ObjectIds.ModellingRule_MandatoryPlaceholder))) this._modellingRule = UaModellingRule.PlaceHolder;
        else this._modellingRule = UaModellingRule.None;
    }
}
