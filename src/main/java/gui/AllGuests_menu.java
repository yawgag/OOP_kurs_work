package gui;

import org.jdatepicker.impl.JDatePickerImpl;
import test.hibernate.Guest;
import test.hibernate.Reservation;
import test.hibernate.Room;
import test.hibernate.completedReservations;

import javax.persistence.EntityManager;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;


class AllGuests_menu {
    static JPanel content_window;
    static DataTemplate GUESTS_FORM_TEMPLATE;
    static EntityManager ENTITY_MANAGER;
    String IMG_SOURCE = "./img/guests.png";
    static String[] COLUMNS = {"id", "Имя", "Фамилия" , "Возраст", "Номер комнаты", "Дата заезда", "Дата выезда"};
    static List<Guest> INFORMATION_ROWS;

    static JDatePickerImpl check_in_datePicker;
    static JDatePickerImpl check_out_datePicker;
    static JTextField[] changed_data_inputs;
    static JDialog change_data_dialog;



    AllGuests_menu(JPanel window, EntityManager em){
        content_window = window;
        ENTITY_MANAGER = em;
    }

    public JButton draw_AllGuests_button() { // drawing button in main menu
        GUESTS_FORM_TEMPLATE = new DataTemplate(IMG_SOURCE, "Всё постояльцы");
        return GUESTS_FORM_TEMPLATE.draw_template_button(new AllGuests_menu.main_menu_button_action());
    }

    public static String[][] get_guests_information_rows(){ // convert database information to string to fill Jtable
        ENTITY_MANAGER.getTransaction().begin();
        INFORMATION_ROWS = ENTITY_MANAGER.createNativeQuery("select * from Guest", Guest.class).getResultList();
        ENTITY_MANAGER.getTransaction().commit();

        int size = INFORMATION_ROWS.size();
        String[][] data = new String[size][COLUMNS.length];
        ENTITY_MANAGER.getTransaction().begin();
        for(int i = 0; i < size; i++){
            Guest tmp_guest = INFORMATION_ROWS.get(i);
            data[i][0] = String.valueOf(tmp_guest.getGuestId());
            data[i][1] = tmp_guest.getName();
            data[i][2] = tmp_guest.getLastName();
            data[i][3] = String.valueOf(tmp_guest.getAge());

            Reservation tmp_reservation = ENTITY_MANAGER.find(Reservation.class, tmp_guest.getReserv());
            data[i][5] = String.valueOf(tmp_reservation.getCheckIn());
            data[i][6] = String.valueOf(tmp_reservation.getCheckOut());
            data[i][4] = String.valueOf(ENTITY_MANAGER.find(Room.class, tmp_reservation.getRoom()).getRoomNumber());
        }
        ENTITY_MANAGER.getTransaction().commit();
        return data;
    }

    static void draw_guests_menu(){ // drawing all menu
        DataTemplate.draw_table_area(content_window, get_guests_information_rows(), COLUMNS, new AllGuests_menu.change_row_button_action());
        add_table_methods();
    }

    static class main_menu_button_action implements ActionListener {  // handles main menu buttons actions
        @Override
        public void actionPerformed(ActionEvent e) {
            draw_guests_menu();
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
                change_data_window(); // call window to change data
            }
        }
    }

    static class save_changes_button_action implements ActionListener{ // changing data in data base
        @Override
        public void actionPerformed(ActionEvent e){
            int row = DataTemplate.information_table.getSelectedRows()[0];
            JTextField[] changed_row = changed_data_inputs;
            String[] changed_data = new String[COLUMNS.length-1];


            for(int i = 0; i < COLUMNS.length-3; i++){
                changed_data[i] = changed_row[i].getText();
            }
            changed_data[4] = check_in_datePicker.getModel().getYear() + "-" +
                    check_in_datePicker.getModel().getMonth() + "-" +
                    check_in_datePicker.getModel().getDay();
            changed_data[5] = check_out_datePicker.getModel().getYear() + "-" +
                    check_out_datePicker.getModel().getMonth() + "-" +
                    check_out_datePicker.getModel().getDay();

            if(Validators.validate_guest(changed_data, ENTITY_MANAGER)){
                for(int i = 0; i < COLUMNS.length-1; i++){
                    DataTemplate.INFORMATION_ROWS[row][i+1] = changed_data[i];
                }
                change_guest_row(changed_data);
            }

        }
    }

    private static void change_guest_row(String[] changed_data){
        int changed_guest_id = Integer.parseInt(DataTemplate.INFORMATION_ROWS[DataTemplate.information_table.getSelectedRow()][0]);

        ENTITY_MANAGER.getTransaction().begin();

        Guest changed_guest = ENTITY_MANAGER.find(Guest.class, changed_guest_id);
        Reservation changed_reservation = ENTITY_MANAGER.find(Reservation.class, changed_guest.getReserv());

        changed_guest.setName(changed_data[0]);
        changed_guest.setLastName(changed_data[1]);
        changed_guest.setAge(Integer.parseInt(changed_data[2]));

        changed_reservation.setCheckIn(Date.valueOf(changed_data[4]));
        changed_reservation.setCheckOut(Date.valueOf(changed_data[5]));

        List<Room> new_room = ENTITY_MANAGER.createNativeQuery("select * from Hotel.Room where Room_number = :room_number", Room.class)
                .setParameter("room_number", Integer.parseInt(changed_data[3])).getResultList();

        changed_reservation.setRoom(new_room.get(0).getRoomId());

        ENTITY_MANAGER.getTransaction().commit();
        DataTemplate.draw_table();
        change_data_dialog.dispose();
    }

    private static void add_table_methods(){
        JPanel window = DataTemplate.change_data;

        JButton end_reservation_button = new JButton("Завершить бронирование");
        end_reservation_button.setToolTipText("гость выехал из номера");
        end_reservation_button.addActionListener(new end_reservation_button_action());
        JButton decline_reservation_button = new JButton("удалить бронирование");
        decline_reservation_button.setToolTipText("бронирование не завершилось");
        decline_reservation_button.addActionListener(new decline_reservation_button_action());
        window.add(end_reservation_button);
        window.add(decline_reservation_button);
        window.revalidate();
        window.repaint();
    }
    static class end_reservation_button_action implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){
            int row = DataTemplate.information_table.getSelectedRow();
            if(row == -1){
                JOptionPane.showMessageDialog(content_window,
                        "Выберите законченную резервацию",
                        "Ошибка", JOptionPane.INFORMATION_MESSAGE);
            } else {
                int res = JOptionPane.showOptionDialog(new JFrame(), "Вы действительно хотите завершить выбранную резервацию?","Подтверждерние",
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
                        new Object[] { "Да", "Нет" }, JOptionPane.YES_OPTION);
                if(res == JOptionPane.YES_OPTION) {
                    delete_decline_guest_process(1, row);
                }
            }
        }
    }
    static class decline_reservation_button_action implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){
            int row = DataTemplate.information_table.getSelectedRow();
            if(row == -1){
                JOptionPane.showMessageDialog(content_window,
                        "Выберите отменяемую резервацию",
                        "Ошибка", JOptionPane.INFORMATION_MESSAGE);
            } else {
                int res = JOptionPane.showOptionDialog(new JFrame(), "Вы действительно хотите удалить выбранную резерваци?","Подтверждерние",
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
                        new Object[] { "Да", "Нет" }, JOptionPane.YES_OPTION);
                if(res == JOptionPane.YES_OPTION) {
                    delete_decline_guest_process(0, row); // key to decline guest
                }
            }
        }
    }
    private static void delete_decline_guest_process(int key, int row){ // key = 0 - decline guest process, key = 1 - delete guest process

        Guest guest = ENTITY_MANAGER.find(Guest.class, Integer.parseInt(DataTemplate.INFORMATION_ROWS[row][0]));
        Reservation reservation = ENTITY_MANAGER.find(Reservation.class, guest.getReserv());
        Room room = ENTITY_MANAGER.find(Room.class, reservation.getRoom());
        ENTITY_MANAGER.getTransaction().begin();
        if(key == 1){ // decline reservation
            completedReservations completed_reservations = new completedReservations();
            completed_reservations.setName(guest.getName());
            completed_reservations.setLastName(guest.getLastName());
            completed_reservations.setAge(guest.getAge());
            completed_reservations.setCheckIn(reservation.getCheckIn());
            completed_reservations.setChechOut(reservation.getCheckOut());
            completed_reservations.setRoom(room.getRoomId());
            ENTITY_MANAGER.persist(completed_reservations);
        }
        ENTITY_MANAGER.remove(guest);
        ENTITY_MANAGER.remove(reservation);
        ENTITY_MANAGER.getTransaction().commit();

        int cnt = 0;
        for(int i = 0; i < DataTemplate.INFORMATION_ROWS.length; i++){
            if(Objects.equals(DataTemplate.INFORMATION_ROWS[i][COLUMNS.length - 1], "да")){ cnt++;}
        }
        if(cnt > 0){ room.setReservedNow(1);}

        DataTemplate.INFORMATION_ROWS = get_guests_information_rows();
        DataTemplate.draw_table();
    }

    private static void change_data_window() // draw window to change data
    {
        change_data_dialog = new JDialog();
        change_data_dialog.setTitle("изменить данные");
        change_data_dialog.setLayout(new GridBagLayout());
        change_data_dialog.setSize(400, 400);
        change_data_dialog.setLocation(600, 300);

        JLabel[] text_tip = new JLabel[COLUMNS.length-1];
        changed_data_inputs = new JTextField[COLUMNS.length-1];
        int row = DataTemplate.information_table.getSelectedRow();
        for(int i = 0; i < COLUMNS.length-1; i++){text_tip[i] = new JLabel(COLUMNS[i+1]);}
        for(int i = 0; i < COLUMNS.length-3; i++){
            changed_data_inputs[i] = new JTextField(DataTemplate.INFORMATION_ROWS[row][i+1],15);
        }
        for(int i = 0; i < COLUMNS.length-3; i++){
            change_data_dialog.add(text_tip[i], new GridBagConstraints(0, i, 1, 1, 0, 0,
                    GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                    new Insets(5, 5, 5, 5), 0, 0));
            change_data_dialog.add(changed_data_inputs[i], new GridBagConstraints(1, i, 1, 1, 0, 0,
                    GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                    new Insets(5, 5, 5, 5), 0, 0));
        }

        check_in_datePicker = datePicker.get_data_picker();
        Calendar cal = Calendar.getInstance();
        cal.setTime(Date.valueOf(DataTemplate.INFORMATION_ROWS[row][5]));
        check_in_datePicker.getModel().setDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        check_in_datePicker.getModel().setSelected(true);
        check_out_datePicker = datePicker.get_data_picker();
        cal.setTime(Date.valueOf(DataTemplate.INFORMATION_ROWS[row][6]));
        check_out_datePicker.getModel().setDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        check_out_datePicker.getModel().setSelected(true);

        change_data_dialog.add(text_tip[4], new GridBagConstraints(0, 4, 1, 1, 0, 0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(5, 5, 5, 5), 0, 0));
        change_data_dialog.add(check_in_datePicker, new GridBagConstraints(1, 4, 1, 1, 0, 0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(5, 5, 5, 5), 0, 0));
        change_data_dialog.add(text_tip[5], new GridBagConstraints(0, 5, 1, 1, 0, 0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(5, 5, 5, 5), 0, 0));
        change_data_dialog.add(check_out_datePicker, new GridBagConstraints(1, 5, 1, 1, 0, 0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(5, 5, 5, 5), 0, 0));


        JButton save_changes_button = new JButton("сохранить");
        save_changes_button.addActionListener(new save_changes_button_action());
        change_data_dialog.add(save_changes_button, new GridBagConstraints(0, COLUMNS.length, 2, 1, 0, 0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(5, 5, 5, 5), 0, 0));


        change_data_dialog.setVisible(true);
    }

}
