package cn.lym.lucene.index;

import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Paths;

import static org.junit.Assert.*;

/**
 * Created by liuyimin01 on 2017/6/15.
 */
public class IndexerTest {
    private Indexer indexer;

    @Before
    public void setup() throws Exception {
        this.indexer = new Indexer(Paths.get("E:\\index"));
    }

    @Test
    public void index() throws Exception {
        this.indexer.index(Paths.get("E:\\lucene-6.6.0"));
    }

    @After
    public void teardown() throws Exception {
        IOUtils.closeQuietly(this.indexer);
    }
}