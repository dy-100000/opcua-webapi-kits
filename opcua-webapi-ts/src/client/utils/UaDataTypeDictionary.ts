import { BrowseDescription, BrowseDirection, NodeClass } from "opcua-webapi";
import { DataTypeIds, parseUaNodeIdOrNull, UaDataType, UaExtensionObject, UaLocalizedText, UaNodeId, UaPayloadMapper, UaArrayType, UaVariantType, UaEnumValueType, ReferenceTypeIds, UaBrowseDescription } from "../../common"
import { UaWebClient } from "../UaWebClient"

export class UaDataTypeDictionary
{
    private static _abstractDataTypeIds : Set<number> = null;
    private _dataTypes : Map<string, UaDataType>;
    private _remainingNodesToBrowse : Array<UaNodeId>;
    private _remainingEnumerationToRead : Array<UaNodeId>;

    constructor()
    {
        if (null == UaDataTypeDictionary._abstractDataTypeIds)
        {
            UaDataTypeDictionary._abstractDataTypeIds = new Set;
            UaDataTypeDictionary._abstractDataTypeIds.add(DataTypeIds.BaseDataType);
            UaDataTypeDictionary._abstractDataTypeIds.add(DataTypeIds.Number);
            UaDataTypeDictionary._abstractDataTypeIds.add(DataTypeIds.Structure);
            UaDataTypeDictionary._abstractDataTypeIds.add(DataTypeIds.Integer);
            UaDataTypeDictionary._abstractDataTypeIds.add(DataTypeIds.UInteger);
            UaDataTypeDictionary._abstractDataTypeIds.add(DataTypeIds.Enumeration);
        }        

        this._dataTypes = new Map;

        let baseDataTypeId = new UaNodeId(DataTypeIds.BaseDataType);
        this._dataTypes.set(
            baseDataTypeId.toString(), 
            new UaDataType(baseDataTypeId, "BaseDataType", new UaLocalizedText("BaseDataType"), true));

        this._remainingNodesToBrowse = [ new UaNodeId(DataTypeIds.BaseDataType) ];
        this._remainingEnumerationToRead = [];
    }

    public async read(client : UaWebClient)
    {
        await this.__browseDataTypes(client);
        await this.__readEnumValues(client);
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

    private async __browseDataTypes(client : UaWebClient)
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
                    NodeClass.DataType,
                    31);

            nodesToBrowse.push(browseDescription);
        }

        let results = await client.browse(nodesToBrowse);

        for (let i=0; i<nodeIds.length; ++i)
        {
            if (results[i].statusCode.isNotGood()) continue;

            let parentType = this._dataTypes.get(nodeIds[i].toString());

            for (let item of results[i].references)
            {
                let dataTypeId = item.nodeId.getNodeId();
                if (null == dataTypeId ||
                    NodeClass.DataType != item.nodeClass ||
                    !item.browseName || !item.displayName) continue;

                let isAbstract = false;
                if (dataTypeId.nsIndex == 0 &&
                    UaDataTypeDictionary._abstractDataTypeIds.has(dataTypeId.numericId())) isAbstract = true;

                let dataType = new UaDataType(
                    dataTypeId, 
                    item.browseName, 
                    item.displayName,
                    isAbstract);              
                
                this._dataTypes.set(dataTypeId.toString(), dataType);
                this._remainingNodesToBrowse.push(dataTypeId);
                if (parentType) dataType.setParentType(parentType);
                
                dataType.classify();
                if (DataTypeIds.Enumeration == dataType.valueType)
                {
                    this._remainingEnumerationToRead.push(dataType.nodeId);
                }
            }
        }

        if (this._remainingNodesToBrowse.length != 0)
        {
            await this.__delay(20);
            await this.__browseDataTypes(client);
        }
    }

    private async __readEnumValues(client : UaWebClient)
    {
        if (this._remainingEnumerationToRead.length == 0) return;

        let nodeIds = this._remainingEnumerationToRead.splice(0, 20);

        let nodesToBrowse: Array<UaBrowseDescription> = [];

        for (let item of nodeIds)
        {
            let browseDescription = new UaBrowseDescription(
                    item,
                    BrowseDirection.Forward,
                    new UaNodeId(ReferenceTypeIds.HasProperty),
                    false,
                    NodeClass.Variable,
                    8);

            nodesToBrowse.push(browseDescription);
        }

        let results = await client.browse(nodesToBrowse);

        let enumDataTypeIds : Array<UaNodeId> = [];
        let enumValueIds : Array<UaNodeId> = [];

        for (let i=0; i<nodeIds.length; ++i)
        {                
            if (results[i].statusCode.isNotGood() ||
                results[i].references.length == 0) continue;

            if (results[i].references[0].browseName == "EnumStrings" || results[i].references[0].browseName == "EnumValues")
            {
                let enumValueId = results[i].references[0].nodeId.getNodeId();
                if (enumValueId)
                {
                    enumDataTypeIds.push(nodeIds[i]);
                    enumValueIds.push(enumValueId);
                }
            }
        }
        
        if (enumDataTypeIds.length!= 0)
        {
            let values = await client.readValues(enumValueIds);
            for (let i=0; i<enumValueIds.length; ++i)
            {                
                if (values[i].statusCode.isNotGood() || 
                    values[i].value.arrayType != UaArrayType.Array) continue;

                let dataType = this._dataTypes.get(enumDataTypeIds[i].toString());
                if (!dataType) continue;               

                dataType.setEnumValues(values[i].value);            
            }
        }

        if (this._remainingEnumerationToRead.length != 0)
        {
            await this.__delay(20);
            await this.__readEnumValues(client);
        }        
    }

    private async __delay(ms: number): Promise<void> {
        return new Promise(resolve => setTimeout(resolve, ms));
    }
}