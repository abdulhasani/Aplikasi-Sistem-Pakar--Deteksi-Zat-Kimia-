/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jmat.deteksisenyawa.internalframe.ciri.renderList;

import java.awt.Color;
import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import jmat.deteksisenyawa.entity.EntityCiri;

/**
 *
 * @author hasani
 */
public class RenderListCiri extends DefaultListCellRenderer{

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        JLabel label= (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus); //To change body of generated methods, choose Tools | Templates.
        if(value instanceof EntityCiri){
            EntityCiri entityCiri;
            entityCiri = (EntityCiri) value;
            label.setText(entityCiri.getPenjelasan());
        }
        if (isSelected) {
            label.setBackground(new Color(116, 97, 215));
        } else {

            if (index % 2 == 1) {
                label.setBackground(Color.WHITE);
            } else {
                label.setBackground(Color.GRAY);
            }
        }
        return label;
    }
    
}
