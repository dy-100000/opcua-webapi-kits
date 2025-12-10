import { BrowseDescription, BrowseDirection, NodeClass } from "opcua-webapi";
import { parseUaNodeIdOrNull, UaLocalizedText, UaNodeId, UaPayloadMapper, ReferenceTypeIds, ObjectTypeIds } from "../../common"
import { UaWebClient } from "../UaWebClient"
import { UaObjectType } from "../../common/nodes/UaObjectType";

export class UaObjectTypeDictionary
{
    private _objectTypes : Map<UaNodeId, UaObjectType>;
    private _remainingNodesToBrowse : Array<UaNodeId>;

    constructor()
    {
        this._objectTypes = new Map;
    
        let baseObjectTypeId = new UaNodeId(ObjectTypeIds.BaseObjectType);
        this._objectTypes.set(
                baseObjectTypeId, 
                new UaObjectType(
                    baseObjectTypeId, 
                    "BaseObjectType", 
                    new UaLocalizedText("BaseObjectType"), 
                    false));
    
        this._remainingNodesToBrowse = [ baseObjectTypeId ];
    }
    
    public async read(client : UaWebClient)
    {
        await this.__browseObjectTypes(client);
    }

    public getObjectType(nodeId: UaNodeId) : UaObjectType | null
    {
        let objectType = this._objectTypes.get(nodeId);
        return (objectType) ? objectType : null;
    }

    public getObjectTypeIds() : Array<UaNodeId>
    {
        return [...this._objectTypes.keys()];
    }

    private async __browseObjectTypes(client : UaWebClient)
    {
        if (0 == this._remainingNodesToBrowse.length) return;

        let nodeIds = this._remainingNodesToBrowse.splice(0,15);

        let nodesToBrowse: Array<BrowseDescription> = [];

        for (let item of nodeIds)
        {
            nodesToBrowse.push(
                {
                    NodeId: item.toString(),
                    BrowseDirection: BrowseDirection.Forward,
                    ReferenceTypeId: new UaNodeId(ReferenceTypeIds.HasSubtype).toString(),
                    IncludeSubtypes: false,
                    NodeClassMask: NodeClass.ObjectType,
                    ResultMask: 31
                }
            );
        }

        let results = await client.browse(nodesToBrowse);

        for (let i=0; i<nodeIds.length; ++i)
        {
            let statusCode = UaPayloadMapper.statusCodeFromWebApi(results[i].StatusCode);
            if (statusCode.isNotGood() || !results[i].References) continue;

            let parentType = this._objectTypes.get(nodeIds[i]);

            for (let item of results[i].References)
            {
                let objectTypeId = parseUaNodeIdOrNull(item.NodeId);
                if (!objectTypeId || NodeClass.ObjectType != item.NodeClass ||
                    !item.BrowseName || !item.DisplayName) continue;

                let objectType = new UaObjectType(
                    objectTypeId, 
                    item.BrowseName, 
                    UaPayloadMapper.localizedTextFromWebApi(item.DisplayName),
                    false);
                
                this._objectTypes.set(objectTypeId, objectType);
                this._remainingNodesToBrowse.push(objectTypeId);
                if (parentType) objectType.setParentType(parentType);
            }
        }

        if (this._remainingNodesToBrowse.length != 0)
        {
            await this.__delay(20);
            await this.__browseObjectTypes(client);
        }
    }

    private async __delay(ms: number): Promise<void> {
        return new Promise(resolve => setTimeout(resolve, ms));
    }    
}