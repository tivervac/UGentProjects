package be.ugent.vopro1.persistence.jdbc.postgresql.rowmapper;

import be.ugent.vopro1.bean.PersistentUser;
import be.ugent.vopro1.persistence.jdbc.postgresql.UserDAOImpl;
import be.ugent.vopro1.persistence.jdbc.postgresql.structure.UserTable;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * An implementation of the RowMapper interface, used by the ProjectDaoImpl
 * to make PersistentUsers (Users) out of queries.
 *
 * @see PersistentUser
 * @see UserDAOImpl
 * @see RowMapper
 */
public class UserRowMapper implements RowMapper<PersistentUser> {

    /**
     * Maps a row from a ResultSet to a PersistentUser (user) to be used in
     * UserDAOImpl.
     *
     * @param rs the ResultSet retrieved from the database in which we will
     * look for data
     * @param rowNum the number of the row of the ResultSet
     * @return the PersistentUser containing the data from the rownumber
     * rowNum from ResultSet rs
     * @see PersistentUser
     * @see ResultSet
     */
    @Override
    public PersistentUser mapRow(ResultSet rs, int rowNum) throws SQLException {
        return PersistentUser.PersistentUserBuilder.aPersistentUser()
                .id(rs.getInt(UserTable.UserColumn.ID.repr()))
                .firstName(rs.getString(UserTable.UserColumn.FIRST_NAME.repr()))
                .lastName(rs.getString(UserTable.UserColumn.LAST_NAME.repr()))
                .email(rs.getString(UserTable.UserColumn.EMAIL.repr()))
                .hashedPassword(rs.getString(UserTable.UserColumn.PASSWORD.repr()))
                .admin(rs.getBoolean(UserTable.UserColumn.IS_ADMIN.repr()))
                .build();
    }
}
