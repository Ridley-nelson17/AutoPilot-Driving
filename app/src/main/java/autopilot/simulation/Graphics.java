package autopilot.simulation;

import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.Taskbar;

import autopilot.simulation.subsystems.VirtualCar;

@SuppressWarnings("serial")
public class Graphics extends JFrame implements ActionListener, Constants {
    private VirtualCar robot;
    
    private JMenuBar menuBar = new JMenuBar();
    private JMenu optionsMenu = new JMenu("Options"), subMenu = new JMenu("Team Settings"), 
            viewMenu = new JMenu("View");
    private JMenuItem robotPosition = new JMenuItem("Show Robot Position"),
            robotType = new JMenuItem("Select Robot type"), teamNumber = new JMenuItem("Select Team Number"),
            teamName = new JMenuItem("Set Team Name"), showFeild = new JMenuItem("Show Feild"),
            changeRobotSize = new JMenuItem("Change Robot Size"),
            simulationGridView = new JMenuItem("Simulation Grid View");

    private JLabel teamNumberLabel, robotTypeLabel, robotPositionLabel;
    
    public Graphics(VirtualCar inputRobot) {
        initUI(inputRobot, false);
    }

    public Graphics(VirtualCar inputRobot, boolean useKeyboard) {
        initUI(inputRobot, useKeyboard);
        robot = inputRobot;
    }

    private void initUI(VirtualCar inputRobot, boolean useKeyboard) {
        add(new SimulationViewDisplay(inputRobot, useKeyboard)); // add the main display
        setTitle("Autopilot Simulation Interactive Window");
        Taskbar taskbar = Taskbar.getTaskbar(); // set the task bar to the system's task bar
        try {taskbar.setIconImage(new ImageIcon(getClass().getResource("drivetrain-img-dict/SimulationGUI.png")).getImage());
        } catch (final UnsupportedOperationException e) {} catch (final SecurityException e) {}
        setIconImage(new ImageIcon(getClass().getResource("drivetrain-img-dict/SimulationGUI.png")).getImage()); // set the icon image
        setSize(900, 600); // set the size of the display
        createMenuItems(); // create the menu items
        setJMenuBar(menuBar); // set the menu bar
        setLocationRelativeTo(null);
        setBackground(Color.WHITE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void createMenuItems() {
        teamNumberLabel = new JLabel("     Team Number: 0000");
        robotTypeLabel = new JLabel("     Drivetrain: differential");
        robotPositionLabel = new JLabel("     X: 0, Y: 0, Z: 0");

        // View
        viewMenu.add(simulationGridView);

        optionsMenu.add(robotPosition); optionsMenu.add(changeRobotSize); optionsMenu.add(robotType); optionsMenu.add(showFeild); optionsMenu.add(subMenu);
        robotPosition.setMnemonic(KeyEvent.VK_R);
        subMenu.add(teamName); subMenu.add(teamNumber); subMenu.add(teamNumberLabel); subMenu.add(robotTypeLabel); subMenu.add(robotPositionLabel);
        menuBar.add(optionsMenu); menuBar.add(viewMenu);

        addMenuListeners();
    }

    /**
     * Autonomous way to add listeners to every menu object in the menubar.
     */
    private void addMenuListeners() {
        for (int i = 0; i < menuBar.getMenuCount(); i++) {
            JMenu m = menuBar.getMenu(i);
            if (testMode) System.out.println("Menu:" + m.getText());
            for (int j = 0; j < m.getMenuComponentCount(); j++) {
                java.awt.Component comp = m.getMenuComponent(j);
                if (comp instanceof JMenu) {
                    JMenu tempMenu = (JMenu)comp;
                    if (testMode) System.out.println("Menu: " + tempMenu.getText());
                    
                    for (int j2 = 0; j2 < tempMenu.getMenuComponentCount(); j2++) {
                        java.awt.Component comp2 = tempMenu.getMenuComponent(j2);
                        if (comp2 instanceof JMenu) {
                            JMenu tempMenu2 = (JMenu)comp2;
                            if (testMode) System.out.println("Menu: " + tempMenu2.getText());
                        } else if (comp2 instanceof JMenuItem) {
                            JMenuItem menuItem2 = (JMenuItem)comp2;
                            menuItem2.addActionListener(this);
                            if (testMode) System.out.println("MenuItem: " + menuItem2.getText());
                        }
                    }
                } else if (comp instanceof JMenuItem) {
                    JMenuItem menuItem = (JMenuItem)comp;
                    menuItem.addActionListener(this);
                    if (testMode) System.out.println("MenuItem: " + menuItem.getText());
                }
            }
        }
    }

    /**
     * A hacky while loop of java GUI getting input from the user.
     * @param parent - The JFrame parent element in which to show everything in.
     * @param message - The message to display.
     * @param toBeValue - An {@code int} that is used to compare with the result length.
     * @return the user input, should be 4 digits (numbers).
     */
    private String showTeamNumberInput(JFrame parent, String message, Integer toBeValue) {
        String input = (String)JOptionPane.showInputDialog(parent, message, null);
        if (input.length() != toBeValue) showTeamNumberInput(parent, message, toBeValue);
        return input;
    }

    /**
     * Displays a dropdown with availible drivetrains supported by the simulator.
     * @param parent - The JFrame parent element in which to show everything in.
     * @param message - The message to display.
     */
    private void showDriveChangeInput(JFrame parent, String message) {
        JFrame frame = new JFrame("Drivetrain Changer");
        frame.setResizable(false);
        frame.setSize(250, 170);
        frame.setVisible(true);

        JLabel label = new JLabel("Please select a drive type.");
        label.setSize(400,100);
        label.setBounds(32, 5, 186, 30);

        JButton submit = new JButton("Done");
        submit.setBounds(5, 90, 60, 40);

        String options[] = {"Differential","Omni","Tank","Mecanum"};
        final JComboBox<String> comboBox = new JComboBox<String>(options);
        comboBox.setBounds(5,60,240,25);

        frame.add(submit); frame.add(label); frame.add(comboBox);

        submit.addActionListener(new ActionListener() {  
            public void actionPerformed(ActionEvent e) {       
                String data = (String)comboBox.getItemAt(comboBox.getSelectedIndex());  
                if (data == "Differential") robot.setDrivetrain("d");
                if (data == "Omni") robot.setDrivetrain("o");
                if (data == "Tank") robot.setDrivetrain("t");
                if (data == "Mecanum") robot.setDrivetrain("m");
                
                if (SimulationOS.getOs() == "mac") robotType.setText("Change Robot Type: " + data);
                else robotTypeLabel.setText("     Drivetrain: " + data);
                frame.dispose();
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == robotPosition) {
            new MotorView(robot).setVisible(true);;
        }
        if (e.getSource() == robotType) {
            final JFrame parent = new JFrame();
            parent.setVisible(false);
            showDriveChangeInput(parent, "What would you like your new drivetrain to be? [d, o, t]");
        }
        if (e.getSource() == teamNumber) {
            final JFrame parent = new JFrame();
            parent.setVisible(false);
            String number = showTeamNumberInput(parent, "What is your 4 digit team number?", 4);
            robot.setTeamNumber(Integer.parseInt(number));
            if (SimulationOS.getOs() == "mac") teamNumber.setText("Change Team Number: " + robot.getTeamNumber());
            else teamNumberLabel.setText("     Team Number: " + robot.getTeamNumber());
        }
        if (e.getSource() == teamName) {

        }
        if (e.getSource() == showFeild) {
        }
    }
}