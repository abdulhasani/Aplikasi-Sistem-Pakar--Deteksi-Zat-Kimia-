/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jmat.deteksisenyawa.entity;

import com.stripbandunk.jwidget.annotation.TableColumn;
import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author hasani
 */
public class EntitySenyawa implements Serializable{
    private Long idSenyawa;
    @TableColumn(number = 1,name ="NAMA ZAT",size = 30)
    private String namaZat;
    @TableColumn(number = 2,name ="RUMUS EMPIRIS",size = 20)
    private String rumusEmpiris;
    @TableColumn(number = 3,name ="RUMUS MOLEKUL",size = 20)
    private String rumusMolekul;

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

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + Objects.hashCode(this.idSenyawa);
        hash = 41 * hash + Objects.hashCode(this.namaZat);
        hash = 41 * hash + Objects.hashCode(this.rumusEmpiris);
        hash = 41 * hash + Objects.hashCode(this.rumusMolekul);
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
        final EntitySenyawa other = (EntitySenyawa) obj;
        if (!Objects.equals(this.idSenyawa, other.idSenyawa)) {
            return false;
        }
        if (!Objects.equals(this.namaZat, other.namaZat)) {
            return false;
        }
        if (!Objects.equals(this.rumusEmpiris, other.rumusEmpiris)) {
            return false;
        }
        if (!Objects.equals(this.rumusMolekul, other.rumusMolekul)) {
            return false;
        }
        return true;
    }
    
    
}
