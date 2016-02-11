package net.pawelhajduk;

import com.intellij.ide.util.TreeJavaClassChooserDialog;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.ui.SelectFromListDialog;
import com.intellij.psi.PsiClass;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.SearchScope;
import com.intellij.psi.search.searches.AllClassesSearch;
import com.intellij.psi.search.searches.ClassInheritorsSearch;
import com.intellij.util.Query;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class TestAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        List<PsiClass> modulesClasses = extractDagge2ModulesPsiClasses(e);
        SelectModulesDialog dg =  new SelectModulesDialog(e.getProject(), modulesClasses);
        dg.show();
    }

    @NotNull
    private List<PsiClass> extractDagge2ModulesPsiClasses(AnActionEvent e) {
        DaggerModuleClassFilter filter= new DaggerModuleClassFilter();
        final Module module = LangDataKeys.MODULE.getData(e.getDataContext());
        SearchScope scope = GlobalSearchScope.moduleScope(module);
        Query<PsiClass> search = AllClassesSearch.search(scope, e.getProject());
        List<PsiClass> modulesClasses = new ArrayList<>();
        for (PsiClass psiClass : search.findAll()) {
            if(filter.isAccepted(psiClass)){
                modulesClasses.add(psiClass);
            }
        }
        return modulesClasses;
    }
}
