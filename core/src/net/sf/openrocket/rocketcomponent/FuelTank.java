package net.sf.openrocket.rocketcomponent;

import net.sf.openrocket.startup.Application;
import net.sf.openrocket.l10n.Translator;

/**
 * This class represents a fuel tank.  It's fuel burn rate is set in configuration
 * and decreases mass with time.
 *
 * @author Misha Turnbull <mishaturnbull@gmail.com>
 */

public class FuelTank extends MassObject {
    private static final Translator trans = Application.getTranslator();

    private double mass = 0;
    private double fuelQty = 0;
    private double initialFuelQty;
    private double burnRate = 0;
    private double fuelDensity = 0;

    public static enum FuelType {
        FUEL(Application.getTranslator().get("FuelType.Fuel")),
        LOX(Application.getTranslator().get("FuelType.LOX")),
        KEROSENE(Application.getTranslator().get("FuelType.Kerosene")),
        RP1(Application.getTranslator().get("FuelType.RP1"));

        private String title;
        FuelType(String title) {
            this.title = title;
        }
        @Override
        public String toString() {
            return title;
        }
    }

    private FuelType fuelType = FuelType.FUEL;

    public FuelTank() {
        super();
    }

    public FuelTank(double length, double radius, double mass,
                    double fuelQty, double burnRate, double fuelDensity) {
        super(length, radius);
        this.mass = mass;
        this.initialFuelQty = fuelQty;
        this.fuelQty = fuelQty;
        this.burnRate = burnRate;
        this.fuelDensity = fuelDensity;
    }

    @Override
    public double getComponentMass() {
        return mass + fuelQty;
    }

    public void setComponentMass(double mass) {
        mass = Math.max(mass, 0);
        if (this.mass == mass) {
            return;
        }
        this.mass = mass;
        fireComponentChangeEvent(ComponentChangeEvent.MASS_CHANGE);
    }

    private double estimateMassLeftAtTime(double time_burning) {
        double burned = this.burnRate * time_burning;
        double newFuelQty = this.initialFuelQty - burned;
        if (newFuelQty != this.fuelQty) {
            this.fuelQty = newFuelQty;
            fireComponentChangeEvent(ComponentChangeEvent.MASS_CHANGE);

        }
        return this.fuelQty;
    }

    public final FuelTank.FuelType getFuelType() {
        mutex.verify();
        return this.fuelType;
    }

    public void setFuelType(FuelTank.FuelType fuelType) {
        mutex.verify();
        if (this.fuelType == fuelType) {
            return;
        }
        checkState();  // misha: what does this do?
        this.fuelType = fuelType;
        fireComponentChangeEvent(ComponentChangeEvent.NONFUNCTIONAL_CHANGE);
    }

    public double getFuelQty() {
        mutex.verify();
        return this.fuelQty;
    }

    public void setFuelQty(double fuelQty) {
        mutex.verify();
        if (this.fuelQty == fuelQty) {
            return;
        }
        checkState();
        this.fuelQty = fuelQty;
        this.estimateMassLeftAtTime(0);
    }

    public double getBurnRate() {
        mutex.verify();
        return this.burnRate;
    }

    public void setBurnRate(double burnRate) {
        mutex.verify();
        if (this.burnRate == burnRate) {
            return;
        }
        checkState();
        this.burnRate = burnRate;
        fireComponentChangeEvent(ComponentChangeEvent.NONFUNCTIONAL_CHANGE);
    }

    @Override
    public String getComponentName() {
        return trans.get("FuelTank.FuelTank");
    }

    @Override
    public boolean allowsChildren() {
        return false;
    }

    @Override
    public boolean isCompatible(Class <? extends RocketComponent> type) {
        return false;
    }

}
