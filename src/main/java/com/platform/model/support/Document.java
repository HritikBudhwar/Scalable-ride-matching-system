package com.platform.model.support;

import com.platform.model.enums.DocType;
import com.platform.model.enums.VerificationStatus;
import com.platform.model.user.Driver;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Driver verification document (DL, RC, Insurance).
 * Status managed by Administrator during onboarding.
 */
@Entity
@Table(name = "documents")
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_id", nullable = false)
    private Driver driver;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DocType docType;

    @Enumerated(EnumType.STRING)
    private VerificationStatus verificationStatus = VerificationStatus.PENDING;

    private String fileUrl;     // path/URL to uploaded file

    private LocalDateTime uploadedAt;
    private LocalDateTime reviewedAt;

    @PrePersist
    protected void onCreate() { uploadedAt = LocalDateTime.now(); }

    public void approve() {
        this.verificationStatus = VerificationStatus.APPROVED;
        this.reviewedAt = LocalDateTime.now();
    }

    public void reject() {
        this.verificationStatus = VerificationStatus.REJECTED;
        this.reviewedAt = LocalDateTime.now();
    }

    // --- Getters and Setters ---

    public Long getId() { return id; }

    public Driver getDriver() { return driver; }
    public void setDriver(Driver driver) { this.driver = driver; }

    public DocType getDocType() { return docType; }
    public void setDocType(DocType docType) { this.docType = docType; }

    public VerificationStatus getVerificationStatus() { return verificationStatus; }

    public String getFileUrl() { return fileUrl; }
    public void setFileUrl(String fileUrl) { this.fileUrl = fileUrl; }

    public LocalDateTime getUploadedAt() { return uploadedAt; }
    public LocalDateTime getReviewedAt() { return reviewedAt; }
}