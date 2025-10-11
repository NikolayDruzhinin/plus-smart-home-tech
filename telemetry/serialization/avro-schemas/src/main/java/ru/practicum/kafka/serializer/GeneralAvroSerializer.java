package ru.practicum.kafka.serializer;

import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificDatumWriter;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.common.errors.SerializationException;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Component
public class GeneralAvroSerializer {
    private static final EncoderFactory ENCODER_FACTORY = EncoderFactory.get();

    public byte[] serialize(String topic, SpecificRecordBase data) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            if (data == null) return null;
            BinaryEncoder binaryEncoder = ENCODER_FACTORY.binaryEncoder(outputStream, null);
            DatumWriter<SpecificRecordBase> dw = new SpecificDatumWriter<>(data.getSchema());
            dw.write(data, binaryEncoder);
            binaryEncoder.flush();
            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new SerializationException("Error while serializing data for topic [" + topic + "], " + e);
        }
    }
}
