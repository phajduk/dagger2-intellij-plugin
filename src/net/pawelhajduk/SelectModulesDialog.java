package net.pawelhajduk;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.psi.PsiClass;
import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JPanel;
import net.pawelhajduk.component.TypeInfo;
import net.pawelhajduk.component.TypeSelectionPanel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SelectModulesDialog extends DialogWrapper {

    private List<PsiClass> classes;
    private List<TypeInfo> typeInfos;

    protected SelectModulesDialog(@Nullable Project project, List<PsiClass> classes) {
        super(project);
        this.classes = classes;
        init();
        setTitle("Select modules");
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        typeInfos = createTypeInfos();
        final TypeSelectionPanel typeSelectionPanel = new TypeSelectionPanel("Select modules for component",
                typeInfos);
        panel.add(typeSelectionPanel, BorderLayout.CENTER);
        return panel;
    }

    @NotNull
    @Override
    protected Action[] createActions() {
        return new Action[]{getOKAction(), getCancelAction()};
    }

    @Override
    protected void doOKAction() {
        super.doOKAction();

    }

    private List<TypeInfo> createTypeInfos() {
        List<TypeInfo> result = new ArrayList<>();
        for (PsiClass aClass : classes) {
            result.add(createFromClass(aClass));
        }
        return result;
    }

    private TypeInfo createFromClass(PsiClass aClass) {
        return new TypeInfo(aClass);
    }

    public List<TypeInfo> getTypeInfos() {
        return typeInfos;
    }
}

