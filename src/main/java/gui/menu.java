package gui;

import documentation.CreateXml;
import java.awt.*;
import javax.persistence.EntityManager;
import javax.swing.*;

public class menu{
    static JFrame window;
    public JPanel content;
    EntityManager em;

    public menu(EntityManager em){
        this.em = em;
    }

    public void main_menu(){


        window = new JFrame("Отель");
        window.setSize(830, 900);
        window.setLocation(500, 50);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        content = new JPanel();

        JToolBar toolBar = new JToolBar("панель инструментов");
        toolBar.add(new NewGuest_menu(content, em).draw_new_guest());
        toolBar.add(new AllGuests_menu(content, em).draw_AllGuests_button());
        toolBar.add(new AllRooms_menu(content, em).draw_AllRooms_button());
        toolBar.add(new AllWorkers_menu(content, em).draw_AllWorkers_button());
        toolBar.add(new CreateXml(content, em).draw_template_button());

        window.setLayout(new BorderLayout());
        window.add(toolBar, BorderLayout.NORTH);
        window.add(content);
        window.setVisible(true);
    }


}
