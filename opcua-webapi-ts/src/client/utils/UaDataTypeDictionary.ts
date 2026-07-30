import { NodeClass } from "opcua-webapi";
import { DataTypeIds, UaDataType, UaLocalizedText, UaNodeId, UaReference } from "../../common"
import { UaWebClient } from "../UaWebClient"
import { UaTypeBrowser, UaNodeReader } from "../..";

export class UaDataTypeDictionary
{
    private _client : UaWebClient;
    private _dataTypes : Map<string, UaDataType>;

    constructor(client : UaWebClient)
    {
        this._client = client;
        this._dataTypes = new Map;

        let baseDataTypeId = new UaNodeId(DataTypeIds.BaseDataType);
        this._dataTypes.set(
            baseDataTypeId.toString(), 
            new UaDataType(baseDataTypeId, "BaseDataType", new UaLocalizedText("BaseDataType"), true));
    }

    public async read()
    {
        let browser = new UaTypeBrowser(this._client,[UaNodeId.from(DataTypeIds.BaseDataType)]);
        await this._read(browser);
    }

    public getDataType(nodeId: UaNodeId) : UaDataType | null
    {
        let dataType = this._dataTypes.get(nodeId.toString());
        return (dataType) ? dataType : null;
    }

    public getDataTypes() : Array<UaDataType>
    {
        return [...this._dataTypes.values()];
    }

    private async _read(browser: UaTypeBrowser)
    {
        // Browse child type
        await browser.browse();
        let results = browser.results();
        
        let referencesToRead: Array<UaReference> = [];
        let needToReadEnumValue = false;

        for (let item of results)
        {
            for (let reference of item.references) referencesToRead.push(reference);             
            if (item.nodeId.equal(UaNodeId.from(DataTypeIds.Enumeration))) needToReadEnumValue = true;
        }

        if (0 == referencesToRead.length) return;

        // Read child type
        let nodeIdsToBrowse: Array<UaNodeId> = [];
        let nodeReader = new UaNodeReader(this._client, false, needToReadEnumValue, false, false,true);
        let nodes = await nodeReader.readByReferences(referencesToRead);

        for (let node of nodes)
        {
            if (NodeClass.DataType != node.nodeClass) continue;
            nodeIdsToBrowse.push(node.nodeId);
            this._dataTypes.set(node.nodeId.toString(), node as UaDataType);         
        }

        // Build parent-child relationship
        for (let item of results)
        {
            let parentType = this._dataTypes.get(item.nodeId.toString());

            for (let reference of item.references)
            {
                let dataType = this._dataTypes.get(reference.nodeId.toString());                
                if (dataType && parentType) dataType.setParentType(parentType);
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