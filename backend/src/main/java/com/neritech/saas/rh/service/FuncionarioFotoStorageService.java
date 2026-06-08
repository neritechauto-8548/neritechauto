package com.neritech.saas.rh.service;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FuncionarioFotoStorageService {
    private final Path baseDir;

    public FuncionarioFotoStorageService() {
        this.baseDir = Paths.get("uploads", "funcionarios", "fotos").toAbsolutePath().normalize();
        try { Files.createDirectories(this.baseDir); } catch (IOException e) { throw new RuntimeException(e); }
    }

    public String store(Long funcionarioId, MultipartFile file) {
        if (file == null || file.isEmpty()) throw new IllegalArgumentException("Arquivo vazio");
        String extension = getFileExtension(file.getOriginalFilename());
        String filename = "foto_" + funcionarioId + (extension.isEmpty() ? "" : "." + extension);
        Path target = baseDir.resolve(filename);
        try {
            Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar foto", e);
        }
        return target.toAbsolutePath().toString();
    }

    public Resource load(String absolutePath) {
        Path p = Paths.get(absolutePath);
        if (!Files.exists(p)) throw new IllegalArgumentException("Arquivo não encontrado");
        return new FileSystemResource(p.toFile());
    }

    private String getFileExtension(String filename) {
        if (filename == null) return "";
        int lastIndex = filename.lastIndexOf('.');
        return lastIndex == -1 ? "" : filename.substring(lastIndex + 1);
    }
}
