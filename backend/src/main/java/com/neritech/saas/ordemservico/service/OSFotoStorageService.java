package com.neritech.saas.ordemservico.service;

import com.neritech.saas.common.tenancy.TenantContext;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.Normalizer;

@Service
public class OSFotoStorageService {
    private final Path baseDir;

    public OSFotoStorageService() {
        this.baseDir = Paths.get("uploads", "os-fotos").toAbsolutePath().normalize();
        try { Files.createDirectories(this.baseDir); } catch (IOException e) { throw new RuntimeException(e); }
    }

    public StorageInfo store(Long osId, MultipartFile file) {
        if (file == null || file.isEmpty()) throw new IllegalArgumentException("Arquivo vazio");
        Long tenantId = TenantContext.getCurrentTenant();
        String filename = sanitize(file.getOriginalFilename());
        Path dir = baseDir.resolve(String.valueOf(tenantId)).resolve(String.valueOf(osId));
        try { Files.createDirectories(dir); } catch (IOException e) { throw new RuntimeException(e); }
        Path target = dir.resolve(filename);
        try { Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING); } catch (IOException e) { throw new RuntimeException(e); }
        StorageInfo info = new StorageInfo();
        info.absolutePath = target.toAbsolutePath().toString();
        info.filename = filename;
        info.contentType = file.getContentType() != null ? file.getContentType() : "application/octet-stream";
        info.size = file.getSize();
        return info;
    }

    public Resource load(String absolutePath) {
        Path p = Paths.get(absolutePath);
        if (!Files.exists(p)) throw new IllegalArgumentException("Arquivo n\u00E3o encontrado");
        return new FileSystemResource(p.toFile());
    }

    private String sanitize(String name) {
        if (name == null || name.isBlank()) return "arquivo";
        String n = Normalizer.normalize(name, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
        n = n.replaceAll("[\\/]+", "_").replaceAll("[\r\n]", "").replaceAll("\s+", "_");
        return n;
    }

    public static class StorageInfo {
        public String absolutePath;
        public String filename;
        public String contentType;
        public long size;
    }
}
