import { UaTestWebServer } from ".";

void UaTestWebServer.launch().catch((error) => {
    console.error("Failed to launch test server", error);
    process.exit(1);
});