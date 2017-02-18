package org.rdlopes.gumshoe.ui;

import com.google.common.collect.Lists;
import com.jidesoft.swing.FontUtils;
import com.jidesoft.swing.JideSwingUtilities;
import com.tulskiy.keymaster.common.HotKey;
import com.tulskiy.keymaster.common.HotKeyListener;
import com.tulskiy.keymaster.common.Provider;
import org.jdesktop.swingx.JXFrame;
import org.rdlopes.gumshoe.GumshoeProperties;
import org.rdlopes.gumshoe.ui.ext.Apple;
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

/**
 * Created by rui on 18/02/2017.
 * Part of gumshoe.
 * <p>
 */
@Component
public class GumshoeFrame extends JXFrame {

    private final InternalFocusListener focusListener;

    private final InternalKeyListener keyListener;

    private final KeyStroke keyStroke;

    private final GumshoeProperties properties;

    private final GumshoeInputField searchInputField;

    @Autowired
    public GumshoeFrame(GumshoeProperties properties, GumshoeInputField searchInputField) {
        super();
        this.properties = properties;
        this.searchInputField = searchInputField;
        this.keyStroke = properties.getKeyStroke();
        this.focusListener = new InternalFocusListener();
        this.keyListener = new InternalKeyListener();

        setUndecorated(true);
        setAutoRequestFocus(true);
        setAlwaysOnTop(true);
        setBackground(properties.getBackground());
        setForeground(properties.getForeground());
        getRootPaneExt().setBorder(BorderFactory.createEmptyBorder(properties.getBorderSize(),
                                                                   properties.getBorderSize(),
                                                                   properties.getBorderSize(),
                                                                   properties.getBorderSize()));
    }

    private void configure(Container container, Font font) {
        Lists.newArrayList(container)
             .forEach(component -> {
                 if (font != null) {
                     component.setFont(FontUtils.getCachedDerivedFont(font,
                                                                      Font.PLAIN,
                                                                      properties.getFontSize()));
                 }
                 component.setBackground(properties.getBackground());
                 component.setForeground(properties.getForeground());
                 if (Container.class.isAssignableFrom(component.getClass())) {
                     Arrays.stream(component.getComponents())
                           .forEach(component1 -> configure((Container) component1, font));
                 }
             });

    }

    @PostConstruct
    public void init() {
        setContentPane(searchInputField);
        searchInputField.addFocusListener(focusListener);
        searchInputField.addKeyListener(keyListener);
        Provider.getCurrentProvider(true)
                .register(keyStroke, keyListener);
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

        // Mac OSX - force to front even if app not active
        Apple.INSTANCE.requestForeground(true);
    }

    private class InternalKeyListener extends KeyAdapter implements HotKeyListener {

        @Override
        public void keyTyped(KeyEvent e) {
            if (e.getKeyChar() == KeyEvent.VK_ESCAPE) {
                setVisible(false);
            }
        }

        @Override
        public void onHotKey(HotKey hotKey) {
            if (hotKey.keyStroke == keyStroke) {
                setVisible(true);
            }
        }
    }

    private class InternalFocusListener implements FocusListener {

        @Override
        public void focusGained(FocusEvent e) {
            // no-op
        }

        @Override
        public void focusLost(FocusEvent e) {
            // when focus lost, close window
            setVisible(false);
        }
    }

}
