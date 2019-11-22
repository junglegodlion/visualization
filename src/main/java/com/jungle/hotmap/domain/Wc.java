package com.jungle.hotmap.domain;

import javax.persistence.*;

public class Wc {
    private String word;

    @Column(name = "word_count")
    private Integer wordCount;

    /**
     * @return word
     */
    public String getWord() {
        return word;
    }

    /**
     * @param word
     */
    public void setWord(String word) {
        this.word = word;
    }

    /**
     * @return word_count
     */
    public Integer getWordCount() {
        return wordCount;
    }

    /**
     * @param wordCount
     */
    public void setWordCount(Integer wordCount) {
        this.wordCount = wordCount;
    }
}