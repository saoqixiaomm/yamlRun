package com.mm.yamlrunner;

import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.resolve.reference.PsiReferenceRegistrarImpl;
import com.intellij.psi.impl.source.resolve.reference.impl.providers.JavaClassReference;
import com.intellij.psi.impl.source.resolve.reference.impl.providers.JavaClassReferenceProvider;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.psi.YAMLKeyValue;
import org.jetbrains.yaml.psi.YAMLScalar;

public class YamlClassReferenceContributor extends PsiReferenceContributor {
    @Override
    public void registerReferenceProviders(@NotNull PsiReferenceRegistrar registrar) {
        registrar.registerReferenceProvider(
                PlatformPatterns.psiElement(YAMLScalar.class),
                new PsiReferenceProvider() {
                    @Override
                    public PsiReference @NotNull [] getReferencesByElement(@NotNull PsiElement element, @NotNull ProcessingContext context) {
                        // 找到父节点的 key-value
                        YAMLKeyValue keyValue = PsiTreeUtil.getParentOfType(element, YAMLKeyValue.class);
                        if (keyValue != null && "class".equals(keyValue.getKeyText())) {
                            JavaClassReferenceProvider provider = new JavaClassReferenceProvider();
                            // 开启全限定名校验（类似 XML 中的 class 属性）
                            provider.setOption(JavaClassReferenceProvider.RESOLVE_QUALIFIED_CLASS_NAME, Boolean.TRUE);
                            String textValue = ((YAMLScalar) element).getTextValue(); // 取干净的字符串（不带引号）
                            return provider.getReferencesByString(textValue, element, 0);
                        }
                        return PsiReference.EMPTY_ARRAY;
                    }
                }
        );
    }
}
