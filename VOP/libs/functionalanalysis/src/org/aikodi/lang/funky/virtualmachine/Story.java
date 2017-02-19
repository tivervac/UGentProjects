package org.aikodi.lang.funky.virtualmachine;

import java.util.ArrayList;
import java.util.List;

public class Story {
    public List<StoryEntry> entries;

    public Story(){
        entries = new ArrayList<>();
    }

    public Story(Story s){
        entries = s.getEntries();
    }

    public void addEntry(StoryEntry e){
        entries.add(e);
    }

    public void setEntries(List<StoryEntry> entries){
        if (entries == null || entries.contains(null)){
            throw new IllegalArgumentException("List of entries is invalid");
        }

        this.entries = new ArrayList<>(entries);
    }

    public List<StoryEntry> getEntries(){
        return new ArrayList<>(entries);
    }
}
