package com.finsight.account_service.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileProcessingService {
     int processCsvFile(MultipartFile file, Long userId);
}
