package com.vechain.thorclient.core.model.clients;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.vechain.thorclient.utils.Prefix;

import java.io.IOException;

public class AddressSerializer extends JsonSerializer<Address> {

    @Override
    public void serialize(Address value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        // Serialize the Address as a plain hex string, e.g., "0x..."
        gen.writeString(value.toHexString(Prefix.ZeroLowerX));
    }
}
