package test.hibernate;

import gui.menu;
import javax.persistence.*;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;;import java.io.FileNotFoundException;

class  Main{

    public static void main(String[] args) throws FileNotFoundException, ParserConfigurationException, TransformerException {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("test_persistence");
        EntityManager em = emf.createEntityManager();
        menu mn = new menu(em);
        mn.main_menu();
    }

}