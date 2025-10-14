"use strict";
var __createBinding = (this && this.__createBinding) || (Object.create ? (function(o, m, k, k2) {
    if (k2 === undefined) k2 = k;
    var desc = Object.getOwnPropertyDescriptor(m, k);
    if (!desc || ("get" in desc ? !m.__esModule : desc.writable || desc.configurable)) {
      desc = { enumerable: true, get: function() { return m[k]; } };
    }
    Object.defineProperty(o, k2, desc);
}) : (function(o, m, k, k2) {
    if (k2 === undefined) k2 = k;
    o[k2] = m[k];
}));
var __exportStar = (this && this.__exportStar) || function(m, exports) {
    for (var p in m) if (p !== "default" && !Object.prototype.hasOwnProperty.call(exports, p)) __createBinding(exports, m, p);
};
Object.defineProperty(exports, "__esModule", { value: true });
exports.ua_assert = ua_assert;
__exportStar(require("./UaError"), exports);
__exportStar(require("./UaStatusCode"), exports);
__exportStar(require("./UaGuid"), exports);
__exportStar(require("./UaLocalizedText"), exports);
__exportStar(require("./UaNodeId"), exports);
__exportStar(require("./UaVariant"), exports);
__exportStar(require("./UaClientConfigure"), exports);
__exportStar(require("./UaRequestPatameters"), exports);
__exportStar(require("./UaReferenceDescriptor"), exports);
__exportStar(require("./UaBrowseResult"), exports);
__exportStar(require("./UaNodeAttributes"), exports);
function ua_assert(cond, message) {
    if (!cond) {
        const err = new Error(message);
        throw err;
    }
}
exports.default = ua_assert;
