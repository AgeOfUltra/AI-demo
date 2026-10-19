package com.sri.ai.aidemo.config;

import jakarta.annotation.PostConstruct;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class VectorInitializer {

    private final VectorStore vectorStore;

    public VectorInitializer(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
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
        vectorStore.add(documentList); // using vectorStore interface it will inject the Vector Db and save the chunks

        documentList.clear();
        TextReader productDataReader = new TextReader(new ClassPathResource("product-data.txt"));
        documentList = tokenTextSplitter.split(productDataReader.get());
        vectorStore.add(documentList);
    }
}
