package be.ugent.vopro1.interactor.mocks;

import be.ugent.vopro1.interactor.authentication.HeaderAuthenticationHandler;

public class MockHeaderAuthenticationHandler implements HeaderAuthenticationHandler {

    private boolean allow;

    @Override
    public boolean hasPermission(String authHeader) {
        return !(authHeader==null) && allow;
    }

    @Override
    public boolean hasPermission(String authHeader, boolean loginRequired) {
        return !(authHeader==null) && allow;
    }

    @Override
    public boolean hasPermission(String authHeader, boolean loginRequired, boolean adminRequired) {
        return !(authHeader==null) && allow;
    }

    @Override
    public String getEmail(String authHeader) {
        return "me@example.com";
    }

    @Override
    public boolean hasPermission(String email, String password) {
        return !(email==null||password==null) && allow;
    }

    @Override
    public boolean hasPermission(String email, String password, boolean loginRequired) {
        return !(email==null||password==null) && allow;
    }

    @Override
    public boolean hasPermission(String email, String password, boolean loginRequired, boolean adminRequired) {
        return !(email==null||password==null) && allow;
    }

    public void setAllow(boolean allow) {
        this.allow = allow;
    }
}
