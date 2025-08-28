package com.finsight.account_service.service;

import org.springframework.web.multipart.MultipartFile;

public interface CsvFileProcessingService {
     int processCsvFile(MultipartFile file, Long userId);
}
