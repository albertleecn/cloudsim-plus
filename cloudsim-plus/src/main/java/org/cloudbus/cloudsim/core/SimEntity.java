package org.cloudbus.cloudsim.core;

import org.cloudbus.cloudsim.core.predicates.Predicate;

/**
 * An interface that represents a simulation entity. An entity handles events and can
 * send events to other entities.
 *
 * @author Manoel Campos da Silva Filho
 * @see CloudSimEntity
 */
public interface SimEntity extends Nameable, Cloneable, Runnable {
    /**
     * The Constant RUNNABLE.
     */
    int RUNNABLE = 0;
    /**
     * The Constant WAITING.
     */
    int WAITING = 1;
    /**
     * The Constant HOLDING.
     */
    int HOLDING = 2;
    /**
     * The Constant FINISHED.
     */
    int FINISHED = 3;

    /**
     * Gets the CloudSim instance that represents the simulation the Entity is related to.
     * @return 
     */
    Simulation getSimulation();

    /**
     * Sets the CloudSim instance that represents the simulation the Entity is related to.
     * @param simulation The CloudSim instance that represents the simulation the Entity is related to
     * @return 
     */
    SimEntity setSimulation(Simulation simulation);

    /**
     * Processes events or services that are available for the entity. This
     * method is invoked by the {@link CloudSim} class whenever there is an
     * event in the deferred queue, which needs to be processed by the entity.
     *
     * @param ev information about the event just happened
     *
     * @pre ev != null
     * @post $none
     */
    void processEvent(SimEvent ev);

    /**
     * The run loop to process events fired during the simulation. The events
     * that will be processed are defined in the
     * {@link #processEvent(SimEvent)} method.
     *
     * @see #processEvent(SimEvent)
     */
    @Override void run();

    /**
     * This method is invoked by the {@link CloudSim} class when the simulation
     * is started. It should be responsible for starting the entity up.
     */
    void startEntity();

    /**
     * Shuts down the entity. This method is invoked by the {@link CloudSim}
     * before the simulation finishes. If you want to save data in log files
     * this is the method in which the corresponding code would be placed.
     */
    void shutdownEntity();
    
    /**
     * Sets the Entity name.
     *
     * @param newName the new name
     * @return 
     * @throws IllegalArgumentException when the entity name is <tt>null</tt> or empty
     */
    SimEntity setName(String newName) throws IllegalArgumentException;    

    /**
     * An attribute that implements the Null Object Design Pattern for {@link SimEntity}
     * objects.
     */
    final SimEntity NULL = new SimEntity() {
        @Override public Simulation getSimulation() { return Simulation.NULL; }
        @Override public SimEntity setSimulation(Simulation simulation) { return this; }
        @Override public void processEvent(SimEvent ev) {}
        @Override public void run() {}
        @Override public void startEntity() {}
        @Override public void shutdownEntity() {}
        @Override public SimEntity setName(String newName) throws IllegalArgumentException { return this; }
        @Override public String getName() { return ""; }
        @Override public int getId() { return 0; }
    };
}
