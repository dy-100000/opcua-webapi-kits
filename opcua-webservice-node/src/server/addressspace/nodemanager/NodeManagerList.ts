import { StatusCodes } from "opcua-webapi";
import { makeUaStatusCode, UaError } from "opcua-webapi-ts";
import { NodeManagerBase } from "./NodeManagerBase";

export class NodeManagerList {
    private readonly _nodeManagers: Array<NodeManagerBase>;
    static readonly nodeManagerList = new NodeManagerList();

    constructor() {
        this._nodeManagers = [];
    }

    getNsIndexes(): Array<number> {
        const ret: Array<number> = [];

        for (const item of this._nodeManagers) {
            ret.push(item.nsIndex());
        }

        return ret;
    }

    getNewNsIndex(): number {
        return this._nodeManagers.length;
    }

    getNodeManager(nsIndex: number): NodeManagerBase | null {
        if (this._nodeManagers.length <= nsIndex) {
            return null;
        }

        return this._nodeManagers[nsIndex] ?? null;
    }

    addNodeManager(nodeManager: NodeManagerBase): void {
        if (nodeManager.nsIndex() !== this._nodeManagers.length) {
            throw new UaError(makeUaStatusCode(StatusCodes.BadInternalError));
        }

        this._nodeManagers.splice(nodeManager.nsIndex(), 0, nodeManager);
    }
}