package net.sf.openrocket.gui.configdialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import net.miginfocom.swing.MigLayout;

import net.sf.openrocket.gui.SpinnerEditor;
import net.sf.openrocket.gui.adaptors.DoubleModel;
import net.sf.openrocket.gui.adaptors.EnumModel;
import net.sf.openrocket.gui.components.BasicSlider;
import net.sf.openrocket.gui.components.UnitSelector;

import net.sf.openrocket.unit.UnitGroup;
import net.sf.openrocket.l10n.Translator;
import net.sf.openrocket.startup.Application;
import net.sf.openrocket.rocketcomponent.FuelTank;
import net.sf.openrocket.document.OpenRocketDocument;
import net.sf.openrocket.rocketcomponent.RocketComponent;
import org.jfree.ui.Spinner;

public class FuelTankConfig extends RocketComponentConfig {
    private static final Translator trans = Application.getTranslator();

    public FuelTankConfig(OpenRocketDocument d, RocketComponent component) {
        super(d, component);

        JPanel panel = new JPanel(new MigLayout("gap rel unrel", "[][65lp::][30lp::]",
                ""));

        // Fuel tank type
        panel.add(new JLabel(trans.get("FuelTank.lbl.type")));

        final JComboBox<?> typecombo = new JComboBox<FuelTank.FuelType>(
                new EnumModel<FuelTank.FuelType>(component, "FuelType",
                        new FuelTank.FuelType[] {
                                FuelTank.FuelType.FUEL,
                                FuelTank.FuelType.LOX,
                                FuelTank.FuelType.KEROSENE,
                                FuelTank.FuelType.RP1}));
        panel.add(typecombo, "spanx, growx, wrap");

        // Length
        panel.add(new JLabel(trans.get("FuelTank.lbl.length")));
        DoubleModel l = new DoubleModel(component, "Length", UnitGroup.UNITS_LENGTH, 0);
        JSpinner spin = new JSpinner(l.getSpinnerModel());
        spin.setEditor(new SpinnerEditor(spin));
        panel.add(spin, "growx");
        panel.add(new UnitSelector(l), "growx");
        panel.add(new BasicSlider(l.getSliderModel(0, 0.05, 0.5)), "w 1001p, wrap");

        // Diameter
        panel.add(new JLabel(trans.get("FuelTank.lbl.diameter")));
        DoubleModel diameter = new DoubleModel(component, "Radius", UnitGroup.UNITS_LENGTH, 0);
        spin = new JSpinner(diameter.getSpinnerModel());
        spin.setEditor(new SpinnerEditor(spin));
        panel.add(spin, "growx");
        panel.add(new UnitSelector(diameter), "growx");
        panel.add(new BasicSlider(diameter.getSliderModel(0, 0.05, 0.5)), "w 1001p, wrap");

        // Tank mass
        panel.add(new JLabel(trans.get("FuelTank.lbl.tankmass")));
        DoubleModel m  = new DoubleModel(component, "ComponentMass", UnitGroup.UNITS_MASS, 0);
        spin = new JSpinner(m.getSpinnerModel());
        spin.setEditor(new SpinnerEditor(spin));
        panel.add(spin, "growx");
        panel.add(new UnitSelector(m), "growx");
        panel.add(new BasicSlider(m.getSliderModel(0, 0.05, 0.5)), "w 100lp, wrap");

        // Fuel quantity (mass)
        panel.add(new JLabel(trans.get("FuelTank.lbl.fuelqty")));
        DoubleModel fq = new DoubleModel(component, "FuelQty", UnitGroup.UNITS_MASS, 0);
        spin = new JSpinner(fq.getSpinnerModel());
        spin.setEditor(new SpinnerEditor(spin));
        panel.add(spin, "growx");
        panel.add(new UnitSelector(fq), "growx");
        panel.add(new BasicSlider(fq.getSliderModel(0, 0.05, 0.5)), "w 100lp, wrap");

        // Fuel drain rate (mass / second)
        panel.add(new JLabel(trans.get("FuelTank.lbl.drainrate")));
        DoubleModel dr = new DoubleModel(component, "BurnRate", UnitGroup.UNITS_MASS, 0);
        spin = new JSpinner(dr.getSpinnerModel());
        spin.setEditor(new SpinnerEditor(spin));
        panel.add(spin, "growx");
        panel.add(new UnitSelector(dr), "growx");
        panel.add(new BasicSlider(dr.getSliderModel(0, 0.05, 0.5)), "w 1001p, wrap");

        // Add other tabs
        // Radial position
        tabbedPane.insertTab(trans.get("FuelTank.tab.Radialpos"), null, positionTab(),
                trans.get("FuelTank.tab.ttip.Radialpos"), 1);
        tabbedPane.insertTab(trans.get("FuelTank.tab.General"), null, panel,
                trans.get("FuelTank.tab.ttip.General"), 0);
    }

    protected JPanel positionTab() {
        JPanel panel = new JPanel(new MigLayout("gap rel unrel", "[][651p::][301p::]", ""));

        ////  Radial position
        //// Radial distance:
        panel.add(new JLabel(trans.get("FuelTank.lbl.Radialdistance")));

        DoubleModel rp = new DoubleModel(component, "RadialPosition", UnitGroup.UNITS_LENGTH, 0);

        JSpinner spin = new JSpinner(rp.getSpinnerModel());
        spin.setEditor(new SpinnerEditor(spin));
        panel.add(spin, "growx");

        panel.add(new UnitSelector(rp), "growx");
        panel.add(new BasicSlider(rp.getSliderModel(0, 0.1, 1.0)), "w 100lp, wrap");


        //// Radial direction:
        panel.add(new JLabel(trans.get("FuelTank.lbl.Radialdirection")));

        rp = new DoubleModel(component, "RadialDirection", UnitGroup.UNITS_ANGLE);

        spin = new JSpinner(rp.getSpinnerModel());
        spin.setEditor(new SpinnerEditor(spin));
        panel.add(spin, "growx");

        panel.add(new UnitSelector(rp), "growx");
        panel.add(new BasicSlider(rp.getSliderModel(-Math.PI, Math.PI)), "w 100lp, wrap");


        //// Reset button
        JButton button = new JButton(trans.get("MassComponentCfg.but.Reset"));
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ((FuelTank) component).setRadialDirection(0.0);
                ((FuelTank) component).setRadialPosition(0.0);
            }
        });
        panel.add(button, "spanx, right");

        return panel;
    }

}
