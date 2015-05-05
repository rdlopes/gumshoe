package org.rdlopes.gumshoe.ui.hints;

import com.google.common.collect.ImmutableList;
import com.jidesoft.hints.AbstractIntelliHints;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.event.KeyEvent;

/**
 * User: ruilopes
 * Date: 03/05/2015
 * Time: 18:30
 */
public class SearchHintsComponent extends AbstractIntelliHints {

    private static KeyStroke[] KEY_STROKE_ARRAY = ImmutableList.of(
            KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0),
            KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0),
            KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_DOWN, 0),
            KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_UP, 0),
            KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_DOWN, KeyEvent.CTRL_DOWN_MASK),
            KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_UP, KeyEvent.CTRL_DOWN_MASK),
            KeyStroke.getKeyStroke(KeyEvent.VK_HOME, KeyEvent.CTRL_DOWN_MASK),
            KeyStroke.getKeyStroke(KeyEvent.VK_END, KeyEvent.CTRL_DOWN_MASK)
    ).toArray(new KeyStroke[0]);

    private SearchHintsPanel searchHintsPanel;

    public SearchHintsComponent(JTextComponent textComponent) {
        super(textComponent);
        searchHintsPanel = new SearchHintsPanel(getTextComponent());
    }

    @Override
    public void hideHintsPopup() {
        super.hideHintsPopup();
    }

    @Override
    public KeyStroke[] getDelegateKeyStrokes() {
        return KEY_STROKE_ARRAY;
    }

    @Override
    protected JComponent getDelegateComponent() {
        return searchHintsPanel.getHintsList();
    }

    @Override
    public JComponent createHintsComponent() {
        return searchHintsPanel.getContentPanel();
    }

    @Override
    public boolean updateHints(Object context) {
        searchHintsPanel.updateHints(String.valueOf(context));
        return searchHintsPanel.getHintsList().getModel().getSize() > 0;
    }

    public Object getSelectedHint() {
        return searchHintsPanel.getSelectedValue();
    }
}
