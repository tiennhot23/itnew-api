package com.example.itnews.service;

import com.google.api.services.drive.model.File;

public interface DriveService {
    File uploadFile(String fileName, String filePath, String mimeType);
    void removeById(String fileId);
}
