package be.ugent.vopro1.interactor.mocks;

import be.ugent.vopro1.interactor.project.ProjectPermission;

public class ProjectPermissionMock implements ProjectPermission {
    
    boolean allow = true;
    
    @Override
    public boolean canAdd(String auth) {
        return allow;
    }

    @Override
    public boolean canEdit(String auth, String projectName) {
        return allow;
    }

    @Override
    public boolean canGet(String auth, String projectName) {
        return allow;
    }

    @Override
    public boolean canGetAll(String auth) {
        return allow;
    }

    @Override
    public boolean canSearch(String auth) {
        return allow;
    }

    @Override
    public boolean canRemove(String auth, String projectName) {
        return allow;
    }

    @Override
    public boolean canGetAnalysts(String auth, String projectName) {
        return allow;
    }

    @Override
    public boolean canAddAnalyst(String auth, String projectName) {
        return allow;
    }


    @Override
    public boolean canEditAnalyst(String auth, String projectName) {
        return allow;
    }

    @Override
    public boolean canRemoveAnalyst(String auth, String projectName) {
        return allow;
    }

    @Override
    public boolean canGetSchedule(String auth, String projectName) {
        return allow;
    }

    public void setAllow(boolean allow) {
        this.allow = allow;
    }
}
