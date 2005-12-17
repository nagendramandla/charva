/* class FocusEvent
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

package charva.awt.event;

import charva.awt.event.AWTEvent;
import charva.awt.*;

/**
 * A low-level event that indicates that an object has gained or lost the
 * keyboard focus. This event is generated by a component such as a 
 * textfield. This event is passed to every FocusListener object that is
 * registered to receive such events using the object's addFocusListener()
 * method.
 */
public class FocusEvent
    extends AWTEvent
{
    /**
     * Create a new FocusEvent.
     * @param id_ The event identifier. Must be FOCUS_LOST or FOCUS_GAINED.
     * @param source_ The component to which the event must be delivered.
     */
    public FocusEvent(int id_, Component source_) {
	super(source_, id_);
    }

    public String toString() {
	return ("FocusEvent: source=[" + getSource() + "] id=" + 
	    ((getID() == FOCUS_LOST) ? "FOCUS_LOST" : "FOCUS_GAINED"));
    }
}
