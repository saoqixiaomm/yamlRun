package com.mm.yamlrunner;

import com.intellij.execution.actions.ConfigurationContext;
import com.intellij.execution.actions.RunConfigurationProducer;
import com.intellij.execution.application.ApplicationConfiguration;
import com.intellij.execution.application.ApplicationConfigurationType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Ref;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

public class YamlApplicationConfigurationProducer extends RunConfigurationProducer<ApplicationConfiguration> {

    public YamlApplicationConfigurationProducer() {
        super(ApplicationConfigurationType.getInstance());
    }


    @Override
    protected boolean setupConfigurationFromContext(@NotNull ApplicationConfiguration configuration, @NotNull ConfigurationContext context, @NotNull Ref<PsiElement> sourceElement) {
        PsiElement element = sourceElement.get();

        if (!isYamlRunnableElement(element)) {
            return false;
        }
        // 设置配置基本信息
        configuration.setName("Run YAML with Java");
        configuration.setMainClassName("com.guandata.test.TestExecutor");  // TODO: 填你自己真正的 Main 类
        configuration.setProgramParameters("-f\n" + getRelativePath(configuration.getProject(),
                element.getContainingFile().getVirtualFile()));
        configuration.setWorkingDirectory(context.getProject().getBasePath());

        return true;
    }

    public static String getRelativePath(Project project, VirtualFile file) {
        if (file == null) {
            return "";
        }

        // 获取项目的根目录
        VirtualFile projectBaseDir = project.getBaseDir();

        if (projectBaseDir == null) {
            return "";
        }

        // 获取文件的绝对路径
        String absolutePath = file.getPath();

        // 获取项目根目录的绝对路径
        String projectBasePath = projectBaseDir.getPath();

        // 如果文件路径是以项目根目录路径为前缀
        if (absolutePath.startsWith(projectBasePath)) {
            // 计算相对路径
            return absolutePath.substring(projectBasePath.length() + "src/main/resources/".length() + 1);
        }

        // 如果文件路径不在项目根目录下，返回空字符串
        return "";
    }

    @Override
    public boolean isConfigurationFromContext(@NotNull ApplicationConfiguration configuration, @NotNull ConfigurationContext context) {
        // 判断当前配置是不是我们想要的
        PsiElement element = context.getPsiLocation();
        boolean yamlRunnableElement = isYamlRunnableElement(element);
        if (isYamlRunnableElement(element)) {
            configuration.setProgramParameters("-f\n" + getRelativePath(configuration.getProject(),
                    element.getContainingFile().getVirtualFile()));
            return true;
        }
        return false;
    }

    // 判断是否是你想处理的 YAML 元素（自己写）
    private boolean isYamlRunnableElement(PsiElement element) {
        if (element == null) {
            return false;
        }
        // 简单点：只要是 YAML 文件
        return element.getContainingFile() != null;
    }
}
