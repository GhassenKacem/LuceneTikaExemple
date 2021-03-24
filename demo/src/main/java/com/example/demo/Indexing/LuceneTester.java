package com.example.demo.Indexing;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;

public class LuceneTester {

    String indexDir = "C:\\Users\\MSI\\Desktop\\lucene\\Index";
    String dataDir = "C:\\Users\\MSI\\Desktop\\lucene\\Data";
    Indexer indexer;
    Searcher searcher;

    public static void main(String[] args) {
        LuceneTester tester;
        try {
            tester = new LuceneTester();
            tester.createIndex();
            tester.search("ghassen");

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void createIndex() throws IOException {
        indexer = new Indexer(indexDir);
        int numIndexed;
        long startTime = System.currentTimeMillis();
        numIndexed = indexer.createIndex(dataDir, new TextFileFilter());
        long endTime = System.currentTimeMillis();
        indexer.close();
        System.out.println(numIndexed+" File indexed, time taken: "
                +(endTime-startTime)+" ms");
    }


    private void DeleteIndex() throws IOException {
        indexer = new Indexer(indexDir);

        long startTime = System.currentTimeMillis();
        File file = new File(dataDir + "\\test2.txt");
        System.out.println(file.getName());
         indexer.deleteDocument(file);
        long endTime = System.currentTimeMillis();
        indexer.close();
        System.out.println(" File indexed, time taken: "
                +(endTime-startTime)+" ms");
    }

    private void search(String searchQuery) throws IOException, ParseException {
        searcher = new Searcher(indexDir);
        long startTime = System.currentTimeMillis();
        TopDocs hits = searcher.search(searchQuery);
        long endTime = System.currentTimeMillis();

        System.out.println(hits.totalHits +
                " documents found. Time :" + (endTime - startTime));
        for(ScoreDoc scoreDoc : hits.scoreDocs) {
            Document doc = searcher.getDocument(scoreDoc);
            System.out.println("File: "
                    + doc.get(LuceneConstants.FILE_PATH));
        }
    }

    private void Check() throws IOException, ParseException {

    }



}
