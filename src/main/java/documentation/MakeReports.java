package documentation;


import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRXmlDataSource;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.*;
import net.sf.jasperreports.view.JasperViewer;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static documentation.CreateXml.*;

public class MakeReports {
    private static JComboBox table_report_chooser;
    private static JComboBox file_type_chooser;
    static JPanel WINDOW;

    public static void select_report_values(JPanel content_window){
        WINDOW = content_window;
        JDialog choose_values_window = new JDialog();
        choose_values_window.setTitle("выбрать отчёт");
        choose_values_window.setSize(300, 200);
        choose_values_window.setLocation(600, 300);

        choose_values_window.setLayout(new GridBagLayout());
        String[] table_report_values = {"Постояльцы", "Номера", "Работники"};
        String[] file_type_values = {"pdf", "html"};
        table_report_chooser = new JComboBox(table_report_values);
        file_type_chooser = new JComboBox(file_type_values);
        JButton create_report_button = new JButton("создать отчёт");
        create_report_button.addActionListener(new create_report_button_action());

        choose_values_window.add(table_report_chooser, new GridBagConstraints(
                0, 0, 1, 1, 0, 0,
                GridBagConstraints.CENTER, GridBagConstraints.LINE_START,
                new Insets(5, 5, 5, 5), 0, 0));
        choose_values_window.add(file_type_chooser, new GridBagConstraints(
                1, 0, 1, 1, 0, 0,
                GridBagConstraints.CENTER, GridBagConstraints.LINE_START,
                new Insets(5, 5, 5, 5), 0, 0));
        choose_values_window.add(create_report_button, new GridBagConstraints(
                0, 1, 2, 1, 0, 0,
                GridBagConstraints.CENTER, GridBagConstraints.LINE_START,
                new Insets(5, 5, 5, 5), 0, 0));
        choose_values_window.setVisible(true);
    }
    static class create_report_button_action implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int table_value = table_report_chooser.getSelectedIndex();
            String outputFilePath = "/home/yaw/IdeaProjects/another_stupid_file_name/all_reports/";
            String template_source = "/home/yaw/IdeaProjects/another_stupid_file_name/src/main/resources/xml_reports/";
            if(table_value == 0){
                try {
                    create_all_guests_report_xml();
                    String xPath_value = "/main/guest";
                    print("guests.xml",
                            outputFilePath + "guests_report." + file_type_chooser.getSelectedItem(),
                            xPath_value,
                            template_source + "Guests_report.jrxml");
                    JOptionPane.showMessageDialog(WINDOW, "Файл был успешно создан");
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            } else if (table_value == 1){
                try {
                    create_all_rooms_report_xml();
                    String xPath_value = "/main/all_rooms/room";
                    print("rooms.xml",
                            outputFilePath + "rooms_report." + file_type_chooser.getSelectedItem(),
                            xPath_value,
                            template_source + "Rooms_report.jrxml");
                    JOptionPane.showMessageDialog(WINDOW, "Файл был успешно создан");
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            } else if(table_value == 2){
                try {
                    create_all_workers_report_xml();
                    String xPath_value = "/main/all_workers/Worker";
                    print("workers.xml",
                            outputFilePath + "workers_report." + file_type_chooser.getSelectedItem(),
                            xPath_value,
                            template_source + "Workers_report.jrxml");
                    JOptionPane.showMessageDialog(WINDOW, "Файл был успешно создан");
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }


        }
    }



//    static String outputFilePath = "/home/yaw/IdeaProjects/another_stupid_file_name/src/main/resources/xml_reports/report.pdf";
    public static void print(String dataFilePath, String outputFilePath, String xPath_value, String template_source) throws Exception
    {
        try {
            JRXmlDataSource dataSource = new JRXmlDataSource(dataFilePath, xPath_value);
            JasperReport jasperReport = JasperCompileManager.compileReport(template_source);
            JasperPrint print = JasperFillManager.fillReport(jasperReport, null, dataSource);
            if (outputFilePath.toLowerCase().endsWith("pdf")) {
                JRPdfExporter exporter = new JRPdfExporter();
                exporter.setExporterInput(new SimpleExporterInput(print));
                exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputFilePath));
                exporter.setConfiguration(new SimplePdfReportConfiguration());
                exporter.setConfiguration(new SimplePdfExporterConfiguration());
                exporter.exportReport();
            } else{
                HtmlExporter exporter = new HtmlExporter();
                exporter.setExporterInput(new SimpleExporterInput(print));
                exporter.setExporterOutput(new SimpleHtmlExporterOutput(outputFilePath));
                exporter.setConfiguration(new SimpleHtmlReportConfiguration());
                exporter.setConfiguration(new SimpleHtmlExporterConfiguration());
                exporter.exportReport();
            }
            JasperViewer.viewReport(print, false);
        } catch (JRException e) {
            e.printStackTrace();
        }
    }

}


