package be.ugent.vopro1.persistence.factory;

import be.ugent.vopro1.persistence.ScheduleDAO;
import be.ugent.vopro1.util.ApplicationContextProvider;

/**
 * Holds a single instance of a ScheduleDAO.
 *
 * @see ScheduleDAO
 * @see be.ugent.vopro1.persistence.jdbc.postgresql.ScheduleDAOImpl
 * @see DAOFactory
 */
public class ScheduleDAOFactory implements DAOFactory<ScheduleDAO> {

    private ScheduleDAO instance;

    @Override
    public void setInstance(ScheduleDAO instance) {
        this.instance = instance;
    }

    @Override
    public ScheduleDAO getInstance() {
        if (instance == null) {
            instance = ApplicationContextProvider.getApplicationContext().getBean("scheduleDAO", ScheduleDAO.class);
        }

        return instance;
    }
}
