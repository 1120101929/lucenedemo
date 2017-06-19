package cn.lym.lucene.index;

import org.apache.commons.io.IOUtils;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.Closeable;
import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Created by liuyimin01 on 2017/6/15.
 */
public class Indexer implements Closeable {
    private Directory directory;
    private IndexWriter writer;

    public Indexer(Path indexPath) throws IOException {
        if (Files.notExists(indexPath)) {
            Files.createDirectories(indexPath);
        }

        this.directory = FSDirectory.open(indexPath);
        IndexWriterConfig config = new IndexWriterConfig();
        this.writer = new IndexWriter(this.directory, config);
    }

    public void index(Path pathToIndex) throws IOException {
        Files.walk(pathToIndex, FileVisitOption.FOLLOW_LINKS).forEach((path) -> {
            if (Files.isRegularFile(path)) {
                System.out.println("indexing " + path.toString() + " ...");

                Document document = new Document();
                //文件名
                document.add(new StringField("filename", path.getFileName().toString(), Field.Store.YES));
                try {
                    //文件大小
                    document.add(new LongPoint("size", Files.size(path)));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    //修改时间
                    document.add(new LongPoint("modified", Files.getLastModifiedTime(path).toMillis()));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                String type = getFileType(path);
                //文件类型
                document.add(new StringField("type", type, Field.Store.YES));

                if (isJavaFile(type) || isTextFile(type)) {
                    try {
                        //文件内容
                        document.add(new TextField("content", Files.newBufferedReader(path)));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                try {
                    this.writer.addDocument(document);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        this.writer.commit();

        System.out.println("done.");
    }

    private boolean isTextFile(String type) {
        return "txt".equalsIgnoreCase(type);
    }

    /**
     * 判断是否是java文件
     *
     * @param type
     * @return
     */
    private boolean isJavaFile(String type) {
        return "java".equalsIgnoreCase(type);
    }

    /**
     * 获得文件类型
     *
     * @param path
     * @return
     */
    private String getFileType(Path path) {
        String filename = path.getFileName().toString();
        int index = filename.lastIndexOf(".");
        if (index == -1) {
            return filename;
        }
        return filename.substring(index + 1);
    }

    @Override
    public void close() {
        IOUtils.closeQuietly(this.writer, this.directory);
    }
}
