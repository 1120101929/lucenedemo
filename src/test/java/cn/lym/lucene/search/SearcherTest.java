package cn.lym.lucene.search;

import org.apache.commons.io.IOUtils;
import org.apache.lucene.document.Document;
import org.junit.BeforeClass;
import org.junit.Test;

import java.nio.file.Paths;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by liuyimin01 on 2017/6/19.
 */
public class SearcherTest {
    private static Searcher searcher;

    @BeforeClass
    public static void setup() throws Exception {
        searcher = new Searcher(Paths.get("E:\\index"));
    }

    @Test
    public void testGetByFileName() throws Exception {
        String filename = "index.html";
        List<Document> documents = searcher.getByFileName(filename);
        System.out.println("size: " + documents.size());
        assertEquals(32, documents.size());
        for (Document document : documents) {
            System.out.println(document);
        }
    }

    @Test
    public void testGetBySize() throws Exception {
        long size = 2973L;
        List<Document> documents = searcher.getBySize(size);
        System.out.println("size: " + documents.size());
        for (Document document : documents) {
            System.out.println(document);
        }
    }

    @Test
    public void testGetBySize2() throws Exception {
        long minSize = 1024 * 1024L;
        long maxSize = 1024 * 1024 * 10L;
        List<Document> documents = searcher.getBySize(minSize, maxSize);
        System.out.println("size: " + documents.size());
        for (Document document : documents) {
            System.out.println(document);
        }
    }

    @Test
    public void testGetByType() throws Exception {
        String type = "jar";
        List<Document> documents = searcher.getByType(type);
        System.out.println("size: " + documents.size());
        for (Document document : documents) {
            System.out.println(document);
        }
    }

    @Test
    public void testGetByContent() throws Exception {
        String keyword = "lucene";
        List<Document> documents = searcher.getByContent(keyword);
        System.out.println("size: " + documents.size());
        for (Document document : documents) {
            System.out.println(document);
        }
    }

    public static void teardown() throws Exception {
        IOUtils.closeQuietly(searcher);
    }
}