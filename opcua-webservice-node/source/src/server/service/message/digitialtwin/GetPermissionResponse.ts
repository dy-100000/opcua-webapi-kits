export class GetPermissionResponse {
    private readonly _deletePermission: boolean;
    private readonly _renamePermission: boolean;
    private readonly _modifyDescriptorPermission: boolean;

    constructor(
        deletePermission: boolean,
        renamePermission: boolean,
        modifyDescriptorPermission: boolean = false) {
        this._renamePermission = renamePermission;
        this._deletePermission = deletePermission;
        this._modifyDescriptorPermission = modifyDescriptorPermission;
    }

    get deletePermission(): boolean {
        return this._deletePermission;
    }

    get renamePermission(): boolean {
        return this._renamePermission;
    }

    get modifyDescriptorPermission(): boolean {
        return this._modifyDescriptorPermission;
    }    
}