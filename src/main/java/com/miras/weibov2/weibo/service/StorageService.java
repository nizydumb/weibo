package com.miras.weibov2.weibo.service;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;


import javax.validation.constraints.Min;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service
public class StorageService {

//        private final Path root = Paths.get("uploads");
//
//
//
////        public void init() {
////            try {
////                Files.createDirectory(root);
////            } catch (IOException e) {
////                throw new RuntimeException("Could not initialize folder for upload!");
////            }
////        }
//
//
//        public void save(MultipartFile file, String folderPath) {
//            try {
////                Path path = Path.of("C:/Users/nizzy/Documents/Posts/5");
//                Path path = Path.of(folderPath);
//                try {
//                    Files.createDirectory(path);
//                }catch (FileAlreadyExistsException e){
//                    throw new RuntimeException("Folder with post Id " + folderPath + " already exists");
//                }
//                Files.copy(file.getInputStream(),path.resolve(file.getOriginalFilename()));
//            } catch (Exception e) {
//                throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
//            }
//        }
//
//
//        public Resource load(String filename) {
//            try {
//                Path file = root.resolve(filename);
//                Resource resource = new UrlResource(file.toUri());
//
//                if (resource.exists() || resource.isReadable()) {
//                    return resource;
//                } else {
//                    throw new RuntimeException("Could not read the file!");
//                }
//            } catch (MalformedURLException e) {
//                throw new RuntimeException("Error: " + e.getMessage());
//            }
//        }
//
//
//
//        public void deleteAll() {
//            FileSystemUtils.deleteRecursively(root.toFile());
//        }
//
//
//        public Stream<Path> loadAll() {
//            try {
//                return Files.walk(this.root, 1).filter(path -> !path.equals(this.root)).map(this.root::relativize);
//            } catch (IOException e) {
//                throw new RuntimeException("Could not load the files!");
//            }
//        }

//
//
//
////        public void init() {
////            try {
////                Files.createDirectory(root);
////            } catch (IOException e) {
////                throw new RuntimeException("Could not initialize folder for upload!");
////            }
////        }
////

    private final String folderPath = "C:/Users/nizzy/Documents/Posts/";


    public void save(MultipartFile file, long postId) {
        try {
            Path path = Path.of(folderPath + postId);
            try {
                Files.createDirectory(path);
            } catch (FileAlreadyExistsException e) {
                //throw new RuntimeException("Folder with post Id " + folderPath + postId + " already exists");
            }
            Files.copy(file.getInputStream(), path.resolve(file.getOriginalFilename()));
        } catch (Exception e) {
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }
    }


    public Resource load(String filename, long postId) {
        try {
            Path root = Path.of(folderPath + postId);
            Path file = root.resolve(filename);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }
//    public Resource load(long postId, long imageId) {
//        Resource resource = null;
//        try {
//            Path root = Path.of(folderPath + postId);
//            //Path file = root.resolve(filename);
//            List<Path> pathList = loadAll(postId).collect(Collectors.toList());
//            resource = new UrlResource(root.resolve(pathList.get((int) imageId - 1 )).toUri());
//
//            boolean exists = resource.exists();
//            boolean isReadable = resource.isReadable();
//            if (exists || isReadable) {
//                return resource;
//            } else {
//                throw new RuntimeException("Could not read the file!");
//            }
//        } catch (MalformedURLException e) {
//            throw new RuntimeException("Error: " + e.getMessage());
//        } catch (RuntimeException e){
//
//        }
//        return resource;
//    }
    public Resource load(long postId, long imageId) {
        Resource resource = null;
        try {
            Path root = Path.of(folderPath + postId);
            Path path = loadAllv2(postId, imageId);
            resource = new UrlResource(root.resolve(path).toUri());

            boolean exists = resource.exists();
            boolean isReadable = resource.isReadable();
            if (exists || isReadable) {
                return resource;
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        } catch (RuntimeException e){

        }
        return resource;
    }


    public void deleteAll(long postId) {
        Path root = Path.of(folderPath + postId);
        FileSystemUtils.deleteRecursively(root.toFile());
    }


    public Stream<Path> loadAll(long postId) {
        Path root = Path.of(folderPath + postId);
        try {
            return Files.walk(root, 1).filter(path -> !path.equals(root)).map(root::relativize);
        } catch (IOException e) {
            throw new RuntimeException("Could not load the files!");
        }
    }
    public Path loadAllv2(long postId,long i) {
        Path root = Path.of(folderPath + postId);
        try {
//            return Files.walk(root, 1).filter(path -> !path.equals(root)).map(root::relativize);
            if(i == 0) throw  new RuntimeException();
            return Files.walk(root,1).skip(i).findFirst().get();
        } catch (IOException e) {
            throw new RuntimeException("Could not load the files!");
        }
    }
}


