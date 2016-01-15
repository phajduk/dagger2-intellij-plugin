package net.pawelhajduk;

import com.intellij.codeInsight.generation.ClassMember;
import com.intellij.codeInsight.generation.GenerateConstructorHandler;
import com.intellij.codeInsight.generation.GenerationInfo;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiModifierList;
import com.intellij.util.IncorrectOperationException;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class GenerateInjectedConstructorHandler extends GenerateConstructorHandler {
    @NotNull
    @Override
    protected List<? extends GenerationInfo> generateMemberPrototypes(PsiClass aClass, ClassMember[] members) throws IncorrectOperationException {
        List<? extends GenerationInfo> memberPrototypes = super.generateMemberPrototypes(aClass, members);
        for (GenerationInfo memberPrototype : memberPrototypes) {
            PsiModifierList modifierList = memberPrototype.getPsiMember().getModifierList();
            if (modifierList != null) {
                modifierList.addAnnotation("javax.inject.Inject");
            }
        }
        return memberPrototypes;
    }
}
