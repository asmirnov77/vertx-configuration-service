package io.vertx.ext.configuration.impl.spi;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.configuration.spi.ConfigurationStore;
import io.vertx.ext.configuration.spi.ConfigurationStoreFactory;

/**
 * An implementation of configuration store loading the content from the system properties.
 * <p>
 * As this configuration store is a singleton, the factory returns always the same instance.
 *
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class SystemPropertiesConfigurationStore implements ConfigurationStore, ConfigurationStoreFactory {
  private boolean cache;
  private JsonObject cached;

  @Override
  public String name() {
    return "sys";
  }

  @Override
  public ConfigurationStore create(Vertx vertx, JsonObject configuration) {
    cache = configuration.getBoolean("cache", true);
    return this;
  }

  @Override
  public void get(Handler<AsyncResult<Buffer>> completionHandler) {
    if (!cache || cached == null) {
      cached = JsonObjectHelper.from(System.getProperties());
    }
    completionHandler.handle(Future.succeededFuture(Buffer.buffer(cached.encode())));
  }


}
