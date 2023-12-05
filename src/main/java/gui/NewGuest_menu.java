package gui;


import org.jdatepicker.impl.JDatePickerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import test.hibernate.Guest;
import test.hibernate.Reservation;
import test.hibernate.Room;

import javax.persistence.EntityManager;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.util.List;



class NewGuest_menu {
    private static final Logger logger = LoggerFactory.getLogger(NewGuest_menu.class);

    static JPanel window;
    static JPanel allowed_rooms;
    static String[] data_columns = {"Имя:", "фамилия:", "Возраст:", "Номер комнаты:", "Дата заезда:", "Дата выезда:", };
    static JLabel[] text_tip = new JLabel[data_columns.length];
    static JTextField[] data_inputs = new JTextField[data_columns.length];
    static EntityManager ENTITY_MANAGER;
    static JRadioButton free_room;
    static JTextField room_copacity_input;

    static JDatePickerImpl check_in_datePicker;
    static JDatePickerImpl check_out_datePicker;



    NewGuest_menu(JPanel get_window, EntityManager local_entity_manager){
        window = get_window;
        ENTITY_MANAGER = local_entity_manager;
    }

    public static void draw_new_guest_menu() {
        logger.info("draw new window 'New guest menu'");
        window.removeAll();
        window.setLayout(new GridBagLayout());


        for (int i = 0; i < data_columns.length; i++) {
            text_tip[i] = new JLabel(data_columns[i]);
        }
        for (int i = 0; i < data_columns.length-2; i++) {
            data_inputs[i] = new JTextField(15);
        }

        JPanel input_data = new JPanel(); // area to write all information about new guest
        input_data.setLayout(new GridBagLayout());
        for (int i = 0; i < data_columns.length-2; i++) { // make all text tip and text field to add new guest
            input_data.add(text_tip[i], new GridBagConstraints(0, i, 1, 1, 0, 0,
                    GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                    new Insets(5, 5, 5, 5), 0, 0));
            input_data.add(data_inputs[i], new GridBagConstraints(1, i, 1, 1, 0, 0,
                    GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                    new Insets(5, 5, 5, 5), 0, 0));
        }


        check_out_datePicker = datePicker.get_data_picker();
        check_in_datePicker = datePicker.get_data_picker();

        input_data.add(text_tip[4], new GridBagConstraints(0, 4, 1, 1, 0, 0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(5, 5, 5, 5), 0, 0));
        input_data.add(check_in_datePicker, new GridBagConstraints(1, 4, 1, 1, 0, 0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(5, 5, 5, 5), 0, 0));
        input_data.add(text_tip[5], new GridBagConstraints(0, 5, 1, 1, 0, 0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(5, 5, 5, 5), 0, 0));
        input_data.add(check_out_datePicker, new GridBagConstraints(1, 5, 1, 1, 0, 0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(5, 5, 5, 5), 0, 0));


        JButton add_guest = new JButton("Добавить гостя");
        add_guest.addActionListener(new add_guest_button_action());


        input_data.add(add_guest, new GridBagConstraints(0, 7, 2, 1, 0, 0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(5, 5, 35, 5), 0, 0));

        window.add(input_data, new GridBagConstraints(0, 0, 1, 1, 0, 0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(5, 5, 5, 5), 0, 0));


        JPanel choose_room = new JPanel();
        choose_room.setLayout(new GridBagLayout());
        room_copacity_input = new JTextField(15);
        JLabel text_tip_copacity = new JLabel("вместимость номера");
        free_room = new JRadioButton("пустой номер");
        JButton search_number_with_parameters = new JButton("Найти доступный номер");
        search_number_with_parameters.addActionListener(new find_number_action());

        choose_room.add(text_tip_copacity, new GridBagConstraints(0, 1, 1, 1, 0, 0,
                GridBagConstraints.NORTH,
                GridBagConstraints.HORIZONTAL,
                new Insets(5, 5, 5, 5),
                0, 0));
        choose_room.add(room_copacity_input, new GridBagConstraints(1, 1, 1, 1, 0, 0,
                GridBagConstraints.NORTH,
                GridBagConstraints.HORIZONTAL,
                new Insets(5, 5, 5, 5),
                0, 0));
        choose_room.add(free_room, new GridBagConstraints(0, 3, 2, 1, 0, 0,
                GridBagConstraints.NORTH,
                GridBagConstraints.HORIZONTAL,
                new Insets(5, 5, 5, 5),
                0, 0));
        choose_room.add(search_number_with_parameters, new GridBagConstraints(0, 4, 2, 1, 0, 0,
                GridBagConstraints.NORTH,
                GridBagConstraints.HORIZONTAL,
                new Insets(5, 5, 5, 5),
                0, 0));
        window.add(choose_room, new GridBagConstraints(0, 1, 1, 1, 0, 0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(5, 5, 5, 5), 0, 0));


        allowed_rooms = new JPanel();
        draw_allowed_rooms();

        window.add(allowed_rooms, new GridBagConstraints(0, 3, 1, 1, 1, 0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(5, 5, 5, 5), 0, 0));


        window.revalidate();
        window.repaint();
    }

    private static void draw_allowed_rooms(){
        allowed_rooms.removeAll();

        String [] columns = {"номер", "вместимость", "стоимость"};
        List<Room> data_query;

        if(!free_room.isSelected() && (room_copacity_input.getText().isEmpty())){ // if no input parameters
            data_query = ENTITY_MANAGER.createNativeQuery("select * from Room", Room.class).getResultList();
        } else if (free_room.isSelected() && (room_copacity_input.getText().isEmpty())){ // if "empty room" and no input capacity
            data_query = ENTITY_MANAGER.createNativeQuery("select * from Room where reserved_now = 0", Room.class).getResultList();
        } else if (free_room.isSelected() && (!room_copacity_input.getText().isEmpty())){ // if "empty room" and input capacity
            data_query = ENTITY_MANAGER.createNativeQuery("select * from Room where reserved_now = 0 and copacity = :copacity", Room.class)
                    .setParameter("copacity", Integer.parseInt(room_copacity_input.getText()))
                    .getResultList();
        } else {
            data_query = ENTITY_MANAGER.createNativeQuery("select * from Room where copacity = :copacity", Room.class)
                    .setParameter("copacity", Integer.parseInt(room_copacity_input.getText()))
                    .getResultList();
        }

        String[][] data = new String[data_query.size()][];
        for(int i = 0; i < data_query.size(); i++){
            Room tmp_room = data_query.get(i);
            data[i] = new String[]{Integer.toString(tmp_room.getRoomNumber()),
                    Integer.toString(tmp_room.getCopacity()),
                    Integer.toString(tmp_room.getPrice())};

        }

        DefaultTableModel rooms = new DefaultTableModel(data, columns);
        JTable drawed_rooms = new JTable(rooms);
        JScrollPane scroll = new JScrollPane(drawed_rooms);
        allowed_rooms.add(scroll);

        allowed_rooms.revalidate();
        allowed_rooms.repaint();
    }

    public JButton draw_new_guest(){
        JButton new_guest = new JButton(new ImageIcon("./img/new_guest.png"));
        new_guest.setToolTipText("Добавить нового гостя");
        new_guest.addActionListener(new Action());
        return new_guest;
    }

    public static class add_guest_button_action implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){
            logger.info("action to save new guest");
            String[] changed_data = new String[data_inputs.length];

            for(int i = 0; i < data_inputs.length-2; i++){
                changed_data[i] = data_inputs[i].getText();
            }
            changed_data[4] = check_in_datePicker.getModel().getYear() + "-" +
                    check_in_datePicker.getModel().getMonth() + "-" +
                    check_in_datePicker.getModel().getDay();
            changed_data[5] = check_out_datePicker.getModel().getYear() + "-" +
                    check_out_datePicker.getModel().getMonth() + "-" +
                    check_out_datePicker.getModel().getDay();

            if(Validators.validate_guest(changed_data, ENTITY_MANAGER)){
                Reservation new_reservation = new Reservation(
                        get_room_id_by_number(changed_data[3])
                        ,Date.valueOf(changed_data[4])
                        ,Date.valueOf(changed_data[5]));



                ENTITY_MANAGER.getTransaction().begin();
                ENTITY_MANAGER.persist(new_reservation);

                Guest new_guest = new Guest(changed_data[0],
                        changed_data[1],
                        Integer.parseInt(changed_data[2]),
                        new_reservation.getReservationId());
                Room room = ENTITY_MANAGER.find(Room.class, new_reservation.getRoom());
                room.setReservedNow(1);

                ENTITY_MANAGER.persist(new_guest);
                ENTITY_MANAGER.getTransaction().commit();




                for (int i = 0; i < data_inputs.length-3; i++) { data_inputs[i].setText("");}
                check_in_datePicker.getJFormattedTextField().setText("");
                check_out_datePicker.getJFormattedTextField().setText("");

                JOptionPane.showMessageDialog(window,
                        "Новый постоялец был добавлен в базу",
                        "Уведомление",
                        JOptionPane.INFORMATION_MESSAGE);

                logger.info("new guest add in data base with id = {}", new_guest.getGuestId());
            }
        }
    }

    public static int get_room_id_by_number(String number){
        List<Room> room = ENTITY_MANAGER
                .createNativeQuery("select * from Room where Room_number = :value", Room.class)
                .setParameter("value", Integer.valueOf(number))
                .getResultList();
        return room.get(0).getRoomId();
    }

    static class find_number_action implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){
            draw_allowed_rooms();
        }
    }

    public static class Action implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            draw_new_guest_menu();
        }
    }
}
