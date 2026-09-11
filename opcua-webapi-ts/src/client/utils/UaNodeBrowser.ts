import { BrowseDirection, NodeClass } from "opcua-webapi";
import { UaError, UaLocalizedText, UaNodeId, parseUaNodeId, UaBrowseDescription, ReferenceTypeIds, UaReference } from "../../common";
import { UaWebClient } from "../UaWebClient"

type CpToBrowse = {
    nodeId: UaNodeId;
    continuationPoint: string;
}

export type BrowseReferenceResult = {
    nodeId: UaNodeId;
    references: Array<UaReference>;
}

export class UaNodeBrowser {
    private _client : UaWebClient;
    private _nodesToBrowse: Array<UaNodeId>;
    private _continuationPointToBrowse: Array<CpToBrowse>;
    private _referenceType: UaNodeId;
    private _nodeClassToReturn: number;
    private _maxNodesPerBrowse: number;
    private _maxReferencesPerNode: number;
    private _readResults: Map<string,Array<UaReference>>;

    constructor(
        client: UaWebClient,
        nodeIds: Array<UaNodeId>,
        referenceType: UaNodeId,
        nodeClassToReturn: number,
        maxNodesPerBrowse?: number | null,
        maxReferencesPerNode?: number | null) 
    {
        this._client = client;
        this._nodesToBrowse = [];
        this._referenceType = referenceType;
        this._nodeClassToReturn = nodeClassToReturn;
        this._continuationPointToBrowse = [];
        this._maxNodesPerBrowse = (null == maxNodesPerBrowse || maxNodesPerBrowse <= 0) ? 50 : maxNodesPerBrowse;
        this._maxReferencesPerNode = (null == maxReferencesPerNode || maxReferencesPerNode < 0) ? 50 : maxReferencesPerNode;
        this._readResults = new Map<string, Array<UaReference>>();

        if (this._nodeClassToReturn != 0)
        {
            let nodeIdSet: Set<string> = new Set;
            for (let item of nodeIds) {
                if (nodeIdSet.has(item.toString())) continue;
                nodeIdSet.add(item.toString());
                this._nodesToBrowse.push(item);
            }
        }
    }

    async browse() {
        if (this.isFinish()) return;
        await this.browseOnce();
        await this.browse();
    }

    public async browseOnce() {  
        if (this._continuationPointToBrowse.length != 0) {
            await this._browseContinuationPoints();
        } else if (this._nodesToBrowse.length != 0) {
            await this._browseNodes();
        } 
    }

    results(): Array<BrowseReferenceResult> {
        let results: Array<BrowseReferenceResult> = [];
        for (let item of this._readResults) {
            results.push({
                nodeId: parseUaNodeId(item[0]),
                references: item[1]
            });                       
        }

        this._readResults.clear();

        return results;
    }

    isFinish(): boolean {
        return ((this._nodesToBrowse.length == 0) && (this._continuationPointToBrowse.length == 0)) || 0 == this._nodeClassToReturn;
    }

    private async _browseNodes() {
        let nodesToBrowse = this._nodesToBrowse.splice(0, this._maxNodesPerBrowse);
        let browseDescriptions: Array<UaBrowseDescription> = [];

        for (let item of nodesToBrowse) {
            let browseDescription = new UaBrowseDescription(
                item,
                BrowseDirection.Forward,
                this._referenceType,
                true,
                this._nodeClassToReturn,
                63
            );

            browseDescriptions.push(browseDescription);
        }

        let results = await this._client.browse(browseDescriptions, this._maxReferencesPerNode);

        for (let i = 0; i < nodesToBrowse.length; ++i) {            
            let currentNodeId = nodesToBrowse[i];
            let currentResult = results[i];

            if (currentResult.statusCode.isNotGood()) throw new UaError(currentResult.statusCode);

            let currentReferences: Array<UaReference> = this._readResults.get(currentNodeId.toString());
            if (!currentReferences)
            {
                currentReferences = [];
                this._readResults.set(currentNodeId.toString(), currentReferences);
            }

            let newReferences: Array<UaReference> = [];
            for (let item of currentResult.references) {
                let nodeId = item.nodeId.getNodeId();
                let typeDefinition = item.typeDefinition?.getNodeId();
                if (null == nodeId || null === typeDefinition) continue;

                let reference = new UaReference(
                        nodeId,
                        (item.nodeClass) ? item.nodeClass : NodeClass.Unspecified,
                        item.browseName ? item.browseName : "",
                        item.displayName ? item.displayName : UaLocalizedText.nullText,
                        item.referenceTypeId ? item.referenceTypeId : UaNodeId.nullNodeId,
                        (typeDefinition) ? typeDefinition : UaNodeId.nullNodeId);

                currentReferences.push(reference);
                newReferences.push(reference);
            }

            if (currentResult.continuationPoint) {
                this._continuationPointToBrowse.push({
                    nodeId: currentNodeId,
                    continuationPoint: currentResult.continuationPoint
                });
            }           
        }
    }

    private async _browseContinuationPoints() {
        let cpsToBrowse = this._continuationPointToBrowse.splice(0, this._maxNodesPerBrowse);
        let continuationPoints: Array<string> = [];

        for (let item of cpsToBrowse) {
            continuationPoints.push(item.continuationPoint);
        }

        let results = await this._client.browseNext(continuationPoints, false);

        for (let i = 0; i < cpsToBrowse.length; ++i) {
            let currentNodeId = cpsToBrowse[i].nodeId;
            let currentResult = results[i];

            if (currentResult.statusCode.isNotGood()) throw new UaError(currentResult.statusCode);

            let newReferences: Array<UaReference> = [];
            for (let item of currentResult.references) {
                let nodeId = item.nodeId.getNodeId();
                let typeDefinition = item.typeDefinition?.getNodeId();
                if (null == nodeId || null === typeDefinition) continue;

                let currentReferences: Array<UaReference> = this._readResults.get(currentNodeId.toString());
                if (!currentReferences) 
                {
                    currentReferences = [];
                    this._readResults.set(currentNodeId.toString(), currentReferences);
                }

                let reference = new UaReference(
                        nodeId,
                        (item.nodeClass) ? item.nodeClass : NodeClass.Unspecified,
                        item.browseName ? item.browseName : "",
                        item.displayName ? item.displayName : UaLocalizedText.nullText,
                        item.referenceTypeId ? item.referenceTypeId : UaNodeId.nullNodeId,
                        (typeDefinition) ? typeDefinition : UaNodeId.nullNodeId);

                currentReferences.push(reference);
                newReferences.push(reference);
            }

            if (currentResult.continuationPoint) {
                this._continuationPointToBrowse.push({
                    nodeId: currentNodeId,
                    continuationPoint: currentResult.continuationPoint
                });
            }
        }
    }
}

export class UaObjectBrowser extends UaNodeBrowser {
    constructor(
        client: UaWebClient,
        nodeIds: Array<UaNodeId>,
        referenceType: UaNodeId = UaNodeId.from(ReferenceTypeIds.HierarchicalReferences)) {
        super(
            client,
            nodeIds, 
            referenceType,
            NodeClass.Object, 
            20);
    }
}

export class UaTypeBrowser extends UaNodeBrowser {
    constructor(
        client: UaWebClient,
        nodeIds: Array<UaNodeId>) {
        super(
            client,
            nodeIds, 
            UaNodeId.from(ReferenceTypeIds.HierarchicalReferences),
            NodeClass.ObjectType| NodeClass.VariableType | NodeClass.DataType | NodeClass.ReferenceType, 
            20);
    }
}