import { Configuration, NodeClass } from "opcua-webapi";
import { UaWebClient, UaClientConfiguration, UaNodeId,  UaVariant, UaVariantType, UaExtensionObject, parseUaNodeId, UaQuery, UaQueryFilter, UaQueryFilterType, ObjectIds, ObjectTypeIds, UaReadValueId, UaWriteValue, UaLocalizedText, UaObject } from "../src";
import { UaRange, UaEUInformation,UaArgument } from "../src";
import { UaEnumValueType } from "../src/common/structure/UaEnumValueType";
import { UaDataTypeDictionary, UaLinkBrowser, UaModelling, UaObjectBrowser, UaObjectReader, UaObjectTypeDictionary, UaReferenceTypeDictionary, UaTypeReader } from "../src/client/utils";

class Test {
    private client : UaWebClient;

    constructor()
    {
        let apiConfig : Configuration = new Configuration({
            basePath: "http://localhost:4842"
        });

        let clientConfig = new UaClientConfiguration(apiConfig);
        clientConfig.defaultTimeout = 20000;

        this.client = new UaWebClient(clientConfig);
    }

    async run()
    {
        try
        { 
            await this.testSetDescription();           
            /*
            await this.testFindServer();
            await this.testReadValues();
            await this.testBrowse();
            await this.testReadNodeAttribute();
            await this.testReadVariableAttribute();
            await this.testReadMethodArgument();
            await this.testWriteValues();
            await this.testMethodCall(); 
            await this.testHistoryReadRawData();
            await this.testHistoryReadEvent();
            await this.testGetGeneratedEvent();
            await this.testDataTypeDictionary();    
            await this.testReferenceTypeDictionary();
            await this.testObjectTypeDictionary();
            await this.testReaderNode();
            await this.testChildBrowser();
            await this.testLinkBrowser();
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
        let nodeId = parseUaNodeId("ns=2;b=eyJvaSI6eyJpIjoiRW50cnkiLCJ0IjoibnM9MjtzPVRlc3REaWdpdGFsVHdpbkRpcmVjdG9yeSJ9fQ==");

        let attribute = await this.client.readNodeAttributes(nodeId, false);
        console.log(attribute);
    }

    async testReadVariableAttribute()
    {
        console.log("testReadVariableAttribute");
        
        let nodeId = parseUaNodeId("ns=2;b=eyJvaSI6eyJpIjoiMCIsImlkIjoibnM9MjtzPVN1Ym1vZGVsVGVzdFR5cGUtRWxlbWVudExpc3QifSwiY2kiOnsicCI6IjAifX0=");
        let attribute = await this.client.readVariableAttributes([nodeId]);
        console.log(attribute);
    }

    async testReadValues()
    {
        console.log("testReadValues");

        let nodeId1 = parseUaNodeId("ns=2;b=eyJvaSI6eyJpIjoiMCIsImlkIjoibnM9MjtzPVN1Ym1vZGVsVGVzdFR5cGUtQ29sbGVjdGlvbkEifSwiY2kiOnsicCI6IkJvb2wifX0=");
        let nodeId2 = parseUaNodeId("ns=2;b=eyJvaSI6eyJpIjoiMCIsImlkIjoibnM9MjtzPVN1Ym1vZGVsVGVzdFR5cGUtQ29sbGVjdGlvbkEifSwiY2kiOnsicCI6IkRvdWJsZSJ9fQ==");
        let nodeId3 = parseUaNodeId("ns=2;b=eyJvaSI6eyJpIjoiMCIsImlkIjoibnM9MjtzPVN1Ym1vZGVsVGVzdFR5cGUtQ29sbGVjdGlvbkEifSwiY2kiOnsicCI6IlJhbmdlIn19");

        let nodeIds : Array<UaNodeId> = [
            nodeId1,
            nodeId2,
            nodeId3,
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

    async testWriteValue()
    {
        console.log("testWriteValue");

        let nodeId = parseUaNodeId("ns=2;b=eyJvaSI6eyJ0IjoibnM9MjtzPVN1Ym1vZGVsVGVzdFR5cGUiLCJpIjoiMCIsImlkIjoibnM9MjtzPVRlc3REaWdpdGFsVHdpbi1TdWJtb2RlbCJ9LCJjaSI6eyJwIjoiRG91YmxlIn19");
        let value = UaVariant.double(20);
        //let value = UaVariant.extensionObject(new UaRange(15,50).toExtensionObject())

        await this.client.writeValue(nodeId, value);
    }

    async testWriteValues()
    {
        console.log("testWriteValues");

        let nodeId1 = parseUaNodeId("ns=2;b=eyJvaSI6eyJpIjoiMCIsImlkIjoibnM9MjtzPVN1Ym1vZGVsVGVzdFR5cGUtQ29sbGVjdGlvbkEifSwiY2kiOnsicCI6IkJvb2wifX0=");
        let nodeId2 = parseUaNodeId("ns=2;b=eyJvaSI6eyJpIjoiMCIsImlkIjoibnM9MjtzPVN1Ym1vZGVsVGVzdFR5cGUtQ29sbGVjdGlvbkEifSwiY2kiOnsicCI6IkRvdWJsZSJ9fQ==");
        let nodeId3 = parseUaNodeId("ns=2;b=eyJvaSI6eyJpIjoiMCIsImlkIjoibnM9MjtzPVN1Ym1vZGVsVGVzdFR5cGUtQ29sbGVjdGlvbkEifSwiY2kiOnsicCI6IlJhbmdlIn19");

        let nodesToWrite: Array<UaWriteValue> = [];
        nodesToWrite.push(new UaWriteValue(nodeId1, UaVariant.boolean(true)));
        nodesToWrite.push(new UaWriteValue(nodeId2, UaVariant.double(20)));
        nodesToWrite.push(new UaWriteValue(nodeId3, UaVariant.extensionObject(new UaRange(15,50).toExtensionObject())));

        await this.client.write(nodesToWrite);
    }

    async testMethodCall()
    {
        console.log("testMethodCall");

        let objectId = parseUaNodeId("ns=2;b=eyJvaSI6eyJpIjoiMCIsImlkIjoibnM9MjtzPVRlc3REaWdpdGFsVHdpbi1TdWJtb2RlbCJ9fQ==");
        let methodId = parseUaNodeId("ns=2;b=eyJvaSI6eyJpIjoiMCIsImlkIjoibnM9MjtzPVRlc3REaWdpdGFsVHdpbi1TdWJtb2RlbCJ9LCJjaSI6eyJwIjoiTWV0aG9kIiwibW4iOnRydWV9fQ==");

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
        
        let nodeId = parseUaNodeId("ns=2;b=eyJvaSI6eyJpIjoiVEVfMzAxOUAwMDAwMDAwMC0wMDAwLTAwMDAtMDAwMC0wMDAwMDAzRTJEMzUiLCJpZCI6Im5zPTI7cz1TZW5zb3JUeXBlLVNpZ25hbCJ9LCJjaSI6eyJwIjoiVmFsdWUifX0=");
                
        const startTime = new Date("2026-01-02T00:00:00Z");
        const endTime = new Date("2026-01-02T00:02:00Z");
 
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

    async testHistoryReadProcessed()
    {
        console.log("testHistoryReadProcessed");
        
        let nodeId = parseUaNodeId("ns=2;b=eyJvaSI6eyJpIjoiVEVfMzAxOUAwMDAwMDAwMC0wMDAwLTAwMDAtMDAwMC0wMDAwMDAzRTJEMzUiLCJpZCI6Im5zPTI7cz1TZW5zb3JUeXBlLVNpZ25hbCJ9LCJjaSI6eyJwIjoiVmFsdWUifX0=");
                
        const startTime = new Date("2026-01-01T13:00:00Z");
        const endTime = new Date("2026-01-01T13:05:00Z");

        let historyData = await this.client.historyReadProcessed(
            nodeId,
            startTime,
            endTime,
            10000);
        
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

        let nodeId = parseUaNodeId("ns=2;b=eyJvaSI6eyJpIjoiMSIsImlkIjoibnM9MjtzPUVtcGxveWVlRGF0YVN1Ym1vZGVsVHlwZS1BdHRlbmRhbmNlIn19");
        let startTime = new Date("2026-01-05T07:00:00");
        let endTime = new Date("2026-06-07T07:00:00");

        let filters : Array<UaQueryFilter> = [
            new UaQueryFilter("CheckIn", UaQueryFilterType.Equals, UaVariant.boolean(true))
        ];

        let select = ["EventId","EventType","Time","Message"];
        let where : UaQuery = new UaQuery(filters);

        let historyData = await this.client.historyReadEvent(
            nodeId,
            startTime,
            endTime,
            select,
            null,
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

        let dataTypeDictionary = new UaDataTypeDictionary(this.client);
        await dataTypeDictionary.read();
        
        let dataTypes = dataTypeDictionary.getDataTypes();       
        
        for (let item of dataTypes)
        {
            console.dir(item.toJson(), { depth: null });
            console.log("Parent: " + item.parentType()?.browseName); 
        }
    }

    async testReferenceTypeDictionary()
    {
        console.log("testReferenceTypeDictionary");

        let referenceTypeDictionary = new UaReferenceTypeDictionary(this.client);
        await referenceTypeDictionary.read();
        
        let referenceTypes = referenceTypeDictionary.getReferenceTypes();       
        
        for (let item of referenceTypes)
        {
            console.dir(item.toJson(), { depth: null });
            console.log("Parent: " + item.parentType()?.browseName); 
        }
    }

    async testObjectTypeDictionary()
    {
        console.log("testObjectTypeDictionary");

        let objectTypeDictionary = new UaObjectTypeDictionary(this.client);
        await objectTypeDictionary.read();
        
        let objectTypes = objectTypeDictionary.getObjectTypes();       
        
        for (let item of objectTypes)
        {
            console.dir(item.toJson(), { depth: null });
            console.log("Parent: " + item.parentType()?.browseName);        
        }
    }

    async testObjectReader()
    {
        let nodeIds : Array<UaNodeId> = [
            parseUaNodeId("ns=2;b=eyJvaSI6eyJpIjoiMCIsImlkIjoibnM9MjtzPVRlc3REaWdpdGFsVHdpbi1TdWJtb2RlbCJ9fQ=="),
            parseUaNodeId("ns=2;b=eyJvaSI6eyJpIjoiMCIsImlkIjoibnM9MjtzPVN1Ym1vZGVsVGVzdFR5cGUtQ29sbGVjdGlvbkEifX0=")
        ];

        let reader = new UaObjectReader(this.client,false,true);
        let nodes = await reader.read(nodeIds);

        for (let item of nodes)
        {
            console.dir(item.toJson(), { depth: null });
        }
    }

    async testTypeReader()
    {
        let nodeIds : Array<UaNodeId> = [
            parseUaNodeId("ns=2;s=EnumTest")
        ];

        let reader = new UaTypeReader(this.client);
        let nodes = await reader.read(nodeIds);

        for (let item of nodes)
        {
            console.dir(item.toJson(), { depth: null });
        }
    }

    async testObjectBrowser()
    {
        let nodeIds : Array<UaNodeId> = [
            parseUaNodeId("ns=2;b=eyJvaSI6eyJpIjoiMCIsImlkIjoibnM9MjtzPVRlc3REaWdpdGFsVHdpbi1TdWJtb2RlbFRlc3RUeXBlIn19"),
            parseUaNodeId("ns=2;b=eyJvaSI6eyJpIjoiMCIsImlkIjoibnM9MjtzPVN1Ym1vZGVsVGVzdFR5cGUtQ29sbGVjdGlvbkIifX0="),
        ];

        let reader = new UaObjectBrowser(this.client,nodeIds, true);
        await reader.browse();

        let references = reader.results();
        for (let item of references)
        {
            console.log("--- " + item.nodeId.toString() + " ---");

            for (let item2 of item.references)
            {
                console.dir(item2.toJson(), { depth: null });
            }
        }
    }

    async testLinkBrowser()
    {
        let nodeIds : Array<UaNodeId> = [
            parseUaNodeId("ns=2;b=eyJvaSI6eyJpIjoiMDAwMDAwMDAtMDAwMC0wMDAwLTAwMDAtMDAwMDAwM0UyQTdEIiwiaWQiOiJucz0yO3M9RGV2aWNlU3VibW9kZWxUeXBlLVNlbnNvcnMifX0="),
            parseUaNodeId("ns=2;b=eyJvaSI6eyJpIjoiMDAwMDAwMDAtMDAwMC0wMDAwLTAwMDAtMDAwMDAwM0UyQTdEIiwiaWQiOiJucz0yO3M9RGV2aWNlU3VibW9kZWxUeXBlLUVxdWlwbWVudHMifX0=")
        ];

        let reader = new UaLinkBrowser(this.client,nodeIds,true);
        await reader.browse();

        let references = reader.results();
        for (let item of references)
        {
            console.log("--- " + item.nodeId.toString() + " ---");

            for (let item2 of item.references)
            {
                console.dir(item2.toJson(), { depth: null });
            }
        }
    }

    async testGetObjectTypeCanAdd()
    {
        console.log("testGetObjectTypeCanAdd");

        let objectTypeDictionary = new UaObjectTypeDictionary(this.client);
        await objectTypeDictionary.read();

        let modelling = new UaModelling(this.client);
        let objectTypeId = parseUaNodeId("ns=2;s=TestDigitalTwinDirectory");

        let objectTypeCanAdd = await modelling.getObjectTypeToAdd(objectTypeId,objectTypeDictionary);
        console.log("Object types that can be added:");
        for (let item of objectTypeCanAdd) {
            console.dir(item.toJson(), { depth: null });
        }
    }

    async testAddObject()
    {
        console.log("testAddObject");
        let parentNodeId = parseUaNodeId("ns=2;b=eyJvaSI6eyJpIjoiMCIsImlkIjoibnM9MjtzPVRlc3REaWdpdGFsVHdpbi1FbGVtZW50TGlzdFN1Ym1vZGVsIn19");
        let objectTypeId = parseUaNodeId("ns=2;s=ElementCollectionTestAType");
        let name = UaLocalizedText.from("TestElement");

        let modelling = new UaModelling(this.client);
        let newNodeId = await modelling.addObject(parentNodeId, objectTypeId, name);
        console.log("New object added with NodeId: " + newNodeId.toString());
    }

    async testDeleteObject()
    {
        console.log("testDeleteObject");
        let objectId = parseUaNodeId("ns=2;b=eyJvaSI6eyJpIjoiMCIsInQiOiJucz0yO3M9RWxlbWVudENvbGxlY3Rpb25UZXN0QVR5cGUifX0=");

        let modelling = new UaModelling(this.client);
        await modelling.deleteNode(objectId);
        console.log("Object deleted");
    }

    async testGetWriteMask()
    {
        console.log("testGetWriteMask");
        
        let nodeIdToDelete = parseUaNodeId("ns=2;b=eyJvaSI6eyJpIjoiMCIsInQiOiJucz0yO3M9RWxlbWVudENvbGxlY3Rpb25UZXN0QVR5cGUifX0=");
        let objectToDelete = new UaObject(nodeIdToDelete, "Test", UaLocalizedText.from("Test"), 0, parseUaNodeId("ns=2;s=TestDigitalTwin"));
        
        let modelling = new UaModelling(this.client);
        await modelling.getWriteMask([objectToDelete]);
        console.log("Write masks:" + objectToDelete.writeMask);
    }

    async testRenameObject()
    {
        console.log("testRenameObject");
        let objectId = parseUaNodeId("ns=2;b=eyJvaSI6eyJpIjoiMSIsInQiOiJucz0yO3M9RWxlbWVudENvbGxlY3Rpb25UZXN0QVR5cGUifX0=");
        let name = UaLocalizedText.from("TestElement");
        let modelling = new UaModelling(this.client);
        await modelling.rename(objectId, name);
        console.log("Object renamed");
    }

    async testSetDescription()
    {
        console.log("testSetDescription");
        let objectId = parseUaNodeId("ns=2;b=eyJvaSI6eyJpIjoiMSIsInQiOiJucz0yO3M9RWxlbWVudENvbGxlY3Rpb25UZXN0QVR5cGUifX0=");
        let description = UaLocalizedText.from("Test element description");
        let modelling = new UaModelling(this.client);
        await modelling.setDescription(objectId, description);
        console.log("Description set");
    }
}

let test = new Test;
test.run();