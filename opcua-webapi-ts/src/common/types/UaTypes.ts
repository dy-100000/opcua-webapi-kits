import { UaArgument } from "../structure";
import { UaLocalizedText } from "./UaLocalizedText";

export type UaMethodArguments = {
    inputArguments : Array<UaArgument>;
    outputArguments : Array<UaArgument>;
}

export type UaApplicationDescriptor = {
    urls : Array<string>;
    applicationUri : string;
    applicationName : UaLocalizedText;
    productUri : string;
}