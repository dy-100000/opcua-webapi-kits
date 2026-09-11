import { NodeClass } from "opcua-webapi";
import { UaReferenceType, UaLocalizedText, UaNodeId, ReferenceTypeIds, UaBrowseDescription, UaReference } from "../../common"
import { UaWebClient } from "../UaWebClient"
import { UaNodeReader } from "./UaNodeReader";
import { UaTypeBrowser } from "./UaNodeBrowser";

export class UaReferenceTypeDictionary
{
    private _client : UaWebClient;
    private _referenceTypes : Map<string, UaReferenceType>;

    constructor(client : UaWebClient)
    {
        this._client = client;
        this._referenceTypes = new Map;
    
        let referencesId = UaNodeId.from(ReferenceTypeIds.References);
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
    }
    
    public async read()
    {
        let browser = new UaTypeBrowser(this._client,[UaNodeId.from(ReferenceTypeIds.References)]);
        await this._read(browser);
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

    private async _read(browser: UaTypeBrowser)
    {
        // Browse child type
        await browser.browse();
        let results = browser.results();
        
        let referencesToRead: Array<UaReference> = [];

        for (let item of results)
        {
            for (let reference of item.references) referencesToRead.push(reference);        
        }

        if (0 == referencesToRead.length) return;

        // Read child type
        let nodeIdsToBrowse: Array<UaNodeId> = [];
        let nodeReader = new UaNodeReader(            
            this._client, 
            false,
            false, 
            false, 
            UaNodeId.from(ReferenceTypeIds.Aggregates),
            false,
            false,
            false,
            false,
            false);
        let nodes = await nodeReader.readByReferences(referencesToRead);

        for (let node of nodes)
        {
            if (NodeClass.ReferenceType != node.nodeClass) continue;
            nodeIdsToBrowse.push(node.nodeId);
            this._referenceTypes.set(node.nodeId.toString(), node as UaReferenceType);         
        }

        // Build parent-child relationship
        for (let item of results)
        {
            let parentType = this._referenceTypes.get(item.nodeId.toString());

            for (let reference of item.references)
            {
                let referenceType = this._referenceTypes.get(reference.nodeId.toString());                
                if (referenceType && parentType) referenceType.setParentType(parentType);
            }            
        }

        if (0 == nodeIdsToBrowse.length) return;
        await this.__delay(20);

        // Continue to browse and read child type
        let childBrowser = new UaTypeBrowser(this._client, nodeIdsToBrowse);
        await this._read(childBrowser);
    }

    private async __delay(ms: number): Promise<void> {
        return new Promise(resolve => setTimeout(resolve, ms));
    }    
}