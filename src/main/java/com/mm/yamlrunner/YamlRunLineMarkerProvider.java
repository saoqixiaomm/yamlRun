package com.mm.yamlrunner;

import com.intellij.execution.lineMarker.ExecutorAction;
import com.intellij.execution.lineMarker.RunLineMarkerContributor;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.yaml.psi.YAMLFile;

import javax.swing.*;
import java.util.function.Function;

/**
 * 在YAML文件中添加运行和调试图标
 */
public class YamlRunLineMarkerProvider extends RunLineMarkerContributor {
    private static final Logger LOG = Logger.getInstance(YamlRunLineMarkerProvider.class);

    @Nullable
    @Override
    public Info getInfo(@NotNull PsiElement element) {
        // 检查是否是YAML文件的根元素
        if (!(element.getContainingFile() instanceof YAMLFile)) {
            return null;
        }

        // 只在文件的第一行添加运行和调试按钮
        PsiFile file = element.getContainingFile();
        if (file.getFirstChild() != element) {
            return null;
        }

        System.out.println("=== 添加YAML运行/调试图标 ===");
        System.out.println("YAML文件: " + file.getName());
        LOG.info("为YAML文件添加运行和调试图标: " + file.getName());

        // 创建运行和调试按钮
        DefaultActionGroup actionGroup = new DefaultActionGroup();

        // 运行按钮
        AnAction runAction = new AnAction("运行YAML", "运行这个YAML文件", AllIcons.Actions.Execute) {
            @Override
            public void actionPerformed(@NotNull AnActionEvent e) {
                System.out.println("=== 点击了运行YAML按钮 ===");
                System.out.println("YAML文件: " + file.getName());
                System.out.println("项目: " + (e.getProject() != null ? e.getProject().getName() : "未知"));
                LOG.info("点击运行YAML按钮: " + file.getName());
                //RunUtil.runYamlFile(e.getProject(), (YAMLFile) file, false);
            }
        };
        actionGroup.add(runAction);

        // 调试按钮
        AnAction debugAction = new AnAction("调试YAML", "调试这个YAML文件", AllIcons.Actions.StartDebugger) {
            @Override
            public void actionPerformed(@NotNull AnActionEvent e) {
                System.out.println("=== 点击了调试YAML按钮 ===");
                System.out.println("YAML文件: " + file.getName());
                System.out.println("项目: " + (e.getProject() != null ? e.getProject().getName() : "未知"));
                LOG.info("点击调试YAML按钮: " + file.getName());
                //RunUtil.runYamlFile(e.getProject(), (YAMLFile) file, true);
            }
        };
        actionGroup.add(debugAction);

        // 动态图标，根据执行器类型决定
        Icon icon = AllIcons.RunConfigurations.TestState.Run; // 默认 Run 图标

        // 动作数组，ExecutorAction.getActions() 自动拿所有执行器
        AnAction[] actions = ExecutorAction.getActions();

        // 动态提示文字
        Function<PsiElement, String> tooltipProvider = e -> "运行或调试 YAML 配置";

        return new Info(icon, actions, tooltipProvider);
    }
}
