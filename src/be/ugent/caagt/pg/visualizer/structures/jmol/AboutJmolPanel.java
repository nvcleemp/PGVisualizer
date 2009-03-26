/* AboutJmolPanel.java
 * =========================================================================
 * This file is part of the PG project - http://caagt.ugent.be/azul
 * 
 * Copyright (C) 2008-2009 Universiteit Gent
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or (at
 * your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 * 
 * A copy of the GNU General Public License can be found in the file
 * LICENSE.txt provided with the source distribution of this program (see
 * the META-INF directory in the source jar). This license can also be
 * found on the GNU website at http://www.gnu.org/licenses/gpl.html.
 * 
 * If you did not receive a copy of the GNU General Public License along
 * with this program, contact the lead developer, or write to the Free
 * Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301, USA.
 */

package be.ugent.caagt.pg.visualizer.structures.jmol;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.MessageFormat;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.Timer;
import javax.swing.table.AbstractTableModel;

import org.jmol.api.JmolViewer;
import org.jmol.viewer.JmolConstants;

/**
 *
 * @author nvcleemp
 */
public class AboutJmolPanel extends JPanel {

    private JmolViewer viewer;

    public AboutJmolPanel(JmolViewer viewer) {
        super(new BorderLayout());
        this.viewer = viewer;
        add(new JTable(new AboutPanelTableModel()), BorderLayout.CENTER);
    }

    private class AboutPanelTableModel extends AbstractTableModel implements ActionListener {

        public AboutPanelTableModel() {
            new Timer(5000, this).start();
        }

        public int getRowCount() {
            return Rows.values().length;
        }

        public int getColumnCount() {
            return 2;
        }

        public Object getValueAt(int rowIndex, int columnIndex) {
            if (columnIndex == 1) {
                return Rows.values()[rowIndex].getValue(viewer);
            } else {
                return Rows.values()[rowIndex].getName();
            }
        }

        public void actionPerformed(ActionEvent e) {
            for (int i = 0; i < Rows.values().length; i++) {
                if (Rows.values()[i].isVariable()) {
                    fireTableCellUpdated(i, 1);
                }
            }
        }
    }

    private static enum Rows {

        VERSION {

            @Override
            String getName() {
                return "Jmol version";
            }

            @Override
            Object getValue(JmolViewer viewer) {
                return JmolConstants.version;
            }
        }, DATE {

            @Override
            String getName() {
                return "Build date";
            }

            @Override
            Object getValue(JmolViewer viewer) {
                return JmolConstants.date;
            }
        }, OS {

            @Override
            String getName() {
                return "Operating system";
            }

            @Override
            Object getValue(JmolViewer viewer) {
                return viewer.getOperatingSystemName();
            }
        }, VENDER {

            @Override
            String getName() {
                return "Java Vendor";
            }

            @Override
            Object getValue(JmolViewer viewer) {
                return viewer.getJavaVendor();
            }
        }, JAVAVERSION {

            @Override
            String getName() {
                return "Java version";
            }

            @Override
            Object getValue(JmolViewer viewer) {
                return viewer.getJavaVersion();
            }
        }, TOTALMEM {

            @Override
            String getName() {
                return "Total memory";
            }

            @Override
            Object getValue(JmolViewer viewer) {
                return MessageFormat.format("{0} MB",
                        convertToMegabytes(Runtime.getRuntime().totalMemory()));
            }

            @Override
            boolean isVariable() {
                return true;
            }
        }, FREEMEM {

            @Override
            String getName() {
                return "Free memory";
            }

            @Override
            Object getValue(JmolViewer viewer) {
                return MessageFormat.format("{0} MB",
                        convertToMegabytes(Runtime.getRuntime().freeMemory()));
            }

            @Override
            boolean isVariable() {
                return true;
            }
        }, MAXMEM {

            @Override
            String getName() {
                return "Maximum memory";
            }

            @Override
            Object getValue(JmolViewer viewer) {
                return MessageFormat.format("{0} MB",
                        convertToMegabytes(Runtime.getRuntime().maxMemory()));
            }

            @Override
            boolean isVariable() {
                return true;
            }
        }, PROCESSORS {

            @Override
            String getName() {
                return "Processors";
            }

            @Override
            Object getValue(JmolViewer viewer) {
                return Runtime.getRuntime().availableProcessors();
            }

            @Override
            boolean isVariable() {
                return true;
            }
        };

        abstract String getName();

        abstract Object getValue(JmolViewer viewer);

        boolean isVariable() {
            return false;
        }
    }

    private static long convertToMegabytes(long num) {
        if (num <= Long.MAX_VALUE - 512 * 1024) {
            num += 512 * 1024;
        }
        return num / (1024 * 1024);
    }
}
