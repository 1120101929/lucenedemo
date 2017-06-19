package cn.lym.lucene.index;

import org.apache.commons.io.IOUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.nio.file.Paths;

/**
 * Created by liuyimin01 on 2017/6/15.
 */
public class IndexerTest {
    private static Indexer indexer;

    @BeforeClass
    public static void setup() throws Exception {
        indexer = new Indexer(Paths.get("E:\\index"));
    }

    @Test
    public void index() throws Exception {
        indexer.index(Paths.get("E:\\lucene-6.6.0"));
    }

    @AfterClass
    public static void teardown() throws Exception {
        IOUtils.closeQuietly(indexer);
    }
}