package fly.spring.common.cdc.listener

import fly.spring.common.cdc.domain.Payload
import fly.spring.common.cdc.properties.EmbeddedDebeziumChangeListenerProperties
import fly.spring.common.pojo.IdEntity

class Test1Listener(filePath: String) : AbstractEmbeddedDebeziumChangeListener<IdEntity>(
    EmbeddedDebeziumChangeListenerProperties()
        .setSlotName("test1")
        .addTable("test1")
        .setDebeziumProp("connector.class", "io.debezium.connector.postgresql.PostgresConnector")
        .setDebeziumProp("plugin.name", "pgoutput")
        .setDebeziumProp("database.hostname", "127.0.0.1")
        .setDebeziumProp("database.user", "postgres")
        .setDebeziumProp("database.password", "zdh")
        .setDebeziumProp("database.dbname", "database_demo")
        .setDebeziumProp("snapshot.mode", "never")
        .setDebeziumProp("offset.storage.file.filename", "debezium/$filePath/offsets.dat")
) {

    override fun handleEntityBatch(payloads: List<Payload<IdEntity>>) {
        IdEntity()
        println(payloads)
    }
}
