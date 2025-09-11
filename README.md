# MovieSearchApp

电影搜索应用程序，允许用户查找和浏览电影信息。支持按电影名称搜索，并显示详细信息，包括海报、简介、评分等。项目采用主流移动开发技术实现，界面简洁，交互流畅。

## 功能简介

- **电影搜索**：输入电影名称，快速检索相关电影。
- **电影详情**：点击电影条目查看详细信息，包括海报、剧情简介、评分等。
- **历史记录**：保存用户的搜索历史，方便随时回顾。
- **收藏功能**：可收藏喜欢的电影，便于后续查看。
- **响应式设计**：适配多种屏幕尺寸，移动端体验友好。

## 技术栈

- **前端**：React Native 或 Flutter（根据实际项目情况填写）
- **后端**：Node.js + Express 或其他服务（如使用第三方API，注明API来源）
- **数据源**：The Movie Database (TMDb) API 或其他公开电影数据库

## 安装与运行

1. **克隆代码库**
   ```bash
   git clone https://github.com/EfbSm5/MovieSearchApp.git
   ```

2. **安装依赖**
   ```bash
   cd MovieSearchApp
   npm install
   # 或者使用yarn
   yarn install
   ```

3. **配置API密钥**
   - 在项目根目录下新建 `.env` 文件
   - 添加你的电影数据库API密钥，例如：`TMDB_API_KEY=your_api_key_here`

4. **运行项目**
   ```bash
   npm start
   ```

## 项目结构

```
MovieSearchApp/
├── src/
│   ├── components/    # 组件
│   ├── screens/       # 页面
│   ├── services/      # API服务
│   ├── assets/        # 静态资源
│   └── App.js         # 入口文件
├── .env               # 环境变量
├── package.json
└── README.md
```

## 贡献指南

欢迎提出 Issue 或 Pull Request 参与项目改进！

1. Fork 本仓库
2. 新建分支并提交你的更改
3. 创建 Pull Request

## License

本项目遵循 MIT 许可证，详情请见 [LICENSE](LICENSE)。

---

如有任何疑问或建议，请在 Issue 区留言。

