package com.sri.ai.aidemo.config;

import jakarta.annotation.PostConstruct;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class VectorInitializer {

    private final VectorStore vectorStore;

    public VectorInitializer(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    private List<Document> withStableIds(List<Document> docs, String source) {
        return docs.stream()
                .map(d -> {
                    String id = DigestUtils.md5DigestAsHex(
                            (source + "::" + d.getText()).getBytes(StandardCharsets.UTF_8));
                    return new Document(id, d.getText(), d.getMetadata());
                })
                .toList();
    }
    @PostConstruct
    public void init() {
        TokenTextSplitter tokenTextSplitter = TokenTextSplitter.builder()
                .withChunkSize(100)              // target chunk size (tokens)
                .withMinChunkSizeChars(100)      // min chunk size (characters)
                .withMinChunkLengthToEmbed(5)    // discard chunks shorter than this
                .withMaxNumChunks(1000)          // max chunks per document
                .withKeepSeparator(true)         // keep line breaks/separators
                .build();

        TextReader jobListReader = new TextReader(new ClassPathResource("job_listings.txt"));
        List<Document> documentList = tokenTextSplitter.split(jobListReader.get());
        vectorStore.add(withStableIds(documentList, "job_listings")); // using vectorStore interface it will inject the Vector Db and save the chunks


        TextReader productDataReader = new TextReader(new ClassPathResource("product-data.txt"));
        documentList.clear();
        documentList = tokenTextSplitter.split(productDataReader.get());
        vectorStore.add(withStableIds(documentList,"product-data"));
    }
}
