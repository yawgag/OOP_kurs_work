package gui;


import test.hibernate.Workers;
import javax.persistence.EntityManager;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.DoubleSummaryStatistics;
import java.util.List;


public class AllWorkers_menu {
    static JPanel content_window;
    static DataTemplate WORKERS_FORM_TEMPLATE;
    static EntityManager ENTITY_MANAGER;
    String IMG_SOURCE = "./img/workers.png";
    static String[] COLUMNS = {"id", "Фамилия", "Имя", "Возраст", "Должность", "стаж работы(в годах)"};
    static List<Workers> INFORMATION_ROWS;
    static JTextField[] new_worker_input_fields;

    AllWorkers_menu(JPanel window, EntityManager em){
        content_window = window;
        ENTITY_MANAGER = em;

    }

    public JButton draw_AllWorkers_button() { // drawing button in main menu
        WORKERS_FORM_TEMPLATE = new DataTemplate(IMG_SOURCE, "все работники");
        return WORKERS_FORM_TEMPLATE.draw_template_button(new main_menu_button_action());
    }


    public static String[][] get_workers_information_rows(){ // convert database information to string to fill Jtable
        ENTITY_MANAGER.getTransaction().begin();
        INFORMATION_ROWS = ENTITY_MANAGER.createNativeQuery("select * from Workers", Workers.class).getResultList();
        ENTITY_MANAGER.getTransaction().commit();

        int size = INFORMATION_ROWS.size();
        String[][] data = new String[size][COLUMNS.length];
        for(int i = 0; i < size; i++){
            data[i] = INFORMATION_ROWS.get(i).toString().split(" ");
        }

        return data;
    }

    static void draw_workers_menu(){ // drawing all menu
        DataTemplate.draw_table_area(content_window, get_workers_information_rows(), COLUMNS, new change_row_button_action());
        add_table_methods();
    }

    static class main_menu_button_action implements ActionListener {  // handles main menu buttons actions
        @Override
        public void actionPerformed(ActionEvent e) { draw_workers_menu(); }
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
                DataTemplate.change_data_window("изменить данные", new save_changes_button_action()); // call window to change data
            }
        }
    }

    static class save_changes_button_action implements ActionListener{ // changing data in data base
        @Override
        public void actionPerformed(ActionEvent e){
            int row = DataTemplate.information_table.getSelectedRows()[0];
            JTextField[] changed_row = DataTemplate.data_inputs;
            String[] changed_data = new String[COLUMNS.length];
            for(int i = 0; i < COLUMNS.length-1; i++){
                changed_data[i] = changed_row[i].getText();
            }
            if(Validators.validate_worker(changed_data)){
                for(int i = 0; i < COLUMNS.length-1; i++){
                    DataTemplate.INFORMATION_ROWS[row][i+1] = changed_data[i];
                }
                change_worker_row(changed_data);
                DataTemplate.change_data_dialog.dispose();
            }

        }
    }

    public static void change_worker_row(String[] changed_data){
        int changed_worker_id = Integer.parseInt(DataTemplate.INFORMATION_ROWS[DataTemplate.information_table.getSelectedRows()[0]][0]);
        Workers changed_worker = ENTITY_MANAGER.find(Workers.class, changed_worker_id);
        ENTITY_MANAGER.getTransaction().begin();
        changed_worker.setLastName(changed_data[0]);
        changed_worker.setName(changed_data[1]);
        changed_worker.setAge(Integer.parseInt(changed_data[2]));
        changed_worker.setProfession(changed_data[3]);
        changed_worker.setWorkExperience(Integer.parseInt(changed_data[4]));
        ENTITY_MANAGER.getTransaction().commit();
        DataTemplate.draw_table();
    }

    private static void add_table_methods(){
        JPanel window = DataTemplate.change_data;
        JButton add_worker_button = new JButton("добавить работника");
        add_worker_button.addActionListener(new add_worker_button_action());
        JButton delete_worker_button = new JButton("удалить работника");
        delete_worker_button.addActionListener(new delete_worker_button_action());
        window.add(add_worker_button);
        window.add(delete_worker_button);
        window.revalidate();
        window.repaint();
    }
    static class add_worker_button_action implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){
            JDialog dialog = new JDialog();
            dialog.setTitle("добавление нового работника");
            dialog.setLayout(new GridBagLayout());
            dialog.setSize(400, 400);
            dialog.setLocation(600, 300);

            JLabel[] text_tip = new JLabel[COLUMNS.length-1];
            new_worker_input_fields = new JTextField[COLUMNS.length-1];
            for(int i = 0; i < COLUMNS.length-1; i++){text_tip[i] = new JLabel(COLUMNS[i+1]);}
            for(int i = 0; i < COLUMNS.length-1; i++){new_worker_input_fields[i] = new JTextField(15);}

            for(int i = 0; i < COLUMNS.length-1; i++){
                dialog.add(text_tip[i], new GridBagConstraints(0, i, 1, 1, 0, 0,
                        GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                        new Insets(5, 5, 5, 5), 0, 0));
                dialog.add(new_worker_input_fields[i], new GridBagConstraints(1, i, 1, 1, 0, 0,
                        GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                        new Insets(5, 5, 5, 5), 0, 0));
            }

            JButton save_new_worker_button = new JButton("добавить нового работника");
            save_new_worker_button.addActionListener(new save_new_worker_button_action());
            dialog.add(save_new_worker_button, new GridBagConstraints(0, COLUMNS.length, 2, 1, 0, 0,
                    GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                    new Insets(5, 5, 5, 5), 0, 0));

            dialog.setVisible(true);
        }
    }
    static class save_new_worker_button_action implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){
            String[] new_worker_info = new String[COLUMNS.length-1];
            for(int i = 0; i < COLUMNS.length-1; i++){ new_worker_info[i] = new_worker_input_fields[i].getText();}
            if(Validators.validate_worker(new_worker_info)){
                ENTITY_MANAGER.getTransaction().begin();

                Workers new_worker = new Workers();
                new_worker.setName(new_worker_info[1]);
                new_worker.setLastName(new_worker_info[0]);
                new_worker.setAge(Integer.parseInt(new_worker_info[2]));
                new_worker.setProfession(new_worker_info[3]);
                new_worker.setWorkExperience(Integer.parseInt(new_worker_info[4]));

                ENTITY_MANAGER.persist(new_worker);
                ENTITY_MANAGER.getTransaction().commit();
                DataTemplate.INFORMATION_ROWS = get_workers_information_rows();
                DataTemplate.draw_table();
            }

        }
    }
    static class delete_worker_button_action implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){
            int row = DataTemplate.information_table.getSelectedRow();
            if(row == -1){
                JOptionPane.showMessageDialog(content_window,
                        "Выберите удаляемую строку",
                        "Ошибка", JOptionPane.INFORMATION_MESSAGE);
            } else {
                int res = JOptionPane.showOptionDialog(new JFrame(), "Вы действительно хотите удалить выбранного работника?","Подтверждерние",
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
                        new Object[] { "Да", "Нет" }, JOptionPane.YES_OPTION);
                if(res == JOptionPane.YES_OPTION) {
                    Workers worker = ENTITY_MANAGER.find(Workers.class, Integer.parseInt(DataTemplate.INFORMATION_ROWS[row][0]));
                    ENTITY_MANAGER.getTransaction().begin();
                    ENTITY_MANAGER.remove(worker);
                    ENTITY_MANAGER.getTransaction().commit();
                    DataTemplate.INFORMATION_ROWS = get_workers_information_rows();
                    DataTemplate.draw_table();
                }
            }
        }
    }
}

