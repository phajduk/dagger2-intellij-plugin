package net.pawelhajduk.component;

import com.intellij.psi.PsiClass;

public class TypeInfo extends TypeInfoBase<PsiClass> {

    public TypeInfo(PsiClass member) {
        super(member);
        LOG.assertTrue(member.isValid());
    }
}

