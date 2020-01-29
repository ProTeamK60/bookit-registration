package se.knowit.bookitregistration.servicediscovery;

import com.amazonaws.services.servicediscovery.AWSServiceDiscovery;
import com.amazonaws.services.servicediscovery.model.DiscoverInstancesRequest;
import com.amazonaws.services.servicediscovery.model.DiscoverInstancesResult;

public class AwsDiscoveryServiceImpl implements DiscoveryService {

    private final AWSServiceDiscovery serviceDiscoverClient;

    public AwsDiscoveryServiceImpl(AWSServiceDiscovery serviceDiscoveryClient) {
        this.serviceDiscoverClient = serviceDiscoveryClient;
    }

    @Override
    public DiscoveryServiceResult discoverInstances(String namespaceName, String serviceName) {
        DiscoveryServiceResult result = new DiscoveryServiceResult();
        DiscoverInstancesResult discoverInstancesResult = serviceDiscoverClient.discoverInstances(
                new DiscoverInstancesRequest().withNamespaceName(namespaceName).withServiceName(serviceName));

        if (!discoverInstancesResult.getInstances().isEmpty()) {
            discoverInstancesResult.getInstances().forEach(instanceSummary -> {
                Instance instance = new Instance();
                instance.setInstanceIpv4(instanceSummary.getAttributes().get("AWS_INSTANCE_IPV4"));
                instance.setInstancePort(instanceSummary.getAttributes().get("AWS_INSTANCE_PORT"));
                instance.setRegion(instanceSummary.getAttributes().get("REGION"));
                result.addInstance(instance);
            });
        }

        return result;
    }
}
