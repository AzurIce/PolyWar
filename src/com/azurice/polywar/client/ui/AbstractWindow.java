package com.azurice.polywar.client.ui;

import javax.swing.*;

public abstract class AbstractWindow extends JFrame {
    public abstract void initViews();

    public abstract void initListeners();

    public abstract void onClose();

    public void display() {
        initViews();
        initListeners();
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

}
