package com.dolzhik.xml_test.entity;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;

public class Response {
    private List<DataBaseEntry> entries;
    private int currentPage;
    private int totalPages;

    public Response(Page<DataBaseEntry> page) {
        this.currentPage = page.getNumber();
        this.totalPages = page.getTotalPages();
        this.entries = page.getContent();
    }

    public Response() {
        this.currentPage = 0;
        this.totalPages = 0;
        this.entries = new ArrayList<DataBaseEntry>();
    }

    public void setEntries(List<DataBaseEntry> entries) {
        this.entries = entries;
    }

    public List<DataBaseEntry> getEntries() {
        return entries;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getTotalPages() {
        return totalPages;
    }
}
