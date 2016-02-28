package net.pawelhajduk;

import com.intellij.lang.LangBundle;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.InputValidatorEx;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiNameHelper;
import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import net.pawelhajduk.component.TypeInfo;
import net.pawelhajduk.component.TypeSelectionPanel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SelectModulesDialog extends DialogWrapper {

    private List<PsiClass> classes;
    private List<TypeInfo> typeInfos;
    private JTextField componentTypeName;
    private InputValidatorEx myInputValidator;

    protected SelectModulesDialog(@Nullable Project project, List<PsiClass> classes) {
        super(project);
        this.classes = classes;
        init();
        setTitle("Generate component");
        myInputValidator = new InputValidatorEx() {
            @Override
            public String getErrorText(String inputString) {
                if (inputString.length() > 0 && !PsiNameHelper.getInstance(project).isQualifiedName(inputString)) {
                    return "This is not a valid Java qualified name";
                }
                return null;
            }

            @Override
            public boolean checkInput(String inputString) {
                return true;
            }

            @Override
            public boolean canClose(String inputString) {
                return !StringUtil.isEmptyOrSpaces(inputString) && getErrorText(inputString) == null;
            }
        };
    }

    @Nullable
    @Override
    protected ValidationInfo doValidate() {
        if (myInputValidator != null) {
            final String text = componentTypeName.getText();
            final boolean canClose = myInputValidator.canClose(text);
            if (!canClose) {
                String errorText = LangBundle.message("incorrect.name");
                if (myInputValidator instanceof InputValidatorEx) {
                    String message = ((InputValidatorEx)myInputValidator).getErrorText(text);
                    if (message != null) {
                        errorText = message;
                    }
                }
                return new ValidationInfo(errorText, componentTypeName);
            }
        }
        return super.doValidate();
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        final TypeSelectionPanel typeSelectionPanel = createTypeSelectionPanel();
        panel.add(typeSelectionPanel, BorderLayout.CENTER);

        final JPanel classNamePanel = getClassNamePanel();
        panel.add(classNamePanel, BorderLayout.NORTH);
        return panel;
    }

    @NotNull
    private JPanel getClassNamePanel() {
        final JPanel classNamePanel = new JPanel(new BorderLayout());
        classNamePanel.add(new JLabel("Component class name"), BorderLayout.WEST);
        componentTypeName = new JTextField("");

        classNamePanel.add(componentTypeName, BorderLayout.CENTER);
        return classNamePanel;
    }

    @NotNull
    private TypeSelectionPanel createTypeSelectionPanel() {
        typeInfos = createTypeInfos();
        return new TypeSelectionPanel("Select modules for component",
                typeInfos);
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

    public String getComponentName(){
        return componentTypeName.getText();
    }
}

