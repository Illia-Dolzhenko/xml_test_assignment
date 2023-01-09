package com.dolzhik.xml_test.controller;

import java.io.IOException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.dolzhik.xml_test.constants.Constants;
import com.dolzhik.xml_test.entity.Response;
import com.dolzhik.xml_test.repository.XmlRepository;
import com.dolzhik.xml_test.service.XmlParser;
import com.dolzhik.xml_test.service.XmlValidator;

@RestController
public class FileController {

    private Logger logger = LoggerFactory.getLogger(FileController.class);

    @Autowired
    XmlValidator validator;
    @Autowired
    XmlParser parser;
    @Autowired
    XmlRepository repository;

    @PostMapping("/upload")
    ResponseEntity<String> fileUpload(@RequestPart("file") MultipartFile file) {
        try {
            if (validator.isValid(file.getBytes())) {
                var optional = parser.parseXml(file.getBytes());

                if (optional.isPresent()) {
                    var entry = optional.get();
                    entry.setFileName(file.getOriginalFilename());
                    repository.save(entry);
                    logger.info("File recieved: {}", entry);
                } else {
                    return ResponseEntity.badRequest().body("Failed to parse XML file.");
                }
            } else {
                return ResponseEntity.badRequest().body("Invalid XML.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Failed to validate XML file.");
        }
        return ResponseEntity.ok("File saved.");
    }

    @GetMapping(value = { "/files/{id}", "/files" })
    ResponseEntity<Response> getFiles(@RequestParam("sort") Optional<String> sortBy, @RequestParam("page") Integer page,
            @RequestParam("fileName") Optional<String> fileName, @PathVariable("id") Optional<Long> id) {
        var request = PageRequest.of(page, Constants.ENTRIES_PER_PAGE);

        if (sortBy.isPresent()) {
            request = request.withSort(Sort.by(sortBy.get()));
        }

        if (id.isPresent()) {
            var result = repository.findById(id.get());
            if (result.isPresent()) {
                Response response = new Response();
                response.getEntries().add(result.get());
                return ResponseEntity.ok(response);
            } else {
                logger.info("Failed to find entry with id: {}", id.get());
                return ResponseEntity.notFound().build();
            }
        }

        if (fileName.isPresent()) {
            var results = repository.findByFileNameContaining(fileName.get(), request);
            Response response = new Response(results);
            return ResponseEntity.ok(response);
        }

        var entiresPaged = repository.findAll(request);
        Response response = new Response(entiresPaged);
        return ResponseEntity.ok(response);
    }
}
