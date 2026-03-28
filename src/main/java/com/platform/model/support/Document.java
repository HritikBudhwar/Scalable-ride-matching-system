package com.platform.model.support;

import com.platform.model.enums.DocType;
import com.platform.model.enums.VerificationStatus;

import java.time.LocalDateTime;

/**
 * Document entity class
 */
public class Document {
    private Long id;
    private DocType docType;
    private String documentUrl;
    private VerificationStatus verificationStatus;
    private LocalDateTime uploadedAt;
    private LocalDateTime verifiedAt;
    private String remarks;
}