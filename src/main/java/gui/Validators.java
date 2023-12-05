package gui;

import Exceptions.Exceptions.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import test.hibernate.Room;

import javax.persistence.EntityManager;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Objects;
import java.util.List;


public class Validators {
    private static final Logger logger = LoggerFactory.getLogger(Validators.class);


    public static boolean validate_guest(String[] input_data, EntityManager em){ //  0-name 1-last_name 2-age 3-room 4,5-date
        List<String> errors = new ArrayList<>();
        try{ empty_string_validator(input_data[0]); } catch(EmptyDataString e){ errors.add("-Пустая строка имени\n"); logger.warn(e.getMessage()); }
        try{ empty_string_validator(input_data[1]); } catch(EmptyDataString e){ errors.add("-Пустая строка фамилии\n"); logger.warn(e.getMessage()); }
        try{ empty_string_validator(input_data[2]); } catch(EmptyDataString e){ errors.add("-Пустая строка возраста\n"); logger.warn(e.getMessage()); }
        try{ first_last_name_validator(input_data[0]); } catch (InvalidFirstLastName e) { errors.add("-Имя не может содержать цифр\n"); logger.warn(e.getMessage()); }
        try{ first_last_name_validator(input_data[1]); } catch (InvalidFirstLastName e) { errors.add("-Фамилия не может содержать цифр\n"); logger.warn(e.getMessage()); }
        try{ integer_validator(input_data[2]); } catch(InvalidInt e) { errors.add("-Неправильно введён возраст\n"); logger.warn(e.getMessage()); }
        try{ date_earlier_later_validator(input_data[4], input_data[5]);} catch (InvalidDatesInput e) { errors.add("-Неправильно введены даты или их порядок\n"); logger.warn(e.getMessage()); }
        try{ room_number_validate(input_data[3], em); } catch(InvalidRoomNumber e) { errors.add("-Неправиль введен номер комнаты\n"); logger.warn(e.getMessage()); }



        if(errors.isEmpty()){ // этот ужас надо переписать
            return true;
        } else {
            logger.warn("incorrect guest information entered");
            String[] out_errors = new String[errors.size()];
            for(int i = 0; i < errors.size(); i++){ out_errors[i] = errors.get(i); }
            draw_errors(out_errors);
            return false;
        }
    }


    public static boolean validate_worker(String[] input_data){

        List<String> errors = new ArrayList<>();
        try{ first_last_name_validator(input_data[1]); } catch (InvalidFirstLastName e) { errors.add("-Имя не может содержать цифр\n"); logger.warn(e.getMessage()); }
        try{ first_last_name_validator(input_data[0]); } catch (InvalidFirstLastName e) { errors.add("-Фамилия не может содержать цифр\n"); logger.warn(e.getMessage()); }
        try{ empty_string_validator(input_data[1]); } catch(EmptyDataString e){ errors.add("-Неправлиьно введено имя\n"); logger.warn(e.getMessage()); }
        try{ empty_string_validator(input_data[0]); } catch(EmptyDataString e){ errors.add("-Неправлиьно введена фамилия\n"); logger.warn(e.getMessage()); }
        try{ integer_validator(input_data[2]); } catch(InvalidInt e) { errors.add("-Неправлиьно введён возраст\n"); logger.warn(e.getMessage()); }
        try{ empty_string_validator(input_data[3]); } catch(EmptyDataString e){ errors.add("-Неправлиьно введена должность\n"); logger.warn(e.getMessage()); }
        try{ integer_validator(input_data[4]); } catch(InvalidInt e) { errors.add("-Неправлиьно введён стаж работы\n"); logger.warn(e.getMessage()); }
        try{ work_experience_age_validator(input_data[2], input_data[4]); } catch(InvalidWorkExperience e) { errors.add("-Слишком большой стжа работы\n"); logger.warn(e.getMessage()); }
        
        if(errors.isEmpty()){
            return true;
        } else {
            logger.warn("incorrect worker information entered");
            String[] out_errors = new String[errors.size()];
            for(int i = 0; i < errors.size(); i++){ out_errors[i] = errors.get(i); }
            draw_errors(out_errors);
            return false;
        }
    }

    public static boolean validate_room(String[] input_data){
        List<String> errors = new ArrayList<>();
        if(input_data.length == 3){ }
        try{ integer_validator(input_data[0]); } catch(InvalidInt e) { errors.add("-неправильно введен номер комнаты"); logger.warn(e.getMessage()); }
        try{ integer_validator(input_data[1]); } catch(InvalidInt e) { errors.add("-неправильно введена вместимость номера"); logger.warn(e.getMessage()); }
        try{ integer_validator(input_data[2]); } catch(InvalidInt e) { errors.add("-неправильно введена стоимость номера"); logger.warn(e.getMessage()); }
        try{ room_status_validator(input_data[3]); } catch (InvalidRoomStatus e) { errors.add("-неправильно введен статус номера"); logger.warn(e.getMessage()); }

        if(errors.isEmpty()){
            return true;
        } else {
            logger.warn("incorrect room information entered");
            String[] out_errors = new String[errors.size()];
            for(int i = 0; i < errors.size(); i++){ out_errors[i] = errors.get(i); }
            draw_errors(out_errors);
            return false;
        }
    }

    public static void first_last_name_validator(String input_string) throws InvalidFirstLastName{
         for(char i : input_string.toLowerCase().toCharArray()){
             if(!Character.isLetter(i) && i != ' '){
                 throw new InvalidFirstLastName();
             }
         }
    }
    public static void room_status_validator(String reserved_status) throws InvalidRoomStatus{
        if(!Objects.equals(reserved_status.toLowerCase(), "да") && !Objects.equals(reserved_status.toLowerCase(), "нет")){
            throw new InvalidRoomStatus();
        }
    }
    public static void work_experience_age_validator(String age_str, String work_experience_str) throws InvalidWorkExperience {
        try{
            int age = Integer.parseInt(age_str);
            int work_experience = Integer.parseInt(work_experience_str);
            if(age - 16 < work_experience){
                throw new InvalidWorkExperience();
            }
        } catch (IllegalArgumentException ignored){

        }
    }

    public static void integer_validator(String number_str) throws InvalidInt{
        for(char i : number_str.toCharArray()){
            if(i < '0' || i > '9'){
                throw new InvalidInt();
            }
        }
    }

    public static void empty_string_validator(String input_string) throws EmptyDataString{
        input_string = input_string.replaceAll(" ", "");
        if(Objects.equals(input_string, "")){
            throw new EmptyDataString();
        }
    }


    public static void date_earlier_later_validator(String date_check_in_str, String date_check_out_str) throws InvalidDatesInput {

        try{
            Date date_check_in = Date.valueOf(date_check_in_str);
            Date date_check_out = Date.valueOf(date_check_out_str);
            if(date_check_out.compareTo(date_check_in) <= 0){
                throw new InvalidDatesInput();
            }
        } catch (IllegalArgumentException e){
            throw new InvalidDatesInput();
        }
    }

    public static void room_number_validate(String room_number_str, EntityManager em) throws InvalidRoomNumber{
        try{
            int room_number_int = Integer.parseInt(room_number_str);
            List<Room> room = em.createNativeQuery("select * from Room where Room_number = :room_number").setParameter("room_number", room_number_int).getResultList();
            if(room.isEmpty()){
                throw new InvalidRoomNumber();
            }
        } catch(IllegalArgumentException e){
            throw new InvalidRoomNumber();
        }
    }

    static JDialog dialog;
    static void draw_errors(String[] errors){
        dialog = new JDialog();
        dialog.setTitle("Ошибка");
        dialog.setLayout(new GridBagLayout());
        dialog.setSize(330, 250);
        dialog.setLocation(600, 300);
        for(int i = 0; i < errors.length; i++){
            dialog.add(new JLabel(errors[i]), new GridBagConstraints(0, i, 1, 1, 0, 0,
                    GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                    new Insets(5, 5, 5, 5), 0, 0));
        }
        JButton confirm_button = new JButton("Ok");
        confirm_button.addActionListener(new confirm_button_action());
        dialog.add(confirm_button, new GridBagConstraints(0, errors.length+1, 1, 1, 0, 0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(5, 5, 5, 5), 0, 0));
        dialog.setVisible(true);
    }
    static class confirm_button_action implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e){
            dialog.dispose();
        }
    }


}

