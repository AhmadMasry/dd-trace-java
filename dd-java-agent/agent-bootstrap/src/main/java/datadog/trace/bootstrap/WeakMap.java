package datadog.trace.bootstrap;

import datadog.trace.api.Function;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import lombok.extern.slf4j.Slf4j;

public interface WeakMap<K, V> {

  int size();

  boolean containsKey(K target);

  V get(K key);

  void put(K key, V value);

  void putIfAbsent(K key, V value);

  V computeIfAbsent(K key, Function<? super K, ? extends V> supplier);

  class Provider {
    private static volatile Implementation PROVIDER = Implementation.DEFAULT;

    public static synchronized void registerIfAbsent(final Implementation provider) {
      if (provider != null && provider != Implementation.DEFAULT) {
        PROVIDER = provider;
      }
    }

    public static boolean isProviderRegistered() {
      return PROVIDER != Implementation.DEFAULT;
    }

    public static <K, V> WeakMap<K, V> newWeakMap() {
      return PROVIDER.get();
    }
  }

  interface Implementation {
    <K, V> WeakMap<K, V> get();

    Implementation DEFAULT = new Default();

    @Slf4j
    class Default implements Implementation {

      @Override
      public <K, V> WeakMap<K, V> get() {
        log.debug("WeakMap.Supplier not registered. Returning a synchronized WeakHashMap.");
        return new MapAdapter<>(Collections.synchronizedMap(new WeakHashMap<K, V>()));
      }
    }
  }

  class MapAdapter<K, V> implements WeakMap<K, V> {

    private final Map<K, V> map;

    public MapAdapter(final Map<K, V> map) {
      this.map = map;
    }

    @Override
    public int size() {
      return map.size();
    }

    @Override
    public boolean containsKey(final K key) {
      return map.containsKey(key);
    }

    @Override
    public V get(final K key) {
      return map.get(key);
    }

    @Override
    public void put(final K key, final V value) {
      map.put(key, value);
    }

    @Override
    public void putIfAbsent(final K key, final V value) {
      // We can't use putIfAbsent since it was added in 1.8.
      // As a result, we must use double check locking.
      if (!map.containsKey(key)) {
        synchronized (this) {
          if (!map.containsKey(key)) {
            map.put(key, value);
          }
        }
      }
    }

    @Override
    public V computeIfAbsent(final K key, final Function<? super K, ? extends V> supplier) {
      // We can't use computeIfAbsent since it was added in 1.8.
      V value = map.get(key);
      if (null == value) {
        synchronized (this) {
          value = map.get(key);
          if (null == value) {
            value = supplier.apply(key);
            map.put(key, value);
          }
        }
      }
      return value;
    }

    @Override
    public String toString() {
      return map.toString();
    }
  }
}
