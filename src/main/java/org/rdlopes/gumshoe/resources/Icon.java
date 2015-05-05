package org.rdlopes.gumshoe.resources;

import com.google.common.collect.Lists;
import com.jidesoft.icons.IconsFactory;
import org.slf4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.function.Function;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * User: ruilopes
 * Date: 02/05/2015
 * Time: 14:19
 */
public enum Icon {
    SEARCH("/icons/search.png"),
    MAIN("/icons/main.png");

    private static final Logger LOGGER = getLogger(Icon.class);

    private String path;

    private List<Function<ImageIcon, ImageIcon>> transformations = Lists.newLinkedList();

    Icon(String path) {
        this.path = path;
    }

    public Icon scale(int w, int h) {
        return scale(null, w, h);
    }

    public Icon scale(Component component, int w, int h) {
        transformations.add(imageIcon -> IconsFactory.getScaledImage(component, imageIcon, w, h));
        return this;
    }

    public Icon negative() {
        transformations.add(imageIcon -> IconsFactory.createNegativeImage(imageIcon.getImage()));
        return this;
    }

    public ImageIcon build() {
        final ImageIcon[] built = {IconsFactory.getImageIcon(Icon.class, path)};
        transformations.stream().forEach(function -> built[0] = function.apply(built[0]));
        transformations.clear();
        return built[0];
    }

    public Icon tinted(Color color) {
        transformations.add(imageIcon -> IconsFactory.createTintedImage(imageIcon, color));
        return this;
    }
}
