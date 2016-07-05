package modules.processing.bloom;

import models.BloomTaxonomy;
import models.ParserTask;
import models.document.Document;
import models.document.HTMLDocument;
import modules.input.HTMLGrabber;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import utils.ProcessingException;
import utils.concurrency.DownloadThread;
import utils.observer.StateChangedEvent;
import utils.observer.StateListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by feliciaan on 12/04/2016.
 */
public class BloomLevel {

    private static final String[] remembering = {"Choose", "Define", "Find", "How", "Label", "List", "Match", "Name", "Omit", "Recall", "Relate", "Select", "Show", "Spell", "Tell", "What", "When", "Where", "Which", "Who", "Why"};
    private static final String[] understanding = {"Classify", "Compare", "Contrast", "Demonstrate", "Explain", "Extend", "Illustrate", "Infer", "Interpret", "Outline", "Relate", "Rephrase", "Show", "Summarize", "Translate"};
    private static final String[] applying = {"Apply", "Build", "Choose", "Construct", "Develop", "Experiment with", "Identify", "Interview", "Make use of", "Model", "Organize", "Plan", "Select", "Solve", "Utilize"};
    private static final String[] analyzing = {"Analyze", "Assume", "Categorize", "Classify", "Compare", "Conclusion", "Contrast", "Discover", "Dissect", "Distinguish", "Divide", "Examine", "Function", "Inference", "Inspect", "List", "Motive", "Relationships", "Simplify", "Survey", "Take part in", "Test for", "Theme"};
    private static final String[] evaluating = {"Agree", "Appraise", "Assess", "Award", "Choose", "Compare", "Conclude", "Criteria", "Criticize", "Decide", "Deduct", "Defend", "Determine", "Disprove", "Estimate", "Evaluate", "Explain", "Importance", "Influence", "Interpret", "Judge", "Justify", "Mark", "Measure", "Opinion", "Perceive", "Prioritize", "Prove", "Rate", "Recommend", "Rule on", "Select", "Support", "Value"};
    private static final String[] creating = {"Adapt", "Build", "Change", "Choose", "Combine", "Compile", "Compose", "Construct", "Create", "Delete", "Design", "Develop", "Discuss", "Elaborate", "Estimate", "Formulate", "Happen", "Imagine", "Improve", "Invent", "Make up", "Maximize", "Minimize", "Modify", "Original", "Originate", "Plan", "Predict", "Propose", "Solution", "Solve", "Suppose", "Test", "Theory"};

    private static final HashMap<BloomTaxonomy, String[]> actionVerbs = new HashMap<>();
    static {
        actionVerbs.put(BloomTaxonomy.REMEMBER, remembering);
        actionVerbs.put(BloomTaxonomy.UNDERSTAND, understanding);
        actionVerbs.put(BloomTaxonomy.REMEMBER_UNDERSTAND, ArrayUtils.addAll(remembering, understanding));
        //actionVerbs.put(BloomTaxonomy.APPLY, applying);
        actionVerbs.put(BloomTaxonomy.ANALYZE, analyzing);
        //actionVerbs.put(BloomTaxonomy.EVALUATE, evaluating);
        //actionVerbs.put(BloomTaxonomy.CREATE, creating);
    }

    public static BloomTaxonomy findMostUsedActionVerb(String text) {
        if (text == null || text.isEmpty()) {
            System.err.println("Text input is empty for bloom level");
        }
        text = text.toLowerCase();
        Map<BloomTaxonomy, Integer> counter = new HashMap<>();
        BloomTaxonomy biggest = BloomTaxonomy.NONE;
        int biggestCount = 0;
        for (BloomTaxonomy key: actionVerbs.keySet()) {
            String[] words = actionVerbs.get(key);

            for (String word: words) {
                int count = StringUtils.countMatches(text, word.toLowerCase()) + counter.getOrDefault(key, 0);
                counter.put(key, count);
            }

            if (counter.getOrDefault(key, 0) > biggestCount) {
                biggest = key;
                biggestCount = counter.get(key);
            }
        }
        if (biggest == BloomTaxonomy.NONE) {
            biggest = BloomTaxonomy.REMEMBER;
        }
        return biggest;
    }

    public static BloomTaxonomy findMostUsedActionVerb(Document doc) {
        if (doc == null) {
            return null;
        }
        return findMostUsedActionVerb(doc.getText());
    }

    public static BloomTaxonomy findMostUsedActionVerbWithUrl(String url, StateListener listener) throws ProcessingException {
        Queue<String> urls = new ConcurrentLinkedQueue<>();
        Queue<HTMLDocument> documents = new ConcurrentLinkedQueue<>();
        urls.add(url);

        Thread t = new DownloadThread<>("", new HTMLGrabber(), urls, documents, listener);
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            System.err.println("InterruptedException in BloomLevel");
            throw new ProcessingException(e.getMessage());
        }

        return findMostUsedActionVerb(documents.poll());
    }
}
