/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jmat.deteksisenyawa.dao;

import java.util.List;
import jmat.deteksisenyawa.entity.EntityCiri;

/**
 *
 * @author hasani
 */
public interface DaoCiri {

    public void insert(EntityCiri entityCiri);

    public void update(EntityCiri entityCiri);

    public void delete(Long id);

    public List<EntityCiri> getAll(int skip, int max);

    public List<EntityCiri> getAll();

    public EntityCiri getId(Long id);

    public List<EntityCiri> getSearch(String penjelasan, int skip, int max);

    public List<EntityCiri> getSearch(String penjelasan);

    public Long getCount();
}
