package pl.com.app.parsers.json;

import pl.com.app.dto.InvoiceDTO;

import java.util.List;


public class InvoicesConverter extends JsonConverter<List<InvoiceDTO>> {
    public InvoicesConverter(String jsonFilename) {
        super(jsonFilename);
    }
}
