package com.monegoo;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;

import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static java.util.Objects.requireNonNull;

@Path("/")
public class IndexPage {


    @Inject
    EntityManager entityManager;

    private final Template page;

    public IndexPage(Template page) {
        this.page = requireNonNull(page, "page is required");
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance get(@QueryParam("name") String name) {

        List<Currency> resultList = entityManager.createNamedQuery("Currency.findAll", Currency.class)
                .getResultList();

        Currency currency = resultList.get(0);

        String pattern = "MM-dd-yyyy HH:mm";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        Date instant = new Date();
        instant.setTime(currency.getTimestamp() * 1000);
        String date = simpleDateFormat.format(instant);

        return page.data("currency", currency).data("now", date);
    }

}
