"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.UaClientParameters = void 0;
class UaClientParameters {
    get timeout() {
        return this._timeout;
    }
    set timeout(timeout) {
        if (timeout < 500) {
            this._timeout = 500;
        }
        else {
            this._timeout = this.timeout;
        }
    }
    get returnDiagnostics() {
        return this._returnDiagnostics;
    }
    set returnDiagnostics(returnDiagnostics) {
        this._returnDiagnostics = returnDiagnostics;
    }
    get auditEntryId() {
        return this._auditEntryId;
    }
    set auditEntryId(auditEntryId) {
        this._auditEntryId = auditEntryId;
    }
}
exports.UaClientParameters = UaClientParameters;
