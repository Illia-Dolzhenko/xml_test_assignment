package com.dolzhik.xml_test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.dolzhik.xml_test.service.XmlParser;

@SpringBootTest
public class XmlParserTests {

    @Autowired
    XmlParser parser;
    String validXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<epaperRequest>\n\t<deviceInfo name=\"Browser\" id=\"asd@asd\">\n\t\t<screenInfo width=\"1920\" height=\"1080\" dpi=\"300\" />\n\t\t<osInfo name=\"Browser\" version=\"2.0\" />\n\t\t<appInfo>\n\t\t\t<newspaperName>jdkjfhlsdkjflhaj</newspaperName>\n\t\t\t<version>1.5</version>\n\t\t</appInfo>\n\t</deviceInfo>\n\t<getPages editionDefId=\"11\" publicationDate=\"2007-01-02\" />\n</epaperRequest>";

    @Test
    void parseValidXml_ShoudReturnDataBaseEntry() {
        var result = parser.parseXml(validXml.getBytes());
        assertTrue(result.isPresent());
        assertEquals(result.get().getDpi(), 300);
        assertEquals(result.get().getWidth(), 1920);
        assertEquals(result.get().getHeight(), 1080);
        assertEquals(result.get().getNewspaperName(), "jdkjfhlsdkjflhaj");
    }

    @Test
    void parseInvalidXml_ShoudReturnEmptyOptional() {
        var result = parser.parseXml((validXml + ">").getBytes());
        assertTrue(result.isEmpty());
    }
}
