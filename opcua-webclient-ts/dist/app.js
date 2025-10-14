"use strict";
var __awaiter = (this && this.__awaiter) || function (thisArg, _arguments, P, generator) {
    function adopt(value) { return value instanceof P ? value : new P(function (resolve) { resolve(value); }); }
    return new (P || (P = Promise))(function (resolve, reject) {
        function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }
        function rejected(value) { try { step(generator["throw"](value)); } catch (e) { reject(e); } }
        function step(result) { result.done ? resolve(result.value) : adopt(result.value).then(fulfilled, rejected); }
        step((generator = generator.apply(thisArg, _arguments || [])).next());
    });
};
Object.defineProperty(exports, "__esModule", { value: true });
const opcua_webapi_1 = require("opcua-webapi");
const _1 = require(".");
const node_opcua_nodeid_1 = require("node-opcua-nodeid");
class Test {
    constructor() {
        let apiConfig = new opcua_webapi_1.Configuration({
            basePath: "http://127.0.0.1:4840/Server-1"
        });
        let clientConfig = new _1.UaClientConfiguration(apiConfig);
        this.client = new _1.OpcUaWebClient(clientConfig);
    }
    run() {
        return __awaiter(this, void 0, void 0, function* () {
            try {
                //            await this.testBrowse();
                //            await this.testReadNodeAttribute();
                //            await this.testReadVariableAttribute();
                yield this.testReadValues();
            }
            catch (e) {
                console.log(e.name);
            }
        });
    }
    testBrowse() {
        return __awaiter(this, void 0, void 0, function* () {
            let nodeId = new node_opcua_nodeid_1.NodeId(node_opcua_nodeid_1.NodeIdType.STRING, "BuildingAutomation", 4);
            let nodeClassToReturn = Number(opcua_webapi_1.NodeClass.Object | opcua_webapi_1.NodeClass.Variable | opcua_webapi_1.NodeClass.Method);
            console.log("browseChild");
            let children = yield this.client.browseChild(nodeId, nodeClassToReturn, 3);
            for (let item of children.results) {
                console.log(item.displayName.toString());
            }
            if (children.continuationPoint) {
                console.log("browseNextChild");
                children = yield this.client.browseNextChild(children.continuationPoint);
                for (let item of children.results) {
                    console.log(item.displayName.toString());
                }
            }
        });
    }
    testReadNodeAttribute() {
        return __awaiter(this, void 0, void 0, function* () {
            let nodeId = new node_opcua_nodeid_1.NodeId(node_opcua_nodeid_1.NodeIdType.STRING, "Demo.History.Historian_1", 3);
            console.log("readNodeAttribute");
            let attribute = yield this.client.readNodeAttributes(nodeId, true);
            console.log(attribute);
        });
    }
    testReadVariableAttribute() {
        return __awaiter(this, void 0, void 0, function* () {
            let nodeId = new node_opcua_nodeid_1.NodeId(node_opcua_nodeid_1.NodeIdType.STRING, "Demo.History.Historian_1", 3);
            console.log("readVariableAttribute");
            let attribute = yield this.client.readVariableAttributes(nodeId);
            console.log(attribute);
        });
    }
    testReadValues() {
        return __awaiter(this, void 0, void 0, function* () {
            let nodeIds = [
                new node_opcua_nodeid_1.NodeId(node_opcua_nodeid_1.NodeIdType.STRING, "AirConditioner_1.Humidity.EURange", 4),
            ];
            console.log("readValues");
            let values = yield this.client.readValues(nodeIds);
            for (let item of values) {
                if (item.statusCode.isGood()) {
                    console.log(item.value.value);
                }
                else {
                    console.log(item.statusCode.toString());
                }
            }
        });
    }
}
let test = new Test;
test.run();
