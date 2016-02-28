package net.pawelhajduk;

import com.intellij.codeInsight.actions.ReformatCodeProcessor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.codeStyle.JavaCodeStyleManager;

public class Util {
    public static void reformatClass(Project project, PsiElement psiClass) {
        JavaCodeStyleManager styleManager = JavaCodeStyleManager.getInstance(project);
        styleManager.optimizeImports(psiClass.getContainingFile());
        styleManager.shortenClassReferences(psiClass);
        new ReformatCodeProcessor(project, psiClass.getContainingFile(), null, false).runWithoutProgress();
    }
}
