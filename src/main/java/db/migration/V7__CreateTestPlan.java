package db.migration;

import org.flywaydb.core.api.migration.jdbc.JdbcMigration;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;

import java.sql.Connection;
import java.sql.Statement;

import static org.jooq.impl.DSL.*;

public class V7__CreateTestPlan implements JdbcMigration {
    @Override
    public void migrate(Connection connection) throws Exception {
        try(Statement stmt = connection.createStatement()) {
            DSLContext create = DSL.using(connection);
            String ddl = create.createTable(table("test_plans"))
                    .column(field("test_plan_id", SQLDataType.BIGINT.identity(true)))
                    .column(field("name", SQLDataType.VARCHAR(255).nullable(false)))
                    .column(field("notes", SQLDataType.CLOB.nullable(true)))
                    .column(field("active", SQLDataType.BOOLEAN.nullable(false)))
                    .getSQL();
            stmt.execute(ddl);
        }

        try(Statement stmt = connection.createStatement()) {
            DSLContext create = DSL.using(connection);
            String ddl = create.createTable(table("test_case_plans"))
                    .column(field("test_case_plan_id", SQLDataType.BIGINT.identity(true)))
                    .column(field("test_case_id", SQLDataType.BIGINT.nullable(false)))
                    .column(field("test_plan_id", SQLDataType.BIGINT.nullable(false)))
                    .column(field("node_order", SQLDataType.INTEGER.nullable(false)))
                    .constraints(
                            foreignKey(field("test_case_id")).references(table("test_cases"), field("test_case_id")),
                            foreignKey(field("test_plan_id")).references(table("test_plans"), field("test_plan_id"))
                    )
                    .getSQL();
            stmt.execute(ddl);
        }
    }
}
