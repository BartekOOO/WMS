package edu.uws.ii.springboot.services;

import edu.uws.ii.springboot.commands.deliveries.GetDeliveriesCommand;
import edu.uws.ii.springboot.interfaces.IDeliveriesService;
import edu.uws.ii.springboot.models.Delivery;
import edu.uws.ii.springboot.repositories.IDeliveriesRepository;
import edu.uws.ii.springboot.specifications.DeliverySpecifications;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeliveriesService implements IDeliveriesService {

    private final IDeliveriesRepository deliveriesRepository;

    public DeliveriesService(IDeliveriesRepository deliveriesRepository) {
        this.deliveriesRepository = deliveriesRepository;
    }


    @Override
    public List<Delivery> getDeliveries(GetDeliveriesCommand command) {
        return deliveriesRepository.findAll(DeliverySpecifications.byFilter(command));
    }
}
