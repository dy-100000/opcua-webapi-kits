export enum UaEventNotifier {
    SubscribeToEvents = 1,
    HistoryRead = 4,
    HistoryWrite = 8
}

export function isEventNotifierAccessible(eventNotifier: number, requiredEventNotifier: UaEventNotifier): boolean {
    return (eventNotifier & requiredEventNotifier) === requiredEventNotifier;
}