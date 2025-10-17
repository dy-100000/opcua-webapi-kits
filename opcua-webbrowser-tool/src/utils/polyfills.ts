// Browser polyfills for Node.js modules
// This file provides polyfills for node-opcua dependencies

// Buffer polyfill
if (typeof window !== 'undefined' && !window.Buffer) {
  // Simple Buffer polyfill for browser
  class BufferPolyfill extends Uint8Array {
    constructor(input?: any, encoding?: any, offset?: any) {
      if (typeof input === 'string') {
        const encoder = new TextEncoder();
        const bytes = encoder.encode(input);
        super(bytes);
      } else if (input instanceof ArrayBuffer) {
        super(input);
      } else if (Array.isArray(input)) {
        super(input);
      } else if (typeof input === 'number') {
        super(input);
      } else {
        super(0);
      }
    }

    static from(input: any, encoding?: any): BufferPolyfill {
      return new BufferPolyfill(input, encoding);
    }

    static alloc(size: number): BufferPolyfill {
      return new BufferPolyfill(size);
    }

    toString(encoding?: string): string {
      const decoder = new TextDecoder(encoding || 'utf8');
      return decoder.decode(this);
    }
  }

  (window as any).Buffer = BufferPolyfill;
  (globalThis as any).Buffer = BufferPolyfill;
}

// Process polyfill
if (typeof window !== 'undefined' && !window.process) {
  (window as any).process = {
    env: {},
    browser: true,
    version: '',
    versions: {},
    platform: 'browser',
    nextTick: (fn: Function) => setTimeout(fn, 0),
  };
  (globalThis as any).process = (window as any).process;
}

// Global polyfill
if (typeof window !== 'undefined' && !window.global) {
  (window as any).global = window;
  (globalThis as any).global = globalThis;
}

// Console polyfill (if needed)
if (typeof window !== 'undefined' && !window.console) {
  (window as any).console = {
    log: () => {},
    error: () => {},
    warn: () => {},
    info: () => {},
    debug: () => {},
  };
}

export {}; 