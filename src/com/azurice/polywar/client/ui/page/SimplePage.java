package com.azurice.polywar.client.ui.page;

import com.azurice.polywar.client.ui.MainWindow;

import javax.swing.*;

public class SimplePage extends AbstractPage {

    public SimplePage(MainWindow parent) {
        super(parent);
    }

    @Override
    public void initViews() {
        add(new JLabel("??????"));
    }

    @Override
    public void initListeners() {

    }

    @Override
    void render() {

    }

    @Override
    void tick() {

    }
}
