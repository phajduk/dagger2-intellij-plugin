package net.pawelhajduk;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiEnumConstant;
import com.intellij.psi.PsiMember;
import com.intellij.refactoring.RefactoringBundle;
import com.intellij.refactoring.ui.MemberSelectionPanel;
import com.intellij.refactoring.util.classMembers.MemberInfo;
import com.intellij.refactoring.util.classMembers.MemberInfoStorage;
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
        List<TypeInfo> typeInfos = createTypeInfos();
        final TypeSelectionPanel typeSelectionPanel = new TypeSelectionPanel("Select modules for component",
                typeInfos);
//        final MemberSelectionPanel memberSelectionPanel = createTestingMembersPanel();
//        panel.add(memberSelectionPanel, BorderLayout.CENTER);
        panel.add(typeSelectionPanel, BorderLayout.CENTER);
        return panel;
    }

    @NotNull
    private MemberSelectionPanel createTestingMembersPanel() {
        MemberInfoStorage memberInfoStorage = new MemberInfoStorage(classes.get(0), new MemberInfo.Filter<PsiMember>() {
            public boolean includeMember(PsiMember element) {
                return !(element instanceof PsiEnumConstant);
            }
        });
        List<MemberInfo> members = memberInfoStorage.getClassMemberInfos(classes.get(0));

        return new MemberSelectionPanel(
                RefactoringBundle.message("members.to.be.pushed.down.panel.title"),
                members,
                RefactoringBundle.message("keep.abstract.column.header"));
    }


    @NotNull
    @Override
    protected Action[] createActions() {
        return new Action[]{getOKAction(), getCancelAction()};
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
}

