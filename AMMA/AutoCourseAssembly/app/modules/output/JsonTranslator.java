package modules.output;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.Activity;
import models.Module;
import models.Section;
import models.document.Document;
import models.document.JsonDocument;
import org.apache.http.util.TextUtils;
import play.libs.Json;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * This class can PRINT a Document to a json file
 *
 * @author Titouan Vervack
 */
public class JsonTranslator implements DocumentTranslator<JsonDocument> {

    private static final int MAX_TAGS = 10;

    @Override
    public JsonDocument writeDocument(Document document) {
        ObjectNode result = Json.newObject();
        result.put("@type", "CourseEdit");
        result.put("title", document.getTitle());

        ObjectNode props = Json.newObject();
        props.put("onsophic.course.assembly.chapters", true);
        props.put("onsophic.course.assembly.blooms", true);

        result.put("properties", props);

        ArrayNode embeddedModules = createModules(document.getModules());
        result.put("embeddedModules", embeddedModules);

        return new JsonDocument(result);
    }

    private ArrayNode createModules(List<Module> modules) {
        ArrayNode embeddedModules = Json.newObject().arrayNode();
        for (Module m : modules) {
            ObjectNode unit = Json.newObject().put("name", m.getTitle());
            ObjectNode embeddedModule = Json.newObject();
            embeddedModule.put("@type", "ModuleEdit");
            embeddedModule.put("unit", unit);
            embeddedModule.put("title", m.getTitle());
            embeddedModule.put("tags", Json.toJson(m.getXTags(MAX_TAGS)));

            String chapter = m.getChapter();
            if (!TextUtils.isEmpty(chapter))
            {
                embeddedModule.put("chapter", chapter);
            }

            ArrayNode embeddedSections = Json.newObject().arrayNode();

            for (Section s : m.getSections()) {
                ObjectNode embeddedSection = Json.newObject();
                embeddedSection.put("@type", "SectionEdit");
                embeddedSection.put("title", s.getTitle());
                Activity a = s.getActivity();
                ObjectNode activity = Json.newObject();
                ObjectNode activityType = Json.newObject();
                activityType.put("id", "READ");
                activityType.put("title", "Read");
                activity.put("url", a.getUrl());
                activity.put("modality", "text");
                activity.put("activityType", activityType);
                activity.put("title", a.getTitle());
                activity.put("studentSecondMedian", 60);
                if (a.getBloomLevel() != null) {
                    activity.put("level", "" + a.getBloomLevel().toString().toLowerCase());
                } else {
                    System.err.println("" + a.getTitle() + ": Bloomlevel is null");
                }
                embeddedSection.put("activity", activity);
                embeddedSection.put("visible", true);
                embeddedSection.put("optionality", "required");
                embeddedSection.put("tags", Json.toJson(s.getTags()));
                embeddedSection.put("weight", s.getWeight());
                embeddedSections.add(embeddedSection);
            }
            embeddedModule.put("embeddedSections", embeddedSections);
            embeddedModules.add(embeddedModule);
        }

        return embeddedModules;
    }
}

/*
{
  "@type": "CourseEdit",
  "title": "AMMA test course",
  "imageUrl": "https://upload.wikimedia.org/wikipedia/commons/thumb/a/ac/No_image_available.svg/2000px-No_image_available.svg.png",
  "embeddedModules": [
    {
      "@type": "ModuleEdit",
      "unit": {
        "name": "Example Topic 1"
      },
      "title": "Example Topic 1",
      "chapter": "Chapter 1",
      "tags": [],
      "embeddedSections": [
        {
          "@type": "SectionEdit",
          "activity": {
            "url": "http://studiegids.ugent.be/2015/EN/studiefiches/E019161.pdf",
            "modality": "text",
            "activityType": {
              "id": "READ",
              "title": "Read"
            },
            "title": "Advanced Multimedia Applications",
            "studentSecondMedian": 60,
            "level": "remember"
          },
          "title": "Advanced Multimedia Applications",
          "visible": true,
          "optionality": "required",
          "tags": []
        }
      ],
      "embeddedTests": []
    }
  ]
}
*/
