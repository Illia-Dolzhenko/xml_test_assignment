package com.dolzhik.xml_test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;

import com.dolzhik.xml_test.service.XmlValidator;

@SpringBootTest
public class XmlValidatorTests {

    @Autowired
    XmlValidator validator;

    @Value("classpath:valid.xml")
    Resource validXml;
    @Value("classpath:valid2.xml")
    Resource validXml2;
    @Value("classpath:invalid.xml")
    Resource invalidXml;
    @Value("classpath:invalid2.xml")
    Resource invalidXml2;


    @Test
    void testValidXml_souldReturnTrue() throws IOException {
        var bytes = Files.readAllBytes(validXml.getFile().toPath());
        assertTrue(validator.isValid(bytes));
    }

    @Test
    void testValidXml2_souldReturnTrue() throws IOException {
        var bytes = Files.readAllBytes(validXml2.getFile().toPath());
        assertTrue(validator.isValid(bytes));
    }

    @Test
    void testInvalidXml_ShouldReturnFalse() throws IOException {
        var bytes = Files.readAllBytes(invalidXml.getFile().toPath());
        assertFalse(validator.isValid(bytes));
    }

    @Test
    void testInvalidAgainstSchema_ShouldReturnFalse() throws IOException {
        var bytes = Files.readAllBytes(invalidXml2.getFile().toPath());
        assertFalse(validator.isValid(bytes));
    }
}
