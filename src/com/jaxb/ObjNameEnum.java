//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.03.20 at 02:27:11 PM IST 
//


package com.jaxb;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for objNameEnum.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="objNameEnum">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="User"/>
 *     &lt;enumeration value="Note"/>
 *     &lt;enumeration value="Group"/>
 *     &lt;enumeration value="Account"/>
 *     &lt;enumeration value="FeedItem"/>
 *     &lt;enumeration value="FeedComment"/>
 *     &lt;enumeration value="Attachment"/>
 *     &lt;enumeration value="GroupMember"/>
 *     &lt;enumeration value="Document"/>
 *     &lt;enumeration value="Opportunity"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "objNameEnum")
@XmlEnum
public enum ObjNameEnum {

    @XmlEnumValue("User")
    USER("User"),
    @XmlEnumValue("Note")
    NOTE("Note"),
    @XmlEnumValue("Group")
    GROUP("Group"),
    @XmlEnumValue("Account")
    ACCOUNT("Account"),
    @XmlEnumValue("FeedItem")
    FEED_ITEM("FeedItem"),
    @XmlEnumValue("FeedComment")
    FEED_COMMENT("FeedComment"),
    @XmlEnumValue("Attachment")
    ATTACHMENT("Attachment"),
    @XmlEnumValue("GroupMember")
    GROUP_MEMBER("GroupMember"),
    @XmlEnumValue("Document")
    DOCUMENT("Document"),
    @XmlEnumValue("Opportunity")
    OPPORTUNITY("Opportunity");
    private final String value;

    ObjNameEnum(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ObjNameEnum fromValue(String v) {
        for (ObjNameEnum c: ObjNameEnum.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
