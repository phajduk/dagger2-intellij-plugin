package net.pawelhajduk.component;


import com.intellij.openapi.util.Iconable;
import com.intellij.psi.PsiClass;
import com.intellij.refactoring.classMembers.MemberInfoModel;
import com.intellij.refactoring.ui.AbstractMemberSelectionTable;
import com.intellij.refactoring.ui.EnableDisableAction;
import com.intellij.ui.BooleanTableCellRenderer;
import com.intellij.ui.ColoredTableCellRenderer;
import com.intellij.ui.JBColor;
import com.intellij.ui.RowIcon;
import com.intellij.ui.SimpleTextAttributes;
import com.intellij.ui.TableSpeedSearch;
import com.intellij.ui.TableUtil;
import com.intellij.ui.table.JBTable;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.swing.Icon;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Class based on {@link AbstractMemberSelectionTable}
 *
 * @author Pawel Hajduk
 */
public abstract class AbstractTypeSelectionTable<T extends PsiClass, M extends TypeInfoBase<T>> extends JBTable {

    protected static final int CHECKED_COLUMN = 0;
    protected static final int DISPLAY_NAME_COLUMN = 1;
    protected static final String DISPLAY_NAME_COLUMN_HEADER = "Module";
    protected static final int VISIBILITY_ICON_POSITION = 1;
    protected static final int MEMBER_ICON_POSITION = 0;

    protected List<M> myTypesInfos;
    protected TypeInfoModel<T, M> myTypeInfoModel;
    protected MyTableModel<T, M> myTableModel;

    public AbstractTypeSelectionTable(Collection<M> memberInfos, @Nullable TypeInfoModel<T, M> memberInfoModel) {
        myTableModel = new MyTableModel<T, M>(this);

        myTypesInfos = new ArrayList<M>(memberInfos);
        if (memberInfoModel != null) {
            myTypeInfoModel = memberInfoModel;
        } else {
            myTypeInfoModel = new DefaultTypeInfoModel<T, M>();
        }

        setModel(myTableModel);

        TableColumnModel model = getColumnModel();
        model.getColumn(DISPLAY_NAME_COLUMN).setCellRenderer(new MyTableRenderer<T, M>(this));
        TableColumn checkBoxColumn = model.getColumn(CHECKED_COLUMN);
        TableUtil.setupCheckboxColumn(checkBoxColumn);
        checkBoxColumn.setCellRenderer(new MyBooleanRenderer<T, M>(this));

        setPreferredScrollableViewportSize(new Dimension(400, getRowHeight() * 12));
        getSelectionModel().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        setShowGrid(false);
        setIntercellSpacing(new Dimension(0, 0));

        new MyEnableDisableAction().register();
        new TableSpeedSearch(this);
    }

    public Collection<M> getSelectedTypesInfos() {
        ArrayList<M> list = new ArrayList<M>(myTypesInfos.size());
        for (M info : myTypesInfos) {
            if (isTypeInfoSelected(info)) {
                list.add(info);
            }
        }
        return list;
    }

    private boolean isTypeInfoSelected(final M info) {
        final boolean memberEnabled = myTypeInfoModel.isTypeEnabled(info);
        return (memberEnabled && info.isChecked()) || (!memberEnabled && myTypeInfoModel.isCheckedWhenDisabled(info));
    }

    public TypeInfoModel<T, M> getMemberInfoModel() {
        return myTypeInfoModel;
    }

    public void setMemberInfoModel(TypeInfoModel<T, M> memberInfoModel) {
        myTypeInfoModel = memberInfoModel;
    }

    public void fireExternalDataChange() {
        myTableModel.fireTableDataChanged();
    }

    /**
     * Redraws table
     */
    public void redraw() {
        myTableModel.redraw(getSelectedTypesInfos());
        myTableModel.fireTableDataChanged();
    }

    public void setMemberInfos(Collection<M> memberInfos) {
        myTypesInfos = new ArrayList<M>(memberInfos);
        myTableModel.fireTableDataChanged();
    }

    public void scrollSelectionInView() {
        for (int i = 0; i < myTypesInfos.size(); i++) {
            if (isTypeInfoSelected(myTypesInfos.get(i))) {
                Rectangle rc = getCellRect(i, 0, false);
                scrollRectToVisible(rc);
                break;
            }
        }
    }

    @Override
    public void addNotify() {
        super.addNotify();
        scrollSelectionInView();
    }

    protected abstract void setVisibilityIcon(M memberInfo, RowIcon icon);

    protected Icon getMemberIcon(M memberInfo, @Iconable.IconFlags int flags) {
        return memberInfo.getTypeObject().getIcon(flags);
    }

    private static class DefaultTypeInfoModel<T extends PsiClass, M extends TypeInfoBase<T>> implements TypeInfoModel<T, M> {
        @Override
        public boolean isTypeEnabled(M member) {
            return true;
        }

        @Override
        public boolean isCheckedWhenDisabled(M member) {
            return false;
        }

        @Override
        public int checkForProblems(@NotNull M member) {
            return OK;
        }

        @Override
        public String getTooltipText(M member) {
            return null;
        }
    }

    protected static class MyTableModel<T extends PsiClass, M extends TypeInfoBase<T>> extends AbstractTableModel {
        private final AbstractTypeSelectionTable<T, M> myTable;

        public MyTableModel(AbstractTypeSelectionTable<T, M> table) {
            myTable = table;
        }

        @Override
        public int getColumnCount() {
            return 2;

        }

        @Override
        public int getRowCount() {
            return myTable.myTypesInfos.size();
        }

        @Override
        public Class getColumnClass(int columnIndex) {
            if (columnIndex == CHECKED_COLUMN) {
                return Boolean.class;
            }
            return super.getColumnClass(columnIndex);
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            final M memberInfo = myTable.myTypesInfos.get(rowIndex);
            switch (columnIndex) {
                case CHECKED_COLUMN:
                    if (myTable.myTypeInfoModel.isTypeEnabled(memberInfo)) {
                        return memberInfo.isChecked();
                    } else {
                        return myTable.myTypeInfoModel.isCheckedWhenDisabled(memberInfo);
                    }
                case DISPLAY_NAME_COLUMN:
                    return memberInfo.getDisplayName();
                default:
                    throw new RuntimeException("Incorrect column index");
            }
        }

        @Override
        public String getColumnName(int column) {
            switch (column) {
                case CHECKED_COLUMN:
                    return " ";
                case DISPLAY_NAME_COLUMN:
                    return DISPLAY_NAME_COLUMN_HEADER;
                default:
                    throw new RuntimeException("Incorrect column index");
            }
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            switch (columnIndex) {
                case CHECKED_COLUMN:
                    return myTable.myTypeInfoModel.isTypeEnabled(myTable.myTypesInfos.get(rowIndex));
            }
            return false;
        }


        @Override
        public void setValueAt(final Object aValue, final int rowIndex, final int columnIndex) {
            if (columnIndex == CHECKED_COLUMN) {
                myTable.myTypesInfos.get(rowIndex).setChecked(((Boolean) aValue).booleanValue());
            }

            Collection<M> changed = Collections.singletonList(myTable.myTypesInfos.get(rowIndex));
            redraw(changed);
        }

        public void redraw(Collection<M> changed) {
            fireTableDataChanged();
        }
    }

    private static class MyTableRenderer<T extends PsiClass, M extends TypeInfoBase<T>> extends ColoredTableCellRenderer {
        private final AbstractTypeSelectionTable<T, M> myTable;

        public MyTableRenderer(AbstractTypeSelectionTable<T, M> table) {
            myTable = table;
        }

        @Override
        public void customizeCellRenderer(JTable table, final Object value,
                                          boolean isSelected, boolean hasFocus, final int row, final int column) {

            final int modelColumn = myTable.convertColumnIndexToModel(column);
            final M memberInfo = myTable.myTypesInfos.get(row);
            setToolTipText(myTable.myTypeInfoModel.getTooltipText(memberInfo));
            switch (modelColumn) {
                case DISPLAY_NAME_COLUMN: {
                    Icon memberIcon = myTable.getMemberIcon(memberInfo, 0);
                    RowIcon icon = new RowIcon(3);
                    icon.setIcon(memberIcon, MEMBER_ICON_POSITION);
                    setIcon(icon);
                    break;
                }
                default: {
                    setIcon(null);
                }
            }
            setIconOpaque(false);
            setOpaque(false);
            final boolean cellEditable = myTable.myTypeInfoModel.isTypeEnabled(memberInfo);
            setEnabled(cellEditable);

            if (value == null) return;
            final int problem = myTable.myTypeInfoModel.checkForProblems(memberInfo);
            Color c = null;
            if (problem == MemberInfoModel.ERROR) {
                c = JBColor.RED;
            } else if (problem == MemberInfoModel.WARNING && !isSelected) {
                c = JBColor.BLUE;
            }
            append((String) value, new SimpleTextAttributes(Font.PLAIN, c));
        }

    }

    private static class MyBooleanRenderer<T extends PsiClass, M extends TypeInfoBase<T>> extends BooleanTableCellRenderer {
        private final AbstractTypeSelectionTable<T, M> myTable;

        public MyBooleanRenderer(AbstractTypeSelectionTable<T, M> table) {
            myTable = table;
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if (component instanceof JCheckBox) {
                int modelColumn = myTable.convertColumnIndexToModel(column);
                M memberInfo = myTable.myTypesInfos.get(row);
                component.setEnabled(
                        (modelColumn == CHECKED_COLUMN && myTable.myTypeInfoModel.isTypeEnabled(memberInfo)) ||
                                (memberInfo.isChecked())
                );
            }
            return component;
        }
    }

    private class MyEnableDisableAction extends EnableDisableAction {

        @Override
        protected JTable getTable() {
            return AbstractTypeSelectionTable.this;
        }

        @Override
        protected void applyValue(int[] rows, boolean valueToBeSet) {
            List<M> changedInfo = new ArrayList<M>();
            for (int row : rows) {
                final M memberInfo = myTypesInfos.get(row);
                memberInfo.setChecked(valueToBeSet);
                changedInfo.add(memberInfo);
            }
            final int[] selectedRows = getSelectedRows();
            myTableModel.fireTableDataChanged();
            final ListSelectionModel selectionModel = getSelectionModel();
            for (int selectedRow : selectedRows) {
                selectionModel.addSelectionInterval(selectedRow, selectedRow);
            }
        }

        @Override
        protected boolean isRowChecked(final int row) {
            return myTypesInfos.get(row).isChecked();
        }
    }
}
