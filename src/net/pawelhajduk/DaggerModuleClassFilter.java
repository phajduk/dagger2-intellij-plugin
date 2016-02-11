package net.pawelhajduk;

import com.intellij.ide.util.ClassFilter;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiClass;

public class DaggerModuleClassFilter implements ClassFilter {
    @Override
    public boolean isAccepted(PsiClass aClass) {
        PsiAnnotation daggerModuleAnnotation = aClass.getModifierList().findAnnotation("dagger.Module");
        return daggerModuleAnnotation != null;
    }

}
