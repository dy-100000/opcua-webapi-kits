import {
        UaAccessLevel,
        UaExtensionObject,
        UaEnumValueType,
        UaLocalizedText,
        UaNodeId,
        UaValueRank,
        UaVariant,
} from "opcua-webapi-ts";
import { NodeManager } from "../nodemanager";
import { UaDataTypes, UaVariableTypes } from "./builtin";
import { UaDataType } from "./UaDataType";
import { UaVariable } from "./UaVariable";

export class UaEnumDataType extends UaDataType {
        private static readonly EnumStringsBrowseName = "EnumStrings";
        private static readonly EnumValuesBrowseName = "EnumValues";

        constructor(
                dataTypeId: string,
                displayName: UaLocalizedText,
                enumStrings: Array<string>,
                nodeManager: NodeManager
        );
        constructor(
                dataTypeId: string,
                displayName: UaLocalizedText,
                enumValues: Array<UaEnumValueType>,
                nodeManager: NodeManager
        );
        constructor(
                dataTypeId: string,
                displayName: UaLocalizedText,
                enumMembers: Array<string> | Array<UaEnumValueType>,
                nodeManager: NodeManager
        ) {
                super(
                        new UaNodeId(dataTypeId, nodeManager.nsIndex()),
                        dataTypeId,
                        displayName,
                        false,
                );

                this.setParentType(UaDataTypes.Enumeration);

                let enumVariable: UaVariable;
                if (this.isEnumValueTypeArray(enumMembers)) {
                        enumVariable = this.setEnumValues(
                                new UaNodeId(`${dataTypeId}-${UaEnumDataType.EnumValuesBrowseName}`, nodeManager.nsIndex()),
                                enumMembers as Array<UaEnumValueType>,
                        );
                } else {
                        enumVariable = this.setEnumStrings(
                                new UaNodeId(`${dataTypeId}-${UaEnumDataType.EnumStringsBrowseName}`, nodeManager.nsIndex()),
                                enumMembers as Array<string>,
                        );
                }

                nodeManager.addNode(this);
                nodeManager.addNode(enumVariable);
        }

        parse(value: unknown): number | null {
                return null;
        }

        private isEnumValueTypeArray(values: Array<string> | Array<UaEnumValueType>): boolean {
                return values.length > 0 && typeof values[0] !== "string";
        }

        private setEnumStrings(nodeId: UaNodeId, enumStrings: Array<string>): UaVariable {
                const variable = this.createPropertyVariable(
                        nodeId,
                        UaEnumDataType.EnumStringsBrowseName,
                        UaDataTypes.LocalizedText.nodeId,
                );

                const localizedEnumStrings: Array<UaLocalizedText> = [];
                for (const enumString of enumStrings) {
                        localizedEnumStrings.push(new UaLocalizedText(enumString));
                }

                variable.value = UaVariant.localizedTexts(localizedEnumStrings);
                this.addMemberNode(variable);

                return variable;
        }

        private setEnumValues(nodeId: UaNodeId, enumValues: Array<UaEnumValueType>): UaVariable {
                const variable = this.createPropertyVariable(
                        nodeId,
                        UaEnumDataType.EnumValuesBrowseName,
                        UaDataTypes.EnumValueType.nodeId,
                );

                const extensionObjects: Array<UaExtensionObject> = [];
                for (const enumValue of enumValues) {
                        extensionObjects.push(enumValue.toExtensionObject());
                }

                variable.value = UaVariant.extensionObjects(extensionObjects);
                this.addMemberNode(variable);

                return variable;
        }

        private createPropertyVariable(nodeId: UaNodeId, browseName: string, dataType: UaNodeId): UaVariable {
                return new UaVariable(
                        nodeId,
                        browseName,
                        new UaLocalizedText(browseName),
                        dataType,
                        UaValueRank.OneDimension,
                        UaAccessLevel.CurrentRead,
                        UaVariableTypes.PropertyType,
                );
        }
}