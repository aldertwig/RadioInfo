package src;

import org.junit.Test;

import static org.junit.Assert.*;

public class RadioInfoXMLParserTest {
    String address = "xmlAddress";
    RadioInfoXMLParser xmlParser = new RadioInfoXMLParser(address);

    @Test
    public void createXMLParserTest() {
        assertNotNull(xmlParser);
    }

    @Test
    public void getXmlAddressTest() throws Exception {
        assertEquals(address, xmlParser.getXmlAddress());
    }
}