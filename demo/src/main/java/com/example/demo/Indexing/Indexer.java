package com.example.demo.Indexing;

import java.io.*;
import java.net.MalformedURLException;
import java.nio.file.Paths;
import java.text.ParseException;

import com.example.demo.Service;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MimeTypeException;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.sax.BodyContentHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.xml.sax.SAXException;


public class Indexer {




    private IndexWriter writer;

    public Indexer(String indexDirectoryPath) throws IOException {
        Directory indexDirectory =
                FSDirectory.open(Paths.get(indexDirectoryPath));

        StandardAnalyzer analyzer = new StandardAnalyzer();
        IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
        writer = new IndexWriter(indexDirectory, iwc);
    }

    public void close() throws CorruptIndexException, IOException {
        writer.close();
    }

    private Document getDocument(File file) throws IOException {
        Document document = new Document();

        TextField contentField = null;

        try {
              contentField = new TextField(LuceneConstants.CONTENTS, new StringReader(AnyFileToString(file)));

        } catch (SAXException e) {
            e.printStackTrace();
        } catch (TikaException e) {
            e.printStackTrace();
        }


        TextField fileNameField = new TextField(LuceneConstants.FILE_NAME,
                file.getName(),TextField.Store.YES);
        TextField filePathField = new TextField(LuceneConstants.FILE_PATH,
                file.getCanonicalPath(),TextField.Store.YES);

        document.add(contentField);
        document.add(fileNameField);
        document.add(filePathField);

        return document;
    }


    public void deleteDocument(File file) throws IOException {


        System.out.println(file.getName().split("[.]", 2)[0]);
        System.out.println(file.getCanonicalPath());
        //delete indexes for a file
        writer.deleteDocuments(
                new Term(LuceneConstants.FILE_PATH,file.getCanonicalPath()));

        writer.commit();
        System.out.println("index contains deleted files: "+writer.hasDeletions());
        //System.out.println("index contains documents: "+writer.maxDoc());
        //System.out.println("index contains deleted documents: "+writer.numDocs());
    }


    private void updateDocument(File file) throws IOException {
        Document document = getDocument(file);

        //update indexes for file contents
        writer.updateDocument(
                new Term(LuceneConstants.FILE_NAME,
                        file.getName().split("[.]", 2)[0]),document);
        writer.commit();
    }


    private void indexFile(File file) throws IOException {
         /* System.out.println("Indexing "+file.getCanonicalPath());
        Document document = getDocument(file);
        writer.addDocument(document);*/
        System.out.println("Deleting index: "+file.getCanonicalPath());
         deleteDocument(file);
        /*System.out.println("Updating index: "+file.getCanonicalPath());
        updateDocument(file);*/
    }

    public int createIndex(String dataDirPath, FileFilter filter)
            throws IOException {
        File[] files = new File(dataDirPath).listFiles();
        //writer.deleteAll();


        for (File file : files) {
            if(!file.isDirectory()
                    && !file.isHidden()
                    && file.exists()
                    && file.canRead()
                    && filter.accept(file)
            ){
 
                indexFile(file);
            }
        }
        return writer.numDocs();
    }





    public  String AnyFileToString(File file) throws MalformedURLException,
            IOException, MimeTypeException, SAXException, TikaException
    {
        InputStream stream    = new FileInputStream(file);
        InputStreamReader StreamReader = new InputStreamReader(stream);
        AutoDetectParser parser    = new AutoDetectParser();
        BodyContentHandler handler   = new BodyContentHandler(-1);
        Metadata metadata  = new Metadata();
        try{
            parser.parse(stream, handler, metadata);
            return handler.toString().toLowerCase();
        }
        catch (IOException | SAXException | TikaException e){
            System.out.println(file + ": " + e + "\n");
            return e.getMessage();
        }
    }



}
