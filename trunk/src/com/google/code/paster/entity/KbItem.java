package com.google.code.paster.entity;

/**
 * Simple Entity Definition for the KnowledgeBase Items
 *
 * @author Bram Borggreve
 */
public class KbItem {
    int id;
    String title;
    String text;

    public KbItem(int id, String title, String text) {
        this.id = id;
        this.title = title;
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return this.getTitle();
    }

    
}
