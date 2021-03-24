package com.example.demo;


import com.example.demo.Indexing.LuceneConstants;
import com.github.fge.largetext.LargeText;
import com.github.fge.largetext.LargeTextFactory;
import com.github.fge.largetext.SizeUnit;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;

import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MimeTypeException;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.SAXException;

@org.springframework.stereotype.Service
public class Service {




    public void Serachtest()
    {
        // Default factory
         LargeTextFactory factory = LargeTextFactory.defaultFactory();
         String Test  = "environ";
          Pattern PATTERN = Pattern.compile( "("+Test+")",
                Pattern.MULTILINE);

        final Path path = Paths.get("C:\\Users\\MSI\\Desktop\\workspace\\test1.txt");

        System.out.println(String.format(".*\\b%s\\b.*", Test));

        try {
             LargeText  largeText = factory.fromPath(path);
             Matcher m = PATTERN.matcher(largeText);

            while(m.find()) {
                System.out.println("found: " + m.group(1));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public  boolean contains(File file, String s) throws MalformedURLException,
            IOException, MimeTypeException, SAXException, TikaException
    {
        InputStream         stream    = new FileInputStream(file);
        AutoDetectParser    parser    = new AutoDetectParser();
        BodyContentHandler  handler   = new BodyContentHandler(-1);
        Metadata            metadata  = new Metadata();
        try{
            parser.parse(stream, handler, metadata);
            boolean Valid = handler.toString().toLowerCase().contains(s.toLowerCase());
            return Valid;
        }
        catch (IOException | SAXException | TikaException e){
            System.out.println(file + ": " + e + "\n");
            return false;
        }
    }


    public  String AnyFileToString(File file) throws MalformedURLException,
            IOException, MimeTypeException, SAXException, TikaException
    {
        InputStream         stream    = new FileInputStream(file);
        AutoDetectParser    parser    = new AutoDetectParser();
        BodyContentHandler  handler   = new BodyContentHandler(-1);
        Metadata            metadata  = new Metadata();
        try{
            parser.parse(stream, handler, metadata);
            return handler.toString().toLowerCase();
         }
        catch (IOException | SAXException | TikaException e){
            System.out.println(file + ": " + e + "\n");
            return e.getMessage();
        }
    }



      public void RecursiveSearch(File[] arr,int index,int level, String searchParam)
    {
        // terminate condition
        if(index == arr.length)
            return;

        // tabs for internal levels
        for (int i = 0; i < level; i++)
            System.out.print("\t");

        // for files
        if(arr[index].isFile()) {
             try {
                System.out.println(arr[index].getName()+": "+contains(arr[index], searchParam));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (TikaException e) {
                e.printStackTrace();
            }
        }

        // for sub-directories
        else if(arr[index].isDirectory())
        {
            System.out.println("[" + arr[index].getName() + "]");

            // recursion for sub-directories
            RecursiveSearch(arr[index].listFiles(), 0, level + 1, searchParam);
        }

        // recursion for main directory
        RecursiveSearch(arr,++index, level, searchParam);
    }



    public void deleteDocument(File file) throws IOException {
         IndexWriter writer = null;

        //delete indexes for a file
        writer.deleteDocuments(
                new Term(LuceneConstants.FILE_NAME,file.getName()));

        writer.commit();
    }



}
