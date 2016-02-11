package net.pawelhajduk.component;

import com.intellij.psi.PsiClass;
import com.intellij.ui.ScrollPaneFactory;
import com.intellij.ui.SeparatorFactory;
import java.awt.BorderLayout;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class TypeSelectionPanel<T extends PsiClass, M extends TypeInfo> extends JPanel {
    private final TypeSelectionTable myTable;

    public TypeSelectionPanel(String title, List<TypeInfo> memberInfo) {
        super();
        setLayout(new BorderLayout());

        myTable = createMemberSelectionTable(memberInfo);
        JScrollPane scrollPane = ScrollPaneFactory.createScrollPane(myTable);
        add(SeparatorFactory.createSeparator(title, myTable), BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    protected TypeSelectionTable createMemberSelectionTable(List<TypeInfo> memberInfo) {
        return new TypeSelectionTable(memberInfo, null);
    }

    public TypeSelectionTable getTable() {
        return myTable;
    }
}

