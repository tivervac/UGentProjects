package be.ugent.vopro1.interactor.mocks;

import be.ugent.vopro1.interactor.team.TeamPermission;

public class TeamPermissionMock implements TeamPermission {

    private boolean allow;

    @Override
    public boolean canAdd(String auth) {
        return allow;
    }

    @Override
    public boolean canEdit(String auth, int teamId) {
        return allow;
    }

    @Override
    public boolean canGet(String auth, int teamId) {
        return allow;
    }

    @Override
    public boolean canGetAll(String auth) {
        return allow;
    }

    @Override
    public boolean canRemove(String auth, int teamId) {
        return allow;
    }

    @Override
    public boolean canGetMembers(String auth, int teamId) {
        return allow;
    }

    @Override
    public boolean canAddMember(String auth, int teamId) {
        return allow;
    }

    @Override
    public boolean canRemoveMember(String auth, int teamId) {
        return allow;
    }

    @Override
    public boolean canGetProjects(String auth, int teamId) {
        return allow;
    }

    @Override
    public boolean canAddProject(String auth, int teamId, String projectName) {
        return allow;
    }

    @Override
    public boolean canRemoveProject(String auth, int teamId, String projectName) {
        return allow;
    }

    public void setAllow(boolean allow) {
        this.allow = allow;
    }
}
