package models;

import java.util.*;

/**
 * This class represents a Module in Onsophic's JSON
 *
 * @author Titouan Vervack
 */
public class Module {
    private String title;
    private List<Section> sections;
    private Map<String, Integer> tags;
    private String chapter;

    public Module() {
        sections = new ArrayList<>();
        tags = new HashMap<>();
    }

    public String getChapter(){
        return chapter;
    }

    public void setChapter(String chapter)
    {
        this.chapter = chapter;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void addSection(Section section) {
        sections.add(section);
    }

    public List<Section> getSections() {
        return sections;
    }

    public void setTags(List<String> tags) {
        this.tags.clear();
        addTags(tags);
    }

    public List<String> getTags() {
        return new ArrayList<>(tags.keySet());
    }

    public List<String> getXTags(int amount) {
        List<Map.Entry<String, Integer>> entries = entriesSortedByValues(tags);
        List<Map.Entry<String, Integer>> shortList = entries.subList(0, amount > entries.size() ? entries.size() : amount);
        List<String> result = new ArrayList<>();
        shortList.forEach(e -> result.add(e.getKey()));

        return result;
    }

    private List<Map.Entry<String, Integer>> entriesSortedByValues(Map<String, Integer> map) {
        List<Map.Entry<String, Integer>> sortedEntries = new ArrayList<>(map.entrySet());

        Collections.sort(sortedEntries,
                (e1, e2) -> e2.getValue().compareTo(e1.getValue())
        );

        return sortedEntries;
    }

    private void addTag(String tag) {
        if (!tags.containsKey(tag)) {
            tags.put(tag, 1);
        } else {
            tags.put(tag, tags.get(tag) + 1);
        }
    }

    public void addTags(List<String> tags) {
        tags.forEach(this::addTag);
    }
}
