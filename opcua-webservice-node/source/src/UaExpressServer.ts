import express, { Express, NextFunction, Request, Response } from 'express';
import cors from 'cors';
import * as OpenApiValidator from 'express-openapi-validator';
import path from 'path';
import fs from 'fs';
import { addNodes, addReferences, browse, browseNext, call, deleteNodes, deleteReferences, findServers, getEndpoints, historyRead, read, write } from './controllers/DefaultController';

export class UaExpressServer {
    private _app : Express;
    private _port: number;
    private _apiSpecPath: string;

    constructor(port: number, apiSpecDir?: string)
    {
        this._app = express();
        this._port = port;
        let specDir = (apiSpecDir) ? apiSpecDir : path.join(__dirname, 'api');
        this._apiSpecPath = path.join(specDir, 'openapi.yaml');
        
        if (!fs.existsSync(this._apiSpecPath)) {
            console.warn("API spec not found at", this._apiSpecPath);
        }
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
        this._app.post("/findservers", findServers);
        this._app.post(["/getEndpoints","/:path/getEndpoints"], getEndpoints);
        this._app.post(["/browse","/:path/browse"], browse);
        this._app.post(["/browsenext","/:path/browsenext"], browseNext);
        this._app.post(["/read","/:path/read"], read);
        this._app.post(["/write","/:path/write"], write);
        this._app.post(["/call","/:path/call"], call);
        this._app.post(["/historyread","/:path/historyread"], historyRead);
        this._app.post(["/addnodes","/:path/addnodes"], addNodes);
        this._app.post(["/addreferences","/:path/addreferences"], addReferences);
        this._app.post(["/deletenodes","/:path/deletenodes"], deleteNodes);
        this._app.post(["/deletereferences","/:path/deletereferences"], deleteReferences);
    }

    private initializeErrorHandlers()
    {        
        this._app.use((err: any, req: Request, res: Response, next: NextFunction) => {
            console.error('Request error:', err && err.stack ? err.stack : err);
            res.status(err.status || 500).json({
                message: err.message,
                errors: err.errors || []
            });
        });
    }
}