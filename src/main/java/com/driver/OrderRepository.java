package com.driver;

import java.util.*;

import org.springframework.stereotype.Repository;

@Repository
public class OrderRepository {

    private final HashMap<String, Order> orderMap;
    private final HashMap<String, DeliveryPartner> partnerMap;
    private final HashMap<String, HashSet<String>> partnerToOrderMap;
    private final HashMap<String, String> orderToPartnerMap;

    public OrderRepository() {
        this.orderMap = new HashMap<String, Order>();
        this.partnerMap = new HashMap<String, DeliveryPartner>();
        this.partnerToOrderMap = new HashMap<String, HashSet<String>>();
        this.orderToPartnerMap = new HashMap<String, String>();
    }

    public void saveOrder(Order order) {
        orderMap.put(order.getId(), order);
    }

    public void savePartner(String partnerId) {
        partnerMap.put(partnerId, new DeliveryPartner(partnerId));
        // your code here
        // create a new partner with given partnerId and save it
    }

    public void saveOrderPartnerMap(String orderId, String partnerId) {
        if (orderMap.containsKey(orderId) && partnerMap.containsKey(partnerId)) {
            // your code here
            //add order to given partner's order list
            //increase order count of partner
            //assign partner to this order
            orderToPartnerMap.put(orderId, partnerId);
            HashSet<String> orders = partnerToOrderMap.getOrDefault(partnerId, new HashSet<String>());
            orders.add(orderId);
            partnerToOrderMap.put(partnerId, orders);
            updatePartnerOrderCount(partnerId);
        }
    }

    public Order findOrderById(String orderId) {
        return orderMap.get(orderId);
    }

    public DeliveryPartner findPartnerById(String partnerId) {
        return partnerMap.get(partnerId);
    }

    public Integer findOrderCountByPartnerId(String partnerId) {
        return partnerToOrderMap.getOrDefault(partnerId, new HashSet<String>()).size();
    }

    public List<String> findOrdersByPartnerId(String partnerId) {
        return new ArrayList<>(partnerToOrderMap.getOrDefault(partnerId, new HashSet<String>()));
    }

    public List<String> findAllOrders() {
        // your code here
        // return list of all orders
        return new ArrayList<>(orderMap.keySet());
    }

    public void deletePartner(String partnerId) {
        // your code here
        // delete partner by ID
        partnerMap.remove(partnerId);
        partnerToOrderMap.remove(partnerId);
        orderToPartnerMap.values().removeIf(partnerId::equals);
    }

    public void deleteOrder(String orderId) {
        // your code here
        // delete order by ID
        orderMap.remove(orderId);
        for(String orderId1 : partnerToOrderMap.keySet()){
            HashSet<String> orders = partnerToOrderMap.get(orderId1););
              if(orders.contains(orderId)){
                  orders.remove(orderId);
                  partnerToOrderMap.put(orderId1, orders);
              }
        }
        orderToPartnerMap.remove(orderId);
    }

    private void updatePartnerOrderCount(String partnerId){
        DeliveryPartner deliveryPartner = partnerMap.get(partnerId);
        deliveryPartner.setNumberOfOrders(partnerToOrderMap.get(partnerId).size());
        partnerMap.put(partnerId, deliveryPartner);
    }

    public Integer findCountOfUnassignedOrders() {
        Integer count = 0;
        for (String orderId : orderMap.keySet()) {
            if (!orderToPartnerMap.containsKey(orderId)) {
                count++;
            }
        }
        return count;
    }

    public Integer findOrdersLeftAfterGivenTimeByPartnerId(String timeString, String partnerId) {
        HashSet<String> orders = partnerToOrderMap.getOrDefault(partnerId, new HashSet<String>());
        Integer count = 0;
        for (String orderId : orders) {
            Order order = orderMap.get(orderId);
            if (null != order && order.getDeliveryTime() > Utils.convertTimeToMinutes(timeString)) {
                count++;
            }
        }
        return count;
    }

    public String findLastDeliveryTimeByPartnerId(String partnerId) {
        String result = "";
        HashSet<String> orders = partnerToOrderMap.getOrDefault(partnerId, new HashSet<String>());
        if (!orders.isEmpty()) {
            Integer maxTime = -1;
            for (String orderId : orders) {
                Order order = orderMap.get(orderId);
                if (null != order && order.getDeliveryTime() > maxTime) {
                    maxTime = order.getDeliveryTime();
                }
            }
            result = result + (maxTime / 60) + ":" + (maxTime % 60);
        }
        return result;
    }
}