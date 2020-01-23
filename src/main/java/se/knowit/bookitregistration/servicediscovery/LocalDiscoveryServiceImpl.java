package se.knowit.bookitregistration.servicediscovery;

import org.springframework.core.env.Environment;

public class LocalDiscoveryServiceImpl implements DiscoveryService {

    private final Environment environment;
    private final static String prefix = "discovery.service.";

    public LocalDiscoveryServiceImpl(Environment environment) {
        this.environment = environment;
    }

    @Override
    public DiscoveryServiceResult discoverInstances(String namespaceName, String serviceName) {
        DiscoveryServiceResult result = new DiscoveryServiceResult();
        String address = environment.getProperty(prefix + serviceName);

        if(address != null) {
            Instance instance = new Instance();
            String[] ipPort = address.split(":");
            instance.setInstanceIpv4(ipPort[0]);
            instance.setInstancePort(ipPort[1]);
            result.addInstance(instance);
        }
        return result;
    }

}

