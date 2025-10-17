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