package net.sf.openrocket.file.openrocket.savers;

import java.util.List;
import net.sf.openrocket.rocketcomponent.FuelTank;

public class FuelTankSaver extends InternalComponentSaver {

    @Override
    protected void addParams(net.sf.openrocket.rocketcomponent.RocketComponent c,
                             List<String> elements) {
        super.addParams(c, elements);

        FuelTank fuel = (FuelTank) c;

        elements.add("<packedmass>" + fuel.getMass() + "</packedmass>");
        elements.add("<packedfuelqty>" + fuel.getFuelQty() + "</packedfuelqty>");
        //elements.add("<packedinitqty>" + fuel.getInitialFuelQty() + "</packedinitqty>");
        elements.add("<packedburnrate>" + fuel.getBurnRate() + "</packedburnrate>");

    }

}

