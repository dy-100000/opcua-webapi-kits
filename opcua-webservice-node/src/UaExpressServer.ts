import express, { Express, NextFunction, Request, Response } from 'express';
import cors from 'cors';
import * as OpenApiValidator from 'express-openapi-validator';
import path from 'path';
import { getEndpoints, read } from './controllers/DefaultController';

export class UaExpressServer {
    private _app : Express;
    private _port: number;
    private _apiSpecPath: string;

    constructor(port: number)
    {
        this._app = express();
        this._port = port;
        this._apiSpecPath = path.join(__dirname, 'api', 'openapi.yaml');
    }

    get app() : Express
    {
        return this._app;
    }

    public start(): void {
        this.initializeMiddleware();
        this.initializeRouters();
        this.initializeErrorHandlers();

        this.app.listen(this._port, () => {
        console.log(`Server running on port ${this._port}`);
        });
    }

    protected initializeMiddleware()
    {
        this._app.use(cors());
        this._app.use(express.json());
        this._app.use(express.urlencoded({ extended: false }));

        this._app.use(
              OpenApiValidator.middleware({
                apiSpec: this._apiSpecPath,
                validateRequests:true,
                validateResponses:false,
                unknownFormats: ["UaNodeId", "UaExpandedNodeId","UaQualifiedName","NodeClass","BrowseDirection","MonitoringMode","StructureType",
                  "FilterOperator","ApplicationType","MessageSecurityMode","UserTokenType","TimestampsToReturn","rfc3066"]
              }),
        );
    }

    protected initializeRouters()
    {
        this._app.post("/getEndpoints", getEndpoints);
        this._app.post(["/read","/:path/read"], read);
    }

    private initializeErrorHandlers()
    {        
        this._app.use((err: any, req: Request, res: Response, next: NextFunction) => {
            res.status(err.status || 500).json({
                message: err.message,
                errors: err.errors || []
            });
        });
    }
}