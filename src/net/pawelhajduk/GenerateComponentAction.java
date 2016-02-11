package net.pawelhajduk;

import com.intellij.CommonBundle;
import com.intellij.ide.IdeView;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Ref;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiNameIdentifierOwner;
import com.intellij.util.PlatformIcons;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GenerateComponentAction extends AnAction implements DumbAware {
    public GenerateComponentAction() {
        super("Create component", null, PlatformIcons.INTERFACE_ICON);
    }

    @Override
    public void actionPerformed(AnActionEvent e) {

    }
/*
    //todo append $END variable to templates?
    public static void moveCaretAfterNameIdentifier(PsiNameIdentifierOwner createdElement) {
        final Project project = createdElement.getProject();
        final Editor editor = FileEditorManager.getInstance(project).getSelectedTextEditor();
        if (editor != null) {
            final VirtualFile virtualFile = createdElement.getContainingFile().getVirtualFile();
            if (virtualFile != null) {
                if (FileDocumentManager.getInstance().getDocument(virtualFile) == editor.getDocument()) {
                    final PsiElement nameIdentifier = createdElement.getNameIdentifier();
                    if (nameIdentifier != null) {
                        editor.getCaretModel().moveToOffset(nameIdentifier.getTextRange().getEndOffset());
                    }
                }
            }
        }
    }

    @Override
    public final void actionPerformed(final AnActionEvent e) {
        final DataContext dataContext = e.getDataContext();

        final IdeView view = LangDataKeys.IDE_VIEW.getData(dataContext);
        if (view == null) {
            return;
        }

        final Project project = CommonDataKeys.PROJECT.getData(dataContext);

        final PsiDirectory dir = view.getOrChooseDirectory();
        if (dir == null || project == null) return;

        final SelectModulesDialog.Builder builder = SelectModulesDialog.createDialog(project);
        buildDialog(project, dir, builder);

        final Ref<String> selectedTemplateName = Ref.create(null);
        final PsiFile createdElement =
                builder.show(getErrorTitle(), getDefaultTemplateName(dir), new SelectModulesDialog.FileCreator<PsiFile>() {

                    @Override
                    public PsiFile createFile(@NotNull String name, @NotNull String templateName) {
                        selectedTemplateName.set(templateName);
                        return GenerateComponentAction.this.createFile(name, templateName, dir);
                    }

                    @Override
                    @NotNull
                    public String getActionName(@NotNull String name, @NotNull String templateName) {
                        return GenerateComponentAction.this.getActionName(dir, name, templateName);
                    }
                });
        if (createdElement != null) {
            view.selectElement(createdElement);
            postProcess(createdElement, selectedTemplateName.get(), builder.getCustomProperties());
        }
    }

    protected void postProcess(PsiFile createdElement, String templateName, Map<String, String> customProperties) {
    }

    ;

    @Nullable
    protected PsiFile createFile(String name, String templateName, PsiDirectory dir) {
        return null;
    }

    ;

    protected void buildDialog(Project project, PsiDirectory directory, SelectModulesDialog.Builder builder) {

    }

    @Nullable
    protected String getDefaultTemplateName(@NotNull PsiDirectory dir) {
        String property = getDefaultTemplateProperty();
        return property == null ? null : PropertiesComponent.getInstance(dir.getProject()).getValue(property);
    }

    @Nullable
    protected String getDefaultTemplateProperty() {
        return null;
    }

    @Override
    public void update(final AnActionEvent e) {
        final DataContext dataContext = e.getDataContext();
        final Presentation presentation = e.getPresentation();

        final boolean enabled = isAvailable(dataContext);

        presentation.setVisible(enabled);
        presentation.setEnabled(enabled);
    }

    protected boolean isAvailable(DataContext dataContext) {
        final Project project = CommonDataKeys.PROJECT.getData(dataContext);
        final IdeView view = LangDataKeys.IDE_VIEW.getData(dataContext);
        return project != null && view != null && view.getDirectories().length != 0;
    }

    ;

    protected String getActionName(PsiDirectory directory, String newName, String templateName) {
        return "Create compontent";
    }

    protected String getErrorTitle() {
        return CommonBundle.getErrorTitle();
    }


*/

}

