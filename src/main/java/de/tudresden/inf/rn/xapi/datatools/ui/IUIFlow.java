package de.tudresden.inf.rn.xapi.datatools.ui;

import java.util.List;

public interface IUIFlow extends IUIIconable {
    String getName();
    String getEntrypoint();
    List<IUIStep> getSteps();
}
