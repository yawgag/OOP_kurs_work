package documentation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static documentation.CreateXml.ENTITY_MANAGER;

public class mult2 implements Runnable{
    private static final Logger logger = LoggerFactory.getLogger(mult2.class);
    private final String name;

    public mult2(String name){
        this.name = name;
    }

    @Override
    public void run() {
        logger.info("making xml");
        System.out.println("thread " + this.name + " start");
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        }
        Document document = builder.newDocument();
        Element root = document.createElement("main");

        root.appendChild(XmlMethods.create_workers_xml(document, ENTITY_MANAGER));
        document.appendChild(root);
        Transformer t = null;
        try {
            t = TransformerFactory.newInstance().newTransformer();
        } catch (TransformerConfigurationException e) {
            throw new RuntimeException(e);
        }
        t.setOutputProperty(OutputKeys.INDENT, "yes");
        try {
            t.transform(new DOMSource(document), new StreamResult(new FileOutputStream("workers.xml")));
        } catch (TransformerException | FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        System.out.println("thread " + this.name + " stop");
    }
}
