package org.example.utils;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.example.payloads.request.PostRequestPayload;
import org.example.payloads.request.UserRequestPayload;

public final class XMLUtils {

    private static final XmlMapper XML_MAPPER = new XmlMapper();

    private XMLUtils() {}

    public static String userToXml(UserRequestPayload user) {
        try {
            return XML_MAPPER.writeValueAsString(user);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String postToXml(PostRequestPayload post) {
        try {
            return XML_MAPPER.writeValueAsString(post);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
