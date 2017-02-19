package be.ugent.vopro1.interactor.mocks;

import be.ugent.vopro1.interactor.entity.EntityPermission;

public class EntityPermissionMock implements EntityPermission {
    
    private boolean allow = true;
    
    @Override
    public boolean canAdd(String auth, String projectName) {
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
    public boolean canGetAll(String auth, String projectName) {
        return allow;
    }

    @Override
    public boolean canRemove(String auth, String projectName) {
        return allow;
    }

    @Override
    public boolean canSearch(String auth, String projectName) {
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
    public boolean canRemoveAnalyst(String auth, String projectName) {
        return allow;
    }

    public void setAllow(boolean allow) {
        this.allow = allow;
    }
}
