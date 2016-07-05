package models;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Wouter Pinnoo
 */
public class Section {

    private String title;

    private Activity activity;
    private Set<String> tags;
    private int weight;

    public Section() {
        tags = new HashSet<>();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public void setTags(List<String> tags) {
        this.tags.clear();
        addTags(tags);
    }

    public Set<String> getTags() {
        return tags;
    }

    public void addTags(List<String> tags) {
        this.tags.addAll(tags);
    }

    public void setWeight(int weight)
    {
        this.weight = weight;
    }

    public int getWeight()
    {
        return this.weight;
    }
}
