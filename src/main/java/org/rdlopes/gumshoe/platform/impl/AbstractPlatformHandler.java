package org.rdlopes.gumshoe.platform.impl;

import com.google.common.collect.Maps;
import com.tulskiy.keymaster.common.HotKeyListener;
import com.tulskiy.keymaster.common.Provider;
import org.rdlopes.gumshoe.platform.PlatformHandler;
import org.slf4j.Logger;

import javax.swing.*;
import java.util.Map;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * User: ruilopes
 * Date: 03/05/2015
 * Time: 22:39
 */
public abstract class AbstractPlatformHandler implements PlatformHandler {

    private static final Logger LOGGER = getLogger(AbstractPlatformHandler.class);

    private static final Provider PROVIDER = Provider.getCurrentProvider(true);

    private final Map<KeyStroke, HotKeyListener> listenerMap = Maps.newHashMap();

    public void registerHotKey(KeyStroke keystroke, HotKeyListener listener) {
        if (!listenerMap.containsKey(keystroke)) {
            listenerMap.put(keystroke, listener);
            PROVIDER.register(keystroke, listener);
        }
    }

    public void unregisterHotKey(KeyStroke keystroke, HotKeyListener listener) {
        if (listenerMap.containsKey(keystroke)) {
            PROVIDER.reset();
            listenerMap.entrySet().parallelStream()
                    .forEach(entry -> registerHotKey(entry.getKey(), entry.getValue()));
        }
    }

}
