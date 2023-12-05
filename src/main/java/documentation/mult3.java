package documentation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import javax.persistence.EntityManager;
import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

import static documentation.CreateXml.content_window;

public class mult3 implements Runnable{
    private static final Logger logger = LoggerFactory.getLogger(mult3.class);
    private final String name;
    EntityManager ENTITY_MANAGER = CreateXml.ENTITY_MANAGER;

    public mult3(String name){
        this.name = name;
    }

    @Override
    public void run() {
        logger.info("making report");
        System.out.println("thread " + this.name + " start");
        MakeReports.select_report_values(content_window);
        System.out.println("thread " + this.name + " stop");
    }
}
