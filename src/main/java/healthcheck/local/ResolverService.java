package healthcheck.local;

import healthcheck.dto.SimpleResponse;

import java.util.Locale;

public interface ResolverService {
    public SimpleResponse setLocaleLanguage(Locale locale) ;
}
