package org.makarov.automate.serialize;

import org.makarov.automate.Automate;

public interface AutomateSerializer {

    <T> String serialize(Automate<T> automate);
}
