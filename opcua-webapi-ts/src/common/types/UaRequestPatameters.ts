export class UaClientParameters {
    private _timeout? : number;
    private _returnDiagnostics?: number;
    private _auditEntryId?: string;

    get timeout() : number | undefined
    {
        return this._timeout;
    }

    set timeout(timeout: number)
    {
        if (timeout < 500)
        {
            this._timeout = 500;
        } else {
            this._timeout = this.timeout;
        }
    } 

    get returnDiagnostics(): number | undefined
    {
        return this._returnDiagnostics;
    }

    set returnDiagnostics(returnDiagnostics : number)
    {        
        this._returnDiagnostics = returnDiagnostics;
    }

    get auditEntryId(): string | undefined
    {
        return this._auditEntryId;
    }

    set auditEntryId(auditEntryId : string)
    {        
        this._auditEntryId = auditEntryId;
    }
}