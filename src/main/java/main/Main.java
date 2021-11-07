package main;

import framework.application.Application;
import framework.state.ApplicationState;
import state.LaboratoryState;

public class Main {

    private static final String STRING_PROPERTIES_PATH = "/laboratory.properties";

    public static void main(String[] args) {
        ApplicationState state = new LaboratoryState();
        Application application = new Application.ApplicationBuilder(STRING_PROPERTIES_PATH, state)
                .build();
        application.start();
    }

}
