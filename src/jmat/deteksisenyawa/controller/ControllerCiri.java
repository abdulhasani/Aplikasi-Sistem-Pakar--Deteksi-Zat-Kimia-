/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jmat.deteksisenyawa.controller;

import java.util.concurrent.ExecutionException;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import jmat.deteksisenyawa.internalframe.ciri.InternalCiri;
import jmat.deteksisenyawa.internalframe.ciri.InternalTBCiri;
import jmat.deteksisenyawa.internalframe.rule.InternalRule;
import jmat.deteksisenyawa.model.ModelCiri;
import org.apache.log4j.Logger;

/**
 *
 * @author hasani
 */
public class ControllerCiri {
    
    private ModelCiri modelCiri;
    private static final Logger LOGGER = Logger.getLogger(ControllerCiri.class);
    
    public void setModelCiri(ModelCiri modelCiri) {
        this.modelCiri = modelCiri;
    }
    
    public void insertCiri(InternalCiri internalCiri) {
        String penjelasan;
        penjelasan = internalCiri.getTxtPenjelasan().getText().trim();
        if (penjelasan.isEmpty()) {
            JOptionPane.showMessageDialog(internalCiri, "Penjelasan wajib diisi", "WARRNING", JOptionPane.WARNING_MESSAGE);
        } else {
            modelCiri.setIdCiri(System.currentTimeMillis());
            modelCiri.setPenjelasan(penjelasan);
            runProgressBar(internalCiri);
        }
    }
    
    public void updateCiri(InternalCiri internalCiri) {
        String penjelasan;
        String idCiri;
        penjelasan = internalCiri.getTxtPenjelasan().getText().trim();
        idCiri = internalCiri.getTxtIdCiri().getText().trim();
        if (penjelasan.isEmpty()) {
            JOptionPane.showMessageDialog(internalCiri, "Penjelasan wajib diisi", "WARRNING", JOptionPane.WARNING_MESSAGE);
        } else {
            modelCiri.setIdCiri(Long.parseLong(idCiri));
            modelCiri.setPenjelasan(penjelasan);
            runProgressBar(internalCiri);
        }
    }
    
    private void runProgressBar(final InternalCiri internalCiri) {
        new SwingWorker<Integer, Object>() {
            @Override
            protected Integer doInBackground() throws Exception {
                Integer i = 0;
                for (int run = 0; run <= 100; run++) {
                    Thread.sleep(50);
                    internalCiri.getProgresbarCiri().setValue(run);
                    
                    if (run == 100) {
                        internalCiri.getProgresbarCiri().setValue(0);
                        internalCiri.getProgresbarCiri().setVisible(false);
                        internalCiri.getLblKeterangan().setText("Sukses menyimpan data");
                        internalCiri.getLblKeterangan().setVisible(true);
                        visibleComponent(internalCiri, true);
                        Thread.sleep(550);
                        internalCiri.getProgresbarCiri().setValue(0);
                        internalCiri.getProgresbarCiri().setVisible(true);
                        internalCiri.getLblKeterangan().setVisible(false);
                        i = run;
                    } else {
                        
                        visibleComponent(internalCiri, false);
                    }
                }
                return i;
            }
            
            @Override
            protected void done() {
                try {
                    if (get().intValue() == 100) {
                        if (internalCiri.getTitle().equals("Tambah Ciri Suatu Zat")) {
                            modelCiri.insert();
                            clear(internalCiri);
                        } else {
                            modelCiri.update();
                            clear(internalCiri);
                        }
                    }
                } catch (InterruptedException | ExecutionException ex) {
                    LOGGER.error(ex.getMessage(), ex);
                }
            }
        }.execute();
    }
    
    private void visibleComponent(InternalCiri internalCiri, Boolean cek) {
        internalCiri.getTxtPenjelasan().setEnabled(cek);
        internalCiri.getBtnSimpanCiri().setEnabled(cek);
        internalCiri.getBtnClearCiri().setEnabled(cek);
        internalCiri.getDeteksiSenyawa().getjMenuBar1().setVisible(cek);
        internalCiri.setClosable(cek);
        if (internalCiri.getDeteksiSenyawa().internalTBCiri instanceof InternalTBCiri) {
            InternalTBCiri internalTBCiri;
            internalTBCiri = (InternalTBCiri) internalCiri.getDeteksiSenyawa().internalTBCiri;
            internalTBCiri.getBtnCariCiri().setEnabled(cek);
            internalTBCiri.getBtnDeleteCiri().setEnabled(cek);
            internalTBCiri.getBtnEditCiri().setEnabled(cek);
            internalTBCiri.getTabelCiri().setEnabled(cek);
            internalTBCiri.setClosable(cek);
            internalTBCiri.getBtnReloadCiri().setEnabled(cek);
        }
        if (internalCiri.getDeteksiSenyawa().internalRule instanceof InternalRule) {
            InternalRule internalRule;
            internalRule = (InternalRule) internalCiri.getDeteksiSenyawa().internalRule;
            visibileComponentRule(internalRule, cek);
        }
    }
    
    private void visibileComponentRule(InternalRule internalRule, Boolean cek) {
        internalRule.getBtnAddPenjelasan().setEnabled(cek);
        internalRule.getBtnRemovePenjelasan().setEnabled(cek);
        internalRule.getBtnCari().setEnabled(cek);
        internalRule.getBtnReload().setEnabled(cek);
        internalRule.getBtnSimpan().setEnabled(cek);
        internalRule.getTabelPenjelasan().setEnabled(cek);
        internalRule.getListPenjelasan().setEnabled(cek);
        internalRule.getComboSenyawa().setEnabled(cek);
        internalRule.getRdNamaZaat().setEnabled(cek);
        internalRule.getRdRumusEmpiris().setEnabled(cek);
        internalRule.getRdRumusMolekul().setEnabled(cek);
        internalRule.setClosable(cek);
    }
    
    private void clear(InternalCiri internalCiri) {
        internalCiri.getTxtPenjelasan().setText("");
    }
}
