package com.dolzhik.xml_test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.dolzhik.xml_test.constants.Constants;
import com.dolzhik.xml_test.entity.DataBaseEntry;
import com.dolzhik.xml_test.repository.XmlRepository;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class XmlTestApplicationTests {

	@Autowired
	private MockMvc mvc;

	@MockBean
	XmlRepository repository;
	@Mock
	Page<DataBaseEntry> mockedPage;
	String validXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<epaperRequest>\n\t<deviceInfo name=\"Browser\" id=\"asd@asd\">\n\t\t<screenInfo width=\"1920\" height=\"1080\" dpi=\"300\" />\n\t\t<osInfo name=\"Browser\" version=\"2.0\" />\n\t\t<appInfo>\n\t\t\t<newspaperName>jdkjfhlsdkjflhaj</newspaperName>\n\t\t\t<version>1.5</version>\n\t\t</appInfo>\n\t</deviceInfo>\n\t<getPages editionDefId=\"11\" publicationDate=\"2007-01-02\" />\n</epaperRequest>";

	@BeforeAll
	void setup(){
		when(mockedPage.getNumber()).thenReturn(0);
		when(mockedPage.getTotalPages()).thenReturn(0);
		when(mockedPage.getContent()).thenReturn(new ArrayList<>());
	}

	@Test
	void uploadValidFile_ShouldReturnOk() throws Exception {
		mvc.perform(MockMvcRequestBuilders.multipart("/upload")
				.file(new MockMultipartFile("file", "test.xml", MediaType.APPLICATION_XHTML_XML_VALUE,
						validXml.getBytes()))
				.characterEncoding("UTF-8"))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	void uploadInvalidFile_ShouldReturnBadRequest() throws Exception {
		mvc.perform(MockMvcRequestBuilders.multipart("/upload")
				.file(new MockMultipartFile("file", "test.xml", MediaType.APPLICATION_XHTML_XML_VALUE,
						(validXml + ">").getBytes()))
				.characterEncoding("UTF-8"))
				.andExpect(MockMvcResultMatchers.status().isBadRequest());
	}

	@Test
	void uploadWrongFile_ShouldReturnBadRequest() throws Exception {
		mvc.perform(MockMvcRequestBuilders.multipart("/upload")
				.file(new MockMultipartFile("file", "aboba", MediaType.APPLICATION_XHTML_XML_VALUE,
						"invalid file".getBytes()))
				.characterEncoding("UTF-8"))
				.andExpect(MockMvcResultMatchers.status().isBadRequest());
	}

	@Test
	void uploadWithoutFileAttached_ShouldReturnBadRequest() throws Exception {
		mvc.perform(MockMvcRequestBuilders.multipart("/upload")
				.file(new MockMultipartFile("wrongName", "aboba", MediaType.APPLICATION_XHTML_XML_VALUE,
						"invalid file".getBytes()))
				.characterEncoding("UTF-8"))
				.andExpect(MockMvcResultMatchers.status().isBadRequest());
	}

	@Test
	void getAllFiles_shouldInvokeFindAll() throws Exception {
		when(repository.findAll(any(Pageable.class))).thenReturn(mockedPage);
		mvc.perform(MockMvcRequestBuilders.get("/files?page=0"))
				.andExpect(MockMvcResultMatchers.status().isOk());
		verify(repository).findAll(PageRequest.of(0, Constants.ENTRIES_PER_PAGE));
	}

	@Test
	void getAllFilesWithSort_shouldInvokeFindAll() throws Exception {
		when(repository.findAll(any(Pageable.class))).thenReturn(mockedPage);
		mvc.perform(MockMvcRequestBuilders.get("/files?page=0&sort=file_name"))
				.andExpect(MockMvcResultMatchers.status().isOk());
		verify(repository).findAll(PageRequest.of(0, Constants.ENTRIES_PER_PAGE, Sort.by("file_name")));
	}

	@Test
	void getFileWithId_shouldInvokeFindById() throws Exception {
		when(repository.findById(any(Long.class))).thenReturn(Optional.of(new DataBaseEntry()));
		mvc.perform(MockMvcRequestBuilders.get("/files/1?page=0"))
				.andExpect(MockMvcResultMatchers.status().isOk());
		verify(repository).findById(1L);
	}

	@Test
	void getFileWithId_shouldInvokeFindByIdAndReturn404() throws Exception {
		when(repository.findById(any(Long.class))).thenReturn(Optional.empty());
		mvc.perform(MockMvcRequestBuilders.get("/files/1?page=0"))
				.andExpect(MockMvcResultMatchers.status().isNotFound());
		verify(repository).findById(1L);
	}

	@Test
	void getFileWithFileName_shouldInvokeFindByFileName() throws Exception {
		when(repository.findByFileNameContaining(any(String.class), any(Pageable.class))).thenReturn(mockedPage);
		mvc.perform(MockMvcRequestBuilders.get("/files?page=0&fileName=test"))
				.andExpect(MockMvcResultMatchers.status().isOk());
		verify(repository).findByFileNameContaining("test", PageRequest.of(0, Constants.ENTRIES_PER_PAGE));
	}

	@Test
	void getFileWithFileNameWithSorting_shouldInvokeFindByFileName() throws Exception {
		when(repository.findByFileNameContaining(any(String.class), any(Pageable.class))).thenReturn(mockedPage);
		mvc.perform(MockMvcRequestBuilders.get("/files?page=0&fileName=test&sort=file_name"))
				.andExpect(MockMvcResultMatchers.status().isOk());
		verify(repository).findByFileNameContaining("test", PageRequest.of(0, Constants.ENTRIES_PER_PAGE, Sort.by("file_name")));
	}
}
