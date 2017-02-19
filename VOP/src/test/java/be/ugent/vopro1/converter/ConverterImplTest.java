package be.ugent.vopro1.converter;

import be.ugent.vopro1.bean.*;
import be.ugent.vopro1.converter.json.JsonConverter;
import be.ugent.vopro1.converter.json.JsonConverterFactory;
import be.ugent.vopro1.model.*;
import be.ugent.vopro1.persistence.EntityDAO;
import be.ugent.vopro1.persistence.UserDAO;
import be.ugent.vopro1.persistence.factory.DAOProvider;
import be.ugent.vopro1.persistence.factory.EntityDAOFactory;
import be.ugent.vopro1.persistence.factory.UserDAOFactory;
import be.ugent.vopro1.persistence.jdbc.mocks.EntityDAOMock;
import be.ugent.vopro1.persistence.jdbc.mocks.UserDAOMock;
import be.ugent.vopro1.persistence.jdbc.mocks.UserDAOProjectMock;
import org.aikodi.lang.funky.behavior.description.TextualDescription;
import org.aikodi.lang.funky.concept.Concept;
import org.aikodi.lang.funky.executors.Actor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/test/resources/spring_test.xml")
public class ConverterImplTest {

    private JsonConverter jsonConverter;
    private ConverterFacade beanConverter;
    private UserDAO userDAO;
    private EntityDAO entityDAO;


    @Before
    public void setup() {
        jsonConverter = JsonConverterFactory.getInstance();
        beanConverter = ConverterFactory.getInstance();
        userDAO = new UserDAOProjectMock();
        UserDAOFactory factory = new UserDAOFactory();
        factory.setInstance(userDAO);
        DAOProvider.setInstance("user", factory);
        entityDAO = new EntityDAOMock();
        EntityDAOFactory entityDAOFactory = new EntityDAOFactory();
        entityDAOFactory.setInstance(entityDAO);
        DAOProvider.setInstance("entity", entityDAOFactory);
    }

    @Test
    public void testConvert() throws Exception {
        String actorBlob = "{\"name\":\"actor12\",\"type\":\"actor\"}";
        String conceptBlob = "{\"name\":\"conceptnewrenamed\",\"definition\":{\"type\":\"textual\",\"data\":{\"text\":\"testdefinition\"}},\"attributes\":[\"attr1\",\"attr2\"]}";
        Actor actor = jsonConverter.convert(Actor.class, actorBlob);

        assertNotNull(actor);
        assertEquals("actor12", actor.name());

        Concept concept = jsonConverter.convert(Concept.class, conceptBlob);

        assertNotNull(concept);
        assertEquals("conceptnewrenamed", concept.getName());
        assertEquals("testdefinition", concept.getDefinition().text());
    }

    @Test
    public void testConvertToString() throws Exception {
        Actor actor = new Actor("testactor");
        String actorBlob = jsonConverter.convertToString(actor);

        assertNotNull(actorBlob);
        assertEquals("{\"name\":\"testactor\"}", actorBlob);

        Concept concept = new Concept("testconcept", new TextualDescription("testdefinition"), Arrays.asList("attr1", "attr2"));
        String conceptBlob = jsonConverter.convertToString(concept);

        assertNotNull(conceptBlob);
        assertEquals("{\"name\":\"testconcept\",\"definition\":{\"type\":\"textual\",\"data\":{\"text\":\"testdefinition\"}},\"attributes\":[\"attr1\",\"attr2\"]}",
                conceptBlob);

        UsecaseEntity usecase = new UsecaseEntity("testusecase");
        usecase.setPreconditions(Arrays.asList(new TextualDescription("precondition1")));
        usecase.setPostconditions(Arrays.asList(new TextualDescription("postcondition1")));
        usecase.setActors(Arrays.asList("actor1", "actor2"));
        usecase.setObjective("objective");
        //usecase.setProcess("proces");
        usecase.setConcepts(Arrays.asList("concept1", "concept2"));
    }

    //TODO rewerite when UsecaseEntity is finished
    @Test
    public void testConvertPersistentObject() throws Exception {
        String actorBlob = "{\"name\":\"actor12\",\"derived\":false}";
        String conceptBlob = "{\"name\":\"concept\",\"definition\":{\"type\":\"textual\",\"data\":{\"text\":\"testdefinition\"}},\"attributes\":[\"attr1\",\"attr2\"]}";
        
        PersistentObject persistentActor = new PersistentObject(1, 1, "actor12", "actor", actorBlob);
        PersistentObject persistentConcept = new PersistentObject(55, 1, "concept", "concept", conceptBlob);
        
        Actor actorEntity = beanConverter.convert(persistentActor);
        Concept conceptEntity = beanConverter.convert(persistentConcept);

        assertNotNull(actorEntity);
        assertNotNull(conceptEntity);

        assertEquals("actor12", actorEntity.name());
        assertEquals("concept", conceptEntity.getName());
    }

    @Test
    public void testConvertEntity() throws Exception {
        Actor entity = new Actor("testentity");
        PersistentObject persistentObject = beanConverter.convert(entity);

        assertNotNull(persistentObject);
        assertEquals(-1, persistentObject.getProjectId());  // Entities don't have project ids
        assertEquals("testentity", persistentObject.getName());
        assertEquals("actor", persistentObject.getType());
        assertEquals(jsonConverter.convertToString(entity), persistentObject.getBlob());
    }

    @Test
    public void testConvertPersistentProject() throws Exception {
        PersistentProject persistentProject = new PersistentProject(5, "testproject", 1);
        EntityProject entityProject = beanConverter.convert(persistentProject);

        assertNotNull(entityProject);
        assertEquals("testproject", entityProject.getName());
    }

    @Test
    public void testConvertEntityProject() throws Exception {
        EntityProject entityProject = new EntityProject("testproject");
        entityProject.setLeader(User.UserBuilder.aUser().email("test@test.com").build());
        PersistentProject persistentProject = beanConverter.convert(entityProject);

        assertNotNull(persistentProject);
        assertEquals("testproject", persistentProject.getName());
    }
    
    @Test
    public void testConvertPersistentUser() {
        PersistentUser persistentUser = PersistentUser.PersistentUserBuilder.aPersistentUser()
                .admin(true)
                .email("email@me.com")
                .firstName("first")
                .lastName("last")
                .hashedPassword("lolcode")
                .id(1)
                .build();
        User user = beanConverter.convert(persistentUser);
        
        assertNotNull(user);
        assertTrue(user.isAdmin());
        assertEquals("email@me.com", user.getEmail());
        assertEquals("first", user.getFirstName());
        assertEquals("last", user.getLastName());
        assertEquals("lolcode", user.getPassword());
    }
    
    @Test
    public void testConvertUserEntity() {
        User user = User.UserBuilder.aUser()
                .admin(true)
                .email("email@me.com")
                .firstName("first")
                .lastName("last")
                .password("lolcode")
                .build();
        PersistentUser persistentUser = beanConverter.convert(user);

        assertNotNull(persistentUser);
        assertTrue(persistentUser.isAdmin());
        assertEquals("email@me.com", persistentUser.getEmail());
        assertEquals("first", persistentUser.getFirstName());
        assertEquals("last", persistentUser.getLastName());
        assertNotEquals("lolcode", persistentUser.getPassword());   // Password should be hashed!
    }
    
    @Test
    public void testConvertPersistentTeam() {
        PersistentTeam persistentTeam = PersistentTeam.PersistentTeamBuilder.aPersistentTeam()
                .id(1)
                .name("team")
                .leader(1)
                .build();
        Team team = beanConverter.convert(persistentTeam);
        
        assertNotNull(team);
        assertEquals("team", team.getName());
    }
    
    @Test
    public void testConvertTeamEntity() {
        Team team = Team.TeamBuilder.aTeam()
                .name("team")
                .leader(User.UserBuilder.aUser().email("test@test.com").build())
                .build();
        PersistentTeam persistentTeam = beanConverter.convert(team);

        assertNotNull(persistentTeam);
        assertEquals("team", persistentTeam.getName());
    }
}