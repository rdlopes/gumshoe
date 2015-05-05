package org.rdlopes.gumshoe.platform;

import com.tulskiy.keymaster.common.HotKeyListener;
import org.rdlopes.gumshoe.platform.impl.MacPlatform;
import org.rdlopes.gumshoe.platform.impl.WindowsPlatform;
import org.rdlopes.gumshoe.platform.impl.X11Platform;
import org.slf4j.Logger;

import javax.swing.*;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * User: ruilopes
 * Date: 30/04/2015
 * Time: 22:15
 */
public enum Platform {
    INSTANCE;

    private static final Logger LOGGER = getLogger(Platform.class);

    private static final PlatformHandler PLATFORM_IMPLEMENTATION;

    static {
        if (com.sun.jna.Platform.isX11()) {
            PLATFORM_IMPLEMENTATION = new X11Platform();
        } else if (com.sun.jna.Platform.isWindows()) {
            PLATFORM_IMPLEMENTATION = new WindowsPlatform();
        } else if (com.sun.jna.Platform.isMac()) {
            PLATFORM_IMPLEMENTATION = new MacPlatform();
        } else {
            LOGGER.warn("No suitable provider for {}", System.getProperty("os.name"));
            PLATFORM_IMPLEMENTATION = null;
        }
    }

    public void toFront() {
        PLATFORM_IMPLEMENTATION.toFront();
    }

    public void registerHotKey(KeyStroke keystroke, HotKeyListener listener) {
        PLATFORM_IMPLEMENTATION.registerHotKey(keystroke, listener);
    }

    public void unregisterHotKey(KeyStroke keystroke, HotKeyListener listener) {
        PLATFORM_IMPLEMENTATION.unregisterHotKey(keystroke, listener);
    }
}
