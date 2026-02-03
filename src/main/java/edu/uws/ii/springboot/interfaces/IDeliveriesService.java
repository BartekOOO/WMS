package edu.uws.ii.springboot.interfaces;

import edu.uws.ii.springboot.commands.deliveries.GetDeliveriesCommand;
import edu.uws.ii.springboot.models.Delivery;

import java.util.List;

public interface IDeliveriesService {
    List<Delivery> getDeliveries(GetDeliveriesCommand command);
}
