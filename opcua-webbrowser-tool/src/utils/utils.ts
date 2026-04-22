import { ref, watch } from 'vue'

/**
 * 对值做 debounce
 * @param value 要监听的响应式值
 * @param delay 防抖延迟(ms)
 */
export function useDebounce<T>(value: T, delay = 300) {
  const debounced = ref(value) as { value: T }

  let timer: ReturnType<typeof setTimeout> | null = null
  watch(() => value, (newVal: T) => {
    if (timer) clearTimeout(timer)
    timer = setTimeout(() => (debounced.value = newVal), delay)
  }, { immediate: true })

  return debounced
}

/**
 * 对函数做 debounce
 * @param fn 要防抖的函数
 * @param delay 延迟(ms)
 */
export function useDebounceFn<T extends (...args: any[]) => any>(
  fn: T,
  delay = 300
) {
  let timer: ReturnType<typeof setTimeout> | null = null

  return ((...args: Parameters<T>) => {
    if (timer) clearTimeout(timer)
    timer = setTimeout(() => fn(...args), delay)
  }) as T
}


/**
 * 对值做 throttle
 */
export function useThrottle<T>(value: T, delay = 300) {
  const throttled = ref(value) as { value: T }

  let last = 0
  watch(() => value, (newVal: T) => {
    const now = Date.now()
    if (now - last >= delay) {
      throttled.value = newVal
      last = now
    }
  }, { immediate: true })

  return throttled
}

/**
 * 对函数做 throttle
 */
export function useThrottleFn<T extends (...args: any[]) => any>(
  fn: T,
  delay = 300
) {
  let last = 0
  return ((...args: Parameters<T>) => {
    const now = Date.now()
    if (now - last >= delay) {
      fn(...args)
      last = now
    }
  }) as T
}

/**
 * 将PNG图片的字符串数组数据转换为可显示的图片
 * @param stringArray PNG图片的字符串数组数据
 * @returns 包含imageUrl和base64Image的对象
 */
export function convertPngStringArrayToImage(stringArray: string[]): {
  imageUrl: string;
  base64Image: string;
  blob: Blob;
} | null {
  if (!stringArray || !Array.isArray(stringArray)) {
    console.warn('Invalid string array provided for PNG conversion');
    return null;
  }

  try {
    // 将字符串数组转换为Uint8Array
    const binaryData = new Uint8Array(stringArray.length);
    for (let i = 0; i < stringArray.length; i++) {
      binaryData[i] = stringArray[i].charCodeAt(0);
    }

    // 验证是否为有效的PNG文件（检查PNG文件头）
    if (binaryData.length < 8 ||
      binaryData[0] !== 0x89 ||
      binaryData[1] !== 0x50 ||
      binaryData[2] !== 0x4E ||
      binaryData[3] !== 0x47) {
      console.warn('Invalid PNG file format');
      return null;
    }

    // 创建Blob对象
    const blob = new Blob([binaryData], { type: 'image/png' });

    // 创建Object URL用于显示图片
    const imageUrl = URL.createObjectURL(blob);

    // 创建base64编码
    const base64Image = `data:image/png;base64,${btoa(String.fromCharCode(...binaryData))}`;

    return {
      imageUrl,
      base64Image,
      blob
    };
  } catch (error) {
    console.error('Error converting PNG string array to image:', error);
    return null;
  }
}

/**
 * 清理Object URL，防止内存泄漏
 * @param imageUrl 要清理的Object URL
 */
export function revokeImageUrl(imageUrl: string): void {
  if (imageUrl && imageUrl.startsWith('blob:')) {
    URL.revokeObjectURL(imageUrl);
  }
}

// utils/asyncUtils.js
// export function withTimeout(promise, timeoutMs = 10000, timeoutMessage = '请求超时') {
//   // 创建超时 Promise
//   const timeoutPromise = new Promise((_, reject) => {
//     const timeoutId = setTimeout(() => {
//       reject(new Error(timeoutMessage));
//     }, timeoutMs);

//     // 清理超时定时器（当原始 promise 完成时）
//     promise.finally(() => clearTimeout(timeoutId));
//   });

//   // 使用 Promise.race 竞争
//   return Promise.race([promise, timeoutPromise]);
// }
export function withTimeout(promise: Promise<any>, ms: number, message = '请求超时', abortController: AbortController | null = null) {
  return Promise.race([
    promise,
    new Promise((_, reject) => {
      const timeoutId = setTimeout(() => {
        if (abortController && !abortController.signal.aborted) {
          try {
            abortController.abort();
          } catch (e) {
            // 忽略 abort 时的错误
            console.warn('AbortController abort failed:', e);
          }
        }
        reject(new Error(message));
      }, ms);

      // 当原始 promise 完成时清理定时器
      promise.finally(() => {
        clearTimeout(timeoutId);
      });
    })
  ]);
}

/**
 * 简单的超时包装器，不依赖 AbortController
 * 用于避免 AbortController 的竞态条件问题
 */
export function withSimpleTimeout(promise: Promise<any>, ms: number, message = '请求超时') {
  return Promise.race([
    promise,
    new Promise((_, reject) => {
      const timeoutId = setTimeout(() => {
        reject(new Error(message));
      }, ms);

      // 当原始 promise 完成时清理定时器
      promise.finally(() => {
        clearTimeout(timeoutId);
      });
    })
  ]);
}

/**
 * 安全的异步函数执行器，捕获所有未处理的错误
 * 避免 Uncaught (in promise) 错误
 */
export async function safeAsync<T>(fn: () => Promise<T>): Promise<T | null> {
  try {
    return await fn();
  } catch (error) {
    console.error('Safe async execution failed:', error);
    return null;
  }
}

/**
 * 初始化全局错误处理
 * 捕获所有未处理的 Promise 错误
 */
export function initGlobalErrorHandling() {
  // 捕获未处理的 Promise 错误
  window.addEventListener('unhandledrejection', (event) => {
    console.error('Unhandled promise rejection:', event.reason);
    // 阻止默认的错误处理行为
    event.preventDefault();
  });

  // 捕获全局 JavaScript 错误
  window.addEventListener('error', (event) => {
    console.error('Global error:', event.error);
  });
}

/**
 * 带重试机制的异步函数执行器
 * @param fn 要执行的异步函数
 * @param maxRetries 最大重试次数
 * @param delay 重试延迟时间(ms)
 * @param retryCondition 重试条件函数，返回true时重试
 */
export async function withRetry(
  fn: () => Promise<any>,
  maxRetries = 3,
  delay = 1000,
  retryCondition = (error: Error) => {
    // 只对网络连接错误重试，不对超时错误重试
    return error.message?.includes('Failed to fetch') ||
      error.message?.includes('ERR_CONNECTION_TIMED_OUT') ||
      error.message?.includes('网络连接失败') ||
      error.message?.includes('ERR_NETWORK_CHANGED') ||
      error.message?.includes('ERR_INTERNET_DISCONNECTED');
  }
) {
  let lastError: Error = new Error('未知错误');

  for (let attempt = 0; attempt <= maxRetries; attempt++) {
    try {
      return await fn();
    } catch (error) {
      lastError = error as Error;

      // 如果是最后一次尝试，或者不满足重试条件，则抛出错误
      if (attempt === maxRetries || !retryCondition(lastError)) {
        throw error;
      }

      console.warn(`尝试 ${attempt + 1}/${maxRetries + 1} 失败，${delay}ms 后重试:`, lastError.message);

      // 等待指定时间后重试
      await new Promise(resolve => setTimeout(resolve, delay));

      // 指数退避：每次重试延迟时间翻倍
      delay *= 2;
    }
  }

  throw lastError;
}

/**
 * 专门用于网络连接错误的重试函数
 * 只对真正的网络连接问题重试，不对超时或业务逻辑错误重试
 */
export async function withNetworkRetry(
  fn: () => Promise<any>,
  maxRetries = 2,
  delay = 1000
) {
  return withRetry(fn, maxRetries, delay, (error: Error) => {
    // 只对真正的网络连接错误重试
    return error.message?.includes('Failed to fetch') ||
      error.message?.includes('ERR_CONNECTION_TIMED_OUT') ||
      error.message?.includes('ERR_NETWORK_CHANGED') ||
      error.message?.includes('ERR_INTERNET_DISCONNECTED') ||
      error.message?.includes('ERR_CONNECTION_REFUSED');
  });
}