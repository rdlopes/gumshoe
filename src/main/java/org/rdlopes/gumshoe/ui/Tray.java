package org.rdlopes.gumshoe.ui;

import org.rdlopes.gumshoe.resources.Icon;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * User: ruilopes
 * Date: 18/04/15
 * Time: 14:07
 */
@Component
public class Tray extends TrayIcon {

    private static final Logger LOGGER = getLogger(Tray.class);

    @Autowired
    private SearchFrame searchFrame;

    public Tray() {
        super(Icon.MAIN.scale(16, 16).build().getImage(), "Gumshoe");
        setImageAutoSize(true);
    }

    @PostConstruct
    public void register() throws AWTException {
        LOGGER.debug("register");

        // clicking opens main frame
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                showSearchFrame();
            }
        });

        // add to system tray
        if (SystemTray.isSupported()) SystemTray.getSystemTray().add(this);
    }

    private void showSearchFrame() {
        searchFrame.setVisible(true);
    }

    @PreDestroy
    public void unregister() {
        LOGGER.debug("unregister");
        if (SystemTray.isSupported()) SystemTray.getSystemTray().remove(this);
    }

}
