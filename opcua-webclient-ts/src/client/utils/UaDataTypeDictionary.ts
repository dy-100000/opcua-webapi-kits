import { BrowseDescription, BrowseDirection, NodeClass, ReferenceTypeIds } from "opcua-webapi";
import { DataTypeIds, parseUaNodeIdOrNull, UaDataType, UaExtensionObject, UaLocalizedText, UaNodeId, UaPayloadMapper, UaArrayType, UaVariantType, UaEnumValueType } from "../../common"
import { UaWebClient } from "../UaWebClient"

export class UaDataTypeDictionary
{
    private static _abstractDataTypeIds : Set<number> = null;
    private _dataTypes : Map<UaNodeId, UaDataType>;
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
            baseDataTypeId, 
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
        let dataType = this._dataTypes.get(nodeId);
        return (dataType) ? dataType : null;
    }

    public getDataTypeIds() : Array<UaNodeId>
    {
        return [...this._dataTypes.keys()];
    }

    private async __browseDataTypes(client : UaWebClient)
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
                    ReferenceTypeId: ReferenceTypeIds.HasSubtype,
                    IncludeSubtypes: false,
                    NodeClassMask: NodeClass.DataType,
                    ResultMask: 31
                }
            );
        }

        let results = await client.browse(nodesToBrowse);

        for (let i=0; i<nodeIds.length; ++i)
        {
            let statusCode = UaPayloadMapper.statusCodeFromWebApi(results[i].StatusCode);
            if (statusCode.isNotGood() || !results[i].References) continue;

            let parentType = this._dataTypes.get(nodeIds[i]);

            for (let item of results[i].References)
            {
                let dataTypeId = parseUaNodeIdOrNull(item.NodeId);
                if (!dataTypeId || NodeClass.DataType != item.NodeClass ||
                    !item.BrowseName || !item.DisplayName) continue;

                let isAbstract = false;
                if (dataTypeId.nsIndex == 0 &&
                    UaDataTypeDictionary._abstractDataTypeIds.has(dataTypeId.numericId())) isAbstract = true;

                let dataType = new UaDataType(
                    dataTypeId, 
                    item.BrowseName, 
                    UaPayloadMapper.localizedTextFromWebApi(item.DisplayName),
                    isAbstract);              
                
                this._dataTypes.set(dataTypeId, dataType);
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

        let nodesToBrowse: Array<BrowseDescription> = [];

        for (let item of nodeIds)
        {
            nodesToBrowse.push(
                {
                    NodeId: item.toString(),
                    BrowseDirection: BrowseDirection.Forward,
                    ReferenceTypeId: ReferenceTypeIds.HasProperty,
                    IncludeSubtypes: false,
                    NodeClassMask: NodeClass.Variable,
                    ResultMask: 8
                }
            );
        }

        let results = await client.browse(nodesToBrowse);

        let enumDataTypeIds : Array<UaNodeId> = [];
        let enumValueIds : Array<UaNodeId> = [];

        for (let i=0; i<nodeIds.length; ++i)
        {
            let statusCode = UaPayloadMapper.statusCodeFromWebApi(results[i].StatusCode);            
            if (statusCode.isNotGood() || !results[i].References || results[i].References.length == 0) continue;
            if (results[i].References[0].BrowseName == "EnumStrings" || results[i].References[0].BrowseName == "EnumValues")
            {
                let enumValueId = parseUaNodeIdOrNull(results[i].References[0].NodeId);
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

                let dataType = this._dataTypes.get(enumDataTypeIds[i]);
                if (!dataType) continue;

                let enumValues: Map<number, UaLocalizedText> = null;

                if (UaVariantType.LocalizedText == values[i].value.type)
                {
                    enumValues = new Map();
                    let localizedTexts = values[i].value.value as Array<UaLocalizedText>;

                    for (let j=0; j<localizedTexts.length; ++j)
                    {
                        enumValues.set(j, localizedTexts[j]);
                    }

                    dataType.setEnumValues(enumValues);
                    
                } else if (UaVariantType.ExtensionObject == values[i].value.type) {
                    enumValues = new Map();

                    let extensionObjects = values[i].value.value as Array<UaExtensionObject>;                    
                    for (let item of extensionObjects)
                    {
                        let enumValue = UaEnumValueType.fromExtensionObject(item);
                        if (enumValue) enumValues.set(enumValue.value, enumValue.displayName);
                    }

                    dataType.setEnumValues(enumValues);
                }                
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