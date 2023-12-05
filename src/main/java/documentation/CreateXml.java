package documentation;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.persistence.EntityManager;
import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class CreateXml {


    static JPanel content_window;
    static EntityManager ENTITY_MANAGER;


    public CreateXml(JPanel window, EntityManager em){
        content_window = window;
        ENTITY_MANAGER = em;
    }
    
    public JButton draw_template_button(){  // function to draw main menu buttons
        JButton button_template = new JButton(new ImageIcon("./img/CreateXmlIMG.png"));
        button_template.setToolTipText("Импорт-экспорт данных");
        button_template.addActionListener(new main_menu_button_action());
        return button_template;
    }
    
    public static void draw_data_export_import_menu(){
        content_window.removeAll();
        content_window.setLayout(new FlowLayout());

        JButton all_data_xml_button = new JButton("данные в xml");
        all_data_xml_button.addActionListener(new all_data_xml_button_action());

        JButton parse_xml_button = new JButton("xml в данные");
        parse_xml_button.addActionListener(new parse_xml_button_action());

        JButton make_pdf_report_button = new JButton("создать отчёт");
        make_pdf_report_button.addActionListener(new make_pdf_report_button_action());

        JButton multi_thread_button = new JButton("многопоточность");
        multi_thread_button.addActionListener(new multi_thread_button_action());

        content_window.add(all_data_xml_button);
        content_window.add(parse_xml_button);
        content_window.add(make_pdf_report_button);
        content_window.revalidate();
        content_window.repaint();
    }

    public static void create_all_data_xml() throws ParserConfigurationException, TransformerException, FileNotFoundException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.newDocument();

        Element root = document.createElement("main");

        root.appendChild(XmlMethods.create_workers_xml(document, ENTITY_MANAGER));
        root.appendChild(XmlMethods.create_rooms_xml(document, ENTITY_MANAGER));
        root.appendChild(XmlMethods.create_reservations_xml(document, ENTITY_MANAGER));
        root.appendChild(XmlMethods.create_guests_xml(document, ENTITY_MANAGER));

        document.appendChild(root);

        Transformer t = TransformerFactory.newInstance().newTransformer();
        t.setOutputProperty(OutputKeys.INDENT, "yes");
        t.transform(new DOMSource(document), new StreamResult(new FileOutputStream("test_dom.xml")));
    }


    public static void create_all_guests_report_xml() throws ParserConfigurationException, TransformerException, FileNotFoundException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.newDocument();
//        Element root = document.createElement("main");

        XmlMethods.create_guests_report_xml(document, ENTITY_MANAGER);


        Transformer t = TransformerFactory.newInstance().newTransformer();
        t.setOutputProperty(OutputKeys.INDENT, "yes");
        t.transform(new DOMSource(document), new StreamResult(new FileOutputStream("guests.xml")));
    }

    public static void create_all_rooms_report_xml() throws ParserConfigurationException, TransformerException, FileNotFoundException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.newDocument();
        Element root = document.createElement("main");

        root.appendChild(XmlMethods.create_rooms_xml(document, ENTITY_MANAGER));
        document.appendChild(root);
        Transformer t = TransformerFactory.newInstance().newTransformer();
        t.setOutputProperty(OutputKeys.INDENT, "yes");
        t.transform(new DOMSource(document), new StreamResult(new FileOutputStream("rooms.xml")));
    }

    public static void create_all_workers_report_xml() throws InterruptedException {
        Thread th2 = new Thread(new mult2("making xml"));
        th2.start();
        th2.join();
    }

    static class main_menu_button_action implements ActionListener {  // handles main menu buttons actions
        @Override
        public void actionPerformed(ActionEvent e) {
            draw_data_export_import_menu();
        }
    }

    static class all_data_xml_button_action implements ActionListener {  
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                create_all_data_xml();
                JOptionPane.showMessageDialog(content_window, "Файл был успешно создан");
            } catch (ParserConfigurationException | TransformerException | FileNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    static class parse_xml_button_action implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {

            Thread th1 = new Thread(new mult1("'parsing xml'"));
            th1.start();
            try {
                th1.join();
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }

        }
    }

    static class make_pdf_report_button_action implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {

            Thread th3 = new Thread(new mult3("making report"));
            th3.start();
            try {
                th3.join();
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    static class multi_thread_button_action implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            MakeReports.select_report_values(content_window);
        }
    }
}
