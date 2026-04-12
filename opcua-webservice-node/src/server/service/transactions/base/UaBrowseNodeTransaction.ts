import { UaBrowseDescription } from "opcua-webapi-ts";
import { NodeManager } from "../../../addressspace/nodemanager";
import { ServiceContext, UaBrowseAdditionalInfo } from "../../../types";
import { UaBrowseTransaction } from "./UaBrowseTransaction";

export class UaBrowseNodeTransaction extends UaBrowseTransaction {
    private readonly _nodeManager: NodeManager;

    constructor(
        serviceContext: ServiceContext,
        nodeToBrowse: UaBrowseDescription,
        additionalInfo: UaBrowseAdditionalInfo,
        handleId: number,
        nodeManager: NodeManager) {
        super(serviceContext, nodeToBrowse, additionalInfo, handleId);
        this._nodeManager = nodeManager;
    }

    async execute(): Promise<void> {
        try {
            const result = this._nodeManager.browse(this.getItem(), this._additionalInfo);
            this._statusCode = result.statusCode;
            this._continuationPoint = result.continuationPoint;
            this._references = result.references;
        } catch (error) {
            this.buildErrorResults(error);
        }
    }
}