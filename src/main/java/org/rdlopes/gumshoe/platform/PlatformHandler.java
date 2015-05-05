package org.rdlopes.gumshoe.platform;

import com.tulskiy.keymaster.common.HotKeyListener;

import javax.swing.*;

/**
 * User: ruilopes
 * Date: 30/04/2015
 * Time: 22:17
 */
public interface PlatformHandler {

    void toFront();

    void registerHotKey(KeyStroke keystroke, HotKeyListener listener);

    void unregisterHotKey(KeyStroke keystroke, HotKeyListener listener);

}
