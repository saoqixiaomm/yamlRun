# YAML Runner Plugin

这是一个IntelliJ IDEA插件，用于在YAML文件上显示运行按钮，点击后可以执行特定脚本。

## 功能

- 在YAML文件的行标记器中显示运行按钮
- 点击按钮执行与YAML文件关联的脚本
- 显示脚本执行结果通知

## 如何构建

### 使用 IntelliJ IDEA 打开项目（推荐）

1. 在 IntelliJ IDEA 中选择 `File > Open...`
2. 选择 `yaml-runner-plugin` 目录
3. IDEA 将自动识别 Gradle 项目并提示下载依赖
4. 接受提示，等待项目构建完成
5. 选择 `Build > Build Project` 来构建插件

### 使用命令行构建（如果遇到 Gradle Wrapper 问题）

如果 `./gradlew build` 命令失败，可能是因为 Gradle Wrapper JAR 文件缺失。建议使用 IntelliJ IDEA 打开项目后进行构建。

构建完成后，插件将位于`build/distributions/yaml-runner-plugin-1.0-SNAPSHOT.zip`。

## 如何安装

1. 在IntelliJ IDEA中，转到`File > Settings > Plugins > ⚙️ > Install Plugin from Disk...`
2. 选择生成的zip文件
3. 重启IDE

## 使用方法

1. 打开任意YAML文件
2. 在文件的第一行左侧会出现运行图标
3. 点击图标执行与该YAML文件关联的脚本
4. 执行结果将通过通知显示

## 定制脚本

默认情况下，插件执行一个简单的示例脚本，显示YAML文件的路径和执行时间。

要自定义执行的脚本，可以修改`RunYamlAction.java`中的`createTempScript`方法。 