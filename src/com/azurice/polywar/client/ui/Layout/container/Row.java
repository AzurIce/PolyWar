package com.azurice.polywar.client.ui.Layout.container;

import javax.swing.*;
import java.awt.*;

public class Row extends JPanel {
    public Row() {
        this(Alignment.CENTER);
    }

    public Row(Alignment alignment) {
        super();
        setLayout(
                new FlowLayout(switch (alignment) {
                    case START -> FlowLayout.LEFT;
                    case CENTER -> FlowLayout.CENTER;
                    case END -> FlowLayout.RIGHT;
                })
        );
    }

    public void add(Component... components) {
        for (Component component : components) {
            add(component);
        }
    }

    public enum Alignment {
        START, CENTER, END
    }
}
