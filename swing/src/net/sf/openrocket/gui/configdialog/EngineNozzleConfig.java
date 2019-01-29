package net.sf.openrocket.gui.configdialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;

import net.miginfocom.swing.MigLayout;
import net.sf.openrocket.document.OpenRocketDocument;
import net.sf.openrocket.gui.SpinnerEditor;
import net.sf.openrocket.gui.adaptors.BooleanModel;
import net.sf.openrocket.gui.adaptors.DoubleModel;
import net.sf.openrocket.gui.components.BasicSlider;
import net.sf.openrocket.gui.components.DescriptionArea;
import net.sf.openrocket.gui.components.UnitSelector;
import net.sf.openrocket.l10n.Translator;
import net.sf.openrocket.material.Material;
import net.sf.openrocket.rocketcomponent.RocketComponent;
import net.sf.openrocket.rocketcomponent.EngineNozzle;
import net.sf.openrocket.startup.Application;
import net.sf.openrocket.unit.UnitGroup;

public class EngineNozzleConfig extends RocketComponentConfig {

    private static final Translator trans = Application.getTranslator();
    private JComboBox<EngineNozzle.Shape> typeBox;
    private JLabel shapeLabel;
    private JSpinner shapeSpinner;
    private BasicSlider shapeSlider;
    private DescriptionArea description;
    private static final String PREDESC = "<html>";  // ???

    public EngineNozzleConfig(OpenRocketDocument d, RocketComponent c) {
        super(d, c);

        final JPanel panel = new JPanel(new MigLayout("gap rel unrel", "[][65lp::][30lp::]", ""));

        panel.add(new JLabel(trans.get("EngineNozzle.lbl.shape")));

        EngineNozzle.Shape selected = ((EngineNozzle) component).getType();
        EngineNozzle.Shape[] typeList = EngineNozzle.Shape.values();

        typeBox = new JComboBox<EngineNozzle.Shape>(typeList);
        typeBox.setEditable(false);
        typeBox.setSelectedItem(selected);
        typeBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EngineNozzle.Shape s = (EngineNozzle.Shape) typeBox.getSelectedItem();
                ((EngineNozzle) component).setType(s);
                description.setText(PREDESC + s.getEngineNozzleDescription());
                updateEnabled();
            }
        });
        panel.add(typeBox, "span, split 2");

        {
            final JCheckBox checkbox = new JCheckBox(new BooleanModel(component, "Clipped"));
            checkbox.setText(trans.get("EngineNozzle.lbl.Clipped"));
            panel.add(checkbox, "wrap");

            // shape parameter
            this.shapeLabel = new JLabel(trans.get("EngineNozzle.lbl.Shapeparam"));
            panel.add(shapeLabel);
            updateEnabled();
        }

        {
            final DoubleModel shapeModel = new DoubleModel(component, "ShapeParameter");
            this.shapeSpinner = new JSpinner(shapeModel.getSpinnerModel());
            panel.add(shapeSpinner, "growx");
            DoubleModel min = new DoubleModel(component, "ShapeParameterMin");
            DoubleModel max = new DoubleModel(component, "ShapeParameterMax");
            this.shapeSlider = new BasicSlider(shapeModel.getSliderModel(min, max));
            panel.add(shapeSlider, "skip, w 100lp, wrap");
            updateEnabled();
        }

        {
            panel.add(new JLabel(trans.get("EngineNozzle.lbl.length")));
            final DoubleModel lengthModel = new DoubleModel(component, "Length", UnitGroup.UNITS_LENGTH, 0);
            final JSpinner lengthSpinner = new JSpinner(lengthModel.getSpinnerModel());
            lengthSpinner.setEditor(new SpinnerEditor(lengthSpinner));
            panel.add(lengthSpinner, "growx");
            panel.add(new UnitSelector(lengthModel), "growx");
            panel.add(new BasicSlider(lengthModel.getSliderModel(0, 0.05, 0.3)), "w 100lp, wrap");
        }

        {
            panel.add(new JLabel(trans.get("EngineNozzle.lbl.foreDiam")));
            final DoubleModel foreRadiusModel = new DoubleModel(component, "ForeRadius", 2, UnitGroup.UNITS_LENGTH, 0);
            final JSpinner foreRadiusSpinner = new JSpinner(foreRadiusModel.getSpinnerModel());
            foreRadiusSpinner.setEditor(new SpinnerEditor(foreRadiusSpinner));
            panel.add(foreRadiusSpinner, "growx");
            panel.add(new UnitSelector(foreRadiusModel), "growx");
            panel.add(new BasicSlider(foreRadiusModel.getSliderModel(0, 0.04, 02)), "w 100lp, wrap 0px");

            final JCheckBox checkbox = new JCheckBox(foreRadiusModel.getAutomaticAction());
            checkbox.setText(trans.get("EngineNozzle.lbl.automatic"));
            panel.add(checkbox, "skip, span 2, wrap");
        }

        {
            panel.add(new JLabel(trans.get("EngineNozzle.lbl.aftdiam")));
            final DoubleModel aftRadiusModel = new DoubleModel(component, "AftRadius", 2, UnitGroup.UNITS_LENGTH, 0);
            final JSpinner aftRadiusSpinner = new JSpinner(aftRadiusModel.getSpinnerModel());
            aftRadiusSpinner.setEditor(new SpinnerEditor(aftRadiusSpinner));
            panel.add(aftRadiusSpinner, "growx");
            panel.add(new UnitSelector(aftRadiusModel), "growx");
            panel.add(new BasicSlider(aftRadiusModel.getSliderModel(0, 0.04, 0.)), "w 100lp, wrap 0px");

            final JCheckBox checkbox = new JCheckBox(aftRadiusModel.getAutomaticAction());
            checkbox.setText(trans.get("EngineNozzle.lbl.automatic"));
            panel.add(checkbox, "skip, span 2, wrap");
        }

        {
            panel.add(new JLabel(trans.get("EngineNozzle.lbl.wallthick")));
            final DoubleModel thickModel = new DoubleModel(component, "Thickness", UnitGroup.UNITS_LENGTH, 0);
            final JSpinner thickSpinner = new JSpinner(thickModel.getSpinnerModel());
            thickSpinner.setEditor(new SpinnerEditor(thickSpinner));
            panel.add(thickSpinner, "growx");
            panel.add(new UnitSelector(thickModel), "growx");
            panel.add(new BasicSlider(thickModel.getSliderModel(0, 0.01)), "w 100lp, wrap 0px");
        }

        JPanel panel2 = new JPanel(new MigLayout("ins 0"));
        description = new DescriptionArea(5);
        description.setText(PREDESC + ((EngineNozzle) component).getType().getEngineNozzleDescription());
        panel2.add(description, "wmin 250lp, spanx, grpwx, wrap para");

        panel2.add(materialPanel(Material.Type.BULK), "span, wrap");
        panel.add(panel2, "cell 4 0, gapleft paragrap, alighy 0%, spany");

        // tabs
        tabbedPane.insertTab(trans.get("EngineNozzle.tab.General"), null, panel,
                trans.get("EngineNozzle.lbl.Generalproperties"), 0);
        tabbedPane.insertTab(trans.get("TransitionCfg.tab.Shoulder"), null, shoulderTab(),
                trans.get("TransitionCfg.tab.Shoulderproperties"), 1);
        tabbedPane.setSelectedIndex(0);
    }

    private void updateEnabled() {
        boolean e = ((EngineNozzle) component).getType().usesParameter();
        shapeLabel.setEnabled(e);
        shapeSpinner.setEnabled(e);
        shapeSlider.setEnabled(e);
    }


}
