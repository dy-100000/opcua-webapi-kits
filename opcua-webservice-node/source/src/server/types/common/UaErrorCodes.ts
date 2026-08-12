import { StatusCodes } from 'opcua-webapi';
import { UaStatusCode } from 'opcua-webapi-ts';

 export class UaErrorCodes {
    public static readonly BadInvalidArgument = UaStatusCode.from(StatusCodes.BadInvalidArgument);
    public static readonly BadNotImplemented = UaStatusCode.from(StatusCodes.BadNotImplemented);
    public static readonly BadDataUnavailable = UaStatusCode.from(StatusCodes.BadDataUnavailable);
    public static readonly BadNodeIdUnknown = UaStatusCode.from(StatusCodes.BadNodeIdUnknown);
    public static readonly BadNotFound = UaStatusCode.from(StatusCodes.BadNotFound);
    public static readonly BadTimeout = UaStatusCode.from(StatusCodes.BadTimeout);
    public static readonly BadNotReadable = UaStatusCode.from(StatusCodes.BadNotReadable);
    public static readonly BadNotWritable = UaStatusCode.from(StatusCodes.BadNotWritable);
    public static readonly BadNotExecutable = UaStatusCode.from(StatusCodes.BadNotExecutable);
    public static readonly BadUnexpectedError = UaStatusCode.from(StatusCodes.BadUnexpectedError);
}