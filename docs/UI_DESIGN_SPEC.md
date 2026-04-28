# cosmic-replay v4 UI设计规范

## 设计理念

**"Cosmic" 宇宙深邃 + "Replay" 时间循环**

融合宇宙深邃感与时间循环概念，打造专业测试工具的科技美学。

---

## 一、核心设计元素

### 1. Logo设计

**概念**：星轨环绕的播放按钮

- 外圈：椭圆轨道（代表宇宙/Cosmic）
- 内核：播放三角形（代表回放/Replay）
- 配色：青蓝→紫罗兰渐变

### 2. 配色方案

```
主色调（深空系列）：
- 深空黑：#0A0E1A (背景主色)
- 星云蓝：#1A1F3A (卡片背景)
- 极光紫：#4C1D95 (强调色)
- 星光青：#06B6D4 (高亮色)
- 银河白：#F1F5F9 (文字主色)

渐变：
- Logo渐变：linear-gradient(135deg, #06B6D4, #8B5CF6, #A855F7)
- 按钮渐变：linear-gradient(135deg, #0891B2, #3B82F6)
- 背景渐变：radial-gradient(ellipse at top, rgba(99,102,241,0.15) 0%, transparent 50%)

毛玻璃效果：
- background: rgba(26, 31, 58, 0.6)
- backdrop-filter: blur(20px)
- border: 1px solid rgba(148, 163, 184, 0.1)
```

### 3. 背景设计

**宇宙星空背景**：
- CSS径向渐变模拟星云
- 可选：Canvas粒子动画（缓慢移动的星点）

### 4. 字体

```
标题：Inter / SF Pro Display (系统字体)
代码：JetBrains Mono / Fira Code
数字：Tabular-nums (等宽数字)
```

---

## 二、组件设计规范

### 导航栏

```
样式：毛玻璃 + 底部发光边框
高度：56px
Logo：左侧（SVG，带动画）
导航：居中偏左（pill风格按钮）
环境：右侧（胶囊选择器）
主题：右侧（图标按钮）
```

### 统计卡片

```
布局：4列网格
样式：毛玻璃卡片 + 悬浮光晕
动画：hover时scale(1.02) + 光晕增强
数据：大号数字 + 渐变色
```

### 用例表格

```
表头：半透明背景 + 固定顶部
行：hover时背景变亮
状态徽章：圆角pill + 状态色
操作按钮：图标按钮组
```

### 按钮

```
主按钮：渐变背景 + 悬浮光晕
次要按钮：透明背景 + 边框
图标按钮：圆形 + hover光晕
```

---

## 三、动画规范

### 页面过渡

```css
/* 页面切换 */
.fadein {
  animation: fadein 0.3s ease;
}

@keyframes fadein {
  from { opacity: 0; transform: translateY(10px); }
  to { opacity: 1; transform: translateY(0); }
}
```

### 交互反馈

```css
/* 按钮悬浮 */
.btn-primary:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(6, 182, 212, 0.3);
}

/* 卡片悬浮 */
.glass-card:hover {
  border-color: rgba(6, 182, 212, 0.3);
  box-shadow: 0 12px 40px rgba(0, 0, 0, 0.4), 0 0 30px rgba(6, 182, 212, 0.1);
}
```

### Logo动画

```svg
<!-- 轨道上的星点动画 -->
<animate attributeName="cx" values="16;-16;16" dur="3s" repeatCount="indefinite"/>
<animate attributeName="cy" values="-6;6;-6" dur="3s" repeatCount="indefinite"/>
```

---

## 四、响应式断点

```
- Desktop: ≥1280px (4列统计卡片)
- Tablet: 768-1279px (2列统计卡片)
- Mobile: <768px (1列统计卡片，隐藏侧边栏)
```

---

## 五、暗色/亮色主题

### 暗色主题（默认）

```
背景：#0A0E1A → #1A1F3A
文字：#F1F5F9 / #94A3B8
强调：#06B6D4 / #8B5CF6
```

### 亮色主题

```
背景：#F8FAFC → #E2E8F0
文字：#0F172A / #475569
强调：#0891B2 / #7C3AED
```

---

## 六、实施优先级

### P0 - 核心视觉升级

1. ✅ Logo SVG（带动画）
2. ✅ 宇宙背景渐变
3. ✅ 毛玻璃卡片效果
4. ✅ 统计卡片重设计
5. ✅ 导航栏重设计

### P1 - 交互增强

1. ✅ 按钮渐变 + 光晕效果
2. ✅ 页面过渡动画
3. ✅ 表格hover效果
4. ✅ 状态徽章重设计

### P2 - 细节优化

1. ⬜ Canvas星空粒子背景
2. ⬜ 用例执行进度动画
3. ⬜ 成功/失败状态动画
