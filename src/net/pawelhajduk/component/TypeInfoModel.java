package net.pawelhajduk.component;


import com.intellij.psi.PsiClass;
import org.jetbrains.annotations.NotNull;

public interface TypeInfoModel<T extends PsiClass, M extends TypeInfoBase<T>> {
    int OK = 0;
    int WARNING = 1;
    int ERROR = 2;

    boolean isTypeEnabled(M member);

    boolean isCheckedWhenDisabled(M member);

    int checkForProblems(@NotNull M member);

    String getTooltipText(M member);
}
