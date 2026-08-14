package com.estatehub.backend.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.estatehub.backend.utils.AppBussinessException;

@Service
public class FileStorageService {

    private static final Set<String> IMAGE_TYPES = Set.of("image/jpeg", "image/png", "image/webp", "image/gif");
    private static final Set<String> DOCUMENT_TYPES = Set.of(
            "application/pdf", "application/msword",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
            "image/jpeg", "image/png", "image/webp");

    private final Path uploadRoot;

    public FileStorageService(@Value("${app.upload-dir}") String uploadDir) {
        this.uploadRoot = Path.of(uploadDir).toAbsolutePath().normalize();
        try {
            Files.createDirectories(uploadRoot);
        } catch (IOException ex) {
            throw new IllegalStateException("Could not create upload directory: " + uploadDir, ex);
        }
    }

    public String storeProfileImage(MultipartFile file) {
        return store(file, "profiles", IMAGE_TYPES, "Profile image");
    }

    public String storePropertyDocument(MultipartFile file) {
        return store(file, "documents", DOCUMENT_TYPES, "Legal document");
    }

    public String storePropertyImage(MultipartFile file) {
        return store(file, "properties", IMAGE_TYPES, "Property image");
    }

    private String store(MultipartFile file, String subDir, Set<String> allowedTypes, String label) {
        if (file == null || file.isEmpty()) {
            throw new AppBussinessException(label + " file is required.");
        }
        if (!allowedTypes.contains(file.getContentType())) {
            throw new AppBussinessException(label + " must be one of: " + String.join(", ", allowedTypes));
        }

        String original = StringUtils.cleanPath(file.getOriginalFilename() == null ? "" : file.getOriginalFilename());
        String extension = StringUtils.getFilenameExtension(original);
        String storedName = UUID.randomUUID().toString().replace("-", "")
                + (extension != null && !extension.isBlank() ? "." + extension.toLowerCase() : "");

        Path targetDir = uploadRoot.resolve(subDir);
        try {
            Files.createDirectories(targetDir);
            Path target = targetDir.resolve(storedName).normalize();
            if (!target.startsWith(uploadRoot)) {
                throw new AppBussinessException("Invalid upload path.");
            }
            Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            throw new AppBussinessException("Could not store the uploaded file. Please try again.");
        }

        return "/uploads/" + subDir + "/" + storedName;
    }

    public void deleteIfExists(String url) {
        if (url == null || !url.startsWith("/uploads/")) {
            return;
        }
        Path resolved = uploadRoot.resolve(url.substring("/uploads/".length())).normalize();
        if (!resolved.startsWith(uploadRoot)) {
            return;
        }
        try {
            Files.deleteIfExists(resolved);
        } catch (IOException ignored) {
        }
    }
}