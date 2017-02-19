package be.ugent.vopro1.model;

import be.ugent.vopro1.model.stub.MemberTeamEntity;
import be.ugent.vopro1.model.stub.ProjectAnalystEntity;
import be.ugent.vopro1.model.stub.TeamMemberEntity;
import be.ugent.vopro1.model.stub.TeamProjectEntity;
import org.junit.Test;

import static org.junit.Assert.*;

public class StubEntityTests {

    @Test
    public void testStubEntities() {
        MemberTeamEntity mte = new MemberTeamEntity();
        ProjectAnalystEntity pae = new ProjectAnalystEntity();
        TeamMemberEntity tme = new TeamMemberEntity();
        TeamProjectEntity tpe = new TeamProjectEntity();

        assertNotNull(mte);
        assertNotNull(pae);
        assertNotNull(tme);
        assertNotNull(tpe);
    }

}
