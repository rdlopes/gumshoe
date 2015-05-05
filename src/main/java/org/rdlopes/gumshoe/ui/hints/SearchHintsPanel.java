package org.rdlopes.gumshoe.ui.hints;

import com.google.common.base.Strings;
import com.jidesoft.swing.FontUtils;
import com.jidesoft.swing.JideScrollPane;
import com.jidesoft.swing.NullPanel;
import com.jidesoft.swing.Sticky;
import org.jdesktop.swingx.JXList;
import org.jdesktop.swingx.VerticalLayout;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static org.rdlopes.gumshoe.ui.UIGlobals.*;

/**
 * User: ruilopes
 * Date: 03/05/2015
 * Time: 19:11
 */
public class SearchHintsPanel {

    private final JTextComponent textSource;

    private final Predicate<String> searchInputPredicate;

    private final InternalPanel contentPanel = new InternalPanel();

    private final HintsListCellRenderer hintsCellRenderer = new HintsListCellRenderer();

    public SearchHintsPanel(JTextComponent textComponent) {
        this.textSource = textComponent;

        searchInputPredicate = s -> {
            boolean hasContent = !Strings.isNullOrEmpty(s);
            boolean hasEnoughLength = s.length() > 3;
            return hasContent && hasEnoughLength;
        };

    }

    public Object getSelectedValue() {
        return getHintsList().getSelectedValue();
    }

    public JXList getHintsList() {
        return getContentPanel().getHintsList();
    }

    public InternalPanel getContentPanel() {
        return contentPanel;
    }

    public void updateHints(String searchInput) {
        getHintsList().setListData(
                Stream.of(new SearchHintsSupplier(searchInput).get())
                        .filter(searchInputPredicate)
                        .toArray());
        getHintsList().getSelectionModel().setAnchorSelectionIndex(-1); // has to call setAnchor first
        getHintsList().getSelectionModel().setLeadSelectionIndex(-1);
        getHintsList().getSelectionModel().clearSelection();
        getContentPanel().getScrollPanel().setViewportView(getHintsList());
    }

    private class InternalPanel extends NullPanel {

        private final JXList hintsList;

        private final JideScrollPane scrollPanel;

        public InternalPanel() {
            super(new VerticalLayout());

            hintsList = new SearchHintsList();
            scrollPanel = new JideScrollPane(hintsList);
            new Sticky(hintsList);
            hintsList.setFocusable(false);

            scrollPanel.setHorizontalScrollBarPolicy(JideScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            scrollPanel.setBorder(BorderFactory.createEmptyBorder());
            scrollPanel.getVerticalScrollBar().setFocusable(false);
            scrollPanel.getHorizontalScrollBar().setFocusable(false);

            add(scrollPanel, BorderLayout.CENTER);
        }

        public JideScrollPane getScrollPanel() {
            return scrollPanel;
        }

        public JXList getHintsList() {
            return hintsList;
        }
    }

    private class SearchHintsList extends JXList {

        /**
         * Constructs a <code>JXList</code> with an empty model and filters disabled.
         */
        public SearchHintsList() {
            super();
            setCellRenderer(hintsCellRenderer);
        }

        @Override
        public int getVisibleRowCount() {
            int size = getModel().getSize();
            return size < super.getVisibleRowCount() ? size : super.getVisibleRowCount();
        }

        @Override
        public Dimension getPreferredScrollableViewportSize() {
            if (getModel().getSize() == 0) {
                return new Dimension(0, 0);
            } else {
                // TODO - find correct end without overlapping
                int width = textSource.getWidth();
                int height = (int) super.getPreferredScrollableViewportSize().getHeight();
                return new Dimension(width, height);
            }
        }
    }

    private class HintsListCellRenderer extends NullPanel implements ListCellRenderer<String> {

        public HintsListCellRenderer() {
            Font font = textSource == null ? super.getFont() : textSource.getFont();
            if (font != null)
                setFont(FontUtils.getCachedDerivedFont(font, Font.PLAIN, FONT_SIZE));
            setBackground(BACKGROUND);
            setForeground(FOREGROUND);
            setOpaque(false);
            setBorder(BorderFactory.createEmptyBorder(0, GAP, 0, GAP));
            setOpaque(false);
            setBorder(BorderFactory.createEmptyBorder(0, GAP, 0, GAP));
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends String> list, String value, int index, boolean isSelected, boolean cellHasFocus) {
            return this;
        }
    }
}
