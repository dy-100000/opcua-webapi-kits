import { UaDataTypeDictionary, UaNodeId, UaObject, UaObjectBrowser, UaObjectReader, UaObjectSerializer, UaObjectTypeDictionary, UaReference, UaReferenceTypeDictionary, UaWebClient } from "../src";

export class TestExport {
    private _client : UaWebClient;
    private _dataTypeDictionary : UaDataTypeDictionary;
    private _objectTypeDictionary : UaObjectTypeDictionary;
    private _referenceTypeDictionary : UaReferenceTypeDictionary;
    
    constructor(
        client : UaWebClient,
        dataTypeDictionary : UaDataTypeDictionary,
        objectTypeDictionary : UaObjectTypeDictionary,
        referenceTypeDictionary : UaReferenceTypeDictionary)
    {
        this._client = client;
        this._dataTypeDictionary = dataTypeDictionary;
        this._objectTypeDictionary = objectTypeDictionary;
        this._referenceTypeDictionary = referenceTypeDictionary;
    }
    
    async exportObjects(nodeId : UaNodeId) : Promise<any>
    {        
        let serializer = new UaObjectSerializer(this._objectTypeDictionary, this._dataTypeDictionary);
     
        let reader = new UaObjectReader(this._client);
        let results = await reader.read([nodeId]);
        if (results.length == 0) return [];

        let objects : Array<any> = [];
        let references : Array<any> = [];
                
        let rootObject = results[0] as UaObject;
        objects.push(serializer.toJson(rootObject));       

        await this._exportObjects([nodeId], serializer,objects, references);
        return { objects: objects, references: references };
    }

    private async _exportObjects(
        objectIdsToBrowse : Array<UaNodeId>,
        serializer : UaObjectSerializer,
        objects : Array<any>,
        references : Array<any>) : Promise<void>
    {
        if (objectIdsToBrowse.length == 0) return;

        let objectBrowser = new UaObjectBrowser(this._client, objectIdsToBrowse);
        await objectBrowser.browse();

        let results = objectBrowser.results();
        let objectReferences : Array<UaReference> = [];

        for (let result of results) {
            let from = result.nodeId.toString();           
            
            for (let reference of result.references) {
                let referenceType = this._referenceTypeDictionary.getReferenceType(reference.referenceTypeId);
                if (!referenceType) continue;   
                
                objectReferences.push(reference);
                references.push({ from, to: reference.nodeId.toString(), ref: referenceType.browseName });                
            }
        }

        let objectReader = new UaObjectReader(this._client);
        let objectResults = await objectReader.readByReferences(objectReferences);
        let remainingObjectIds : Array<UaNodeId> = [];
        for (let item of objectResults) {
            remainingObjectIds.push(item.nodeId);
            objects.push(serializer.toJson(item as UaObject));
        }

        await this._exportObjects(remainingObjectIds, serializer, objects, references);
    }
}