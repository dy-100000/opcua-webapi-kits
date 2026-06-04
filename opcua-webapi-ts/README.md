## opcua-webapi-ts

This TypeScript/JavaScript client is based on opcua-webapi-typescript(the offical release from OPC foundation), opcua-webapi-ts is an wrapper to easier use OPC UA WebApi, including basical type and web client. 

This module can be used in the following environments:

Environment
* Node.js
* Webpack
* Browserify

Language level
* ES6

Module system
* CommonJS
* ES6 module system

### Building

To build and compile the typescript sources to javascript use:
```
npm install
npm run build
```

### Publishing

Build the package before publishing:

```bash
npm install
npm run build
```

Then publish it to npm:

```bash
npm publish
```

### Consuming

```bash
npm install opcua-webapi-ts
```

CommonJS:

```js
const { UaWebClient } = require("opcua-webapi-ts")
```

TypeScript:

```ts
import { UaWebClient } from "opcua-webapi-ts"
```
