/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jmat.deteksisenyawa.internalframe.hipotesis;

import com.stripbandunk.jwidget.model.DynamicTableModel;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import jmat.deteksisenyawa.dao.DaoCiri;
import jmat.deteksisenyawa.dao.DaoRule;
import jmat.deteksisenyawa.entity.EntityCiri;
import jmat.deteksisenyawa.entity.EntityRule;
import jmat.deteksisenyawa.entity.EntitySenyawa;
import jmat.deteksisenyawa.frame.DeteksiSenyawa;
import jmat.deteksisenyawa.helper.HelperConnection;
import jmat.deteksisenyawa.internalframe.ciri.renderList.RenderListCiri;
import jmat.deteksisenyawa.internalframe.rule.InternalRule;
import org.apache.log4j.Logger;

/**
 *
 * @author hasani
 */
public class InternalH_Senyawa extends javax.swing.JInternalFrame {

    /**
     * Creates new form InternalH_Senyawa
     */
    private static final Logger LOGGER = Logger.getLogger(InternalRule.class);
    private DeteksiSenyawa deteksiSenyawa;
    private DefaultListModel<EntityCiri> listModelCiri;
    private DynamicTableModel<EntityCiri> tableModelCiri;
    private List<EntityRule> wallkingMemory = new ArrayList<>();
    private List<EntitySenyawa> objekSenyawa = new ArrayList<>();
    private List<EntitySenyawa> swapWallkingMemory = new ArrayList<>();
    private List<String> heapHistoryWallking = new ArrayList<>();
    private String concat = "";
    private int eqaulsJum = 0;

    public InternalH_Senyawa() {
        initComponents();
        setList();
        setTabel();
        loadCiri();
    }

    private void setList() {
        listModelCiri = new DefaultListModel<>();
        listH_Ciri.setModel(listModelCiri);
        listH_Ciri.setCellRenderer(new RenderListCiri());
    }

    private void setTabel() {

        tableModelCiri = new DynamicTableModel<>(EntityCiri.class);
        tabelH_Ciri.setDynamicModel(tableModelCiri);
        tabelH_Ciri.setShowGrid(true);
        tabelH_Ciri.setRowHeight(24);
    }

    private void loadCiri() {
        new SwingWorker<List<EntityCiri>, Object>() {
            @Override
            protected List<EntityCiri> doInBackground() throws Exception {
                DaoCiri daoCiri;
                daoCiri = HelperConnection.getDaoCiri();
                List<EntityCiri> all;
                Thread.sleep(280);
                all = daoCiri.getAll();
                return all;
            }

            @Override
            protected void done() {
                try {
                    tableModelCiri.clear();
                    for (EntityCiri enCri : get()) {
                        tableModelCiri.add(enCri);
                    }
                    removeDuplicateCiri();
                    txtH_Cari.setText("");
                } catch (InterruptedException | ExecutionException ex) {
                    LOGGER.error(ex.getMessage(), ex);
                }
            }
        }.execute();

    }

    private void loadCariCiri(final String penjelasan) {
        new SwingWorker<List<EntityCiri>, Object>() {
            @Override
            protected List<EntityCiri> doInBackground() throws Exception {
                DaoCiri daoCiri;
                daoCiri = HelperConnection.getDaoCiri();
                List<EntityCiri> getCari;
                Thread.sleep(300);
                getCari = daoCiri.getSearch(penjelasan);
                return getCari;
            }

            @Override
            protected void done() {
                try {
                    if (!get().isEmpty()) {

                        tableModelCiri.clear();
                        for (EntityCiri enCri : get()) {
                            tableModelCiri.add(enCri);
                        }
                        removeDuplicateCiri();
                        if (tableModelCiri.getRowCount() == 0) {
                            JOptionPane.showMessageDialog(InternalH_Senyawa.this, "Silahkan cekking pada penampung ciri senyawa,"
                                    + "\nkemungkinan data yang anda maksud ada di disitu.",
                                    "WARRNING", JOptionPane.WARNING_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(InternalH_Senyawa.this, "Data yang dicari tidak ditemukan.",
                                "WARRNING", JOptionPane.WARNING_MESSAGE);
                    }
                } catch (InterruptedException | ExecutionException ex) {
                    LOGGER.error(ex.getMessage(), ex);
                }
            }
        }.execute();
    }

    private void removeDuplicateCiri() {
        int sizeList = listModelCiri.getSize() - 1;
        int rowCount = tableModelCiri.getRowCount() - 1;
        List<Integer> removeByIndex;
        removeByIndex = new ArrayList<>();
        if (sizeList >= 0 && rowCount >= 0) {
            for (int indexT = 0; indexT <= rowCount; indexT++) {
                for (int indexL = 0; indexL <= sizeList; indexL++) {
                    if (tableModelCiri.get(indexT).equals(listModelCiri.get(indexL))) {
                        removeByIndex.add(indexT);
                    }
                }
            }
        }
        if (!removeByIndex.isEmpty()) {
            for (int rm = removeByIndex.size() - 1; rm >= 0; rm--) {
                tableModelCiri.remove(removeByIndex.get(rm));
            }
        }
    }

    private String validasiCari() {
        String cari = null;
        if (!txtH_Cari.getText().trim().isEmpty()) {
            cari = txtH_Cari.getText().trim();
        } else {
            JOptionPane.showMessageDialog(this, "Mohon isi kolom pencarian.", "WARRNING", JOptionPane.WARNING_MESSAGE);
        }
        return cari;
    }

    private Boolean validasiaddCiri() {
        boolean cek = false;
        int selectedRowCount;
        selectedRowCount = tabelH_Ciri.getSelectedRowCount();
        if (selectedRowCount > 0) {
            cek = true;
        }

        return cek;

    }

    private List<EntityCiri> addCiri() {
        List<EntityCiri> entityCiris = new ArrayList<>();
        int[] selectedRows;
        selectedRows = tabelH_Ciri.getSelectedRows();
        for (Integer in : selectedRows) {
            entityCiris.add(tableModelCiri.get(in));
        }
        for (int rem = selectedRows.length - 1; rem >= 0; rem--) {
            tableModelCiri.remove(selectedRows[rem]);
        }
        return entityCiris;


    }

    private Boolean validasiRemoveCiri() {
        boolean cek = false;
        int[] selectedIndices = listH_Ciri.getSelectedIndices();
        if (selectedIndices != null) {
            if (selectedIndices.length > 0) {
                cek = true;
            }
        }
        return cek;

    }

    public DeteksiSenyawa getDeteksiSenyawa() {
        return deteksiSenyawa;
    }

    public void setDeteksiSenyawa(DeteksiSenyawa deteksiSenyawa) {
        this.deteksiSenyawa = deteksiSenyawa;
    }

    public List<EntityRule> getWallkingMemory() {
        return wallkingMemory;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        dialogHipotesis1 = new jmat.deteksisenyawa.internalframe.hipotesis.DialogHipotesis();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtH_Cari = new javax.swing.JTextArea();
        btnH_Cari = new javax.swing.JButton();
        btnH_Reload = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tabelH_Ciri = new com.stripbandunk.jwidget.JDynamicTable();
        jPanel7 = new javax.swing.JPanel();
        btnH_AddCiri = new javax.swing.JButton();
        btnH_RemoveCiri = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        listH_Ciri = new javax.swing.JList();
        jPanel9 = new javax.swing.JPanel();
        btnH_Create = new javax.swing.JButton();
        btnH_Clear = new javax.swing.JButton();
        btnUpHipostesis = new javax.swing.JButton();
        btnDownHipotesis = new javax.swing.JButton();

        setClosable(true);
        setTitle("Menu Create Hipotesis");
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

        jPanel2.setLayout(new java.awt.BorderLayout());

        txtH_Cari.setColumns(20);
        txtH_Cari.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        txtH_Cari.setRows(5);
        jScrollPane1.setViewportView(txtH_Cari);

        btnH_Cari.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        btnH_Cari.setText("Cari");
        btnH_Cari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnH_CariActionPerformed(evt);
            }
        });

        btnH_Reload.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        btnH_Reload.setText("Reload");
        btnH_Reload.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnH_ReloadActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnH_Reload)
                    .addComponent(btnH_Cari))
                .addContainerGap(311, Short.MAX_VALUE))
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                    .addContainerGap(89, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 283, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap()))
        );

        jPanel3Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btnH_Cari, btnH_Reload});

        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnH_Cari)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnH_Reload)
                .addContainerGap(38, Short.MAX_VALUE))
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap()))
        );

        jPanel4.add(jPanel3);

        jPanel2.add(jPanel4, java.awt.BorderLayout.PAGE_START);

        jScrollPane2.setViewportView(tabelH_Ciri);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 476, Short.MAX_VALUE)
            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 452, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 380, Short.MAX_VALUE)
            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 356, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        jPanel5.add(jPanel6);

        jPanel2.add(jPanel5, java.awt.BorderLayout.CENTER);

        btnH_AddCiri.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        btnH_AddCiri.setText(">");
        btnH_AddCiri.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnH_AddCiriActionPerformed(evt);
            }
        });

        btnH_RemoveCiri.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        btnH_RemoveCiri.setText("<");
        btnH_RemoveCiri.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnH_RemoveCiriActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnH_RemoveCiri, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnH_AddCiri, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(21, Short.MAX_VALUE))
        );

        jPanel7Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btnH_AddCiri, btnH_RemoveCiri});

        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(188, 188, 188)
                .addComponent(btnH_AddCiri)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnH_RemoveCiri)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel1.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        jLabel1.setText("<html><I>Untuk create hipotesis senyawa,<br/> minimal anda menampung 3 ciri senyawa. <br/>Tampung ciri di bawah ini :<I></html>");

        listH_Ciri.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        listH_Ciri.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane3.setViewportView(listH_Ciri);

        btnH_Create.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        btnH_Create.setText("Create");
        btnH_Create.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnH_CreateActionPerformed(evt);
            }
        });
        jPanel9.add(btnH_Create);

        btnH_Clear.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        btnH_Clear.setText("Clear");
        btnH_Clear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnH_ClearActionPerformed(evt);
            }
        });
        jPanel9.add(btnH_Clear);

        btnUpHipostesis.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        btnUpHipostesis.setText("UP");
        btnUpHipostesis.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpHipostesisActionPerformed(evt);
            }
        });

        btnDownHipotesis.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        btnDownHipotesis.setText("Down");
        btnDownHipotesis.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDownHipotesisActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane3)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnUpHipostesis)
                    .addComponent(btnDownHipotesis))
                .addContainerGap(36, Short.MAX_VALUE))
        );

        jPanel8Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btnDownHipotesis, btnUpHipostesis});

        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 353, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(btnUpHipostesis)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnDownHipotesis)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formInternalFrameClosed(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosed
        // TODO add your handling code here:
        deteksiSenyawa.getjMenuBar1().setVisible(true);
        deteksiSenyawa.internalH_Senyawa = null;
        deteksiSenyawa = null;
    }//GEN-LAST:event_formInternalFrameClosed

    private void btnH_ClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnH_ClearActionPerformed
        // TODO add your handling code here:
        if (listModelCiri.getSize() > 0) {
            listModelCiri.removeAllElements();
            loadCiri();
        }
    }//GEN-LAST:event_btnH_ClearActionPerformed

    private void btnH_ReloadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnH_ReloadActionPerformed
        // TODO add your handling code here:
        loadCiri();
    }//GEN-LAST:event_btnH_ReloadActionPerformed

    private void btnH_CariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnH_CariActionPerformed
        // TODO add your handling code here:
        if (validasiCari() instanceof String) {
            String string;
            string = (String) validasiCari();
            loadCariCiri(string);
        }
    }//GEN-LAST:event_btnH_CariActionPerformed

    private void btnH_AddCiriActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnH_AddCiriActionPerformed
        // TODO add your handling code here:
        if (validasiaddCiri() == true) {
            for (EntityCiri enCri : addCiri()) {
                listModelCiri.addElement(enCri);
            }
        } else {
            if (tableModelCiri.getRowCount() > 0) {

                JOptionPane.showMessageDialog(this, "Silahkan seleksi data ciri senyawa\n"
                        + "pada tabel disamping kiri anda.", "WARRNING", JOptionPane.WARNING_MESSAGE);
            }
        }
    }//GEN-LAST:event_btnH_AddCiriActionPerformed

    private void btnH_RemoveCiriActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnH_RemoveCiriActionPerformed
        // TODO add your handling code here:
        if (validasiRemoveCiri() == true) {
            List<EntityCiri> entityCiris = listH_Ciri.getSelectedValuesList();
            for (EntityCiri enCri : entityCiris) {
                tableModelCiri.add(enCri);
                listModelCiri.removeElement(enCri);
            }
        } else {
            if (listModelCiri.getSize() > 0) {

                JOptionPane.showMessageDialog(this, "Silahkan seleksi data ciri senyawa\n"
                        + "pada list disamping kanan anda.", "WARRNING", JOptionPane.WARNING_MESSAGE);
            }
        }
    }//GEN-LAST:event_btnH_RemoveCiriActionPerformed

    public void loadCiriByListZeroIndex() {

        final EntityCiri enCiri;
        enCiri = listModelCiri.get(0);
        //Cari senyawa yang memiliki ciri sama pada list index 0
        new SwingWorker<List<EntityRule>, Object>() {
            @Override
            protected List<EntityRule> doInBackground() throws Exception {
                DaoRule daoRule;
                daoRule = HelperConnection.getDaoRule();
                List<EntityRule> iByCiri;
                Thread.sleep(150);
                iByCiri = daoRule.getIByCiri(enCiri.getIdCiri());
                return iByCiri;

            }

            @Override
            protected void done() {
                try {
                    if (!get().isEmpty()) {
                        dialogHipotesis1.getTxtWalkingMemory().append("History Walking Memory In Program :\n\n");
                        dialogHipotesis1.getTxtWalkingMemory().append("Terdeteksi" + " : ");
                        concat = enCiri.getPenjelasan();
                        int number = 1;
                        for (EntityRule enRu : get()) {

                            objekSenyawa.add(enRu.getIdSenyawa());
                            dialogHipotesis1.getTxtWalkingMemory().append("\n" + number + ".  " + enRu.getIdSenyawa().getNamaZat());
                            number++;
                        }
                        dialogHipotesis1.getTxtWalkingMemory().append("\n");

                    } else {
                        objekSenyawa.clear();
                    }
                } catch (InterruptedException | ExecutionException ex) {
                    LOGGER.error(ex.getMessage(), ex);
                }
            }
        }.execute();

    }

    private void loadRuleIdBySenyawa(final EntitySenyawa entitySenyawa) {
        DaoRule daoRule;
        daoRule = HelperConnection.getDaoRule();
        EntityRule idBySenyawa;
        idBySenyawa = daoRule.getIdBySenyawa(entitySenyawa.getIdSenyawa());
        if (idBySenyawa instanceof EntityRule) {
            EntityRule entityRule;
            entityRule = (EntityRule) idBySenyawa;
            wallkingMemory.add(entityRule);
        }

    }

    private void CreatWallkingMemory() {
        new SwingWorker<List<EntitySenyawa>, Object>() {
            @Override
            protected List<EntitySenyawa> doInBackground() throws Exception {
                loadCiriByListZeroIndex();
                Thread.sleep(250);

                return objekSenyawa;

            }

            @Override
            protected void done() {
                try {
                    for (EntitySenyawa enSy : get()) {
                        Thread.sleep(300);
                        loadRuleIdBySenyawa(enSy);
                    }
                    int size = listModelCiri.getSize();
                    for (int ja = 0; ja < size; ja++) {
                        if (ja < size - 1) {
                            concat = concat.concat(" & " + listModelCiri.get(ja + 1).getPenjelasan());
                            heapHistoryWallking.add(concat);
                        }

                    }
                    if (!wallkingMemory.isEmpty()) {
                        dialogHipotesis1.getTxtWalkingMemory().append("\n\nFinish Rule : \n");
                        dialogHipotesis1.getTxtWalkingMemory().append(heapHistoryWallking.get(heapHistoryWallking.size() - 1));
                        for (EntityRule entityRule : wallkingMemory) {
                            finsihHipotesis(entityRule, size);
                            eqaulsJum = 0;
                        }
                    }
                    String createTable = "<html><table border=1>"
                            + "<thead>"
                            + "<th width=350px align=center>NAMA ZAT</th>"
                            + "<th width=350px align=center>RUMUS EMPIRIS</th>"
                            + "<th width=350px align=center>RUMUS MOLEKUL</th>"
                            + "</thead>";

                    if (!swapWallkingMemory.isEmpty()) {

                        String concatTable = createTable;
                        String createBody;
                        for (EntitySenyawa enSey : swapWallkingMemory) {

                            createBody =
                                    "<tbody><tr>"
                                    + "<td>" + enSey.getNamaZat() + "</td>"
                                    + "<td>" + enSey.getRumusEmpiris() + "</td>"
                                    + "<td>" + enSey.getRumusMolekul() + "</td>"
                                    + "</tr></tbody>";
                            concatTable = concatTable.concat(createBody);

                        }
                        dialogHipotesis1.getTxtHipotesisSenyawa().setText(concatTable + "</html></table>");
                    } else {
                        dialogHipotesis1.getTxtHipotesisSenyawa().setText(""
                                + "<center><h2>TIDAK ADA HIPOTESIS ZAT KIMIA</h2></center>");
                    }
                } catch (InterruptedException | ExecutionException ex) {
                    LOGGER.error(ex.getMessage(), ex);
                }
            }
        }.execute();
    }

    private void finsihHipotesis(EntityRule enRule, int size) {
        for (int ja = 0; ja < size; ja++) {


            for (EntityCiri entityCiri : enRule.getListCiri()) {

                if (listModelCiri.get(ja).getIdCiri().toString().equals(entityCiri.getIdCiri().toString())) {
                    eqaulsJum++;
                }

            }
            if (eqaulsJum == listModelCiri.getSize()) {
                dialogHipotesis1.getTxtWalkingMemory().append("\n - " + enRule.getIdSenyawa().getNamaZat());
                swapWallkingMemory.add(enRule.getIdSenyawa());
            }
        }
    }

    public void clearHipotesis() {
        swapWallkingMemory.clear();
        wallkingMemory.clear();
        objekSenyawa.clear();
        heapHistoryWallking.clear();
        eqaulsJum = 0;
        concat = "";
        dialogHipotesis1.getTxtHipotesisSenyawa().setText("");
        dialogHipotesis1.getTxtWalkingMemory().setText("");
    }
    private void btnH_CreateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnH_CreateActionPerformed
        // TODO add your handling code here:
        clearHipotesis();

        int size = listModelCiri.getSize();
        if (size >= 3) {
            CreatWallkingMemory();
            dialogHipotesis1.setLocationRelativeTo(this);
            dialogHipotesis1.setInternalH_Senyawa(this);
            dialogHipotesis1.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Baca keterangan di atas list tampung !!!",
                    "WARRNING", JOptionPane.WARNING_MESSAGE);

        }

    }//GEN-LAST:event_btnH_CreateActionPerformed
    private void setUpList() {

        if (listModelCiri.size() > 1) {
            EntityCiri selectedValue = (EntityCiri) listH_Ciri.getSelectedValue();
            int indexAsal = listH_Ciri.getSelectedIndex();
            int updIndex = listH_Ciri.getSelectedIndex() - 1;
            if (updIndex > -1) {
                listModelCiri.setElementAt(listModelCiri.get(updIndex), indexAsal);
                listModelCiri.setElementAt(selectedValue, updIndex);
                listH_Ciri.setSelectedIndex(updIndex);
            }
        }
    }

    private void setDownList() {
        if (listModelCiri.size() > 1) {
            EntityCiri selectedValue = (EntityCiri) listH_Ciri.getSelectedValue();
            int indexAsal = listH_Ciri.getSelectedIndex();
            int updIndex = listH_Ciri.getSelectedIndex() + 1;
            if (updIndex < listModelCiri.size()) {
                listModelCiri.setElementAt(listModelCiri.get(updIndex), indexAsal);
                listModelCiri.setElementAt(selectedValue, updIndex);
                listH_Ciri.setSelectedIndex(updIndex);
            }
        }
    }
    private void btnUpHipostesisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpHipostesisActionPerformed
        // TODO add your handling code here:
        int[] selectedIndices;
        selectedIndices = listH_Ciri.getSelectedIndices();
        if (selectedIndices.length == 1) {
            setUpList();
        } else {
            JOptionPane.showMessageDialog(this, "Seleksi salah satu data tampung !!!",
                    "WARRNING", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_btnUpHipostesisActionPerformed

    private void btnDownHipotesisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDownHipotesisActionPerformed
        // TODO add your handling code here:
        int[] selectedIndices;
        selectedIndices = listH_Ciri.getSelectedIndices();
        if (selectedIndices.length == 1) {
            setDownList();
        } else {
            JOptionPane.showMessageDialog(this, "Seleksi salah satu data tampung !!!",
                    "WARRNING", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_btnDownHipotesisActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDownHipotesis;
    private javax.swing.JButton btnH_AddCiri;
    private javax.swing.JButton btnH_Cari;
    private javax.swing.JButton btnH_Clear;
    private javax.swing.JButton btnH_Create;
    private javax.swing.JButton btnH_Reload;
    private javax.swing.JButton btnH_RemoveCiri;
    private javax.swing.JButton btnUpHipostesis;
    private jmat.deteksisenyawa.internalframe.hipotesis.DialogHipotesis dialogHipotesis1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JList listH_Ciri;
    private com.stripbandunk.jwidget.JDynamicTable tabelH_Ciri;
    private javax.swing.JTextArea txtH_Cari;
    // End of variables declaration//GEN-END:variables
}
