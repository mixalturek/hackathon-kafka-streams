package com.github.mixalturek.hackhathon.streams.serde;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class AfterDeserializer extends JsonDeserializer<After> {
    @Override
    public After deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String text = p.getText();
        ObjectCodec codec = p.getCodec();
        JsonParser nestedParser = codec.getFactory().createParser(text);
        return codec.readValue(nestedParser, After.class);
    }
}
