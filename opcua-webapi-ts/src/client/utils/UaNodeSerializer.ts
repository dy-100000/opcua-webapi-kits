import { UaObject, UaObjectType } from "../../common";
import { UaDataTypeDictionary } from "./UaDataTypeDictionary";
import { UaObjectTypeDictionary } from "./UaObjectTypeDictionary";

export class UaObjectSerializer {
    private _objectDictionary : UaObjectTypeDictionary;
    private _dataTypeDictionary : UaDataTypeDictionary;
    
    constructor(
        objectDictionary : UaObjectTypeDictionary,
        dataTypeDictionary : UaDataTypeDictionary)
    {
        this._objectDictionary = objectDictionary;
        this._dataTypeDictionary = dataTypeDictionary;
    }
    
    toJson(object : UaObject) : any
    {
        let variableMembers = object.variableMembers;
        let methodMembers = object.methodMembers;

        let objectType = this._objectDictionary.getObjectType(object.typeDefinitionId);

        let variables = [];
        let properties = [];
        let methods = [];

        for (let variable of variableMembers)
        {
            if (variable.isProperty)
            {
                let propertyJson = {
                    name: variable.browseName,
                    displayName: variable.displayName.text,
                    value: (variable.value) ? variable.value.value : null
                };
                properties.push(propertyJson);
            }
            else
            {
                let dataType = this._dataTypeDictionary.getDataType(variable.dataType);
                if (!dataType) continue;

                let variableJson = {
                    id: variable.nodeId.toString(),
                    name: variable.browseName,
                    displayName: variable.displayName.text,
                    dataType: dataType.browseName,                        
                    accessLevel: variable.accessLevel,
                    historizing: (variable.historizing) ? variable.historizing : undefined,
                    valueRank: (-1 != variable.valueRank) ? variable.valueRank : undefined
                };

                variables.push(variableJson);
            }             
        }

        for (let method of methodMembers) {
            let inputArguments = [];
            let outputArguments = [];

            if (method.inputArguments)
            {
                for (let inputArgument of method.inputArguments)
                {
                    let dataType = this._dataTypeDictionary.getDataType(inputArgument.dataType);
                    if (!dataType) continue;
    
                    let inputArgumentJson = {
                        name: inputArgument.name,
                        dataType: dataType.browseName
                    };
                    inputArguments.push(inputArgumentJson);
                }
            }

            if (method.outputArguments)
            {
                for (let outputArgument of method.outputArguments)
                {
                    let dataType = this._dataTypeDictionary.getDataType(outputArgument.dataType);
                    if (!dataType) continue;

                    let outputArgumentJson = {
                        name: outputArgument.name,
                        dataType: dataType.browseName
                    };
                    outputArguments.push(outputArgumentJson);
                }
            }

            let methodJson = {
                id: method.nodeId.toString(),
                name: method.browseName,
                displayName: method.displayName.text,
                inputArguments: (inputArguments.length > 0) ? inputArguments : undefined,
                outputArguments: (outputArguments.length > 0) ? outputArguments : undefined
            };

            methods.push(methodJson);            
        }

        let ret = {
            id: object.nodeId.toString(),
            name: object.browseName,
            displayName: object.displayName.text,
            description: (object.description) ? object.description.text : undefined,
            objectType: objectType.browseName,
            variables: (variables.length > 0) ? variables : undefined,
            properties: (properties.length > 0) ? properties : undefined,
            methods: (methods.length > 0) ? methods : undefined
        }

        return ret;
    }
}

export class UaObjectTypeSerializer {
    private _dataTypeDictionary : UaDataTypeDictionary;
    
    constructor(dataTypeDictionary : UaDataTypeDictionary)
    {
        this._dataTypeDictionary = dataTypeDictionary;
    }

    toJson(objectType : UaObjectType) : any
    {
        let variableMembers = objectType.variableMembers;
        let methodMembers = objectType.methodMembers;

        let variables = [];
        let properties = [];
        let methods = [];

        for (let variable of variableMembers)
        {
            let dataType = this._dataTypeDictionary.getDataType(variable.dataType);
            if (!dataType) continue;

            let variableJson = {
                id: variable.nodeId.toString(),
                name: variable.browseName,
                displayName: variable.displayName.text,
                dataType: dataType.browseName,                        
                accessLevel: variable.accessLevel,
                historizing: (variable.historizing) ? variable.historizing : undefined,
                valueRank: (-1 != variable.valueRank) ? variable.valueRank : undefined
            };


            if (variable.isProperty)
            {
                properties.push(variableJson);
            } else {                
                variables.push(variableJson);
            }             
        }

        for (let method of methodMembers) {
            let inputArguments = [];
            let outputArguments = [];

            for (let inputArgument of method.inputArguments)
            {
                let dataType = this._dataTypeDictionary.getDataType(inputArgument.dataType);
                if (!dataType) continue;

                let inputArgumentJson = {
                    name: inputArgument.name,
                    dataType: dataType.browseName
                };
                inputArguments.push(inputArgumentJson);
            }

            for (let outputArgument of method.outputArguments)
            {
                let dataType = this._dataTypeDictionary.getDataType(outputArgument.dataType);
                if (!dataType) continue;

                let outputArgumentJson = {
                    name: outputArgument.name,
                    dataType: dataType.browseName
                };
                outputArguments.push(outputArgumentJson);
            }

            let methodJson = {
                id: method.nodeId.toString(),
                name: method.browseName,
                displayName: method.displayName.text,
                inputArguments: (inputArguments.length > 0) ? inputArguments : undefined,
                outputArguments: (outputArguments.length > 0) ? outputArguments : undefined
            };

            methods.push(methodJson);            
        }

        let ret = {
            id: objectType.nodeId.toString(),
            name: objectType.browseName,
            displayName: objectType.displayName.text,
            description: (objectType.description) ? objectType.description.text : undefined,
            objectType: objectType.browseName,
            variables: (variables.length > 0) ? variables : undefined,
            properties: (properties.length > 0) ? properties : undefined,
            methods: (methods.length > 0) ? methods : undefined
        }

        return ret;    
    }
}


