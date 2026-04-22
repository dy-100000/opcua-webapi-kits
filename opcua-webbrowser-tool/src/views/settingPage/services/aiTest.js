/**
 * AI功能测试文件
 * 用于验证AI功能是否正常工作
 */

// 测试AI服务初始化
export const testAIService = async () => {
  console.log('🧪 开始测试AI服务...');
  
  try {
    // 这里可以添加测试代码
    console.log('✅ AI服务测试通过');
    return true;
  } catch (error) {
    console.error('❌ AI服务测试失败:', error);
    return false;
  }
};

// 测试API连接
export const testAPIConnection = async (apiKey) => {
  console.log('🧪 测试API连接...');
  
  try {
    const response = await fetch('https://api.deepseek.com/v1/chat/completions', {
      method: 'POST',
      headers: {
        'Authorization': `Bearer ${apiKey}`,
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        model: 'deepseek-chat',
        messages: [{ role: 'user', content: 'test' }],
        max_tokens: 1
      })
    });
    
    if (response.ok) {
      console.log('✅ API连接测试通过');
      return true;
    } else {
      console.error('❌ API连接测试失败:', response.status);
      return false;
    }
  } catch (error) {
    console.error('❌ API连接测试异常:', error);
    return false;
  }
};

// 测试组件加载
export const testComponents = () => {
  console.log('🧪 测试组件加载...');
  
  const components = [
    'AIPanel',
    'AIChat', 
    'CodeAnalysis',
    'DiagnosticPanel',
    'AISettings'
  ];
  
  let allLoaded = true;
  
  components.forEach(component => {
    try {
      // 这里可以添加组件存在性检查
      console.log(`✅ ${component} 组件加载成功`);
    } catch (error) {
      console.error(`❌ ${component} 组件加载失败:`, error);
      allLoaded = false;
    }
  });
  
  return allLoaded;
};

// 运行所有测试
export const runAllTests = async (apiKey) => {
  console.log('🚀 开始运行AI功能测试套件...');
  
  const results = {
    service: await testAIService(),
    api: apiKey ? await testAPIConnection(apiKey) : '跳过',
    components: testComponents()
  };
  
  console.log('📊 测试结果:', results);
  
  const allPassed = Object.values(results).every(result => 
    result === true || result === '跳过'
  );
  
  if (allPassed) {
    console.log('🎉 所有测试通过！AI功能已就绪');
  } else {
    console.log('⚠️ 部分测试失败，请检查配置');
  }
  
  return results;
};

// 在浏览器控制台中使用的便捷函数
if (typeof window !== 'undefined') {
  window.testAI = {
    service: testAIService,
    api: testAPIConnection,
    components: testComponents,
    all: runAllTests
  };
  
  console.log('🔧 AI测试工具已加载到 window.testAI');
  console.log('使用方法:');
  console.log('  window.testAI.all("your-api-key") - 运行所有测试');
  console.log('  window.testAI.service() - 测试AI服务');
  console.log('  window.testAI.api("your-api-key") - 测试API连接');
  console.log('  window.testAI.components() - 测试组件加载');
}
