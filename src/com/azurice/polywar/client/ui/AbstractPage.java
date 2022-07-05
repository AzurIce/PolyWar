package com.azurice.polywar.client.ui;

import javax.swing.*;

public abstract class AbstractPage extends JFrame {
    public void display() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
