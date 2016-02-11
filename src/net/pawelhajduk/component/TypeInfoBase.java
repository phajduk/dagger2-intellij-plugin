package net.pawelhajduk.component;


import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.PsiClass;

public abstract class TypeInfoBase<T extends PsiClass> {
    protected static final Logger LOG = Logger.getInstance("#com.intellij.refactoring.extractSuperclass.MemberInfo");
    protected String displayName;
    private PsiClass myType;
    private boolean isChecked = false;

    public TypeInfoBase(T member) {
        updateMember(member);
    }

    public String getDisplayName() {
        return displayName;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public PsiClass getTypeObject() {
        return myType;
    }

    public void updateMember(T element) {
        myType = element;
        displayName = element.getName();
    }
}
