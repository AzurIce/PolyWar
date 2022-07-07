package com.azurice.polywar.client.ui.Layout;

import javax.swing.*;
import java.awt.*;

public class Row extends JPanel {
    public Row() {
        super();
        setLayout(new FlowLayout());
    }

    public void add(Component... components) {
        for (Component component : components) {
            add(component);
        }
    }
}
