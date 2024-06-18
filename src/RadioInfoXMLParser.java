import org.w3c.dom.*;
import org.xml.sax.SAXException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Class that represents the xml parser. Parses a xml gotten from a url
 * connection and saves the relevant information.
 * @author Johan Ahlqvist
 * 2017-12-04
 */
public class RadioInfoXMLParser {
    private DocumentBuilderFactory docFactory;  //Document builder factory.
    private DocumentBuilder docBuilder;         //Document builder.
    private Document doc;                       //Document.
    private URL url;                            //The url address.
    private URLConnection urlConnection;        //The connection.
    private String xmlAddress;  //The xml address.
    private int totalChannels;  //Total channels to be read.

    /**
     * Constructor for the XML parser. Takes a xml address as parameter.
     * @param xmlAddress    String - the xml address.
     */
    public RadioInfoXMLParser(String xmlAddress) {
            setXmlAddress(xmlAddress);
    }

    /**
     * Gets the XML address.
     * @return  String - the xml address.
     */
    public String getXmlAddress() {
        return xmlAddress;
    }

    /**
     * Sets the XML address.
     * @param xmlAddress    String - xml address.
     */
    private void setXmlAddress(String xmlAddress) {
        this.xmlAddress = xmlAddress;
    }

    /**
     * Opens a url connection and parses the XML. Saves relevant data. Takes
     * a controller and a swing worker as parameters.
     * @param controller    RadioInfoController - the controller.
     * @param updateWorker  RadioInfoUpdateWorker - the swing worker.
     * @throws IOException
     * @throws SAXException
     * @throws ParserConfigurationException
     */
    public void getRadioInfo(RadioInfoController controller,
                             RadioInfoUpdateWorker updateWorker) throws
                             IOException, SAXException,
                             ParserConfigurationException {
        /* Open a url connection. */
        url = new URL(xmlAddress);
        urlConnection = url.openConnection();
        /* Build document for parsing. */
        docFactory = DocumentBuilderFactory.newInstance();
        docBuilder = docFactory.newDocumentBuilder();
        doc = docBuilder.parse(urlConnection.getInputStream());
        doc.getDocumentElement().normalize();

        /* Get information about amount of data to read. */
        String elementTagName = "pagination";
        NodeList pageInfo = doc.getElementsByTagName(elementTagName);
        String pageAmount = getElementContent((Element) pageInfo.item(0),
                                              "totalpages", 0);
        int pages = Integer.parseInt(pageAmount);

        String totalAmount = getElementContent((Element) pageInfo.item(0),
                                               "totalhits", 0);
        int totalHits = Integer.parseInt(totalAmount);
        /* Create progress updater */
        updateWorker.createUpdateProgress(totalHits);
        totalChannels = 0;
        updateWorker.updateProgress(totalChannels);
        String nextPageURL = null;

        /* If more than one page of information, get next page address. */
        if (pages > 1) {
            nextPageURL = getElementContent((Element) pageInfo.item(0),
                                            "nextpage", 0);
        }

        /* Get channel elements. */
        elementTagName = "channel";
        NodeList channelList = doc.getElementsByTagName(elementTagName);
        getChannelElements(channelList, controller, updateWorker);
        /* If the are more than one page, get information from next pages. */
        if (pages > 1) {
            for (int page = 2; page <= pages; page++) {
                url = new URL(nextPageURL);
                urlConnection = url.openConnection();

                docFactory = DocumentBuilderFactory.newInstance();
                docBuilder = docFactory.newDocumentBuilder();
                doc = docBuilder.parse(urlConnection.getInputStream());
                doc.getDocumentElement().normalize();
                /* If the are more pages, get next address. */
                if (page < pages) {
                    elementTagName = "pagination";
                    pageInfo = doc.getElementsByTagName(elementTagName);
                    nextPageURL = getElementContent((Element)
                                                    pageInfo.item(0),
                                                    "nextpage", 0);
                }
                /* Get channel elements. */
                elementTagName = "channel";
                channelList = doc.getElementsByTagName(elementTagName);
                getChannelElements(channelList, controller, updateWorker);
            }
        }
    }

    /**
     * Parses through channel elements and saves relevant information.
     * @param channelList   NodeList - list of nodes.
     * @param controller    RadioInfoController - the controller.
     * @param updateWorker  RadioInfoUpdateWorker - the swing worker.
     * @throws IOException
     * @throws ParserConfigurationException
     * @throws SAXException
     */
    private void getChannelElements(NodeList channelList,
                                    RadioInfoController controller,
                                    RadioInfoUpdateWorker updateWorker)
            throws IOException, ParserConfigurationException, SAXException {

        /* Check each node. */
        for (int i = 0; i < channelList.getLength(); i++) {
            Node channelNode = channelList.item(i);
            if (channelNode.getNodeType() == Node.ELEMENT_NODE) {
                Element channelElem = (Element) channelNode;
                RadioChannelInfo channelInfo = new RadioChannelInfo();

                String name = channelElem.getAttribute("name");
                channelInfo.setName(name);

                String channelType = getElementContent(channelElem,
                                                       "channeltype", 0);
                channelInfo.setChannelType(channelType);

                String scheduleURL = getElementContent(channelElem,
                                                       "scheduleurl", 0);
                /* If there is scheduled episodes. */
                if (scheduleURL != null) {
                    url = new URL(scheduleURL);
                    urlConnection = url.openConnection();

                    docFactory = DocumentBuilderFactory.newInstance();
                    docBuilder = docFactory.newDocumentBuilder();
                    doc = docBuilder.parse(urlConnection.getInputStream());
                    doc.getDocumentElement().normalize();
                    /* Get amount of episode pages. */
                    String elemTagName = "pagination";
                    NodeList pageInfo = doc.getElementsByTagName(elemTagName);
                    String pageAmount = getElementContent((Element)
                                                          pageInfo.item(0),
                                                          "totalpages", 0);
                    int pages = Integer.parseInt(pageAmount);
                    String nextPageURL = null;
                    /* Get next page if exist. */
                    if (pages > 1) {
                        nextPageURL = getElementContent((Element)
                                                        pageInfo.item(0),
                                                        "nextpage", 0);
                    }
                    /* Get the episode elements. */
                    elemTagName = "scheduledepisode";
                    NodeList episodeList;
                    episodeList = doc.getElementsByTagName(elemTagName);
                    getEpisodeElements(episodeList, channelInfo, controller);
                    /* Go through next pages if there's more than one. */
                    if (pages > 1) {
                        for (int page = 2; page <= pages; page++) {
                            url = new URL(nextPageURL);
                            urlConnection = url.openConnection();

                            docFactory = DocumentBuilderFactory.newInstance();
                            docBuilder = docFactory.newDocumentBuilder();
                            doc = docBuilder.parse(urlConnection.
                                                   getInputStream());

                            doc.getDocumentElement().normalize();
                            if (page < pages) {
                                elemTagName = "pagination";
                                pageInfo = doc.getElementsByTagName
                                           (elemTagName);
                                nextPageURL = getElementContent((Element)
                                              pageInfo.item(0),
                                              "nextpage", 0);
                            }
                            /* Get episode elements. */
                            elemTagName = "scheduledepisode";
                            episodeList = doc.getElementsByTagName(elemTagName);
                            getEpisodeElements(episodeList, channelInfo,
                                               controller);
                        }
                    }
                }
                /* Add channel information to list. */
                controller.getChannelInfoList().add(channelInfo);
                totalChannels++;
                /* Update progress. */
                updateWorker.updateProgress(totalChannels);
            }
        }
    }

    /**
     * Gets information from episode elements.
     * @param episodeList   NodeList - list with episode nodes.
     * @param channelInfo   RadioChannelInfo - the channel information.
     * @param controller    RadioInfoController - the controller.
     */
    private void getEpisodeElements(NodeList episodeList,
                                   RadioChannelInfo channelInfo,
                                   RadioInfoController controller) {
        /* Check each episode. */
        for (int j = 0; j < episodeList.getLength(); j++) {
            Element episodeElement = (Element) episodeList.item(j);

            String title = getElementContent(episodeElement, "title", 0);

            String description = getElementContent(episodeElement,
                                                   "description", 0);

            String startTimeUTC = getElementContent(episodeElement,
                                                    "starttimeutc", 0);

            String endTimeUTC = getElementContent(episodeElement,
                                                  "endtimeutc", 0);

            /* If episode is plaing inside given time span. */
            if (controller.checkTimeDate(startTimeUTC, endTimeUTC)) {
                RadioEpisodeInfo episodeInfo = new RadioEpisodeInfo(title);
                episodeInfo.setDescription(description);
                episodeInfo.setEndTimeUTC(endTimeUTC);
                episodeInfo.setStartTimeUTC(startTimeUTC);

                String imageURL;
                imageURL = getElementContent(episodeElement, "imageurl", 0);
                episodeInfo.setImageURL(imageURL);

                /* Add episode to channel's episode list. */
                channelInfo.addEpisodeInfo(episodeInfo);
            }
        }
    }

    /**
     * Gets text content from given element name inside a element if it's not
     * null.
     * @param el        Element - the element.
     * @param elName    String - element name.
     * @param index     int - element item index.
     * @return  String - the text content if exist, else null.
     */
    private String getElementContent(Element el, String elName, int index) {
        String elContent = null;
        if(el != null && el.getElementsByTagName(elName) != null &&
                el.getElementsByTagName(elName).item(index) != null) {
            elContent = el.getElementsByTagName(elName).
                    item(index).getTextContent();
        }
        return elContent;
    }
}
