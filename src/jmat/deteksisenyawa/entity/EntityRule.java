/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jmat.deteksisenyawa.entity;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author hasani
 */
public class EntityRule implements Serializable {

    private EntitySenyawa idSenyawa;
    private List<EntityCiri> listCiri;


    public EntitySenyawa getIdSenyawa() {
        return idSenyawa;
    }

    public void setListSenyawa(EntitySenyawa idSenyawa) {
        this.idSenyawa = idSenyawa;
    }

    public List<EntityCiri> getListCiri() {
        return listCiri;
    }

    public void setListCiri(List<EntityCiri> listCiri) {
        this.listCiri = listCiri;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.idSenyawa);
        hash = 79 * hash + Objects.hashCode(this.listCiri);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final EntityRule other = (EntityRule) obj;
        if (!Objects.equals(this.idSenyawa, other.idSenyawa)) {
            return false;
        }
        if (!Objects.equals(this.listCiri, other.listCiri)) {
            return false;
        }
        return true;
    }

    
}
