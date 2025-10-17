import { ModellingServiceApi } from "./modellingserviceapi";
import * as ServiceTypes from "./modellingapitypes";

export class TypeManager
{
    public constructor()
    {
        this.dataTypes = new Map;
        this.objectTypeConfigures = new Map;
    }

    public getDataType(dataTypeId : string) : ServiceTypes.UaDataType | undefined
    {
        return this.dataTypes.get(dataTypeId);
    }

    public async loadDataType(api : ModellingServiceApi)
    {
        await this.__loadDataType(api, 0);
    }

    public getObjectTypeConfigure(objectTypeId : string): ServiceTypes.UaObjectTypeConfigure|undefined
    {
        return this.objectTypeConfigures.get(objectTypeId);
    }

    public async loadObjectTypeConfigures(api : ModellingServiceApi, objectTypes : string[])
    {
        let objectTypesToFind : Set<string> = new Set;

        for (let item of objectTypes)
        {
            if (this.objectTypeConfigures.get(item)) continue;

            objectTypesToFind.add(item);
        }

        if (0 == objectTypesToFind.size) return;

        let nodeIds : string[] = [];
        objectTypesToFind.forEach(item => {
            nodeIds.push(item);
        });

        let results = await api.getObjectTypeConfigures({ nodeIds: nodeIds });
        if (!results || 0 == results.length ) return;
        for (let item of results)
        {   
          item.nodeId ? this.objectTypeConfigures.set(item.nodeId, item):"";
        }
        return results;
    }

    public clear()
    {
        this.dataTypes.clear();
        this.objectTypeConfigures.clear();
    }

    private async __loadDataType(api : ModellingServiceApi, page : number)
    {
        let limit = 100;
        let results = await api.getDataTypes({page: page, limit: limit});

        for (let item of results)
        {
            this.dataTypes.set(item.nodeId, item);
        }

        if (results.length < limit) return;

        await this.__loadDataType(api, page+1);
    }

    private dataTypes : Map<string, ServiceTypes.UaDataType>;
    private objectTypeConfigures : Map<string, ServiceTypes.UaObjectTypeConfigure>;
}

export let typeManager = new TypeManager;
