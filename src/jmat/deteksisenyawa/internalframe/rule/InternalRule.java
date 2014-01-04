/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jmat.deteksisenyawa.internalframe.rule;

import com.stripbandunk.jwidget.JDynamicTable;
import com.stripbandunk.jwidget.model.DynamicTableModel;
import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.SwingWorker;
import jmat.deteksisenyawa.controller.ControllerRule;
import jmat.deteksisenyawa.dao.DaoCiri;
import jmat.deteksisenyawa.dao.DaoRule;
import jmat.deteksisenyawa.dao.DaoSenyawa;
import jmat.deteksisenyawa.entity.EntityCiri;
import jmat.deteksisenyawa.entity.EntityRule;
import jmat.deteksisenyawa.entity.EntitySenyawa;
import jmat.deteksisenyawa.frame.DeteksiSenyawa;
import jmat.deteksisenyawa.helper.HelperConnection;
import jmat.deteksisenyawa.internalframe.ciri.renderCombo.RenderComboSenyawa;
import jmat.deteksisenyawa.internalframe.ciri.renderList.RenderListCiri;
import jmat.deteksisenyawa.model.ModelRule;
import org.apache.log4j.Logger;

/**
 *
 * @author hasani
 */
public class InternalRule extends javax.swing.JInternalFrame {

    /**
     * Creates new form InternalRule
     */
    private static final Logger LOGGER = Logger.getLogger(InternalRule.class);
    private DeteksiSenyawa deteksiSenyawa;
    private DynamicTableModel<EntityCiri> tabelModelCiri;
    private DefaultComboBoxModel<EntitySenyawa> comboBoxModelSenyawa;
    private DefaultListModel<EntityCiri> listModelCiri;
    private ModelRule modelRule;
    private ControllerRule controllerRule;

    public InternalRule() {
        initComponents();
        setBisnisLogic();
        setTabelModel();
        setComboModel();
        setListModel();
    }

    private void setBisnisLogic() {
        modelRule = new ModelRule();
        controllerRule = new ControllerRule();
        controllerRule.setModelRule(modelRule);
    }

    public DeteksiSenyawa getDeteksiSenyawa() {
        return deteksiSenyawa;
    }

    public void setDeteksiSenyawa(DeteksiSenyawa deteksiSenyawa) {
        this.deteksiSenyawa = deteksiSenyawa;
    }

    public void setTitleBaru(String titile) {
        this.setTitle(title);
        this.lblKeterangan.setVisible(false);
        this.rdNamaZaat.setSelected(true);
    }

    private void setTabelModel() {
        tabelModelCiri = new DynamicTableModel<>(EntityCiri.class);
        tabelPenjelasan.setDynamicModel(tabelModelCiri);
        tabelPenjelasan.setShowGrid(true);
        tabelPenjelasan.setRowHeight(24);

    }

    public JList getListPenjelasan() {
        return listPenjelasan;
    }

    public JComboBox getComboSenyawa() {
        return comboSenyawa;
    }

    public DefaultComboBoxModel<EntitySenyawa> getComboBoxModelSenyawa() {
        return comboBoxModelSenyawa;
    }

    public JButton getBtnAddPenjelasan() {
        return btnAddPenjelasan;
    }

    public JButton getBtnCari() {
        return btnCari;
    }

    public JButton getBtnReload() {
        return btnReload;
    }

    public JButton getBtnRemovePenjelasan() {
        return btnRemovePenjelasan;
    }

    public JButton getBtnSimpan() {
        return btnSimpan;
    }

    public JLabel getLblKeterangan() {
        return lblKeterangan;
    }

    public JProgressBar getProgresbarRule() {
        return progresbarRule;
    }

    public JDynamicTable getTabelPenjelasan() {
        return tabelPenjelasan;
    }

    public DynamicTableModel<EntityCiri> getTabelModelCiri() {
        return tabelModelCiri;
    }

    public JRadioButton getRdNamaZaat() {
        return rdNamaZaat;
    }

    public JRadioButton getRdRumusEmpiris() {
        return rdRumusEmpiris;
    }

    public JRadioButton getRdRumusMolekul() {

        return rdRumusMolekul;
    }

    public DefaultListModel<EntityCiri> getListModelCiri() {
        return listModelCiri;
    }

    public void loadCiriPenjelasan() {
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
                    tabelModelCiri.clear();
                    for (EntityCiri enCri : get()) {
                        tabelModelCiri.add(enCri);
                    }
                    removeDuplicateCiri();
                    txtCariPenjelasan.setText("");
                } catch (InterruptedException | ExecutionException ex) {
                    LOGGER.error(ex.getMessage(), ex);
                }
            }
        }.execute();
    }

    public void loadCariPenjelasan(final String penjelasan) {
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

                        tabelModelCiri.clear();
                        for (EntityCiri enCri : get()) {
                            tabelModelCiri.add(enCri);
                        }
                        removeDuplicateCiri();
                        if (tabelModelCiri.getRowCount() == 0) {
                            JOptionPane.showMessageDialog(InternalRule.this, "Silahkan cekking pada Pack Rule,"
                                    + "\nkemungkinan data yang anda maksud ada di disitu.",
                                    "WARRNING", JOptionPane.WARNING_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(InternalRule.this, "Data yang dicari tidak ditemukan.",
                                "WARRNING", JOptionPane.WARNING_MESSAGE);
                    }
                } catch (InterruptedException | ExecutionException ex) {
                    LOGGER.error(ex.getMessage(), ex);
                }
            }
        }.execute();
    }

    private String validasiCari() {
        String cari = null;
        if (!txtCariPenjelasan.getText().trim().isEmpty()) {
            cari = txtCariPenjelasan.getText().trim();
        } else {
            JOptionPane.showMessageDialog(this, "Mohon isi kolom pencarian.", "WARRNING", JOptionPane.WARNING_MESSAGE);
        }
        return cari;
    }

    private void setComboModel() {
        comboBoxModelSenyawa = new DefaultComboBoxModel<>();
        comboSenyawa.setModel(comboBoxModelSenyawa);
    }

    public void loadSenyawa() {
        new SwingWorker<List<EntitySenyawa>, Object>() {
            @Override
            protected List<EntitySenyawa> doInBackground() throws Exception {
                DaoSenyawa daoSenyawa;
                daoSenyawa = HelperConnection.getDaoSenyawa();
                List<EntitySenyawa> all;
                Thread.sleep(215);
                all = daoSenyawa.getAll();
                return all;
            }

            @Override
            protected void done() {
                try {
                    if (!get().isEmpty()) {
                        comboBoxModelSenyawa.removeAllElements();
                        for (EntitySenyawa enSeny : get()) {
                            comboBoxModelSenyawa.addElement(enSeny);
                        }
                    }
                } catch (InterruptedException | ExecutionException ex) {
                    LOGGER.error(ex.getMessage(), ex);
                }
            }
        }.execute();
    }

    private void setRenderCombo() {
        RenderComboSenyawa renderComboSenyawa;
        renderComboSenyawa = new RenderComboSenyawa();
        if (rdRumusEmpiris.isSelected() == true) {
            renderComboSenyawa.setNameCombo("RE");
            comboSenyawa.setRenderer(renderComboSenyawa);
            comboSenyawa.requestFocus();
        } else if (rdRumusMolekul.isSelected() == true) {
            renderComboSenyawa.setNameCombo("RM");
            comboSenyawa.setRenderer(renderComboSenyawa);
            comboSenyawa.requestFocus();
        } else {
            renderComboSenyawa.setNameCombo("NamaZat");
            comboSenyawa.setRenderer(renderComboSenyawa);
            comboSenyawa.requestFocus();
        }
    }

    private void setListModel() {
        listModelCiri = new DefaultListModel<>();
        listPenjelasan.setModel(listModelCiri);
        listPenjelasan.setCellRenderer(new RenderListCiri());
    }

    public void loadRule(final EntitySenyawa idSenyawa) {

        new SwingWorker<EntityRule, Object>() {
            @Override
            protected EntityRule doInBackground() throws Exception {

                DaoRule daoRule;
                daoRule = HelperConnection.getDaoRule();
                EntityRule entityRule;
                Thread.sleep(290);
                entityRule = daoRule.getIdBySenyawa(idSenyawa.getIdSenyawa());
                return entityRule;

            }

            @Override
            protected void done() {
                try {
                    if (get() instanceof EntityRule) {
                        EntityRule entityRule;
                        entityRule = (EntityRule) get();
                        if (!entityRule.getListCiri().isEmpty()) {
                            listModelCiri.removeAllElements();
                            for (EntityCiri enCiri : entityRule.getListCiri()) {
                                listModelCiri.addElement(enCiri);
                            }
                            loadCiriPenjelasan();
                        } else {
                            if (listModelCiri.size() > 0) {
                                listModelCiri.removeAllElements();
                                loadCiriPenjelasan();
                            }
                        }
                    }
                } catch (InterruptedException | ExecutionException ex) {
                    LOGGER.error(ex.getMessage(), ex);
                }
            }
        }.execute();
    }

    public void removeDuplicateCiri() {
        int sizeList = listModelCiri.getSize() - 1;
        int rowCount = tabelModelCiri.getRowCount() - 1;
        List<Integer> removeByIndex;
        removeByIndex = new ArrayList<>();
        if (sizeList >= 0 && rowCount >= 0) {
            for (int indexT = 0; indexT <= rowCount; indexT++) {
                for (int indexL = 0; indexL <= sizeList; indexL++) {
                    if (tabelModelCiri.get(indexT).equals(listModelCiri.get(indexL))) {
                        removeByIndex.add(indexT);
                    }
                }
            }
        }
        if (!removeByIndex.isEmpty()) {
            for (int rm = removeByIndex.size() - 1; rm >= 0; rm--) {
                tabelModelCiri.remove(removeByIndex.get(rm));
            }
        }
    }

    private Boolean validasiaddCiri() {
        boolean cek = false;
        int selectedRowCount;
        selectedRowCount = tabelPenjelasan.getSelectedRowCount();


        if (selectedRowCount > 0) {
            cek = true;
        }

        return cek;

    }

    private List<EntityCiri> addCiri() {
        List<EntityCiri> entityCiris = new ArrayList<>();
        int[] selectedRows;
        selectedRows = tabelPenjelasan.getSelectedRows();
        for (Integer in : selectedRows) {
            entityCiris.add(tabelModelCiri.get(in));
        }
        for (int rem = selectedRows.length - 1; rem >= 0; rem--) {
            tabelModelCiri.remove(selectedRows[rem]);
        }
        return entityCiris;


    }

    private Boolean validasiRemoveCiri() {
        boolean cek = false;
        int[] selectedIndices = listPenjelasan.getSelectedIndices();
        if (selectedIndices != null) {
            if (selectedIndices.length > 0) {
                cek = true;
            }
        }
        return cek;

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnGroupRenderCombo = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtCariPenjelasan = new javax.swing.JTextArea();
        btnCari = new javax.swing.JButton();
        btnReload = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tabelPenjelasan = new com.stripbandunk.jwidget.JDynamicTable();
        jPanel3 = new javax.swing.JPanel();
        btnAddPenjelasan = new javax.swing.JButton();
        btnRemovePenjelasan = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        rdNamaZaat = new javax.swing.JRadioButton();
        rdRumusEmpiris = new javax.swing.JRadioButton();
        rdRumusMolekul = new javax.swing.JRadioButton();
        jPanel9 = new javax.swing.JPanel();
        comboSenyawa = new javax.swing.JComboBox();
        jScrollPane3 = new javax.swing.JScrollPane();
        listPenjelasan = new javax.swing.JList();
        jPanel5 = new javax.swing.JPanel();
        btnSimpan = new javax.swing.JButton();
        jPanel10 = new javax.swing.JPanel();
        progresbarRule = new javax.swing.JProgressBar();
        lblKeterangan = new javax.swing.JLabel();

        setClosable(true);
        setTitle("Rule Senyawa");
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

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Penjelasan Ciri", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Abyssinica SIL", 1, 12))); // NOI18N

        txtCariPenjelasan.setColumns(20);
        txtCariPenjelasan.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        txtCariPenjelasan.setRows(5);
        jScrollPane1.setViewportView(txtCariPenjelasan);

        btnCari.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        btnCari.setText("Cari");
        btnCari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCariActionPerformed(evt);
            }
        });

        btnReload.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        btnReload.setText("Reload");
        btnReload.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReloadActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnCari, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnReload, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1)
                .addContainerGap())
        );

        jPanel6Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btnCari, btnReload});

        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(btnCari)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnReload)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jScrollPane2.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        jScrollPane2.setViewportView(tabelPenjelasan);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 506, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btnAddPenjelasan.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        btnAddPenjelasan.setText(">");
        btnAddPenjelasan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddPenjelasanActionPerformed(evt);
            }
        });

        btnRemovePenjelasan.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        btnRemovePenjelasan.setText("<");
        btnRemovePenjelasan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemovePenjelasanActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnRemovePenjelasan, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 45, Short.MAX_VALUE)
                    .addComponent(btnAddPenjelasan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(226, 226, 226)
                .addComponent(btnAddPenjelasan)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnRemovePenjelasan)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Rule", javax.swing.border.TitledBorder.RIGHT, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Abyssinica SIL", 1, 12))); // NOI18N

        jLabel1.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        jLabel1.setText("Show in combo :");

        btnGroupRenderCombo.add(rdNamaZaat);
        rdNamaZaat.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        rdNamaZaat.setText("Nama Zat");
        rdNamaZaat.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                rdNamaZaatItemStateChanged(evt);
            }
        });

        btnGroupRenderCombo.add(rdRumusEmpiris);
        rdRumusEmpiris.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        rdRumusEmpiris.setText("Rumus Empiris");
        rdRumusEmpiris.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                rdRumusEmpirisItemStateChanged(evt);
            }
        });

        btnGroupRenderCombo.add(rdRumusMolekul);
        rdRumusMolekul.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        rdRumusMolekul.setText("Rumus Molekul");
        rdRumusMolekul.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                rdRumusMolekulItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(rdNamaZaat)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(rdRumusEmpiris)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(rdRumusMolekul)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(rdNamaZaat)
                    .addComponent(rdRumusEmpiris)
                    .addComponent(rdRumusMolekul))
                .addContainerGap(16, Short.MAX_VALUE))
        );

        jPanel9.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Pack Rule", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Abyssinica SIL", 1, 12))); // NOI18N
        jPanel9.setFont(new java.awt.Font("Ubuntu", 0, 15)); // NOI18N

        comboSenyawa.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        comboSenyawa.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        comboSenyawa.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                comboSenyawaItemStateChanged(evt);
            }
        });

        listPenjelasan.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        listPenjelasan.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane3.setViewportView(listPenjelasan);

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(comboSenyawa, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane3))
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(comboSenyawa, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        btnSimpan.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        btnSimpan.setText("Simpan");
        btnSimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSimpanActionPerformed(evt);
            }
        });

        lblKeterangan.setFont(new java.awt.Font("Ubuntu", 0, 16)); // NOI18N
        lblKeterangan.setForeground(new java.awt.Color(66, 208, 75));
        lblKeterangan.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblKeterangan.setText("Sukses Menyimpan data");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(progresbarRule, javax.swing.GroupLayout.PREFERRED_SIZE, 389, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblKeterangan)
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblKeterangan)
                    .addComponent(progresbarRule, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnSimpan)
                .addGap(18, 18, 18)
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnSimpan)
                    .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jPanel2, jPanel4});

        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 41, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formInternalFrameClosed(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosed
        // TODO add your handling code here:
        deteksiSenyawa.internalRule = null;
        deteksiSenyawa = null;
    }//GEN-LAST:event_formInternalFrameClosed

    private void btnReloadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReloadActionPerformed
        // TODO add your handling code here:
        loadCiriPenjelasan();
        
    }//GEN-LAST:event_btnReloadActionPerformed

    private void btnCariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCariActionPerformed
        // TODO add your handling code here:
        if (validasiCari() instanceof String) {
            String string;
            string = (String) validasiCari();
            loadCariPenjelasan(string);
        }
    }//GEN-LAST:event_btnCariActionPerformed

    private void rdNamaZaatItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_rdNamaZaatItemStateChanged
        // TODO add your handling code here:
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            setRenderCombo();
        }
    }//GEN-LAST:event_rdNamaZaatItemStateChanged

    private void rdRumusEmpirisItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_rdRumusEmpirisItemStateChanged
        // TODO add your handling code here:
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            setRenderCombo();
        }
    }//GEN-LAST:event_rdRumusEmpirisItemStateChanged

    private void rdRumusMolekulItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_rdRumusMolekulItemStateChanged
        // TODO add your handling code here:
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            setRenderCombo();
        }
    }//GEN-LAST:event_rdRumusMolekulItemStateChanged

    private void comboSenyawaItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_comboSenyawaItemStateChanged
        // TODO add your handling code here:
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            EntitySenyawa entitySenyawa;
            entitySenyawa = (EntitySenyawa) comboBoxModelSenyawa.getSelectedItem();
            loadRule(entitySenyawa);

        }
    }//GEN-LAST:event_comboSenyawaItemStateChanged

    private void btnAddPenjelasanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddPenjelasanActionPerformed
        // TODO add your handling code here:
        if (validasiaddCiri() == true) {
            if (comboBoxModelSenyawa.getSize()>0) {

                for (EntityCiri enCri : addCiri()) {
                    listModelCiri.addElement(enCri);
                }
            }else{
                JOptionPane.showMessageDialog(this, "Tidak ada senyawa yang terdeteksi.", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            if (tabelModelCiri.getRowCount() > 0) {

                JOptionPane.showMessageDialog(this, "Silahkan seleksi data ciri senyawa\n"
                        + "pada tabel disamping kiri anda.", "WARRNING", JOptionPane.WARNING_MESSAGE);
            }
        }
    }//GEN-LAST:event_btnAddPenjelasanActionPerformed

    private void btnRemovePenjelasanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemovePenjelasanActionPerformed
        // TODO add your handling code here:
        if (validasiRemoveCiri() == true) {
            List<EntityCiri> entityCiris = listPenjelasan.getSelectedValuesList();
            for (EntityCiri enCri : entityCiris) {
                tabelModelCiri.add(enCri);
                listModelCiri.removeElement(enCri);
            }
        } else {
            if (listModelCiri.getSize() > 0) {

                JOptionPane.showMessageDialog(this, "Silahkan seleksi data ciri senyawa\n"
                        + "pada list disamping kanan anda.", "WARRNING", JOptionPane.WARNING_MESSAGE);
            }
        }
    }//GEN-LAST:event_btnRemovePenjelasanActionPerformed

    private void btnSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSimpanActionPerformed
        // TODO add your handling code here:
        if (listModelCiri.size() > 0) {
            listPenjelasan.setSelectionInterval(0, listModelCiri.size() - 1);
            controllerRule.insertRule(this);
        } else {
            controllerRule.deleteRule(this);
        }
    }//GEN-LAST:event_btnSimpanActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddPenjelasan;
    private javax.swing.JButton btnCari;
    private javax.swing.ButtonGroup btnGroupRenderCombo;
    private javax.swing.JButton btnReload;
    private javax.swing.JButton btnRemovePenjelasan;
    private javax.swing.JButton btnSimpan;
    private javax.swing.JComboBox comboSenyawa;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
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
    private javax.swing.JLabel lblKeterangan;
    private javax.swing.JList listPenjelasan;
    private javax.swing.JProgressBar progresbarRule;
    private javax.swing.JRadioButton rdNamaZaat;
    private javax.swing.JRadioButton rdRumusEmpiris;
    private javax.swing.JRadioButton rdRumusMolekul;
    private com.stripbandunk.jwidget.JDynamicTable tabelPenjelasan;
    private javax.swing.JTextArea txtCariPenjelasan;
    // End of variables declaration//GEN-END:variables
}
