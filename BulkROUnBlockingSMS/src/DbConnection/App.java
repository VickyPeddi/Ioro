package DbConnection;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.File;
import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

/**
 *
 * @author 00501310
 */
public class App {

    /**
     * @param args the command line arguments
     */
    public static String getDetails(String appName) {
        String sReturn = "";
        try {
            File inputFile = new File("C:\\connection.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("app");

            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                if (nNode.getAttributes().item(0).getNodeValue().toString().equalsIgnoreCase(appName)) {
                    if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element eElement = (Element) nNode;
                        sReturn=eElement.getElementsByTagName("JDBCurl").item(0).getTextContent()+","+
                        eElement.getElementsByTagName("USERID").item(0).getTextContent()+","+
                        eElement.getElementsByTagName("PASSWORD").item(0).getTextContent();
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return sReturn;
    }
}
