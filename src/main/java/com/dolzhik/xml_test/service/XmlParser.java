package com.dolzhik.xml_test.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.dolzhik.xml_test.entity.DataBaseEntry;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Optional;

import javax.xml.XMLConstants;
import javax.xml.parsers.*;;

@Service
public class XmlParser {

    private DocumentBuilderFactory documentBuilderFactory;
    private DocumentBuilder documentBuilder;
    private Logger logger = LoggerFactory.getLogger(XmlParser.class);

    public XmlParser() {
        documentBuilderFactory = DocumentBuilderFactory.newInstance();
        try {
            documentBuilderFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            logger.error("Failed to set secure processing for DocumentBuilderFactory.", e);
        }
    }

    public Optional<DataBaseEntry> parseXml(byte[] xml) {
        DataBaseEntry dataBaseEntry = new DataBaseEntry();

        try {
            Document document = documentBuilder.parse(new ByteArrayInputStream(xml));
            document.getDocumentElement().normalize();
            NodeList nodes = document.getElementsByTagName("screenInfo");
            if (nodes.getLength() > 0) {
                Node node = nodes.item(0);
                var attributes = node.getAttributes();
                var width = attributes.getNamedItem("width");
                var height = attributes.getNamedItem("height");
                var dpi = attributes.getNamedItem("dpi");

                try {
                    dataBaseEntry.setDpi(Integer.parseInt(dpi.getTextContent()));
                    dataBaseEntry.setHeight(Integer.parseInt(height.getTextContent()));
                    dataBaseEntry.setWidth(Integer.parseInt(width.getTextContent()));
                } catch (NumberFormatException e) {
                    logger.error("Failed to parse XML attributes.", e);
                    return Optional.empty();
                }

            } else {
                logger.error("Failed to find screenInfo tag.");
                return Optional.empty();
            }

            nodes = document.getElementsByTagName("newspaperName");
            if (nodes.getLength() > 0) {
                Node node = nodes.item(0);
                dataBaseEntry.setNewspaperName(node.getTextContent());
            } else {
                logger.error("Failed to find newspaperName tag");
                return Optional.empty();
            }

        } catch (SAXException | IOException e) {
            logger.error("Failed to parse XML file.", e);
            return Optional.empty();
        }

        return Optional.of(dataBaseEntry);
    }
}
