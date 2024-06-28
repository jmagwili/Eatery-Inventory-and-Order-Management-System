package com.raven.swing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.*;
import java.util.ArrayList;
import java.util.List;

public class MultiSelectComboBox extends JPanel {

    private JTextField textField;
    private JButton button;
    private JList<String> list;
    private JPopupMenu popup;
    private DefaultListModel<String> listModel;
    
    public MultiSelectComboBox() {
        initComponents();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        
        textField = new JTextField();
        textField.setEditable(false);
        add(textField, BorderLayout.CENTER);
        
        button = new JButton("▼");
//        button.setPreferredSize(new Dimension(37,10));
        button.setFont(new Font("SansSerif",5,9));
        add(button, BorderLayout.EAST);
        
        listModel = new DefaultListModel<>();
        list = new JList<>(listModel);
        list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        list.setVisibleRowCount(5);
        list.setPreferredSize(new Dimension(237,0));
        
        popup = new JPopupMenu();
        popup.add(new JScrollPane(list));
        
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (popup.isVisible()) {
                    popup.setVisible(false);
                } else {
                    popup.show(MultiSelectComboBox.this, 0, getHeight());
                }
            }
        });
        
        list.addListSelectionListener(e -> updateTextField());
    }

    private void updateTextField() {
        List<String> selectedValues = list.getSelectedValuesList();
        StringBuilder selectedText = new StringBuilder();
        for (String value : selectedValues) {
            if (selectedText.length() > 0) {
                selectedText.append(", ");
            }
            selectedText.append(value);
        }
        textField.setText(selectedText.toString());
    }

    public List<String> getSelectedItems() {
        return list.getSelectedValuesList();
    }

    public void setItems(List<String> items) {
        listModel.removeAllElements();
        for (String item : items) {
            listModel.addElement(item);
        }
    }
    
    public String[] getItems() {
        String[] items = new String[listModel.size()];
        for (int i = 0; i < listModel.size(); i++) {
            items[i] = listModel.getElementAt(i);
        }
        return items;
    }

    public void setSelectedItems(List<String> selectedItems) {
        list.clearSelection();
        for (String selectedItem : selectedItems) {
            int index = listModel.indexOf(selectedItem);
            if (index != -1) {
                list.addSelectionInterval(index, index);
            }
        }
    }
    
//    public String[] getSelectedItems() {
//        List<String> selectedItems = new ArrayList<>();
//        for (int index : list.getSelectedIndices()) {
//            selectedItems.add(listModel.getElementAt(index));
//        }
//        return selectedItems.toArray(new String[0]);
//    }
}
