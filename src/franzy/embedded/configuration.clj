(ns franzy.embedded.configuration
  (:require [schema.core :as s]
            [franzy.common.configuration.codec :as config-codec]
            [franzy.common.broker.schema :as bs]
            [franzy.embedded.defaults :as defaults]
            [taoensso.timbre :as timbre])
  (:import (kafka.server KafkaConfig)))

(s/defn ^:always-validate make-kafka-config :- KafkaConfig
  ([] (make-kafka-config (defaults/default-config)))
  ([broker-config] (make-kafka-config broker-config false))
  ([broker-config :- (s/maybe bs/BrokerConfig) do-log?]
    (let [conf (or broker-config (defaults/default-config))]
      ;;outputting broker config because it's easy to forget what your settings are, get confused by env, etc.
      (timbre/info "Creating a Kafka Config with the following values:" conf)
      (-> conf
          (config-codec/encode)
          (KafkaConfig. do-log?)))))

;;TODO: more helpers for loading configs from disk, env, etc.
