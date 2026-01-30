package edu.uws.ii.springboot.config;

import edu.uws.ii.springboot.enums.CustomerTypeEnum;
import edu.uws.ii.springboot.models.Address;
import edu.uws.ii.springboot.models.Customer;
import edu.uws.ii.springboot.repositories.IAddressesRepository;
import edu.uws.ii.springboot.repositories.ICustomersRepository;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;

@Configuration
public class Config {

    @Autowired
    private ICustomersRepository customersRepository;

    @Autowired
    private IAddressesRepository addressesRepository;

    @Bean
    InitializingBean init() {

        return () -> {

        };
    }
}
