package pl.com.app.parsers.json;

import pl.com.app.dto.TvPackageDTO;

import java.util.List;


public class TvPackagesConverter extends JsonConverter<List<TvPackageDTO>> {
    public TvPackagesConverter(String jsonFilename) {
        super(jsonFilename);
    }
}
