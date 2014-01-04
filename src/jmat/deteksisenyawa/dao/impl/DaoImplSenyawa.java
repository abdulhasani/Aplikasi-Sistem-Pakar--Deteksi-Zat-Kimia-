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
import jmat.deteksisenyawa.dao.DaoSenyawa;
import jmat.deteksisenyawa.entity.EntitySenyawa;
import org.apache.log4j.Logger;

/**
 *
 * @author hasani
 */
public class DaoImplSenyawa implements DaoSenyawa {

    private static final Logger LOGGER = Logger.getLogger(DaoImplSenyawa.class);
    private Connection connection;
    private final String insert = "INSERT INTO senyawa (idSenyawa,nama_zat,rumus_empiris,rumus_molekul) "
            + "VALUES (?,?,?,?)";
    private final String update = "UPDATE senyawa SET nama_zat=?,rumus_empiris=?,rumus_molekul=? WHERE idSenyawa=? ";
    private final String delete = "DELETE FROM senyawa WHERE idSenyawa=? ";
    private final String select = "SELECT * FROM senyawa ";
    private final String getId = "SELECT * FROM senyawa WHERE idSenyawa=? ";
    private final String getCount = "SELECT COUNT(*) AS TOTAL FROM senyawa ";

    public DaoImplSenyawa(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void insert(EntitySenyawa entitySenyawa) {
        PreparedStatement statement = null;
        try {
            connection.setAutoCommit(false);
            statement = connection.prepareStatement(insert);
            statement.setLong(1, entitySenyawa.getIdSenyawa());
            statement.setString(2, entitySenyawa.getNamaZat());
            statement.setString(3, entitySenyawa.getRumusEmpiris());
            statement.setString(4, entitySenyawa.getRumusMolekul());
            statement.executeUpdate();
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
    public void update(EntitySenyawa entitySenyawa) {
        PreparedStatement statement = null;
        try {
            connection.setAutoCommit(false);
            statement = connection.prepareStatement(update);
            statement.setString(1, entitySenyawa.getNamaZat());
            statement.setString(2, entitySenyawa.getRumusEmpiris());
            statement.setString(3, entitySenyawa.getRumusMolekul());
            statement.setLong(4, entitySenyawa.getIdSenyawa());
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
    public void delete(Long id) {
        PreparedStatement statement = null;
        try {
            connection.setAutoCommit(false);
            statement = connection.prepareStatement(delete);
            statement.setLong(1, id);
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
    public List<EntitySenyawa> getAll(int skip, int max) {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<EntitySenyawa> list = new ArrayList<>();
        try {
            connection.setAutoCommit(false);
            statement = connection.prepareStatement(select + " LIMIT " + skip + "," + max);
            resultSet = statement.executeQuery();
            EntitySenyawa entitySenyawa;
            while (resultSet.next()) {
                entitySenyawa = new EntitySenyawa();
                entitySenyawa.setIdSenyawa(resultSet.getLong("idSenyawa"));
                entitySenyawa.setNamaZat(resultSet.getString("nama_zat"));
                entitySenyawa.setRumusEmpiris(resultSet.getString("rumus_empiris"));
                entitySenyawa.setRumusMolekul(resultSet.getString("rumus_molekul"));
                list.add(entitySenyawa);
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
    public List<EntitySenyawa> getAll() {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<EntitySenyawa> list = new ArrayList<>();
        try {
            connection.setAutoCommit(false);
            statement = connection.prepareStatement(select);
            resultSet = statement.executeQuery();
            EntitySenyawa entitySenyawa;
            while (resultSet.next()) {
                entitySenyawa = new EntitySenyawa();
                entitySenyawa.setIdSenyawa(resultSet.getLong("idSenyawa"));
                entitySenyawa.setNamaZat(resultSet.getString("nama_zat"));
                entitySenyawa.setRumusEmpiris(resultSet.getString("rumus_empiris"));
                entitySenyawa.setRumusMolekul(resultSet.getString("rumus_molekul"));
                list.add(entitySenyawa);
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
    public EntitySenyawa getId(Long id) {
        PreparedStatement statement = null;
        ResultSet resultSet;
        EntitySenyawa entitySenyawa = null;
        try {

            statement = connection.prepareStatement(getId);
            statement.setLong(1, id);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                entitySenyawa = new EntitySenyawa();
                entitySenyawa.setIdSenyawa(resultSet.getLong("idSenyawa"));
                entitySenyawa.setNamaZat(resultSet.getString("nama_zat"));
                entitySenyawa.setRumusEmpiris(resultSet.getString("rumus_empiris"));
                entitySenyawa.setRumusMolekul(resultSet.getString("rumus_molekul"));
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
        }
        return entitySenyawa;
    }

    @Override
    public List<EntitySenyawa> getSearch(String namaZat) {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<EntitySenyawa> list = new ArrayList<>();
        final String sql = "SELECT * FROM senyawa WHERE nama_zat LIKE '%" + namaZat + "%'";
        try {
            connection.setAutoCommit(false);
            statement = connection.prepareStatement(sql);
            resultSet = statement.executeQuery();
            EntitySenyawa entitySenyawa;
            while (resultSet.next()) {
                entitySenyawa = new EntitySenyawa();
                entitySenyawa.setIdSenyawa(resultSet.getLong("idSenyawa"));
                entitySenyawa.setNamaZat(resultSet.getString("nama_zat"));
                entitySenyawa.setRumusEmpiris(resultSet.getString("rumus_empiris"));
                entitySenyawa.setRumusMolekul(resultSet.getString("rumus_molekul"));
                list.add(entitySenyawa);
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

            statement = connection.prepareStatement(getCount);
            resultSet = statement.executeQuery();
            EntitySenyawa entitySenyawa;
            if (resultSet.next()) {
                total = resultSet.getLong("TOTAL");
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
        return total;
    }
}
