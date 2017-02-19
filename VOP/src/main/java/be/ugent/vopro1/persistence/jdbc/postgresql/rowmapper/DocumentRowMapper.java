package be.ugent.vopro1.persistence.jdbc.postgresql.rowmapper;

import be.ugent.vopro1.bean.PersistentObject;
import be.ugent.vopro1.persistence.jdbc.postgresql.EntityDAOImpl;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import static be.ugent.vopro1.persistence.jdbc.postgresql.structure.EntityTable.EntityColumn.*;

/**
 * An implementation of the RowMapper interface, used
 * to make PersistentObjects out of queries.
 *
 * @see PersistentObject
 * @see EntityDAOImpl
 * @see RowMapper
 */
public class DocumentRowMapper implements RowMapper<PersistentObject> {

        /**
         * Maps a row from a ResultSet to a PersistentObject (Entity) to be used
         * in EntityDAOImpl.
         *
         * @param rs the ResultSet retrieved from the database in which we will
         * look for data
         * @param rowNum the number of the row of the ResultSet
         * @return the PersistentObject containing the data from the rownumber
         * rowNum from ResultSet rs
         * @see PersistentObject
         * @see ResultSet
         */
        @Override
        public PersistentObject mapRow(ResultSet rs, int rowNum) throws SQLException {
        int id = rs.getInt(ID.repr());
        int projectId = rs.getInt(PROJECT.repr());
        String name = rs.getString(NAME.repr());
        String type = rs.getString(TYPE.repr());
        String blob = rs.getString(BLOB.repr());

        return new PersistentObject(id, projectId, name, type, blob);
    }

}
