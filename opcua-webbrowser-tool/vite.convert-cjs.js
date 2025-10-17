// vite.convert-cjs.js
import { defineConfig } from 'vite';
import commonjs from '@rollup/plugin-commonjs';

export default defineConfig({
  plugins: [
    commonjs({
      include: /node_modules\/node-opcua.*/,
      extensions: ['.js'],
      ignore: ['conditional-runtime-dependency']
    })
  ]
});