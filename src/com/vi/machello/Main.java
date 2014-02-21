package com.vi.machello;

import com.jme3.app.SimpleApplication;
import com.vi.machello.app.ApplicationState;
import com.vi.machello.app.EntityDataState;

/**
 * Application entry point.
 *
 * @author Kyle D. Williams
 */
public class Main extends SimpleApplication {

    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }

    public Main() {
        super(
                new ApplicationState(),
                new EntityDataState());
    }

    @Override
    public void simpleInitApp() {
    }
}
