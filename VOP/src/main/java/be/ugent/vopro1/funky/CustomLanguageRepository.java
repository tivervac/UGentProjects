package be.ugent.vopro1.funky;

import org.aikodi.chameleon.core.factory.Factory;
import org.aikodi.chameleon.core.language.Language;
import org.aikodi.chameleon.workspace.LanguageRepository;
import org.aikodi.lang.funky.language.Funky;

/**
 * Provides a LanguageRepository for the Funky library.
 *
 * @see LanguageRepository
 */
public class CustomLanguageRepository extends LanguageRepository {

    /**
     * Creates a new CustomLanguageRepository that adds Funky to the Chameleon
     * framework.
     * 
     * @see Language
     * @see Factory
     */
    public CustomLanguageRepository() {
        super();

        Language fun = new Funky();
        fun.setPlugin(Factory.class, new Factory());
        this.add(fun);
    }

}
