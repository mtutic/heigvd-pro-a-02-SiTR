/*
 * Filename : Simulation.java
 * Creation date : 07.04.2019
 */

package ch.heigvd.sitr.model;

import ch.heigvd.sitr.gui.simulation.Displayer;
import ch.heigvd.sitr.gui.simulation.SimulationWindow;
import ch.heigvd.sitr.vehicle.Vehicle;
import ch.heigvd.sitr.vehicle.VehicleController;
import lombok.Getter;

import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Simulation class handles all global simulation settings and values
 * The main simulation loop runs here as well
 *
 * @author Luc Wachter
 */
public class Simulation {
    // The displayable component we need to repaint
    private Displayer window;
    // List of vehicles generated by traffic generator
    private LinkedList<Vehicle> vehicles = new LinkedList<>();

    // Rate at which the redrawing will happen in milliseconds
    private static final int UPDATE_RATE = 40;

    // the ratio px/m
    @Getter
    private double scale;

    /**
     * Constructor
     *
     * @param scale the ratio px/m
     */
    public Simulation(double scale) {
        this.scale = scale;

        // Manual hard coded tests
        VehicleController vehicleController = new VehicleController("vehicleController/timid.xml");
        VehicleController vehicleController2 = new VehicleController("vehicleController/careful.xml");

        Vehicle v = new Vehicle(vehicleController, 1.7, 33.33, null);
        v.setPosition(120);
        vehicles.add(v);

        Vehicle v2 = new Vehicle(vehicleController2, 1.7, 33.33, null);
        v2.setPosition(0);
        v2.setFrontVehicle(v);
        vehicles.add(v2);

        // Launch main window
        window = SimulationWindow.getInstance();
    }

    /**
     * Main display loop, runs in a fixed rate timer loop
     */
    public void loop() {
        // Schedule a task to run immediately, and then
        // every UPDATE_RATE per second
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                for (Vehicle vehicle : vehicles) {
                    vehicle.update(0.5);
                    vehicle.draw();
                    // DEBUG
                    System.out.println(vehicle);
                }

                // Callback to paintComponent()
                window.repaint();
            }
        }, 0, UPDATE_RATE);
    }

    /**
     * Convert meters per second to kilometers per hour
     *
     * @param mps The amount of m/s to convert
     * @return the corresponding amount of km/h
     */
    public static double mpsToKph(double mps) {
        // m/s => km/h : x * 3.6
        return mps * 3.6;
    }

    /**
     * Convert kilometers per hour to meters per second
     *
     * @param kph The amount of km/h to convert
     * @return the corresponding
     */
    public static double kphToMps(double kph) {
        // km/h => m/s : x / 3.6
        return kph / 3.6;
    }

    /**
     * Convert m to px
     * @param m the number of m
     * @return the number of px
     */
    public int mToPx(double m) {
        return (int)Math.round(m * scale);
    }

    /**
     * Convert px to m
     * @param px the number of px
     * @return the number of px
     */
    public double pxToM(int px) {
        return px / scale;
    }
}
