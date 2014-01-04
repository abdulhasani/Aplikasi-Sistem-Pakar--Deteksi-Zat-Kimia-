/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jmat.deteksisenyawa.model;

import jmat.deteksisenyawa.entity.EntitySenyawa;
import jmat.deteksisenyawa.helper.HelperConnection;
import jmat.deteksisenyawa.listener.ListenerSenyawa;

/**
 *
 * @author hasani
 */
public class ModelSenyawa {

    private Long idSenyawa;
    private String namaZat;
    private String rumusEmpiris;
    private String rumusMolekul;
    private ListenerSenyawa listenerSenyawa;

    public Long getIdSenyawa() {
        return idSenyawa;
    }

    public void setIdSenyawa(Long idSenyawa) {
        this.idSenyawa = idSenyawa;
    }

    public String getNamaZat() {
        return namaZat;
    }

    public void setNamaZat(String namaZat) {
        this.namaZat = namaZat;
    }

    public String getRumusEmpiris() {
        return rumusEmpiris;
    }

    public void setRumusEmpiris(String rumusEmpiris) {
        this.rumusEmpiris = rumusEmpiris;
    }

    public String getRumusMolekul() {
        return rumusMolekul;
    }

    public void setRumusMolekul(String rumusMolekul) {
        this.rumusMolekul = rumusMolekul;
    }

    public ListenerSenyawa getListenerSenyawa() {
        return listenerSenyawa;
    }

    public void setListenerSenyawa(ListenerSenyawa listenerSenyawa) {
        this.listenerSenyawa = listenerSenyawa;
    }

    protected void fireOnInsert(EntitySenyawa entitySenyawa) {
        if (listenerSenyawa != null) {
            listenerSenyawa.OnInsert(entitySenyawa);
        }
    }

    protected void fireOnUpdate(EntitySenyawa entitySenyawa) {
        if (listenerSenyawa != null) {
            listenerSenyawa.OnUpdate(entitySenyawa);
        }
    }

    public void insert() {
        EntitySenyawa entitySenyawa;
        entitySenyawa = new EntitySenyawa();
        entitySenyawa.setIdSenyawa(idSenyawa);
        entitySenyawa.setNamaZat(namaZat);
        entitySenyawa.setRumusEmpiris(rumusEmpiris);
        entitySenyawa.setRumusMolekul(rumusMolekul);
        HelperConnection.getDaoSenyawa().insert(entitySenyawa);
        fireOnInsert(entitySenyawa);
    }

    public void update() {
        EntitySenyawa entitySenyawa;
        entitySenyawa = new EntitySenyawa();
        entitySenyawa.setIdSenyawa(idSenyawa);
        entitySenyawa.setNamaZat(namaZat);
        entitySenyawa.setRumusEmpiris(rumusEmpiris);
        entitySenyawa.setRumusMolekul(rumusMolekul);
        HelperConnection.getDaoSenyawa().update(entitySenyawa);
        fireOnUpdate(entitySenyawa);
    }
}
