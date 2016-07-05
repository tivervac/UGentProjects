package models;

import modules.processing.bloom.BloomLevel;
import utils.ProcessingException;
import utils.observer.StateListener;

/**
 * @author Wouter Pinnoo
 */
public class Activity {

    private final StateListener listener;
    private String title;
    private String url;
    private BloomTaxonomy bloomLevel;
    private String modality;

    public Activity(StateListener listener) {
        this.listener = listener;
        modality = "text";
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) throws ProcessingException {
        this.url = url;

        // parse the bloomlevel here, because it shouldn't be called manually
        BloomTaxonomy bloom = BloomTaxonomy.NONE;
        if (title != null) {
            BloomLevel.findMostUsedActionVerb(title);
        }
        if (bloom == BloomTaxonomy.NONE) {
            bloom = BloomLevel.findMostUsedActionVerbWithUrl(url, listener);
        }
        setBloomLevel(bloom);
    }


    public BloomTaxonomy getBloomLevel() {
        return bloomLevel;
    }

    public void setBloomLevel(BloomTaxonomy bloomLevel) {
        this.bloomLevel = bloomLevel;
    }

    public String getModality() {
        return modality;
    }

    public void setModality(String modality) {
        this.modality = modality;
    }

}
