/* class InputEvent
 *
 * Copyright (C) 2001-2003  R M Pitman
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

package charva.awt.event;

import java.awt.AWTEvent;

import charva.awt.*;

/**
 * This is the superclass of KeyEvent and MouseEvent.
 */
public class InputEvent
        extends AWTEvent {

    public InputEvent(Component source_, int id_) {
        super(source_, id_);
    }

    /**
     * Provides a way to flag the event as having been consumed,
     * so that it never reaches its destination component.
     */
    public void consume() {
        _consumed = true;
    }

    public boolean isConsumed() {
        return _consumed;
    }

    //====================================================================
    // INSTANCE VARIABLES
    private boolean _consumed = false;
}
