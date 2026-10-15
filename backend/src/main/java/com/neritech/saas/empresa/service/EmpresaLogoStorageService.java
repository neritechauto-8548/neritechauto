package com.neritech.saas.empresa.service;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Locale;
import java.util.UUID;

/** Storage is private; only the tenant-authorized controller may serve images. */
@Service
public class EmpresaLogoStorageService {
    public static final int MAX_BYTES = 2 * 1024 * 1024;
    private final Path baseDir;

    public EmpresaLogoStorageService() {
        this.baseDir = Paths.get("uploads", "logos").toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.baseDir);
        } catch (IOException e) {
            throw new IllegalStateException("Não foi possível preparar o armazenamento de logos", e);
        }
    }

    public String store(Long empresaId, MultipartFile file) {
        if (file == null || file.isEmpty() || file.getSize() > MAX_BYTES) {
            throw invalidLogo();
        }
        try {
            byte[] bytes = file.getBytes();
            if (bytes.length == 0 || bytes.length > MAX_BYTES) throw invalidLogo();
            String extension = detectExtension(bytes);
            String declaredType = file.getContentType() == null ? "" :
                    file.getContentType().toLowerCase(Locale.ROOT);
            if (extension == null || !declaredType.equals(contentType(extension))) {
                throw invalidLogo();
            }
            Path target = baseDir.resolve("logo_" + empresaId + "_" + UUID.randomUUID() + "." + extension);
            Files.write(target, bytes, StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE);
            return target.toString();
        } catch (IOException e) {
            throw new IllegalStateException("Não foi possível armazenar a logomarca", e);
        }
    }

    public Resource load(String path) {
        Path target = Paths.get(path).toAbsolutePath().normalize();
        if (!target.startsWith(baseDir) ||
                !Files.isRegularFile(target, LinkOption.NOFOLLOW_LINKS)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Logomarca não encontrada");
        }
        return new FileSystemResource(target);
    }

    public String mediaType(String path) {
        String name = Paths.get(path).getFileName().toString().toLowerCase(Locale.ROOT);
        if (name.endsWith(".png")) return "image/png";
        if (name.endsWith(".jpg")) return "image/jpeg";
        if (name.endsWith(".webp")) return "image/webp";
        // Reject legacy active content (SVG, HTML) and unsupported formats.
        throw new ResponseStatusException(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Formato de logomarca não suportado");
    }

    private static String detectExtension(byte[] b) {
        if (b.length >= 8 && (b[0] & 0xff) == 0x89 && b[1] == 'P' &&
                b[2] == 'N' && b[3] == 'G' && (b[4] & 0xff) == 13 &&
                (b[5] & 0xff) == 10 && (b[6] & 0xff) == 26 && (b[7] & 0xff) == 10) {
            return "png";
        }
        if (b.length >= 3 && (b[0] & 0xff) == 0xff &&
                (b[1] & 0xff) == 0xd8 && (b[2] & 0xff) == 0xff) return "jpg";
        if (b.length >= 12 && b[0] == 'R' && b[1] == 'I' && b[2] == 'F' && b[3] == 'F' &&
                b[8] == 'W' && b[9] == 'E' && b[10] == 'B' && b[11] == 'P') return "webp";
        return null;
    }

    private static String contentType(String extension) {
        return switch (extension) {
            case "png" -> "image/png";
            case "jpg" -> "image/jpeg";
            case "webp" -> "image/webp";
            default -> "";
        };
    }

    private static ResponseStatusException invalidLogo() {
        return new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                "Envie uma imagem PNG, JPG ou WebP válida de até 2 MB");
    }
}
