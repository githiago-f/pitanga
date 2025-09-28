package br.edu.ifrs.poa.pitanga_code.infra;

import java.util.LinkedHashMap;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.edu.ifrs.poa.pitanga_code.domain.pbl.dto.ScenarioOutput;
import jakarta.persistence.AttributeConverter;

public class ScenarioOutputJsonConverter implements AttributeConverter<ScenarioOutput, String> {
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(ScenarioOutput attribute) {
        try {
            if (attribute == null)
                return null;

            var rootObj = mapper.createObjectNode();
            rootObj.put("input", attribute.getInput());
            rootObj.put("actualOutput", attribute.getActualOutput());
            rootObj.put("expectedOutput", attribute.getExpectedOutput());

            return mapper.writeValueAsString(rootObj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ScenarioOutput convertToEntityAttribute(String dbData) {
        try {
            if (dbData == null)
                return null;
            var obj = mapper.readValue(dbData, LinkedHashMap.class);
            if (obj == null)
                return null;
            return new ScenarioOutput(
                    (String) obj.get("input"),
                    (String) obj.get("expectedOutput"),
                    (String) obj.get("actualOutput"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
