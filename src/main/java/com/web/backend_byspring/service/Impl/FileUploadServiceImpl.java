package com.web.backend_byspring.service.Impl;

import com.web.backend_byspring.service.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;;
import java.util.Base64;
import java.util.UUID;
@Service
@RequiredArgsConstructor
public class FileUploadServiceImpl implements FileUploadService {

    @Value("${project.upload}")
    private String path;

    @Value("${base.url}")
    private String baseUrl;

    @Override
    public byte[] getFileName(String fileName) {
        try {
            Path filename = Paths.get(path, fileName); // Use 'path' from @Value
            return Files.readAllBytes(filename);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String saveFile(MultipartFile file, String directoryPath) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File is empty or null");
        }

        try {
            // Ensure the directory exists
            Path dirPath = Paths.get(directoryPath);
            Files.createDirectories(dirPath);

            String originalFileName = file.getOriginalFilename();
            if (originalFileName == null) {
                throw new IllegalArgumentException("Original file name is null");
            }

            String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
            String newFileName = UUID.randomUUID().toString() + fileExtension;

            Path filePath = dirPath.resolve(newFileName);
            Files.copy(file.getInputStream(), filePath);

            return newFileName;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to save file", e);
        }
    }

    @Override
    public String saveBase64Image(String directoryPath, String base64Image) {
        try {
            byte[] decodedImage = Base64.getDecoder().decode(base64Image);
            String uniqueId = UUID.randomUUID().toString();
            String newFileName = uniqueId + ".png";

            Path dirPath = Paths.get(directoryPath);
            Files.createDirectories(dirPath);

            Path filePath = dirPath.resolve(newFileName);
            Files.write(filePath, decodedImage);

            return newFileName;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to save Base64 image", e);
        }
    }

////    @Override
////    public byte[] getFileName(String fileName) {
////        try {
////            Path filename = Paths.get("upload", fileName);
////            return Files.readAllBytes(filename);
////        } catch (Exception e) {
////            e.printStackTrace();
////            return null;
////        }
////    }
//
//    @Override
//    public byte[] getFileName(String fileName) {
//        try {
//            Path filename = Paths.get("upload", fileName);
//            return Files.readAllBytes(filename);
//        } catch (NoSuchFileException e) {
//            try {
//                Path defaultFilePath = Paths.get("upload", "https://via.placeholder.com/150");
//                return Files.readAllBytes(defaultFilePath);
//            } catch (IOException ex) {
//                ex.printStackTrace();
//                throw new RuntimeException("Default file is not available", ex);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new RuntimeException("An unexpected error occurred while retrieving the file", e);
//        }
//    }
//
//
//    @Override
//    public List<FileRespones> findAll() {
//        List<FileRespones> fileDtoList = new ArrayList<>();
//        Path directoryPath = Paths.get(path);
//        try (Stream<Path> paths = Files.list(directoryPath)) {
//            List<Path> pathList = paths.toList();
//
//            for (Path p : pathList) {
//                Resource resource = new UrlResource(p.toUri());
//
//                fileDtoList.add(FileRespones.builder()
//                        .name(resource.getFilename())
//                        .uri(baseUrl + "/api/v1/upload/" + resource.getFilename())
//                        .downloadUri(baseUrl + "/api/v1/upload/" + resource.getFilename())
//                        .extension(this.getExtension(resource.getFilename()))
//                        .size(resource.contentLength())
//                        .build());
//            }
//
//            return fileDtoList;
//
//        } catch (IOException e) {
//
//            throw new RuntimeException("Failed to retrieve files", e);
//        }
//    }
//
//    @Override
//    public String saveBase64Image(String path, byte[] base64Image) {
//        return "";
//    }
//
//    @Override
//    public FileRespones uploadSingle(MultipartFile file, String path) {
//        try {
//            return this.save(file, path);
//        } catch (IOException e) {
//            throw new RuntimeException("Failed to upload file: " + file.getOriginalFilename(), e);
//        }
//    }
//
//    @Override
//    public List<FileRespones> uploadMultiple(List<MultipartFile> files, String path) {
//        return files.stream().map(file -> {
//            try {
//                return save(file, path);
//            } catch (IOException e) {
//                throw new RuntimeException("Failed to upload file: " + file.getOriginalFilename(), e);
//            }
//        }).collect(Collectors.toList());
//    }
//
//    private FileRespones save(MultipartFile file, String path) throws IOException {
//        String extension = getExtension(file.getOriginalFilename());
//        String name = UUID.randomUUID() + "." + extension;
//        String uri = baseUrl + "/api/v1/upload/" + name;
//        Long size = file.getSize();
//
//        Path filePath = Paths.get(path, name);
//
//        try {
//            Files.copy(file.getInputStream(), filePath);
//        } catch (IOException e) {
//            throw new IOException("Could not save file: " + file.getOriginalFilename(), e);
//        }
//
//        return FileRespones.builder().name(name).uri(uri).downloadUri(filePath.toString()).extension(extension)
//                .size(size).build();
//    }
//
//    private String getExtension(String fileName) {
//        int lastDotIndex = fileName.lastIndexOf(".");
//        return lastDotIndex != -1 ? fileName.substring(lastDotIndex + 1) : "";
//    }
//
//    @Override
//    public void deleteAll() {
//
//    }
//
//    @Override
//    public void deleteByName(String fileName) {
//
//    }
//
//    @Override
//    public Resource downloadByName(String fileName) {
//        return null;
//    }
}
