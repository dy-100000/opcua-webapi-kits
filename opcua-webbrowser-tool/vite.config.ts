import vue from '@vitejs/plugin-vue';
import vueJsx from '@vitejs/plugin-vue-jsx';
import { resolve } from 'path';
import path from 'path';
import { defineConfig } from 'vite';
// import commonjs from 'vite-plugin-commonjs';
import commonjs from '@rollup/plugin-commonjs';
import { nodePolyfills } from 'vite-plugin-node-polyfills';
//直接获取文件的text
import convertCjsConfig from './vite.convert-cjs';
import { nodeResolve } from '@rollup/plugin-node-resolve';
function rawTransform(
  fileRegex: Array<RegExp>,
): {
  name: string;
  transform: (src: string, id: string) => string | void;
} {
  return {
    name: 'get-file-raw',
    transform(src, id): string | void {
      if (fileRegex.filter((re) => re.test(id)).length > 0) {
        return `export default ${JSON.stringify(src)}`;
      }
    },
  };
}
const opcuaCommonJSConfig = {
  plugins: [
    nodeResolve({
      browser: true,
      preferBuiltins: false,
    }),
    commonjs({
      include: [
        /node_modules\/node-opcua-.*/,
        /node_modules\/async/,
        /node_modules\/bn\.js/,
        /node_modules\/asn1\.js/,
        /node_modules\/asn1js/,
        /node_modules\/pem\-jwk/,
        /node_modules\/jws/,
        /node_modules\/jwa/,
        /node_modules\/buffer\-more\-ints/,
        /node_modules\/bcrypt-pbkdf/,
        /node_modules\/ecc\-jsbn/,
        /node_modules\/jsbn/,
      ],
      requireReturnsDefault: "auto",
      dynamicRequireTargets: [
        'node_modules/node-opcua-*/*.js',
        'node_modules/async/*.js'
      ],
      transformMixedEsModules: true,
      ignoreTryCatch: false,
      sourceMap: false
    })
  ],
  optimizeDeps: {
    include: [
      'node-opcua-binary-stream',
      'node-opcua-buffer-utils',
      'node-opcua-basic-types'
    ],
    exclude: ['node-opcua-crypto']
  }
};
export default defineConfig({
  server: {
    host: "127.0.0.1",
    cors: true,
    headers: {
      'Access-Control-Allow-Origin': '*',
      'Access-Control-Allow-Methods': 'GET, POST, PUT, DELETE, PATCH, OPTIONS',
      'Access-Control-Allow-Headers': 'X-Requested-With, content-type, Authorization'
    },

    proxy: {
      '/api': {
        target: 'http://dingyan3:4840',
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api/, ''),
        secure: false
      }

    }
  },
  base: './',
  ...convertCjsConfig,
  plugins: [
    ...(opcuaCommonJSConfig.plugins || []),
    vue(), vueJsx(),
    commonjs(), nodePolyfills({
      include: [
        'buffer',
        'crypto',
        'events',
        'stream',
        'util',
        'process',
        'path'],
      globals: {
        Buffer: true,
        global: true,
        process: true,
      },

    }),


  ],
  define: {
    '__filename': JSON.stringify('browser-file.js'),
    '__dirname': JSON.stringify('/browser-dir'),
    // 'process.env': process.env,
    // 'global': 'globalThis',
    'process.env': JSON.stringify({}),
    'process.browser': true,

  },
  resolve: {
    // alias: [
    //   {
    //     find: '@',
    //     replacement: resolve(__dirname, './src'),
    //   },
    // ],
    alias: {
      '@': resolve(__dirname, './src'),
      'node-opcua-buffer-utils': path.resolve(__dirname, 'src/utils/opcua-buffer-utils.js'),
      'node:buffer': 'buffer',
      'node:crypto': 'crypto-browserify',
      'node:stream': 'stream-browserify',
      'node:util': 'util',
      'node:process': 'process/browser',
      'node:path': 'path-browserify',
      'node:events': 'events',
      'node-opcua-binary-stream': path.resolve(__dirname, 'src/utils/binary-stream-browser.js')
    }
  },
  optimizeDeps: {
    //声明深度路径模块
    include: [
      'bpmn-js/lib/Modeler',
      'highlight.js',
      'codemirror',
      'codemirror/mode/xml/xml.js',
      'codemirror/addon/hint/xml-hint.js',
      'bpmn-js/lib/features/label-editing/LabelUtil.js',
      'buffer',
      'crypto-browserify',
      'stream-browserify',
      'util'
    ],
    exclude: ['node-opcua-binary-stream']
  },
  build: {
    commonjsOptions: {
      transformMixedEsModules: true,
      include: /node_modules\/node-opcua/,
      exclude: [],
      ignoreGlobal: false
    },
    rollupOptions: {
      plugins: [
        ...(opcuaCommonJSConfig.plugins || [])
      ]
    }
  }
});




