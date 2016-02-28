package net.pawelhajduk;

import com.intellij.ide.IdeView;
import com.intellij.ide.actions.CreateFileAction;
import com.intellij.ide.fileTemplates.FileTemplate;
import com.intellij.ide.fileTemplates.FileTemplateManager;
import com.intellij.ide.fileTemplates.FileTemplateUtil;
import com.intellij.ide.fileTemplates.JavaTemplateUtil;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Computable;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.SearchScope;
import com.intellij.psi.search.searches.AllClassesSearch;
import com.intellij.util.PlatformIcons;
import com.intellij.util.Query;
import java.util.ArrayList;
import java.util.List;
import net.pawelhajduk.component.TypeInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GenerateComponentAction extends AnAction implements DumbAware {

    public GenerateComponentAction() {
        super("Create component", null, PlatformIcons.INTERFACE_ICON);
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        List<PsiClass> modulesClasses = extractDagger2ModulesPsiClasses(e);
        SelectModulesDialog dg = new SelectModulesDialog(e.getProject(), modulesClasses);

        if (!dg.showAndGet()) {
            return;
        }

        List<PsiClass> componentModules = new ArrayList<>();
        List<TypeInfo> typeInfos = dg.getTypeInfos();
        for (TypeInfo typeInfo : typeInfos) {
            if (typeInfo.isChecked()) {
                PsiClass typeObject = typeInfo.getTypeObject();
                componentModules.add(typeObject);
            }
        }

        final DataContext dataContext = e.getDataContext();

        final IdeView view = LangDataKeys.IDE_VIEW.getData(dataContext);
        if (view == null) {
            return;
        }

        final Project project = CommonDataKeys.PROJECT.getData(dataContext);

        final PsiDirectory dir = view.getOrChooseDirectory();
        if (dir == null || project == null) return;

        PsiClass createdComponentClass = (PsiClass) doCreate(dir, dg.getComponentName(), project, componentModules);
    }

    @NotNull
    private List<PsiClass> extractDagger2ModulesPsiClasses(AnActionEvent e) {
        DaggerModuleClassFilter filter = new DaggerModuleClassFilter();
        final Module module = LangDataKeys.MODULE.getData(e.getDataContext());
        SearchScope scope = GlobalSearchScope.moduleScope(module);
        Query<PsiClass> search = AllClassesSearch.search(scope, e.getProject());
        List<PsiClass> modulesClasses = new ArrayList<>();
        for (PsiClass psiClass : search.findAll()) {
            if (filter.isAccepted(psiClass)) {
                modulesClasses.add(psiClass);
            }
        }
        return modulesClasses;
    }

    private PsiElement doCreate(PsiDirectory myDirectory, @Nullable String fileName, Project project, List<PsiClass> componentModules) {
        FileTemplate myTemplate = FileTemplateManager.getInstance(project).getInternalTemplate(JavaTemplateUtil.INTERNAL_CLASS_TEMPLATE_NAME);
        try {
            String newName = fileName;
            PsiDirectory directory = myDirectory;
            if (fileName != null) {
                final String finalFileName = fileName;
                CreateFileAction.MkDirs mkDirs = ApplicationManager.getApplication().runWriteAction(new Computable<CreateFileAction.MkDirs>() {
                    @Override
                    public CreateFileAction.MkDirs compute() {
                        return new CreateFileAction.MkDirs(finalFileName, myDirectory);
                    }
                });
                newName = mkDirs.newName;
                directory = mkDirs.directory;
            }
            PsiElement psiElement = FileTemplateUtil.createFromTemplate(myTemplate, newName, null, directory);
            CommandProcessor.getInstance().executeCommand(project, new Runnable() {
                @Override
                public void run() {
                    ApplicationManager.getApplication().runWriteAction(new Runnable() {
                        @Override
                        public void run() {
                            PsiClass psiClass = (PsiClass) psiElement;
                            PsiAnnotation annotationFromText;
                            if (componentModules.isEmpty()) {
                                annotationFromText = JavaPsiFacade.getInstance(project).getElementFactory().createAnnotationFromText("@dagger.Component", psiClass.getModifierList());

                            } else {
                                StringBuilder sb = new StringBuilder();
                                for (PsiClass moduleClass : componentModules) {
                                    sb.append(moduleClass.getName() + ".class, ");
                                }
                                annotationFromText = JavaPsiFacade.getInstance(project).getElementFactory().createAnnotationFromText(
                                        "@dagger.Component(modules = {" + sb.substring(0, sb.length() - 2).toString() + "})", psiClass);
                            }
                            psiClass.getModifierList().addAfter(annotationFromText, null);
                            FileEditorManager.getInstance(project).openFile(psiClass.getContainingFile().getVirtualFile(), false);
                            Util.reformatClass(project, psiClass);
                        }
                    });
                }
            }, null, null);
            return psiElement;
        } catch (Exception e) {
            String message = e.getMessage();
//            showErrorDialog(e);
        }
        return null;
    }
}

