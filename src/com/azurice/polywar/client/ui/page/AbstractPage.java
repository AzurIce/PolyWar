package com.azurice.polywar.client.ui.page;

import com.azurice.polywar.Renderable;
import com.azurice.polywar.Tickable;
import com.azurice.polywar.client.ui.MainWindow;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * A JPanel with render and logic cycle
 */
public abstract class AbstractPage extends JPanel implements Tickable, Renderable {
    protected MainWindow parent;

    public AbstractPage(MainWindow parent) {
        this.parent = parent;
        initViews();
        initListeners();
    }

    public void initViews() {
        setFocusable(true);
    }

    public void initListeners() {
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                AbstractPage.this.keyPressed(e);
            }

            @Override
            public void keyReleased(KeyEvent e) {
                AbstractPage.this.keyReleased(e);
            }
        });
    }

    public void onShow() {

    }

    ////// Key Listener //////

    public abstract void keyPressed(KeyEvent e);

    public abstract void keyReleased(KeyEvent e);
}
