/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jmat.deteksisenyawa.model;

import java.util.List;
import jmat.deteksisenyawa.entity.EntityCiri;
import jmat.deteksisenyawa.entity.EntityRule;
import jmat.deteksisenyawa.entity.EntitySenyawa;
import jmat.deteksisenyawa.helper.HelperConnection;

/**
 *
 * @author hasani
 */
public class ModelRule {
    
    private EntitySenyawa entitySenyawa;
    private List<EntityCiri> entityCiris;
    
    public EntitySenyawa getEntitySenyawa() {
        return entitySenyawa;
    }
    
    public void setEntitySenyawa(EntitySenyawa entitySenyawa) {
        this.entitySenyawa = entitySenyawa;
    }
    
    public List<EntityCiri> getEntityCiris() {
        return entityCiris;
    }
    
    public void setEntityCiris(List<EntityCiri> entityCiris) {
        this.entityCiris = entityCiris;
    }
    
    public void insert() {
        EntityRule entityRule;
        entityRule = new EntityRule();
        entityRule.setListSenyawa(entitySenyawa);
        entityRule.setListCiri(entityCiris);
        HelperConnection.getDaoRule().insert(entityRule);
    }

    public void delete() {
        HelperConnection.getDaoRule().delete(entitySenyawa);
    }
}
