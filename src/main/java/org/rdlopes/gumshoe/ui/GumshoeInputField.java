package org.rdlopes.gumshoe.ui;

import com.jidesoft.swing.DefaultOverlayable;
import com.jidesoft.swing.NullPanel;
import com.jidesoft.swing.Overlayable;
import com.jidesoft.swing.StyledLabel;
import org.jdesktop.swingx.HorizontalLayout;
import org.jdesktop.swingx.JXLabel;
import org.rdlopes.gumshoe.GumshoeProperties;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.FocusListener;
import java.awt.event.KeyListener;

import static javax.swing.BorderFactory.createEmptyBorder;
import static org.rdlopes.gumshoe.resources.Icon.SEARCH;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by rui on 18/02/2017.
 * Part of gumshoe.
 * <p>
 */
@Component
public class GumshoeInputField extends NullPanel implements DocumentListener {

    private static final Logger LOG = getLogger(GumshoeInputField.class);

    private final DefaultOverlayable hintContainer;

    private final JXLabel hintLabel;

    private final StyledLabel iconLabel;

    private final JTextArea textArea;

    public GumshoeInputField(GumshoeProperties properties) {
        super(new HorizontalLayout());

        textArea = new JTextArea(1, 20);
        iconLabel = new StyledLabel("", SwingConstants.CENTER);
        hintLabel = new JXLabel("Type search...");
        hintContainer = new DefaultOverlayable(textArea, hintLabel, DefaultOverlayable.LEADING);

        add(iconLabel);
        add(hintContainer);

        textArea.setOpaque(false);
        iconLabel.setOpaque(false);
        hintLabel.setOpaque(false);

        Border border = createEmptyBorder(0, properties.getBorderGap(), 0, properties.getBorderGap());
        textArea.setBorder(border);
        iconLabel.setBorder(border);
        hintLabel.setBorder(border);
    }

    @Override
    public void addFocusListener(FocusListener l) {
        textArea.addFocusListener(l);
    }

    @Override
    public void removeFocusListener(FocusListener l) {
        textArea.removeFocusListener(l);
    }

    @Override
    public FocusListener[] getFocusListeners() {
        return textArea.getFocusListeners();
    }

    @Override
    public void addKeyListener(KeyListener l) {
        super.addKeyListener(l);
        textArea.addKeyListener(l);
        hintContainer.addKeyListener(l);
        hintLabel.addKeyListener(l);
    }

    @Override
    public void removeKeyListener(KeyListener l) {
        textArea.removeKeyListener(l);
    }

    @Override
    public KeyListener[] getKeyListeners() {
        return textArea.getKeyListeners();
    }

    private void adjustOverlay(JTextComponent textComponent, Overlayable overlayable) {
        String text = textComponent.getText();
        if (text != null && text.length() != 0) {
            overlayable.setOverlayVisible(false);
        } else {
            overlayable.setOverlayVisible(true);
        }
    }

    void didBecomeVisible(boolean visible) {
        adjustOverlay(textArea, hintContainer);
        if (visible) {
            LOG.info("didBecomeVisible with text {}", textArea.getText());
            textArea.selectAll();
        }
    }

    JTextArea getTextArea() {
        return textArea;
    }

    @PostConstruct
    public void init() {
        iconLabel.setIcon(SEARCH.tinted(Color.lightGray).scale(this, 16, 16).build());
        textArea.setRequestFocusEnabled(true);
        textArea.getDocument().addDocumentListener(this);

    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        adjustOverlay(textArea, hintContainer);
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        adjustOverlay(textArea, hintContainer);
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        adjustOverlay(textArea, hintContainer);
    }

    void willBecomeVisible(boolean visible) {
        adjustOverlay(textArea, hintContainer);
        LOG.info("willBecomeInvisible {}", visible);
    }
}
