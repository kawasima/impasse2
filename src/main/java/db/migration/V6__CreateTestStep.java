package db.migration;

import org.flywaydb.core.api.migration.jdbc.JdbcMigration;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;

import java.sql.Connection;
import java.sql.Statement;

import static org.jooq.impl.DSL.*;

public class V6__CreateTestStep implements JdbcMigration {
    @Override
    public void migrate(Connection connection) throws Exception {
        try(Statement stmt = connection.createStatement()) {
            DSLContext create = DSL.using(connection);
            String ddl = create.createTable(table("test_steps"))
                    .column(field("test_step_id", SQLDataType.BIGINT.identity(true)))
                    .column(field("step_number", SQLDataType.INTEGER.nullable(false)))
                    .column(field("actions", SQLDataType.CLOB.nullable(true)))
                    .column(field("expected_results", SQLDataType.INTEGER.nullable(true)))
                    .constraints(
                            foreignKey(field("test_step_id")).references(table("test_steps"), field("test_step_id"))
                    )
                    .getSQL();
            stmt.execute(ddl);
        }

    }
}
