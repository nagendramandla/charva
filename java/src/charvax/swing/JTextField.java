/* class JTextField
 *
 * Copyright (C) 2001  R M Pitman
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package charvax.swing;

import charva.awt.*;
import charva.awt.event.*;
import charvax.swing.text.JTextComponent;

import java.util.Vector;

/**
 * JTextField is a component that allows the display and editing of a
 * single line of text.
 * The JTextField class, being a subclass of JComponent, has a setBorder()
 * method which allows an optional Border to be set.
 */

/**
 * Support for
 * public void setBounds( Rectangle bounds )
 * public void setBounds( int top_, int left_, int bottom_, int right_)
 * public void setBounds(Point topleft_, Dimension size_)
 */
public class JTextField
        extends JTextComponent {

    /**
     * Construct a text field. The initial string is empty and the
     * number of columns is 10.
     */
    public JTextField() {
        this("", 10);
    }

    /**
     * Use this constructor when you want to initialize the value.
     */
    public JTextField(String text_) {
        this(text_, text_.length());
    }

    /**
     * Use this constructor when you want to leave the text field empty
     * but set its length.
     */
    public JTextField(int length_) {
        this("", length_);
    }

    /**
     * Use this constructor when you want to set both the initial value and the
     * length.
     */
    public JTextField(String text_, int length_) {
        super.setText(text_);
        setColumns(length_);
        super._caretPosition = super._document.length();
        if (super._document.length() > _columns)
            _offset = super._document.length() - _columns;
        else
            _offset = 0;
    }

    /**
     * Sets the number of columns in this Textfield, and then invalidates
     * the layout.
     */
    public void setColumns(int columns_) {
        _columns = columns_;

        StringBuffer padbuf = new StringBuffer();
        for (int i = 0; i < _columns; i++)
            padbuf.append(' ');
        _padding = new String(padbuf);
        super.invalidate();
    }

    public void setBounds(Rectangle bounds) {
        super.setBounds(bounds);
        setColumns(bounds.getRight() - bounds.getLeft() + 1);

    }

    public void setBounds(int top_, int left_, int bottom_, int right_) {
        super.setBounds(top_, left_, bottom_, right_);
        setColumns(right_ - left_ + 1);
    }

    public void setBounds(Point topleft_, Dimension size_) {
        super.setBounds(topleft_, size_);
        setColumns(size_.width);
    }

    /**
     * Return the number of columns in the text field.
     */
    public int getColumns() {
        return _columns;
    }

    public void setFont(Font font_) {
        _bold = ((font_.getStyle() & Font.BOLD) != 0);
    }


    /**
     * Set the action command
     */
    public void setActionCommand(String cmd_) {
        _actionCommand = cmd_;
    }

    /**
     * Get the action command
     */
    public String getActionCommand() {
        return _actionCommand;
    }

    /**
     * Return the size of the text field. Overrides the method in the
     * Component superclass.
     */
    public Dimension getSize() {
        Insets insets = super.getInsets();
        return new Dimension(_columns + insets.left + insets.right,
                1 + insets.top + insets.bottom);
    }

    public int getWidth() {
        Insets insets = super.getInsets();
        return _columns + insets.left + insets.right;
    }

    public int getHeight() {
        Insets insets = super.getInsets();
        return 1 + insets.top + insets.bottom;
    }

    /**
     * Called by the LayoutManager.
     */
    public Dimension minimumSize() {
        return getSize();
    }

    /**
     * Sets whether this textfield can be edited.
     */
    public void setEditable(boolean editable_) {
        super.setEnabled(editable_);
    }

    public boolean isEditable() {
        return isEnabled();
    }

    /**
     * Called by this JTextField's parent container.
     */
    public void draw() {

        /* Draw the border inherited from JComponent, if it exists.
         */
        super.draw();

        /* If the field is enabled, it is drawn with the UNDERLINE
         * attribute.  If it is disabled, it is drawn without the
         * UNDERLINE attribute.
         */
        int attrib = 0;
        if (super._enabled)
            attrib |= Toolkit.A_UNDERLINE;

        if (_bold)
            attrib |= Toolkit.A_BOLD;

        /* Get the absolute origin of this component.
         */
        Point origin = getLocationOnScreen();
        Insets insets = super.getInsets();
        origin.translate(insets.left, insets.top);

        Toolkit term = Toolkit.getDefaultToolkit();

        int colorpair = getCursesColor();

        term.setCursor(origin);
        term.addString(_padding, attrib, colorpair);
        term.setCursor(origin);

        // Get the displayable portion of the string
        int end;
        if (super.getText().length() > (_offset + _columns))
            end = _offset + _columns;
        else
            end = super.getText().length();

        term.addString(super.getText().substring(_offset, end).toString(),
                attrib, colorpair);
        term.setCursor(origin.addOffset(super._caretPosition - _offset, 0));
    }

    /**
     * Process KeyEvents that have been generated by this JTextField.
     */
    public void processKeyEvent(KeyEvent ke_) {

        /* First call all KeyListener objects that may have been registered
         * for this component.
         */
        super.processKeyEvent(ke_);

        /* Check if any of the KeyListeners consumed the KeyEvent.
         */
        if (ke_.isConsumed())
            return;

        int key = ke_.getKeyCode();
        if (key == '\t') {
            getParent().nextFocus();
            return;
        } else if (key == KeyEvent.VK_BACK_TAB) {
            getParent().previousFocus();
            return;
        }

        /*
         */
        if (!ke_.isActionKey()) {

            /* It is a control-character or a printable character. If it is not a control character,
             * insert it into the text buffer.
             */
            if (key >= ' ') {
                super._document.insert(super._caretPosition, ke_.getKeyChar());
                super._caretPosition++;
                if (super._caretPosition - _offset > _columns)
                    _offset++;
            }
        } else {

            /* It is an action key.
             */
            if (key == KeyEvent.VK_LEFT && super._caretPosition > 0) {
                super._caretPosition--;
                if (super._caretPosition < _offset)
                    _offset--;

            } else if (key == KeyEvent.VK_RIGHT && super._caretPosition < super._document.length()) {
                super._caretPosition++;
                if (super._caretPosition - _offset > _columns)
                    _offset++;

            } else if (key == KeyEvent.VK_BACK_SPACE && super._caretPosition > 0) {
                super._caretPosition--;
                super._document.deleteCharAt(super._caretPosition);
                if (super._caretPosition < _offset)
                    _offset--;

            } else if (key == KeyEvent.VK_DELETE &&
                    super._caretPosition >= 0 && super._caretPosition < super._document.length()) {
                super._document.deleteCharAt(super._caretPosition);

            } else if (key == KeyEvent.VK_HOME) {
                super._caretPosition = 0;
                _offset = 0;

            } else if (key == KeyEvent.VK_END) {
                super._caretPosition = super._document.length();
                if (super._document.length() > _columns)
                    _offset = super._document.length() - _columns;
                else
                    _offset = 0;
            }

            /* Post an action event if ENTER was pressed.
             */
            else if (key == KeyEvent.VK_ENTER) {
                ActionEvent ae = new ActionEvent(this, getActionCommand());
                Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(ae);
            }
        }
        draw();
        super.requestSync();
    }

    /**
     * Process a MouseEvent that was generated by clicking the mouse
     * somewhere inside this JTextField.
     * Clicking the mouse inside the JTextField moves the caret position
     * to where the mouse was clicked.
     */
    public void processMouseEvent(MouseEvent e_) {
        super.processMouseEvent(e_);
        if (e_.isConsumed())
            return;

        if (e_.getButton() == MouseEvent.BUTTON1 &&
                e_.getModifiers() == MouseEvent.MOUSE_CLICKED &&
                this.isFocusTraversable()) {

            int x = e_.getX();

            /* Get the absolute origin of this component.
             */
            Point origin = getLocationOnScreen();
            Insets insets = super.getInsets();
            origin.translate(insets.left, insets.top);

            int new_caret = _offset + (x - origin.x);
            super._caretPosition =
                    (new_caret < super._document.length()) ?
                            new_caret : super._document.length();
            repaint();
        }
    }

    /**
     * Set the text that is presented by this JTextField.
     * This method is thread-safe, although most Charva methods are not.
     */
    public synchronized void setText(String text_) {
        super.setText(text_);

        super._caretPosition = super._document.length();
        if (super._document.length() > _columns)
            _offset = super._document.length() - _columns;
        else
            _offset = 0;

        /* If this component is already displayed, generate a PaintEvent
         * and post it onto the queue.
         */
        super.repaint();
    }

    /**
     * Process events.
     */
    protected void processEvent(AWTEvent evt_) {

        super.processEvent(evt_);

        if (evt_ instanceof ActionEvent)
            postActionEvent((ActionEvent) evt_);
    }

    /**
     * Invoke all the ActionListener callbacks that may have been registered
     * for this component. The listener list is processed in last to first order.
     */
    public void postActionEvent(ActionEvent ae_) {
        if (_actionListeners != null) {
            for (int i = _actionListeners.size() - 1; i >= 0; i--) {
                ActionListener al = (ActionListener) _actionListeners.get(i);
                al.actionPerformed(ae_);
            }
        }
    }

    public void fireActionPerformed() {
        ActionEvent ae = new ActionEvent(this, getActionCommand());
        postActionEvent(ae);
    }

    /**
     * Register an ActionListener object for this component.
     */
    public void addActionListener(ActionListener al_) {
        if (_actionListeners == null)
            _actionListeners = new Vector();
        _actionListeners.add(al_);
    }

    public void requestFocus() {
        /* Generate the FOCUS_GAINED event.
         */
        super.requestFocus();

        /* Get the absolute origin of this component.
         */
        Point origin = getLocationOnScreen();
        Insets insets = super.getInsets();
        origin.translate(insets.left, insets.top);
        Toolkit.getDefaultToolkit().setCursor(origin.addOffset(super._caretPosition - _offset, 0));
    }

    /**
     * Returns a String representation of this component.
     */
    public String toString() {
        return "JTextField location=" + getLocation() +
                " text=\"" + super._document + "\"" +
                " actionCommand=\"" + getActionCommand() + "\"";
    }

    public void debug(int level_) {
        for (int i = 0; i < level_; i++)
            System.err.print("    ");
        System.err.println("JTextField origin=" + _origin +
                " size=" + getSize() + " text=" + super._document);
    }

    //====================================================================
    // INSTANCE VARIABLES

    protected boolean _bold = false;

    protected int _columns;

    /**
     * Index (from the start of the string) of the character displayed
     * in the left corner of the field. This is always 0 if the string
     * is shorter than the field.
     */
    protected int _offset = 0;

    /**
     * A blank-filled string the same length as the JTextField.
     */
    protected String _padding;

    /**
     * The string that is sent inside an ActionEvent when ENTER is pressed.
     */
    private String _actionCommand = new String("");

    /**
     * A list of ActionListeners registered for this component.
     */
    protected Vector _actionListeners = null;
}
