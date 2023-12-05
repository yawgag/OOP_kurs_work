package documentation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import test.hibernate.Guest;
import test.hibernate.Reservation;
import test.hibernate.Room;
import test.hibernate.Workers;

import javax.persistence.EntityManager;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.util.List;
import java.util.Objects;

public class XmlMethods{
    private static final Logger logger = LoggerFactory.getLogger(XmlMethods.class);

    public static Element create_workers_xml(Document document, EntityManager ENTITY_MANAGER){
        Element all_workers = document.createElement("all_workers");

        List<Workers> INFORMATION_ROWS = ENTITY_MANAGER.createNativeQuery("select * from Workers", Workers.class).getResultList();

        for (Workers tmp_worker : INFORMATION_ROWS) {
            Element worker = document.createElement("Worker");

            worker.setAttribute("id", String.valueOf(tmp_worker.getWorkerId()));
            worker.setAttribute("name", String.valueOf(tmp_worker.getName()));
            worker.setAttribute("last_name", String.valueOf(tmp_worker.getLastName()));
            worker.setAttribute("age", String.valueOf(tmp_worker.getAge()));
            worker.setAttribute("profession", String.valueOf(tmp_worker.getProfession()));
            worker.setAttribute("work_experience", String.valueOf(tmp_worker.getWorkExperience()));

            all_workers.appendChild(worker);
        }
        return all_workers;
    }

    public static Element create_rooms_xml(Document document, EntityManager ENTITY_MANAGER){
        Element all_rooms = document.createElement("all_rooms");

        List<Room> INFORMATION_ROWS = ENTITY_MANAGER.createNativeQuery("select * from Room", Room.class).getResultList();

        for (Room tmp_room : INFORMATION_ROWS) {
            Element room = document.createElement("room");

            room.setAttribute("id", String.valueOf(tmp_room.getRoomId()));
            room.setAttribute("number", String.valueOf(tmp_room.getRoomNumber()));
            room.setAttribute("copacity", String.valueOf(tmp_room.getCopacity()));
            room.setAttribute("price", String.valueOf(tmp_room.getPrice()));
            room.setAttribute("reserved_now", String.valueOf(tmp_room.getReservedNow()));

            all_rooms.appendChild(room);
        }
        return all_rooms;
    }

    public static Element create_reservations_xml(Document document, EntityManager ENTITY_MANAGER){
        Element all_reservations = document.createElement("all_reservations");

        List<Reservation> INFORMATION_ROWS = ENTITY_MANAGER.createNativeQuery("select * from Reservation", Reservation.class).getResultList();

        for (Reservation tmp_reservation : INFORMATION_ROWS) {
            Element reservation = document.createElement("reservation");

            reservation.setAttribute("id", String.valueOf(tmp_reservation.getReservationId()));
            reservation.setAttribute("number_id", String.valueOf(String.valueOf(tmp_reservation.getRoom())));
            reservation.setAttribute("check_in", String.valueOf(tmp_reservation.getCheckIn()));
            reservation.setAttribute("check_out", String.valueOf(tmp_reservation.getCheckOut()));

            all_reservations.appendChild(reservation);
        }
        return all_reservations;
    }

    public static Element create_guests_xml(Document document, EntityManager ENTITY_MANAGER){
        Element all_guests = document.createElement("all_guests");

        List<Guest> INFORMATION_ROWS = ENTITY_MANAGER.createNativeQuery("select * from Guest", Guest.class).getResultList();

        for (Guest tmp_guest : INFORMATION_ROWS) {
            Element guest = document.createElement("guest");

            guest.setAttribute("last_name", String.valueOf(tmp_guest.getLastName()));
            guest.setAttribute("age", String.valueOf(tmp_guest.getAge()));
            guest.setAttribute("reservation_id", String.valueOf(tmp_guest.getReserv()));
            guest.setAttribute("id", String.valueOf(tmp_guest.getGuestId()));
            guest.setAttribute("name", String.valueOf(tmp_guest.getName()));

            all_guests.appendChild(guest);
        }

        return all_guests;
    }

    public static void parse_xml_to_database(String file_name, EntityManager ENTITY_MANAGER) throws ParserConfigurationException, IOException, SAXException {
        logger.info("parsing xml to data base");
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new File(file_name));

        Element element = document.getDocumentElement();

        NodeList nodeList = element.getChildNodes();
        NodeList workers = element.getElementsByTagName("Worker");
        NodeList Rooms = element.getElementsByTagName("room");
        NodeList Reservations = element.getElementsByTagName("reservation");
        NodeList Guests = element.getElementsByTagName("guest");

        ENTITY_MANAGER.getTransaction().begin();
        for(int i = 0; i < Rooms.getLength(); i++){
            Room room = new Room();
            room.setCopacity(Integer.valueOf(Rooms.item(i).getAttributes().getNamedItem("copacity").getNodeValue()));
            room.setRoomNumber(Integer.valueOf(Rooms.item(i).getAttributes().getNamedItem("number").getNodeValue()));
            room.setPrice(Integer.valueOf(Rooms.item(i).getAttributes().getNamedItem("price").getNodeValue()));

            ENTITY_MANAGER.persist(room);
            for(int j = 0; j < Reservations.getLength(); j++){
                if(Integer.valueOf(Reservations.item(j).getAttributes().getNamedItem("number_id").getNodeValue()).equals(Integer.valueOf(Rooms.item(i).getAttributes().getNamedItem("id").getNodeValue()))){
                    room.setReservedNow(1);
                    Reservation reservation =  new Reservation();
                    reservation.setRoom(room.getRoomId());
                    reservation.setCheckIn(Date.valueOf(Reservations.item(i).getAttributes().getNamedItem("check_in").getNodeValue()));
                    reservation.setCheckOut(Date.valueOf(Reservations.item(i).getAttributes().getNamedItem("check_out").getNodeValue()));
                    ENTITY_MANAGER.persist(reservation);
                    for(int k = 0; k < Guests.getLength(); k++){
                        if(Objects.equals(Reservations.item(j).getAttributes().getNamedItem("id").getNodeValue(), Guests.item(k).getAttributes().getNamedItem("reservation_id").getNodeValue())){
                            Guest guest = new Guest();
                            guest.setReserv(reservation.getReservationId());
                            guest.setAge(Integer.parseInt(Guests.item(k).getAttributes().getNamedItem("age").getNodeValue()));
                            guest.setName(Guests.item(k).getAttributes().getNamedItem("name").getNodeValue());
                            guest.setLastName(Guests.item(k).getAttributes().getNamedItem("last_name").getNodeValue());
                            ENTITY_MANAGER.persist(guest);
                        }
                    }
                }
            }



        }
        ENTITY_MANAGER.getTransaction().commit();


        for(int i = 0; i < workers.getLength(); i++){
            Workers tmp = new Workers();

            tmp.setLastName(workers.item(i).getAttributes().getNamedItem("last_name").getNodeValue());
            tmp.setName(workers.item(i).getAttributes().getNamedItem("name").getNodeValue());
            tmp.setProfession(workers.item(i).getAttributes().getNamedItem("profession").getNodeValue());
            tmp.setWorkExperience(Integer.valueOf(workers.item(i).getAttributes().getNamedItem("work_experience").getNodeValue()));
            tmp.setAge(Integer.valueOf(workers.item(i).getAttributes().getNamedItem("age").getNodeValue()));

            ENTITY_MANAGER.getTransaction().begin();
            ENTITY_MANAGER.persist(tmp);
            ENTITY_MANAGER.getTransaction().commit();
        }

    }

    public static Document create_guests_report_xml(Document document, EntityManager ENTITY_MANAGER){
        List<Guest> guests = ENTITY_MANAGER.createNativeQuery("select * from Guest", Guest.class).getResultList();


        Element root = document.createElement("main");
        document.appendChild(root);

        for (int i = 0; i < guests.size(); i++) {
            Guest tmp_guest = guests.get(i);
            Reservation reservation = ENTITY_MANAGER.find(Reservation.class, tmp_guest.getReserv());
            Room room = ENTITY_MANAGER.find(Room.class, reservation.getRoom());

            Element guest = document.createElement("guest");

            guest.setAttribute("last_name", String.valueOf(tmp_guest.getLastName()));
            guest.setAttribute("age", String.valueOf(tmp_guest.getAge()));
            guest.setAttribute("id", String.valueOf(tmp_guest.getGuestId()));
            guest.setAttribute("name", String.valueOf(tmp_guest.getName()));
            guest.setAttribute("room", String.valueOf(room.getRoomNumber()));
            guest.setAttribute("check-in", String.valueOf(reservation.getCheckIn()));
            guest.setAttribute("check-out", String.valueOf(reservation.getCheckOut()));


            root.appendChild(guest);
        }
        return document;
    }
}
