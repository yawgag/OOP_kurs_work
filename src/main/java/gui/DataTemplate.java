package gui;


import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import javax.swing.table.DefaultTableModel;



class DataTemplate {
    static JTextField[] data_inputs; // text field for user input information to change one data row
    static JTable information_table; // information table
    static String[][] INFORMATION_ROWS; // data from database, to draw information_table
    static String[] COLUMNS; // columns in database
    static JPanel data_panel; // area to draw data
    static JPanel change_data; // area to draw table methods
    static JPanel sort_menu; // area to draw sorting methods
    static JPanel WINDOW; // all function window
    static ActionListener change_row_action_listener; // sorry, dumb code, need to rewrite this
    static JComboBox sort_value; // selected values by user
    static JComboBox sort_value_up_down;
    static JDialog change_data_dialog;
    String IMG_SOURCE; // img to draw main menu button
    String TOOL_TIP_TEXT; // text to main menu button


    DataTemplate(String img_source, String tool_tip_text){
        this.IMG_SOURCE = img_source;
        this.TOOL_TIP_TEXT = tool_tip_text;
    }


    public static void draw_table_area(JPanel window_taken, String[][] information_rows_taken, String[] columns_taken, ActionListener change_row_action)
    {
        WINDOW = window_taken;
        WINDOW.removeAll();
        WINDOW.setLayout(new GridBagLayout());

        INFORMATION_ROWS = information_rows_taken;
        COLUMNS = columns_taken;
        change_row_action_listener = change_row_action;
        data_panel = new JPanel();
        sort_menu = new JPanel();
        change_data = new JPanel();  // block to change row in table
        change_data.setLayout(new GridBagLayout());

        draw_table();
        table_methods();
        draw_sorting_methods();

        WINDOW.add(data_panel, new GridBagConstraints(
                0, 1, 1, 1, 1, 0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(5, 5, 5, 5), 0, 0));
        WINDOW.add(change_data, new GridBagConstraints(
                0, 2, 1, 1, 1, 0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(5, 5, 5, 5), 0, 0));

        WINDOW.add(sort_menu, new GridBagConstraints(
                0, 3, 1, 1, 1, 1,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(5, 5, 5, 5), 0, 0));

        WINDOW.revalidate();
        WINDOW.repaint();
    }

    public static void draw_table(){
        data_panel.removeAll();
        data_panel.setLayout(new GridBagLayout());

        DefaultTableModel model = new DefaultTableModel(INFORMATION_ROWS, COLUMNS);
        information_table = new JTable(model){  // all data table
            @Override
            public boolean isCellEditable(int table_row, int table_column){  // make table uneditable
                return false;
            }
        };
        information_table.getColumnModel().getColumn(0).setMaxWidth(45);
        information_table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scroll = new JScrollPane(information_table);  // making scroll bar to table
        data_panel.add(scroll, new GridBagConstraints(
                0, 0, 3, 5, 1, 1,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(5, 5, 5, 5), 0, 0));

        data_panel.revalidate();
        data_panel.repaint();
    }




    public static void table_methods(){
        change_data.setLayout(new FlowLayout());
        JButton change_row = new JButton("изменить запись");
        change_row.addActionListener(change_row_action_listener);
        change_data.add(change_row);

        data_panel.add(change_data);
    }

    public static void draw_sorting_methods(){
        sort_menu.removeAll();
        sort_menu.setLayout(new GridBagLayout());

        JLabel text_tip_sort_value = new JLabel("Параметр сортировки: ");
        String[] values = COLUMNS;
        sort_value = new JComboBox(values);
        String[] values_up_down = {"По возрастанию", "По убыванию"};
        sort_value_up_down = new JComboBox(values_up_down);
        JButton make_sort_button = new JButton("Отсортировать");
        make_sort_button.addActionListener(new sort_data());

        sort_menu.add(text_tip_sort_value, new GridBagConstraints(
                0, 0, 1, 1, 0, 0,
                GridBagConstraints.CENTER, GridBagConstraints.LINE_START,
                new Insets(5, 5, 5, 5), 0, 0));
        sort_menu.add(sort_value, new GridBagConstraints(
                1, 0, 1, 1, 0, 0,
                GridBagConstraints.CENTER, GridBagConstraints.LINE_START,
                new Insets(5, 5, 5, 5), 0, 0));
        sort_menu.add(sort_value_up_down, new GridBagConstraints(
                2, 0, 1, 1, 0, 0,
                GridBagConstraints.CENTER, GridBagConstraints.LINE_START,
                new Insets(5, 5, 5, 5), 0, 0));
        sort_menu.add(make_sort_button, new GridBagConstraints(
                0, 1, 3, 1, 0, 0,
                GridBagConstraints.CENTER, GridBagConstraints.LINE_START,
                new Insets(5, 5, 5, 5), 0, 0));

        sort_menu.revalidate();
        sort_menu.repaint();
    }

    public JButton draw_template_button(ActionListener main_menu_button_action){  // function to draw main menu buttons
        JButton button_template = new JButton(new ImageIcon(IMG_SOURCE));
        button_template.setToolTipText(TOOL_TIP_TEXT);
        button_template.addActionListener(main_menu_button_action);
        return button_template;
    }


    public static JDialog change_data_window(String Title, ActionListener save_changes_button_action) // draw window to change data
    {
        change_data_dialog = new JDialog();
        change_data_dialog.setTitle(Title);
        change_data_dialog.setLayout(new GridBagLayout());
        change_data_dialog.setSize(400, 400);
        change_data_dialog.setLocation(600, 300);

        JLabel[] text_tip = new JLabel[COLUMNS.length-1];
        data_inputs = new JTextField[COLUMNS.length-1];
        for(int i = 0; i < COLUMNS.length-1; i++){text_tip[i] = new JLabel(COLUMNS[i+1]);}
        for(int i = 0; i < COLUMNS.length-1; i++){
            int[] row = information_table.getSelectedRows();
            data_inputs[i] = new JTextField(INFORMATION_ROWS[row[0]][i+1],15);
        }
        for(int i = 0; i < COLUMNS.length-1; i++){
            change_data_dialog.add(text_tip[i], new GridBagConstraints(0, i, 1, 1, 0, 0,
                    GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                    new Insets(5, 5, 5, 5), 0, 0));
            change_data_dialog.add(data_inputs[i], new GridBagConstraints(1, i, 1, 1, 0, 0,
                    GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                    new Insets(5, 5, 5, 5), 0, 0));
        }

        JButton save_changes_button = new JButton("сохранить");
        save_changes_button.addActionListener(save_changes_button_action);
        change_data_dialog.add(save_changes_button, new GridBagConstraints(0, COLUMNS.length, 2, 1, 0, 0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(5, 5, 5, 5), 0, 0));


        change_data_dialog.setVisible(true);
        return change_data_dialog;
    }

    static class sort_data implements ActionListener{ //sorting data by user values
        @Override
        public void  actionPerformed(ActionEvent e){
            try{
                Integer.parseInt(INFORMATION_ROWS[0][sort_value.getSelectedIndex()]);
                Arrays.sort(INFORMATION_ROWS, Comparator.comparing(arr -> Integer.parseInt(arr[sort_value.getSelectedIndex()])));
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException ex) {
                Arrays.sort(INFORMATION_ROWS, Comparator.comparing(arr -> arr[sort_value.getSelectedIndex()]));
            }
            if(sort_value_up_down.getSelectedIndex() != 0){
                Collections.reverse(Arrays.asList(INFORMATION_ROWS));
            }
            draw_table();
        }
    }

}
