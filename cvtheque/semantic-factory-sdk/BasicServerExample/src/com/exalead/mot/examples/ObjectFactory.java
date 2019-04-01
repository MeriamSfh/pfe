package com.exalead.mot.examples;
import javax.xml.bind.*;
import javax.xml.bind.annotation.*;
import javax.xml.namespace.QName;

@XmlRegistry
public class ObjectFactory {
    private final static QName _BasicServerExample_QNAME = new QName("jexa:com.exalead.mot.examples", "BasicServerExample");
    public BasicServerExample createBasicServerExample() {
        return new BasicServerExample();
    }
    @XmlElementDecl(namespace = "jexa:com.exalead.mot.examples", name = "BasicServerExample")
    public JAXBElement<BasicServerExample> createBasicServerExample(BasicServerExample value) {
        return new JAXBElement<BasicServerExample>(_BasicServerExample_QNAME, BasicServerExample.class, null, value);
    }
}
