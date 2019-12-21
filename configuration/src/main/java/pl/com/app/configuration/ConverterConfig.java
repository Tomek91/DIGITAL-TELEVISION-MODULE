package pl.com.app.configuration;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import pl.com.app.parsers.FileNames;
import pl.com.app.parsers.json.*;

@Configuration
public class ConverterConfig {

    @Bean
    public AgreementDurationConverter agreementDurationConverter() {
        return new AgreementDurationConverter(FileNames.DURATION_AGREEMENT);
    }

    @Bean
    public TvPackagesConverter tvPackagesConverter() {
        return new TvPackagesConverter(FileNames.TV_PACKAGES);
    }

    @Bean
    public UsersConverter usersConverter() {
        return new UsersConverter(FileNames.USERS);
    }

    @Bean
    public RolesConverter rolesConverter() {
        return new RolesConverter(FileNames.ROLES);
    }

    @Bean
    public AgreementsConverter agreementsConverter() {
        return new AgreementsConverter(FileNames.AGREEMENT);
    }

    @Bean
    public DealsConverter dealsConverter() {
        return new DealsConverter(FileNames.DEALS);
    }

    @Bean
    public InvoicesConverter invoicesConverter() {
        return new InvoicesConverter(FileNames.INVOICES);
    }


    @Bean
    public PreferencesConverter preferencesConverter() {
        return new PreferencesConverter(FileNames.PREFERENCES);
    }


}
