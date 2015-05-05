/*
 * Copyright (c) 2006-2015 DMDirc Developers
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.rdlopes.gumshoe.platform.impl.ext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.EventObject;

/**
 * Integrate DMDirc with OS X better.
 */
public enum Apple implements InvocationHandler {
    INSTANCE;

    private static final Logger LOG = LoggerFactory.getLogger(Apple.class);

    /**
     * The "Application" object used to do stuff on OS X.
     */
    private Object application;

    /**
     * Whether we're listening or not.
     */
    private boolean isListener;

    private Apple() {

    }

    /**
     * Are we using the OS X look and feel?
     *
     * @return true if we are using the OS X look and feel
     */
    public static boolean isAppleUI() {
        final String name = UIManager.getLookAndFeel().getClass().getName();
        return isApple() && ("apple.laf.AquaLookAndFeel".equals(name)
                || "com.apple.laf.AquaLookAndFeel".equals(name));
    }

    /**
     * Are we on OS X?
     *
     * @return true if we are running on OS X
     */
    public static boolean isApple() {
        return System.getProperty("os.name").contains("OS X");
    }

    /**
     * Set some OS X only UI settings.
     */
    public void setUISettings() {
        if (!isApple()) {
            return;
        }

        System.setProperty("apple.awt.showGrowBox", "true");
        System.setProperty("com.apple.mrj.application.growbox.intrudes", "false");
        System.setProperty("com.apple.mrj.application.live-resize", "true");
    }

    /**
     * Requests this application to move to the foreground.
     *
     * @param allWindows if all windows of this application should be moved to the foreground, or
     *                   only the foremost one
     */
    public void requestForeground(final boolean allWindows) {
        doAppleMethod("requestForeground", new Class<?>[]{Boolean.TYPE}, new Object[]{allWindows});
    }

    /**
     * Handle a method call to the apple Application class.
     *
     * @param methodName Method to call
     * @param classes    Array of classes to pass when calling getMethod
     * @param objects    Array of objects to pass when invoking.
     * @return Output from method.invoke()
     */
    private Object doAppleMethod(final String methodName, final Class<?>[] classes,
                                 final Object[] objects) {
        if (!isApple()) {
            return null;
        }

        return reflectMethod(getApplication(), null, methodName, classes, objects);
    }

    /**
     * Call a method on the given object.
     *
     * @param obj        Object to call method on.
     * @param className  Name of class that object really is.
     * @param methodName Method to call
     * @param classes    Array of classes to pass when calling getMethod
     * @param objects    Array of objects to pass when invoking.
     * @return Output from method.invoke()
     */
    private Object reflectMethod(final Object obj, final String className, final String methodName,
                                 final Class<?>[] classes, final Object[] objects) {
        try {
            final Class<?> clazz = className == null ? obj.getClass() : Class.forName(className);
            final Method method = clazz.getMethod(methodName, classes == null ? new Class<?>[0]
                    : classes);
            return method.invoke(obj, objects == null ? new Object[0] : objects);
        } catch (ReflectiveOperationException ex) {
            LOG.error("Unable to find OS X classes.", ex);
        }

        return null;
    }

    /**
     * Get the "Application" object.
     *
     * @return Object that on OSX will be an "Application"
     */
    public Object getApplication() {
        synchronized (Apple.class) {
            if (isApple() && application == null) {
                application = reflectMethod(null, "com.apple.eawt.Application", "getApplication",
                        null, null);
            }
            return application;
        }
    }

    /**
     * Requests user attention to this application (usually through bouncing the Dock icon).
     * Critical requests will continue to bounce the Dock icon until the app is activated. An
     * already active application requesting attention does nothing.
     *
     * @param isCritical If this is false, the dock icon only bounces once, otherwise it will bounce
     *                   until clicked on.
     */
    public void requestUserAttention(final boolean isCritical) {
        doAppleMethod("requestUserAttention", new Class<?>[]{Boolean.TYPE}, new Object[]{isCritical});
    }

    /**
     * Get the PopupMenu attached to the application's Dock icon.
     *
     * @return the PopupMenu attached to this application's Dock icon
     */
    public PopupMenu getDockMenu() {
        final Object result = doAppleMethod("getDockMenu", null, null);
        return result instanceof PopupMenu ? (PopupMenu) result : null;
    }

    /**
     * Attaches the contents of the provided PopupMenu to the application's Dock icon.
     *
     * @param menu the PopupMenu to attach to this application's Dock icon
     */
    public void setDockMenu(final PopupMenu menu) {
        doAppleMethod("setDockMenu", new Class<?>[]{PopupMenu.class}, new Object[]{menu});
    }

    /**
     * Obtains an image of this application's Dock icon.
     *
     * @return The application's dock icon.
     */
    public Image getDockIconImage() {
        final Object result = doAppleMethod("getDockIconImage", null, null);
        return result instanceof Image ? (Image) result : null;
    }

    /**
     * Changes this application's Dock icon to the provided image.
     *
     * @param image The image to use
     */
    public void setDockIconImage(final Image image) {
        doAppleMethod("setDockIconImage", new Class<?>[]{Image.class}, new Object[]{image});
    }

    /**
     * Affixes a small system provided badge to this application's Dock icon. Usually a number.
     *
     * @param badge textual label to affix to the Dock icon
     */
    public void setDockIconBadge(final String badge) {
        doAppleMethod("setDockIconBadge", new Class<?>[]{String.class}, new Object[]{badge});
    }

    /**
     * Sets the default menu bar to use when there are no active frames. Only used when the system
     * property "apple.laf.useScreenMenuBar" is "true", and the Aqua Look and Feel is active.
     *
     * @param menuBar to use when no other frames are active
     */
    public void setDefaultMenuBar(final JMenuBar menuBar) {
        doAppleMethod("setDefaultMenuBar", new Class<?>[]{JMenuBar.class}, new Object[]{menuBar});
    }

    /**
     * Set this up as a listener for the Apple Events.
     *
     * @return True if the listener was added, else false.
     */
    public boolean setListener() {
        if (!isApple() || isListener) {
            return false;
        }

        addHandler("com.apple.eawt.OpenURIHandler", "setOpenURIHandler");
        addHandler("com.apple.eawt.AboutHandler", "setAboutHandler");
        addHandler("com.apple.eawt.QuitHandler", "setQuitHandler");
        addHandler("com.apple.eawt.PreferencesHandler", "setPreferencesHandler");
        isListener = true;

        return true;
    }

    /**
     * Add this application as a handler for the given event.
     *
     * @param handlerClass  Class used as the handler.
     * @param handlerMethod Method used to set the handler.
     * @return True if we succeeded.
     */
    private boolean addHandler(final String handlerClass, final String handlerMethod) {
        try {
            final Class<?> listenerClass = Class.forName(handlerClass);
            final Object listener = Proxy.newProxyInstance(getClass().getClassLoader(),
                    new Class<?>[]{listenerClass}, this);

            final Method method = getApplication().getClass().getMethod(handlerMethod,
                    listenerClass);
            method.invoke(getApplication(), listener);

            return true;
        } catch (ClassNotFoundException | NoSuchMethodException |
                IllegalAccessException | InvocationTargetException ex) {
            return false;
        }
    }

    @Override
    public Object invoke(final Object proxy, final Method method, final Object[] args)
            throws ReflectiveOperationException {
        if (!isApple()) {
            return null;
        }

        try {
            final Class<?>[] classes = new Class<?>[args.length];

            for (int i = 0; i < args.length; i++) {
                if (EventObject.class.isInstance(args[i])) {
                    classes[i] = EventObject.class;
                } else {
                    final Class<?> c = args[i].getClass();
                    if ("com.apple.eawt.QuitResponse".equals(c.getCanonicalName())) {
                        classes[i] = Object.class;
                    } else {
                        classes[i] = c;
                    }
                }
            }

            final Method thisMethod = getClass().getMethod(method.getName(), classes);
            return thisMethod.invoke(this, args);
        } catch (final NoSuchMethodException e) {
            if ("equals".equals(method.getName()) && args.length == 1) {
                return proxy == args[0];
            }
        }

        return null;
    }

}