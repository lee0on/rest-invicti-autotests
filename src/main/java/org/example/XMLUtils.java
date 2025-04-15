package org.example;
import org.example.payloads.User;
import org.example.payloads.UserResponse;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.io.StringWriter;

public class XMLUtils {

    public static String userToXml(User user) throws Exception {
        JAXBContext context = JAXBContext.newInstance(User.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

        StringWriter writer = new StringWriter();
        marshaller.marshal(user, writer);
        return writer.toString();
    }

    public static User xmlToUser(String xml) throws Exception {
        JAXBContext context = JAXBContext.newInstance(User.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        return (User) unmarshaller.unmarshal(new StringReader(xml));
    }

    public static UserResponse xmlToUserResponse(String xml) throws Exception {
        JAXBContext context = JAXBContext.newInstance(UserResponse.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        return (UserResponse) unmarshaller.unmarshal(new StringReader(xml));
    }
}
