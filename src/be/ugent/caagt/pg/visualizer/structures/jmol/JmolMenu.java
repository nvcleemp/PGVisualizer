/* JmolMenu.java
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

import be.ugent.caagt.pg.visualizer.gui.ToolWindow;
import be.ugent.caagt.pg.visualizer.gui.action.ShowWindowAction;
import be.ugent.caagt.pg.visualizer.structures.JmolPanel;
import java.awt.Color;
import java.text.MessageFormat;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author nvcleemp
 */
public class JmolMenu {

    private JmolMenu(){
        //should not be instantiated
    }

    public static JMenuBar getJMenuBar(final JmolPanel panel, final JmolData data, final JFrame target){
        JMenuBar bar = new JMenuBar();
        //---------------EXPORT----------------------
        JMenu export = new JMenu("Export");
        export.add(new JmolExportImageAction(panel));
        bar.add(export);
        //----------------VIEW-----------------------
        JMenu view = new JMenu("View");
        view.add(new ViewAction("Front", data.getViewer(), ViewAction.FRONT));
        view.add(new ViewAction("Left", data.getViewer(), ViewAction.LEFT));
        view.add(new ViewAction("Right", data.getViewer(), ViewAction.RIGHT));
        view.add(new ViewAction("Top", data.getViewer(), ViewAction.TOP));
        view.add(new ViewAction("Bottom", data.getViewer(), ViewAction.BOTTOM));
        view.add(new ViewAction("Back", data.getViewer(), ViewAction.BACK));
        bar.add(view);
        //----------------DISPLAY-----------------------
        JMenu display = new JMenu("Display");
        display.add(new JmolToggleMenuItem("Perspective depth", "set perspectiveDepth true", "set perspectiveDepth false", data, true));
        display.add(new JmolToggleMenuItem("Bounding box", "boundbox ON", "boundbox OFF", data));
        display.add(new JmolToggleMenuItem("Axes", "axes ON", "axes OFF", data));
        //stereographic menu
        JMenu stereographicMenu = new JMenu("Stereographic");
        ButtonGroup stereographicGroup = new ButtonGroup();
        String[] stereographicNames = {"None", "Red+Cyan glasses", "Red+Blue glasses",
                                "Red+Green glasses", "Cross-eyed viewing", "Wall-eyed viewing"};
        String[] stereographicCommands = {"stereo off", "stereo redcyan 3", "stereo redblue 3",
                                "stereo redgreen 3", "stereo 5", "stereo -5"};
        for (int i = 0; i < stereographicCommands.length; i++) {
            JRadioButtonMenuItem stereographicItem = new JRadioButtonMenuItem(
                    new DefaultJmolAction(stereographicNames[i], data.getViewer(), stereographicCommands[i]));
            stereographicItem.setSelected(i==0);
            stereographicGroup.add(stereographicItem);
            stereographicMenu.add(stereographicItem);
        }
        display.add(stereographicMenu);
        display.addSeparator();
        //style menu
        JMenu styleMenu = new JMenu("Style");
        ButtonGroup styleGroup = new ButtonGroup();
        String[] styleNames = {"CPK Spacefill", "Ball and Stick", "Sticks", "Wireframe"};
        String[] styleCommands = {"backbone off;wireframe off;spacefill 100%", "backbone off;spacefill 20%;wireframe 0.15",
                                  "backbone off;spacefill off;wireframe 0.3", "backbone off;spacefill off;wireframe on"};
        for (int i = 0; i < styleCommands.length; i++) {
            JRadioButtonMenuItem styleItem = new JRadioButtonMenuItem(
                    new DefaultJmolAction(styleNames[i], data.getViewer(), styleCommands[i]));
            styleItem.setSelected(i==1);
            styleGroup.add(styleItem);
            styleMenu.add(styleItem);
        }
        display.add(styleMenu);
        display.addSeparator();
        //labels menu
        JMenu labelsMenu = new JMenu("Labels");
        ButtonGroup labelGroup = new ButtonGroup();
        String[] labelNames = {"None", "Element symbol", "Atom number", "Atom name"};
        String[] labelCommands = {"label off", "label %e", "label %i", "label %a"};
        for (int i = 0; i < labelCommands.length; i++) {
            JRadioButtonMenuItem labelItem = new JRadioButtonMenuItem(
                    new DefaultJmolAction(labelNames[i], data.getViewer(), labelCommands[i]));
            labelItem.setSelected(i==0);
            labelGroup.add(labelItem);
            labelsMenu.add(labelItem);
        }
        labelsMenu.addSeparator();
        ButtonGroup labelPositionGroup = new ButtonGroup();
        String[] labelPositionNames = {"Centered", "Upper Right", "Lower Right", "Upper Left", "Lower Left"};
        String[] labelPositionCommands = {"set labeloffset 0 0", "set labeloffset 4 4",
               "set labeloffset 4 -4", "set labeloffset -4 4", "set labeloffset -4 -4"};
        for (int i = 0; i < labelPositionCommands.length; i++) {
            JRadioButtonMenuItem labelPositionItem = new JRadioButtonMenuItem(
                    new DefaultJmolAction(labelPositionNames[i], data.getViewer(), labelPositionCommands[i]));
            labelPositionItem.setSelected(i==1);
            labelPositionGroup.add(labelPositionItem);
            labelsMenu.add(labelPositionItem);
        }
        display.add(labelsMenu);
        //atoms menu
        JMenu atomsMenu = new JMenu("Atoms");
        ButtonGroup atomsGroup = new ButtonGroup();
        String[] atomStyleName = {"Off", "15% van der Waals", "20% van der Waals",
        "25% van der Waals", "50% van der Waals", "75% van der Waals", "100% van der Waals"};
        String[] atomstyleCommand = {"cpk off", "cpk 15%", "cpk 20%", "cpk 25%", "cpk 50%",
        "cpk 75%", "cpk on"};
        for (int i = 0; i < atomstyleCommand.length; i++) {
            JRadioButtonMenuItem atomStyleItem = new JRadioButtonMenuItem(
                    new DefaultJmolAction(atomStyleName[i], data.getViewer(), atomstyleCommand[i]));
            atomStyleItem.setSelected(i==2);
            atomsGroup.add(atomStyleItem);
            atomsMenu.add(atomStyleItem);
        }
        display.add(atomsMenu);
        //bonds menu
        JMenu bondsStyleMenu = new JMenu("Bonds");
        ButtonGroup bondsStyleGroup = new ButtonGroup();
        String[] bondsStyleNames = {"None", "0.10 \u00C5", "0.15 \u00C5", "0.20 \u00C5", "0.25 \u00C5", "0.30 \u00C5", "0.50 \u00C5"};
        String[] bondsStyleCommands = {"wireframe off", "wireframe .1", "wireframe .15", "wireframe .2", "wireframe .25", "wireframe .3", "wireframe .5"};
        for (int i = 0; i < bondsStyleCommands.length; i++) {
            JRadioButtonMenuItem bondsStyleItem = new JRadioButtonMenuItem(
                    new DefaultJmolAction(bondsStyleNames[i], data.getViewer(), bondsStyleCommands[i]));
            bondsStyleItem.setSelected(i==2);
            bondsStyleGroup.add(bondsStyleItem);
            bondsStyleMenu.add(bondsStyleItem);
        }
        display.add(bondsStyleMenu);
        bar.add(display);
        //----------------COLORS-----------------------
        JMenu colors = new JMenu("Colors");
        final JCheckBoxMenuItem atomsTranslucency = new JmolToggleMenuItem("Translucent atoms", "color ATOMS translucent", "color ATOMS opaque", data);
        atomsTranslucency.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                data.setAtomsTranslucent(atomsTranslucency.isSelected());
            }
        });
        colors.add(atomsTranslucency);
        final JCheckBoxMenuItem bondsTranslucency = new JmolToggleMenuItem("Translucent bonds", "color BONDS translucent", "color BONDS opaque", data);
        atomsTranslucency.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                data.setBondsTranslucent(bondsTranslucency.isSelected());
            }
        });
        colors.add(bondsTranslucency);
        colors.addSeparator();
        JCheckBoxMenuItem previewColors = new JCheckBoxMenuItem("Preview colors");
        colors.add(previewColors);
        colors.addSeparator();
        colors.add(new JmolColorAction("Background color", previewColors.getModel(), "Background", Color.BLACK, data.getViewer()));
        colors.add(new JmolColorAction("Bounding box color", previewColors.getModel(), "BoundBox", Color.WHITE, data.getViewer()));
        JMenu axesColorsMenu = new JMenu("Axes color");
        axesColorsMenu.add(new JmolColorAction("All", previewColors.getModel(), "Axes", Color.WHITE, data.getViewer()));
        axesColorsMenu.addSeparator();
        axesColorsMenu.add(new JmolColorAction("X axis", previewColors.getModel(), "Axis1", Color.RED, data.getViewer()));
        axesColorsMenu.add(new JmolColorAction("Y axis", previewColors.getModel(), "Axis2", Color.GREEN, data.getViewer()));
        axesColorsMenu.add(new JmolColorAction("Z axis", previewColors.getModel(), "Axis3", Color.BLUE, data.getViewer()));
        colors.add(axesColorsMenu);
        JMenu labelsColorMenu = new JMenu("Labels color");
        final JMenuItem labelsColorItem = new JMenuItem(new JmolColorAction("Set color", previewColors.getModel(), "Labels", Color.WHITE, data.getViewer()));
        labelsColorItem.setEnabled(false);
        final JCheckBoxMenuItem labelsInheritColorItem = new JCheckBoxMenuItem("Inherit");
        labelsInheritColorItem.setSelected(true);
        labelsInheritColorItem.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                labelsColorItem.setEnabled(!labelsInheritColorItem.isSelected());
                if(labelsInheritColorItem.isSelected()){
                    data.getViewer().evalString("color Labels none");
                }
            }
        });
        labelsColorMenu.add(labelsInheritColorItem);
        labelsColorMenu.add(labelsColorItem);
        colors.add(labelsColorMenu);
        bar.add(colors);
        //----------------SPIN-----------------------
        JMenu spin = new JMenu("Spin");
        spin.add(new JmolToggleMenuItem("Active", "spin on", "spin off", data));
        spin.addSeparator();
        int[] spinFrequency = {0, 5, 10, 20, 30, 40, 50};
        //X rate
        JMenu spinXMenu = new JMenu("Set X Rate");
        ButtonGroup spinXGroup = new ButtonGroup();
        for (int i = 0; i < spinFrequency.length; i++) {
            JRadioButtonMenuItem spinXItem = new JRadioButtonMenuItem(
                    new DefaultJmolAction(Integer.toString(spinFrequency[i]), data.getViewer(),
                    MessageFormat.format("set spin X {0}", spinFrequency[i])));
            spinXItem.setSelected(i==0);
            spinXGroup.add(spinXItem);
            spinXMenu.add(spinXItem);
        }
        spin.add(spinXMenu);
        //Y rate
        JMenu spinYMenu = new JMenu("Set Y Rate");
        ButtonGroup spinYGroup = new ButtonGroup();
        for (int i = 0; i < spinFrequency.length; i++) {
            JRadioButtonMenuItem spinYItem = new JRadioButtonMenuItem(
                    new DefaultJmolAction(Integer.toString(spinFrequency[i]), data.getViewer(),
                    MessageFormat.format("set spin Y {0}", spinFrequency[i])));
            spinYItem.setSelected(i==2);
            spinYGroup.add(spinYItem);
            spinYMenu.add(spinYItem);
        }
        spin.add(spinYMenu);
        //Z rate
        JMenu spinZMenu = new JMenu("Set Z Rate");
        ButtonGroup spinZGroup = new ButtonGroup();
        for (int i = 0; i < spinFrequency.length; i++) {
            JRadioButtonMenuItem spinZItem = new JRadioButtonMenuItem(
                    new DefaultJmolAction(Integer.toString(spinFrequency[i]), data.getViewer(),
                    MessageFormat.format("set spin Z {0}", spinFrequency[i])));
            spinZItem.setSelected(i==0);
            spinZGroup.add(spinZItem);
            spinZMenu.add(spinZItem);
        }
        spin.add(spinZMenu);
        spin.addSeparator();
        //FPS rate
        JMenu spinFpsMenu = new JMenu("Set FPS");
        ButtonGroup spinFpsGroup = new ButtonGroup();
        for (int i = 0; i < spinFrequency.length; i++) {
            JRadioButtonMenuItem spinFpsItem = new JRadioButtonMenuItem(
                    new DefaultJmolAction(Integer.toString(spinFrequency[i]), data.getViewer(),
                    MessageFormat.format("set spin Fps {0}", spinFrequency[i])));
            spinFpsItem.setSelected(i==3);
            spinFpsGroup.add(spinFpsItem);
            spinFpsMenu.add(spinFpsItem);
        }
        spin.add(spinFpsMenu);
        bar.add(spin);
        //----------------ZOOM-----------------------
        JMenu zoom = new JMenu("Zoom");
        int[] zoomLevels = {25, 50, 100, 150, 200, 400, 800};
        for (int i = 0; i < zoomLevels.length; i++) {
            zoom.add(new ZoomAction(data.getViewer(), zoomLevels[i]));
        }
        zoom.addSeparator();
        zoom.add(new DefaultJmolAction("Zoom in",data.getViewer(),"move 0 0 0 40 0 0 0 0 1"));
        zoom.add(new DefaultJmolAction("Zoom out",data.getViewer(),"move 0 0 0 -40 0 0 0 0 1"));
        bar.add(zoom);
        //----------------ABOUT-----------------------
        JMenu about = new JMenu("About");
        about.add(new ShowWindowAction("About Jmol", new ToolWindow(new AboutJmolPanel(data.getViewer()), target)));
        bar.add(about);
        return bar;
    }

}
