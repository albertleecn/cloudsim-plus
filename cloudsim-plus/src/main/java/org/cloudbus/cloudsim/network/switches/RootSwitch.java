/*
 * Title:        CloudSim Toolkit
 * Description:  CloudSim (Cloud Simulation) Toolkit for Modeling and Simulation of Clouds
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2009-2012, The University of Melbourne, Australia
 */
package org.cloudbus.cloudsim.network.switches;

import org.cloudbus.cloudsim.network.HostPacket;
import org.cloudbus.cloudsim.util.Log;

import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.core.events.SimEvent;
import org.cloudbus.cloudsim.datacenters.network.NetworkDatacenter;
import org.cloudbus.cloudsim.vms.Vm;

/**
 * This class allows to simulate Root switch which connects Datacenters to
 * external network. It interacts with other sws in order to exchange
 * packets.
 *
 * <p>Please refer to following publication for more details:
 * <ul>
 * <li>
 * <a href="http://dx.doi.org/10.1109/UCC.2011.24">
 * Saurabh Kumar Garg and Rajkumar Buyya, NetworkCloudSim: Modelling Parallel
 * Applications in Cloud Simulations, Proceedings of the 4th IEEE/ACM
 * International Conference on Utility and Cloud Computing (UCC 2011, IEEE CS
 * Press, USA), Melbourne, Australia, December 5-7, 2011.
 * </a>
 * </ul>
 * </p>
 *
 * @author Saurabh Kumar Garg
 * @author Manoel Campos da Silva Filho
 *
 * @since CloudSim Toolkit 3.0
 */
public class RootSwitch extends AbstractSwitch {
    /**
     * The level (layer) of the switch in the network topology.
     */
    public static final int LEVEL = 0;

    /**
     * Default number of root switch ports that defines the number of
     * {@link AggregateSwitch} that can be connected to it.
     */
    public static final int PORTS = 1;

    /**
     * Default switching delay in milliseconds.
     */
    public static final double SWITCHING_DELAY = 0.00285;

    /**
     * The downlink bandwidth of RootSwitch in Megabits/s.
     * It also represents the uplink bandwidth of connected aggregation sws.
     */
    public static final long DOWNLINK_BW = 40 * 1024 * 1024 * 1024L; // 40000 Megabits (40 Gigabits)

    /**
     * Instantiates a Root AbstractSwitch specifying what other sws are connected
     * to its downlink ports, and corresponding bandwidths.
     *
     * @param simulation The CloudSim instance that represents the simulation the Entity is related to
     * @param dc The Datacenter where the switch is connected to
     */
    public RootSwitch(CloudSim simulation, NetworkDatacenter dc) {
        super(simulation, dc);
        setDownlinkBandwidth(DOWNLINK_BW);
        setSwitchingDelay(SWITCHING_DELAY);
        setPorts(PORTS);
    }

    @Override
    protected void processPacketUp(SimEvent ev) {
        super.processPacketUp(ev);

        HostPacket netPkt = (HostPacket) ev.getData();
        Vm receiverVm = netPkt.getVmPacket().getDestination();
        Switch edgeSwitch = getDatacenter().getVmToSwitchMap().get(receiverVm);
        Switch aggSwitch = findAggregateSwitchConnectedToGivenEdgeSwitch(edgeSwitch);

        if (aggSwitch == Switch.NULL) {
            Log.printLine("No destination switch for this packet");
        } else {
            addPacketToBeSentToDownlinkSwitch(aggSwitch, netPkt);
        }
    }

    /**
     * Finds which aggregate switch is connected to a given edge switch
     * @param edgeSwitch the id of the edge switch to find the aggregate switch
     * that it is connected to
     * @return the id of the aggregate switch that is connected to the given
     * edge switch or {@link Switch#NULL} if not found.
     */
    private Switch findAggregateSwitchConnectedToGivenEdgeSwitch(Switch edgeSwitch) {
        for (Switch aggregateSw : getDownlinkSwitches()) {
            for (Switch edgeSw : aggregateSw.getDownlinkSwitches()) {
                if (edgeSw.getId() == edgeSwitch.getId()) {
                    return aggregateSw;
                }
            }
        }

        return Switch.NULL;
    }

    @Override
    public int getLevel() {
        return LEVEL;
    }
}
