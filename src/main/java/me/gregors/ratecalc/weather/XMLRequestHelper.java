package me.gregors.ratecalc.weather;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.MissingResourceException;
import java.util.function.Function;

public class XMLRequestHelper {

    /**
     * Gets root element form xml doc. url
     * @param urlString url that contains xml document
     * @return root element from that url doc.
     */
    public static Element getRootOf(String urlString) throws IOException, ParserConfigurationException, SAXException {
        URL url = new URL("https://www.ilmateenistus.ee/ilma_andmed/xml/observations.php");

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(url.openStream());

        return doc.getDocumentElement();
    }

    /**
     * Gets string content from element, by tag
     * @param tag tag that element should have
     * @param element element from what you want to get tag value
     * @return tags string value
     */
    public static String getNodeContent(String tag, Element element) {
        NodeList name = element.getElementsByTagName(tag);
        if (name.getLength() != 1) throw
                new UnsupportedOperationException("Missing tag " + tag + " on element. (or too many tags)");
        String content = name.item(0).getTextContent();
        if (content.isEmpty())
            return null;
        return content;
    }

    /**
     * Gets list of element nodes from node list
     * @param nodes NodeList to convert to element list
     * @return list of elements, from NodeList.
     */
    public static List<Element> getElementNodes(NodeList nodes) {
        ArrayList<Element> list = new ArrayList<>();
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            if (node.getNodeType() != Node.ELEMENT_NODE) continue;
            list.add((Element) node);
        }
        return list;
    }
}
