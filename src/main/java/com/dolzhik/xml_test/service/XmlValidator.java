package com.dolzhik.xml_test.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

@Service
public class XmlValidator {

    private Validator validator;
    private Logger logger = LoggerFactory.getLogger(XmlValidator.class);

    public XmlValidator(@Value("classpath:schema.xsd") Resource schemaResource) throws IOException, SAXException {
        SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Source schemaFile = new StreamSource(schemaResource.getFile());
        Schema schema = factory.newSchema(schemaFile);
        validator = schema.newValidator();
    }

    public boolean isValid(byte[] xml) {
        try {
            validator.validate(new StreamSource(new ByteArrayInputStream(xml)));
            return true;
        } catch (SAXException | IOException e) {
            logger.error("Invalid XML.", e);
            return false;
        }
    }
}
