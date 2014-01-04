/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jmat.deteksisenyawa.dao;

import java.util.List;
import jmat.deteksisenyawa.entity.EntitySenyawa;

/**
 *
 * @author hasani
 */
public interface DaoSenyawa {

    public void insert(EntitySenyawa entitySenyawa);

    public void update(EntitySenyawa entitySenyawa);

    public void delete(Long id);

    public List<EntitySenyawa> getAll(int skip, int max);

    public List<EntitySenyawa> getAll();

    public EntitySenyawa getId(Long id);

    public List<EntitySenyawa> getSearch(String namaZat);

    public Long getCount();
}
