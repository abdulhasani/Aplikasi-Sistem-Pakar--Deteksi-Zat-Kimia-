/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jmat.deteksisenyawa.internalframe.senyawa;

import com.stripbandunk.jwidget.JDynamicTable;
import com.stripbandunk.jwidget.model.DynamicTableModel;
import java.beans.PropertyVetoException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import jmat.deteksisenyawa.dao.DaoSenyawa;
import jmat.deteksisenyawa.entity.EntitySenyawa;
import jmat.deteksisenyawa.frame.DeteksiSenyawa;
import jmat.deteksisenyawa.helper.HelperConnection;
import jmat.deteksisenyawa.internalframe.rule.InternalRule;
import org.apache.log4j.Logger;

/**
 *
 * @author hasani
 */
public class InternalTBSenyawa extends javax.swing.JInternalFrame {

    /**
     * Creates new form InternalTBSenyawa
     */
    private static final Logger LOGGER = Logger.getLogger(InternalTBSenyawa.class);
    private DeteksiSenyawa deteksiSenyawa;
    private DynamicTableModel<EntitySenyawa> tabelModelSenyawa;

    public InternalTBSenyawa() {
        initComponents();
        setTabelSenyawa();
    }

    public DeteksiSenyawa getDeteksiSenyawa() {
        return deteksiSenyawa;
    }

    public void setDeteksiSenyawa(DeteksiSenyawa deteksiSenyawa) {
        this.deteksiSenyawa = deteksiSenyawa;
    }

    public DynamicTableModel<EntitySenyawa> getTabelModelSenyawa() {
        return tabelModelSenyawa;
    }

    public JDynamicTable getTabelSenyawa() {
        return tabelSenyawa;
    }

    public JButton getBtnCariSenyawa() {
        return btnCariSenyawa;
    }

    public JButton getBtnReloadSenyawa() {
        return btnReloadSenyawa;
    }

    public JButton getBtnDeleteSenyawa() {
        return btnDeleteSenyawa;
    }

    public JButton getBtnEditSenyawa() {
        return btnEditSenyawa;
    }

    private void setTabelSenyawa() {

        tabelModelSenyawa = new DynamicTableModel<>(EntitySenyawa.class);
        tabelSenyawa.setDynamicModel(tabelModelSenyawa);
        tabelSenyawa.setShowGrid(true);
        tabelSenyawa.setRowHeight(24);
    }

    public void loadSenyawa() {
        new SwingWorker<List<EntitySenyawa>, Object>() {
            @Override
            protected List<EntitySenyawa> doInBackground() throws Exception {
                DaoSenyawa daoSenyawa;
                daoSenyawa = HelperConnection.getDaoSenyawa();
                List<EntitySenyawa> all;
                Thread.sleep(250);
                all = daoSenyawa.getAll();
                return all;

            }

            @Override
            protected void done() {
                if (tabelModelSenyawa instanceof DynamicTableModel) {
                    try {
                        if (!get().isEmpty()) {
                            tabelModelSenyawa.clear();
                            for (EntitySenyawa enS : get()) {
                                tabelModelSenyawa.add(enS);
                            }
                        }
                    } catch (InterruptedException | ExecutionException ex) {
                        LOGGER.error(ex.getMessage(), ex);
                    }
                }
            }
        }.execute();
    }

    private InternalSenyawa cekFrame() {
        InternalSenyawa internalSenyawa = null;
        if (deteksiSenyawa.internalSenyawa instanceof InternalSenyawa) {
            try {
                internalSenyawa = (InternalSenyawa) deteksiSenyawa.internalSenyawa;
                internalSenyawa.setTitleBaru("Ubah Zat");
                internalSenyawa.setSelected(true);
            } catch (PropertyVetoException ex) {
                LOGGER.error(ex.getMessage(), ex);
            }
        } else {
            if (deteksiSenyawa.getJdPane().getComponentCount() < 2) {
                try {
                    internalSenyawa = new InternalSenyawa();
                    internalSenyawa.setTitleBaru("Ubah Zat");
                    internalSenyawa.setDeteksiSenyawa(deteksiSenyawa);
                    internalSenyawa.setLocation(deteksiSenyawa.getWidth() / 3, deteksiSenyawa.getHeight() / 4);
                    deteksiSenyawa.getJdPane().add(internalSenyawa);
                    internalSenyawa.setVisible(true);
                    internalSenyawa.setSelected(true);
                    deteksiSenyawa.internalSenyawa = internalSenyawa;
                } catch (PropertyVetoException ex) {
                    LOGGER.error(ex.getMessage(), ex);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Proses ini akan membuka jendela menu Senyawa,\n"
                        + "silahkan tutup salah satu jendela menu yang terbuka", "WARRNING", JOptionPane.WARNING_MESSAGE);
            }
        }
        return internalSenyawa;

    }

    private void mappingData(EntitySenyawa entitySenyawa) {
        if (cekFrame() instanceof InternalSenyawa) {
            InternalSenyawa internalSenyawa;
            internalSenyawa = (InternalSenyawa) cekFrame();
            internalSenyawa.getTxtIdSenyawa().setText(entitySenyawa.getIdSenyawa().toString());
            internalSenyawa.getTxtnamaZat().setText(entitySenyawa.getNamaZat());
            internalSenyawa.getTxtRempiris().setText(entitySenyawa.getRumusEmpiris());
            internalSenyawa.getTxtRMolekul().setText(entitySenyawa.getRumusMolekul());
            internalSenyawa.setSenyawaObjekOld(entitySenyawa.getNamaZat(),
                    entitySenyawa.getRumusEmpiris(), entitySenyawa.getRumusMolekul());
        }
    }

    private EntitySenyawa validasiRunEdit() {
        EntitySenyawa entitySenyawa = null;
        int rowCount = tabelSenyawa.getRowCount();
        int selectedRowCount = tabelSenyawa.getSelectedRowCount();
        if (rowCount > 0) {
            if (selectedRowCount == 1) {
                int selectedRow = tabelSenyawa.getSelectedRow();
                int convertRowIndexToModel = tabelSenyawa.convertRowIndexToModel(selectedRow);
                entitySenyawa = tabelModelSenyawa.get(convertRowIndexToModel);
            }
        }
        return entitySenyawa;


    }

    private int[] validasiRunDelete() {
        int[] selectedRows = null;
        int rowCount = tabelSenyawa.getRowCount();
        int selectedRowCount = tabelSenyawa.getSelectedRowCount();
        if (rowCount > 0) {
            if (selectedRowCount > 0) {
                selectedRows = tabelSenyawa.getSelectedRows();
            }
        }
        return selectedRows;


    }

    private void deleteSenyawa() {
        int[] validasiRunDelete;
        validasiRunDelete = validasiRunDelete();
        if (validasiRunDelete != null) {
            int showConfirmDialog = JOptionPane.showConfirmDialog(this, "Proses delete data yang terseleksi akan dijalankan ?", "WARRNING", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (JOptionPane.YES_OPTION == showConfirmDialog) {

                for (int index = validasiRunDelete.length - 1; index >= 0; index--) {
                    EntitySenyawa entitySenyawa = tabelModelSenyawa.get(validasiRunDelete[index]);
                    HelperConnection.getDaoSenyawa().delete(entitySenyawa.getIdSenyawa());
                    tabelModelSenyawa.remove(validasiRunDelete[index]);
                    eventInRule(entitySenyawa);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Silahkan seleksi data", "WARRNING", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void eventInRule(EntitySenyawa entitySenyawa) {
        if (deteksiSenyawa.internalRule instanceof InternalRule) {
            InternalRule internalRule;
            internalRule = (InternalRule) deteksiSenyawa.internalRule;
            internalRule.getComboBoxModelSenyawa().removeElement(entitySenyawa);
            if (internalRule.getComboBoxModelSenyawa().getSize() == 0) {
                internalRule.getListModelCiri().removeAllElements();
            }
        }
    }

    private String validasiCari() {
        String cari = null;
        if (!txtCariSenyawa.getText().trim().isEmpty()) {
            cari = txtCariSenyawa.getText().trim();
        } else {
            JOptionPane.showMessageDialog(this, "Mohon isi kolom pencarian,\n"
                    + "pencarian berdasarkan nama zat kimia", "WARRNING", JOptionPane.WARNING_MESSAGE);
        }
        return cari;

    }

    private void loadCari(final String T) {
        new SwingWorker<List<EntitySenyawa>, Object>() {
            @Override
            protected List<EntitySenyawa> doInBackground() throws Exception {
                DaoSenyawa daoSenyawa;
                daoSenyawa = HelperConnection.getDaoSenyawa();
                List<EntitySenyawa> cari;
                Thread.sleep(250);
                cari = daoSenyawa.getSearch(T);
                return cari;

            }

            @Override
            protected void done() {
                if (tabelModelSenyawa instanceof DynamicTableModel) {
                    try {
                        if (!get().isEmpty()) {
                            tabelModelSenyawa.clear();
                            for (EntitySenyawa enS : get()) {
                                tabelModelSenyawa.add(enS);
                            }
                        } else {
                            JOptionPane.showMessageDialog(InternalTBSenyawa.this, "Data yang anda cari tidak ditemukan", "INFORMASI", JOptionPane.INFORMATION_MESSAGE);
                        }
                    } catch (InterruptedException | ExecutionException ex) {
                        LOGGER.error(ex.getMessage(), ex);
                    }
                }
            }
        }.execute();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelLatarTBSenyawa = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        panelCariTBSenyawa = new javax.swing.JPanel();
        btnReloadSenyawa = new javax.swing.JButton();
        btnCariSenyawa = new javax.swing.JButton();
        txtCariSenyawa = new com.uiMIF.JTextEx();
        jPanel5 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabelSenyawa = new com.stripbandunk.jwidget.JDynamicTable();
        panelCrudTbSenyawa = new javax.swing.JPanel();
        btnEditSenyawa = new javax.swing.JButton();
        btnDeleteSenyawa = new javax.swing.JButton();

        setClosable(true);
        setTitle("TB_Zat");
        addInternalFrameListener(new javax.swing.event.InternalFrameListener() {
            public void internalFrameActivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt) {
                formInternalFrameClosed(evt);
            }
            public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeactivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeiconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameIconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt) {
            }
        });

        btnReloadSenyawa.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        btnReloadSenyawa.setText("Reload");
        btnReloadSenyawa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReloadSenyawaActionPerformed(evt);
            }
        });

        btnCariSenyawa.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        btnCariSenyawa.setText("Cari");
        btnCariSenyawa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCariSenyawaActionPerformed(evt);
            }
        });

        txtCariSenyawa.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        txtCariSenyawa.setMaxlength(550);

        javax.swing.GroupLayout panelCariTBSenyawaLayout = new javax.swing.GroupLayout(panelCariTBSenyawa);
        panelCariTBSenyawa.setLayout(panelCariTBSenyawaLayout);
        panelCariTBSenyawaLayout.setHorizontalGroup(
            panelCariTBSenyawaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelCariTBSenyawaLayout.createSequentialGroup()
                .addContainerGap(334, Short.MAX_VALUE)
                .addComponent(btnReloadSenyawa)
                .addGap(29, 29, 29))
            .addGroup(panelCariTBSenyawaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panelCariTBSenyawaLayout.createSequentialGroup()
                    .addGap(9, 9, 9)
                    .addComponent(btnCariSenyawa)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(txtCariSenyawa, javax.swing.GroupLayout.PREFERRED_SIZE, 253, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(116, Short.MAX_VALUE)))
        );
        panelCariTBSenyawaLayout.setVerticalGroup(
            panelCariTBSenyawaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCariTBSenyawaLayout.createSequentialGroup()
                .addComponent(btnReloadSenyawa)
                .addGap(0, 14, Short.MAX_VALUE))
            .addGroup(panelCariTBSenyawaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panelCariTBSenyawaLayout.createSequentialGroup()
                    .addGap(2, 2, 2)
                    .addGroup(panelCariTBSenyawaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtCariSenyawa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnCariSenyawa))
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        jPanel3.add(panelCariTBSenyawa);

        jPanel5.setLayout(new java.awt.BorderLayout());

        tabelSenyawa.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(tabelSenyawa);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 580, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 391, Short.MAX_VALUE)
        );

        jPanel5.add(jPanel4, java.awt.BorderLayout.CENTER);

        btnEditSenyawa.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        btnEditSenyawa.setText("Edit");
        btnEditSenyawa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditSenyawaActionPerformed(evt);
            }
        });
        panelCrudTbSenyawa.add(btnEditSenyawa);

        btnDeleteSenyawa.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        btnDeleteSenyawa.setText("Delete");
        btnDeleteSenyawa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteSenyawaActionPerformed(evt);
            }
        });
        panelCrudTbSenyawa.add(btnDeleteSenyawa);

        javax.swing.GroupLayout panelLatarTBSenyawaLayout = new javax.swing.GroupLayout(panelLatarTBSenyawa);
        panelLatarTBSenyawa.setLayout(panelLatarTBSenyawaLayout);
        panelLatarTBSenyawaLayout.setHorizontalGroup(
            panelLatarTBSenyawaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(panelLatarTBSenyawaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(panelCrudTbSenyawa, javax.swing.GroupLayout.DEFAULT_SIZE, 613, Short.MAX_VALUE))
        );
        panelLatarTBSenyawaLayout.setVerticalGroup(
            panelLatarTBSenyawaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelLatarTBSenyawaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 391, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(52, Short.MAX_VALUE))
            .addGroup(panelLatarTBSenyawaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelLatarTBSenyawaLayout.createSequentialGroup()
                    .addGap(0, 457, Short.MAX_VALUE)
                    .addComponent(panelCrudTbSenyawa, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        getContentPane().add(panelLatarTBSenyawa, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formInternalFrameClosed(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosed
        // TODO add your handling code here:
        deteksiSenyawa.internalTBSenyawa = null;
        deteksiSenyawa = null;
    }//GEN-LAST:event_formInternalFrameClosed

    private void btnEditSenyawaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditSenyawaActionPerformed
        // TODO add your handling code here:
        if (validasiRunEdit() instanceof EntitySenyawa) {
            EntitySenyawa entitySenyawa = (EntitySenyawa) validasiRunEdit();

            mappingData(entitySenyawa);

        } else {
            tabelSenyawa.clearSelection();
            JOptionPane.showMessageDialog(this, "Silahkan seleksi salah satu data", "WARRNING", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_btnEditSenyawaActionPerformed

    private void btnDeleteSenyawaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteSenyawaActionPerformed
        // TODO add your handling code here:

        deleteSenyawa();

    }//GEN-LAST:event_btnDeleteSenyawaActionPerformed

    private void btnCariSenyawaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCariSenyawaActionPerformed
        // TODO add your handling code here:
        if (validasiCari() instanceof String) {
            String string;
            string = (String) validasiCari();
            loadCari(string);
        }
    }//GEN-LAST:event_btnCariSenyawaActionPerformed

    private void btnReloadSenyawaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReloadSenyawaActionPerformed
        // TODO add your handling code here:
        loadSenyawa();
        txtCariSenyawa.setText("");
    }//GEN-LAST:event_btnReloadSenyawaActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCariSenyawa;
    private javax.swing.JButton btnDeleteSenyawa;
    private javax.swing.JButton btnEditSenyawa;
    private javax.swing.JButton btnReloadSenyawa;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel panelCariTBSenyawa;
    private javax.swing.JPanel panelCrudTbSenyawa;
    private javax.swing.JPanel panelLatarTBSenyawa;
    private com.stripbandunk.jwidget.JDynamicTable tabelSenyawa;
    private com.uiMIF.JTextEx txtCariSenyawa;
    // End of variables declaration//GEN-END:variables
}
