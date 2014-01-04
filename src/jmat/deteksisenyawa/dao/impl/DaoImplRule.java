/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jmat.deteksisenyawa.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import jmat.deteksisenyawa.dao.DaoRule;
import jmat.deteksisenyawa.entity.EntityCiri;
import jmat.deteksisenyawa.entity.EntityRule;
import jmat.deteksisenyawa.entity.EntitySenyawa;
import org.apache.log4j.Logger;

/**
 *
 * @author hasani
 */
public class DaoImplRule implements DaoRule {

    private static final Logger LOGGER = Logger.getLogger(DaoImplRule.class);
    private Connection connection;
    private final String insert = "INSERT INTO rule (idSenyawa,idCiri) VALUES (?,?)";
    private String update;
    private String updateORdelete;
    private String delete;
    private final String deleteByIdSenyawa = "DELETE FROM rule WHERE idSenyawa=? ";
    private String getAll;
    private final String getIdSenyawa = "SELECT * FROM rule WHERE idSenyawa=? ";
    private final String getByIdCiri = "SELECT * FROM rule WHERE idCiri=? ";
    private String getCount = "SELECT COUNT(*) FROM rule ";

    public DaoImplRule(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void insert(EntityRule entityRule) {
        PreparedStatement statement = null;
        try {
            connection.setAutoCommit(false);
            for (EntityCiri enCir : entityRule.getListCiri()) {
                statement = connection.prepareStatement(insert);
                statement.setLong(1, entityRule.getIdSenyawa().getIdSenyawa());
                statement.setLong(2, enCir.getIdCiri());
                statement.executeUpdate();
            }
            connection.commit();
        } catch (SQLException ex) {
            try {
                connection.rollback();
            } catch (SQLException ex1) {
                LOGGER.error(ex1.getMessage(), ex1);
            }
            ex.printStackTrace();
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException ex) {
                LOGGER.error(ex.getMessage(), ex);
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException ex) {
                    LOGGER.error(ex.getMessage(), ex);
                }
            }
        }
    }

    @Override
    public void update(EntityRule entityRule) {
        PreparedStatement statement = null;
        try {
            connection.setAutoCommit(false);
            statement = connection.prepareStatement(updateORdelete);
            statement.setLong(1, entityRule.getIdSenyawa().getIdSenyawa());
            statement.executeUpdate();
            insert(entityRule);
            connection.commit();
        } catch (SQLException ex) {
            try {
                connection.rollback();
            } catch (SQLException ex1) {
                LOGGER.error(ex1.getMessage(), ex1);
            }
            LOGGER.error(ex.getMessage(), ex);
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException ex) {
                LOGGER.error(ex.getMessage(), ex);
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException ex) {
                    LOGGER.error(ex.getMessage(), ex);
                }
            }
        }
    }

    @Override
    public void delete(Long idCiri) {
        PreparedStatement statement = null;
        try {
            connection.setAutoCommit(false);
            statement = connection.prepareStatement(delete);
            statement.setLong(1, idCiri);
            statement.executeUpdate();
            connection.commit();
        } catch (SQLException ex) {
            try {
                connection.rollback();
            } catch (SQLException ex1) {
                LOGGER.error(ex1.getMessage(), ex1);
            }
            LOGGER.error(ex.getMessage(), ex);
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException ex) {
                LOGGER.error(ex.getMessage(), ex);
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException ex) {
                    LOGGER.error(ex.getMessage(), ex);
                }
            }
        }
    }

    @Override
    public void delete(EntitySenyawa entitySenyawa) {
        PreparedStatement statement = null;
        try {
            connection.setAutoCommit(false);
            statement = connection.prepareStatement(deleteByIdSenyawa);
            statement.setLong(1, entitySenyawa.getIdSenyawa());
            statement.executeUpdate();
            connection.commit();
        } catch (SQLException ex) {
            try {
                connection.rollback();
            } catch (SQLException ex1) {
                LOGGER.error(ex.getMessage(), ex);
            }
            LOGGER.error(ex.getMessage(), ex);
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException ex) {
                LOGGER.error(ex.getMessage(), ex);
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException ex) {
                    LOGGER.error(ex.getMessage(), ex);
                }
            }
        }
    }

    @Override
    public List<EntityRule> getAll(int skip, int max) {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<EntityRule> list = new ArrayList<>();
        try {
            connection.setAutoCommit(false);
            statement = connection.prepareStatement(getAll + " LIMIT " + skip + "," + max);
            resultSet = statement.executeQuery();
            EntityRule entityRule;
            List<EntityCiri> ciris = new ArrayList<>();
            while (resultSet.next()) {
                entityRule = new EntityRule();
                entityRule.setListSenyawa(new DaoImplSenyawa(connection).getId(resultSet.getLong("idSenyawa")));
                ciris.add(new DaoImplCiri(connection).getId(resultSet.getLong("idCiri")));
                entityRule.setListCiri(ciris);
                list.add(entityRule);
            }
            connection.commit();
        } catch (SQLException ex) {
            try {
                connection.rollback();
            } catch (SQLException ex1) {
                LOGGER.error(ex1.getMessage(), ex1);
            }
            LOGGER.error(ex.getMessage(), ex);
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException ex) {
                LOGGER.error(ex.getMessage(), ex);
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException ex) {
                    LOGGER.error(ex.getMessage(), ex);
                }
            }
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException ex) {
                    LOGGER.error(ex.getMessage(), ex);
                }
            }
        }
        return list;

    }

    @Override
    public List<EntityRule> getAll() {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<EntityRule> list = new ArrayList<>();
        try {
            connection.setAutoCommit(false);
            statement = connection.prepareStatement(getAll);
            resultSet = statement.executeQuery();
            EntityRule entityRule;
            List<EntityCiri> ciris = new ArrayList<>();
            while (resultSet.next()) {
                entityRule = new EntityRule();
                entityRule.setListSenyawa(new DaoImplSenyawa(connection).getId(resultSet.getLong("idSenyawa")));
                ciris.add(new DaoImplCiri(connection).getId(resultSet.getLong("idCiri")));
                entityRule.setListCiri(ciris);
                list.add(entityRule);
            }
            connection.commit();
        } catch (SQLException ex) {
            try {
                connection.rollback();
            } catch (SQLException ex1) {
                LOGGER.error(ex1.getMessage(), ex1);
            }
            LOGGER.error(ex.getMessage(), ex);
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException ex) {
                LOGGER.error(ex.getMessage(), ex);
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException ex) {
                    LOGGER.error(ex.getMessage(), ex);
                }
            }
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException ex) {
                    LOGGER.error(ex.getMessage(), ex);
                }
            }
        }
        return list;
    }

    @Override
    public EntityRule getIdBySenyawa(Long idSenyawa) {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        EntityRule entityRule = null;
        try {
            connection.setAutoCommit(false);
            statement = connection.prepareStatement(getIdSenyawa);
            statement.setLong(1, idSenyawa);
            resultSet = statement.executeQuery();
            entityRule = new EntityRule();
            List<EntityCiri> ciris = new ArrayList<>();
            while (resultSet.next()) {
                entityRule.setListSenyawa(new DaoImplSenyawa(connection).getId(resultSet.getLong("idSenyawa")));
                ciris.add(new DaoImplCiri(connection).getId(resultSet.getLong("idCiri")));
            }
            entityRule.setListCiri(ciris);
            connection.commit();
        } catch (SQLException ex) {
            try {
                connection.rollback();
            } catch (SQLException ex1) {
                LOGGER.error(ex1.getMessage(), ex1);
            }
            ex.printStackTrace();
            LOGGER.error(ex.getMessage(), ex);
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException ex) {
                LOGGER.error(ex.getMessage(), ex);
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException ex) {
                    LOGGER.error(ex.getMessage(), ex);
                }
            }
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException ex) {
                    LOGGER.error(ex.getMessage(), ex);
                }
            }
        }
        return entityRule;
    }

    @Override
    public List<EntityRule> getIByCiri(Long Idciri) {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<EntityRule> list = new ArrayList<>();
        try {
            
            statement = connection.prepareStatement(getByIdCiri);
            statement.setLong(1, Idciri);
            resultSet = statement.executeQuery();
            EntityRule entityRule;
            List<EntityCiri> ciris = new ArrayList<>();
            while (resultSet.next()) {
                entityRule = new EntityRule();
                entityRule.setListSenyawa(new DaoImplSenyawa(connection).getId(resultSet.getLong("idSenyawa")));
                ciris.add(new DaoImplCiri(connection).getId(resultSet.getLong("idCiri")));
                entityRule.setListCiri(ciris);
                list.add(entityRule);
            }
            
        } catch (SQLException ex) {
            
            LOGGER.error(ex.getMessage(), ex);
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException ex) {
                    LOGGER.error(ex.getMessage(), ex);
                }
            }
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException ex) {
                    LOGGER.error(ex.getMessage(), ex);
                }
            }
        }
        return list;
    }

    @Override
    public List<EntityRule> getSearh(String namaZat, int skip, int max) {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String sql = "SELECT rl.idRule,rl.idSenyawa,rl.idCiri FROM rule AS rl "
                + "INNER JOIN senyawa AS sn ON rl.idSenyawa=sn.idSenyawa WHERE "
                + "sn.nama_zat LIKE '%" + namaZat + "%'";
        List<EntityRule> list = new ArrayList<>();
        try {
            connection.setAutoCommit(false);
            statement = connection.prepareStatement(sql + " LIMIT " + skip + "," + max);
            resultSet = statement.executeQuery();
            EntityRule entityRule;
            List<EntityCiri> ciris = new ArrayList<>();
            while (resultSet.next()) {
                entityRule = new EntityRule();
                entityRule.setListSenyawa(new DaoImplSenyawa(connection).getId(resultSet.getLong("idSenyawa")));
                ciris.add(new DaoImplCiri(connection).getId(resultSet.getLong("idCiri")));
                entityRule.setListCiri(ciris);
                list.add(entityRule);
            }
            connection.commit();
        } catch (SQLException ex) {
            try {
                connection.rollback();
            } catch (SQLException ex1) {
                LOGGER.error(ex1.getMessage(), ex1);
            }
            LOGGER.error(ex.getMessage(), ex);
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException ex) {
                LOGGER.error(ex.getMessage(), ex);
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException ex) {
                    LOGGER.error(ex.getMessage(), ex);
                }
            }
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException ex) {
                    LOGGER.error(ex.getMessage(), ex);
                }
            }
        }
        return list;
    }

    @Override
    public List<EntityRule> getSearh(String namaZat) {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String sql = "SELECT rl.idRule,rl.idSenyawa,rl.idCiri FROM rule AS rl "
                + "INNER JOIN senyawa AS sn ON rl.idSenyawa=sn.idSenyawa WHERE "
                + "sn.nama_zat LIKE '%" + namaZat + "%'";
        List<EntityRule> list = new ArrayList<>();
        try {
            connection.setAutoCommit(false);
            statement = connection.prepareStatement(sql);
            resultSet = statement.executeQuery();
            EntityRule entityRule;
            List<EntityCiri> ciris = new ArrayList<>();
            while (resultSet.next()) {
                entityRule = new EntityRule();
                entityRule.setListSenyawa(new DaoImplSenyawa(connection).getId(resultSet.getLong("idSenyawa")));
                ciris.add(new DaoImplCiri(connection).getId(resultSet.getLong("idCiri")));
                entityRule.setListCiri(ciris);
                list.add(entityRule);
            }
            connection.commit();
        } catch (SQLException ex) {
            try {
                connection.rollback();
            } catch (SQLException ex1) {
                LOGGER.error(ex1.getMessage(), ex1);
            }
            LOGGER.error(ex.getMessage(), ex);
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException ex) {
                LOGGER.error(ex.getMessage(), ex);
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException ex) {
                    LOGGER.error(ex.getMessage(), ex);
                }
            }
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException ex) {
                    LOGGER.error(ex.getMessage(), ex);
                }
            }
        }
        return list;
    }

    @Override
    public Long getCount() {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Long total = 0l;
        try {
            connection.setAutoCommit(false);
            statement = connection.prepareStatement(getCount);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                total = resultSet.getLong("TOTAL");
            }
            connection.commit();
        } catch (SQLException ex) {
            try {
                connection.rollback();
            } catch (SQLException ex1) {
                LOGGER.error(ex1.getMessage(), ex1);
            }
            LOGGER.error(ex.getMessage(), ex);
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException ex) {
                LOGGER.error(ex.getMessage(), ex);
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException ex) {
                    LOGGER.error(ex.getMessage(), ex);
                }
            }
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException ex) {
                    LOGGER.error(ex.getMessage(), ex);
                }
            }
        }
        return total;
    }
}
