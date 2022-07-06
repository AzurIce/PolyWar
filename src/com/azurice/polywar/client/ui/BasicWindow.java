package com.azurice.polywar.client.ui;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class BasicWindow extends AbstractWindow {

    @Override
    public void initViews() {

    }

    @Override
    public void initListeners() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                onClose();
//                super.windowClosing(e);
            }
        });
    }

    @Override
    public void onClose() {
        System.exit(0);
    }

}