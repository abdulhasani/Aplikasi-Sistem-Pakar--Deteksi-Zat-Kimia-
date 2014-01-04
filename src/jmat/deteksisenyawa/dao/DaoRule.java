/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jmat.deteksisenyawa.dao;

import java.util.List;
import jmat.deteksisenyawa.entity.EntityRule;
import jmat.deteksisenyawa.entity.EntitySenyawa;

/**
 *
 * @author hasani
 */
public interface DaoRule {

    public void insert(EntityRule entityRule);

    public void update(EntityRule entityRule);

    public void delete(Long idCiri);

    public void delete(EntitySenyawa entitySenyawa);

    public List<EntityRule> getAll(int skip, int max);

    public List<EntityRule> getAll();

    public EntityRule getIdBySenyawa(Long idSenyawa);

    public List<EntityRule> getIByCiri(Long idSenyawa);

    public List<EntityRule> getSearh(String namaZat, int skip, int max);

    public List<EntityRule> getSearh(String namaZat);

    public Long getCount();
}
