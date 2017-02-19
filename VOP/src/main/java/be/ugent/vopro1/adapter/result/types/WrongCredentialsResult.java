package be.ugent.vopro1.adapter.result.types;

/**
 * Result type for a request that requires authentication
 *
 * @see ResultType
 */
public class WrongCredentialsResult extends ResultType {
    @Override
    public String getMessage() {
        return "Login error: no user with this e-mail or password";
    }

    @Override
    public boolean isSuccessful() {
        return false;
    }
}
