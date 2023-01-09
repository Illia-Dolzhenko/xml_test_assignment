package com.dolzhik.xml_test.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.dolzhik.xml_test.entity.DataBaseEntry;

public interface XmlRepository extends JpaRepository<DataBaseEntry, Long> {
    public Page<DataBaseEntry> findByFileNameContaining(String fileName, Pageable pageable);
    public Page<DataBaseEntry> findById(Long id, Pageable pageable);
}
