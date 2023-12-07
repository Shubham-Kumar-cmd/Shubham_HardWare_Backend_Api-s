package com.shubham.hardware.services.impl;

import com.shubham.hardware.exceptions.BadApiRequestException;
import com.shubham.hardware.services.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    private Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    @Override
    public String uploadFile(MultipartFile file, String path) throws IOException {
//        abc.png
        String originalFileName = file.getOriginalFilename();
        logger.info("Filename : {} ",originalFileName);
        String filename= UUID.randomUUID().toString();
        assert originalFileName != null;
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String filaNameWithExtension=filename + extension; // 5a1012eb-fa97-4479-bcca-e5b66d70ef98.png
        String fullPathWithFileName = path+filaNameWithExtension;
        logger.info("Full image path : {}",fullPathWithFileName);
        if (extension.equalsIgnoreCase(".png") || extension.equalsIgnoreCase(".jpeg") || extension.equalsIgnoreCase(".jpg")){

//            file save
            logger.info("File extension : {}",extension);
            File folder=new File(path);
            if (!folder.exists()){
//                create the folder
                folder.mkdirs();
            }
//            upload
            Files.copy(file.getInputStream(), Paths.get(fullPathWithFileName));
            return filaNameWithExtension;
        }else {
            throw new BadApiRequestException("File with this "+extension+" not allowed!!");
        }
    }

    @Override
    public InputStream getResource(String path, String name) throws FileNotFoundException {
        String fullPath=path+File.separator+name;
        InputStream inputStream=new FileInputStream(fullPath);
        return inputStream;
    }
}
