export enum UaAccessLevel {
    None = 0,
    CurrentRead = 1,
    CurrentWrite = 2,
    HistoryRead = 4,
    HistoryWrite = 8,
    SemanticChange = 16,
    StatusWrite = 32,
    TimestampWrite = 64
}

export function isAccessible(accessLevel: number, requiredAccessLevel: UaAccessLevel): boolean {
    return (accessLevel & requiredAccessLevel) === requiredAccessLevel;
}