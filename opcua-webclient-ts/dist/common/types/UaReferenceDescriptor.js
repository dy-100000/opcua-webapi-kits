"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.UaReferenceDescriptor = void 0;
class UaReferenceDescriptor {
    toString() {
        let ret = "nodeId: " + this.nodeId.toString();
        ret += " nodeClass: " + this.nodeClass;
        ret += " browseName: " + this.browseName;
        ret += " displayName: " + this.displayName.toString();
        ret += " referenceTypeId: " + this.referenceTypeId.toString();
        ret += " isForward: " + this.isForward;
        if (this.typeDefinition)
            ret += " typeDefinition: " + this.typeDefinition.toString();
        return ret;
    }
}
exports.UaReferenceDescriptor = UaReferenceDescriptor;
