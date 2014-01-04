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
public class EntityCiri implements Serializable{
    private Long idCiri;
    @TableColumn(number = 1,name = "CIRI SENYAWA",size = 70)
    private String penjelasan;

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

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + Objects.hashCode(this.idCiri);
        hash = 67 * hash + Objects.hashCode(this.penjelasan);
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
        final EntityCiri other = (EntityCiri) obj;
        if (!Objects.equals(this.idCiri, other.idCiri)) {
            return false;
        }
        if (!Objects.equals(this.penjelasan, other.penjelasan)) {
            return false;
        }
        return true;
    }
    
    
}
