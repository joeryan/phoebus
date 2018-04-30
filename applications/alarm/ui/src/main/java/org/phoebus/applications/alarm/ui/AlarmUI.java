/*******************************************************************************
 * Copyright (c) 2018 Oak Ridge National Laboratory.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.phoebus.applications.alarm.ui;

import org.phoebus.applications.alarm.model.SeverityLevel;
import org.phoebus.ui.javafx.ImageCache;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;

/** Common alarm UI helpers
 *
 *  <p>Icons for {@link SeverityLevel}.
 *
 *  @author Kay Kasemir
 */
@SuppressWarnings("nls")
public class AlarmUI
{
    // Next arrays follow the ordinal of SeverityLevel
    private static Color[] severity_colors = new Color[]
    {
        Color.rgb(  0, 100,   0), // OK
        Color.rgb(180, 170,  70), // MINOR_ACK
        Color.rgb(255,  90,  90), // MAJOR_ACK
        Color.rgb(255, 128, 255), // INVALID_ACK
        Color.rgb(255, 128, 255), // UNDEFINED_ACK
        Color.rgb(207, 192,   0), // MINOR
        Color.rgb(255,   0,   0), // MAJOR
        Color.rgb(255,   0, 255), // INVALID
        Color.rgb(255,   0, 255), // UNDEFINED
    };

    private static Image[] severity_icons = new Image[]
    {
        null, // OK
        ImageCache.getImage(AlarmUI.class, "/icons/minor_ack.png"),
        ImageCache.getImage(AlarmUI.class, "/icons/major_ack.png"),
        ImageCache.getImage(AlarmUI.class, "/icons/unknown_ack.png"),
        ImageCache.getImage(AlarmUI.class, "/icons/unknown_ack.png"),
        ImageCache.getImage(AlarmUI.class, "/icons/minor.png"),
        ImageCache.getImage(AlarmUI.class, "/icons/major.png"),
        ImageCache.getImage(AlarmUI.class, "/icons/unknown.png"),
        ImageCache.getImage(AlarmUI.class, "/icons/unknown.png")
    };

    /** @param severity {@link SeverityLevel}
     *  @return Color
     */
    public static Color getColor(final SeverityLevel severity)
    {
        return severity_colors[severity.ordinal()];

    }

    /** @param severity {@link SeverityLevel}
     *  @return Icon, may be <code>null</code>
     */
    public static Image getIcon(final SeverityLevel severity)
    {
        return severity_icons[severity.ordinal()];
    }
}