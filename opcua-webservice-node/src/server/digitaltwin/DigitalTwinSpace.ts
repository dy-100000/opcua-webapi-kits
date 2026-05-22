import { UaLocalizedText, UaNodeId } from "opcua-webapi-ts";
import { NodeManagerReactiveObject } from "../addressspace/nodemanager/NodeManagerReactiveObject";
import { UaObject } from "../addressspace/nodes/UaObject";
import { UaObjects } from "../addressspace/nodes/builtin/UaObjects";
import { UaInstanceIdentifier, UaObjectIdentifier } from "../types";
import type { DigitalTwinRepositoryType } from "./digitaltwin/DigitalTwinRepositoryType";

export class DigitalTwinSpace extends NodeManagerReactiveObject {
    constructor(namespaceUri: string) {
        super(namespaceUri);
    }

    addRepository(
        repositoryType: DigitalTwinRepositoryType,
        id: string,
        displayName: UaLocalizedText,
        description: UaLocalizedText,
    ): void {
        const objectIdentifier = new UaInstanceIdentifier(
            new UaObjectIdentifier(repositoryType.nodeId.toString(), id, null),
            null,
        );

        const objectNodeId = new UaNodeId(objectIdentifier.toByteString(), this.nsIndex());
        const newObject = new UaObject(objectNodeId, id, displayName, repositoryType);
        newObject.description = description;

        this.addNode(newObject);
        UaObjects.ObjectsFolder.organizes(newObject);
    }
}
