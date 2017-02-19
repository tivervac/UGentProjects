package be.ugent.vopro1.adapter.result.types;

import be.ugent.vopro1.rest.mapper.WebMapper;

/**
 * Provides an way to identify result types in {@link WebMapper}s.
 *
 * @see WebMapper
 */
public abstract class ResultType {
    public abstract String getMessage();
    public abstract boolean isSuccessful();

    @Override
    public boolean equals(Object o){
        if(o == null || !(o instanceof ResultType)) {
            return false;
        }

        ResultType other = (ResultType) o;

        boolean result = true;

        result &= (getClass().equals(other.getClass()));
        result &= (getMessage().equals(other.getMessage()));

        return result;
    }

    @Override
    public int hashCode(){
        int hash = 5;

        hash *= getClass().hashCode() * 31;
        hash *= getMessage().hashCode() * 31;

        return hash;
    }
}
