package com.example.yamlrunner;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.search.GlobalSearchScope;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.yaml.psi.YAMLScalar;

public class YamlClassReference extends PsiReferenceBase<YAMLScalar> {

    public YamlClassReference(@NotNull YAMLScalar element) {
        super(element);
    }

    @Override
    public @Nullable PsiElement resolve() {
        String className = getValue();
        Project project = getElement().getProject();

        // 用 PSI 找到这个类
        JavaPsiFacade facade = JavaPsiFacade.getInstance(project);
        PsiClass psiClass = facade.findClass(className, GlobalSearchScope.allScope(project));

        return psiClass;
    }

    @Override
    public Object @NotNull [] getVariants() {
        return new Object[0]; // 这里可以实现自动补全（可选）
    }
}
