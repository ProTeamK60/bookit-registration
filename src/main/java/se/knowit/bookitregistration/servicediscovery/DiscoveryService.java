package se.knowit.bookitregistration.servicediscovery;

public interface DiscoveryService {
    DiscoveryServiceResult discoverInstances(String namespaceName, String serviceName);
}
