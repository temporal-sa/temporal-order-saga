package io.temporal.samples.batchprocessing;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class BatchParentWorkflowParams {
    private int numWords;
    private int offset;

    @JsonCreator
    public BatchParentWorkflowParams(@JsonProperty("numWords") int numWords,
                                     @JsonProperty("offset") int offset) {
        this.numWords = numWords;
        this.offset = offset;
    }

    public int getNumWords() {
        return numWords;
    }

    public void setNumWords(int numWords) {
        this.numWords = numWords;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }
}
