package pl.com.app.parsers.json;

import pl.com.app.dto.PackageDealDTO;

import java.util.List;


public class DealsConverter extends JsonConverter<List<PackageDealDTO>> {
    public DealsConverter(String jsonFilename) {
        super(jsonFilename);
    }
}
