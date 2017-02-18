package org.rdlopes.gumshoe;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.swing.*;
import java.awt.*;

/**
 * Created by rui on 18/02/2017.
 * Part of gumshoe.
 * <p>
 */
@Data
@ConfigurationProperties("gumshoe")
public class GumshoeProperties {

    private Color background = Color.darkGray;

    private int borderGap = 5;

    private int borderSize = 20;

    private int fontSize = 20;

    private Color foreground = Color.lightGray;

    private KeyStroke keyStroke = KeyStroke.getKeyStroke("control SPACE");
}
