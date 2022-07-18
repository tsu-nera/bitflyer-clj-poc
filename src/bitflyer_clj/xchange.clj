(ns bitflyer-clj.xchange
  (:import
    (org.knowm.xchange
      ExchangeFactory)
    (org.knowm.xchange.bitflyer
      BitflyerExchange)))


(defn create-exchange
  [instance]
  (let [ex      instance
        ex-spec (.getDefaultExchangeSpecification ex)]
    (.createExchange (ExchangeFactory/INSTANCE) ex-spec)))


(comment
  (def exchange (create-exchange (BitflyerExchange.)))
  (def service (.getMarketDataService exchange))

  (dotimes [i 5]
    (println (.toString (.getTicker service "BTC_JPY")))
    (Thread/sleep 1000))
  )
