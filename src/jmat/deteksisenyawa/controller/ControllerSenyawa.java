/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jmat.deteksisenyawa.controller;

import java.util.concurrent.ExecutionException;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import jmat.deteksisenyawa.internalframe.rule.InternalRule;
import jmat.deteksisenyawa.internalframe.senyawa.InternalSenyawa;
import jmat.deteksisenyawa.internalframe.senyawa.InternalTBSenyawa;
import jmat.deteksisenyawa.model.ModelSenyawa;
import org.apache.log4j.Logger;

/**
 *
 * @author hasani
 */
public class ControllerSenyawa {
    
    private ModelSenyawa modelSenyawa;
    private static final Logger LOGGER = Logger.getLogger(ControllerSenyawa.class);
    
    public void setModelSenyawa(ModelSenyawa modelSenyawa) {
        this.modelSenyawa = modelSenyawa;
    }
    
    public void insertSenyawa(InternalSenyawa internalSenyawa) {
        String namaZat = internalSenyawa.getTxtnamaZat().getText().trim();
        String rumusEmpiris = internalSenyawa.getTxtRempiris().getText();
        String rumusMolekul = internalSenyawa.getTxtRMolekul().getText();
        
        if (namaZat.isEmpty()) {
            JOptionPane.showMessageDialog(internalSenyawa, "nama zat wajib diisi", "Warrning", JOptionPane.WARNING_MESSAGE);
            internalSenyawa.getTxtnamaZat().requestFocus();
        } else {
            modelSenyawa.setIdSenyawa(System.currentTimeMillis());
            modelSenyawa.setNamaZat(namaZat);
            modelSenyawa.setRumusEmpiris(rumusEmpiris);
            modelSenyawa.setRumusMolekul(rumusMolekul);
            runProgressBar(internalSenyawa);
        }
    }
    
    public void updateSenyawa(InternalSenyawa internalSenyawa) {
        String idSenyawa = internalSenyawa.getTxtIdSenyawa().getText().trim();
        String namaZat = internalSenyawa.getTxtnamaZat().getText().trim();
        String rumusEmpiris = internalSenyawa.getTxtRempiris().getText();
        String rumusMolekul = internalSenyawa.getTxtRMolekul().getText();
        
        if (namaZat.isEmpty()) {
            JOptionPane.showMessageDialog(internalSenyawa, "nama zat wajib diisi", "Warrning", JOptionPane.WARNING_MESSAGE);
            internalSenyawa.getTxtnamaZat().requestFocus();
        } else {
            modelSenyawa.setIdSenyawa(Long.parseLong(idSenyawa));
            modelSenyawa.setNamaZat(namaZat);
            modelSenyawa.setRumusEmpiris(rumusEmpiris);
            modelSenyawa.setRumusMolekul(rumusMolekul);
            runProgressBar(internalSenyawa);
        }
    }
    
    private void runProgressBar(final InternalSenyawa internalSenyawa) {
        new SwingWorker<Integer, Object>() {
            @Override
            protected Integer doInBackground() throws Exception {
                Integer i = 0;
                for (int run = 0; run <= 100; run++) {
                    Thread.sleep(50);
                    internalSenyawa.getProgresbarSenyawa().setValue(run);
                    
                    if (run == 100) {
                        internalSenyawa.getProgresbarSenyawa().setValue(0);
                        internalSenyawa.getProgresbarSenyawa().setVisible(false);
                        internalSenyawa.getLblKeterangan().setText("Sukses menyimpan data");
                        internalSenyawa.getLblKeterangan().setVisible(true);
                        visibleComponent(internalSenyawa, true);
                        Thread.sleep(550);
                        internalSenyawa.getProgresbarSenyawa().setValue(0);
                        internalSenyawa.getProgresbarSenyawa().setVisible(true);
                        internalSenyawa.getLblKeterangan().setVisible(false);
                        i = run;
                    } else {
                        
                        visibleComponent(internalSenyawa, false);
                    }
                }
                return i;
            }
            
            @Override
            protected void done() {
                try {
                    if (get().intValue() == 100) {
                        if (internalSenyawa.getTitle().equals("Tambah Zat")) {
                            modelSenyawa.insert();
                            clear(internalSenyawa);
                        } else {
                            modelSenyawa.update();
                            clear(internalSenyawa);
                        }
                    }
                } catch (InterruptedException | ExecutionException ex) {
                    LOGGER.error(ex.getMessage(), ex);
                }
            }
        }.execute();
    }
    
    private void visibleComponent(InternalSenyawa internalSenyawa, Boolean cek) {
        internalSenyawa.getBtnSimpan().setEnabled(cek);
        internalSenyawa.getBtnClear().setEnabled(cek);
        internalSenyawa.getTxtnamaZat().setEditable(cek);
        internalSenyawa.getTxtRempiris().setEditable(cek);
        internalSenyawa.getTxtRMolekul().setEditable(cek);
        internalSenyawa.getBtnSubRE().setEnabled(cek);
        internalSenyawa.getBtnSupRE().setEnabled(cek);
        internalSenyawa.getBtnNormalRE().setEnabled(cek);
        internalSenyawa.getBtnSubRM().setEnabled(cek);
        internalSenyawa.getBtnSupRM().setEnabled(cek);
        internalSenyawa.getBtnNormalRM().setEnabled(cek);
        internalSenyawa.setClosable(cek);
        internalSenyawa.getDeteksiSenyawa().getjMenuBar1().setVisible(cek);
        internalSenyawa.requestFocus();
        if (internalSenyawa.getDeteksiSenyawa().internalTBSenyawa instanceof InternalTBSenyawa) {
            InternalTBSenyawa internalTBSenyawa;
            internalTBSenyawa = (InternalTBSenyawa) internalSenyawa.getDeteksiSenyawa().internalTBSenyawa;
            internalTBSenyawa.setClosable(cek);
            internalTBSenyawa.getTabelSenyawa().setEnabled(cek);
            internalTBSenyawa.getBtnCariSenyawa().setEnabled(cek);
            internalTBSenyawa.getBtnDeleteSenyawa().setEnabled(cek);
            internalTBSenyawa.getBtnEditSenyawa().setEnabled(cek);
            internalTBSenyawa.getBtnReloadSenyawa().setEnabled(cek);
            
        }
        if(internalSenyawa.getDeteksiSenyawa().internalRule instanceof InternalRule){
            InternalRule internalRule;
            internalRule = (InternalRule) internalSenyawa.getDeteksiSenyawa().internalRule;
            visibileComponentRule(internalRule, cek);
        }
    }
    private void visibileComponentRule(InternalRule internalRule,Boolean cek){
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
    
    private void clear(InternalSenyawa internalSenyawa) {
        internalSenyawa.getTxtnamaZat().setText("");
        internalSenyawa.getTxtRempiris().setText("");
        internalSenyawa.getTxtRMolekul().setText("");
    }
}
