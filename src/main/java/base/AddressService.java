package base;

import java.util.concurrent.ConcurrentLinkedQueue;


public interface AddressService {

    public Address getAddress(Class<?> abonentClass);

    public void setAddress(Abonent abonent);

    public ConcurrentLinkedQueue<Address> getServiceAddressesQueue(Abonent abonent);

}
