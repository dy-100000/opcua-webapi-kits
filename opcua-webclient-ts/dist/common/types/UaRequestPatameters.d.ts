export declare class UaClientParameters {
    private _timeout?;
    private _returnDiagnostics?;
    private _auditEntryId?;
    get timeout(): number | undefined;
    set timeout(timeout: number);
    get returnDiagnostics(): number | undefined;
    set returnDiagnostics(returnDiagnostics: number);
    get auditEntryId(): string | undefined;
    set auditEntryId(auditEntryId: string);
}
