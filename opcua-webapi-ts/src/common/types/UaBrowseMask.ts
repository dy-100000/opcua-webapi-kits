export enum UaBrowseMask {
    None = 0,
    ReferenceTypeId = 1,
    IsForward = 2,
    NodeClass = 4,
    BrowseName = 8,
    DisplayName = 16,
    TypeDefinition = 32,
    All = 63
}

export function isBrowseMaskRequired(mask: number, requiredMask: UaBrowseMask): boolean {
    return (mask & requiredMask) === requiredMask;
}