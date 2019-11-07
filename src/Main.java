import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args){
        System.out.println("Start");
        try {
            List<Flight> flights = parseScheduleXML();
            for (Flight fl: flights) {
                System.out.println(fl.toString());
            }
            saveToFile("C:/Users/Mykola/Downloads/flights.txt", flights);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    private static List<Flight> parseScheduleXML() throws ParserConfigurationException, SAXException, IOException
    {
        //Initialize a list of flights
        List<Flight> flights = new ArrayList<>();

        File dir = new File("C:/Users/Mykola/Downloads/GG_20191029");
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        try {
            for (File file : dir.listFiles()) {
                Scanner s = new Scanner(file);
                Document document = builder.parse(file);
                //Document document = builder.parse(new File("employees.xml"));
                document.getDocumentElement().normalize();
                //NodeList nList = document.getElementsByTagName("FlightSchedule");
                NodeList nList = document.getElementsByTagName("FlightNum");
                for (int temp = 0; temp < nList.getLength(); temp++)
                {
                    Node node = nList.item(temp);
                    if (node.getNodeType() == Node.ELEMENT_NODE)
                    {
                        Element eElement = (Element) node;
                        //Create new Employee Object
                        Flight flight = new Flight();
                        if (eElement.getTagName().equals("FlightNum")) {
                            flight.setFlightNum(eElement.getFirstChild().getNodeValue());
                            flight.setEffectiveStartDate(getNode(eElement, 17).getFirstChild().getNodeValue());
                            flight.setEffectiveEndDate(getNode(eElement, 19).getFirstChild().getNodeValue());
                        }
                        //Add flight to list
                        flights.add(flight);
                    }
                }
                s.close();
            }
        } catch (FileNotFoundException exception) {
            System.out.println("File not found!");
        }
        List<Flight> sortedFlights = flights.stream().sorted(Comparator.comparing(Flight::getEffectiveEndDate)).collect(Collectors.toList());
        return sortedFlights;
    }

    private static Node getNode(Node eElement, Integer integer) {
        Node node = eElement;
        for (int i = 0; i <= integer; i++) {
            node = node.getNextSibling();
        }
        return node;
    }

    private static void saveToFile(String fileName, List<Flight> flights) throws FileNotFoundException {
        PrintWriter pw = new PrintWriter(new FileOutputStream(fileName));
        for (Flight f : flights)
            pw.println(f.toString());
        pw.close();
    }
}
