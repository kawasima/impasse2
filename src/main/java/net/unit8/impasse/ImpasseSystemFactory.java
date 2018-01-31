package net.unit8.impasse;

import enkan.Env;
import enkan.collection.OptionMap;
import enkan.component.ApplicationComponent;
import enkan.component.doma2.DomaProvider;
import enkan.component.flyway.FlywayMigration;
import enkan.component.hikaricp.HikariCPComponent;
import enkan.component.jackson.JacksonBeansConverter;
import enkan.component.jetty.JettyComponent;
import enkan.config.EnkanSystemFactory;
import enkan.system.EnkanSystem;
import org.seasar.doma.jdbc.Naming;
import org.seasar.doma.jdbc.dialect.SqliteDialect;

import static enkan.component.ComponentRelationship.*;
import static enkan.util.BeanBuilder.*;

public class ImpasseSystemFactory implements EnkanSystemFactory {
    @Override
    public EnkanSystem create() {
        return EnkanSystem.of(
                "doma", builder(new DomaProvider())
                        .set(DomaProvider::setDialect, new SqliteDialect())
                        .set(DomaProvider::setNaming, Naming.SNAKE_LOWER_CASE)
                        .build(),
                "jackson", new JacksonBeansConverter(),
                "flyway", new FlywayMigration(),
                "datasource", new HikariCPComponent(OptionMap.of(
                        "uri", Env.getString("JDBC_URL", "jdbc:sqlite:impasse.db")
                )),
                "app", new ApplicationComponent("net.unit8.impasse.ImpasseApplicationFactory"),
                "http", builder(new JettyComponent())
                        .set(JettyComponent::setPort, 3000)
                        .build()

        ).relationships(
                component("http").using("app"),
                component("app").using(
                        "datasource", "doma", "jackson"),
                component("doma").using("datasource", "flyway"),
                component("flyway").using("datasource")
        );
    }
}
