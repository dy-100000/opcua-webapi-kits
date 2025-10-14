import { Configuration, NodeClass, StatusCodes } from "opcua-webapi";
import { UaWebClient, UaClientConfiguration, UaNodeId,  UaNodeIdType, UaVariant, UaVariantType, UaExtensionObject, makeUaStatusCode, DataTypeIds, UaWriteValue, UaExpandedNodeId, UaLocalizedText } from ".";
import { UaRange, UaEUInformation,UaArgument } from ".";
import { UaEnumValueType } from "./common/structure/UaEnumValueType";
import { UaDataTypeDictionary } from "./client/utils";

class Test {
    private client : UaWebClient;

    constructor()
    {
        let apiConfig : Configuration = new Configuration({
            basePath: "http://dingyan3:4840/Server-1"
        });

        let clientConfig = new UaClientConfiguration(apiConfig);
        this.client = new UaWebClient(clientConfig);
    }

    async run()
    {
        try
        {            
            await this.testReadValues();             
            await this.testBrowse();
            await this.testReadNodeAttribute();
            await this.testReadVariableAttribute(); 
            await this.testReadMethodArgument();
            await this.testWriteValues(); 
            await this.testMethodCall(); 
            //await this.testDataTypeDictionary();           
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

            children = await this.client.browseNextChild(children.continuationPoint);
            for (let item of children.results)
            {
                console.log(item.displayName.toString());
            }
        }
    }

    async testReadNodeAttribute()
    {
        console.log("testReadNodeAttribute");
        let nodeId = new UaNodeId("Demo.History.Historian_1",3);

        let attribute = await this.client.readNodeAttributes(nodeId, true);
        console.log(attribute);
    }

    async testReadVariableAttribute()
    {
        console.log("testReadVariableAttribute");
        
        let nodeId = new UaNodeId("Demo.History.Historian_1",3);

        console.log("readVariableAttribute");
        let attribute = await this.client.readVariableAttributes(nodeId);
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

        let nodeIds : Array<UaNodeId> = [
            new UaNodeId(11878)
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
                        if (UaRange.dataTypeId.equal(extensionObject.dataTypeId))
                        {
                            let range = UaRange.fromExtensionObject(extensionObject);
                            console.log(range);
                        } else if (UaEUInformation.dataTypeId.equal(extensionObject.dataTypeId)) {
                            let euInformation = UaEUInformation.fromExtensionObject(extensionObject);
                            console.log(euInformation);
                        }
                    } else if (item.value.isArray()) {
                        let extensionObjects = item.value.value as Array<UaExtensionObject>;
                        for (let item of extensionObjects)
                        {
                            if (UaArgument.dataTypeId.equal(item.dataTypeId))
                            {
                                let argument = UaArgument.fromExtensionObject(item);
                                console.log(argument);
                            }

                            if (UaEnumValueType.dataTypeId.equal(item.dataTypeId))
                            {
                                let enumValueType = UaEnumValueType.fromExtensionObject(item);
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

        let value = UaVariant.statusCodes([makeUaStatusCode(), makeUaStatusCode(StatusCodes.Bad)]);

        let nodesToWrite : Array<UaWriteValue> = [
            { 
                nodeId: new UaNodeId("Demo.Static.Arrays.StatusCode",3),
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
}

let test = new Test;
test.run();