/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jmat.deteksisenyawa.model;

import jmat.deteksisenyawa.entity.EntityCiri;
import jmat.deteksisenyawa.helper.HelperConnection;
import jmat.deteksisenyawa.listener.ListenerCiri;

/**
 *
 * @author hasani
 */
public class ModelCiri {

    private Long idCiri;
    private String penjelasan;
    private ListenerCiri listenerCiri;

    public Long getIdCiri() {
        return idCiri;
    }

    public void setIdCiri(Long idCiri) {
        this.idCiri = idCiri;
    }

    public String getPenjelasan() {
        return penjelasan;
    }

    public void setPenjelasan(String penjelasan) {
        this.penjelasan = penjelasan;
    }

    public ListenerCiri getListenerCiri() {
        return listenerCiri;
    }

    public void setListenerCiri(ListenerCiri listenerCiri) {
        this.listenerCiri = listenerCiri;
    }

    protected void fireOnInsert(EntityCiri entityCiri) {
        if (listenerCiri != null) {
            listenerCiri.insert(entityCiri);
        }
    }

    protected void fireOnUpdate(EntityCiri entityCiri) {
        if (listenerCiri != null) {
            listenerCiri.update(entityCiri);
        }
    }

    public void insert() {
        EntityCiri entityCiri = new EntityCiri();
        entityCiri.setIdCiri(idCiri);
        entityCiri.setPenjelasan(penjelasan);
        HelperConnection.getDaoCiri().insert(entityCiri);
        fireOnInsert(entityCiri);
    }

    public void update() {
        EntityCiri entityCiri = new EntityCiri();
        entityCiri.setIdCiri(idCiri);
        entityCiri.setPenjelasan(penjelasan);
        HelperConnection.getDaoCiri().update(entityCiri);
        fireOnUpdate(entityCiri);
    }
}
