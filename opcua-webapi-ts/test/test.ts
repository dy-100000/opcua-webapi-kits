import { Configuration, NodeClass, StatusCodes } from "opcua-webapi";
import { UaWebClient, UaClientConfiguration, UaNodeId,  UaNodeIdType, UaVariant, UaVariantType, UaExtensionObject, makeUaStatusCode, DataTypeIds, UaWriteValue, UaExpandedNodeId, UaLocalizedText, parseUaNodeId, UaQuery, UaQueryFilter, UaQueryFilterType, ObjectIds, ObjectTypeIds } from "../src";
import { UaRange, UaEUInformation,UaArgument } from "../src";
import { UaEnumValueType } from "../src/common/structure/UaEnumValueType";
import { UaDataTypeDictionary, UaNodeChildReader, UaNodeLinkReader, UaNodeReader, UaObjectTypeDictionary, UaReferenceTypeDictionary } from "../src/client/utils";

class Test {
    private client : UaWebClient;

    constructor()
    {
        let apiConfig : Configuration = new Configuration({
            basePath: "http://zangmobai:4840"
        });

        let clientConfig = new UaClientConfiguration(apiConfig);
        clientConfig.defaultTimeout = 20000;

        this.client = new UaWebClient(clientConfig);
    }

    async run()
    {
        try
        {
            await this.testHistoryReadRawData();    
            /*
            await this.testFindServer();
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
            await this.testReaderNode();
            await this.testNodeChildReader();   
            await this.testNodeLinkReader();   
            await this.testFindServer();      
            */
            
        } catch (e) {            
            console.log(e);
        }
    }

    async testBrowse()
    {
        console.log("testBrowse");

        let nodeId = parseUaNodeId("ns=2;b=eyJvaSI6eyJpIjoiUHJvY2Vzc1NlZ21lbnRzIiwidCI6Im5zPTI7cz1Qcm9jZXNzU2VnbWVudFJlcG9zaXRvcnlUeXBlIn19");
        let nodeClassToReturn = Number(NodeClass.Object | NodeClass.Variable | NodeClass.Method | NodeClass.ObjectType | NodeClass.VariableType | NodeClass.ReferenceType | NodeClass.DataType);

        console.log("browseChild");
        let children = await this.client.browseChild(nodeId, nodeClassToReturn, 3);

        for (let item of children.results)
        {
            console.log(item.displayName?.toString());
        }       
 
        if (children.continuationPoint)
        {
            console.log("browseNextChild");

            children = await this.client.browseNextByCP(children.continuationPoint);
            for (let item of children.results)
            {
                console.log(item.displayName?.toString());
            }
        }
    }

    async testReadNodeAttribute()
    {
        console.log("testReadNodeAttribute");
        let nodeId = parseUaNodeId("ns=2;b=eyJvaSI6eyJ0IjoibnM9MjtzPVRlc3REaWdpdGFsVHdpbiIsImkiOiIwIn19");

        let attribute = await this.client.readNodeAttributes(nodeId, true);
        console.log(attribute);
    }

    async testReadVariableAttribute()
    {
        console.log("testReadVariableAttribute");
        
        let nodeId = parseUaNodeId("ns=2;b=eyJvaSI6eyJ0IjoibnM9MjtzPVN1Ym1vZGVsVGVzdFR5cGUiLCJpIjoiMCIsImlkIjoibnM9MjtzPVRlc3REaWdpdGFsVHdpbi1TdWJtb2RlbCJ9LCJjaSI6eyJwIjoiRG91YmxlIn19");
        let attribute = await this.client.readVariableAttributes([nodeId]);
        console.log(attribute);
    }
    
    async testReadObjectAttribute()
    {
        console.log("testReadObjectAttribute");

        let nodeId = parseUaNodeId("ns=2;b=eyJvaSI6eyJ0IjoibnM9MjtzPUVsZW1lbnRDb2xsZWN0aW9uVGVzdEFUeXBlIiwiaSI6IjAiLCJpZCI6Im5zPTI7cz1TdWJtb2RlbFRlc3RUeXBlLUNvbGxlY3Rpb25BIn19");
        let attribute = await this.client.readObjectAttributes(nodeId);
        console.log(attribute);
    }

    async testReadMethodArgument()
    {
        console.log("testReadMethodArgument");

        let nodeId = parseUaNodeId("ns=2;b=eyJvaSI6eyJ0IjoibnM9MjtzPVN1Ym1vZGVsVGVzdFR5cGUiLCJpIjoiMCIsImlkIjoibnM9MjtzPVRlc3REaWdpdGFsVHdpbi1TdWJtb2RlbCJ9LCJjaSI6eyJwIjoiTWV0aG9kIiwibW4iOnRydWV9fQ==");
        let methodArgs = await this.client.readMethodArguments(nodeId);
        
        for (let item of methodArgs.inputArguments)
        {
            console.log("InputArgument:");
            console.log(item);
        }
        
        for (let item of methodArgs.outputArguments)
        {
            console.log("OutputArgument:");
            console.log(item);
        }
    }

    async testReadValues()
    {
        console.log("testReadValues");

        let nodeId1 = parseUaNodeId("ns=2;b=eyJvaSI6eyJpIjoiMCIsImlkIjoibnM9MjtzPVN1Ym1vZGVsVGVzdFR5cGUtQ29sbGVjdGlvbkEifSwiY2kiOnsicCI6IkJvb2wifX0=");
        let nodeId2 = parseUaNodeId("ns=2;b=eyJvaSI6eyJ0IjoibnM9MjtzPVBlcnNvbmFsRGF0YVN1Ym1vZGVsVHlwZSIsImkiOiIyIiwiaWQiOiJucz0yO3M9RW1wbG95ZWVEaWdpdGFsVHdpblR5cGUtUGVyc29uYWxEYXRhIn0sImNpIjp7InAiOiJBZGRyZXNzIn19");
        let nodeId3 = parseUaNodeId("ns=2;b=eyJvaSI6eyJ0IjoibnM9MjtzPUVtcGxveWVlRGF0YVN1Ym1vZGVsVHlwZSIsImkiOiIyIiwiaWQiOiJucz0yO3M9RW1wbG95ZWVEaWdpdGFsVHdpblR5cGUtRW1wbG95ZWVEYXRhIn0sImNpIjp7InAiOiJTYWxhcnkiLCJwMiI6IkVuZ2luZWVyaW5nVW5pdHMifX0=");
      
        let nodeIds : Array<UaNodeId> = [
            nodeId1,
            nodeId2,
            nodeId3
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

        let nodeId = parseUaNodeId("ns=2;b=eyJvaSI6eyJ0IjoibnM9MjtzPVN1Ym1vZGVsVGVzdFR5cGUiLCJpIjoiMCIsImlkIjoibnM9MjtzPVRlc3REaWdpdGFsVHdpbi1TdWJtb2RlbCJ9LCJjaSI6eyJwIjoiRG91YmxlIn19");
        let value = UaVariant.double(20);
        //let value = UaVariant.extensionObject(new UaRange(15,50).toExtensionObject())

        await this.client.writeValue(nodeId, value);
    }

    async testMethodCall()
    {
        console.log("testMethodCall");

        let objectId = parseUaNodeId("ns=2;b=eyJvaSI6eyJ0IjoibnM9MjtzPVN1Ym1vZGVsVGVzdFR5cGUiLCJpIjoiMCIsImlkIjoibnM9MjtzPVRlc3REaWdpdGFsVHdpbi1TdWJtb2RlbCJ9fQ==");
        let methodId = parseUaNodeId("ns=2;b=eyJvaSI6eyJ0IjoibnM9MjtzPVN1Ym1vZGVsVGVzdFR5cGUiLCJpIjoiMCIsImlkIjoibnM9MjtzPVRlc3REaWdpdGFsVHdpbi1TdWJtb2RlbCJ9LCJjaSI6eyJwIjoiTWV0aG9kIiwibW4iOnRydWV9fQ==");

        let input = UaVariant.string("Hello");

        let inputArguments : Array<UaVariant> = [input];
        let outputArguments = await this.client.methodCall(objectId, methodId, inputArguments);

        for (let item of outputArguments)
        {
            console.log(item.value);
        }
    }   

    async testFindServer()
    {
        console.log("testFindServer"); 
        let results = await this.client.find();
        console.log(results);
    }

    async testHistoryReadRawData()
    {
        console.log("testHistoryReadRawData");
        
        let nodeId = parseUaNodeId("ns=2;b=eyJvaSI6eyJpIjoiRklGU0NBSUAwMDAwMDAwMC0wMDAwLTAwMDAtMDAwMC0wMDAwMDAzRTI5NUYiLCJpZCI6Im5zPTI7cz1TZW5zb3JUeXBlLVNpZ25hbCJ9LCJjaSI6eyJwIjoiVmFsdWUifX0=");
                
        const startTime = new Date("2026-01-01T13:00:00Z");
        const endTime = new Date("2026-01-01T13:02:00Z");

        let historyData = await this.client.historyReadRawData(
            nodeId,
            startTime,
            endTime,
            1000,
            null,            
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

    async testHistoryReadAtTime()
    {
        console.log("testHistoryReadAtTime");
        
        let nodeId = parseUaNodeId("ns=2;b=eyJvaSI6eyJpIjoiMCIsImlkIjoibnM9MjtzPVRlc3REaWdpdGFsVHdpbi1TdWJtb2RlbCJ9LCJjaSI6eyJwIjoiRG91YmxlIn19");
        let startTime = new Date(Date.now());
        let time1 = new Date(startTime.getTime() + 2 * 60 * 1000);
        let time2 = new Date(time1.getTime() + 2 * 60 * 1000);

        let historyData = await this.client.historyReadAtTime(
            nodeId,
            [startTime, time1, time2],
            true,
            null);
        
        for (let item of historyData.historyData)
        {
            console.log(item.value.value);
        }

        if (historyData.continuationPoint)
        {
            console.log("cp:" + historyData.continuationPoint);
        }
    }

    async testHistoryReadEvent()
    {
        console.log("testHistoryReadEvent");
        
        StatusCodes

        let nodeId = parseUaNodeId("ns=2;b=eyJvaSI6eyJ0IjoibnM9MjtzPUVtcGxveWVlQXR0ZW5kYW5jZUV2ZW50VHlwZSIsImkiOiIxIiwiaWQiOiJucz0yO3M9RW1wbG95ZWVEYXRhU3VibW9kZWxUeXBlLUF0dGVuZGFuY2UifX0=");
        let startTime = new Date("2026-01-05T07:00:00");
        let endTime = new Date("2026-01-07T07:00:00");

        let filters : Array<UaQueryFilter> = [
            new UaQueryFilter("CheckIn", UaQueryFilterType.Equals, UaVariant.boolean(true))
        ];

        let select = ["EventId","EventType","Time","Message",];
        let where : UaQuery = new UaQuery(filters);

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
            let data = item.getEventData(select);

            console.log("-------------------------");
            for (let item2 of data)
            {
                console.log(item2[0] + ": " + item2[1].value)
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

        let objectTypeId = parseUaNodeId("ns=2;s=EmployeeAttendanceEventType");
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
        
        let dataTypes = dataTypeDictionary.getDataTypes();       
        
        for (let item of dataTypes)
        {
            console.log(`Id: ${item.nodeId.toString()} Name: ${item.browseName} Abstract: ${item.isAbstract} ValueType: ${item.valueType}`)
            
            if (DataTypeIds.Enumeration == item.valueType && item.enumValues)
            {
                for (let itemL2 of item.enumValues)
                {
                    console.log(`Name: ${itemL2[1]} Value: ${itemL2[0]}`);
                }
            }            
        }
    }

    async testReferenceTypeDictionary()
    {
        console.log("testReferenceTypeDictionary");

        let referenceTypeDictionary = new UaReferenceTypeDictionary();
        await referenceTypeDictionary.read(this.client);
        
        let referenceTypes = referenceTypeDictionary.getReferenceTypes();       
        
        for (let item of referenceTypes)
        {
            console.log(`Id: ${item.nodeId.toString()} Name: ${item.browseName} Abstract: ${item.isAbstract}`)
        }
    }

    async testObjectTypeDictionary()
    {
        console.log("testObjectTypeDictionary");

        let objectTypeDictionary = new UaObjectTypeDictionary();
        await objectTypeDictionary.read(this.client);
        
        let objectTypes = objectTypeDictionary.getObjectTypes();       
        
        for (let item of objectTypes)
        {
            console.log(`Id: ${item.nodeId.toString()} Name: ${item.browseName}`)            
        }
    }

    async testReaderNode()
    {
        let nodeIds : Array<UaNodeId> = [
            parseUaNodeId("ns=2;b=eyJvaSI6eyJ0IjoibnM9MjtzPUVtcGxveWVlRGF0YVN1Ym1vZGVsVHlwZSIsImkiOiIxIiwiaWQiOiJucz0yO3M9RW1wbG95ZWVEaWdpdGFsVHdpblR5cGUtRW1wbG95ZWVEYXRhIn19")
        ];

        let reader = new UaNodeReader(nodeIds);
        await reader.readAll(this.client);

        let nodes = reader.getResults();
        for (let item of nodes)
        {
            console.dir(item.toJson(), { depth: null });
        }
    }

    async testNodeChildReader()
    {
        let nodeIds : Array<UaNodeId> = [
            parseUaNodeId("ns=2;b=eyJvaSI6eyJ0IjoibnM9MjtzPUVtcGxveWVlRGlnaXRhbFR3aW5UeXBlIiwiaSI6IjEifX0=")
        ];

        let reader = new UaNodeChildReader(nodeIds);
        await reader.readAll(this.client);

        let childNodes = reader.getResults();
        for (let item of childNodes)
        {
            console.log("--- " + item.nodeId.toString() + " ---");

            for (let item2 of item.children)
            {
                console.dir(item2.toJson(), { depth: null });
            }
        }
    }

    async testNodeLinkReader()
    {
        let nodeIds : Array<UaNodeId> = [
            parseUaNodeId("ns=2;b=eyJvaSI6eyJpIjoiMCIsImlkIjoibnM9MjtzPVN1Ym1vZGVsVGVzdFR5cGUtUmVmZXJlbmNlIn19")
        ];

        let reader = new UaNodeLinkReader(nodeIds);
        await reader.readAll(this.client);

        let links = reader.getResults();
        for (let item of links)
        {
            console.log("--- " + item.nodeId.toString() + " ---");

            for (let item2 of item.links)
            {
                console.log({
                    target: item2.targetId.toString(),
                    reference: item2.referenceTypeId.toString(),
                    isForward: item2.isForward,
                    browseName: item2.browseName,
                    displayName: item2.displayName.toString(),
                    nodeClass: item2.nodeClass
                });
            }
        }
    }
}

let test = new Test;
test.run();