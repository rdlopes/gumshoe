package org.rdlopes.gumshoe.ui;

import com.google.common.collect.Lists;
import com.jidesoft.swing.FontUtils;
import com.jidesoft.swing.JideSwingUtilities;
import com.tulskiy.keymaster.common.HotKey;
import com.tulskiy.keymaster.common.HotKeyListener;
import org.jdesktop.swingx.JXFrame;
import org.rdlopes.gumshoe.platform.Platform;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.RoundRectangle2D;
import java.util.Arrays;

import static org.rdlopes.gumshoe.ui.UIGlobals.*;

/**
 * User: ruilopes
 * Date: 30/04/2015
 * Time: 20:43
 */
@Component
public class SearchFrame extends JXFrame {

    private final InternalFocusListener internalFocusListener = new InternalFocusListener();

    private final InternalKeyListener internalKeyListener = new InternalKeyListener();

    @Autowired
    private SearchInputField searchInputField;

    public SearchFrame() {
        super();
        setUndecorated(true);
        setAutoRequestFocus(true);
        setAlwaysOnTop(true);
        setBackground(BACKGROUND);
        setForeground(FOREGROUND);
        getRootPaneExt().setBorder(BorderFactory.createEmptyBorder(BORDER_SIZE, BORDER_SIZE, BORDER_SIZE, BORDER_SIZE));
    }

    @PostConstruct
    public void init() {
        setContentPane(searchInputField);
        searchInputField.addFocusListener(internalFocusListener);
        searchInputField.addKeyListener(internalKeyListener);
        Platform.INSTANCE.registerHotKey(CONTROL_SPACE_KEY_STROKE, internalKeyListener);
        configure(this, searchInputField.getTextArea().getFont());
    }

    public void setVisible(boolean visible) {
        if (visible) {
            pack();
            JideSwingUtilities.globalCenterWindow(this);
            Point searchFrameLocation = getLocation();
            searchFrameLocation.y /= 3;
            setLocation(searchFrameLocation);
        }
        searchInputField.willBecomeVisible(visible);
        super.setVisible(visible);
        searchInputField.didBecomeVisible(isVisible());
        if (isVisible()) {
            toFront();
        }
    }

    public void toFront() {
        super.toFront();

        setShape(new RoundRectangle2D.Double(
                getRootPane().getX(), getRootPane().getY(), getRootPane().getWidth(), getRootPane().getHeight(), 50, 50
        ));

        // force to front even if app not active
        Platform.INSTANCE.toFront();
    }

    private class InternalKeyListener extends KeyAdapter implements HotKeyListener {

        public InternalKeyListener() {
            Platform.INSTANCE.registerHotKey(CONTROL_SPACE_KEY_STROKE, this);
        }

        @Override
        public void keyTyped(KeyEvent e) {
            if (e.getKeyChar() == KeyEvent.VK_ESCAPE) {
                setVisible(false);
            }
        }

        @Override
        public void onHotKey(HotKey hotKey) {
            if (hotKey.keyStroke == CONTROL_SPACE_KEY_STROKE) {
                setVisible(true);
            }
        }
    }

    private class InternalFocusListener implements FocusListener {

        @Override
        public void focusGained(FocusEvent e) {

        }

        @Override
        public void focusLost(FocusEvent e) {
            // when focus lost, close window
            setVisible(false);
        }
    }

    void configure(Container container, Font font) {
        Lists.newArrayList(container).stream()
                .forEach(component -> {
                    if (font != null) component.setFont(FontUtils.getCachedDerivedFont(font, Font.PLAIN, FONT_SIZE));
                    component.setBackground(BACKGROUND);
                    component.setForeground(FOREGROUND);
                    if (Container.class.isAssignableFrom(component.getClass())) {
                        Arrays.asList(component.getComponents()).stream()
                                .forEach(component1 -> configure((Container) component1, font));
                    }
                });

    }

}
