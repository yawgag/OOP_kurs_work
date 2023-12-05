package gui;


import test.hibernate.Room;
import javax.persistence.EntityManager;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Objects;

public class AllRooms_menu {
    public static JPanel content_window;
    static DataTemplate ROOMS_FORM_TEMPLATE;
    static EntityManager ENTITY_MANAGER;
    String IMG_SOURCE = "./img/room.png";
    static String[] COLUMNS = {"id", "Номер комнаты", "вместимость", "стоимость", "есть проживающие"};
    static List<Room> INFORMATION_ROWS;
    static JTextField[] new_room_input_fields;

    public AllRooms_menu(JPanel window, EntityManager em){
        content_window = window;
        ENTITY_MANAGER = em;

    }

    public AllRooms_menu() { content_window = new JPanel(); }

    public JButton draw_AllRooms_button() {
        ROOMS_FORM_TEMPLATE = new DataTemplate(IMG_SOURCE, "Все номера");
        return ROOMS_FORM_TEMPLATE.draw_template_button(new AllRooms_menu.main_menu_button_action());
    }

    public static String[][] get_rooms_information_rows(){
        ENTITY_MANAGER.getTransaction().begin();
        INFORMATION_ROWS = ENTITY_MANAGER.createNativeQuery("select * from Room", Room.class).getResultList();
        ENTITY_MANAGER.getTransaction().commit();
        
        int size = INFORMATION_ROWS.size();
        String[][] data = new String[size][COLUMNS.length];
        for(int i = 0; i < size; i++){
            data[i] = INFORMATION_ROWS.get(i).toString().split(" ");
            if(Objects.equals(data[i][4], "1")){
                data[i][4] = "да";
            } else {
                data[i][4] = "нет";
            }
        }

        return data;
    }

    static void draw_rooms_menu(){
        DataTemplate.draw_table_area(content_window, get_rooms_information_rows(), COLUMNS, new AllRooms_menu.change_row_button_action());
        add_table_methods();
    }

    static class main_menu_button_action implements ActionListener {  // handles main menu buttons actions
        @Override
        public void actionPerformed(ActionEvent e) {
            draw_rooms_menu();
        }
    }

    static class change_row_button_action implements ActionListener{ //handles "change row" button action

        @Override
        public void  actionPerformed(ActionEvent e){
            JTable information_table = DataTemplate.information_table;
            if(information_table.getSelectedRows().length == 0){
                JOptionPane.showMessageDialog(content_window,
                        "Выберите изменяемую строку",
                        "Ошибка", JOptionPane.INFORMATION_MESSAGE);
            } else {
                int[] rows = information_table.getSelectedRows();
                JDialog change_data = DataTemplate.change_data_window("изменить данные", new AllRooms_menu.save_changes_button_action());
            }
        }
    }

    static class save_changes_button_action implements ActionListener{ // changing data in data base
        @Override
        public void actionPerformed(ActionEvent e){
            int row = DataTemplate.information_table.getSelectedRows()[0]; // get selected row by user
            JTextField[] changed_row = DataTemplate.data_inputs;
            String[] changed_data = new String[COLUMNS.length];
            for(int i = 0; i < COLUMNS.length-1; i++){
                changed_data[i] = changed_row[i].getText();
            }
            if(Validators.validate_room(changed_data)){
                for(int i = 0; i < COLUMNS.length-1; i++){
                    DataTemplate.INFORMATION_ROWS[row][i+1] = changed_data[i];
                }
                change_room_row(changed_data);
                DataTemplate.change_data_dialog.dispose();
            }


        }
    }

    public static void change_room_row(String[] changed_data){
        int changed_room_id = Integer.parseInt(DataTemplate.INFORMATION_ROWS[DataTemplate.information_table.getSelectedRows()[0]][0]);
        Room changed_room = ENTITY_MANAGER.find(Room.class, changed_room_id);
        ENTITY_MANAGER.getTransaction().begin();
        changed_room.setRoomNumber(Integer.parseInt(changed_data[0]));
        changed_room.setCopacity(Integer.parseInt(changed_data[1]));
        changed_room.setPrice(Integer.parseInt(changed_data[2]));
        if(Objects.equals(changed_data[3].toLowerCase(), "да")){
            changed_room.setReservedNow(1);
        } else {
            changed_room.setReservedNow(0);
        }
        ENTITY_MANAGER.getTransaction().commit();
        DataTemplate.draw_table();
    }

    private static void add_table_methods(){
        JPanel window = DataTemplate.change_data;
        JButton add_room_button = new JButton("добавить номер");
        add_room_button.addActionListener(new add_room_button_action());
        JButton delete_room_button = new JButton("удалить номер");
        delete_room_button.addActionListener(new delete_room_button_action());
        window.add(add_room_button);
        window.add(delete_room_button);
        window.revalidate();
        window.repaint();
    }
    static class add_room_button_action implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){
            JDialog dialog = new JDialog();
            dialog.setTitle("добавление нового номера");
            dialog.setLayout(new GridBagLayout());
            dialog.setSize(400, 400);
            dialog.setLocation(600, 300);

            JLabel[] text_tip = new JLabel[COLUMNS.length-2];
            new_room_input_fields = new JTextField[COLUMNS.length-2];
            for(int i = 0; i < COLUMNS.length-2; i++){text_tip[i] = new JLabel(COLUMNS[i+1]);}
            for(int i = 0; i < COLUMNS.length-2; i++){new_room_input_fields[i] = new JTextField(15);}

            for(int i = 0; i < COLUMNS.length-2; i++){
                dialog.add(text_tip[i], new GridBagConstraints(0, i, 1, 1, 0, 0,
                        GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                        new Insets(5, 5, 5, 5), 0, 0));
                dialog.add(new_room_input_fields[i], new GridBagConstraints(1, i, 1, 1, 0, 0,
                        GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                        new Insets(5, 5, 5, 5), 0, 0));
            }

            JButton save_new_room_button = new JButton("добавить новый номер");
            save_new_room_button.addActionListener(new save_new_room_button_action());
            dialog.add(save_new_room_button, new GridBagConstraints(0, COLUMNS.length, 2, 1, 0, 0,
                    GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                    new Insets(5, 5, 5, 5), 0, 0));

            dialog.setVisible(true);
        }
    }
    static class save_new_room_button_action implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){
            String[] new_room_info = new String[COLUMNS.length-1];
            for(int i = 0; i < COLUMNS.length-2; i++){ new_room_info[i] = new_room_input_fields[i].getText();}
            new_room_info[COLUMNS.length-2] = "нет";
            if(Validators.validate_room(new_room_info)){
                ENTITY_MANAGER.getTransaction().begin();

                Room new_room = new Room();
                new_room.setRoomNumber(Integer.parseInt(new_room_info[0]));
                new_room.setCopacity(Integer.parseInt(new_room_info[1]));
                new_room.setPrice(Integer.parseInt(new_room_info[2]));
                new_room.setReservedNow(0);

                ENTITY_MANAGER.persist(new_room);
                ENTITY_MANAGER.getTransaction().commit();
                DataTemplate.INFORMATION_ROWS = get_rooms_information_rows();
                DataTemplate.draw_table();
            }
        }
    }
    static class delete_room_button_action implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){
            int row = DataTemplate.information_table.getSelectedRow();
            if(row == -1){
                JOptionPane.showMessageDialog(content_window,
                        "Выберите удаляемую строку",
                        "Ошибка", JOptionPane.INFORMATION_MESSAGE);
            } else {
                if(DataTemplate.INFORMATION_ROWS[row][COLUMNS.length-1].equals("да")){
                    JOptionPane.showMessageDialog(content_window,
                            "нельзя удалить номер, в котором есть проживающие",
                            "Ошибка", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    int res = JOptionPane.showOptionDialog(new JFrame(), "Вы действительно хотите удалить выбранный номер?","Подтверждерние",
                            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
                            new Object[] { "Да", "Нет" }, JOptionPane.YES_OPTION);
                    if(res == JOptionPane.YES_OPTION) {
                        Room room = ENTITY_MANAGER.find(Room.class, Integer.parseInt(DataTemplate.INFORMATION_ROWS[row][0]));
                        ENTITY_MANAGER.getTransaction().begin();
                        ENTITY_MANAGER.remove(room);
                        ENTITY_MANAGER.getTransaction().commit();

                        DataTemplate.INFORMATION_ROWS = get_rooms_information_rows();
                        DataTemplate.draw_table();
                    }
                }
            }
        }
    }
}


