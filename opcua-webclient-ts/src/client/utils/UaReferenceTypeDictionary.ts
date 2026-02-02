import { BrowseDescription, BrowseDirection, NodeClass } from "opcua-webapi";
import { parseUaNodeIdOrNull, UaReferenceType, UaLocalizedText, UaNodeId, UaPayloadMapper, ReferenceTypeIds } from "../../common"
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
                    new UaLocalizedText(),
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

        let nodesToBrowse: Array<BrowseDescription> = [];

        for (let item of nodeIds)
        {
            nodesToBrowse.push(
                {
                    NodeId: item.toString(),
                    BrowseDirection: BrowseDirection.Forward,
                    ReferenceTypeId: new UaNodeId(ReferenceTypeIds.HasSubtype).toString(),
                    IncludeSubtypes: false,
                    NodeClassMask: NodeClass.ReferenceType,
                    ResultMask: 31
                }
            );
        }

        let results = await client.browse(nodesToBrowse);

        for (let i=0; i<nodeIds.length; ++i)
        {
            let statusCode = UaPayloadMapper.statusCodeFromWebApi(results[i].StatusCode);
            if (statusCode.isNotGood() || !results[i].References) continue;

            let parentType = this._referenceTypes.get(nodeIds[i].toString());

            for (let item of results[i].References)
            {
                let referenceTypeId = parseUaNodeIdOrNull(item.NodeId);
                if (!referenceTypeId || NodeClass.ReferenceType != item.NodeClass ||
                    !item.BrowseName || !item.DisplayName) continue;

                let referenceType = new UaReferenceType(
                    referenceTypeId, 
                    item.BrowseName, 
                    UaPayloadMapper.localizedTextFromWebApi(item.DisplayName),
                    false,
                    new UaLocalizedText(),
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