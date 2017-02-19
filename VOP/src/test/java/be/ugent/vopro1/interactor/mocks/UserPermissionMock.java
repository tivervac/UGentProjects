package be.ugent.vopro1.interactor.mocks;

import be.ugent.vopro1.interactor.user.UserPermission;

public class UserPermissionMock implements UserPermission {

    private boolean allow;

    @Override
    public boolean canAdd(String auth) {
        return allow;
    }

    @Override
    public boolean canEdit(String auth, int userId) {
        return allow;
    }

    @Override
    public boolean canGet(String auth, int userId) {
        return allow;
    }

    @Override
    public boolean canUpgrade(String auth) {
        return allow;
    }

    @Override
    public boolean canGetSelf(String auth) {
        return allow;
    }

    @Override
    public boolean canGetAll(String auth) {
        return allow;
    }

    @Override
    public boolean canRemove(String auth) {
        return allow;
    }

    @Override
    public boolean canGetTeams(String auth, int userId) {
        return allow;
    }

    @Override
    public boolean canGetAnalystList(String auth, int userId) {
        return allow;
    }

    public void setAllow(boolean allow) {
        this.allow = allow;
    }
}
