package net.sf.openrocket.file.openrocket.savers;

import java.util.ArrayList;
import java.util.List;

public class EngineNozzleSaver extends TransitionSaver {

    private static final EngineNozzleSaver instance = new EngineNozzleSaver();

     public ArrayList<String> getElements(net.sf.openrocket.rocketcomponent.EngineNozzle c) {
         ArrayList<String> list = new ArrayList<String>();

         list.add("<enginenozzle>");
         instance.addParams(c, list);
         list.add("</enginenozzle>");

         return list;
     }

     @Override
    protected void addParams(net.sf.openrocket.rocketcomponent.RocketComponent c, List<String> elements) {
         super.addParams(c, elements);

     }

}
