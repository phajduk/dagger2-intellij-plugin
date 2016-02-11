package net.pawelhajduk.component;

import com.intellij.icons.AllIcons;
import com.intellij.psi.PsiClass;
import com.intellij.ui.RowIcon;
import java.util.List;

public class TypeSelectionTable extends AbstractTypeSelectionTable<PsiClass, TypeInfo> {

    public TypeSelectionTable(final List<TypeInfo> memberInfos, TypeInfoModel<PsiClass, TypeInfo> memberInfoModel) {
        super(memberInfos, memberInfoModel);
    }

    @Override
    protected void setVisibilityIcon(TypeInfo memberInfo, RowIcon icon) {
        icon.setIcon(AllIcons.FileTypes.JavaClass, VISIBILITY_ICON_POSITION);
    }
}

