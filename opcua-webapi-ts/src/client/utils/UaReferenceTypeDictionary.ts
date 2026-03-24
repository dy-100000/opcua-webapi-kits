import { BrowseDirection, NodeClass } from "opcua-webapi";
import { UaReferenceType, UaLocalizedText, UaNodeId, ReferenceTypeIds, UaBrowseDescription } from "../../common"
import { UaWebClient } from "../UaWebClient"

export class UaReferenceTypeDictionary
{
    private _referenceTypes : Map<string, UaReferenceType>;
    private _remainingNodesToBrowse : Array<UaNodeId>;

    constructor()     
    {
        this._referenceTypes = new Map;
    
        let referencesId = new UaNodeId(ReferenceTypeIds.References);
        this._referenceTypes.set(
                referencesId.toString(), 
                new UaReferenceType(
                    referencesId, 
                    "References", 
                    new UaLocalizedText("References"), 
                    true,
                    UaLocalizedText.nullText,
                    true 
                ));
    
        this._remainingNodesToBrowse = [ referencesId ];
    }
    
    public async read(client : UaWebClient)
    {
        await this.__browseReferenceTypes(client);
    }

    public getReferenceType(nodeId: UaNodeId) : UaReferenceType | null
    {
        let referenceType = this._referenceTypes.get(nodeId.toString());
        return (referenceType) ? referenceType : null;
    }

    public getReferenceTypes() : Array<UaReferenceType>
    {
        return [...this._referenceTypes.values()];
    }

    private async __browseReferenceTypes(client : UaWebClient)
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
                    NodeClass.ReferenceType,
                    31);

            nodesToBrowse.push(browseDescription);
        }

        let results = await client.browse(nodesToBrowse);

        for (let i=0; i<nodeIds.length; ++i)
        {
            if (results[i].statusCode.isNotGood()) continue;

            let parentType = this._referenceTypes.get(nodeIds[i].toString());

            for (let item of results[i].references)
            {
                let referenceTypeId = item.nodeId.getNodeId();
                if (null == referenceTypeId || NodeClass.ReferenceType != item.nodeClass ||
                    !item.browseName || !item.displayName) continue;

                let referenceType = new UaReferenceType(
                    referenceTypeId, 
                    item.browseName, 
                    item.displayName,
                    false,
                    UaLocalizedText.nullText,
                    false);              
                
                this._referenceTypes.set(referenceTypeId.toString(), referenceType);
                this._remainingNodesToBrowse.push(referenceTypeId);
                if (parentType) referenceType.setParentType(parentType);
            }
        }

        if (this._remainingNodesToBrowse.length != 0)
        {
            await this.__delay(20);
            await this.__browseReferenceTypes(client);
        }
    }

    private async __delay(ms: number): Promise<void> {
        return new Promise(resolve => setTimeout(resolve, ms));
    }    
}