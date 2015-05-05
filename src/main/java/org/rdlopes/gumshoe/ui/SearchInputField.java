package org.rdlopes.gumshoe.ui;

import com.google.common.base.Strings;
import com.jidesoft.swing.*;
import org.jdesktop.swingx.HorizontalLayout;
import org.jdesktop.swingx.JXLabel;
import org.rdlopes.gumshoe.ui.hints.SearchHintsComponent;

import javax.annotation.PostConstruct;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.FocusListener;
import java.awt.event.KeyListener;
import java.util.Arrays;

import static org.rdlopes.gumshoe.resources.Icon.SEARCH;
import static org.rdlopes.gumshoe.ui.UIGlobals.*;

/**
 * User: ruilopes
 * Date: 03/05/2015
 * Time: 15:50
 */
@org.springframework.stereotype.Component
public class SearchInputField extends NullPanel implements DocumentListener {

    private final StyledLabel iconLabel;

    private final JTextArea textArea;

    private final DefaultOverlayable hintContainer;

    private final JXLabel hintLabel;

    private final SearchHintsComponent searchHintsComponent;

    public SearchInputField() {
        super(new HorizontalLayout());

        textArea = new JTextArea(1, 20);
        searchHintsComponent = new SearchHintsComponent(textArea);
        iconLabel = new StyledLabel("", SwingConstants.CENTER);
        hintLabel = new JXLabel("Type search...");
        hintContainer = new DefaultOverlayable(textArea, hintLabel, DefaultOverlayable.LEADING);

        add(iconLabel);
        add(hintContainer);

        textArea.setOpaque(false);
        iconLabel.setOpaque(false);
        hintLabel.setOpaque(false);

        textArea.setBorder(BorderFactory.createEmptyBorder(0, GAP, 0, GAP));
        iconLabel.setBorder(BorderFactory.createEmptyBorder(0, GAP, 0, GAP));
        hintLabel.setBorder(BorderFactory.createEmptyBorder(0, GAP, 0, GAP));
    }

    public JTextArea getTextArea() {
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

    private void adjustOverlay(JTextComponent textComponent, Overlayable overlayable) {
        String text = textComponent.getText();
        if (text != null && text.length() != 0) {
            overlayable.setOverlayVisible(false);
        } else {
            overlayable.setOverlayVisible(true);
        }
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        adjustOverlay(textArea, hintContainer);
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        adjustOverlay(textArea, hintContainer);
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

    public void willBecomeVisible(boolean visible) {
        adjustOverlay(textArea, hintContainer);
        if (!visible) {
            searchHintsComponent.hideHintsPopup();
        }
    }

    public void didBecomeVisible(boolean visible) {
        adjustOverlay(textArea, hintContainer);
        if (visible) {
            if (!Strings.isNullOrEmpty(textArea.getText())) {
                searchHintsComponent.showHints();
            }
            textArea.selectAll();
        }
    }

}
