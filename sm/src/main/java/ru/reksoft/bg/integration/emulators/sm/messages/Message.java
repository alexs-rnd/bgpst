package ru.reksoft.bg.integration.emulators.sm.messages;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by chaplygin on 06.10.2016.
 */

@XmlRootElement
public class Message
{
    private Integer cardKey;
    private Integer floor;
    private String street;
    private String houseNumber;
    private String corps;
    private String alarmName;
    private String objectName;
    private String fraction;
    private Integer porch;
    private String porchNumber;
    private String flatNumber;
    private String phone;
    private Integer evCode;
    private Integer evAddCode;
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getCardKey() {
        return cardKey;
    }

    @XmlElement
    public void setCardKey(Integer cardKey) {
        this.cardKey = cardKey;
    }

    public Integer getFloor() {
        return floor;
    }

    @XmlElement
    public void setFloor(Integer floor) {
        this.floor = floor;
    }

    public String getStreet() {
        return street;
    }

    @XmlElement
    public void setStreet(String street) {
        this.street = street;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    @XmlElement
    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getCorps() {
        return corps;
    }

    @XmlElement
    public void setCorps(String corps) {
        this.corps = corps;
    }

    public String getAlarmName() {
        return alarmName;
    }

    @XmlElement
    public void setAlarmName(String alarmName) {
        this.alarmName = alarmName;
    }

    public String getObjectName() {
        return objectName;
    }

    @XmlElement
    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public String getFraction() {
        return fraction;
    }

    @XmlElement
    public void setFraction(String fraction) {
        this.fraction = fraction;
    }

    public Integer getPorch() {
        return porch;
    }

    @XmlElement
    public void setPorch(Integer porch) {
        this.porch = porch;
    }

    public String getPorchNumber() {
        return porchNumber;
    }

    @XmlElement
    public void setPorchNumber(String porchNumber) {
        this.porchNumber = porchNumber;
    }

    public String getFlatNumber() {
        return flatNumber;
    }

    @XmlElement
    public void setFlatNumber(String flatNumber) {
        this.flatNumber = flatNumber;
    }

    public String getPhone() {
        return phone;
    }

    @XmlElement
    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getEvCode() {
        return evCode;
    }

    @XmlElement
    public void setEvCode(Integer evCode) {
        this.evCode = evCode;
    }

    public Integer getEvAddCode() {
        return evAddCode;
    }

    @XmlElement
    public void setEvAddCode(Integer evAddCode) {
        this.evAddCode = evAddCode;
    }

}
