package com.dolzhik.xml_test.entity;

import java.sql.Timestamp;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class DataBaseEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false)
    private String newspaperName;
    @Column(nullable = false)
    private int width;
    @Column(nullable = false)
    private int height;
    @Column(nullable = false)
    private int dpi;
    @Column(nullable = false)
    private String fileName;
    @CreationTimestamp
    @Column
    private Timestamp uploadTime;

    public void setNewspaperName(String newspaperName) {
        this.newspaperName = newspaperName;
    }

    public String getNewspaperName() {
        return newspaperName;
    }

    public void setDpi(int dpi) {
        this.dpi = dpi;
    }

    public int getDpi() {
        return dpi;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getHeight() {
        return height;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getWidth() {
        return width;
    }

    @Override
    public String toString() {
        return String.format("{fileName: %s, newspaperName: %s, dpi: %d, height: %d, width: %d}", fileName,
                newspaperName, dpi, height, width);
    }
}
