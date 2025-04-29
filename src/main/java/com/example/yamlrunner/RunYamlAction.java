package com.example.yamlrunner;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.Executor;
import com.intellij.execution.RunManager;
import com.intellij.execution.RunnerAndConfigurationSettings;
import com.intellij.execution.application.ApplicationConfiguration;
import com.intellij.execution.application.ApplicationConfigurationType;
import com.intellij.execution.configurations.RunnerSettings;
import com.intellij.execution.executors.DefaultRunExecutor;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.runners.ExecutionEnvironmentBuilder;
import com.intellij.execution.runners.ProgramRunner;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

/**
 * 用于运行YAML脚本的操作类
 */
public class RunYamlAction extends AnAction {
    private static final Logger LOG = Logger.getInstance(RunYamlAction.class);

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        VirtualFile file = e.getData(CommonDataKeys.VIRTUAL_FILE);

        if (project == null || file == null || !file.isDirectory()) {
            return;
        }

        LOG.info("执行YAML运行操作，选中文件夹：" + file.getPath());
        System.out.println("执行YAML运行操作，选中文件夹：" + file.getPath());

        // 调用你已有的方法
        String yamlDir = YamlApplicationConfigurationProducer.getRelativePath(project, file);

        if (!yamlDir.isEmpty()) {
            RunnerAndConfigurationSettings runYamlWithJava = RunManager.getInstance(project).createConfiguration("Run YAML with Java", ApplicationConfigurationType.class);
            ApplicationConfiguration configuration = (ApplicationConfiguration) runYamlWithJava.getConfiguration();
            // 设置配置基本信息
            configuration.setName("Run YAML with Java");
            configuration.setMainClassName("com.guandata.test.TestExecutor");  // TODO: 填你自己真正的 Main 类
            configuration.setProgramParameters("-f\n" + yamlDir);
            configuration.setWorkingDirectory(project.getBasePath());
            RunManager.getInstance(project).addConfiguration(runYamlWithJava);

            // 执行配置并捕获结果
            Executor executor = DefaultRunExecutor.getRunExecutorInstance();
            ProgramRunner<RunnerSettings> runner = ProgramRunner.getRunner(executor.getId(), runYamlWithJava.getConfiguration());
            try {
                ExecutionEnvironment env = ExecutionEnvironmentBuilder.create(project, executor, runYamlWithJava.getConfiguration()).build();
                assert runner != null;
                runner.execute(env);
            } catch (ExecutionException ex) {
                LOG.error("运行YAML失败", ex);
                throw new RuntimeException(ex);
            }
        } else {
            LOG.info("未找到YAML文件");
            System.out.println("No YAML files found.");
        }
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        VirtualFile file = e.getData(CommonDataKeys.VIRTUAL_FILE);
        Presentation presentation = e.getPresentation();
        
        // 只在选中的是"文件夹"时，启用、显示
        boolean isVisible = project != null && file != null && file.isDirectory();
        
        LOG.info("更新菜单状态, 文件: " + (file != null ? file.getPath() : "null") + ", 可见性: " + isVisible);
        
        presentation.setEnabledAndVisible(isVisible);
        
        // 设置更清晰的文本，便于在菜单中识别
        if (isVisible) {
            presentation.setText("Run YAML Script in " + file.getName());
        }
    }
}
