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
import jmat.deteksisenyawa.dao.DaoCiri;
import jmat.deteksisenyawa.entity.EntityCiri;
import org.apache.log4j.Logger;

/**
 *
 * @author hasani
 */
public class DaoImplCiri implements DaoCiri {

    private static final Logger LOGGER = Logger.getLogger(DaoImplCiri.class);
    private Connection connection;
    private final String insert = "INSERT INTO ciri (idCiri,penjelasan) VALUES (?,?)";
    private final String update = "UPDATE ciri SET penjelasan=? WHERE idCiri=? ";
    private final String delete = "DELETE FROM ciri WHERE idCiri=? ";
    private final String getAll = "SELECT * FROM ciri";
    private final String getId = "SELECT * FROM ciri WHERE idCiri=? ";
    private final String getCount = "SELECT COUNT(*) AS TOTAL FROM ciri ";

    public DaoImplCiri(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void insert(EntityCiri entityCiri) {
        PreparedStatement statement = null;
        try {
            connection.setAutoCommit(false);
            statement = connection.prepareStatement(insert);
            statement.setLong(1, entityCiri.getIdCiri());
            statement.setString(2, entityCiri.getPenjelasan());
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
    public void update(EntityCiri entityCiri) {
        PreparedStatement statement = null;
        try {
            connection.setAutoCommit(false);
            statement = connection.prepareStatement(update);
            statement.setString(1, entityCiri.getPenjelasan());
            statement.setLong(2, entityCiri.getIdCiri());
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
    public List<EntityCiri> getAll(int skip, int max) {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<EntityCiri> list = new ArrayList<>();
        try {
            connection.setAutoCommit(false);
            statement = connection.prepareStatement(getAll + " LIMIT " + skip + "," + max);
            resultSet = statement.executeQuery();
            EntityCiri entityCiri;
            while (resultSet.next()) {
                entityCiri = new EntityCiri();
                entityCiri.setIdCiri(resultSet.getLong("idCiri"));
                entityCiri.setPenjelasan(resultSet.getString("penjelasan"));
                list.add(entityCiri);
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
    public List<EntityCiri> getAll() {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<EntityCiri> list = new ArrayList<>();
        try {

            statement = connection.prepareStatement(getAll);
            resultSet = statement.executeQuery();
            EntityCiri entityCiri;
            while (resultSet.next()) {
                entityCiri = new EntityCiri();
                entityCiri.setIdCiri(resultSet.getLong("idCiri"));
                entityCiri.setPenjelasan(resultSet.getString("penjelasan"));
                list.add(entityCiri);
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
    public EntityCiri getId(Long id) {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        EntityCiri entityCiri = null;
        try {

            statement = connection.prepareStatement(getId);
            statement.setLong(1, id);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                entityCiri = new EntityCiri();
                entityCiri.setIdCiri(resultSet.getLong("idCiri"));
                entityCiri.setPenjelasan(resultSet.getString("penjelasan"));

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
        return entityCiri;
    }

    @Override
    public List<EntityCiri> getSearch(String penjelasan, int skip, int max) {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<EntityCiri> list = new ArrayList<>();
        final String sql = "SELECT * FROM ciri WHERE penjelasan LIKE '%" + penjelasan + "%'";
        try {
            connection.setAutoCommit(false);
            statement = connection.prepareStatement(sql + " LIMIT " + skip + "," + max);
            resultSet = statement.executeQuery();
            EntityCiri entityCiri;
            while (resultSet.next()) {
                entityCiri = new EntityCiri();
                entityCiri.setIdCiri(resultSet.getLong("idCiri"));
                entityCiri.setPenjelasan(resultSet.getString("penjelasan"));
                list.add(entityCiri);
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
            EntityCiri entityCiri;
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

    @Override
    public List<EntityCiri> getSearch(String penjelasan) {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<EntityCiri> list = new ArrayList<>();
        final String sql = "SELECT * FROM ciri WHERE penjelasan LIKE '%" + penjelasan + "%'";
        try {
            connection.setAutoCommit(false);
            statement = connection.prepareStatement(sql);
            resultSet = statement.executeQuery();
            EntityCiri entityCiri;
            while (resultSet.next()) {
                entityCiri = new EntityCiri();
                entityCiri.setIdCiri(resultSet.getLong("idCiri"));
                entityCiri.setPenjelasan(resultSet.getString("penjelasan"));
                list.add(entityCiri);
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
}
