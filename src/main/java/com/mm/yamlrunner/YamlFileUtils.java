package com.mm.yamlrunner;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;

import java.util.ArrayList;
import java.util.List;

public class YamlFileUtils {

    public static String getYamlFileNamesRecursively(Project project, VirtualFile directory) {
        if (directory == null || !directory.isDirectory()) {
            return "";
        }

        List<String> yamlFiles = new ArrayList<>();
        
        // 递归遍历该目录下的所有文件
        collectYamlFiles(project,directory, yamlFiles);

        // 用逗号拼接所有 YAML 文件的文件名
        return String.join(",", yamlFiles);
    }

    private static void collectYamlFiles(Project project,VirtualFile directory, List<String> yamlFiles) {
        for (VirtualFile file : directory.getChildren()) {

            if (file.isDirectory()) {
                // 如果是文件夹，则递归处理
                collectYamlFiles(project,file, yamlFiles);
            } else if (file.getName().endsWith(".yaml") || file.getName().endsWith(".yml")) {
                // 如果是 YAML 文件，则添加文件名到列表
                yamlFiles.add(YamlApplicationConfigurationProducer.getRelativePath(project,file));
            }
        }
    }
}
