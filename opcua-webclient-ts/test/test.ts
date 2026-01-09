import { Configuration, NodeClass, StatusCodes } from "opcua-webapi";
import { UaWebClient, UaClientConfiguration, UaNodeId,  UaNodeIdType, UaVariant, UaVariantType, UaExtensionObject, makeUaStatusCode, DataTypeIds, UaWriteValue, UaExpandedNodeId, UaLocalizedText, parseUaNodeId, UaQuery, UaQueryFilter, UaQueryFilterType } from "../src";
import { UaRange, UaEUInformation,UaArgument } from "../src";
import { UaEnumValueType } from "../src/common/structure/UaEnumValueType";
import { UaDataTypeDictionary, UaObjectTypeDictionary, UaReferenceTypeDictionary } from "../src/client/utils";

class Test {
    private client : UaWebClient;

    constructor()
    {
        let apiConfig : Configuration = new Configuration({
            basePath: "http://localhost:4842"
        });

        let clientConfig = new UaClientConfiguration(apiConfig);
        this.client = new UaWebClient(clientConfig);
    }

    async run()
    {
        try
        {                    
            /*    
            await this.testReadValues();             
            await this.testBrowse();
            await this.testReadNodeAttribute();
            await this.testReadVariableAttribute(); 
            await this.testReadMethodArgument();
            await this.testReadObjectAttribute();
            await this.testWriteValues(); 
            await this.testMethodCall(); 
            await this.testHistoryReadRawData();
            await this.testHistroryReadEvent();
            await this.testGetGeneratedEvent();
            await this.testDataTypeDictionary();             
            await this.testReferenceTypeDictionary();
            await this.testObjectTypeDictionary();            
            */

            await this.testHistroryReadEvent();
            
        } catch (e) {            
            console.log(e);
        }
    }

    async testBrowse()
    {
        console.log("testBrowse");

        let nodeId = new UaNodeId("BuildingAutomation",4,UaNodeIdType.STRING);
        let nodeClassToReturn = Number(NodeClass.Object | NodeClass.Variable | NodeClass.Method);

        console.log("browseChild");
        let children = await this.client.browseChild(nodeId, nodeClassToReturn, 3);

        for (let item of children.results)
        {
            console.log(item.displayName.toString());
        }       
 
        if (children.continuationPoint)
        {
            console.log("browseNextChild");

            children = await this.client.browseNextByCP(children.continuationPoint);
            for (let item of children.results)
            {
                console.log(item.displayName.toString());
            }
        }
    }

    async testReadNodeAttribute()
    {
        console.log("testReadNodeAttribute");
        let nodeId = parseUaNodeId("ns=2;b=eyJvaSI6eyJ0IjoibnM9MjtzPUR5bmFtaWNTdWJtb2RlbFRlc3RUeXBlIiwiaSI6IjAiLCJpZCI6Im5zPTI7cz1UZXN0RGlnaXRhbFR3aW4tRWxlbWVudExpc3RTdWJtb2RlbCJ9fQ==");

        let attribute = await this.client.readNodeAttributes(nodeId, true);
        console.log(attribute);
    }

    async testReadVariableAttribute()
    {
        console.log("testReadVariableAttribute");
        
        let nodeId = new UaNodeId("Demo.History.Historian_1",3);
        let attribute = await this.client.readVariableAttributes([nodeId]);
        console.log(attribute);
    }
    
    async testReadObjectAttribute()
    {
        console.log("testReadObjectAttribute");

        let nodeId = parseUaNodeId("ns=2;b=eyJvaSI6eyJ0IjoibnM9MjtzPUV2ZW50RWxlbWVudFRlc3RUeXBlIiwiaSI6IjAiLCJpZCI6Im5zPTI7cz1TdWJtb2RlbFRlc3RUeXBlLUV2ZW50RWxlbWVudCJ9fQ==");
        let attribute = await this.client.readObjectAttributes(nodeId);
        console.log(attribute);
    }

    async testReadMethodArgument()
    {
        console.log("testReadMethodArgument");

        let nodeId = new UaNodeId("Demo.Method.Multiply",3);
        let methodArgs = await this.client.readMethodArguments(nodeId);
        
        for (let item of methodArgs.inputArguments)
        {
            console.log(item);
        }
        
        for (let item of methodArgs.outputArguments)
        {
            console.log(item);
        }
    }

    async testReadValues()
    {
        console.log("testReadValues");

        let nodeId = parseUaNodeId("ns=2;b=eyJvaSI6eyJ0IjoibnM9MjtzPUVsZW1lbnRDb2xsZWN0aW9uVGVzdEFUeXBlIiwiaSI6IjAiLCJpZCI6Im5zPTI7cz1TdWJtb2RlbFRlc3RUeXBlLUNvbGxlY3Rpb25BIn0sImNpIjp7InAiOiJNZXRob2QiLCJwMiI6IklucHV0QXJndW1lbnRzIiwibW4iOnRydWV9fQ==");
      
        let nodeIds : Array<UaNodeId> = [
            nodeId
        ];

        let values = await this.client.readValues(nodeIds);
        
        for (let item of values)
        {
            if (item.statusCode.isGood())
            {
                if (item.value.type == UaVariantType.ExtensionObject)
                { 
                    if (item.value.isScalar())
                    {
                        let extensionObject = item.value.value as UaExtensionObject;
                        if (UaRange.dataTypeId.equal(extensionObject.typeId))
                        {
                            let range = UaRange.fromExtensionObject(extensionObject);
                            console.log(range);
                        } else if (UaEUInformation.dataTypeId.equal(extensionObject.typeId)) {
                            let euInformation = UaEUInformation.fromExtensionObject(extensionObject);
                            console.log(euInformation);
                        }
                    } else if (item.value.isArray()) {
                        let extensionObjects = item.value.value as Array<UaExtensionObject>;
                       
                        for (let itemL2 of extensionObjects)
                        {
                            if (UaArgument.dataTypeId.equal(itemL2.typeId))
                            {
                                let argument = UaArgument.fromExtensionObject(itemL2);
                                console.log(argument);
                            }

                            if (UaEnumValueType.dataTypeId.equal(itemL2.typeId))
                            {
                                let enumValueType = UaEnumValueType.fromExtensionObject(itemL2);
                                console.log(enumValueType);
                            }
                        }
                    }
                } else {
                    console.log(item.value.value);
                }                    
            } else {
                console.log(item.statusCode.toString());
            }            
        }
    }

    async testWriteValues()
    {
        console.log("testWriteValues");

        let value = UaVariant.extensionObject(new UaRange(15,50).toExtensionObject())

        let nodesToWrite : Array<UaWriteValue> = [
            { 
                nodeId: parseUaNodeId("ns=2;b=eyJvaSI6eyJ0IjoibnM9MjtzPUVsZW1lbnRDb2xsZWN0aW9uVGVzdEFUeXBlIiwiaSI6IjAiLCJpZCI6Im5zPTI7cz1TdWJtb2RlbFRlc3RUeXBlLUNvbGxlY3Rpb25BIn0sImNpIjp7InAiOiJSYW5nZSJ9fQ=="),
                value: value
            }
        ];

        let results = await this.client.writeValues(nodesToWrite);

        for (let item of results)
        {
            console.log(item.value);
        }
    }

    async testMethodCall()
    {
        console.log("testMethodCall");

        let objectId = new UaNodeId("Demo.Method",3);
        let methodId = new UaNodeId("Demo.Method.Multiply",3);

        let inputA = UaVariant.double(15);
        let inputB = UaVariant.double(20);

        let inputArguments : Array<UaVariant> = [inputA, inputB];
        let outputArguments = await this.client.methodCall(objectId, methodId, inputArguments);

        for (let item of outputArguments)
        {
            console.log(item.value);
        }
    }   

    async testHistoryReadRawData()
    {
        console.log("testHistoryReadRawData");
        
        let nodeId = new UaNodeId("Demo.History",3);
        let startTime = new Date(Date.now());
        let endTime = new Date(startTime.getTime() + 2 * 60 * 1000);

        let historyData = await this.client.historyReadRawData(
            nodeId,
            startTime,
            endTime,
            20,
            "abcd",            
            true,
            false);
        
        for (let item of historyData.historyData)
        {
            console.log(item.value.value);
        }

        if (historyData.continuationPoint)
        {
            console.log("cp:" + historyData.continuationPoint);
        }
    }

    async testHistroryReadEvent()
    {
        console.log("testHistoryReadEvent");
        
        let nodeId = parseUaNodeId("ns=2;b=eyJvaSI6eyJ0IjoibnM9MjtzPUV2ZW50RWxlbWVudFRlc3RUeXBlIiwiaSI6IjAiLCJpZCI6Im5zPTI7cz1TdWJtb2RlbFRlc3RUeXBlLUV2ZW50RWxlbWVudCJ9fQ==");
        let startTime = new Date(Date.now());
        let endTime = new Date(startTime.getTime() + 2 * 60 * 1000);

        let filters : Array<UaQueryFilter> = [
            new UaQueryFilter("abc", UaQueryFilterType.Equals, UaVariant.integer(12), true),
            new UaQueryFilter("def", UaQueryFilterType.Like, UaVariant.string("dken"), true,),
            new UaQueryFilter("ghi", UaQueryFilterType.Between, UaVariant.doubles([100.5, 270]))
        ];

        let select = ["EventId","EventType","Time","Message","Customized"];
        let where : UaQuery = new UaQuery(filters, true);

        let historyData = await this.client.historyReadEvent(
            nodeId,
            startTime,
            endTime,
            select,
            where,
            15,
            null,            
            true);
        
        for (let item of historyData.historyEvents)
        {
            console.log("Event");
            for (let item2 of item.eventFields)
            {
                console.log(item2.value);
            }
        }

        if (historyData.continuationPoint)
        {
            console.log("cp:" + historyData.continuationPoint);
        }
    }

    async testGetGeneratedEvent()
    {
        console.log("testGetGeneratedEvent");

        let objectTypeId = new UaNodeId("EventElementTestType",2);
        let eventTypeIds = await this.client.getGeneratedEventType(objectTypeId);

        for (let item of eventTypeIds)
        {
            console.log(item.toString());
        }
    }

    async testDataTypeDictionary()
    {
        console.log("testDataTypeDictionary");

        let dataTypeDictionary = new UaDataTypeDictionary();
        await dataTypeDictionary.read(this.client);
        
        let dataTypes = dataTypeDictionary.getDataTypeIds();       
        
        for (let item of dataTypes)
        {
            let dataType = dataTypeDictionary.getDataType(item);
            if (dataType)
            {
                console.log(`Id: ${dataType.nodeId.toString()} Name: ${dataType.browseName} Abstract: ${dataType.isAbstract} ValueType: ${dataType.valueType}`)
            
                if (DataTypeIds.Enumeration == dataType.valueType && dataType.enumValues)
                {
                    for (let itemL2 of dataType.enumValues)
                    {
                        console.log(`Name: ${itemL2[1]} Value: ${itemL2[0]}`);
                    }
                }
            }
        }
    }

    async testReferenceTypeDictionary()
    {
        console.log("testReferenceTypeDictionary");

        let referenceTypeDictionary = new UaReferenceTypeDictionary();
        await referenceTypeDictionary.read(this.client);
        
        let referenceTypeIds = referenceTypeDictionary.getReferenceTypeIds();       
        
        for (let item of referenceTypeIds)
        {
            let referenceType = referenceTypeDictionary.getReferenceType(item);
            if (referenceType)
            {
                console.log(`Id: ${referenceType.nodeId.toString()} Name: ${referenceType.browseName} Abstract: ${referenceType.isAbstract}`)
            }
        }
    }

    async testObjectTypeDictionary()
    {
        console.log("testObjectTypeDictionary");

        let objectTypeDictionary = new UaObjectTypeDictionary();
        await objectTypeDictionary.read(this.client);
        
        let objectTypeIds = objectTypeDictionary.getObjectTypeIds();       
        
        for (let item of objectTypeIds)
        {
            let objectType = objectTypeDictionary.getObjectType(item);
            if (objectType)
            {
                console.log(`Id: ${objectType.nodeId.toString()} Name: ${objectType.browseName}`)
            }
        }
    }
}

let test = new Test;
test.run();