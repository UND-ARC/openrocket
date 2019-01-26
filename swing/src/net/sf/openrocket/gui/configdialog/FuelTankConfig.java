package net.sf.openrocket.gui.configdialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import net.miginfocom.swing.MigLayout;

import net.sf.openrocket.gui.adaptors.DoubleModel;
import net.sf.openrocket.gui.adaptors.EnumModel;

import net.sf.openrocket.unit.UnitGroup;
import net.sf.openrocket.l10n.Translator;
import net.sf.openrocket.startup.Application;
import net.sf.openrocket.rocketcomponent.FuelTank;
import net.sf.openrocket.document.OpenRocketDocument;
import net.sf.openrocket.rocketcomponent.RocketComponent;

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

        // Tank mass
        panel.add(new JLabel(trans.get("FuelTank.lbl.tankmass")));
        DoubleModel m  = new DoubleModel(component, "ComponentMass", UnitGroup.UNITS_MASS, 0);
        JSpinner spin = new JSpinner(m.getSpinnerModel());
        spin.setEditor(new SpinnerEditor(spin));
        panel.add(spin, "growx");
        panel.add(new UnitSelector(m), "growx");
        panel.add(new BasicSlider(m.getSliderModel(0, 0.05, 0.5)), "w 100lp, wrap");

        // Fuel quantity (mass)
        panel.add(new JLabel(trans.get("FuelTank.lbl.fuelqty")));
        DoubleModel fq = new DoubleModel(component, "FuelQty", UnitGroup.UNITS_MASS, 0);
        JSpinner spin = new JSpinner(fq.getSpinnerModel());
        spin.setEditor(new SpinnerEditor(spin));
        panel.add(spin, "growx");
        panel.add(new UnitSelector(fq), "growx");
        panel.add(new BasicSilder(fq.getSliderModel(0, 0.05, 0.5)), "q 100lp, wrap");

    }

}
