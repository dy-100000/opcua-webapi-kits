import { BrowseDescription, BrowseDirection, NodeClass } from "opcua-webapi";
import { parseUaNodeIdOrNull, UaLocalizedText, UaNodeId, UaPayloadMapper, ReferenceTypeIds, ObjectTypeIds, UaObjectType, UaBrowseDescription } from "../../common"
import { UaWebClient } from "../UaWebClient"

export class UaObjectTypeDictionary
{
    private _objectTypes : Map<string, UaObjectType>;
    private _remainingNodesToBrowse : Array<UaNodeId>;

    constructor()
    {
        this._objectTypes = new Map;
    
        let baseObjectTypeId = new UaNodeId(ObjectTypeIds.BaseObjectType);
        this._objectTypes.set(
                baseObjectTypeId.toString(), 
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
        let objectType = this._objectTypes.get(nodeId.toString());
        return (objectType) ? objectType : null;
    }

    public getObjectTypes() : Array<UaObjectType>
    {
        return [...this._objectTypes.values()];
    }

    private async __browseObjectTypes(client : UaWebClient)
    {
        if (0 == this._remainingNodesToBrowse.length) return;

        let nodeIds = this._remainingNodesToBrowse.splice(0,15);

        let nodesToBrowse: Array<UaBrowseDescription> = [];

        for (let item of nodeIds)
        {
            let browseDescription = new UaBrowseDescription(
                    item,
                    BrowseDirection.Forward,
                    new UaNodeId(ReferenceTypeIds.HasSubtype),
                    false,
                    NodeClass.ObjectType,
                    63);

            nodesToBrowse.push(browseDescription);
        }

        let results = await client.browse(nodesToBrowse);

        for (let i=0; i<nodeIds.length; ++i)
        {
            if (results[i].statusCode.isNotGood()) continue;

            let parentType = this._objectTypes.get(nodeIds[i].toString());

            for (let item of results[i].references)
            {
                let objectTypeId = item.nodeId.getNodeId();
                if (null == objectTypeId || NodeClass.ObjectType != item.nodeClass ||
                    !item.browseName || !item.displayName) continue;

                let objectType = new UaObjectType(
                    objectTypeId, 
                    item.browseName, 
                    item.displayName,
                    false);
                
                this._objectTypes.set(objectTypeId.toString(), objectType);
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