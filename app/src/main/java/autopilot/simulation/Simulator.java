package autopilot.simulation;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import java.awt.EventQueue;

public class Simulator implements Constants {
    private Graphics simulation;
    private static String definiteOS;

    /**
     * Creates a {@code SimulationOS} and a {@code SimulationGUI} object which can be retreived by {@code getSimulationInstance()}.
     * @param os - The operating system name in which the simulation is going to be on. Current options are {@code windows} or {@code mac}.
     * @since v3
     * @see #getSimulationInstance()
     * @see #getOs()
     */
    public Simulator(String os) {
        definiteOS = os;
        if (os == "mac") {
            initMacOS();
        } else if (os == "windows") {
            initWindows();
        } else {
            initMacOS(); // because it's probably linux
        }
    }

    public Simulator(String os, autopilot.simulation.subsystems.VirtualCar inputBot) {
        definiteOS = os;
        if (os == "mac") {
            initMacOS(inputBot);
        } else if (os == "windows") {
            initWindows(inputBot);
        } else {
            initMacOS(inputBot);
        }
    }

    public Simulator(String os, autopilot.simulation.subsystems.VirtualCar inputBot, boolean useKeyboard) {
        definiteOS = os;
        if (os == "mac") {
            initMacOS(inputBot, useKeyboard);
        } else if (os == "windows") {
            initWindows(inputBot, useKeyboard);
        } else {
            initMacOS(inputBot, useKeyboard);
        }
    }

    public SimulationOS(String os, boolean useKeyboard) {
        autopilot.simulation.subsystems.VirtualCar defaultBot = new autopilot.simulation.subsystems.VirtualCar("Test Robot", 4536, "d");
        defaultBot.setSpeed(5);
        definiteOS = os;
        if (os == "mac") {
            initMacOS(defaultBot, useKeyboard);
        } else if (os == "windows") {
            initWindows(defaultBot, useKeyboard);
        } else {
            initMacOS(defaultBot, useKeyboard);
        }
    }

    /// Windows init functions
    private void initWindows() {
        autopilot.simulation.subsystems.VirtualCar defaultBot = new autopilot.simulation.subsystems.VirtualCar("Test Robot", 4536, "d");
        defaultBot.setSpeed(5);
        initWindows(defaultBot);
    }

    private void initWindows(autopilot.simulation.subsystems.VirtualCar inputBot) {
        initWindows(inputBot, false);
    }

    private void initWindows(autopilot.simulation.subsystems.VirtualCar inputBot, boolean useKeyboard) {
        EventQueue.invokeLater(() -> {
			simulation = new Graphics(inputBot, useKeyboard);
			simulation.setVisible(true);
		});
    }


    private void initMacOS() {
        autopilot.simulation.subsystems.VirtualCar defaultBot = new autopilot.simulation.subsystems.VirtualCar("Test Robot", 4536, "d");
        defaultBot.setSpeed(5);
        initMacOS(defaultBot, false);
    }

    private void initMacOS(autopilot.simulation.subsystems.VirtualCar inputBot) {
        initMacOS(inputBot, false);
    }

    private void initMacOS(autopilot.simulation.subsystems.VirtualCar inputBot, boolean useKeyboard) {
        System.setProperty("apple.laf.useScreenMenuBar", macOSToolBarUsage);
        System.setProperty("apple.awt.application.name", "SimulationGUI");
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        EventQueue.invokeLater(() -> {
			simulation = new Graphics(inputBot, useKeyboard);
			simulation.setVisible(true);
		});
    }

    /**
     * Returns the OS initially set.
     * @return {@code String} the OS set.
     * @since v3
     */
    public static String getOs() {
        return definiteOS;
    }

    /**
     * Returns a SimulationGUI object.
     * @return SimulationGUI
     * @since v3
     */
    public Graphics getSimulationInstance() {
        return simulation;
    }
}