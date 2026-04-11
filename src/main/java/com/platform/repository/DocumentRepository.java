package com.platform.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.platform.model.enums.DocType;
import com.platform.model.enums.VerificationStatus;
import com.platform.model.support.Document;
import com.platform.model.user.Driver;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
    List<Document> findByDriver(Driver driver);
    List<Document> findByVerificationStatus(VerificationStatus status);
    Optional<Document> findByDriverAndDocType(Driver driver, DocType docType);
}