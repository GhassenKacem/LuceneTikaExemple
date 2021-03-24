package com.example.demo;


import com.example.demo.Indexing.Indexer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

@RestController
public class Controller {

    @Autowired
    Service service;

    Indexer indexer;

    @RequestMapping(value="/search",method= RequestMethod.GET)
    public void serachtest(@RequestParam(required = false) String searchParam ) {
        File files = new File("C:\\Users\\MSI\\Desktop\\workspace");


        service.RecursiveSearch(files.listFiles(), 0, 0, searchParam);
    }



    @RequestMapping(value="/searchTika",method= RequestMethod.GET)
    public ResponseEntity<?> serachTika(@RequestParam(required = false) String searchParam ) {
    try {

          String filename = "C:\\Users\\MSI\\Desktop\\IF5 PFE 2021 (1) (2).pdf";
          ClassLoader cl = getClass().getClassLoader();
        File file = new File("C:\\Users\\MSI\\Desktop\\IF5 PFE 2021 (1) (2).pdf");
        System.out.println(file.exists());
          return ResponseEntity.ok(service.contains(file, searchParam));
         }    catch (Exception e) {
             System.out.println(e);
            return  ResponseEntity.ok(e);
        }
    }


    @RequestMapping(value="/deleteFileIndex",method= RequestMethod.DELETE)
    public void serachtest(@RequestParam("file") MultipartFile file ) {
        try {
            String directoryString = "C:\\Users\\MSI\\Desktop\\lucene\\Data" + "\\" + file.getOriginalFilename();
            System.out.println(directoryString);
            File directory = new File(directoryString);
            service.deleteDocument(directory);
        } catch (IOException e) {
            System.out.println(e);
            e.printStackTrace();
        }
    }
}
