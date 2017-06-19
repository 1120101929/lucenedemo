package cn.lym.lucene.search;

import org.apache.commons.io.IOUtils;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.LongPoint;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.Closeable;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by liuyimin01 on 2017/6/19.
 */
public class Searcher implements Closeable {
    private Directory directory;
    private IndexReader reader;
    private IndexSearcher searcher;

    public Searcher(Path indexPath) throws IOException {
        this.directory = FSDirectory.open(indexPath);
        this.reader = DirectoryReader.open(this.directory);
        this.searcher = new IndexSearcher(this.reader);
    }

    public List<Document> getByFileName(String filename) throws IOException {
        Query query = new TermQuery(new Term("filename", filename));
        return doSearch(query);
    }

    public List<Document> getBySize(long size) throws IOException {
        Query query = LongPoint.newExactQuery("size", size);
        return doSearch(query);
    }

    public List<Document> getBySize(long minSize, long maxSize) throws IOException {
        Query query = LongPoint.newRangeQuery("size", minSize, maxSize);
        return doSearch(query);
    }

    public List<Document> getByType(String type) throws IOException {
        Query query = new TermQuery(new Term("type", type));
        return doSearch(query);
    }

    public List<Document> getByContent(String keyword) throws Exception {
        Query query = new TermQuery(new Term("content", keyword));
        return doSearch(query);
    }

    private List<Document> doSearch(Query query) throws IOException {
        TopDocs topDocs = this.searcher.search(query, Integer.MAX_VALUE);
        if (topDocs != null) {
            ScoreDoc[] scoreDocs = topDocs.scoreDocs;
            List<Document> documents = new ArrayList<>(scoreDocs.length);
            for (ScoreDoc scoreDoc : scoreDocs) {
                documents.add(this.searcher.doc(scoreDoc.doc));
            }
            return documents;
        }
        return Collections.emptyList();
    }

    @Override
    public void close() {
        IOUtils.closeQuietly(this.reader, this.directory);
    }
}
