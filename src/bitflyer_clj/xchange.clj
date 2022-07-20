(ns bitflyer-clj.xchange
  (:require
   [bitflyer-clj.tool :as tool])
  (:import
   (org.knowm.xchange
    ExchangeFactory)
   (org.knowm.xchange.bitflyer
    BitflyerExchange)
   (org.knowm.xchange.bitflyer.service
    BitflyerMarketDataServiceRaw)))

(def product-code "FX_BTC_JPY")

(def creds-file "creds.edn")
(def creds (tool/load-edn creds-file))

(defn create-exchange
  [instance]
  (let [ex      instance
        ex-spec (.getDefaultExchangeSpecification ex)]
    (.setApiKey ex-spec (:api-key creds))
    (.setSecretKey ex-spec (:api-secret creds))
    (.createExchange (ExchangeFactory/INSTANCE) ex-spec)))

(def exchange (create-exchange (BitflyerExchange.)))
(def ^BitflyerMarketDataServiceRaw market
  (.getMarketDataService exchange))
(def account (.getAccountService exchange))

(defn fetch-order-book []
  (.getOrderBook market product-code))

(comment
  (def exchange (create-exchange (BitflyerExchange.)))
  (def market (.getMarketDataService exchange))
  (def order-book (.getOrderbook market product-code))

  (.getBitflyerTIcker market product-code)

  (dotimes [i 5]
    (println (.toJson (.getTicker market product-code)))
    (Thread/sleep 1000))
  )

(comment
  (def exchange (create-exchange (BitflyerExchange.)))
  (def account (.getAccountService exchange))

  (def info (.getAccountInfo account))
  )
