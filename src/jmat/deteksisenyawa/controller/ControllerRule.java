/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jmat.deteksisenyawa.controller;

import java.util.List;
import java.util.concurrent.ExecutionException;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import jmat.deteksisenyawa.entity.EntityCiri;
import jmat.deteksisenyawa.entity.EntitySenyawa;
import jmat.deteksisenyawa.internalframe.rule.InternalRule;
import jmat.deteksisenyawa.model.ModelRule;
import org.apache.log4j.Logger;

/**
 *
 * @author hasani
 */
public class ControllerRule {

    private ModelRule modelRule;
    private static final Logger LOGGER = Logger.getLogger(ControllerRule.class);

    public void setModelRule(ModelRule modelRule) {
        this.modelRule = modelRule;
    }

    public void insertRule(InternalRule internalRule) {
        Object objekMenatahComboSenyawa = internalRule.getComboSenyawa().getSelectedItem();
        List<EntityCiri> objekMentahListPenjelasan = internalRule.getListPenjelasan().getSelectedValuesList();

        if (objekMenatahComboSenyawa instanceof EntitySenyawa) {
            EntitySenyawa entitySenyawa;
            entitySenyawa = (EntitySenyawa) objekMenatahComboSenyawa;
            if (objekMentahListPenjelasan instanceof List) {
                modelRule.setEntitySenyawa(entitySenyawa);
                modelRule.setEntityCiris(objekMentahListPenjelasan);
                
                runProgressBar(internalRule);
            } else {
                System.err.println("ERROR");
            }
        } else {
            JOptionPane.showMessageDialog(internalRule, "Tidak ada senyawa yang terdeteksi.", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void deleteRule(InternalRule internalRule) {
        Object objekMenatahComboSenyawa = internalRule.getComboSenyawa().getSelectedItem();
        if (objekMenatahComboSenyawa instanceof EntitySenyawa) {
            EntitySenyawa entitySenyawa;
            entitySenyawa = (EntitySenyawa) objekMenatahComboSenyawa;
            modelRule.setEntitySenyawa(entitySenyawa);
            runProgressBar(internalRule);
        } else {
            JOptionPane.showMessageDialog(internalRule, "Tidak ada senyawa yang terdeteksi.", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void runProgressBar(final InternalRule internalRule) {
        new SwingWorker<Integer, Object>() {
            @Override
            protected Integer doInBackground() throws Exception {
                Integer i = 0;
                for (int run = 0; run <= 100; run++) {
                    Thread.sleep(50);
                    internalRule.getProgresbarRule().setValue(run);

                    if (run == 100) {
                        internalRule.getProgresbarRule().setValue(0);
                        internalRule.getProgresbarRule().setVisible(false);
                        internalRule.getLblKeterangan().setText("Sukses menyimpan data");
                        internalRule.getLblKeterangan().setVisible(true);
                        visibleComponent(internalRule, true);
                        Thread.sleep(550);
                        internalRule.getProgresbarRule().setValue(0);
                        internalRule.getProgresbarRule().setVisible(true);
                        internalRule.getLblKeterangan().setVisible(false);
                        i = run;
                    } else {

                        visibleComponent(internalRule, false);
                    }
                }
                return i;
            }

            @Override
            protected void done() {
                try {
                    if (get().intValue() == 100) {
                        if (internalRule.getListModelCiri().size() > 0) {
                            modelRule.delete();
                            modelRule.insert();
                            internalRule.getListPenjelasan().clearSelection();
                        } else {
                            modelRule.delete();
                        }
                    }
                } catch (InterruptedException | ExecutionException ex) {
                    LOGGER.error(ex.getMessage(), ex);
                }
            }
        }.execute();
    }

    private void visibleComponent(InternalRule internalRule, Boolean cek) {
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
        internalRule.getDeteksiSenyawa().getjMenuBar1().setVisible(cek);
        internalRule.requestFocus();
    }
}
