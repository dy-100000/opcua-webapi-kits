import { NodeClass } from "opcua-webapi";
import { UaLocalizedText, UaNodeId, ObjectTypeIds, UaObjectType, UaReference } from "../../common"
import { UaWebClient } from "../UaWebClient"
import { UaChildBrowser, UaNodeReader } from "./UaNodeReader";

export class UaObjectTypeDictionary
{
    private _objectTypes : Map<string, UaObjectType>;
    private _returnAllAttributes: boolean;
    
    constructor(simpleMode?: boolean)
    {
        this._objectTypes = new Map;
        this._returnAllAttributes = (null == simpleMode) ? false : !simpleMode;
    
        let baseObjectTypeId = UaNodeId.from(ObjectTypeIds.BaseObjectType);
        this._objectTypes.set(
                baseObjectTypeId.toString(), 
                new UaObjectType(
                    baseObjectTypeId, 
                    "BaseObjectType", 
                    new UaLocalizedText("BaseObjectType"), 
                    false)); 
    }
    
    public async read(client : UaWebClient)
    {
        let browser = new UaChildBrowser([UaNodeId.from(ObjectTypeIds.BaseObjectType)]);
        await this._read(browser, client);
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

    private async _read(
        browser: UaChildBrowser,
        client: UaWebClient)
    {
        // Browse child type
        await browser.browse(client)
        let results = browser.results();
        
        let referencesToRead: Array<UaReference> = [];

        for (let item of results)
        {
            for (let reference of item.references) referencesToRead.push(reference);        
        }

        if (0 == referencesToRead.length) return;

        // Read child type
        let nodeIdsToBrowse: Array<UaNodeId> = [];
        let nodeReader = new UaNodeReader(this._returnAllAttributes,false, false,this._returnAllAttributes);
        let nodes = await nodeReader.readByReferences(referencesToRead,client);

        for (let node of nodes)
        {
            if (NodeClass.ObjectType != node.nodeClass) continue;
            nodeIdsToBrowse.push(node.nodeId);
            this._objectTypes.set(node.nodeId.toString(), node as UaObjectType);         
        }

        // Build parent-child relationship
        for (let item of results)
        {
            let parentType = this._objectTypes.get(item.nodeId.toString());

            for (let reference of item.references)
            {
                let objectType = this._objectTypes.get(reference.nodeId.toString());                
                if (objectType && parentType) objectType.setParentType(parentType);
            }            
        }

        if (0 == nodeIdsToBrowse.length) return;
        await this.__delay(20);

        // Continue to browse and read child type
        let childBrowser = new UaChildBrowser(nodeIdsToBrowse);
        await this._read(childBrowser, client);
    }

    private async __delay(ms: number): Promise<void> {
        return new Promise(resolve => setTimeout(resolve, ms));
    }   
}