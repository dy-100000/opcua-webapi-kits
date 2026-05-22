import { ApplicationDescription } from "opcua-webapi";
import { UaPayloadMapper } from "opcua-webapi-ts";
import { UaApplicationDescriptor, UaServerConfigure } from "./server/types";

export abstract class UaDiscoveryService {
    abstract find(url: string, applicationUrisToFind: Array<string>): Promise<Array<UaApplicationDescriptor>>;

    static findComplete(
        descriptors: Array<UaApplicationDescriptor>,
        configure: UaServerConfigure,
    ): Array<ApplicationDescription> {
        const descriptions: Array<ApplicationDescription> = [];

        for (const item of descriptors) {
            descriptions.push({
                ApplicationUri: item.uri,
                ProductUri: configure.productUri,
                ApplicationName: UaPayloadMapper.localizedTextToWebApi(item.name),
                ApplicationType: configure.applicationType,
                DiscoveryUrls: [item.url],
            });
        }

        return descriptions;
    }
}
