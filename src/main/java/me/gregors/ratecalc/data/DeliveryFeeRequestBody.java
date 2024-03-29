package me.gregors.ratecalc.data;

/**
 * Object that represents POST method JSON content
 * @param city city where delivery is made
 * @param vehicle vehicle what is used to make delivery
 * @param atTimestamp [OPTIONAL VALUE] if specified calculations will be made with that time.
 */
public record DeliveryFeeRequestBody(String city, String vehicle, Long atTimestamp) {}
