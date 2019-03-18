package net.sf.openrocket.rocketcomponent;

import net.sf.openrocket.l10n.Translator;
import net.sf.openrocket.startup.Application;
import net.sf.openrocket.util.Coordinate;
import net.sf.openrocket.util.MathUtil;

import java.util.Collection;

import static java.lang.Math.sin;
import static net.sf.openrocket.util.MathUtil.pow2;
import static net.sf.openrocket.util.MathUtil.pow3;

public class EngineNozzle extends Transition {
    private static Translator trans = Application.getTranslator();
    private static final double CLIP_PRECISION = 0.0001;

    private Shape type;
    private double shapeParameter;
    private boolean clipped; // don't read directly, isClipped()
    private double foreRadius, aftRadius;
    private boolean autoForeRadius, autoAftRadius;

    private boolean autoAftRadius2;
    private double foreShoulderRadius, aftShoulderRadius;
    private double foreShoulderThickness, aftShoulderThickness;
    private double foreShoulderLength, aftShoulderLength;
    private boolean foreShoulderCapped, aftShoulderCapped;

    private double clipLength = -1;

    public EngineNozzle() {
        super();

        this.foreRadius = DEFAULT_RADIUS;
        this.aftRadius = DEFAULT_RADIUS;
        this.length = DEFAULT_RADIUS * 3;
        this.autoForeRadius = false;
        this.autoAftRadius = true;
        this.type = Shape.PARABOLIC;
        this.shapeParameter = 1;
        this.clipped = true;
    }

    ///////////////////////// Getters & Setters
    ////////  Length  ////////
    @Override
    public void setLength( double length ) {
        if ( this.length == length ) {
            return;
        }
        // Need to clearPreset when length changes.
        clearPreset();
        super.setLength( length );
    }


    ////////  Fore radius  ////////


    @Override
    public double getForeRadius() {
        if (isForeRadiusAutomatic()) {
            // Get the automatic radius from the front
            double r = -1;
            SymmetricComponent c = this.getPreviousSymmetricComponent();
            if (c != null) {
                r = c.getFrontAutoRadius();
            }
            if (r < 0)
                r = DEFAULT_RADIUS;
            return r;
        }
        return foreRadius;
    }

    public void setForeRadius(double radius) {
        if ((this.foreRadius == radius) && (autoForeRadius == false))
            return;

        this.autoForeRadius = false;
        this.foreRadius = Math.max(radius, 0);

        if (this.thickness > this.foreRadius && this.thickness > this.aftRadius)
            this.thickness = Math.max(this.foreRadius, this.aftRadius);

        clearPreset();
        fireComponentChangeEvent(ComponentChangeEvent.BOTH_CHANGE);
    }

    @Override
    public boolean isForeRadiusAutomatic() {
        return autoForeRadius;
    }

    public void setForeRadiusAutomatic(boolean auto) {
        if (autoForeRadius == auto)
            return;

        autoForeRadius = auto;

        clearPreset();
        fireComponentChangeEvent(ComponentChangeEvent.BOTH_CHANGE);
    }


    ////////  Aft radius  /////////

    @Override
    public double getAftRadius() {
        if (isAftRadiusAutomatic()) {
            // Return the auto radius from the rear
            double r = -1;
            SymmetricComponent c = this.getNextSymmetricComponent();
            if (c != null) {
                r = c.getRearAutoRadius();
            }
            if (r < 0)
                r = DEFAULT_RADIUS;
            return r;
        }
        return aftRadius;
    }



    public void setAftRadius(double radius) {
        if ((this.aftRadius == radius) && (autoAftRadius2 == false))
            return;

        this.autoAftRadius2 = false;
        this.aftRadius = Math.max(radius, 0);

        if (this.thickness > this.foreRadius && this.thickness > this.aftRadius)
            this.thickness = Math.max(this.foreRadius, this.aftRadius);

        clearPreset();
        fireComponentChangeEvent(ComponentChangeEvent.BOTH_CHANGE);
    }

    @Override
    public boolean isAftRadiusAutomatic() {
        return autoAftRadius2;
    }

    public void setAftRadiusAutomatic(boolean auto) {
        if (autoAftRadius2 == auto)
            return;

        autoAftRadius2 = auto;

        clearPreset();
        fireComponentChangeEvent(ComponentChangeEvent.BOTH_CHANGE);
    }



    //// Radius automatics

    @Override
    protected double getFrontAutoRadius() {
        if (isAftRadiusAutomatic())
            return -1;
        return getAftRadius();
    }


    @Override
    protected double getRearAutoRadius() {
        if (isForeRadiusAutomatic())
            return -1;
        return getForeRadius();
    }




    ////////  Type & shape  /////////

    public Shape getType() {
        return type;
    }

    public void setType(Shape type) {
        if (type == null) {
            throw new IllegalArgumentException("setType called with null argument");
        }
        if (this.type == type)
            return;
        this.type = type;
        this.clipped = type.isClippable();
        this.shapeParameter = type.defaultParameter();
        fireComponentChangeEvent(ComponentChangeEvent.BOTH_CHANGE);
    }

    public double getShapeParameter() {
        return shapeParameter;
    }

    public void setShapeParameter(double n) {
        if (shapeParameter == n)
            return;
        this.shapeParameter = MathUtil.clamp(n, type.minParameter(), type.maxParameter());
        fireComponentChangeEvent(ComponentChangeEvent.BOTH_CHANGE);
    }

    public void setClipped(boolean c) {
        if (clipped == c)
            return;
        clipped = c;
        fireComponentChangeEvent(ComponentChangeEvent.BOTH_CHANGE);
    }

    public boolean isClippedEnabled() {
        return type.isClippable();
    }

    public double getShapeParameterMin() {
        return type.minParameter();
    }

    public double getShapeParameterMax() {
        return type.maxParameter();
    }


    ////////  Shoulders  ////////

    public double getForeShoulderRadius() {
        return foreShoulderRadius;
    }

    public void setForeShoulderRadius(double foreShoulderRadius) {
        if (MathUtil.equals(this.foreShoulderRadius, foreShoulderRadius))
            return;
        this.foreShoulderRadius = foreShoulderRadius;
        clearPreset();
        fireComponentChangeEvent(ComponentChangeEvent.MASS_CHANGE);
    }

    public double getForeShoulderThickness() {
        return foreShoulderThickness;
    }

    public void setForeShoulderThickness(double foreShoulderThickness) {
        if (MathUtil.equals(this.foreShoulderThickness, foreShoulderThickness))
            return;
        this.foreShoulderThickness = foreShoulderThickness;
        fireComponentChangeEvent(ComponentChangeEvent.MASS_CHANGE);
    }

    public double getForeShoulderLength() {
        return foreShoulderLength;
    }

    public void setForeShoulderLength(double foreShoulderLength) {
        if (MathUtil.equals(this.foreShoulderLength, foreShoulderLength))
            return;
        this.foreShoulderLength = foreShoulderLength;
        fireComponentChangeEvent(ComponentChangeEvent.MASS_CHANGE);
    }

    public boolean isForeShoulderCapped() {
        return foreShoulderCapped;
    }

    public void setForeShoulderCapped(boolean capped) {
        if (this.foreShoulderCapped == capped)
            return;
        this.foreShoulderCapped = capped;
        fireComponentChangeEvent(ComponentChangeEvent.MASS_CHANGE);
    }


    public double getAftShoulderRadius() {
        return aftShoulderRadius;
    }

    public void setAftShoulderRadius(double aftShoulderRadius) {
        if (MathUtil.equals(this.aftShoulderRadius, aftShoulderRadius))
            return;
        this.aftShoulderRadius = aftShoulderRadius;
        clearPreset();
        fireComponentChangeEvent(ComponentChangeEvent.MASS_CHANGE);
    }

    public double getAftShoulderThickness() {
        return aftShoulderThickness;
    }

    public void setAftShoulderThickness(double aftShoulderThickness) {
        if (MathUtil.equals(this.aftShoulderThickness, aftShoulderThickness))
            return;
        this.aftShoulderThickness = aftShoulderThickness;
        fireComponentChangeEvent(ComponentChangeEvent.MASS_CHANGE);
    }

    public double getAftShoulderLength() {
        return aftShoulderLength;
    }

    public void setAftShoulderLength(double aftShoulderLength) {
        if (MathUtil.equals(this.aftShoulderLength, aftShoulderLength))
            return;
        this.aftShoulderLength = aftShoulderLength;
        fireComponentChangeEvent(ComponentChangeEvent.MASS_CHANGE);
    }

    public boolean isAftShoulderCapped() {
        return aftShoulderCapped;
    }

    public void setAftShoulderCapped(boolean capped) {
        if (this.aftShoulderCapped == capped)
            return;
        this.aftShoulderCapped = capped;
        fireComponentChangeEvent(ComponentChangeEvent.MASS_CHANGE);
    }
    ///////////////////////// End Getters & Setters

    /**
     * Numerically solve clipLength from the equation
     *     r1 == type.getRadius(clipLength,r2,clipLength+length)
     * using a binary search.  It assumes getOuterRadius() to be monotonically increasing.
     */
    private void calculateClip(double r1, double r2) {
        double min = 0, max = length;

        if (r1 >= r2) {
            double tmp = r1;
            r1 = r2;
            r2 = tmp;
        }

        if (r1 == 0) {
            clipLength = 0;
            return;
        }

        if (length <= 0) {
            clipLength = 0;
            return;
        }

        // Required:
        //    getR(min,min+length,r2) - r1 < 0
        //    getR(max,max+length,r2) - r1 > 0

        int n = 0;
        while (type.getRadius(max, r2, max + length, shapeParameter) - r1 < 0) {
            min = max;
            max *= 2;
            n++;
            if (n > 10)
                break;
        }

        while (true) {
            clipLength = (min + max) / 2;
            if ((max - min) < CLIP_PRECISION)
                return;
            double val = type.getRadius(clipLength, r2, clipLength + length, shapeParameter);
            if (val - r1 > 0) {
                max = clipLength;
            } else {
                min = clipLength;
            }
        }
    }


    /**
     * Return the radius at point x of the transition.
     */
    @Override
    public double getRadius(double x) {
        if (x < 0 || x > length)
            return 0;

        double r1 = getForeRadius();
        double r2 = getAftRadius();

        if (r1 == r2)
            return r1;

        if (r1 > r2) {
            x = length - x;
            double tmp = r1;
            r1 = r2;
            r2 = tmp;
        }

        if (isClipped()) {
            // Check clip calculation
            if (clipLength < 0)
                calculateClip(r1, r2);
            return type.getRadius(clipLength + x, r2, clipLength + length, shapeParameter);
        } else {
            // Not clipped
            return r1 + type.getRadius(x, r2 - r1, length, shapeParameter);
        }
    }

    public boolean isClipped() {
        if (!type.isClippable())
            return false;
        return clipped;
    }

    @Override
    public double getInnerRadius(double x) {
        return Math.max(getRadius(x) - thickness, 0);
    }

    @Override
    public Collection<Coordinate> getComponentBounds() {
        Collection<Coordinate> bounds = super.getComponentBounds();
        if (foreShoulderLength > 0.001)
            addBound(bounds, -foreShoulderLength, foreShoulderRadius);
        if (aftShoulderLength > 0.001)
            addBound(bounds, getLength() + aftShoulderLength, aftShoulderRadius);
        return bounds;
    }

    @Override
    public double getComponentVolume() {
        double volume =  super.getComponentVolume();
        if (getForeShoulderLength() > 0.001) {
            final double or = getForeShoulderRadius();
            final double ir = Math.max(getForeShoulderRadius() - getForeShoulderThickness(), 0);
            volume += ringVolume( or, ir, getForeShoulderLength() );
        }
        if (isForeShoulderCapped()) {
            final double ir = Math.max(getForeShoulderRadius() - getForeShoulderThickness(), 0);
            volume += ringVolume(ir, 0, getForeShoulderThickness() );
        }

        if (getAftShoulderLength() > 0.001) {
            final double or = getAftShoulderRadius();
            final double ir = Math.max(getAftShoulderRadius() - getAftShoulderThickness(), 0);
            volume += ringVolume(or, ir, getAftShoulderLength() );
        }
        if (isAftShoulderCapped()) {
            final double ir = Math.max(getAftShoulderRadius() - getAftShoulderThickness(), 0);
            volume += ringVolume(ir, 0, getAftShoulderThickness() );
        }

        return volume;
    }

    @Override
    public Coordinate getComponentCG() {
        Coordinate cg = super.getComponentCG();
        if (getForeShoulderLength() > 0.001) {
            final double ir = Math.max(getForeShoulderRadius() - getForeShoulderThickness(), 0);
            cg = cg.average(ringCG(getForeShoulderRadius(), ir, -getForeShoulderLength(), 0,
                    getMaterial().getDensity()));
        }
        if (isForeShoulderCapped()) {
            final double ir = Math.max(getForeShoulderRadius() - getForeShoulderThickness(), 0);
            cg = cg.average(ringCG(ir, 0, -getForeShoulderLength(),
                    getForeShoulderThickness() - getForeShoulderLength(),
                    getMaterial().getDensity()));
        }

        if (getAftShoulderLength() > 0.001) {
            final double ir = Math.max(getAftShoulderRadius() - getAftShoulderThickness(), 0);
            cg = cg.average(ringCG(getAftShoulderRadius(), ir, getLength(),
                    getLength() + getAftShoulderLength(), getMaterial().getDensity()));
        }
        if (isAftShoulderCapped()) {
            final double ir = Math.max(getAftShoulderRadius() - getAftShoulderThickness(), 0);
            cg = cg.average(ringCG(ir, 0,
                    getLength() + getAftShoulderLength() - getAftShoulderThickness(),
                    getLength() + getAftShoulderLength(), getMaterial().getDensity()));
        }
        return cg;
    }


    /*
     * The moments of inertia are not explicitly corrected for the shoulders.
     * However, since the mass is corrected, the inertia is automatically corrected
     * to very nearly the correct value.
     */



    /**
     * Returns the name of the component ("Transition").
     */
    @Override
    public String getComponentName() {
        //// Transition
        return trans.get("Nozzle.Nozzle");
    }

    @Override
    protected void componentChanged(ComponentChangeEvent e) {
        super.componentChanged(e);
        clipLength = -1;
    }

    /**
     * Check whether the given type can be added to this component.  Transitions allow any
     * InternalComponents to be added.
     *
     * @param comptype  The RocketComponent class type to add.
     * @return      Whether such a component can be added.
     */
    @Override
    public boolean isCompatible(Class<? extends RocketComponent> comptype) {
        if (InternalComponent.class.isAssignableFrom(comptype)){
            return true;
        }else if ( FreeformFinSet.class.isAssignableFrom(comptype)){
            return true;
        }
        return false;
    }

}
