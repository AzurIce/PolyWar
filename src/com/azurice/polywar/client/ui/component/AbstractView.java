package com.azurice.polywar.client.ui.component;

import javax.swing.*;

public abstract class AbstractView extends JPanel {

    public AbstractView() {
        initViews();
        initListeners();
    }

    public abstract void initViews();

    public abstract void initListeners();
}
