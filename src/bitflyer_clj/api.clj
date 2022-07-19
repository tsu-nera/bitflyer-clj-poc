(ns bitflyer-clj.api
  (:require
   [camel-snake-kebab.core :as csk]
   [camel-snake-kebab.extras :as cske]
   [clj-http.client :as client]))

(def product-code "FX_BTC_JPY")
(def base-params {"product_code" product-code})

(def base-url "https://api.bitflyer.com/v1/")
(defn ->url [end-point]
  (str base-url end-point))

(defn public [url]
  (when-let [resp (client/get url
                              {:as            :json
                               :query-params  base-params
                               :cookie-policy :standard})]
    (->> resp
         :body
         (cske/transform-keys csk/->kebab-case))))

(defn fetch-markets
  []
  (public (->url "markets")))
#_(fetch-markets)

(defn fetch-order-book
  []
  (public (->url "board")))
#_(fetch-order-book)

(defn fetch-ticker
  []
  (public (->url "ticker")))
#_(fetch-ticker)

(comment
  ;;
  (dotimes [_ 3]
    (println (fetch-ticker))
    (Thread/sleep 1000))
  ;;
  )
