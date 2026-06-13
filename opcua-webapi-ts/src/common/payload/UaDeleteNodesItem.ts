import { DeleteNodesItem } from "opcua-webapi";
import { parseUaNodeIdOrNull, UaNodeId, UaStatusCode } from "../types";
import { DataTypeIds } from "../nodes";

export class UaDeleteNodesItem
{
    static dataTypeId : UaNodeId = new UaNodeId(DataTypeIds.DeleteNodesItem);
    
    private _nodeId: UaNodeId;
    private _deleteTargetReferences: boolean;

    constructor(
        nodeId: UaNodeId, 
        deleteTargetReferences: boolean = false)
    {
        this._nodeId = nodeId;
        this._deleteTargetReferences = deleteTargetReferences;
    }

    get nodeId() : UaNodeId
    {
        return this._nodeId;
    }

    get deleteTargetReferences() : boolean
    {
        return this._deleteTargetReferences;
    }

    toStruct() : DeleteNodesItem
    {
        let item : DeleteNodesItem = {
            NodeId: this._nodeId.toString(),
            DeleteTargetReferences: this._deleteTargetReferences
        };
        return item;
    }

    static fromStruct(item : DeleteNodesItem) : UaDeleteNodesItem | null
    {
        let nodeId = parseUaNodeIdOrNull(item.NodeId);
        if (!nodeId) return null;
        return new UaDeleteNodesItem(nodeId, item.DeleteTargetReferences);
    }
}