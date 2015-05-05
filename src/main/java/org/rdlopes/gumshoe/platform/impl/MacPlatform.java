package org.rdlopes.gumshoe.platform.impl;

import org.rdlopes.gumshoe.platform.impl.ext.Apple;

/**
 * User: ruilopes
 * Date: 30/04/2015
 * Time: 22:20
 */
public class MacPlatform extends AbstractPlatformHandler {

    @Override
    public void toFront() {
        Apple.INSTANCE.requestForeground(true);
    }

}
