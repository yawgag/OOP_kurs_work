package documentation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import javax.persistence.EntityManager;
import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

import static documentation.CreateXml.content_window;

public class mult1 implements Runnable{
    private static final Logger logger = LoggerFactory.getLogger(mult1.class);
    private final String name;
    EntityManager ENTITY_MANAGER = CreateXml.ENTITY_MANAGER;

    public mult1(String name){
        this.name = name;
    }

    @Override
    public void run() {
        System.out.println("thread " + this.name + " start");
        //JFileChooser fileopen = new JFileChooser();
        //int file_choose_window = fileopen.showDialog(null, "Открыть файл");
        try {
//            String.valueOf(fileopen.getSelectedFile());
            String source_file_path = "/home/yaw/IdeaProjects/another_stupid_file_name/test_dom.xml";
            if( source_file_path.endsWith(".xml")){
                XmlMethods.parse_xml_to_database(source_file_path, ENTITY_MANAGER);
            } else if (!source_file_path.equals("null")){
                JOptionPane.showMessageDialog(content_window,
                        "Неправильно выбран файл",
                        "Ошибка",
                        JOptionPane.ERROR_MESSAGE);
            }

        } catch (ParserConfigurationException | IOException | SAXException ex) {
            throw new RuntimeException(ex);
        }
        System.out.println("thread " + this.name + " stop");
    }
}
